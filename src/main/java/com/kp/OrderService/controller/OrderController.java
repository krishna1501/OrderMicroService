package com.kp.OrderService.controller;

import com.kp.OrderService.model.OrderRequest;
import com.kp.OrderService.model.OrderResponse;
import com.kp.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    @Qualifier("orderServiceImpl")
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        long orderId = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<OrderResponse> listOrdersById(@PathVariable("id") long orderId){
        OrderResponse orderResponse = orderService.getOrdersById(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }
}
