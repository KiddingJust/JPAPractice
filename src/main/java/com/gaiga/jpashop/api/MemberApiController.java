package com.gaiga.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gaiga.jpashop.domain.Address;
import com.gaiga.jpashop.domain.Member;
import com.gaiga.jpashop.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
	
	private final MemberService memberService;
	
	@GetMapping("/api/v1/members")
	public List<Member> membersV1(){
		return memberService.findMembers();
	}
	
	@GetMapping("/api/v2/members")
	public Result memberV2() {
		List<Member> findMembers = memberService.findMembers();
		List<MemberDto> collect = findMembers.stream()
							.map(m -> new MemberDto(m.getName(), m.getAddress()))
							.collect(Collectors.toList());
		
		return new Result(collect.size(), collect);
	}
	
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);	
		return new CreateMemberResponse(id);
	}
	
	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
		Member member = new Member();
		member.setName(request.getName());
		Long id = memberService.join(member);	
		return new CreateMemberResponse(id);
	}	
	
	@PutMapping("api/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, 
							@RequestBody @Valid UpdateMemberRequest request) {
		//변경 감지를 통한 수정.
		memberService.update(id, request.getName());
		
		Member findMember = memberService.findOne(id);
		return new UpdateMemberResponse(findMember.getId(), findMember.getName());
	}
	
	
	@Data
	@AllArgsConstructor
	static class Result<T>{
		private int count;
		//제너릭 타입
		private T data;
	}
	
	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String name;
		private Address address;
	}
	
	@Data
	static class CreateMemberRequest{
		private String name;
	}

	@Data
	static class UpdateMemberRequest{
		private String name;
	}
	
	//회원가입 하면 가입된 id값을 내린다고 가정. 
	@Data
	static class CreateMemberResponse {
		private Long id;
		
		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}

	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}
	
}
