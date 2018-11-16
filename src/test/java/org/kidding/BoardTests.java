package org.kidding;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidding.domain.BoardVO;
import org.kidding.domain.QBoardVO;
import org.kidding.persistence.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.Setter;
import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class BoardTests {

	
	@Setter(onMethod_=@Autowired)
	private BoardRepository boardRepository;
	
	@Test
	public void testDynamic() {
		
//		String type="t";
//		String keyword = "10";
//		
//		//조건문에 맞는지 안맞는지 체크하므로 Boolean?
//		BooleanBuilder builder = new BooleanBuilder();
//		
//		//조건문 추가
//		QBoardVO board = QBoardVO.boardVO;
//		if(type.equals("t")) {
//			
//			//builder에 구문 추가. 해당 expression을 추가. 
//			builder.and(
//			board.title.contains(keyword)
//			)
//			//조건 추가. bno가 0보다 큰 경우. 
//			.and(board.bno.gt(0));
//		}
//		
//		Page<BoardVO> result = 
//				boardRepository.findAll(builder, PageRequest.of(0,  10));
//		
//		log.info("" + result);
		//조건문 추가
		
		
		String[]  types = {"t", "c"};
		String keyword = "10";
		
		//이렇게 하면 안됨. 다시. 
//		BooleanBuilder builder = new BooleanBuilder();
//		QBoardVO board = QBoardVO.boardVO;
//		
//		for(int i = 0; i < types.length; i++) {
//			if(types[i].equals("t")) {
//				builder.and(board.title.contains(keyword));
//			}else if(types[i].equals("c")) {
//				builder.and(board.content.contains(keyword));
//			}
//		}
		
		BooleanBuilder builder = new BooleanBuilder();
		QBoardVO board = QBoardVO.boardVO;

		builder.and(board.bno.gt(0));
		
		BooleanExpression[] arr = new BooleanExpression[types.length];
		
		for(int i = 0; i < types.length; i++) {
			
			String type = types[i];
			
			BooleanExpression cond = null;
			
			if(type.equals("t")) {
				cond = board.title.contains(keyword);
			}else if(type.equals("c")) {
				cond = board.content.contains(keyword);
				}
			
				arr[i] = cond;
			}
		
		//이중에 하나라도? andAnyOf이 or 조건을 붙임. 내부에는 당연히 배열 형태가 들어가야 함. 
		builder.andAnyOf(arr);
		
		Page<BoardVO> result = 
				boardRepository.findAll(builder, PageRequest.of(0,  10, Sort.Direction.DESC, "bno"));
		
		log.info("" + result);
		
		
	}
	
	@Test
	public void testWriter() {
		
		Page<BoardVO> result
		= boardRepository.getListByWriter("가아익", PageRequest.of(0, 10));
	
		log.info("" + result);
		
		result.getContent().forEach(vo -> log.info("" + vo));
		
	}
	
	@Test
	public void testContent() {
		
		Page<BoardVO> result
		= boardRepository.getListByContent("이상해", PageRequest.of(0, 10));
	
		log.info("" + result);
		
		result.getContent().forEach(vo -> log.info("" + vo));
		
	}
	
	@Test
	public void testTitle() {
		
		Page<BoardVO> result
		= boardRepository.getListByTitle("10", PageRequest.of(0, 10));
	
		log.info("" + result);
		
		result.getContent().forEach(vo -> log.info("" + vo));
		
	}
	
	@Test
	public void testList() {
		Page<BoardVO> result
			= boardRepository.getList(PageRequest.of(0, 10));
		
		log.info("" + result);
	}
	
//	@Test
//	public void testQ1() {
//		
//		
//		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
//		//아까 못한 부분. BoardVO가 아니라 Object[]로 바꾸어야함 ㅜㅜ 
//		Page<Object[]> result = boardRepository.getList(pageable);
//		
//		log.info(""+result);
//		
//		log.info("TOTAL PAGES: " + result.getTotalPages());
//		log.info("PAGE: " + result.getNumber());
//		log.info("NEXT: " + result.hasNext());
//		log.info("PREV: " + result.hasPrevious());
//		
//		log.info("P NEXT: " + result.nextPageable());
//		log.info("P PREV: " + result.previousPageable());
//		
//		//여기서 에러 발생. 
//		result.getContent().forEach(vo -> log.info("" + Arrays.toString(vo)));
//	}
	
	
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

}
