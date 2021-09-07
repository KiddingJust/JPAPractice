package com.gaiga.jpashop.jpa;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

	@Id @GeneratedValue
	@Column(name = "devliery_id")
	private Long id;
	
	@OneToOne(mappedBy = "delivery", 
			fetch = FetchType.LAZY)
	private Order order;
	
	@Embedded
	private Address address;
	
	//ORDINAL로 하면, 1,2,3,4 숫자로 들어감
	//Default가 ORDINAL인데, 이런 경우 STRING 들어가면 에러 발생
	//그래서 String으로 지정하는편이 맞음. 
	@Enumerated(EnumType.STRING)	
	private DeliveryStatus status; //Ready, Comp(배송)
}
