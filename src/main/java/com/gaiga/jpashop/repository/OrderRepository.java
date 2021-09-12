package com.gaiga.jpashop.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.gaiga.jpashop.domain.Order;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	
	private final EntityManager em;
	
	public void save(Order order) {
		em.persist(order);
	}
	
	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}
	
//	//주문 상태 및 회원명에 따른 검색. 
//	public List<Order> findAll(OrderSearch orderSearch){
//		
//	}
}
