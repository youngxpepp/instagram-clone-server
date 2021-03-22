package com.youngxpepp.instagramcloneserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.dao.ArticleCreatedRepository;
import com.youngxpepp.instagramcloneserver.dao.ArticleRepository;
import com.youngxpepp.instagramcloneserver.dao.MemberLikeArticleRepository;
import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberLikeArticle;
import com.youngxpepp.instagramcloneserver.mapper.ArticleMapper;
import com.youngxpepp.instagramcloneserver.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.dto.GetArticleResponseBody;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import com.youngxpepp.instagramcloneserver.runnable.ArticleCreatedRelay;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final ArticleCreatedRepository articleCreatedRepository;
	private final MemberRepository memberRepository;
	private final MemberLikeArticleRepository memberLikeArticleRepository;
	private final ArticleMapper articleMapper;
	private final ArticleCreatedRelay articleCreatedRelay;

	@Transactional
	public CreateArticleResponseBody createArticle(Long memberId, CreateArticleRequestBody createArticleRequestBody) {
		Member member = memberRepository.getOne(memberId);
		Article article = articleMapper.toArticle(createArticleRequestBody, member);
		articleRepository.save(article);

		ArticleCreated articleCreated = ArticleCreated.builder()
			.article(article)
			.build();
		articleCreatedRepository.save(articleCreated);

		articleCreatedRelay.notifyEmptyCondition();

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
