package com.youngxpepp.instagramcloneserver.event;

import org.springframework.context.ApplicationEvent;

public class ArticleCreatedEvent extends ApplicationEvent {

	private long articleId;

	public ArticleCreatedEvent(Object source, long articleId) {
		super(source);
		this.articleId = articleId;
	}

	public Long getArticleId() {
		return articleId;
	}
}
