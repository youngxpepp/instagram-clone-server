package com.youngxpepp.instagramcloneserver.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UnfollowRequestDto {

    @JsonProperty("member_nickname")
    @NotNull
    @NotEmpty
    private String memberNickname;
}
