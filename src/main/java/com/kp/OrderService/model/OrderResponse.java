package com.kp.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private long productId;
    private long totalAmount;
    private long quantity;
    private PaymentMode paymentMode;
}
