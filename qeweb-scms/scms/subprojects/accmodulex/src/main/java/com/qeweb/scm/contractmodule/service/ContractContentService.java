package com.qeweb.scm.contractmodule.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.contractmodule.entity.ContractContentEntity;
import com.qeweb.scm.contractmodule.entity.ContractEntity;
import com.qeweb.scm.contractmodule.entity.ContractExtEntity;
import com.qeweb.scm.contractmodule.repository.ContractContentDao;
import com.qeweb.scm.contractmodule.repository.ContractDao;
import com.qeweb.scm.contractmodule.repository.ContractExtDao;
@Service
@Transactional(rollbackOn=Exception.class)
public class ContractContentService extends BaseService{
	
	@Autowired
	private ContractContentDao contractContentDao;
	
	@Autowired
	private ContractExtDao contractExtDao;
	
	@Autowired
	private ContractDao contractDao;
	

 
    public static final float MARGIN_TOP = 50;   
    public static final float MARGIN_BOTTOM = 50;  
	
	private final static String CODE_KEY = "CONTMOIT";    //模板编号流水号的开头 CONTMOIT = contrastModuleItem
	
	private final static int MOVE_UP = 0;    //上移
	private final static int MOVE_DOWN = 1;		//下移
	
	public ContractContentEntity findOne(Long id) {
		return contractContentDao.findOne(id);
	}
	
	public ContractExtEntity findContractExt(Long contractId) {
		List<ContractExtEntity> res=contractExtDao.findByContractIdOrderByCreateTimeDesc(contractId);
		if(res==null||res.size()<=0){
			return null;
		}
		return res.get(0);
	}
	
	public void saveContractExt(ContractExtEntity contractExt){
		if(contractExt.getId()>0){
			ContractExtEntity old=contractExtDao.findOne(contractExt.getId());
			BeanUtil.copyPropertyNotNull(contractExt, old);
			contractExtDao.save(old);
		}else{
			contractExtDao.save(contractExt);
		}
		ContractEntity contract=contractDao.findOne(contractExt.getContractId());
		contract.setIsPdf("1");
		contractDao.save(contract);
	}
	
	/**
	 * 创建pdf
	 * @param writer
	 * @param document
	 * @param moduleItemList
	 */
	public void createPdf(PdfWriter writer, Document document,List<ContractContentEntity> moduleItemList,ContractEntity bo){
		int level = 0;	//初始化遍历的层数
		float num = -25f;	//初始化段落缩进数
		BaseFont bfChinese;
		int year=DateUtil.getDateYear(bo.getSignDate());
		int month=DateUtil.getDateMonth(bo.getSignDate());
		Calendar c = new GregorianCalendar();
		c.setTime(bo.getSignDate());
		int day=c.get(Calendar.DATE);
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);		
			Font fontChinese1 = new Font(bfChinese,13,Font.NORMAL);	//13号
			Font fontChinese2 = new Font(bfChinese,24,Font.BOLD);	//24号 加粗
			Font fontChinese3 = new Font(bfChinese,26,Font.BOLD);	//26号 加粗
			Font fontChinese4 = new Font(bfChinese,16,Font.BOLD);	//16号 加粗
			Font fontChinese6 = new Font(bfChinese,15,Font.BOLD);	//15号 加粗
			Font fontChinese7 = new Font(bfChinese,14,Font.UNDERLINE);	//14号 带有下划线
			Paragraph para1 = new Paragraph("版本号：2.0", fontChinese6);
			para1.setAlignment(Element.ALIGN_LEFT); 		//段落居左
			document.add(para1);
			Paragraph para2 = new Paragraph("编号：",fontChinese6);
			para2.setAlignment(Element.ALIGN_LEFT); 		//段落居左
			document.add(para2);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			Paragraph para3 = new Paragraph("", fontChinese2);
			para3.setAlignment(Element.ALIGN_CENTER); 		//段落居中
			document.add(para3);
			Paragraph para4 = new Paragraph(bo.getContractName(), fontChinese3);
			para4.setAlignment(Element.ALIGN_CENTER); 		//段落居中
			document.add(para4);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			PdfPTable table1 =new PdfPTable(3);
			table1.setSpacingBefore(MARGIN_TOP);
			table1.setSpacingAfter(MARGIN_BOTTOM);
			table1.setWidthPercentage(100);
			table1.setTotalWidth(200);
			table1.addCell(createCell(" ", false,1, fontChinese6));
			table1.addCell(createCell("甲方："+bo.getBuyer()!=null?bo.getBuyer().getName():"", false,1, fontChinese6));
			table1.addCell(createCell("", false,1, fontChinese6));
			table1.addCell(createCell(" ", false,3, fontChinese6));
			table1.addCell(createCell(" ", false,1, fontChinese6));
			table1.addCell(createCell("乙方："+bo.getVendor()!=null?bo.getVendor().getName():"", false,1, fontChinese6));
			table1.addCell(createCell("", false,1, fontChinese6));
			table1.addCell(createCell(" ", false,3, fontChinese6));
			table1.addCell(createCell(" ", false,1, fontChinese6));
			table1.addCell(createCell("签订地点"+bo.getSignAddress(), false,1, fontChinese6));
			table1.addCell(createCell("", false,1, fontChinese6));
			table1.addCell(createCell(" ", false,3, fontChinese6));
			table1.addCell(createCell(" ", false,1, fontChinese6));
			table1.addCell(createCell("签订时间：		"+year+"年		"+month+"月"+		day+"日+", false,1, fontChinese6));
			table1.addCell(createCell(" ", false,1, fontChinese6));
			document.add(table1);
			//添加新页面
			document.newPage();
			writer.setPageEmpty(false);

			iteratorTree(num,level,document,moduleItemList,fontChinese1,fontChinese7);
			
			document.newPage();
			writer.setPageEmpty(false);
			
			PdfPTable table =new PdfPTable(2);
			table.setSpacingBefore(MARGIN_TOP);
			table.setSpacingAfter(MARGIN_BOTTOM);
			table.setWidthPercentage(100);
			table.setTotalWidth(200);
			table.addCell(createCell("甲方：", false,1, fontChinese6));
			table.addCell(createCell("乙方：", false,1, fontChinese6));
			table.addCell(createCell("名称："+bo.getBuyer()!=null?bo.getBuyer().getName():"", false,1, fontChinese6));
			table.addCell(createCell("名称："+bo.getVendor()!=null?bo.getVendor().getName():"", false,1, fontChinese6));
			table.addCell(createCell("地址：", false,1, fontChinese6));
			table.addCell(createCell("地址：", false,1, fontChinese6));
			table.addCell(createCell("法人代表(委托代理人)：", false,1, fontChinese6));
			table.addCell(createCell("法人代表(委托代理人)：", false,1, fontChinese6));
			table.addCell(createCell("电话：", false,1, fontChinese6));
			table.addCell(createCell("电话：", false,1, fontChinese6));
			table.addCell(createCell("传真：", false,1, fontChinese6));
			table.addCell(createCell("传真：", false,1, fontChinese6));
			table.addCell(createCell("邮编：", false,1, fontChinese6));
			table.addCell(createCell("邮编：", false,1, fontChinese6));
			document.add(table);
			document.newPage();
			writer.setPageEmpty(false);
			document.add(new Paragraph("附加条款：",fontChinese4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建单元格
	 * @param value
	 * @param boderFlag
	 * @return
	 */
	public PdfPCell createCell(String value,boolean boderFlag,int colspan, Font textfont){  
		String valStr=value;
		PdfPCell cell = new PdfPCell();    
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);   
        cell.setColspan(colspan); 
        cell.setBorder(0);
        cell.setPadding(2.0f);    
        if(!boderFlag){    
       	 	cell.setBorderWidth(0.5f);
            cell.setPaddingTop(3.5f);    
            cell.setPaddingBottom(3.5f);    
        }    
        cell.setPhrase(new Phrase(valStr,textfont));  
       return cell;    
   } 
	
	/**
	 * 遍历moduleItemList 得要所有的条款内容
	 * @param document
	 * @param moduleItemList
	 * @param fontChinese
	 */
	private void iteratorTree(float num,int level,Document document,List<ContractContentEntity> moduleItemList,Font fontChinese,Font fontChinese7){
		 String[] str = { "", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		 String ss[] = new String[] { "", "十", "百", "千", "万", "十", "百", "千", "亿" };
		 level ++;
		 num +=25f;
		if(!CollectionUtils.isEmpty(moduleItemList)){
			for (ContractContentEntity moduleItem : moduleItemList) {
				Paragraph para = new Paragraph();
				Chunk chunk1;
				Chunk chunk2;
				String contentStr1="";
				String s = String.valueOf(moduleItem.getSqenum());
				StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < s.length(); i++) {
		            String index = String.valueOf(s.charAt(i));
		            sb = sb.append(str[Integer.parseInt(index)]);
		        }
		        String sss = String.valueOf(sb);
		        int i = 0;
		        for (int j = sss.length(); j > 0; j--) {
		            sb = sb.insert(j, ss[i++]);
		        }

				if(level == 1){
					chunk1 = new Chunk(sb+" 、", fontChinese);
				}else{
					chunk1 = new Chunk(moduleItem.getSqenum()+" 、", fontChinese);
				}
				para.add(chunk1);
				
				String contentStr = moduleItem.getContent();
				if(contentStr.indexOf("[") !=-1){
					contentStr1 = contentStr.substring(0,contentStr.indexOf("["));
					chunk2 = new Chunk(contentStr1, fontChinese);
					para.add(chunk2);
				}
				if(contentStr.indexOf("[") !=-1 && contentStr.indexOf("]") !=-1){
					contentStr1 = contentStr.substring(contentStr.indexOf("[")+1,contentStr.indexOf("]"));
					chunk2 = new Chunk(contentStr1, fontChinese7);
					para.add(chunk2);
				}
				if(contentStr.indexOf("]") !=-1){
					contentStr1 = contentStr.substring(contentStr.indexOf("]")+1,contentStr.length());
					chunk2 = new Chunk(contentStr1, fontChinese);
					para.add(chunk2);
				}

				if(!(contentStr.indexOf("[") !=-1 && contentStr.indexOf("]") !=-1)){
					chunk2 = new Chunk(contentStr, fontChinese);
					para.add(chunk2);
				}
				para.setIndentationLeft(num);	//段落缩进
				para.setAlignment(Element.ALIGN_LEFT);
				para.setExtraParagraphSpace(50);
				try {
					document.add(para);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
				List<ContractContentEntity> childList = moduleItem.getItemList();
				iteratorTree(num,level,document,childList,fontChinese,fontChinese7);
			}
		}
	}
	
	/**
	  * 写文件
	  * @param contents   文件字符串内容
	  * @param path       写入的文件地址
	  * @throws MalformedURLException 
	  */
	 public boolean writeHTML(String contents,String path) {
	  try  { 
		  	path=new File(path).toURI().toURL().toString();
	         FileOutputStream fos  =   new  FileOutputStream(path.substring(path.indexOf("/")));
	         OutputStreamWriter osw  =   new  OutputStreamWriter(fos,  "UTF-8" ); 
	         osw.write(contents); 
	         osw.flush(); 
	         osw.close();
	         fos.close();
	         return true;
	      }  catch  (Exception e) { 
	         e.printStackTrace(); 
	         return false;
	     }
	 }
	
	/**
	 * 获取合同条款Tree
	 * @return
	 */
	public List<ContractContentEntity> getAllOperateList(Long contractId) {
		//根据合同模板Id 与 合同条款的parentId 获取顶层的合同条款
		List<ContractContentEntity> topList = contractContentDao.findByContractIdAndParentId(contractId, 0l);
		//递归获取子集合同条款
		if(!CollectionUtils.isEmpty(topList)){
			recursionGetOperateList(topList); 
		}
		return topList;
	}
	
	/**
	 * 递归方法
	 * 获取子集合同条款
	 * @param topList
	 */
	private void recursionGetOperateList(List<ContractContentEntity> topList) {
		for (ContractContentEntity moduleItem : topList) {
			List<ContractContentEntity> moduleItemList = contractContentDao.findByParentId(moduleItem.getId(),moduleItem.getContractId());
			if(!CollectionUtils.isEmpty(moduleItemList)){
				moduleItem.setItemList(moduleItemList);
				recursionGetOperateList(moduleItemList);
			}	
		}
	}
	
	/**
	 * 条件查询所有的合同模板
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<ContractContentEntity> getList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ContractContentEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ContractContentEntity.class);
		Page<ContractContentEntity> page = contractContentDao.findAll(spec, pagin);
		return page;
	}
	
	/**
	 * 根据合同模板ID获得合同条款
	 * @param moduleId
	 * @return
	 */
	public List<ContractContentEntity> getContentList(Long contractId){
		List<ContractContentEntity> moduleItemList = contractContentDao.getItemByContractId(contractId,Constant.UNDELETE_FLAG);
		return moduleItemList;
	}
	
	/**
	 * 条款排序
	 * @param moduleItemList
	 * @param moduleItem
	 */
	private void resetModuleItem(List<ContractContentEntity> moduleItemList, ContractContentEntity moduleItem) {
		Integer newNum = null;
		for (ContractContentEntity contractModuleItemEntity : moduleItemList) {
			if(contractModuleItemEntity.getId() == moduleItem.getBeforeId().intValue()){
				newNum = contractModuleItemEntity.getSqenum()+1;
				continue;
			}
			if(newNum !=null){
				contractModuleItemEntity.setSqenum(contractModuleItemEntity.getSqenum()+1);
			}
		}
		moduleItem.setSqenum(newNum);
	}
	
	/**
	 * 保存条款
	 * @param moduleItemList
	 * @return
	 */
	public Map<String,Object> saveModuleItem(List<ContractContentEntity> moduleItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		for (ContractContentEntity item : moduleItemList) {
			Long parentId = item.getParentId();
			List<ContractContentEntity> itemList = contractContentDao.findByParentId(parentId,item.getContractId());
			//新增条款排序
			if(item.getBeforeId() == 0){
				item.setSqenum(itemList.size()+1);
			}else{
				resetModuleItem(itemList,item);
				contractContentDao.save(itemList);
			}
		}
		contractContentDao.save(moduleItemList);
		map.put("msg", "保存成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 更新条款
	 * @param moduleItemList
	 * @return
	 */
	public Map<String,Object> updateModuleItem(List<ContractContentEntity> moduleItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		contractContentDao.save(moduleItemList);
		map.put("msg", "更新成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除条款
	 * @param moduleItemList
	 * @return
	 */
	public Map<String,Object> deleteContent(List<ContractContentEntity> moduleItemList){
		Map<String,Object> map = new HashMap<String, Object>();
		contractContentDao.delete(moduleItemList);
		map.put("msg", "删除成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 复制条款
	 * @param menu
	 * @return
	 */
	public Map<String,Object> copyContent(ContractContentEntity moduleItem) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		ContractContentEntity _moduleItem = new ContractContentEntity();
		BeanUtil.copyPropertyNotNull(moduleItem, _moduleItem);
		_moduleItem.setId(0);
		_moduleItem.setBuyerId(user.orgId);
		contractContentDao.save(_moduleItem);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("msg", "复制成功");
		map.put("success", true);
		return map;
	}
	
	/**
	 * 移动
	 * @param moduleItem
	 * @param moveType
	 * @return
	 */
	public Map<String,Object> moveContent(ContractContentEntity moduleItem , int moveType){
		Map<String,Object> map = new HashMap<String, Object>();
		moduleItem = findOne(moduleItem.getId());
		//向上移动
		if(moveType==MOVE_UP){
			//根据合同条款的parentId与sqenum获取上级条款
			ContractContentEntity preItem = contractContentDao.findByParentIdAndSqenum(moduleItem.getParentId(), moduleItem.getSqenum()-1,Constant.UNDELETE_FLAG,moduleItem.getContractId());
			if(preItem!=null){
				moduleItem.setSqenum(moduleItem.getSqenum()-1);
				preItem.setSqenum(preItem.getSqenum()+1);
				contractContentDao.save(moduleItem);
				contractContentDao.save(preItem);
			}
		}else{
			ContractContentEntity preItem = contractContentDao.findByParentIdAndSqenum(moduleItem.getParentId(), moduleItem.getSqenum()+1,Constant.UNDELETE_FLAG,moduleItem.getContractId());
			if(preItem!=null){
				moduleItem.setSqenum(moduleItem.getSqenum()+1);
				preItem.setSqenum(preItem.getSqenum()-1);
				contractContentDao.save(moduleItem);
				contractContentDao.save(preItem);
			}
		}
		map.put("success", true);
		return map;
	}
	
	/**
	 * 创建条款的编号
	 */
	public String createContentCode(){
		String moduleItemCode = getSerialNumberService().geneterNextNumberByKey(CODE_KEY);
		return moduleItemCode;
	}
}
