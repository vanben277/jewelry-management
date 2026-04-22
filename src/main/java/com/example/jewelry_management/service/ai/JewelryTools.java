package com.example.jewelry_management.service.ai;

import com.example.jewelry_management.dto.res.OrderResponse;
import com.example.jewelry_management.dto.res.ProductResponse;
import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.enums.PaymentMethod;
import com.example.jewelry_management.form.CreateOrderRequestForm;
import com.example.jewelry_management.form.OrderItemRequestForm;
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

import java.util.ArrayList;
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

    @Tool("Tạo đơn hàng mới cho khách sau khi đã xác nhận đầy đủ thông tin. " +
            "Chỉ gọi tool này khi khách đã xác nhận mua hàng.")
    public String createOrder(
            Integer accountId,
            String customerName,
            String customerPhone,
            String customerAddress,
            String paymentMethod,
            String productIdsAndQuantities
    ) {
        if (accountId == null) {
            return "Bạn cần đăng nhập để đặt hàng. Vui lòng đăng nhập và thử lại!";
        }

        try {
            List<OrderItemRequestForm> items = new ArrayList<>();
            for (String part : productIdsAndQuantities.split(",")) {
                String[] split = part.trim().split(":");
                OrderItemRequestForm item = new OrderItemRequestForm();
                item.setProductId(Integer.parseInt(split[0].trim()));
                item.setQuantity(Integer.parseInt(split[1].trim()));
                items.add(item);
            }

            CreateOrderRequestForm form = new CreateOrderRequestForm();
            form.setCustomerName(customerName);
            form.setCustomerPhone(customerPhone);
            form.setCustomerAddress(customerAddress);
            form.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
            form.setItems(items);

            OrderResponse order = orderService.createOrder(form);
            return String.format(
                    "Đơn hàng #%d đã được tạo thành công! Tổng tiền: %s. Trạng thái: %s",
                    order.getId(),
                    order.getTotalPrice(),
                    order.getStatus()
            );
        } catch (Exception e) {
            log.error("Lỗi tạo đơn hàng: {}", e.getMessage());
            return "Không thể tạo đơn hàng: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách đơn hàng của khách. Dùng để khách xem lịch sử mua hàng hoặc trước khi hủy đơn.")
    public String getMyOrders(Integer accountId) {
        if (accountId == null) {
            return "Bạn cần đăng nhập để xem đơn hàng. Vui lòng đăng nhập và thử lại!";
        }
        try {
            List<OrderResponse> orders = orderService.getAllOrdersByMe(
                    accountId,
                    null,
                    PageRequest.of(0, 20, Sort.by("createAt").descending())
            ).getContent();
            if (orders.isEmpty()) return "Bạn chưa có đơn hàng nào.";

            StringBuilder sb = new StringBuilder("Danh sách đơn hàng của bạn:\n");
            for (OrderResponse o : orders) {
                sb.append(String.format("- Đơn #%d | %s | %s | %s\n",
                        o.getId(),
                        o.getStatus(),
                        o.getTotalPrice(),
                        o.getCreateAt()
                ));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Không thể lấy danh sách đơn hàng: " + e.getMessage();
        }
    }

    @Tool("Hủy đơn hàng của khách. Chỉ hủy được khi đơn ở trạng thái PENDING.")
    public String cancelOrder(Integer orderId, Integer accountId) {
        try {
            OrderResponse order = orderService.getOrderById(orderId);

            if (order == null) {
                return "Không tìm thấy đơn hàng #" + orderId;
            }

            Integer orderAccountId = order.getAccountResponse() != null
                    ? order.getAccountResponse().getId()
                    : null;

            if (orderAccountId == null || !orderAccountId.equals(accountId)) {
                return "Bạn không có quyền hủy đơn hàng này.";
            }
            if (!OrderStatus.PENDING.equals(order.getStatus())) {
                return String.format(
                        "Không thể hủy đơn #%d vì đơn đang ở trạng thái %s. Chỉ hủy được khi đơn ở trạng thái PENDING.",
                        orderId, order.getStatus()
                );
            }

            UpdateOrderStatusForm form = new UpdateOrderStatusForm();
            form.setStatus(OrderStatus.CANCELLED);
            orderService.updateStatus(orderId, form);

            return String.format("Đơn hàng #%d đã được hủy thành công!", orderId);

        } catch (Exception e) {
            log.error("Lỗi hủy đơn: {}", e.getMessage());
            return "Không thể hủy đơn hàng: " + e.getMessage();
        }
    }
}
