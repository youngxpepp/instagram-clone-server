package com.youngxpepp.instagramcloneserver.domain.member.dto;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.common.domain.ModelMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class MemberResponseDto {
	private Long id;
	private String nickname;

	public static MemberResponseDto of(Member member) {
		return ModelMapperUtil.mapClass(member, MemberResponseDto.class);
	}

}
