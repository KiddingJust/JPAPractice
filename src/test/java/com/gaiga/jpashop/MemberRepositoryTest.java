package com.gaiga.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.repository.MemberRepositoryOld;

//스프링관련 테스트임을 명시 @RunWith은 이제 쓰지 않음. 
@SpringBootTest
public class MemberRepositoryTest {

	@Autowired
	MemberRepositoryOld memberRepository;
	
	@Test
	@Transactional
	public void testMember() throws Exception {
		
		//given
		Member member = new Member();
		member.setName("memberA");
		
		//when
		Long savedId = memberRepository.save(member);
		Member findMember = memberRepository.findOne(savedId);

		//then
		Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
		Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
		
		//이건? 같음(true). 같은 트랜잭션 안에서(영속성 컨텍스트 안에서)실행되어 같은 엔티티로 파악.
		//select 쿼리도 안읽어버림. 오호... 
		Assertions.assertThat(findMember).isEqualTo(member);
	}
}
