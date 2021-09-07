package com.gaiga.jpashop.jpa.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
//싱글 테이블이므로 저장 시 구분을 위함. 
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {

	private String author;
	private String isbn;
}
