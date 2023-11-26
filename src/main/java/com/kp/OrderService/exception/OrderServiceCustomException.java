package com.kp.OrderService.exception;

import lombok.Data;

@Data
public class OrderServiceCustomException extends RuntimeException{

    private String errorCode;

    public OrderServiceCustomException(String errorMessage,String errorCode){
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
