package com.youngxpepp.instagramcloneserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;

public interface ArticleCreatedRepository extends JpaRepository<ArticleCreated, Long>, CustomArticleCreatedRepository {
}
