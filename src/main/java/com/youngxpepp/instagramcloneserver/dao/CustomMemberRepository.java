package com.youngxpepp.instagramcloneserver.dao;

import java.util.Optional;

import com.youngxpepp.instagramcloneserver.domain.Member;

public interface CustomMemberRepository {

	Optional<Member> findByOAuth2NameAttributeAndOAuth2RegistrationId(String oauth2NameAttribute, String oauth2RegistrationId);
}
