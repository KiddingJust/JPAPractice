package com.gaiga.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaiga.jpashop.domain.item.Book;
import com.gaiga.jpashop.domain.item.Item;
import com.gaiga.jpashop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class ItemService {
	
	private final ItemRepository itemRepository;
	
	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	@Transactional
	public void updateItem(Long itemId, int price, String name, int stockQuantity ) {
		//ID 기반으로 실제 DB에서 영속성 엔티티 가져오고 
		Item findItem = itemRepository.findOne(itemId);
		findItem.setPrice(price);
		findItem.setName(name);
		findItem.setStockQuantity(stockQuantity);
		// 아래는 호출할 필요가 없음.
		// findItem 자체가 영속성 상태이므로
		// 스프링 Transactional에 의해 커밋이 되고 
		//JPA는 flush 날려서, 영속성 엔티티중 변경된 것에 update 쿼리
//		itemRepository.save(findItem);
	}
	
	public List<Item> findItems(){
		return itemRepository.findAll();
	}
	
	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
}
