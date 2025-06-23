package com.example.jewelry_management.repository.specification;

import com.example.jewelry_management.model.Order;
import com.example.jewelry_management.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<Order> nameContains(String customerName) {
        return
                (root, query, cb) ->
                        customerName == null || customerName.isEmpty() ? null :
                                cb.like(cb.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%");

    }

    public static Specification<Order> phoneEqual(String customerPhone) {
        return
                (root, query, cb) ->
                        customerPhone == null || customerPhone.isEmpty() ? null :
                                cb.equal(root.get("customerPhone"), customerPhone);
    }

    public static Specification<Order> addressContains(String customerAddress) {
        return
                (root, query, cb) ->
                        customerAddress == null || customerAddress.isEmpty() ? null :
                                cb.like(cb.lower(root.get("customerAddress")), "%" + customerAddress + "%");

    }

    public static Specification<Order> statusEqual(OrderStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<Order> notDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted"));
    }
}
