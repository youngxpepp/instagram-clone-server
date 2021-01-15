package com.youngxpepp.instagramcloneserver.domain.article.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class CreateArticleRequestBody {

	@NotEmpty
	private String content;

	@NotEmpty
	@Size(min = 1, max = 9)
	private List<@URL String> imageUrls;
}
