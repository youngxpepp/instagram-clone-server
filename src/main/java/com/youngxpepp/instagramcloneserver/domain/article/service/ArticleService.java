package com.youngxpepp.instagramcloneserver.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.article.dto.ArticleMapper;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.domain.article.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.article.repository.ArticleRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final ArticleMapper articleMapper;

	@Transactional
	public CreateArticleResponseBody createArticle(Long memberId, CreateArticleRequestBody createArticleRequestBody) {
		Member member = memberRepository.getOne(memberId);
		Article article = articleMapper.toArticle(createArticleRequestBody, member);
		articleRepository.save(article);

		return articleMapper.toCreateArticleResponseBody(article);
	}
}
