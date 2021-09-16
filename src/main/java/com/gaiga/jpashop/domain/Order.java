package com.gaiga.jpashop.domain;

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

import org.hibernate.annotations.BatchSize;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
public class Order {

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	
	//Order와 Member는 다대일 관계
	@ManyToOne(fetch = FetchType.LAZY)
	//Foreign Key 이름 지정
	@JoinColumn(name = "member_id")
	private Member member;
	
//	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;
	
	//LocalDateTime 쓰면 hibernate가 알아서 지원 (Java 8)
	private LocalDateTime orderDate;
	
	//Enum 타입? 주문 상태(Order, Cancel)넣을 것. 
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	// == 연관관계 편의 메서드 == //
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	/*
	public static void main(String[] args) {
		Member member = new Member();
		Order order = new Order();
		member.getOrders().add(order);
		order.setMember(member);
	}*/
	
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	
	/*== 생성 메서드 ==*/
	//가변인자는 가장 마지막에 넣어야함. 
	//주문 생성에 대한 것을 완결을 시킴 
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for(OrderItem orderItem:orderItems) {
			order.addOrderItem(orderItem);
		}
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		return order;
				
	}
	
	//== 비즈니스 로직 ==//
	//주문취소
	public void cancel() {
		if(delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
		}
		this.setStatus(OrderStatus.CANCEL);
		//고객이 상품 여러개 주문했다가 취소할 수 있으므로 각각 cancel을 해주어야 함. 
		for(OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}
	
	//== 조회 로직 ==//
	//전체 주문 가격 조회 
	public int getTotalPrice() {
		int totalPrice = 0;
		for(OrderItem orderItem : orderItems) {
			//주문할 때 주문 가격과 수량을 곱하는데, 그걸 OrderItem 객체에 넣어주는 것. 
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
	}
	
}
