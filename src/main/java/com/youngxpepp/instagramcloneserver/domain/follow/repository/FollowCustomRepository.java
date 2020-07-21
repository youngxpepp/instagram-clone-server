package com.youngxpepp.instagramcloneserver.domain.follow.repository;

public interface FollowCustomRepository {

	Long getFollowingCountByMemberNickname(String memberNickname);

	Long getFollowerCountByMemberNickname(String memberNickname);
}
