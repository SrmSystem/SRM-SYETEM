package com.qeweb.scm.baseline.common.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
public class BaseDao {
	
	@PersistenceContext
	private EntityManager em; 
	
	@Transactional
	public <A> A save(A entity){
		em.persist(entity);
		return entity;
	}
	@Transactional
	public <A> A update(A entity){
		em.merge(entity);
		return entity;
	}
	
	@Transactional
	public <A> List<A> save(Iterable<A> entities) {
		List<A> result = new ArrayList<A>();
		if (entities == null) {
			return result;
		}
		for (A entity : entities) {
			result.add(save(entity));
		}
		return result;
	}
	
	@Transactional
	public <A> List<A> update(Iterable<A> entities) {
		List<A> result = new ArrayList<A>();
		if (entities == null) {
			return result;
		}
		for (A entity : entities) {
			result.add(update(entity));
		}
		return result;
	}
	
	public  <A> A findOne(Class<? extends A> clazz,Long id){
		return em.find(clazz, id);
	}
	@Transactional
	public <A> void delete(A entity) {
		Assert.notNull(entity, "The entity must not be null!");
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}
	
	@Transactional
	public <A> void delete(Class<A> clazz,Long id) {
		Assert.notNull(id, "The given id must not be null!");
		A entity = findOne(clazz, id);
		if (entity == null) {
			return;
		}
		delete(entity);
	}
	
	@Transactional
	public <A> void delete(Iterable<? extends A> entities) {
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		for (A entity : entities) {
			delete(entity);
		}
	}

}
