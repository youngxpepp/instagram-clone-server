package com.youngxpepp.instagramcloneserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member_like_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberLikeComment extends AbstractBaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(optional = false)
	@JoinColumn(name = "comment_id")
	private Comment comment;

	@Builder
	public MemberLikeComment(Member member, Comment comment) {
		this.member = member;
		this.comment = comment;
	}
}
