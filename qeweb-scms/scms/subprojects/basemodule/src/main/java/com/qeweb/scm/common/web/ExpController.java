package com.qeweb.scm.common.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Controller
@RequestMapping("/manager/export")
public class ExpController {	
	@RequestMapping(value="/exp",method=RequestMethod.POST)
	@ResponseBody
	public void exp(HttpServletResponse response,HttpServletRequest request) throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		String _rows = request.getParameter("rows");
		String _cols = request.getParameter("cols");
		JSONArray rows = new JSONArray(_rows);
		JSONArray cols_ = new JSONArray(_cols);
		Sheet sheet = workbook.createSheet("sheet1");
		int rownum = 0;
		Row row1 = sheet.createRow(rownum);
		JSONArray cols = new JSONArray();
		for(int i=0;i<cols_.length();i++){
			JSONObject col = cols_.getJSONObject(i);
			if(col.has("title")){
				cols.put(col);
			}
		}
		for(int i=0;i<cols.length();i++){
			JSONObject col = cols.getJSONObject(i);
			Cell cell = row1.createCell(i);
			cell.setCellValue(StringUtils.convertToString(col.get("title")));
		}
		for(int j= 0; j < rows.length(); j ++) {
			row1 = sheet.createRow(j+1);
			JSONObject row = rows.getJSONObject(j);
			for(int i=0;i<cols.length();i++){
				JSONObject col = cols.getJSONObject(i);
				Cell cell = row1.createCell(i);
				cell.setCellValue(StringUtils.convertToString(row.get(StringUtils.convertToString(col.get("field")))));
			}
		}
		response.addHeader("Content-Disposition", "attachment;filename="+DateUtil.getCurrentTimeInMillis()+".xls"); 
		ServletOutputStream out = null;
		try{
			out = response.getOutputStream();
			workbook.write(out);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			if(out!=null){
				out.close();
			}
			if(workbook!=null){
				workbook.close();
			}
		}
	}
}
