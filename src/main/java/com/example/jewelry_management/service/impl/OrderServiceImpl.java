package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.request.CreateOrderRequest;
import com.example.jewelry_management.dto.request.OrderItemRequest;
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
import com.example.jewelry_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
}
