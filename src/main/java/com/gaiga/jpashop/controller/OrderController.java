package com.gaiga.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.domain.Order;
import com.gaiga.jpashop.domain.item.Item;
import com.gaiga.jpashop.repository.OrderSearch;
import com.gaiga.jpashop.service.ItemService;
import com.gaiga.jpashop.service.MemberService;
import com.gaiga.jpashop.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

	//고객 및 상품을 모두 선택할 수 있어야 함.
	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;
	
	@GetMapping("/order")
	public String createForm(Model model) {
		
		List<Member> members = memberService.findMembers();
		List<Item> items = itemService.findItems();
		
		model.addAttribute("members", members);
		model.addAttribute("items", items);
		
		return "order/orderForm";
	}
	
	@PostMapping("/order")
	public String order(@RequestParam("memberId") Long memberId,
						@RequestParam("itemId") Long itemId,
						@RequestParam("count") int count) {
		orderService.order(memberId, itemId, count);
		
		//주문내역목록으로 이동하도록 redirect
		//핵심 비즈니스 로직의 경우, 서비스나 엔티티에서 처리해주는 편이 좋음. 
		//영속성 컨텍스트 안에 들어가야 다른 엔티티도 쉽게 변경해줄 수 있음. 
		return "redirect:/orders";
	}
	
	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch,
							Model model) {
		List<Order> orders = orderService.findOrders(orderSearch);
		model.addAttribute("orders", orders);
		
		return "order/orderList";
	}
	
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		orderService.cancelOrder(orderId);;
		return "redirect:/orders";
	}
}
