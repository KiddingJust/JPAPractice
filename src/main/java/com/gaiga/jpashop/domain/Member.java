package com.gaiga.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {
	
	@Id @GeneratedValue
	@Column(name="member_id")
	private Long id;
	
	@NotEmpty
	private String name;
	
	//내장타입을 포함했음을 의미. 
	@Embedded
	private Address address;
	
	//Order와 일대다 관계 - 한명이 여러개 주문
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();
}
