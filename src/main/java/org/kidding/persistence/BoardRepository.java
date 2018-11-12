package org.kidding.persistence;

import java.util.List;

import org.kidding.domain.BoardVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<BoardVO, Long>{
	
//	public List<BoardVO> findByBnoGreaterThanOrderByBnoDesc(Long bno);
	
//	public List<BoardVO> findByBnoGreaterThan(Long bno, Pageable pageable);

	// * 를 인식하지 못하여 에러가 나는 것. 단순 게시물 처리할거면 * 대신 b로 처리하면 됨. 특정 칼럼만 가져가려면 b.bno 식으로 칼럼 지정 
	@Query("select b.bno, b.title, b.content from BoardVO b where b.bno > 0")
	public Page<BoardVO[]> getList(Pageable pageable);
	
	//게시판 만들 때 무조건 PageType만 쓰면 돼애
	public Page<BoardVO> findByBnoGreaterThan(Long bno, Pageable pageable);

	
	public List<BoardVO> findByTitleContainingAndBnoGreaterThan(String keyword, Long bno, Pageable pageable);

	
}
