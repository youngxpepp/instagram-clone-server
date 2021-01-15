package com.youngxpepp.instagramcloneserver.domain.article.dto;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.article.model.Article;
import com.youngxpepp.instagramcloneserver.domain.article.model.ArticleImage;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ArticleMapper {

	@Mapping(source = "url", target = "url")
	@Mapping(target = "article", ignore = true)
	ArticleImage toArticleImage(String url);

	List<ArticleImage> toArticleImageList(List<String> imageUrls);

	@Mapping(source = "createdBy", target = "createdBy")
	@Mapping(source = "createArticleRequestBody.imageUrls", target = "articleImages")
	Article toArticle(CreateArticleRequestBody createArticleRequestBody, Member createdBy);

	default String toImageUrl(ArticleImage articleImage) {
		return articleImage.getUrl();
	}

	List<String> toImageUrls(List<ArticleImage> articleImages);

	@Mapping(source = "articleImages", target = "imageUrls")
	CreateArticleResponseBody toCreateArticleResponseBody(Article article);
}
