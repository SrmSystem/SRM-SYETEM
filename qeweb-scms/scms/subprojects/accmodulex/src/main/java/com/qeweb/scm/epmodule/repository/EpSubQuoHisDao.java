package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpSubQuoHisEntity;

/**
 * 分项报价历史实体类DAO
 * @author ronnie
 *
 */
public interface EpSubQuoHisDao extends BaseRepository<EpSubQuoHisEntity, Serializable>,JpaSpecificationExecutor<EpSubQuoHisEntity>{

}
