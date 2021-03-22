package com.youngxpepp.instagramcloneserver.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.dto.CreatedByDto;
import com.youngxpepp.instagramcloneserver.dto.GetMemberResponseBody;
import com.youngxpepp.instagramcloneserver.dto.MemberDto;
import com.youngxpepp.instagramcloneserver.dto.SignupRequestParam;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MemberMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "role", ignore = true)
	@Mapping(source = "email", target = "email")
	MemberDto toMemberDto(SignupRequestParam signupRequestParam, String email);

	MemberDto toMemberDto(Member member);

	@Mapping(source = "role", target = "role")
	@Mapping(target = "followers", ignore = true)
	Member toMemberEntity(MemberDto memberDto, MemberRole role);

	@Mapping(source = "followerCount", target = "followerCount")
	@Mapping(source = "followingCount", target = "followingCount")
	@Mapping(source = "articleCount", target = "articleCount")
	GetMemberResponseBody toGetMemberResponseBody(Member member, Long followerCount, Long followingCount,
		Long articleCount);

	CreatedByDto toCreatedByDto(Member member);
}
