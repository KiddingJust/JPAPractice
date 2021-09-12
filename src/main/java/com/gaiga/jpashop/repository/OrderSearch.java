package com.gaiga.jpashop.repository;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.gaiga.jpashop.domain.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

	private String memberName;	//회원 이름 
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; //주문 상태
}
