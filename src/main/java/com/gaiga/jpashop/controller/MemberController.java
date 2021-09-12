package com.gaiga.jpashop.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gaiga.jpashop.domain.Address;
import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping("/members/new")
	//컨트롤러가 화면으로 이동할 때, memberform이라는 껍데기 객체를 가지고 감
	//Validation 등을 위해 빈껍데기라도 가져가는 것. 
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberForm());
		return "members/createMemberForm";
	}
	
	@PostMapping("/members/new")
	public String create(@Valid MemberForm memberForm, BindingResult result) {
		
		//아래와 같이 해주면 MemberForm.java에서 설정해둔 에러메세지가 뜬다.
		//html을 보면 fields.hasErrors 를 통해 에러메세지를 뽑아서 출력. 
		if(result.hasErrors()) {
			return "members/createMemberForm";
		}
		
		Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
		
		Member member = new Member();
		member.setName(memberForm.getName());
		member.setAddress(address);
		
		memberService.join(member);
		//첫번째 페이지인 home.html으로 리다이렉트. 
		return "redirect:/";
	}
	
	@GetMapping("/members")
	public String list(Model model) {
		//사실 Entity를 그대로 쓰는 것은 추천하지 않음. 
		//간단하니까 여기서는..ㅎ..  template 엔진에서 렌더링 때는 뭐.. 서버 안에서만 도니까 괜찮
		
		List<Member> members = memberService.findMembers();
		model.addAttribute("members", members);
		return "members/memberList";
	}
}
