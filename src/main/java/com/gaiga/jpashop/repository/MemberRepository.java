package com.gaiga.jpashop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaiga.jpashop.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	//select m from Member m where m.name = ?
	List<Member> findByName(String name);
}
