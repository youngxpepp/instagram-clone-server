package com.youngxpepp.instagramcloneserver.domain.follow.service;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FollowService {

    private FollowRepository followRepository;
    private MemberRepository memberRepository;

    @Transactional
    public void follow(Member fromMember, FollowRequestDto dto) throws BusinessException {
        Member toMember = memberRepository.findByNickname(dto.getMemberNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        followRepository.findByFromMemberIdAndToMemberId(fromMember.getId(), toMember.getId())
                .ifPresent(follow -> {
                    throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
                });

        Follow follow = Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();

        followRepository.save(follow);
    }
}
