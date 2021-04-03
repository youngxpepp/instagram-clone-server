package com.youngxpepp.instagramcloneserver.test;

import static com.youngxpepp.instagramcloneserver.domain.QArticle.*;
import static com.youngxpepp.instagramcloneserver.domain.QArticleCreated.*;
import static com.youngxpepp.instagramcloneserver.domain.QArticleImage.*;
import static com.youngxpepp.instagramcloneserver.domain.QComment.*;
import static com.youngxpepp.instagramcloneserver.domain.QFeed.*;
import static com.youngxpepp.instagramcloneserver.domain.QFollow.*;
import static com.youngxpepp.instagramcloneserver.domain.QMember.*;
import static com.youngxpepp.instagramcloneserver.domain.QMemberLikeArticle.*;
import static com.youngxpepp.instagramcloneserver.domain.QMemberLikeComment.*;
import static com.youngxpepp.instagramcloneserver.domain.QMemberOAuth2Info.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.TestComponent;

import com.youngxpepp.instagramcloneserver.domain.QMemberOAuth2Info;

@TestComponent
public class JpaTestSupport implements InitializingBean {

	private final EntityManagerFactory emf;
	private EntityManager em;
	private JPAQueryFactory jpaQueryFactory;

	public JpaTestSupport(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		em = emf.createEntityManager();
		jpaQueryFactory = new JPAQueryFactory(em);
	}

	public <T> T save(T entity) {
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			em.persist(entity);
			trx.commit();
			em.clear();
			return entity;
		} catch (Exception e) {
			trx.rollback();
		}
		return null;
	}

	public <T> Iterable<T> saveAll(Iterable<T> entities) {
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			for (T e : entities) {
				em.persist(e);
			}
			trx.commit();
			em.clear();
			return entities;
		} catch (Exception e) {
			trx.rollback();
		}
		return null;
	}

	public void deleteAllInOneTable(EntityPath<?> path) {
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();

			jpaQueryFactory.delete(path).execute();

			trx.commit();
			em.clear();
		} catch (Exception e) {
			trx.rollback();
		}
	}

	public void deleteAllInAllTables() {
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();

			jpaQueryFactory.delete(feed).execute();
			jpaQueryFactory.delete(memberLikeComment).execute();
			jpaQueryFactory.delete(comment).execute();
			jpaQueryFactory.delete(memberLikeArticle).execute();
			jpaQueryFactory.delete(articleCreated).execute();
			jpaQueryFactory.delete(articleImage).execute();
			jpaQueryFactory.delete(article).execute();
			jpaQueryFactory.delete(follow).execute();
			jpaQueryFactory.delete(memberOAuth2Info).execute();
			jpaQueryFactory.delete(member).execute();

			trx.commit();
			em.clear();
		} catch (Exception e) {
			trx.rollback();
		}
	}
}
