package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.FileDescriptEntity;


public interface FileDescriptDao extends BaseRepository<FileDescriptEntity, Serializable>,JpaSpecificationExecutor<FileDescriptEntity>{

}
