package com.youngxpepp.instagramcloneserver.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.youngxpepp.instagramcloneserver.domain.Article;
import com.youngxpepp.instagramcloneserver.domain.ArticleImage;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.dto.CreateArticleRequestBody;
import com.youngxpepp.instagramcloneserver.dto.CreateArticleResponseBody;
import com.youngxpepp.instagramcloneserver.dto.GetArticleResponseBody;

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

	@Mapping(source = "article.articleImages", target = "imageUrls")
	@Mapping(source = "isLiked", target = "isLiked")
	GetArticleResponseBody toGetArticleResponseBody(Article article, Boolean isLiked);
}
