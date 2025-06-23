package com.example.jewelry_management.dto.request;


import com.example.jewelry_management.model.OrderStatus;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListByFilterDto {
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private OrderStatus status;

    @Min(value = 1, message = "Kích thước trang ít nhất là 1")
    private Integer pageSize = 5;

    @Min(value = 0, message = "Số trang phải là số dương")
    private Integer pageNumber = 0;
}
