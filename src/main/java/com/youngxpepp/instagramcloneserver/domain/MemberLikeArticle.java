package com.youngxpepp.instagramcloneserver.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_like_article", uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "article_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberLikeArticle extends AbstractBaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "article_id")
	private Article article;

	@Builder
	public MemberLikeArticle(Member member, Article article) {
		this.member = member;
		this.article = article;
	}
}
