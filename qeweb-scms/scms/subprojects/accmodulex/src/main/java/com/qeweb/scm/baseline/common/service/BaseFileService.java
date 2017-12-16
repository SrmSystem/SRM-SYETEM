package com.qeweb.scm.baseline.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.transaction.Transactional;

import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.baseline.common.entity.BaseFileEntity;
import com.qeweb.scm.baseline.common.repository.BaseFileDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.base.BaseService;



@Service
@Transactional
public class BaseFileService extends BaseService{

	@Autowired
	private BaseFileDao baseFileDao;
	
	@Autowired
	private GenerialDao generialDao;
	


	public BaseFileEntity findFile(Long billId,String billType){
		return baseFileDao.findByBillIdAndBillType(billId, billType);
	}

	public void saveFile(File savefile, String fileName) throws IOException {
		BaseFileEntity po=new BaseFileEntity();
		po.setFileName(fileName);
		po.setBillId(10L);
		po.setBillType("10");
		po.setFilePath(savefile.getPath());
		Session session = generialDao.getSession();
		LobHelper lh = session.getLobHelper();
		po.setFileContent(lh.createBlob(file2bytes(savefile)));
		baseFileDao.save(po);
		
	}
	
	/**
	 * 将文件转换为字节编码
	 * @throws IOException 
	 */
	public byte[] file2bytes(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		byte[] data = new byte[in.available()];

		try {
			in.read(data);
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}

		return data;
	}

	
}
