package com.example.jewelry_management.dto.ai;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Structured AI Response DTO
 * Đảm bảo AI luôn trả về format nhất quán cho frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiResponseDto {
    /**
     * Loại response: "text", "products", "orders", "confirmation", "error"
     */
    private String type;
    
    /**
     * Nội dung text chính (luôn có)
     */
    private String message;
    
    /**
     * Danh sách sản phẩm (nếu type = "products")
     */
    private List<ProductInfo> products;
    
    /**
     * Danh sách đơn hàng (nếu type = "orders")
     */
    private List<OrderInfo> orders;
    
    /**
     * Thông tin xác nhận (nếu type = "confirmation")
     */
    private ConfirmationInfo confirmation;
    
    /**
     * Metadata bổ sung
     */
    private MetaInfo meta;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductInfo {
        private Integer id;
        private String name;
        private String price;
        private String status;
        private String sku;
        private String imageUrl;
        private String goldType;
        private String description;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderInfo {
        private Integer id;
        private String status;
        private String totalPrice;
        private String createAt;
        private String customerName;
        private String paymentMethod;
        private Integer itemCount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfirmationInfo {
        private String action;
        private Integer targetId;
        private String targetName;
        private String question;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaInfo {
        private Integer totalCount;
        private Boolean requiresLogin;
        private Boolean requiresConfirmation;
        private String nextAction;
    }
}
