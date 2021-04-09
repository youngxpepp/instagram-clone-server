package com.youngxpepp.instagramcloneserver.dao;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.test.RepositoryTest;

public class ArticleRepositoryTest extends RepositoryTest {

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	public void findByIdWithFollowersThenReturnsNull() {
		// given

		// when
		Optional<Article> result = articleRepository.findByIdWithFollowers(1);

		// then
		then(result).isNotPresent();
	}

	@Test
	public void findByIdWithFollowersThenNoFollowers() {
		// given
		em.persist(principal);

		Article article0 = Article.builder()
			.createdBy(principal)
			.build();
		em.persist(article0);

		em.flush();
		em.clear();

		// when
		Article result = articleRepository.findByIdWithFollowers(principal.getId())
			.orElse(null);

		// then
		then(result.getId()).isEqualTo(article0.getId());
		then(result.getCreatedBy().getId()).isEqualTo(principal.getId());
		then(result.getCreatedBy().getFollowers()).hasSize(0);
	}

	@Test
	public void findByIdWithFollowersThenWithFollowers() {
		// given
		em.persist(principal);

		int followerSize = 3;
		List<Member> followers = new ArrayList<>();
		List<Follow> follows = new ArrayList<>();
		for (int i = 0; i < followerSize; i++) {
			Member follower = Member.builder()
				.build();
			followers.add(follower);
			em.persist(follower);
			Follow follow = Follow.builder()
				.followingMember(follower)
				.followedMember(principal)
				.build();
			follows.add(follow);
			em.persist(follow);
		}

		Article article0 = Article.builder()
			.createdBy(principal)
			.build();
		em.persist(article0);

		em.flush();
		em.clear();

		// when
		Article result = articleRepository.findByIdWithFollowers(principal.getId())
			.orElse(null);

		// then
		then(result.getId()).isEqualTo(article0.getId());
		then(result.getCreatedBy().getId()).isEqualTo(principal.getId());
		then(result.getCreatedBy().getFollowers()).hasSize(followerSize);
	}
}
