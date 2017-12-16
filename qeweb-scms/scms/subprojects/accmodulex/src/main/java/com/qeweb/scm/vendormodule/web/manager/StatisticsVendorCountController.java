package com.qeweb.scm.vendormodule.web.manager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.ExcelAnnotationReader;
import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.constants.TypeConstant;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.ExcelUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.service.VendorBaseInfoService;
import com.qeweb.scm.vendormodule.vo.StatisticsVendorCountTransfer;

@Controller
@RequestMapping(value="/manager/vendor/statistics")
public class StatisticsVendorCountController {

	@Autowired
	private VendorBaseInfoService vendorBaseInfoService;
	
	private Map<String,Object> map;
	
	@LogClass(method="查看", module="各维度供应商数量统计")
	@RequestMapping(method = RequestMethod.GET)
	public String statistics(Model model){
		return "back/vendor/statiVendorCountList";
	}
	
	@RequestMapping(value = "redirect/{type}",method = RequestMethod.GET)
	public String redirect(Model model,@PathVariable("type")Integer type){
		if(type==0) {
			return "back/vendor/statiVendorCountProv";
		}
		if(type==1) {
			return "back/vendor/statiVendorCountProvPhase";
		}
		if(type==2) {
			return "back/vendor/statiVendorCountMatTypePhase";
		}
		if(type==3) {
			return "back/vendor/statiVendorCountProvBrand";
		}
		if(type==4) {
			return "back/vendor/statiVendorCountVendorClassify";
		}
		if(type==5) {
			return "";
		} else {
			return "back/vendor/statiVendorCountList";
		}
	}
	
	@RequestMapping(value = "statiVendorCountList/{type}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> statiVendorCountList(@PathVariable("type")Integer type,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		//搜索条件
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Map<String,Object> checkParamMap = Servlets.getParametersStartingWith(request, "checkbox-");

		String checkedParam = null;
		for (String key : checkParamMap.keySet()) {
			if(checkedParam==null){
				checkedParam = checkParamMap.get(key)+"";
			}else{
				checkedParam += ","+checkParamMap.get(key)+"";
			}
		}
		//返回到页面所有数据的总Map
		map = new HashMap<String, Object>();
		//返回到页面图表中显示的数据格式
		List<Map<String, Object>> list = vendorBaseInfoService.getChartData(type,checkedParam,searchParamMap);
		//返回到页面table中显示的数据格式
		List<VendorBaseInfoEntity> vList = null;
		//[start]单个维度下数据组装
		if(checkParamMap.size()<2){
			if(type==TypeConstant.STATISTICS_VENDOR_COUNT_LIST){
				vList = new ArrayList<VendorBaseInfoEntity>();
				if(TypeConstant.STATISTICS_PROV.equals(checkedParam)){
					for (int i = 0; i < list.size(); i++) {
						VendorBaseInfoEntity v = new VendorBaseInfoEntity();
						v.setId(i);
						v.setCol27(list.get(i).get("注册供应商")==null?"0":list.get(i).get("注册供应商")+"");
						v.setCol28(list.get(i).get("体系外备选供应商")==null?"0":list.get(i).get("体系外备选供应商")+"");
						v.setCol29(list.get(i).get("体系内供应商")==null?"0":list.get(i).get("体系内供应商")+"");
						v.setStatisticsCount(Integer.parseInt(v.getCol27())+Integer.parseInt(v.getCol28())+Integer.parseInt(v.getCol29()));
						v.setProvinceText(list.get(i).get("x-axis")+"");
						vList.add(v);
					}
				}
				if(TypeConstant.STATISTICS_PHASE.equals(checkedParam)){
					for (int i = 0; i < list.size(); i++) {
						VendorBaseInfoEntity v = new VendorBaseInfoEntity();
						v.setId(i);
						String phase = list.get(i).get("x-axis")+"";
						v.setCol1(phase);
						v.setStatisticsCount(Integer.parseInt(list.get(i).get(phase)+""));
						vList.add(v);
					}
				}
				if(TypeConstant.STATISTICS_CLASSIFY2.equals(checkedParam)){
					for (int i = 0; i < list.size(); i++) {
						VendorBaseInfoEntity v = new VendorBaseInfoEntity();
						v.setId(i);
						String classify = list.get(i).get("x-axis")+"";
						v.setCol4(classify);
						v.setStatisticsCount(Integer.parseInt(list.get(i).get(classify)+""));
						vList.add(v);
					}
				}
				if(TypeConstant.STATISTICS_PARTS_TYPE.equals(checkedParam)){
					for (int i = 0; i < list.size(); i++) {
						VendorBaseInfoEntity v = new VendorBaseInfoEntity();
						v.setId(i);
						String partsType = list.get(i).get("x-axis")+"";
						v.setCol6(partsType);
						v.setStatisticsCount(Integer.parseInt(list.get(i).get(partsType)+""));
						vList.add(v);
					}
				}
				if(TypeConstant.STATISTICS_BUSSINESS_TYPE.equals(checkedParam)){
					for (int i = 0; i < list.size(); i++) {
						VendorBaseInfoEntity v = new VendorBaseInfoEntity();
						v.setId(i);
						String bussinessType = list.get(i).get("x-axis")+"";
						v.setCol5(bussinessType);
						v.setStatisticsCount(Integer.parseInt(list.get(i).get(bussinessType)+""));
						vList.add(v);
					}
				}
				if(TypeConstant.STATISTICS_SYSTEM.equals(checkedParam)){
					for (int i = 0; i < list.size(); i++) {
						VendorBaseInfoEntity v = new VendorBaseInfoEntity();
						v.setId(i);
						String system = list.get(i).get("x-axis")+"";
						v.setCol3(system);
						v.setStatisticsCount(Integer.parseInt(list.get(i).get(system)+""));
						vList.add(v);
					}
				}
				
				Integer i=(pageNumber-1)*pageSize;
				Integer p=0;
				if((vList.size()-i)<=pageSize)
					p=vList.size()-i;
				else
					p=pageSize;
				Page<VendorBaseInfoEntity> page =new PageImpl<VendorBaseInfoEntity>(vList.subList(i, p+i), pagin, vList.size());
				
				map.put("rows",page.getContent());
				map.put("total",page.getTotalElements());
//				map.put("rows",vList);
//				map.put("total",list.size());
				map.put("chartData", list);
				return map;
			}
		}
		//[end]
		
		//两个维度下数据组装
		else{
			vList = new ArrayList<VendorBaseInfoEntity>();
			for (int i = 0; i < list.size(); i++) {
				VendorBaseInfoEntity v = new VendorBaseInfoEntity();
				Map<String,Object> vMap = list.get(i);
				v.setId(i);
				for (String str : vMap.keySet()) {
					String value = vMap.get(str)==null?"":vMap.get(str)+"";
					//省份 
					if(TypeConstant.STATISTICS_PROV.equals(str)){
						v.setProvinceText(value);
					}
					//供应商性质
					if(TypeConstant.STATISTICS_PHASE.equals(str)){
						v.setCol1(value);
					}
					//零部件类别
					if(TypeConstant.STATISTICS_PARTS_TYPE.equals(str)){
						v.setCol6(value);
					}
					//与工厂距离
					if(TypeConstant.STATISTICS_DISTANCE.equals(str)){
						v.setCol2(value);
					}
					//业务类型
					if(TypeConstant.STATISTICS_BUSSINESS_TYPE.equals(str)){
						v.setCol5(value);
					}
					//供应商分类 （A、B、C）
					if(TypeConstant.STATISTICS_CLASSIFY2.equals(str)){
						v.setCol4(value);
					}
					//系统   物料一级分类   (动力，底盘，电气，车身……)
					if(TypeConstant.STATISTICS_SYSTEM.equals(str)){
						v.setCol3(value);
					}
				}
				v.setStatisticsCount(Integer.parseInt(list.get(i).get("vendorCount")+""));
				vList.add(v);
			}
			
			
			Integer i=(pageNumber-1)*pageSize;
			Integer p=0;
			if((vList.size()-i)<=pageSize)
				p=vList.size()-i;
			else
				p=pageSize;
			Page<VendorBaseInfoEntity> page =new PageImpl<VendorBaseInfoEntity>(vList.subList(i, p+i), pagin, vList.size());
			
			map.put("rows",page.getContent());
			map.put("total",page.getTotalElements());
			map.put("chartData", packingData(list));
			return map;
		}
		
		
		
		
		//================分割线===========================分割线===========================分割线===========================分割线===========
		/*
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_PROV){
			//省份统计
			vList = new ArrayList<VendorBaseInfoEntity>();
			for (int i = 0; i < list.size(); i++) {
				VendorBaseInfoEntity v = new VendorBaseInfoEntity();
				v.setId(i);
				v.setProvinceText(list.get(i).get("province")+"");
				v.setStatisticsCount(Integer.parseInt(list.get(i).get("totalCount")+""));
				vList.add(v);
			}
			map.put("rows",vList);
			map.put("total",list.size());
		}
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_PROV_PHASE||type==TypeConstant.STATISTICS_VENDOR_COUNT_MAT_PHASE){
			vList = new ArrayList<VendorBaseInfoEntity>();
			for (int i = 0; i < list.size(); i++) {
				VendorBaseInfoEntity v = new VendorBaseInfoEntity();
				v.setId(i);
				v.setCol1(list.get(i).get("注册供应商")==null?"0":list.get(i).get("注册供应商")+"");
				v.setCol2(list.get(i).get("体系外备选供应商")==null?"0":list.get(i).get("体系外备选供应商")+"");
				v.setCol3(list.get(i).get("体系内供应商")==null?"0":list.get(i).get("体系内供应商")+"");
				v.setProvinceText(list.get(i).get("province")+"");
				vList.add(v);
			}
			map.put("rows",vList);
			map.put("total",list.size());
		}
		//筛选品牌按照省份统计供应商数量 
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_PROV_BRAND){
			//省份统计
			vList = new ArrayList<VendorBaseInfoEntity>();
			for (int i = 0; i < list.size(); i++) {
				VendorBaseInfoEntity v = new VendorBaseInfoEntity();
				v.setId(i);
				v.setProvinceText(list.get(i).get("province")+"");
				v.setStatisticsCount(Integer.parseInt(list.get(i).get("totalCount")+""));
				vList.add(v);
			}
			map.put("rows",vList);
			map.put("total",list.size());
		}
				
		//筛选产品线、品牌按照供应商分类统计供应商数量 
		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_VENDORCLASSIFY){
			//供应商分类统计
			vList = new ArrayList<VendorBaseInfoEntity>();
			for (int i = 0; i < list.size(); i++) {
				VendorBaseInfoEntity v = new VendorBaseInfoEntity();
				v.setId(i);
				v.setVendorClassify2(list.get(i).get("province")+"");
				v.setStatisticsCount(Integer.parseInt(list.get(i).get("totalCount")+""));
				vList.add(v);
			}
			map.put("rows",vList);
			map.put("total",list.size());		
		}
		//[start]注释的代码
//		if(type==TypeConstant.STATISTICS_VENDOR_COUNT_MAT_PHASE){
//			List<VendorBaseInfoEntity> vList = new ArrayList<VendorBaseInfoEntity>();
//			for (int i = 0; i < list.size(); i++) {
//				VendorBaseInfoEntity v = new VendorBaseInfoEntity();
//				v.setId(i);
//				v.setCol1(list.get(i).get("注册供应商")==null?"0":list.get(i).get("注册供应商")+"");
//				v.setCol2(list.get(i).get("体系外备选供应商")==null?"0":list.get(i).get("体系外备选供应商")+"");
//				v.setCol3(list.get(i).get("体系内供应商")==null?"0":list.get(i).get("体系内供应商")+"");
//				v.setProvinceText(list.get(i).get("province")+"");
//				vList.add(v);
//			}
//			map.put("rows",vList);
//			map.put("total",list.size());
//		}
		//[end]
		 */
		map.put("chartData", list);
		return map;
	}
	
	/**
	 * 两个维度图表数据组装
	 * @param list
	 * @return
	 */
	public List<Map<String,Object>> packingData(List<Map<String, Object>> list){
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		Map<String,Map<String,Object>> retMap = new HashMap<String,Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> lMap = list.get(i);
			String y_axis = lMap.get("y-axis")+"";
			String count = lMap.get("vendorCount")+"";
			for (String key : lMap.keySet()) {
				if(y_axis.equals(lMap.get(key))||"y-axis".equals(key)||"vendorCount".equals(key)){
					continue;
				}
				Map<String,Object> map = retMap.get(lMap.get("y-axis")+"");
				if(map==null){
					map = new HashMap<String, Object>();
				}
				String iCount = map.get(lMap.get(key))+"";
				if(StringUtils.isEmpty(iCount)||"null".equals(iCount)){
					iCount = count;
				}else{
					iCount = Integer.parseInt(iCount)+Integer.parseInt(count)+"";
				}
				map.put(lMap.get(key)+"", iCount);
				retMap.put(lMap.get("y-axis")+"",map);
			}
		}
		for (String key : retMap.keySet()) {
			retMap.get(key).put("x-axis", key);
			retList.add(retMap.get(key));
		}
		return retList;
	}
	
	/**
	 * 导出统计表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("exportExcel")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Map<String,Object> checkParamMap = Servlets.getParametersStartingWith(request, "checkbox-");

		String checkedParam = null;
		for (String key : checkParamMap.keySet()) {
			if(checkedParam==null){
				checkedParam = checkParamMap.get(key)+"";
			}else{
				checkedParam += ","+checkParamMap.get(key)+"";
			}
		}
		response.setContentType("octets/stream");
	    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("各维度供应商数量统计", "UTF-8")+ DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD_HH_MM) + ".xls");
		List<StatisticsVendorCountTransfer> list = vendorBaseInfoService.getStatisticsVendorCountTransfer(checkedParam,searchParamMap);
		ExcelUtil excelUtil = new ExcelUtil(response.getOutputStream(), new ExcelAnnotationReader(StatisticsVendorCountTransfer.class), null);  
		excelUtil.export(list);  
		return null;   
	}
	

}
