package com.example.jewelry_management.form;

import com.example.jewelry_management.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UpdateOrderStatusForm {
    @NotNull(message = "Trạng thái không được bỏ trống!")
    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private OrderStatus status;
}
