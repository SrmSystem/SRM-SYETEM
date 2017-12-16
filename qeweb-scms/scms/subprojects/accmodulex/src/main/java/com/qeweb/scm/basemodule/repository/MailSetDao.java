package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.MailSetEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 邮箱设置DAO 
 */
public interface MailSetDao extends BaseRepository<MailSetEntity, Serializable>,JpaSpecificationExecutor<MailSetEntity>{
	
	MailSetEntity findByMailTemplateIdAndAbolished(Integer mailTemplateId, Integer abolished);
}
