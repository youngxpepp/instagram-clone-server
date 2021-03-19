package com.youngxpepp.instagramcloneserver.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.Comment;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.dto.CommentDto;
import com.youngxpepp.instagramcloneserver.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.dto.CreateCommentResponseBody;

@Mapper(
	uses = {MemberMapper.class},
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

	@Mapping(source = "requestBody.content", target = "content")
	@Mapping(source = "createdBy", target = "createdBy")
	@Mapping(source = "article", target = "article")
	@Mapping(target = "parentComment", ignore = true)
	@Mapping(target = "nestedComments", ignore = true)
	Comment toComment(CreateCommentRequestBody requestBody, Member createdBy, Article article);

	CreateCommentResponseBody toCreateCommentResponseBody(Comment comment);

	CommentDto toCommentDto(Comment comment);

	List<CommentDto> toCommentDtoList(List<Comment> comments);
}
