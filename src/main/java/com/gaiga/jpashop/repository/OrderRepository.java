package com.gaiga.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.gaiga.jpashop.domain.Order;
import com.gaiga.jpashop.domain.OrderStatus;

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
	
	//주문 상태 및 회원명에 따른 검색. 
	public List<Order> findAll(OrderSearch orderSearch){
		//Order를 조회하고 member와 조인을 하는 것. 잘 모르겠음. 
		return em.createQuery("select o from Order o join o.member m " +
						"where o.status = :status " +
						"and m.name = :name", Order.class)
				.setParameter("status", orderSearch.getOrderStatus())
				.setParameter("name", orderSearch.getMemberName())
				.setMaxResults(1000)	//최대 1,000건 조회
				.getResultList();
	}
}
