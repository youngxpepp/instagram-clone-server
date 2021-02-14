package com.youngxpepp.instagramcloneserver.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.article.dto.ArticleMapper;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.GetArticleResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.article.model.MemberLikeArticle;
import com.youngxpepp.instagramcloneserver.domain.article.repository.ArticleRepository;
import com.youngxpepp.instagramcloneserver.domain.article.repository.MemberLikeArticleRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final MemberLikeArticleRepository memberLikeArticleRepository;
	private final ArticleMapper articleMapper;

	@Transactional
	public CreateArticleResponseBody createArticle(Long memberId, CreateArticleRequestBody createArticleRequestBody) {
		Member member = memberRepository.getOne(memberId);
		Article article = articleMapper.toArticle(createArticleRequestBody, member);
		articleRepository.save(article);

		return articleMapper.toCreateArticleResponseBody(article);
	}

	@Transactional
	public GetArticleResponseBody getArticle(Long memberId, Long articleId) {
		Article article = articleRepository.findByIdWithCreatedBy(articleId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		Boolean isLiked = memberLikeArticleRepository.existsByMemberIdAndArticleId(memberId, articleId);
		GetArticleResponseBody responseBody = articleMapper.toGetArticleResponseBody(article, isLiked);
		return responseBody;
	}

	@Transactional
	public void likeArticle(Long memberId, Long articleId) {
		boolean isLiked = memberLikeArticleRepository.existsByMemberIdAndArticleId(memberId, articleId);

		if (isLiked) {
			throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
		}

		MemberLikeArticle memberLikeArticle = MemberLikeArticle.builder()
			.member(memberRepository.getOne(memberId))
			.article(articleRepository.getOne(articleId))
			.build();
		memberLikeArticleRepository.save(memberLikeArticle);
	}

	@Transactional
	public void unlikeArticle(Long memberId, Long articleId) {
		MemberLikeArticle memberLikeArticle =
			memberLikeArticleRepository.findByMemberIdAndArticleId(memberId, articleId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		memberLikeArticleRepository.delete(memberLikeArticle);
	}
}
