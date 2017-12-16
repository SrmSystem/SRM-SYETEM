package com.qeweb.scm.vendormodule.web.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.utils.Collections3;

import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.vendormodule.entity.VendorPhaseCfgEntity;
import com.qeweb.scm.vendormodule.entity.VendorSurveyCfgEntity;
import com.qeweb.scm.vendormodule.service.VendorPhaseCfgService;
import com.qeweb.scm.vendormodule.service.VendorSurveyCfgService;
import com.qeweb.scm.vendormodule.utils.SurveyStatusUtil;

@Controller
@RequestMapping("/manager/vendor/cfg")
public class VendorCfgController {
	
	@Autowired
	private VendorPhaseCfgService vendorPhaseCfgService;
	@Autowired
	private VendorSurveyCfgService vendorSurveyCfgService;

	/**
	 * 获取供应商的阶段调查表，在这个阶段需要维护的调查表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getVendorPhaseSurveyCfg")
	@ResponseBody
	public List<EasyuiTree> getVendorPhaseSurveyCfg(Long vendorId,Model model) {
		List<VendorPhaseCfgEntity> phaseCfgList = vendorPhaseCfgService.getVendorPhaseSurveyCfg(vendorId);
		List<EasyuiTree> treeNodeList = toEasyuiTree_vendorCfg(phaseCfgList);
		return treeNodeList;
	}

	/**
	 * 对供应商配置组装成EasyUI需要的树状节点
	 * @param phaseCfgList
	 * @return
	 */
	private List<EasyuiTree> toEasyuiTree_vendorCfg(List<VendorPhaseCfgEntity> phaseCfgList) {
		List<EasyuiTree> treeNodeList = new ArrayList<EasyuiTree>();
		for(VendorPhaseCfgEntity vendorPhaseCfgEntity : phaseCfgList){
			//属性构建
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("phaseId", vendorPhaseCfgEntity.getPhaseId());
			//构建调查表树节点 
			List<EasyuiTree> leafNodeList = new ArrayList();
			if(!Collections3.isEmpty(vendorPhaseCfgEntity.getVendorSurveyCfgList())){
				for(VendorSurveyCfgEntity vendorSurveyCfg : vendorPhaseCfgEntity.getVendorSurveyCfgList()){
					//状态图标显示
					//属性构建
					Map<String,Object> leafAttrMap = new HashMap<String, Object>();
					leafAttrMap.put("surveyCode", vendorSurveyCfg.getSurveyTemplate().getCode());
					leafAttrMap.put("surveyName", vendorSurveyCfg.getSurveyTemplate().getName());
					String icon = SurveyStatusUtil.getSurveyStatusIcon(vendorSurveyCfg.getSubmitStatus(), vendorSurveyCfg.getAuditStatus());
					EasyuiTree leafNode = new EasyuiTree(vendorSurveyCfg.getId()+"", vendorSurveyCfg.getSurveyTemplate().getName(), "open", "",icon, null, leafAttrMap);
					leafNodeList.add(leafNode);
				}
			}
			EasyuiTree node = new EasyuiTree(vendorPhaseCfgEntity.getId()+"",
					vendorPhaseCfgEntity.getPhaseName(),"open","",
					"",leafNodeList,attrMap
					);
			treeNodeList.add(node);
		}
		return treeNodeList;
	}
	
	/**
	 * 获取可供降级的阶段
	 * @vendorId 当前供应商基本信息ID
	 * @return 可供降级的阶段
	 */
	@RequestMapping("getDemotionPhase")
	@ResponseBody
	public Map<String,Object> getDemotionPhase(Long vendorId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<VendorPhaseCfgEntity> phaseCfgList = vendorPhaseCfgService.getDemotionPhaseCfgList(vendorId);
		map.put("rows", phaseCfgList);
		return map;
	}
	@RequestMapping("getDemotionPhasesxl/{vendorId}")
	@ResponseBody
	public Map<String,Object> getDemotionPhasesxl(@PathVariable("vendorId") Long vendorId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<VendorPhaseCfgEntity> phaseCfgList = vendorPhaseCfgService.getDemotionPhaseCfgList(vendorId);
		map.put("rows", phaseCfgList);
		return map;
	}
	
}
