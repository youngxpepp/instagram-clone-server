package com.youngxpepp.instagramcloneserver.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class JpaTestSupport implements InitializingBean {

	private final EntityManagerFactory emf;
	private EntityManager em;

	public JpaTestSupport(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.em = emf.createEntityManager();
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

	public void deleteAllInOneTable(String tableName) {
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			deleteQuery(tableName).executeUpdate();
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
			String[] tables = new String[]{
				"feed",
				"member_like_comment",
				"comment",
				"member_like_article",
				"article_created",
				"article_image",
				"article",
				"follow",
				"member"
			};
			for (String t : tables) {
				deleteQuery(t).executeUpdate();
			}
			trx.commit();
			em.clear();
		} catch (Exception e) {
			trx.rollback();
		}
	}

	private Query deleteQuery(String tableName) {
		return em
			.createNativeQuery("DELETE FROM ?1;")
			.setParameter(1, tableName);
	}
}
