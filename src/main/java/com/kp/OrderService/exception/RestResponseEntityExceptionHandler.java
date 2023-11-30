package com.kp.OrderService.exception;


import com.kp.OrderService.model.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(OrderServiceCustomException.class)
	public ResponseEntity<ErrorResponse> orderServiceCustomException(OrderServiceCustomException orderServiceCustomException) {
		log.warn("Matching records NOT_FOUND");
		return new ResponseEntity<>(ErrorResponse.builder()
				.errorCode(orderServiceCustomException.getErrorCode())
				.errorMessage(orderServiceCustomException.getMessage())
				.build(), HttpStatus.valueOf(orderServiceCustomException.getStatus()));
	}
}
