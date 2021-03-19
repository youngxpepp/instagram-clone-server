package com.youngxpepp.instagramcloneserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>, CustomArticleRepository {

	Long countByCreatedById(Long createdById);
}
