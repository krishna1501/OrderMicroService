package com.kp.OrderService.service;

import com.kp.OrderService.entity.Order;
import com.kp.OrderService.exception.OrderServiceCustomException;
import com.kp.OrderService.external.client.PaymentService;
import com.kp.OrderService.external.client.ProductService;
import com.kp.OrderService.external.request.PaymentRequest;
import com.kp.OrderService.external.response.PaymentResponse;
import com.kp.OrderService.external.response.ProductResponse;
import com.kp.OrderService.model.OrderRequest;
import com.kp.OrderService.model.OrderResponse;
import com.kp.OrderService.model.PaymentDetails;
import com.kp.OrderService.model.ProductDetails;
import com.kp.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private RestTemplate restTemplate;


	@Override
	public long placeOrder(OrderRequest orderRequest) {
		log.info("Placing the order request:{}", orderRequest);

		productService.reduceProducts(orderRequest.getProductId(), orderRequest.getQuantity());

		log.info("Order with Status CREATED");
		Order order = Order.builder()
				.productId(orderRequest.getProductId())
				.amount(orderRequest.getTotalAmount())
				.orderStatus("CREATED")
				.orderDate(Instant.now())
				.quantity(orderRequest.getQuantity())
				.build();

		order = orderRepository.save(order);
		log.info("Calling payment service to complete the payment");
		PaymentRequest paymentRequest = PaymentRequest
				.builder()
				.orderId(order.getId())
				.paymentMode(orderRequest.getPaymentMode())
				.amount(order.getAmount())
				.build();

		String orderStatus = null;

		try {
			paymentService.doPayment(paymentRequest);
			log.info("Payment done Successfully. Changing the order status to placed");
			orderStatus = "PLACED";
		} catch (Exception e) {
			log.error("Error occurred in payment. Changing order status to failed");
			orderStatus = "PAYMENT_FAILED";
		}

		order.setOrderStatus(orderStatus);

		orderRepository.save(order);
		log.info("Order Places Successfully with Order Id:{}", order.getId());

		return order.getId();
	}

	@Override
	public OrderResponse getOrdersById(long orderId) {
		log.info("Get order Details for order id: {}", orderId);

		Order order = orderRepository
				.findById(orderId)
				.orElseThrow(() -> new OrderServiceCustomException("Order details for this id not found", "NOT_FOUND", 404));

		log.info("Invoking Product service to fetch the product for id {}", order.getProductId());

		ProductResponse productResponse = restTemplate.getForObject(
				"http://PRODUCT-SERVICE/product/" + order.getId(),
				ProductResponse.class
		);

		log.info("Getting payment information from the Payment Service");

		PaymentResponse paymentResponse = restTemplate.getForObject(
				"http://PAYMENT-SERVICE/payment/order/" + order.getId(),
				PaymentResponse.class
		);

		ProductDetails productDetails = ProductDetails
				.builder()
				.productName(productResponse.getProductName())
				.price(productResponse.getPrice())
				.quantity(productResponse.getQuantity())
				.productId(productResponse.getProductId())
				.build();

		PaymentDetails paymentDetails = PaymentDetails
				.builder()
				.paymentId(paymentResponse.getPaymentId())
				.status(paymentResponse.getStatus())
				.paymentDate(paymentResponse.getPaymentDate())
				.paymentMode(paymentResponse.getPaymentMode())
				.build();

		log.info("Order details retrieved with Order Id:{} successfully", order.getProductId());

		return OrderResponse
				.builder()
				.productId(order.getProductId())
				.quantity(order.getQuantity())
				.totalAmount(order.getAmount())
				.orderStatus(order.getOrderStatus())
				.productDetails(productDetails)
				.paymentDetails(paymentDetails)
				.build();
	}
}
