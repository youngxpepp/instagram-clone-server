package com.youngxpepp.instagramcloneserver.domain.member.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MemberMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "role", ignore = true)
	@Mapping(source = "email", target = "email")
	MemberDto toMemberDto(SignupRequestParam signupRequestParam, String email);

	Member toMemberEntity(MemberDto memberDto);

	@Mapping(source = "role", target = "role")
	Member toMemberEntity(MemberDto memberDto, MemberRole role);

	SignupResponseBody toSignupResponseBody(Member member);

	@Mapping(source = "followerCount", target = "followerCount")
	@Mapping(source = "followingCount", target = "followingCount")
	@Mapping(source = "articleCount", target = "articleCount")
	GetMemberResponseBody toGetMemberResponseBody(Member member, Long followerCount, Long followingCount,
		Long articleCount);
}
