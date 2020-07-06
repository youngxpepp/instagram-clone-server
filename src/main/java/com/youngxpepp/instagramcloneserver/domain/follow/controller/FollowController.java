package com.youngxpepp.instagramcloneserver.domain.follow.controller;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.dto.UnfollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.service.FollowService;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.PostJwtAuthenticationToken;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/follows")
@AllArgsConstructor
@Slf4j
public class FollowController {

    private FollowService followService;

    @PostMapping
    public void follow(@RequestBody @Valid FollowRequestDto dto) throws BusinessException {
        PostJwtAuthenticationToken postJwtAuthenticationToken = (PostJwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        followService.follow(postJwtAuthenticationToken.getMember(), dto);
    }

    @DeleteMapping
    public void unfollow(@RequestBody @Valid UnfollowRequestDto dto) {
        PostJwtAuthenticationToken postJwtAuthenticationToken = (PostJwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        followService.unfollow(postJwtAuthenticationToken.getMember(), dto);
    }
}
