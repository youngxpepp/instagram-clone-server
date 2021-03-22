package com.youngxpepp.instagramcloneserver.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends AbstractBaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "nickname", unique = true)
	private String nickname;

	@Column(name = "name")
	private String name;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(name = "description")
	private String description;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "followedMember")
	private List<Follow> followers = new ArrayList<>();

	@Builder
	public Member(String email, String nickname, String name, String profileImageUrl, String description,
		MemberRole role) {
		this.email = email;
		this.nickname = nickname;
		this.name = name;
		this.profileImageUrl = profileImageUrl;
		this.description = description;
		this.role = role;
	}
}
