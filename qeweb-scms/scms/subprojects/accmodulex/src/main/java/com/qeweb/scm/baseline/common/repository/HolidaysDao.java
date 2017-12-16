package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.baseline.common.entity.BaseFileEntity;
import com.qeweb.scm.baseline.common.entity.HolidaysEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface HolidaysDao extends BaseRepository<HolidaysEntity, Serializable>,JpaSpecificationExecutor<HolidaysEntity>{
	
	HolidaysEntity findByYearAndMonthAndDay(Integer year,Integer month,Integer day);
}
