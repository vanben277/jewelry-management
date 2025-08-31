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
            "FROM Order o WHERE o.status = 'DELIVERED' " +
            "AND o.createAt BETWEEN :start AND :end " +
            "GROUP BY DATE_FORMAT(o.createAt, :dateFormat)")
    List<Object[]> findRevenueByPeriod(@Param("dateFormat") String dateFormat,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    List<Order> findByAccountIdOrderByCreateAtDesc(Integer id);

    @Query("SELECT COUNT(o) FROM Order o WHERE MONTH(o.createAt) = MONTH(CURRENT_DATE) AND YEAR(o.createAt) = YEAR(CURRENT_DATE)")
    int countOrdersInCurrentMonth();

    @Query("SELECT COALESCE(SUM(o.totalPrice),0) FROM Order o WHERE MONTH(o.createAt) = MONTH(CURRENT_DATE) AND YEAR(o.createAt) = YEAR(CURRENT_DATE)")
    long totalRevenueInCurrentMonth();

    @Query("SELECT COALESCE(SUM(od.quantity),0) FROM OrderItem od WHERE MONTH(od.order.createAt) = MONTH(CURRENT_DATE) AND YEAR(od.order.createAt) = YEAR(CURRENT_DATE)")
    int totalProductsSoldInCurrentMonth();

    @Query(value = """
            SELECT MONTH(o.create_at) AS month, COALESCE(SUM(o.total_price), 0) AS revenue
            FROM orders o
            WHERE YEAR(o.create_at) = :year
            GROUP BY MONTH(o.create_at)
            ORDER BY month
            """, nativeQuery = true)
    List<Object[]> findMonthlyRevenueByYear(@Param("year") int year);

}
