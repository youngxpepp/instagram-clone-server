package com.youngxpepp.instagramcloneserver.domain.follow.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FollowMapper {

	FollowDto toFollowDto(Follow follow);
}
