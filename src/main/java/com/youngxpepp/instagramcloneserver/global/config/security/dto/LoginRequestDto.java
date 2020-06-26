package com.youngxpepp.instagramcloneserver.global.config.security.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoginRequestDto {

    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String password;
}
