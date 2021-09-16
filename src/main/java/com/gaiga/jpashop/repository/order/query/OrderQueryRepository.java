package com.gaiga.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
	//OrderRepository는 Order엔티티 조회용
	//query 패키지는 화면이나 비즈니스 관련된, 핏한 쿼리들만 여기에
	
	private final EntityManager em;
	
	//OrderQueryDto를 맨 처음에 가져옴. 
	//루프를 돌면서 컬렉션 부분을 직접 채워줌.
	public List<OrderQueryDto> findOrderQueryDtos(){
		List<OrderQueryDto> result = findOrders();
		
		result.forEach(o -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
			o.setOrderItems(orderItems);
		});
		
		return result;
	}
	
	public List<OrderItemQueryDto> findOrderItems(Long orderId){
		return em.createQuery(
					"select new com.gaiga.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
					" from OrderItem oi" +
					" join oi.item i" +
					" where oi.order.id = :orderId", OrderItemQueryDto.class)
				.setParameter("orderId", orderId)
				.getResultList();
	}
	
	//레파지토리가 Controller의 dto를 참조하면 안되니 여기 패키지에 새롭게 dto들을 만들어줌
	public List<OrderQueryDto> findOrders(){
		//이건 사실상 쿼리를 날리는건데 한줄만 가져옴. OrderItems이 생성자에 끼면 여러줄이 되므로 생성자에서 빼버림. 
		//그래서 위에서 한번 루프를 돌아줌. 
		return em.createQuery(
					"select new com.gaiga.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
					" from Order o" +
					" join o.member m" +
					" join o.delivery d", OrderQueryDto.class)
				.getResultList();
	}

	// == V5 == //
	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> result = findOrders();
		
		List<Long> orderIds = result.stream()
								.map(o -> o.getOrderId())
								.collect(Collectors.toList());
		
		List<OrderItemQueryDto> orderItems = 
							em.createQuery(
								"select new com.gaiga.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
								" from OrderItem oi" +
								" join oi.item i" +
								" where oi.order.id in :orderIds", OrderItemQueryDto.class)
							.setParameter("orderIds", orderIds)
							.getResultList();
		
		//v4와 다른 점은, 메모리에서 map으로 가져온 후 메모리에서 매칭해서 가져오는 것. 
		//쿼리는 두번 나감. 흠.. 
		//맵으로 최적화. 코드로 바꾸기도 쉽고 성능 최적화도 위해
		Map<Long, List<OrderItemQueryDto>> orderItemMap = 
								orderItems.stream()
										.collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
		
		result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
		
		return result;
	}

	// ===== V6 플랫! ===== 
	public List<OrderFlatDto> findAllByDto_flat() {
		return em.createQuery(
					"select new" +
					" com.gaiga.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)"+
					" from Order o" +
					" join o.member m" +
					" join o.delivery d" +
					" join o.orderItems oi" +
					" join oi.item i", OrderFlatDto.class)
				.getResultList();			
	}
	
}
