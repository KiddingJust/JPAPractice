package com.gaiga.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.repository.MemberRepositoryOld;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
public class MemberServiceTest {

	@Autowired MemberService memberService;
	@Autowired MemberRepositoryOld memberRepository; 
	
	@Test
	public void 회원가입() throws Exception {
		//given
		Member member = new Member();
		member.setName("kim");
		
		//when 
		//insert 로그가 안보이는 이유는, Rollback을 하기 때문에 JPA에서는 쿼리를 안날림. 
		Long savedId = memberService.join(member);
		
		//then 
		assertEquals(member, memberRepository.findOne(savedId));
	}

	@Test
	//@Test(expected = IllegalStateException.class) 해주고 밑에 try~catch 지워도 됨. 
	public void 중복_회원_예외() throws Exception {
		//given
		Member member1 = new Member();
		member1.setName("kim");
		
		Member member2 = new Member();
		member2.setName("kim");
		
		//when  
		memberService.join(member1);
		//catch를 걸면 예외가 안떨어짐.정상 처리. 
		//catch 빼면 IllegalStateException: 이미 존재하는 회원입니다. 뜸. 
		try {
			memberService.join(member2);	//에러 발생해야지. 
		}catch(IllegalStateException e) {
			return;
		}
		
		//then
		//코드가 돌다가 여기로 오면 안됨. Assert에서 제공하는 것. 음.. 
		fail("예외가 발생해야 한다.");
	}
}
