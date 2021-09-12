package com.gaiga.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gaiga.jpashop.domain.item.Book;
import com.gaiga.jpashop.domain.item.Item;
import com.gaiga.jpashop.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	
	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "items/createItemForm";
	}
	
	@PostMapping("/items/new")
	public String create(BookForm form) {
		Book book = new Book();
		//사실 이렇게 하는 것보다는 createBook 해서 파라미터 넘기는 게 더욱 좋음.
		//Order에서 그렇게 했잖아. 그게 가장 좋은데, 이건 예제니 편하게 하려고... 
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());
		
		itemService.saveItem(book);
		return "redirect:/";
	}
	
	@GetMapping("/items")
	public String list(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "items/itemList";
	}
	
	@GetMapping("items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		//사실 캐스팅이 좋은 건 아님 ㅎ..
		Book item = (Book) itemService.findOne(itemId);
		
		//Book Entity가 아니라 BookForm을 보낼 것. 
		BookForm form = new BookForm();
		form.setId(item.getId());
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setStockQuantity(item.getStockQuantity());
		form.setAuthor(item.getAuthor());
		form.setIsbn(item.getIsbn());
		
		model.addAttribute("form", form);
		return "items/updateItemForm";
	}
	
	//어차피 form에 itemId 넘어오므로 PathVariable은 넣어줄 필요 없음. 
	@PostMapping("items/{itemId}/edit")
	public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {
		
//		Book book = new Book();
//		book.setId(form.getId());
//		book.setName(form.getName());
//		book.setPrice(form.getPrice());
//		book.setStockQuantity(form.getStockQuantity());
//		book.setAuthor(form.getAuthor());
//		book.setIsbn(form.getIsbn());		
		
		//merge로 업데이트
//		itemService.saveItem(book);
		//변경감지로 업데이트
		
		itemService.updateItem(itemId,
							form.getPrice(), 
							form.getName(), 
							form.getStockQuantity());
		
		return "redirect:/items";
	}	
}
