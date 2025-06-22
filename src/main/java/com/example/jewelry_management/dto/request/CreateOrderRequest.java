package com.example.jewelry_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotBlank(message = "Tên khách hàng không được bỏ trống")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String customerPhone;

    @NotBlank(message = "Địa chỉ không được bỏ trống")
    private String customerAddress;

    @NotNull(message = "Danh sách sản phẩm không được rỗng")
    @Size(min = 1, message = "Phải có ít nhất 1 sản phẩm trong đơn hàng")
    private List<OrderItemRequest> items;
}
