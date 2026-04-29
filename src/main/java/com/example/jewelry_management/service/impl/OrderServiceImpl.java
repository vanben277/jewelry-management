package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.res.MonthRevenueListResponse;
import com.example.jewelry_management.dto.res.MonthlyRevenueResponse;
import com.example.jewelry_management.dto.res.OrderResponse;
import com.example.jewelry_management.dto.res.RevenueReportResponse;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.enums.PaymentMethod;
import com.example.jewelry_management.enums.ProductStatus;
import com.example.jewelry_management.exception.*;
import com.example.jewelry_management.form.*;
import com.example.jewelry_management.mapper.OrderMapper;
import com.example.jewelry_management.model.Account;
import com.example.jewelry_management.model.Order;
import com.example.jewelry_management.model.OrderItem;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.repository.AccountRepository;
import com.example.jewelry_management.repository.OrderRepository;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.OrderSpecification;
import com.example.jewelry_management.service.OrderService;
import com.example.jewelry_management.service.VietQRService;
import com.example.jewelry_management.utils.AccountValidatorUtils;
import com.example.jewelry_management.utils.OrderValidatorUtils;
import com.example.jewelry_management.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final AccountValidator accountValidator;
    private final OrderValidatorUtils validatorUtils;
    private final AccountRepository accountRepository;
    private final AccountValidatorUtils accountValidatorUtils;
    private final VietQRService vietQRService;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequestForm request) {
        Account currentAccount = accountValidator.getCurrentAccount();

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setAccount(currentAccount);
        order.setPaymentMethod(request.getPaymentMethod());

        Set<Integer> productIds = request.getItems().stream()
                .map(OrderItemRequestForm::getProductId)
                .collect(Collectors.toSet());

        Map<Integer, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestForm itemReq : request.getItems()) {
            Product product = productMap.get(itemReq.getProductId());
            if (product == null) {
                throw new NotFoundException(
                        "Sản phẩm không tồn tại: " + itemReq.getProductId(),
                        ErrorCodeConstant.PRODUCT_NOT_FOUND);
            }

            if (product.getStatus() != ProductStatus.IN_STOCK) {
                throw new BusinessException(
                        "Sản phẩm " + (product.getName()) + " không có sẵn trong kho!",
                        ErrorCodeConstant.PRODUCT_OUT_OF_STOCK);
            }

            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new BusinessException(
                        "Sản phẩm " + product.getName() + " không đủ hàng trong kho",
                        ErrorCodeConstant.OUT_OF_STOCK);
            }

            validatorUtils.validateAndReduceStock(product.getId(), itemReq.getSize(), itemReq.getQuantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemReq.getQuantity());
            item.setSize(itemReq.getSize());
            item.setTotal(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));

            total = total.add(item.getTotal());
            orderItems.add(item);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);
        
        // Generate and save QR code URL if payment method is BANK_TRANSFER
        if (request.getPaymentMethod() == PaymentMethod.BANK_TRANSFER) {
            String qrCodeUrl = vietQRService.generateQRCodeUrl(
                null,
                total, 
                "Thanh toan don hang"
            );
            order.setQrCodeUrl(qrCodeUrl);
            log.info("Generated VietQR URL for new order with amount: {}", total);
        }
        
        Order saved = orderRepository.save(order);

        if (saved.getPaymentMethod() == PaymentMethod.BANK_TRANSFER && saved.getQrCodeUrl() != null) {
            String finalQrCodeUrl = vietQRService.generateQRCodeUrl(
                saved.getId(), 
                saved.getTotalPrice(), 
                "Thanh toan don hang"
            );
            saved.setQrCodeUrl(finalQrCodeUrl);
            orderRepository.save(saved);
            log.info("Updated VietQR URL for order {}: {}", saved.getId(), finalQrCodeUrl);
        }

        return orderMapper.toResponse(saved);
    }

    @Override
    public Page<OrderResponse> getOrderListByFilter(OrderListByFilterForm dto) {
        Specification<Order> sf = OrderSpecification.nameContains(dto.getCustomerName())
                .and(OrderSpecification.phoneConstant(dto.getCustomerPhone()))
                .and(OrderSpecification.addressContains(dto.getCustomerAddress()))
                .and(OrderSpecification.statusEqual(dto.getStatus()));
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), Sort.by("createAt").descending());
        Page<Order> savePage = orderRepository.findAll(sf, pageable);
        return savePage.map(orderMapper::toResponse);
    }

    @Override
    public OrderResponse getOrderById(Integer id) {
        Account currentAccount = accountValidator.getCurrentAccount();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng", ErrorCodeConstant.ORDER_NOT_FOUND));

        boolean isAdmin = currentAccount.getRole() == AccountRole.ADMIN;
        if (!isAdmin && !order.getAccount().getId().equals(currentAccount.getId())) {
            throw new ForbiddenException("Ban khong co quyen truy cap. Vui long thu lai!", ErrorCodeConstant.NO_ACCESS);
        }
        
        // QR code URL is already saved in DB, just return it
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Integer id, UpdateOrderStatusForm dto) {
        Order existedById = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy id trong hệ thống", ErrorCodeConstant.ORDER_NOT_FOUND));

        List<OrderStatus> orderStatusList = Arrays.asList(OrderStatus.values());
        if (!orderStatusList.contains(dto.getStatus()))
            throw new BusinessException("Trạng thái không hợp lệ!", ErrorCodeConstant.INVALID_STATUS);

        validatorUtils.validateStatusTransactional(existedById.getStatus(), dto.getStatus());

        log.info("Updating order ID {} from status {} to {}", id, existedById.getStatus(), dto.getStatus());

        existedById.setStatus(dto.getStatus());
        Order saved = orderRepository.save(existedById);
        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RevenueReportResponse> getRevenueReport(RevenueFilterForm filter) {
        if (filter == null || filter.getPeriodType() == null) {
            throw new IllegalArgumentException("Bộ lọc không được trống");
        }

        String periodType = filter.getPeriodType().toUpperCase();

        String dateFormat = switch (periodType) {
            case "DAY" -> "%Y-%m-%d";
            case "MONTH" -> "%Y-%m";
            case "YEAR" -> "%Y";
            default -> throw new BusinessException(
                    "Loại thời gian không hợp lệ: " + periodType,
                    ErrorCodeConstant.INVALID_PERIOD_TYPE
            );
        };

        LocalDateTime start = parseOrDefaultStart(filter.getStartDate(), periodType);
        LocalDateTime end = parseOrDefaultEnd(filter.getEndDate(), start, periodType);

        if (start.isAfter(end)) {
            throw new BusinessException(
                    "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc",
                    ErrorCodeConstant.INVALID_INPUT
            );
        }

        log.debug("Tìm báo cáo doanh thu: {}, từ {} đến {}", periodType, start, end);

        List<Object[]> results = orderRepository.findRevenueByPeriod(dateFormat, start, end);

        return results.stream().map(result -> {
            RevenueReportResponse dto = new RevenueReportResponse();
            dto.setPeriod((String) result[0]);
            dto.setTotalRevenue((BigDecimal) result[1]);
            dto.setOrderCount((Long) result[2]);
            return dto;
        }).toList();
    }

    private LocalDateTime parseOrDefaultStart(String dateStr, String type) {
        if (dateStr == null || dateStr.isBlank()) {
            return switch (type) {
                case "MONTH" -> LocalDate.now().withDayOfMonth(1).atStartOfDay();
                case "YEAR" -> LocalDate.now().withDayOfYear(1).atStartOfDay();
                default -> LocalDate.now().atStartOfDay();
            };
        }

        try {
            return LocalDate.parse(dateStr).atStartOfDay();
        } catch (DateTimeParseException e) {
            log.error("Invalid start date format: {}", dateStr, e);
            throw new BusinessException(
                    "Định dạng ngày bắt đầu không hợp lệ: " + dateStr + ". Vui lòng dùng yyyy-MM-dd",
                    ErrorCodeConstant.INVALID_INPUT
            );
        }
    }

    private LocalDateTime parseOrDefaultEnd(String dateStr, LocalDateTime start, String type) {
        if (dateStr == null || dateStr.isBlank()) {
            return switch (type) {
                case "MONTH" -> start.plusMonths(1).minusNanos(1);
                case "YEAR" -> start.plusYears(1).minusNanos(1);
                default -> start.plusDays(1).minusNanos(1);
            };
        }

        try {
            return LocalDate.parse(dateStr).atTime(LocalTime.MAX);
        } catch (DateTimeParseException e) {
            log.error("Invalid end date format: {}", dateStr, e);
            throw new BusinessException(
                    "Định dạng ngày kết thúc không hợp lệ: " + dateStr + ". Vui lòng dùng yyyy-MM-dd",
                    ErrorCodeConstant.INVALID_INPUT
            );
        }
    }

    @Override
    public List<String> getAllOrderStatus() {
        return Arrays.stream(OrderStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponse> getAllOrdersByMe(Integer id, OrderStatus status, Pageable pageable) {
        Account currentAccount = accountValidator.getCurrentAccount();

        if (!currentAccount.getId().equals(id)) {
            throw new UnauthorizedException("Bạn không có quyền truy cập vào đơn hàng khác", ErrorCodeConstant.NO_ACCESS);
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tài khoản không tồn tại trong hệ thống", ErrorCodeConstant.ACCOUNT_NOT_FOUND));
        accountValidatorUtils.validatorAccountStatus(account);

        Pageable safePageable = pageable == null
                ? PageRequest.of(0, 20, Sort.by("createAt").descending())
                : pageable;

        Page<Order> orders = (status == null)
                ? orderRepository.findByAccountIdOrderByCreateAtDesc(id, safePageable)
                : orderRepository.findByAccountIdAndStatusOrderByCreateAtDesc(id, status, safePageable);

        return orders.map(orderMapper::toResponse);
    }

    @Override
    public MonthlyRevenueResponse monthlyRevenue() {
        Account account = accountValidator.getCurrentAccount();

        accountValidatorUtils.validatorAccountStatus(account);

        MonthlyRevenueResponse revenue = new MonthlyRevenueResponse();
        revenue.setTotalOrderMonthly(
                Integer.valueOf(String.valueOf(orderRepository.countOrdersInCurrentMonth()))
        );

        revenue.setTotalMoneyMonthly(
                BigDecimal.valueOf(orderRepository.totalRevenueInCurrentMonth())
        );

        revenue.setTotalProductMonthly(
                Integer.valueOf(String.valueOf(orderRepository.totalProductsSoldInCurrentMonth()))
        );

        revenue.setTotalInventory(
                productRepository.countInventory()
        );

        return revenue;
    }

    @Override
    public List<MonthRevenueListResponse> getMonthlyRevenue(Integer year, boolean millions) {
        Account account = accountValidator.getCurrentAccount();
        accountValidatorUtils.validatorAccountStatus(account);

        int y = (year == null) ? LocalDate.now().getYear() : year;
        List<Object[]> rows = orderRepository.findMonthlyRevenueByYear(y);

        Map<Integer, BigDecimal> map = new HashMap<>();
        for (Object[] row : rows) {
            Integer month = ((Number) row[0]).intValue();
            BigDecimal rev = row[1] == null ? BigDecimal.ZERO : (BigDecimal) row[1];
            map.put(month, rev);
        }

        List<MonthRevenueListResponse> result = new ArrayList<>(12);
        for (int m = 1; m <= 12; m++) {
            BigDecimal rev = map.getOrDefault(m, BigDecimal.ZERO);
            double value;
            if (millions) {
                value = rev.divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP).doubleValue();
            } else {
                value = rev.setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
            result.add(new MonthRevenueListResponse("T" + m, value));
        }
        return result;
    }
}
