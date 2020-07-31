package com.youngxpepp.instagramcloneserver.domain.post;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.common.domain.ModelMapperUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostControllerDto {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Setter
	public static class ReadOnePostResponseDto {
		private Long id;
		private String content;
		private LocalDateTime createdAt;
		private MemberResponseDto createdBy;
		private LocalDateTime modifiedAt;

		public static ReadOnePostResponseDto of(PostServiceDto.ReadOnePostResponseDto readOnePostResponseDto) {
			return ModelMapperUtil.mapClass(readOnePostResponseDto, ReadOnePostResponseDto.class);
		}
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Setter
	public static class CreateResponseDto {
		private Long id;
		private String content;
		private LocalDateTime createdAt;
		private MemberResponseDto createdBy;
		private LocalDateTime modifiedAt;

		public static CreateResponseDto of(PostServiceDto.CreateResponseDto postDto) {
			return ModelMapperUtil.mapClass(postDto, CreateResponseDto.class);
		}
	}

	@Builder
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateRequestDto {
		private String content;

		public PostServiceDto.CreateRequestDto toServiceDto(Member member) {
			return PostServiceDto.CreateRequestDto.builder()
				.content(this.content)
				.createdBy(member)
				.build();
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ModifyRequestDto {
		private String content;

		public PostServiceDto.ModifyRequestDto toServiceDto(Long id, Member member) {
			return PostServiceDto.ModifyRequestDto.builder()
				.content(content)
				.id(id)
				.modifiedBy(member)
				.build();
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ModifyResponseDto {
		private Long id;
		private String content;
		private LocalDateTime createdAt;
		private MemberResponseDto createdBy;
		private LocalDateTime modifiedAt;

		public static ModifyResponseDto of(PostServiceDto.ModifyResponseDto modifyResponseDto) {
			return ModelMapperUtil.mapClass(modifyResponseDto, ModifyResponseDto.class);
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class DeleteResponseDto {
		private Long id;

		public static DeleteResponseDto of(PostServiceDto.DeleteResponseDto deleteResponseDto) {
			return ModelMapperUtil.mapClass(deleteResponseDto, DeleteResponseDto.class);
		}
	}
}
