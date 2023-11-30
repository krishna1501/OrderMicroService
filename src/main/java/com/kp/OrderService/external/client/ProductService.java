package com.kp.OrderService.external.client;

import com.kp.OrderService.exception.OrderServiceCustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE/products")
public interface ProductService {
	@PutMapping("/reduceQuantity/{id}")
	ResponseEntity<Void> reduceProducts(@PathVariable("id") long productId, @RequestParam long quantity);

	default void fallback(Exception e) {
		throw new OrderServiceCustomException("Payment Service is not available!", "UNAVAILABLE", 500);
	}
}
