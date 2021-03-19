package com.youngxpepp.instagramcloneserver.dao;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.Comment;
import com.youngxpepp.instagramcloneserver.test.RepositoryTest;

public class CommentRepositoryTest extends RepositoryTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CommentRepository commentRepository;

	@Test
	@DisplayName("Given_15개의 댓글_When_findAllByArticleId_Then_10개의 댓글 불러오기 완료")
	public void findAllByArticleIdThen10Comments() {
		// given
		em.persist(principal);

		Article article = Article.builder()
			.content("example article")
			.createdBy(principal)
			.build();
		em.persist(article);

		List<Comment> comments = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			comments.add(
				Comment.builder()
					.content("example comment")
					.createdBy(principal)
					.article(article)
					.build()
			);
			em.persist(comments.get(i));
		}

		// when
		List<Comment> resultComments = commentRepository.findAllByArticleId(article.getId(),
			PageRequest.of(0, 10));

		// then
		then(resultComments).hasSize(10);

		for (int i = 0; i < 10; i++) {
			then(resultComments.get(i).getId()).isEqualTo(comments.get(i).getId());
		}
	}
}
