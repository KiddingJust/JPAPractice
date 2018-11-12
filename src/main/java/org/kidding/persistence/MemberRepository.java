package org.kidding.persistence;

import org.kidding.domain.MemberVO;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<MemberVO, String> {

	
	
}
