package com.youngxpepp.instagramcloneserver.domain.feed.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.common.domain.AbstractBaseTimeEntity;

@Entity(name = "member_like_feed")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberLikeFeed extends AbstractBaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(optional = false)
	@JoinColumn(name = "feed_id")
	private Feed feed;
}
