package com.kp.OrderService.external.client;

import com.kp.OrderService.exception.OrderServiceCustomException;
import com.kp.OrderService.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
	@PostMapping("/doPayment")
	ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

	default void fallback(Exception e) {
		throw new OrderServiceCustomException("Payment Service is not available!", "UNAVAILABLE", 500);
	}


}
