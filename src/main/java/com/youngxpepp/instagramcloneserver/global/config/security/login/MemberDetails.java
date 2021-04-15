package com.youngxpepp.instagramcloneserver.global.config.security.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.youngxpepp.instagramcloneserver.domain.Member;

public class MemberDetails implements UserDetails, CredentialsContainer {

	private final Long id;
	private final String nickname;
	private String password;
	private final Collection<GrantedAuthority> authorities;

	public MemberDetails(Long id, String nickname, String password, Collection<GrantedAuthority> authorities) {
		this.id = id;
		this.nickname = nickname;
		this.password = password;
		this.authorities = Collections.unmodifiableCollection(new ArrayList<>(authorities));
	}

	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return nickname;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void eraseCredentials() {
		password = null;
	}

	public static MemberDetails from(Member member) {
		return new MemberDetails(
			member.getId(),
			member.getNickname(),
			null,
			Arrays.asList(new SimpleGrantedAuthority(member.getRole().getValue()))
		);
	}
}
