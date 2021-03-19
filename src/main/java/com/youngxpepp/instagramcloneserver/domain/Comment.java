package com.youngxpepp.instagramcloneserver.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends AbstractBaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content")
	private String content;

	@ManyToOne
	@JoinColumn(name = "parent_comment_id")
	private Comment parentComment;

	@OneToMany(mappedBy = "parentComment")
	private List<Comment> nestedComments = new ArrayList<>();

	@ManyToOne(optional = false)
	@JoinColumn(name = "member_id")
	private Member createdBy;

	@ManyToOne(optional = false)
	@JoinColumn(name = "article_id")
	private Article article;

	@Builder
	public Comment(String content, Comment parentComment, Member createdBy, Article article) {
		this.content = content;
		this.parentComment = parentComment;
		this.createdBy = createdBy;
		this.article = article;
	}
}
