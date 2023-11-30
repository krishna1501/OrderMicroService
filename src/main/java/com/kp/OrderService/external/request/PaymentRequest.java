package com.kp.OrderService.external.request;

import com.kp.OrderService.external.response.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
	private long orderId;
	private long amount;
	private String referenceNumber;
	private PaymentMode paymentMode;
}
