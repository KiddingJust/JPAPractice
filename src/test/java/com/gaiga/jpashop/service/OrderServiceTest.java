package com.gaiga.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gaiga.jpashop.domain.Address;
import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.domain.Order;
import com.gaiga.jpashop.domain.OrderStatus;
import com.gaiga.jpashop.domain.item.Book;
import com.gaiga.jpashop.domain.item.Item;
import com.gaiga.jpashop.repository.OrderRepository;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
public class OrderServiceTest {

	@Autowired EntityManager em;
	@Autowired OrderService orderService;
	@Autowired OrderRepository orderRepository;
	

	
	@Test
	public void 상품주문() throws Exception {
		//given
		//얘네는 테스트 클래스에서 추출한 메서드. 맨 아래 있음. 
		Member member = createMember();
		Book book = createBook("JPA2", 10000, 10);
		
		int orderCount = 2;
		
		//when
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
		
		//then
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
		assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
		assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문가격은 (가격 * 수량) 이다");
		assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
	}

	@Test
	public void 상품주문_재고수량초과() throws Exception {
		//given
		Member member = createMember();
		Item item = createBook("JPA2", 10000, 10);
		
		int orderCount = 11;
		
		//when
		orderService.order(member.getId(), item.getId(), orderCount);
		
		//then
		//위에서 Exception이 터져야하는데 안터지면.. 혹시나 여길 넘어가면 안되므로
		//위에서 예외처리 안되면 fail이 실행됨. 
		fail("재고 수량 부족 예외가 발생해야 한다.");
	}
	
	@Test
	public void 주문취소() throws Exception {
		//given
		Member member = createMember();
		Book item = createBook("New JPA Book", 10000, 10);
		
		int orderCount = 9;
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		//when
		orderService.cancelOrder(orderId);
		
		//then
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals(OrderStatus.CANCEL, getOrder.getStatus() ,"주문 취소 시 상태는 CANCEL이다.");
		assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 재고가 원복되어야 한다.");
	}
	
	
	//메서드 추출(우클릭 -> Refactor -> extract method로 생성)
	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}
	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-123"));
		em.persist(member);
		return member;
	}
	
}
