package org.kidding;

import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidding.domain.MemberVO;
import org.kidding.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log	//log4j 가 없숴 
public class MemberRepositoryTests {
	
	@Setter(onMethod_=@Autowired)
	private MemberRepository repo;
	
	@Test
	public void readTest() {
		repo.findById("user50").ifPresent(vo -> log.info(""+vo));
	}
	
	@Test
	public void insertTest() {
		
		IntStream.range(0, 100).forEach(i -> {
			
			repo.save(
			MemberVO.builder()
			.mid("user" + i)
			.mpw("pw" + i)
			.mname("사용자" + i)
			.build());
		});
	}
}
