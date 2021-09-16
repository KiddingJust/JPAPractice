package com.gaiga.jpashop.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@SpringBootTest
@Transactional
public class ServiceTests {

	/*
	테스트는 우리가 작성한 코드가 주어진 요구사항을 해결할 수 있는지
	회귀를 통하여 검증할 수 있도록 돕고,
	주어진 요구사항을 테스트하므로 작성된 테스트가 문서로서 작동할 수 있으며,
	의미있는 단위의 테스트를 고민함으로써 좋은 디자인을 만드는 데에도 도움이 된다. 
	
	또한, 테스트로 보호된 코드는 나와 동료들에게 코드의 변경을
	거침없이 시도할 수 있도록 안정감과 자신감을 준다. 
	
	잘못된 테스트는 구현이 조금만 변경되어도 코드가 손상되고,
	무엇을 어떻게 테스트하는지를 확인하기 위해 시간을 많이 쏟아야 한다.
	
	1. 테스트명
	굳이 명사일 필요 없다. 그냥 한글로 동사로 써도 된다. 
	2. 테스트 간의 관계
	테스트는 상호 독립적으로 작성한다.
	테스트를 추가, 제거, 수정할 때 작성되어있는 다른 테스트의 배경과 내용을 이해할 필요 없다. 
	--> 예시로, static Wallet wallet = new Wallet(); 으로
	    모든 메서드가 접근할 수 있도록 지정하지 않음. 상태 공유하므로..
	3. 테스트의 구성 요소
	조건 -> 행위 -> 검증. 즉 given when then 으로. 
	4. 테스트의 구성요소의 위치
	구성 요소(조건, 행위, 검증)는 테스트 안에 작성한다 
	
	 */
	

}
