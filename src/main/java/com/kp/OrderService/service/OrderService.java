package com.kp.OrderService.service;

import com.kp.OrderService.model.OrderRequest;
import com.kp.OrderService.model.OrderResponse;

public interface OrderService {
	long placeOrder(OrderRequest orderRequest);

	OrderResponse getOrdersById(long orderId);
}
