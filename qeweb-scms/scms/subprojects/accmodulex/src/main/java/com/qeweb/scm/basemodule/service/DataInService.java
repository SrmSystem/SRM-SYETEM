package com.qeweb.scm.basemodule.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.vo.DataInVO;

@Service
@Transactional
public class DataInService {
	
	@Autowired
	private MaterialDao materialDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;
	
	
	public boolean combine(List<DataInVO> list) {
		for(DataInVO vo:list)
		{
			List<MaterialEntity>  ms=materialDao.findByCode(vo.getCol3());
			for(MaterialEntity m:ms)
			{
				m.setPartsCode(vo.getCol1());
				List<MaterialTypeEntity>  mts=materialTypeDao.findByCode(vo.getCol1());
				if(mts!=null&&mts.size()>0)
				{
					m.setPartsName(mts.get(0).getName());
					m.setMaterialTypeId(mts.get(0).getId());
				}
			}
			if(ms!=null&&ms.size()>0)
			{
				materialDao.save(ms);
			}
		}
		return true;
	}
	
}
