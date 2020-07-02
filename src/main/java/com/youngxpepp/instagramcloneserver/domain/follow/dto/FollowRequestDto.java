package com.youngxpepp.instagramcloneserver.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FollowRequestDto {

    @JsonProperty("member_nickname")
    @NotNull
    private String memberNickname;
}
