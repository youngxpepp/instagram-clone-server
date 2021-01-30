package com.youngxpepp.instagramcloneserver.domain.comment.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateCommentResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.comment.model.Comment;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

	@Mapping(source = "requestBody.content", target = "content")
	@Mapping(source = "member", target = "member")
	@Mapping(source = "article", target = "article")
	@Mapping(target = "parentComment", ignore = true)
	Comment toComment(CreateCommentRequestBody requestBody, Member member, Article article);

	@Mapping(source = "comment.member", target = "createdBy")
	CreateCommentResponseBody toCreateCommentResponseBody(Comment comment);
}
