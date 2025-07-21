package com.example.jewelry_management.utils;

import com.example.jewelry_management.enums.OrderStatus;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorUtils {
    //     logic(Không được chuyển ngược)
//     Từ PENDING -> CONFIRMED or CANCELLED
//     Từ CONFIRMED -> SHIPPED or CANCELLED.
//     Từ SHIPPED -> DELIVERED.
    public void validateStatusTransactional(OrderStatus currentStatus, OrderStatus newStatus) {
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
}
