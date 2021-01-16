package com.youngxpepp.instagramcloneserver.domain.article.repository;

import java.util.Optional;

import com.youngxpepp.instagramcloneserver.domain.article.model.Article;

public interface CustomArticleRepository {

	Optional<Article> findByIdWithCreatedBy(Long id);
}
