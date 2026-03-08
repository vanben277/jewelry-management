package com.example.jewelry_management.service.ai;

import com.example.jewelry_management.dto.res.ProductResponse;
import com.example.jewelry_management.repository.ChatHistoryRepository;
import com.example.jewelry_management.service.OrderService;
import com.example.jewelry_management.service.ProductService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
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
}
