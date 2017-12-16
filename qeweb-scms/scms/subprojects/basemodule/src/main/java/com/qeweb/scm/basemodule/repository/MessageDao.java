package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.MessageEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface MessageDao extends BaseRepository<MessageEntity, Serializable>,JpaSpecificationExecutor<MessageEntity>{
	
}
