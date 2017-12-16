package com.qeweb.scm.vendormodule.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.persistence.SearchFilterEx.Operator;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.AreaDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorChangeHisEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;
import com.qeweb.scm.vendormodule.entity.VendorPhaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorReportEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyBaseEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyDataEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorChangeHisDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialSupplyRelDao;
import com.qeweb.scm.vendormodule.repository.VendorPhaseDao;
import com.qeweb.scm.vendormodule.repository.VendorReportDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyBaseDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyCfgDao;
import com.qeweb.scm.vendormodule.repository.VendorSurveyDataDao;
import com.qeweb.scm.vendormodule.utils.SimpleExcelWrite;
import com.qeweb.scm.vendormodule.utils.VendorExcel;
import com.qeweb.scm.vendormodule.utils.VendorExcel2;
import com.qeweb.scm.vendormodule.vo.QSATransfer;
import com.qeweb.scm.vendormodule.vo.VendorLevelTransfer;

@Service
@Transactional
public class VendorInformationService {

	@Autowired
	private VendorBaseInfoDao vendorBaseInfoDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private VendorPhaseDao vendorPhaseDao;
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;
	@Autowired
	private VendorSurveyCfgDao cfgDao;
	@Autowired
	private VendorSurveyDataDao vendorSurveyDataDao;
	@Autowired
	private VendorSurveyBaseDao vendorSurveyBaseDao;
	@Autowired
	private VendorReportDao vendorReportDao;
	@Autowired
	private VendorChangeHisDao changeHisDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private VendorMaterialSupplyRelDao vendorMaterialSupplyRelDao;


	public Page<VendorBaseInfoEntity> getVendorInfoList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize,
				null);
		searchParamMap.put("EQ_currentVersion", "" + StatusConstant.STATUS_YES);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorBaseInfoEntity> spec = DynamicSpecificationsEx
				.bySearchFilterEx(filters.values(), VendorBaseInfoEntity.class);
		Page<VendorBaseInfoEntity> vendorBaseInfoPage = vendorBaseInfoDao
				.findAll(spec, pagin);

		return vendorBaseInfoPage;
	}
	public Page<VendorBaseInfoEntity> getVendorInfoListSo(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize,
				null);
		searchParamMap.put("EQ_currentVersion", "" + StatusConstant.STATUS_YES);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorBaseInfoEntity> spec = DynamicSpecificationsEx
				.bySearchFilterEx(filters.values(), VendorBaseInfoEntity.class);
		Page<VendorBaseInfoEntity> vendorBaseInfoPage = vendorBaseInfoDao
				.findAll(spec, pagin);
		
		return vendorBaseInfoPage;
	}

	public String getVendorPhase() {
		List<VendorPhaseEntity> iterable = (List<VendorPhaseEntity>) vendorPhaseDao
				.findAll();
		String data = "[";
		int i = 0;
		for (VendorPhaseEntity vendorPhaseEntity : iterable) {
			if (i > 0) {
				data = data + ",";
			}
			data = data + "{\"id\":\"" + vendorPhaseEntity.getId()
					+ "\",\"text\":\"" + vendorPhaseEntity.getName() + "\"}";
			i++;
		}
		data = data + "]";
		return data;
	}

	public void vendorInfoRela(Long id, HttpServletRequest request,
			HttpServletResponse response) {
		VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoDao.findOne(id);
		request.setAttribute("vendorBaseInfo", vendorBaseInfo);
		getSurvey(request, response, "" + vendorBaseInfo.getOrgId());
		getReport(request, response, vendorBaseInfo.getId());
		getOverhaul(request, response);
		getExtended(request, response);
	}

	public Page<VendorMaterialRelEntity> vendorInfoCon(Long orgId,
			int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize,
				null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		SearchFilterEx filter = new SearchFilterEx("vendorId", Operator.EQ, orgId);
		filters.put("vendorId", filter);
		Specification<VendorMaterialRelEntity> spec = DynamicSpecificationsEx
				.bySearchFilterEx(filters.values(), VendorMaterialRelEntity.class);
		return vendorMaterialRelDao.findAll(spec, pagin);
	}

	public String getVendorLevel() {
		String data = "[";
		int i = 0;
		for (String str : Constant.VEND_LEVEL) {
			if (i > 0) {
				data = data + ",";
			}
			data = data + "{\"id\":\"" + i + "\",\"text\":\"" + str + "\"}";
			i++;
		}
		data = data + "]";
		return data;
	}

	public String getVendorClassify2() {
		String data = "[";
		int i = 0;
		for (String str : Constant.VEND_CLASSIFY2) {
			if (i > 0) {
				data = data + ",";
			}
			data = data + "{\"id\":\"" + i + "\",\"text\":\"" + str + "\"}";
			i++;
		}
		data = data + "]";
		return data;
	}

	public String getVendorClassify() {
		String data = "[";
		int i = 0;
		for (String str : Constant.VEND_CLASSIFY) {
			if (i > 0) {
				data = data + ",";
			}
			data = data + "{\"id\":\"" + i + "\",\"text\":\"" + str + "\"}";
			i++;
		}
		data = data + "]";
		return data;
	}
	public String getprovince() {
		List<String> list=vendorBaseInfoDao.getprovince();
		String data = "[";
		int i = 0;
		for (String str : list) {
			if (i > 0) {
				data = data + ",";
			}
			data = data + "{\"id\":\"" + str + "\",\"text\":\"" + str + "\"}";
			i++;
		}
		data = data + "]";
		return data;
	}

	public void vendorInfoWeiHuStart(Long orgId, HttpServletRequest request,
			HttpServletResponse response) {
		VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoDao.findOne(orgId);
		request.setAttribute("vendorClassify2", Constant.VEND_CLASSIFY2);
		request.setAttribute("vendorClassify", Constant.VEND_CLASSIFY);
		request.setAttribute("vendorLevel", Constant.VEND_LEVEL);
		request.setAttribute("vendorBaseInfo", vendorBaseInfo);

	}

	public String updateVendorClassify(
			VendorBaseInfoEntity vendorBaseInfoEntity,
			HttpServletRequest request, HttpServletResponse response) {
		vendorBaseInfoDao.updateVendorClassify(vendorBaseInfoEntity.getCode(),
				vendorBaseInfoEntity.getVendorLevel(),
				vendorBaseInfoEntity.getVendorClassify(),
				vendorBaseInfoEntity.getVendorClassify2());
		return "1";
	}

	public String updateVendorQSA(VendorBaseInfoEntity vendorBaseInfoEntity,
			HttpServletRequest request, HttpServletResponse response) {
		vendorBaseInfoDao.updateVendorQSA(vendorBaseInfoEntity.getCode(),
				vendorBaseInfoEntity.getQsa(),
				vendorBaseInfoEntity.getQsaResult());
		return "1";
	}

	public void vendorInfoWeiHuStart2(Long orgId, HttpServletRequest request,
			HttpServletResponse response) {
		VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoDao.findOne(orgId);
		request.setAttribute("vendorBaseInfo", vendorBaseInfo);

	}

	public String combineVendorLevel(List<VendorLevelTransfer> list) {
		String rtu = "";
		for (VendorLevelTransfer vendorLevelTransfer : list) {
			VendorBaseInfoEntity v = vendorBaseInfoDao
					.findByCodeAndCurrentVersion(vendorLevelTransfer.getCode(),
							StatusConstant.STATUS_YES);
			if (v != null) {
				int i = 0;
				for (String str : Constant.VEND_LEVEL) {
					if (str.equals(vendorLevelTransfer.getVendorLevel())) {
						i = 1;
						break;
					}
				}
				if (i == 0) {
					rtu = rtu + "," + vendorLevelTransfer.getCode() + ":"
							+ vendorLevelTransfer.getVendorLevel() + "无效字段";
				}
				i = 0;
				for (String str : Constant.VEND_CLASSIFY) {
					if (str.equals(vendorLevelTransfer.getVendorClassify())) {
						i = 1;
						break;
					}
				}
				if (i == 0) {
					rtu = rtu + "," + vendorLevelTransfer.getCode() + ":"
							+ vendorLevelTransfer.getVendorClassify() + "无效字段";
				}
				i = 0;
				for (String str : Constant.VEND_CLASSIFY2) {
					if (str.equals(vendorLevelTransfer.getVendorClassify2())) {
						i = 1;
						break;
					}
				}
				if (i == 0) {
					rtu = rtu + "," + vendorLevelTransfer.getCode() + ":"
							+ vendorLevelTransfer.getVendorClassify2() + "无效字段";
				}
			} else {
				rtu = rtu + "," + vendorLevelTransfer.getCode() + "找不到";
			}
			vendorBaseInfoDao.updateVendorClassify(
					vendorLevelTransfer.getCode(),
					vendorLevelTransfer.getVendorLevel(),
					vendorLevelTransfer.getVendorClassify(),
					vendorLevelTransfer.getVendorClassify2());
		}
		if (rtu.equals("")) {
			rtu = "1";
		} else {
			rtu = "供应商为：" + rtu;
		}
		return rtu;
	}

	public String combineQSA(List<QSATransfer> list) {
		String rtu = "";
		for (QSATransfer qSATransfer : list) {
			VendorBaseInfoEntity v = vendorBaseInfoDao
					.findByCodeAndCurrentVersion(qSATransfer.getCode(),
							StatusConstant.STATUS_YES);
			if (v == null) {
				rtu = rtu + "," + qSATransfer.getCode() + "找不到";
			}
			vendorBaseInfoDao.updateVendorQSA(qSATransfer.getCode(),
					qSATransfer.getQsa(), qSATransfer.getQsaResult());
		}
		if (rtu.equals("")) {
			rtu = "1";
		} else {
			rtu = "供应商为：" + rtu;
		}
		return rtu;
	}

	public void getSurvey(HttpServletRequest request,
			HttpServletResponse response, String orgid) {
		List<VendorSurveyCfgEntity> vendorSurveyCfgEntitys = cfgDao
				.findByOrgId(Long.parseLong(orgid));

		for (VendorSurveyCfgEntity vendorSurveyCfgEntity : vendorSurveyCfgEntitys) {
			VendorSurveyBaseEntity vendorSurveyBaseEntitys = null;
			if (vendorSurveyCfgEntity.getSurveyCode().equals("base")) {
				VendorBaseInfoEntity vendorBaseInfoEntity = vendorBaseInfoDao
						.findByOrgIdAndCurrentVersion(
								vendorSurveyCfgEntity.getOrgId(),
								StatusConstant.STATUS_YES);
				if (vendorBaseInfoEntity != null) {
					vendorSurveyBaseEntitys = new VendorSurveyBaseEntity();
					vendorSurveyBaseEntitys.setAuditStatus(vendorBaseInfoEntity
							.getAuditStatus());
					vendorSurveyBaseEntitys.setVersionNO(vendorBaseInfoEntity
							.getVersionNO());
					vendorSurveyBaseEntitys
							.setCreateUserName(vendorBaseInfoEntity
									.getCreateUserName());
					vendorSurveyBaseEntitys.setCreateTime(vendorBaseInfoEntity
							.getCreateTime());
					vendorSurveyBaseEntitys
							.setLastUpdateTime(vendorBaseInfoEntity
									.getLastUpdateTime());
				}
			} else {
				List<VendorSurveyBaseEntity> surveys = vendorSurveyBaseDao.findByVendorCfgIdAndCurrentVersionOrderByIdDesc(vendorSurveyCfgEntity.getId(),StatusConstant.STATUS_YES);
				VendorSurveyBaseEntity survey = null;
				if(surveys!=null&&surveys.size()>0)
				{
					if(surveys.size()>1)
					{
						for(int i=1;i<surveys.size();i++)
						{
							survey=surveys.get(i);
							survey.setCurrentVersion(0);
							vendorSurveyBaseDao.save(survey);
						}
					}
					vendorSurveyBaseEntitys =surveys.get(0);
				}
			}
			if (vendorSurveyBaseEntitys != null) {
				vendorSurveyCfgEntity
						.setVendorSurveyBaseEntitys(vendorSurveyBaseEntitys);
			}
		}
		request.setAttribute("vendorSurveyCfgEntitys", vendorSurveyCfgEntitys);
	}

	public void getReport(HttpServletRequest request,
			HttpServletResponse response, Long vendorId) {
		List<VendorReportEntity> list = vendorReportDao.findByOrgid(vendorId);
		request.setAttribute("vendorReportEntitys", list);
	}

	public String getReport2(Long vendorId) {
		List<VendorReportEntity> list = vendorReportDao.findByOrgid(vendorId);
		String s = "";
		for (VendorReportEntity vendorReportEntity : list) {
			s = s + "<tr><td>" + vendorReportEntity.getId() + "</td>"
					+ "<td><a href='" + vendorReportEntity.getReportUrl()
					+ "'>" + vendorReportEntity.getReportName() + "</a></td>"
					+ "<td>" + vendorReportEntity.getCreateUserName() + "</td>"
					+ "<td>"
					+ getDateString(vendorReportEntity.getCreateTime())
					+ "</td>";
		}
		return s;
	}

	public void getOverhaul(HttpServletRequest request,
			HttpServletResponse response) {
	}

	public void getExtended(HttpServletRequest request,
			HttpServletResponse response) {
	}

	public Page<VendorSurveyBaseEntity> examine(Long orgId, Long cfgid,
			int pageNumber, int pageSize, Map<String, Object> searchParamMap) {

		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize,
				null);
		searchParamMap.put("EQ_orgId", "" + orgId);
		searchParamMap.put("EQ_vendorCfgId", "" + cfgid);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorSurveyBaseEntity> spec = DynamicSpecificationsEx
				.bySearchFilterEx(filters.values(), VendorSurveyBaseEntity.class);
		Page<VendorSurveyBaseEntity> page = vendorSurveyBaseDao.findAll(spec,
				pagin);
		return page;
	}

	public boolean filesUpload3(long vendid, String saveUrl, String names) {
		BaseEntity baseEntity = new BaseEntity();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (user == null) {
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return false;
		}
		String loginName = user.loginName;
		UserEntity userEntity = userDao.findByLoginName(loginName);
		VendorReportEntity vendorReportEntity = new VendorReportEntity();
		vendorReportEntity.setCreateTime(DateUtil.getCurrentTimestamp());
		vendorReportEntity.setCreateUserName(userEntity.getName());
		vendorReportEntity.setCreateUserId(userEntity.getId());
		vendorReportEntity.setOrgid(vendid);
		vendorReportEntity.setUpdateUserId(userEntity.getId());
		vendorReportEntity.setUpdateUserName(userEntity.getName());
		vendorReportEntity.setReportUrl(saveUrl);
		vendorReportEntity.setReportName(names);
		vendorReportDao.save(vendorReportEntity);
		return true;
	}

	public Page<VendorChangeHisEntity> lookGKJJSS(Long orgId, int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize,
				null);
		searchParamMap.put("EQ_orgId", "" + orgId);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorChangeHisEntity> spec = DynamicSpecificationsEx
				.bySearchFilterEx(filters.values(), VendorChangeHisEntity.class);
		Page<VendorChangeHisEntity> page = changeHisDao.findAll(spec, pagin);
		return page;
	}

	public String getCountryProvince(String id) {
		if (id != null && !(id.equals("")) && !(id.equals("null"))) {
			AreaEntity are = areaDao.findOne(Long.parseLong(id));
			if (are != null && are.getName() != null) {
				return are.getName();
			} else {
				return id;
			}
		} else
			return "未填写";
	}

	public String getLookgonghuoxishu(Long mid, Long orgId) {
		List<VendorMaterialSupplyRelEntity> list = vendorMaterialSupplyRelDao
				.findByMaterialRelIdAndVendorId(mid, orgId);
		String s = "";
		for (VendorMaterialSupplyRelEntity vendorMaterialSupplyRelEntity : list) {
			s = s + "<tr><td>"
					+ vendorMaterialSupplyRelEntity.getBussinessRangeName()
					+ "</td>" + "<td>"
					+ vendorMaterialSupplyRelEntity.getBussinessName()
					+ "</td>" + "<td>"
					+ vendorMaterialSupplyRelEntity.getBrandName() + "</td>"
					+ "<td>"
					+ vendorMaterialSupplyRelEntity.getProductLineName()
					+ "</td>" + "<td>"
					+ vendorMaterialSupplyRelEntity.getFactoryName() + "</td>"
					+ "<td>"
					+ getString(vendorMaterialSupplyRelEntity.getIsmain())
					+ "</td>" + "<td>"
					+ vendorMaterialSupplyRelEntity.getSupplyCoefficient()
					+ "</td></tr>";
		}
		if (list.size() == 0) {
			s = "无数据";
		}
		return s;
	}

	private String getString(Integer ismain) {
		if (ismain == 0) {
			return "否";
		} else {
			return "是";
		}
	}

	private String getDateString(Timestamp date) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String str = formatDate.format(date);
		return str;
	}

	public void download(Long id, HttpServletResponse response)
			throws IOException {
		// TODO Auto-generated method stub

		VendorBaseInfoEntity vendorBaseInfo = vendorBaseInfoDao.findOne(id);
		VendorSurveyBaseEntity base = vendorSurveyBaseDao
				.findByTemplateCodeAndOrgIdAndCurrentVersion(
						"personnel-structure", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"personnel-structure-form1");
		}

		base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
				"bussiness-data1", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data2 = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data2 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"bussinessdata1-tb1");
		}

		base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
				"Main-products", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data3 = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data3 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"mainproducts-tb1");
		}

		base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
				"Main-products", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data4 = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data4 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"mainproducts-tb2");
		}

		base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
				"system-certificate", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data5 = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data5 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"systemCertificate-tb1");
		}

		base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
				"product", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data6 = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data6 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"ppapel-tb1");
		}

		base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
				"capability", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data7 = new ArrayList<VendorSurveyDataEntity>();
		List<VendorSurveyDataEntity> data8 = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data7 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"capability-tb1");
			data8 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"capability-frm1");

		}

		base = vendorSurveyBaseDao.findByTemplateCodeAndOrgIdAndCurrentVersion(
				"Main-products", vendorBaseInfo.getOrgId(), 1);
		List<VendorSurveyDataEntity> data9 = new ArrayList<VendorSurveyDataEntity>();
		if (base != null) {
			data9 = vendorSurveyDataDao.findByBaseIdAndCtId(base.getId(),
					"mainproducts-tb3");
		}
		if (vendorBaseInfo.getProperty() != null) {
			if (vendorBaseInfo.getProperty().equals("1")) {
				vendorBaseInfo.setPropertyText("国有企业");
			}
			else if (vendorBaseInfo.getProperty().equals("2")) {
				vendorBaseInfo.setPropertyText("国有控股企业");
			}
			else if (vendorBaseInfo.getProperty().equals("3")) {
				vendorBaseInfo.setPropertyText("外资企业");
			}
			else if (vendorBaseInfo.getProperty().equals("4")) {
				vendorBaseInfo.setPropertyText("合资企业");
			}
			else if (vendorBaseInfo.getProperty().equals("5")) {
				vendorBaseInfo.setPropertyText("私营企业");
			}
			else
			{
				vendorBaseInfo.setPropertyText(vendorBaseInfo.getProperty());
			}
		}
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
		String str = formatDate.format(new Date());
		int year = Integer.parseInt(str);

		SimpleExcelWrite simpleExcelWrite = new SimpleExcelWrite();

		VendorExcel2 ve21 = new VendorExcel2();
		ve21.setName("主要产品销量");

		VendorExcel2 ve22 = new VendorExcel2();
		ve22.setName("销售总收入（万元）");

		VendorExcel2 ve23 = new VendorExcel2();
		ve23.setName("利润总额（万元）");

		VendorExcel2 ve24 = new VendorExcel2();
		ve24.setName("资产负债率");
		if (data2 != null) {
			for (VendorSurveyDataEntity vsd : data2) {
				if(vsd.getCol1()!=null&&vsd.getCol1().equals(""))
				{
					String yed = vsd.getCol1().split("-")[0];
					if ((year - Integer.parseInt(yed)) == 1) {
						ve21.setCol3(vsd.getCol2());
						ve22.setCol3(vsd.getCol3());
						ve23.setCol3(vsd.getCol4());
						ve24.setCol3(vsd.getCol5());
					}
					if ((year - Integer.parseInt(yed)) == 2) {
						ve21.setCol2(vsd.getCol2());
						ve22.setCol2(vsd.getCol3());
						ve23.setCol2(vsd.getCol4());
						ve24.setCol2(vsd.getCol5());
					}
					if ((year - Integer.parseInt(yed)) == 3) {
						ve21.setCol1(vsd.getCol2());
						ve22.setCol1(vsd.getCol3());
						ve23.setCol1(vsd.getCol4());
						ve24.setCol1(vsd.getCol5());
					}
					if ((year - Integer.parseInt(yed)) != 0) {
						ve21.setCol4(vsd.getCol2());
						ve22.setCol4(vsd.getCol3());
						ve23.setCol4(vsd.getCol4());
						ve24.setCol4(vsd.getCol5());
					}
				}
			}
		}
		List<VendorExcel2> list = new ArrayList<VendorExcel2>();
		list.add(ve21);
		list.add(ve22);
		list.add(ve23);
		list.add(ve24);

		List<VendorExcel> list1 = new ArrayList<VendorExcel>();

		Map<String, List<VendorSurveyDataEntity>> map = new HashMap<String, List<VendorSurveyDataEntity>>();
		for (VendorSurveyDataEntity v : data4) {

			if (map.get(v.getCol1()) != null) {
				map.get(v.getCol1()).add(v);
			} else {
				List<VendorSurveyDataEntity> list222 = new ArrayList<VendorSurveyDataEntity>();
				list222.add(v);
				map.put(v.getCol1(), list222);
			}
		}

		Iterator<Entry<String, List<VendorSurveyDataEntity>>> entries = map
				.entrySet().iterator();
		while (entries.hasNext()) {
			VendorExcel vendExcel = new VendorExcel();
			Entry<String, List<VendorSurveyDataEntity>> entry = entries.next();
			vendExcel.setMainPER(entry.getKey());
			vendExcel.setList(entry.getValue());
			for(VendorSurveyDataEntity s:entry.getValue())
			{
				vendExcel.setThreeyear(vendExcel.getThreeyear()+getDouble(s.getCol5()));
				vendExcel.setTwoyear(vendExcel.getTwoyear()+getDouble(s.getCol6()));
				vendExcel.setOneyear(vendExcel.getOneyear()+getDouble(s.getCol7()));
			}
			list1.add(vendExcel);
		}
		VendorSurveyDataEntity vsde = null;
		if (data != null && data.size() > 0)
			vsde = data.get(0);
		else
			vsde = new VendorSurveyDataEntity();
		simpleExcelWrite.getExcelWrite(response, vendorBaseInfo, vsde, year,
				list, list1, data5, data3, data6, data7, data8, data9);

	}

	public Map<String, Object> updateSoff(String vid, Long sid) {
		 Map<String, Object> map=new HashMap<String, Object>();
		 List<VendorBaseInfoEntity> list=new ArrayList<VendorBaseInfoEntity>();
		 for(String id:vid.split(","))
		 {
			 VendorBaseInfoEntity vb= vendorBaseInfoDao.findOne(Long.parseLong(id));
//			 vb.setSoId(sid);
//			 vb.setSoStatus(1);
			 list.add(vb);
		 }
		 vendorBaseInfoDao.save(list);
		map.put("success",true);
		map.put("msg","设置成功");
		return map;
	}
	private double getDouble(String i){
		String ii=i.replace("元", "");
		double j=1;
		if(ii.indexOf("百")>0)
		{
			j=j*100;
			ii=ii.replace("百", "");
		}
		if(ii.indexOf("千")>0)
		{
			j=j*1000;
			ii=ii.replace("千", "");
		}
		if(ii.indexOf("万")>0)
		{
			j=j*10000;
			ii=ii.replace("万", "");
		}
		if(ii.indexOf("亿")>0)
		{
			j=j*100000000;
			ii=ii.replace("亿", "");
		}
		try{
			double jj=Double.parseDouble(ii);
			return j*jj;
		}catch(Exception e){
			return 0;
		}
	}
}
