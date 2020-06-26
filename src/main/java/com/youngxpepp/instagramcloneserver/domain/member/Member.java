package com.youngxpepp.instagramcloneserver.domain.member;

import com.youngxpepp.instagramcloneserver.global.common.domain.AbstractBaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends AbstractBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String nickname, String name, String email, String password, MemberRole role) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
