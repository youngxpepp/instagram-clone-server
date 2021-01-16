package com.youngxpepp.instagramcloneserver.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.article.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>, CustomArticleRepository {

	Long countByCreatedById(Long createdById);
}
