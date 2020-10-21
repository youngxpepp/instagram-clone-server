package com.youngxpepp.instagramcloneserver.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youngxpepp.instagramcloneserver.domain.member.model.Google;

public interface GoogleRepository extends JpaRepository<Google, Long> {

	Optional<Google> findByKey(String key);
}
