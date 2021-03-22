package com.youngxpepp.instagramcloneserver.dao;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.test.RepositoryTest;

public class CustomArticleCreatedRepositoryImplTest extends RepositoryTest {

	@Autowired
	private ArticleCreatedRepository articleCreatedRepository;

	@ParameterizedTest
	@ValueSource(longs = {0, 1, 2, 3, 4, 5})
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deleteAllInIds(long size) {
		// given
		List<ArticleCreated> acs = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			ArticleCreated ac = ArticleCreated.builder().build();
			jpaTestSupport.save(ac);
			acs.add(ac);
		}

		// when
		long result = articleCreatedRepository.deleteAllInIds(
			acs.stream().map(ArticleCreated::getId).collect(Collectors.toList()));

		// then
		then(result).isEqualTo(size);
	}
}
