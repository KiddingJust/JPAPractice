package com.gaiga.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaiga.jpashop.domain.Address;
import com.gaiga.jpashop.domain.Order;
import com.gaiga.jpashop.domain.OrderStatus;
import com.gaiga.jpashop.repository.OrderRepository;
import com.gaiga.jpashop.repository.OrderSearch;
import com.gaiga.jpashop.repository.OrderSimpleQueryDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * xToOne  (xToMany는 컬렉션이므로 복잡해짐. 다음에 다룰 것.order와 orderitem) 
 * Order
 * Order -> Member 
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1(){
		return orderRepository.findAllByString(new OrderSearch());
	}
	
	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2(){
		//원래는 List가 아니라 Result로 감싸야하는데 걍..ㅎ.. 
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		List<SimpleOrderDto> result = orders.stream()
										.map(o -> new SimpleOrderDto(o))	//A를 B로 바꾸는 것. 
										.collect(Collectors.toList());	//다시 List로 바꾸는 것. 
		return result;
	}

	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3(){
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		
		List<SimpleOrderDto> result = orders.stream()
										.map(o -> new SimpleOrderDto(o))	//A를 B로 바꾸는 것. 
										.collect(Collectors.toList());	//다시 List로 바꾸는 것. 
		
		return result;
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4(){
		return orderRepository.findOrderDtos();
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		
		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
		}
	}
	
	
}
