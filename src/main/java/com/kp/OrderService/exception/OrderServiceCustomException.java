package com.kp.OrderService.exception;

import lombok.Data;

@Data
public class OrderServiceCustomException extends RuntimeException{

    private String errorCode;
    private int status;

    public OrderServiceCustomException(String errorMessage,String errorCode,int status){
        super(errorMessage);
        this.errorCode = errorCode;
        this.status = status;
    }
}
