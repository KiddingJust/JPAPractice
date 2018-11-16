package org.kidding.persistence;

import org.kidding.domain.BoardVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends CrudRepository<BoardVO, Long>, QuerydslPredicateExecutor<BoardVO>{
	
	//지우고 다시 시작
	@Query("select b from BoardVO b where bno > 0 order by b.bno desc")
	public Page<BoardVO> getList(Pageable pageable);

	//제목으로 검색. 키워드 들어감. 지난번엔 containing을 썼음.
	@Query("select b from BoardVO b where b.title like %:title% and bno > 0 order by b.bno desc")
	public Page<BoardVO> getListByTitle(@Param("title") String title, Pageable pageable);
	
	//제목으로 검색. 키워드 들어감. 지난번엔 containing을 썼음.
	@Query("select b from BoardVO b where b.content like %:content% and bno > 0 order by b.bno desc")
	public Page<BoardVO> getListByContent(@Param("content") String content, Pageable pageable);

	//Writer도 똑같은 방식으로 추가하면 됨. 
	@Query("select b from BoardVO b where b.writer like %:writer% and bno > 0 order by b.bno desc")
	public Page<BoardVO> getListByWriter(@Param("writer") String writer, Pageable pageable);
	
	//문제는 복합조건. 어노테이션은 코드를 수정할 수 없다는 점이 단점. 동적으로 코드를 변경하기 어려움. 이제 어려운 조건 들어갈 것. 
	
}
