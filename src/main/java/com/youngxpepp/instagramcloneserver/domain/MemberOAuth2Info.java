package com.youngxpepp.instagramcloneserver.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "member_oauth2_info",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uix_oauth2_name_attribute_oauth2_registration_id",
			columnNames = {"oauth2_name_attribute", "oauth2_registration_id"}
		)
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberOAuth2Info extends AbstractBaseTimeEntity {

	@Id
	private Long id;

	@Column(name = "oauth2_name_attribute")
	private String name;

	@Column(name = "oauth2_registration_id")
	private String registrationId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	public MemberOAuth2Info(String name, String registrationId,
		Member member) {
		this.name = name;
		this.registrationId = registrationId;
		this.member = member;
	}
}
