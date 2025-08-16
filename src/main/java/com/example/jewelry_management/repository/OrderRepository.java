package com.example.jewelry_management.repository;

import com.example.jewelry_management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    @Query("SELECT DATE_FORMAT(o.createAt, :dateFormat) AS period, " +
            "SUM(o.totalPrice) AS totalRevenue, COUNT(o) AS orderCount " +
            "FROM Order o WHERE o.status = 'DELIVERED' AND o.isDeleted = false " +
            "AND o.createAt BETWEEN :start AND :end " +
            "GROUP BY DATE_FORMAT(o.createAt, :dateFormat)")
    List<Object[]> findRevenueByPeriod(@Param("dateFormat") String dateFormat,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    List<Order> findByAccountIdOrderByCreateAtDesc(Integer id);
}
