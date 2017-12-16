package com.qeweb.scm.basemodule.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends PagingAndSortingRepository<T, Serializable>{
	
	public void abolish(ID id);

}
