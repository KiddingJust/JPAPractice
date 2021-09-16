package com.gaiga.jpashop.repository.order.query;

import java.time.LocalDateTime;

import com.gaiga.jpashop.domain.Address;
import com.gaiga.jpashop.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderFlatDto {

	//데이터를 한줄로 플랫하게? 뭐지 이게. 
	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	
	private String itemName;
	private int orderPrice;
	private int count;
	public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address,
			String itemName, int orderPrice, int count) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		this.itemName = itemName;
		this.orderPrice = orderPrice;
		this.count = count;
	}
	
}
