package com.gaiga.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.gaiga.jpashop.domain.item.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {

	@Id @GeneratedValue
	@Column(name = "category_id")
	private Long id;
	
	private String name;
	
	@ManyToMany
	@JoinTable(name = "category_item",
				joinColumns = @JoinColumn(name = "category_id"),
				inverseJoinColumns = @JoinColumn(name = "item_id"))
	private List<Item> items = new ArrayList<>();
	
	//내 부모가 나?
	//같은 엔티티에 대해 양방향 연관관계 걸어둔 것. 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	//내 자식은?
	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<>();
	
	//child를 넣으면 부모와 자식 모두에게 들어가야 함. 
	public void addChildCategory(Category child) {
		this.child.add(child);
		child.setParent(this);
	}
}
