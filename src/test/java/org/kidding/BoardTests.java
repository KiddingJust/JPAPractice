package org.kidding;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidding.domain.BoardVO;
import org.kidding.persistence.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class BoardTests {

	
	@Setter(onMethod_=@Autowired)
	private BoardRepository boardRepository;
	
	
	@Test
	public void testQ1() {
		
		
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		
		Page<BoardVO[]> result = boardRepository.getList(pageable);
		
		log.info(""+result);
		
		log.info("TOTAL PAGES: " + result.getTotalPages());
		log.info("PAGE: " + result.getNumber());
		log.info("NEXT: " + result.hasNext());
		log.info("PREV: " + result.hasPrevious());
		
		log.info("P NEXT: " + result.nextPageable());
		log.info("P PREV: " + result.previousPageable());
		
		//여기서 에러 발생. 
		result.getContent().forEach(vo -> log.info("" + Arrays.toString(vo)));
	}
	
	@Test
	public void testInsert() {
		IntStream.range(200, 1000).forEach(i -> {
			BoardVO vo = new BoardVO();
			vo.setTitle("게시물" + i);
			vo.setContent("이상해" + i);
			vo.setWriter("가아익" + (i % 10));
			
			boardRepository.save(vo);
		});
	}
	
	@Test
	public void testRead(){
		
		Optional<BoardVO> result = boardRepository.findById(10L);
		
		//NullPointerException 처리. 이게 안 걸리면 Null인거지. if null ~ 코드 필요 X
		result.ifPresent( vo -> log.info("" + vo));
		
	}
	
	//수정 (10번 글 수정)
	@Test
	public void testUpdate() {
		//10번 글 가져오기
//		boardRepository.findById(10L).ifPresent(vo -> {
			
//			vo.setContent("수정된 제목 입니다.");
//			boardRepository.save(vo);
			
//		});
		//findById 없이 update
		BoardVO vo = new BoardVO();
		vo.setBno(10L);
		vo.setTitle("제목 10 수정");
		vo.setContent("내용 10 수정");
		vo.setWriter("user10");
		
		//원래는 save만 쓰는 건 아닌데, 스프링에서는 Hibernate에서 알아서 처리. persist 할지 find 할지?
		//
		boardRepository.save(vo);
	}
	
	@Test
	public void testDelete() {
		boardRepository.deleteById(10L);
	}
	
	@Test
	public void testFind1() {
		
		//new PageRequest()는 deprecated된 방식으로 build up 패턴을 이용해야 함.
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		
		//count 쿼리까지 같이 날아감 오우. 
		Page<BoardVO> result = boardRepository.findByBnoGreaterThan(0L, pageable);
		
		log.info(""+result);
		log.info("TOTAL PAGES: " + result.getTotalPages());
		log.info("PAGE: " + result.getNumber());
		log.info("NEXT: " + result.hasNext());
		log.info("PREV: " + result.hasPrevious());
		
		log.info("P NEXT: " + result.nextPageable());
		log.info("P PREV: " + result.previousPageable());
		
		result.getContent().forEach(vo -> log.info(""+vo));
	}
	
//	@Test
//	public void testFind2() {
//		
//		Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "bno");
//
//		boardRepository.findByTitleContainingOrderByBnoDesc("2").forEach(vo -> log.info("" +vo));
//	}
	
	@Test
    public void testFind4() {
        
        //0은 페이지 번호, 10은 10개씩
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC,"bno");
        
        //메소드이름에 따라 쿼리문 실행이 된다.
        boardRepository.findByTitleContainingAndBnoGreaterThan("7",0L,pageable).forEach(vo -> log.info(""+vo));
        
    }
}
