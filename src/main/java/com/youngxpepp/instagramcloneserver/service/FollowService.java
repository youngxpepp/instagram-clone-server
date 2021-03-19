package com.youngxpepp.instagramcloneserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.dto.FollowDto;
import com.youngxpepp.instagramcloneserver.mapper.FollowMapper;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.dao.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final MemberRepository memberRepository;
	private final FollowMapper followMapper;


	@Transactional
	public FollowDto follow(Long followingMemberId, Long followedMemberId) {
		Member followingMember = memberRepository.findById(followingMemberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		Member followedMember = memberRepository.findById(followedMemberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		followRepository.findByFollowingMemberIdAndFollowedMemberId(followingMember.getId(), followedMember.getId())
			.ifPresent(follow -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});

		Follow follow = Follow.builder()
			.followingMember(followingMember)
			.followedMember(followedMember)
			.build();

		followRepository.save(follow);

		return followMapper.toFollowDto(follow);
	}

	@Transactional
	public void unfollow(Long followingMemberId, Long followId) {
		Follow follow = followRepository.findById(followId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (!follow.getFollowingMember().getId().equals(followingMemberId)) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		followRepository.delete(follow);
	}
}
