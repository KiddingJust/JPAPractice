package com.gaiga.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gaiga.jpashop.domain.Address;
import com.gaiga.jpashop.domain.Order;
import com.gaiga.jpashop.domain.OrderItem;
import com.gaiga.jpashop.domain.OrderStatus;
import com.gaiga.jpashop.repository.OrderRepository;
import com.gaiga.jpashop.repository.OrderSearch;
import com.gaiga.jpashop.repository.order.query.OrderFlatDto;
import com.gaiga.jpashop.repository.order.query.OrderItemQueryDto;
import com.gaiga.jpashop.repository.order.query.OrderQueryDto;
import com.gaiga.jpashop.repository.order.query.OrderQueryRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;
	private final OrderQueryRepository orderQueryRepository;
	
	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1(){
		//검색 조건 없으면 전체 가져오는 것
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		//lazy loading에 대해 터치를 해주어야 함. 
		//모든 객체를 다 초기화한 것. 
		for(Order order:all) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();
//			for(OrderItem orderItem : orderItems) {
//				orderItem.getItem().getName();
//			}
			orderItems.stream().forEach(o -> o.getItem().getName());
		}
		return all;
	}
	
	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2(){
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		//orders를 orderDto로 변환. 변환은 map을 씀. 
		List<OrderDto> result = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GetMapping("/api/v3/orders")
	public List<OrderDto> orderV3(){
		List<Order> orders = orderRepository.findAllWithItem();
		List<OrderDto> result = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
		
		return result;
	}

	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> orderV3_1(){
		//xToOne 먼저 가져오고. 
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		
		List<OrderDto> result = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GetMapping("/api/v3.2/orders")
	public List<OrderDto> orderV3_1_page(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "100") int limit ){
		//xToOne 먼저 가져오고. 
		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
		
		List<OrderDto> result = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> ordersV4() {
		return orderQueryRepository.findOrderQueryDtos();
	}
	
	@GetMapping("/api/v5/orders")
	public List<OrderQueryDto> ordersV5() {
		return orderQueryRepository.findAllByDto_optimization();
	}

	@GetMapping("/api/v6/orders")
	public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                		Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), Collectors.toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(Collectors.toList());
    }
	
	
	
	@Getter
	static class OrderDto {
		
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
//		private List<OrderItem> orderItems;
		private List<OrderItemDto> orderItems;
		
		public OrderDto(Order o) {
			orderId = o.getId();
			name = o.getMember().getName();
			orderDate = o.getOrderDate();
			orderStatus = o.getStatus();
			address = o.getDelivery().getAddress();
//			o.getOrderItems().stream().forEach(v -> v.getItem().getName());
//			orderItems = o.getOrderItems();
			orderItems = o.getOrderItems().stream()
									.map(orderItem -> new OrderItemDto(orderItem))
									.collect(Collectors.toList());
		}
	}
	
	//만약 상품명만 필요한 게 요구조건이라면,
	@Getter
	static class OrderItemDto{
		private String itemName;
		private int orderPrice;
		private int count;
		
		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}
	
	
	
}
