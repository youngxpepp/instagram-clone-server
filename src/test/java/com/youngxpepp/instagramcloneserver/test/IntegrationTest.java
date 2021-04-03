package com.youngxpepp.instagramcloneserver.test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.Application;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.global.util.JwtUtils;

@SpringBootTest(classes = {Application.class})
@Import({JpaTestSupport.class})
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Disabled
public abstract class IntegrationTest {
	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected JPAQueryFactory jpaQueryFactory;

	@Autowired
	protected JwtUtils jwtUtils;

	@PersistenceContext
	protected EntityManager em;

	@Autowired
	protected JpaTestSupport jpaTestSupport;

	protected Member principal;

	@BeforeEach
	public void beforeEach() {
		principal = Member.builder()
			.name("geonhong.lee")
			.nickname("youngxpepp")
			.role(MemberRole.MEMBER)
			.build();
		jpaTestSupport.save(principal);
	}

	@AfterEach
	public void afterEach() {
		jpaTestSupport.deleteAllInAllTables();
	}
}
