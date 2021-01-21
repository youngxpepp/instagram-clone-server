package com.youngxpepp.instagramcloneserver.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.article.repository.ArticleRepository;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.dto.GetMemberResponseBody;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberMapper;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;
	private final ArticleRepository articleRepository;
	private final MemberMapper memberMapper;

	@Transactional
	public GetMemberResponseBody getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Long followerCount = followRepository.countByFollowedMemberId(member.getId());
		Long followingCount = followRepository.countByFollowingMemberId(member.getId());
		Long articleCount = articleRepository.countByCreatedById(member.getId());

		return memberMapper.toGetMemberResponseBody(member, followerCount, followingCount, articleCount);
	}

	@Transactional
	public MemberDto signup(MemberDto memberDto) {
		memberRepository.findByEmail(memberDto.getEmail())
			.ifPresent(member -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});
		memberRepository.findByNickname(memberDto.getNickname())
			.ifPresent(member -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});

		Member member = memberMapper.toMemberEntity(memberDto, MemberRole.MEMBER);
		memberRepository.save(member);

		return memberMapper.toMemberDto(member);
	}
}
