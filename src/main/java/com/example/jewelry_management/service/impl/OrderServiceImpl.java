package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.res.OrderResponse;
import com.example.jewelry_management.dto.res.RevenueReportResponse;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.enums.ProductStatus;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.ForbiddenException;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.form.*;
import com.example.jewelry_management.mapper.OrderMapper;
import com.example.jewelry_management.model.Account;
import com.example.jewelry_management.model.Order;
import com.example.jewelry_management.model.OrderItem;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.repository.OrderRepository;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.OrderSpecification;
import com.example.jewelry_management.service.OrderService;
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
import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequestForm request) {
        Account currentAccount = accountValidator.getCurrentAccount();

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setIsDeleted(false);
        order.setAccount(currentAccount);

        Set<Integer> productIds = request.getItems().stream()
                .map(OrderItemRequestForm::getProductId)
                .collect(Collectors.toSet());

        Map<Integer, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        for (OrderItemRequestForm itemReq : request.getItems()) {
            Product product = productMap.get(itemReq.getProductId());
            if (product == null) {
                throw new NotFoundException("Sản phẩm không tồn tại: " + itemReq.getProductId(),
                        ErrorCodeConstant.PRODUCT_NOT_FOUND);
            }

            if (product.getStatus() != ProductStatus.IN_STOCK) {
                throw new BusinessException("Sản phẩm " + (product.getName()) + " không có sẵn trong kho!", ErrorCodeConstant.PRODUCT_OUT_OF_STOCK);
            }

            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new BusinessException("Sản phẩm " + product.getName() + " không đủ hàng trong kho",
                        "OUT_OF_STOCK");
            }

            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productsToUpdate.add(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemReq.getQuantity());
            item.setTotal(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));

            total = total.add(item.getTotal());
            orderItems.add(item);
        }

        productRepository.saveAll(productsToUpdate);

        order.setItems(orderItems);
        order.setTotalPrice(total);
        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    public Page<OrderResponse> getOrderListByFilter(OrderListByFilterForm dto) {
        Specification<Order> sf = OrderSpecification.nameContains(dto.getCustomerName())
                .and(OrderSpecification.phoneEqual(dto.getCustomerPhone()))
                .and(OrderSpecification.addressContains(dto.getCustomerAddress()))
                .and(OrderSpecification.statusEqual(dto.getStatus()))
                .and(OrderSpecification.notDeleted());
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), Sort.by("customerName").descending());
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
    @Transactional
    public OrderResponse softOrderDeleted(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng!", ErrorCodeConstant.ORDER_NOT_FOUND));

        if (Boolean.TRUE.equals(order.getIsDeleted())) {
            throw new BusinessException("Đơn hàng đã đươc xóa khỏi hệ thống", ErrorCodeConstant.ORDER_ALREADY_DELETED);
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CANCELLED) {
            throw new BusinessException("Không thể xóa đơn hàng ở trạng thái " + order.getStatus(), ErrorCodeConstant.INVALID_ORDER_STATUS);
        }

        order.setIsDeleted(true);
        Order saved = orderRepository.save(order);
        orderMapper.toResponse(saved);
        return null;
    }

    @Override
    @Transactional
    public OrderResponse restoreOrderDeleted(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng!", ErrorCodeConstant.ORDER_NOT_FOUND));
        if (Boolean.FALSE.equals(order.getIsDeleted())) {
            throw new BusinessException("Đơn hàng chưa bị xóa khỏi hệ thống!", ErrorCodeConstant.ORDER_NOT_DELETED);
        }
        order.setIsDeleted(false);
        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional()
    public List<RevenueReportResponse> getRevenueReport(RevenueFilterForm filter) {
        if (filter == null || filter.getPeriodType() == null) {
            log.error("Bộ lọc khônng được trống");
            throw new IllegalArgumentException("Bộ lọc khônng được trống");
        }

        String dateFormat;
        LocalDateTime start, end;

        switch (filter.getPeriodType().toUpperCase()) {
            case "DAY":
                dateFormat = "%Y-%m-%d";
                start = filter.getStartDate() != null ? LocalDateTime.parse(filter.getStartDate()) : LocalDateTime.now();
                end = filter.getEndDate() != null ? LocalDateTime.parse(filter.getEndDate()) : start;
                break;
            case "MONTH":
                dateFormat = "%Y-%m";
                start = filter.getStartDate() != null ? LocalDateTime.parse(filter.getStartDate() + "T00:00:00") : LocalDateTime.now().withDayOfMonth(1);
                end = filter.getEndDate() != null ? LocalDateTime.parse(filter.getEndDate() + "T00:00:00").withDayOfMonth(1).plusMonths(1).minusDays(1) : start.withDayOfMonth(1).plusMonths(1).minusDays(1);
                break;
            case "YEAR":
                dateFormat = "%Y";
                start = filter.getStartDate() != null ? LocalDateTime.parse(filter.getStartDate() + "T00:00:00-T00:00:00") : LocalDateTime.now().withDayOfYear(1);
                end = filter.getEndDate() != null ? LocalDateTime.parse(filter.getEndDate() + "T00:00:00-T00:00:00").withDayOfYear(1).plusYears(1).minusDays(1) : start.withDayOfYear(1).plusYears(1).minusDays(1);
                break;
            default:
                log.error("Loại thời gian không hợp lệ: {}", filter.getPeriodType());
                throw new BusinessException("Loại thời gian không hợp lệ: " + filter.getPeriodType(), ErrorCodeConstant.INVALID_PERIOD_TYPE);
        }

        log.debug("Đang tìm báo cáo doanh thu cho periodType: {}, start: {}, end: {}", filter.getPeriodType(), start, end);

        List<Object[]> results = orderRepository.findRevenueByPeriod(dateFormat, start, end);
        return results.stream().map(result -> {
            RevenueReportResponse dto = new RevenueReportResponse();
            dto.setPeriod((String) result[0]);
            dto.setTotalRevenue((BigDecimal) result[1]);
            dto.setOrderCount((Long) result[2]);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllOrderStatus() {
        return Arrays.stream(OrderStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
