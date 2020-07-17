package com.youngxpepp.instagramcloneserver.domain.post;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.global.common.domain.AbstractBaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Post extends AbstractBaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	// TODO
	// image

	@ManyToOne
	@JoinColumn(name = "created_by")
	private Member createdBy;

	@ManyToOne
	@JoinColumn(name = "modified_by")
	private Member modifiedBy;

	public void modify(PostServiceDto.ModifyServiceRequestDto modifyServiceRequestDto) {
		this.content = modifyServiceRequestDto.getContent();
		this.modifiedBy = modifyServiceRequestDto.getModifiedBy();
	}
}
