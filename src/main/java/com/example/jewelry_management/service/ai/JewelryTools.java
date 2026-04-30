package com.example.jewelry_management.service.ai;

import com.example.jewelry_management.dto.res.OrderResponse;
import com.example.jewelry_management.dto.res.ProductResponse;
import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.form.UpdateOrderStatusForm;
import com.example.jewelry_management.repository.ChatHistoryRepository;
import com.example.jewelry_management.service.OrderService;
import com.example.jewelry_management.service.ProductService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JewelryTools {
    private final ProductService productService;
    private final OrderService orderService;
    private final ChatHistoryRepository chatHistoryRepository;

    @Tool("Tìm kiếm sản phẩm trang sức theo tên hoặc mô tả")
    public List<ProductResponse> searchProducts(String query) {
        return productService.searchProducts(query).getContent();
    }

    @Tool("Lấy danh sách các sản phẩm trang sức mới nhất của cửa hàng")
    public List<ProductResponse> getLatestProducts() {
        return productService.latestProducts();
    }

    @Tool("Lấy danh sách các loại vàng đang kinh doanh (10K, 14K, 18K, 24K)")
    public List<String> getGoldTypes() {
        return productService.getAllGoldType();
    }

    @Tool("Lấy báo cáo doanh thu tổng quan của tháng hiện tại")
    public Map<String, Object> getMonthlyRevenueStats() {
        var stats = orderService.monthlyRevenue();
        return Map.of(
                "Tổng đơn hàng tháng này", stats.getTotalOrderMonthly(),
                "Tổng doanh thu tháng này", stats.getTotalMoneyMonthly(),
                "Số lượng sản phẩm đã bán", stats.getTotalProductMonthly(),
                "Tổng số tồn kho hiện tại", stats.getTotalInventory()
        );
    }

    @Tool("Phân tích xu hướng: Lấy danh sách các câu hỏi khách hàng thường hỏi AI gần dây")
    public List<String> getCustomerRecentInterests() {
        return chatHistoryRepository.findRecentCustomerQueries();
    }

    @Tool("Lấy danh sách đơn hàng của khách. Dùng để khách xem lịch sử mua hàng hoặc trước khi hủy đơn.")
    public String getMyOrders(Integer accountId) {
        if (accountId == null) {
            return "{\"type\":\"error\",\"message\":\"Bạn cần đăng nhập để xem đơn hàng. Vui lòng đăng nhập và thử lại!\",\"meta\":{\"requiresLogin\":true}}";
        }
        try {
            List<OrderResponse> orders = orderService.getAllOrdersByMe(
                    accountId,
                    null,
                    PageRequest.of(0, 5, Sort.by("createAt").descending())
            ).getContent();
            if (orders.isEmpty()) {
                return "{\"type\":\"text\",\"message\":\"Bạn chưa có đơn hàng nào.\"}";
            }

            // Build JSON response with orders
            StringBuilder json = new StringBuilder();
            json.append("{\"type\":\"orders\",\"message\":\"Đây là 5 đơn hàng gần nhất của bạn:\",\"orders\":[");
            
            for (int i = 0; i < orders.size(); i++) {
                OrderResponse o = orders.get(i);
                if (i > 0) json.append(",");
                json.append(String.format(
                    "{\"id\":%d,\"status\":\"%s\",\"totalPrice\":\"%s\",\"createAt\":\"%s\",\"customerName\":\"%s\",\"paymentMethod\":\"%s\",\"itemCount\":%d}",
                    o.getId(),
                    o.getStatus(),
                    String.format("%,dđ", o.getTotalPrice().longValue()),
                    o.getCreateAt().toString(),
                    o.getCustomerName() != null ? o.getCustomerName() : "",
                    o.getPaymentMethod() != null ? o.getPaymentMethod() : "",
                    o.getItems() != null ? o.getItems().size() : 0
                ));
            }
            
            json.append("],\"meta\":{\"totalCount\":").append(orders.size()).append("}}");
            return json.toString();
        } catch (Exception e) {
            log.error("Error getting orders for account {}: ", accountId, e);
            return "{\"type\":\"error\",\"message\":\"Không thể lấy danh sách đơn hàng.\"}";
        }
    }

    @Tool("Hủy đơn hàng của khách. Chỉ hủy được khi đơn ở trạng thái PENDING.")
    public String cancelOrder(Integer orderId, Integer accountId) {
        try {
            OrderResponse order = orderService.getOrderById(orderId);

            if (order == null) {
                return String.format("{\"type\":\"error\",\"message\":\"Không tìm thấy đơn hàng #%d\"}", orderId);
            }

            Integer orderAccountId = order.getAccountResponse() != null
                    ? order.getAccountResponse().getId()
                    : null;

            if (orderAccountId == null || !orderAccountId.equals(accountId)) {
                return "{\"type\":\"error\",\"message\":\"Bạn không có quyền hủy đơn hàng này.\"}";
            }
            if (!OrderStatus.PENDING.equals(order.getStatus())) {
                return String.format(
                        "{\"type\":\"error\",\"message\":\"Không thể hủy đơn #%d vì đơn đang ở trạng thái %s. Chỉ hủy được khi đơn ở trạng thái PENDING.\"}",
                        orderId, order.getStatus()
                );
            }

            UpdateOrderStatusForm form = new UpdateOrderStatusForm();
            form.setStatus(OrderStatus.CANCELLED);
            orderService.updateStatus(orderId, form);

            return String.format("{\"type\":\"text\",\"message\":\"Đơn hàng #%d đã được hủy thành công!\"}", orderId);

        } catch (Exception e) {
            log.error("Lỗi hủy đơn: {}", e.getMessage());
            return String.format("{\"type\":\"error\",\"message\":\"Không thể hủy đơn hàng: %s\"}", e.getMessage());
        }
    }
}
