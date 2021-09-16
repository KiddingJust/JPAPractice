package com.gaiga.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.gaiga.jpashop.domain.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryOld {
	
	//스프링 부트가 EntityManager 주입해줌 
	//특정 작업을 위해 데이터베이스에 액세스 하는 역할
	
	private final EntityManager em; 
	
//	@PersistenceContext
//	private EntityManager em;
	
	public Long save(Member member) {
		//JPA가 저장하는 로직. persist 
		em.persist(member);
		return member.getId();
	}
	
	public Member findOne(Long id) {
		//멤버를 찾아서 반환 
		return em.find(Member.class, id);
	}
	
	//회원 목록 조회. --> JPQL 작성해주어야 함.
	//SQL은 테이블을 대상으로 쿼리, JPQL은 엔티티 대상으로 조회. 
	public List<Member> findAll(){
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}
	
	public List<Member> findByName(String name){
		return em.createQuery("select m from Member m where m.name = :name", Member.class)
				.setParameter("name", name)
				.getResultList();
	}
}
