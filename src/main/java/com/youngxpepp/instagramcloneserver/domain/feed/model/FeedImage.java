package com.youngxpepp.instagramcloneserver.domain.feed.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.youngxpepp.instagramcloneserver.global.common.domain.AbstractBaseTimeEntity;

@Entity(name = "feed_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FeedImage extends AbstractBaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "next_feed_image_id")
	private FeedImage nextFeedImage;

	@ManyToOne(optional = false)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	@Column(name = "uri")
	private String uri;
}
