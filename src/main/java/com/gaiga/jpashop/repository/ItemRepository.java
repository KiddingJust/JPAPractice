package com.gaiga.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.gaiga.jpashop.domain.item.Item;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

	private final EntityManager em;
	
	public void save(Item item) {
		//Item은 JPA에 저장하기 전까지 ID값이 없음. 즉 완전히 새로 생성한 객체
		if(item.getId() == null) {
			em.persist(item);
		//이미 DB에 저장된 것을 어디선가 가져오는 것.
		}else {
			em.merge(item);
		}
	}
	
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll(){
		return em.createQuery("select i from Item i", Item.class)
				.getResultList();
	}
}


