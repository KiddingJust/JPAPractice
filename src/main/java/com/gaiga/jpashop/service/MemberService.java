package com.gaiga.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
//	@Autowired
//	public MemberService(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	}
	
	//회원 가입
	@Transactional
	public Long join(Member member) {
		//중복 회원 검증 
		validateDuplicateMember(member);
		
		memberRepository.save(member);
		return member.getId();
	}
	
	//중복회원 Exception 처리 
	private void validateDuplicateMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName());
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	//회원 전체 조회
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	
	//회원 1건 조회
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
}
