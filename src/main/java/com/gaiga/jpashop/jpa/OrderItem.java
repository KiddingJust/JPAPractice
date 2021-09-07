package com.gaiga.jpashop.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gaiga.jpashop.jpa.item.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {

	@Id @GeneratedValue
	@Column(name = "order_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="item_id")
	private Item item;
	
	//하나의 Order가 여러 Order_Item을 가지므로 이쪽이 N. 양방향 연관
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	private int orderPrice;	//주문 가격
	private int count;		//주문 수량
}
