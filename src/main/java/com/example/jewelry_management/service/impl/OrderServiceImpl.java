package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.request.CreateOrderRequest;
import com.example.jewelry_management.dto.request.OrderItemRequest;
import com.example.jewelry_management.dto.request.OrderListByFilterDto;
import com.example.jewelry_management.dto.request.UpdateOrderStatus;
import com.example.jewelry_management.dto.response.OrderResponse;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.mapper.OrderMapper;
import com.example.jewelry_management.model.Order;
import com.example.jewelry_management.model.OrderItem;
import com.example.jewelry_management.model.OrderStatus;
import com.example.jewelry_management.model.Product;
import com.example.jewelry_management.repository.OrderRepository;
import com.example.jewelry_management.repository.ProductRepository;
import com.example.jewelry_management.repository.specification.OrderSpecification;
import com.example.jewelry_management.service.OrderService;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setIsDeleted(false);

        Set<Integer> productIds = request.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .collect(Collectors.toSet());

        Map<Integer, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productMap.get(itemReq.getProductId());
            if (product == null) {
                throw new NotFoundException("Sản phẩm không tồn tại: " + itemReq.getProductId(),
                        ErrorCodeConstant.PRODUCT_NOT_FOUND);
            }
            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new BusinessException("Sản phẩm " + product.getName() + " không đủ hàng trong kho",
                        "OUT_OF_STOCK");
            }

            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productsToUpdate.add(product);

            OrderItem item = new OrderItem();
            item.setOrderId(order);
            item.setProductId(product);
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
    public Page<OrderResponse> getOrderListByFilter(OrderListByFilterDto dto) {
        Specification<Order> sf = OrderSpecification.nameContains(dto.getCustomerName())
                .and(OrderSpecification.phoneEqual(dto.getCustomerPhone()))
                .and(OrderSpecification.addressContains(dto.getCustomerAddress()))
                .and(OrderSpecification.statusEqual(dto.getStatus()));
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), Sort.by("customerName").descending());
        Page<Order> savePage = orderRepository.findAll(sf, pageable);
        return savePage.map(orderMapper::toResponse);
    }

    @Override
    public OrderResponse getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy id trong hệ thống", ErrorCodeConstant.ORDER_NOT_FOUND));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Integer id, UpdateOrderStatus dto) {
        Order existedById = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy id trong hệ thống", ErrorCodeConstant.ORDER_NOT_FOUND));

        List<OrderStatus> orderStatusList = Arrays.asList(OrderStatus.values());
        if (!orderStatusList.contains(dto.getStatus()))
            throw new BusinessException("Trạng thái không hợp lệ!", ErrorCodeConstant.INVALID_STATUS);

        validateStatusTransactional(existedById.getStatus(), dto.getStatus());

        log.info("Updating order ID {} from status {} to {}", id, existedById.getStatus(), dto.getStatus());

        existedById.setStatus(dto.getStatus());
        Order saved = orderRepository.save(existedById);
        return orderMapper.toResponse(saved);
    }
//     logic(Không được chuyển ngược)
//     Từ PENDING -> CONFIRMED or CANCELLED
//     Từ CONFIRMED -> SHIPPED or CANCELLED.
//     Từ SHIPPED -> DELIVERED.
    private void validateStatusTransactional(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING -> {
                if (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED)
                    throw new BusinessException("Không thể chuyển trạng thái từ PENDING sang " + newStatus, ErrorCodeConstant.INVALID_STATUS_TRANSACTIONAL);
            }
            case CONFIRMED -> {
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED)
                    throw new BusinessException("Không thể chuyển trạng thái từ CONFIRMED sang " + newStatus, ErrorCodeConstant.INVALID_STATUS_TRANSACTIONAL);
            }
            case SHIPPED -> {
                if (newStatus != OrderStatus.DELIVERED)
                    throw new BusinessException("Không thể chuyển trạng thái từ SHIPPED sang " + newStatus, ErrorCodeConstant.INVALID_STATUS_TRANSACTIONAL);
            }
            case DELIVERED, CANCELLED ->
                    throw new BusinessException("Không thể chuyển trạng thái từ sang " + currentStatus, ErrorCodeConstant.FINAL_STATUS);
            default ->
                    throw new BusinessException("Trạng thái hiện tại không hợp lệ " + newStatus, ErrorCodeConstant.INVALID_STATUS);
        }
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
}
