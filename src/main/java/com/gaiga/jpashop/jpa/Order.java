package com.gaiga.jpashop.jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	
	//Order와 Member는 다대일 관계
	@ManyToOne(fetch = FetchType.LAZY)
	//Foreign Key 이름 지정
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItem = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;
	
	//LocalDateTime 쓰면 hibernate가 알아서 지원 (Java 8)
	private LocalDateTime orderDate;
	
	//Enum 타입? 주문 상태(Order, Cancel)넣을 것. 
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
}
