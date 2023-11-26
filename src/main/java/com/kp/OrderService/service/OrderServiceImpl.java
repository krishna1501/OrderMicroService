package com.kp.OrderService.service;

import com.kp.OrderService.entity.Order;
import com.kp.OrderService.exception.OrderServiceCustomException;
import com.kp.OrderService.external.client.ProductService;
import com.kp.OrderService.model.OrderRequest;
import com.kp.OrderService.model.OrderResponse;
import com.kp.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing the order request:{}", orderRequest);

        productService.reduceProducts(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Order with Status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);

        log.info("Order Places Successfully with Order Id:{}", order.getId());

        return order.getId();
    }

    @Override
    public OrderResponse getOrdersById(long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderServiceCustomException("Order details for this id not found", "NOT_FOUND",404));
        log.info("Order details retrieved with Order Id:{} successfully", order.getProductId());
        return OrderResponse
                .builder()
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalAmount(order.getAmount())
                .build();
    }
}
