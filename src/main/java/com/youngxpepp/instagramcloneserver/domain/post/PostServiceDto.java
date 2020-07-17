package com.youngxpepp.instagramcloneserver.domain.post;

import java.time.LocalDateTime;

import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.common.domain.ModelMapperUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO image
public class PostServiceDto {

	private PostServiceDto() {
		// blank
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class CreateServiceRequestDto {
		private String content;
		private Member createdBy;

		public Post toEntity() {
			return ModelMapperUtil.mapClass(this, Post.class);
		}
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class ServiceResponseDto {
		private Long id;
		private String content;
		private MemberResponseDto createdBy;
		private MemberResponseDto updatedBy;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;

		public static ServiceResponseDto of(Post post) {
			return ServiceResponseDto.builder()
				.content(post.getContent())
				.id(post.getId())
				.createdAt(post.getCreatedAt())
				.modifiedAt(post.getModifiedAt())
				.createdBy(MemberResponseDto.of(post.getCreatedBy()))
				.updatedBy(MemberResponseDto.of(post.getModifiedBy()))
				.build();
		}
	}
}