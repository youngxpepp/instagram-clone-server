package com.youngxpepp.instagramcloneserver.domain.member.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MemberMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "role", ignore = true)
	@Mapping(source = "email", target = "email")
	MemberDto toMemberDto(SignupRequestBody signupRequestBody, String email);

	Member toMemberEntity(MemberDto memberDto);

	SignupResponseBody toSignupResponseBody(Member member);
}
