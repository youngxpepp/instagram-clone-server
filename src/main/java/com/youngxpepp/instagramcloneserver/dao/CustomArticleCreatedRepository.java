package com.youngxpepp.instagramcloneserver.dao;

import java.util.List;
import java.util.Optional;

import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;

public interface CustomArticleCreatedRepository {

	long deleteAllInIds(List<Long> ids);

	Optional<ArticleCreated> findByIdWithFollowers(long id);
}
