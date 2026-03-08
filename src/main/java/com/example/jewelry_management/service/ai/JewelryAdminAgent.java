package com.example.jewelry_management.service.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface JewelryAdminAgent {
    @SystemMessage({
            "Bạn là AI phân tích kinh doanh của cửa hàng Jewelry Management.",
            "Chỉ hỗ trợ các yêu cầu phân tích dữ liệu, doanh thu, tồn kho.",
            "Đưa ra nhận xét khách quan, số liệu rõ ràng và đề xuất chiến lược kinh doanh."
    })
    String chat(@UserMessage String message);
}
