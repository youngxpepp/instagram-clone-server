package com.youngxpepp.instagramcloneserver.dao;

import java.util.Optional;

import com.youngxpepp.instagramcloneserver.domain.Article;

public interface CustomArticleRepository {

	Optional<Article> findByIdWithCreatedBy(long id);

	Optional<Article> findByIdWithFollowers(long id);
}
