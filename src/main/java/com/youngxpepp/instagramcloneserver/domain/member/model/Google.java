package com.youngxpepp.instagramcloneserver.domain.member.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Google")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Google extends OAuth {

	@Column(name = "key", unique = true)
	private String key;

	public Google(Member member, String key) {
		super(member);
		this.key = key;
	}
}
