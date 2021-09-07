package com.gaiga.jpashop.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
	
	//스프링 부트가 EntityManager 주입해줌 
	//특정 작업을 위해 데이터베이스에 액세스 하는 역할
	@PersistenceContext
	private EntityManager em;
	
	public Long save(Member member) {
		em.persist(member);
		return member.getId();
	}
	
	public Member find(Long id) {
		return em.find(Member.class, id);
	}
}
