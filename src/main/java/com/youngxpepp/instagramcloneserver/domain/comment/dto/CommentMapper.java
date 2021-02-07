package com.youngxpepp.instagramcloneserver.domain.comment.dto;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.comment.model.Comment;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberMapper;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

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
