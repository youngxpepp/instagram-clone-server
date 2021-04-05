package com.youngxpepp.instagramcloneserver.dao;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.domain.ArticleImage;
import com.youngxpepp.instagramcloneserver.domain.Follow;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
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
			acs.add(ac);
		}
		jpaTestSupport.saveAll(acs);

		// when
		long result = articleCreatedRepository.deleteAllInIds(
			acs.stream().map(ArticleCreated::getId).collect(Collectors.toList()));

		// then
		then(result).isEqualTo(size);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void findByIdWithFollowersThenWithFollowers() {
		// given
		jpaTestSupport.save(principal);

		List<Member> followers = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Member follower = Member.builder()
				.email(i + "@gmail.com")
				.nickname("follower" + String.valueOf(i))
				.name("name" + String.valueOf(i))
				.profileImageUrl("http://localhost:8080")
				.description("description" + String.valueOf(i))
				.role(MemberRole.MEMBER)
				.build();
			followers.add(follower);
		}
		jpaTestSupport.saveAll(followers);

		List<Follow> follows = new ArrayList<>();
		for (Member m : followers) {
			Follow follow = Follow.builder()
				.followingMember(m)
				.followedMember(principal)
				.build();
			follows.add(follow);
		}
		jpaTestSupport.saveAll(follows);

		ArticleImage articleImage = ArticleImage.builder()
			.url("https://localhost:8080")
			.build();
		Article article = Article.builder()
			.content("daily")
			.createdBy(principal)
			.articleImages(Arrays.asList(articleImage))
			.build();
		jpaTestSupport.save(article);

		ArticleCreated ac = ArticleCreated.builder()
			.article(article)
			.build();
		jpaTestSupport.save(ac);

		// when
		ArticleCreated result = articleCreatedRepository.findByIdWithFollowers(ac.getId())
			.orElse(null);

		// then
		then(result).isNotNull();
		then(result.getArticle().getId()).isEqualTo(article.getId());
		then(result.getArticle().getCreatedBy().getId()).isEqualTo(principal.getId());
		then(result.getArticle().getCreatedBy().getFollowers()).hasSize(3);
	}

	@Test
	public void findByIdWithFollowersThenReturnsNull() {
		// given
		// when
		ArticleCreated result = articleCreatedRepository.findByIdWithFollowers(1)
			.orElse(null);

		// then
		then(result).isNull();
	}
}
