package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;

/**
 * 询价单实体类DAO
 * @author ronnie
 *
 */
public interface EpPriceDao extends BaseRepository<EpPriceEntity, Serializable>,JpaSpecificationExecutor<EpPriceEntity>{

}
