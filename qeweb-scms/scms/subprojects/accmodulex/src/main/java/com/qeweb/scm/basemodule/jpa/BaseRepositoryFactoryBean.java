package com.qeweb.scm.basemodule.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * 
 * @author pjjxiajun
 * @date 2015年6月16日
 * @path com.qeweb.scm.basemodule.repository.common.BaseRepositoryFactoryBean.java
 */
@NoRepositoryBean
public class BaseRepositoryFactoryBean<R extends PagingAndSortingRepository<T, I>,T,I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I>
{

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

	    return new BaseRepositoryFactory<T,I>(entityManager);
	  }

	  private static class BaseRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

	    private EntityManager entityManager;

	    public BaseRepositoryFactory(EntityManager entityManager) {
	      super(entityManager);

	      this.entityManager = entityManager;
	    }

	    @Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {

	      return new BaseRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
	    }

	    @Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
	      //需要返回实现类		
	      return BaseRepositoryImpl.class;
	    }
	  }
}


