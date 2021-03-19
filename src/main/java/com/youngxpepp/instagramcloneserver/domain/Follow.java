package com.youngxpepp.instagramcloneserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"following_member_id", "followed_member_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Follow extends AbstractBaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "following_member_id")
	private Member followingMember;

	@ManyToOne(optional = false)
	@JoinColumn(name = "followed_member_id")
	private Member followedMember;

	@Builder
	public Follow(Member followingMember, Member followedMember) {
		this.followingMember = followingMember;
		this.followedMember = followedMember;
	}
}
