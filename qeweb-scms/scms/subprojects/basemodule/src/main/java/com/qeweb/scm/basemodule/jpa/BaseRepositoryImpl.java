package com.qeweb.scm.basemodule.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.BaseEntity;

@NoRepositoryBean
public class BaseRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T, Serializable> implements BaseRepository<T, ID>{
	private EntityManager entityManager;
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = entityManager;
	}
	
	

	@Override
	public void abolish(ID id){
		Assert.notNull(id, "The given id must not be null!");
		T entity = findOne(id);
		if(entity instanceof BaseEntity){
			BaseEntity base = (BaseEntity) entity;
			base.setAbolished(1);
			save(entity);
		}
		
		
	}

}
