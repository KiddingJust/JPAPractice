package com.gaiga.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaiga.jpashop.domain.Delivery;
import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.domain.Order;
import com.gaiga.jpashop.domain.OrderItem;
import com.gaiga.jpashop.domain.item.Item;
import com.gaiga.jpashop.repository.ItemRepository;
import com.gaiga.jpashop.repository.MemberRepositoryOld;
import com.gaiga.jpashop.repository.OrderRepository;
import com.gaiga.jpashop.repository.OrderSearch;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepositoryOld memberRepository;
	private final ItemRepository itemRepository;
	
	//주문 - 회원id, 상품id, 수량
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);
		
		//배송정보 생성
		Delivery delivery = new Delivery();
		//(실제와는 다르지만) 회원의 배송지 정보 그대로 입력
		delivery.setAddress(member.getAddress());
		
		//주문상품 생성
		//이렇게 바로 Orderitem.createOrderItem~ 해도 되는건가? 객체 생성 안하고? 
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		//주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		
		//주문 저장
		//order를 변경해주면, OrderItem도 변경되도록 cascade 넣어주었음. 
		//delivery도 마찬가지. OrderItem 및 Delivery 자동으로 persist
		//Delivery 및 OrderItem은 웬만하면 Order만 참조. 그런 경우에만 Cascade
		orderRepository.save(order); 
		return order.getId();
	}
	
	//취소
	@Transactional
	public void cancelOrder(Long orderId) {
		//그냥 orderRepository.delete~ 뭐 이런 식으로 하면 안되나..?
		//배송 상태등도 바꾸어야 해서..?
		Order order = orderRepository.findOne(orderId);
		//비즈니스 로직을 Order.java에 이미 만들었기 때문..! 
		//음..그래도 뭔가 빠진 것 같은데... 
		order.cancel();
	}
	
	//검색 
	//사실 일반 조회면.. 걍 Controller에서 바로 Repository를 접근해도 문제는 없음. 
	public List<Order> findOrders(OrderSearch orderSearch){
//		return orderRepository.findAll(orderSearch);
		return orderRepository.findAllByString(orderSearch);
	}
}
