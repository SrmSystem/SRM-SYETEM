package com.qeweb.scm.basemodule.web.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.AnswerEntity;
import com.qeweb.scm.basemodule.entity.QuestionnaireEntity;
import com.qeweb.scm.basemodule.entity.SubjectEntity;
import com.qeweb.scm.basemodule.service.QuestionnaireService;

@Controller
@RequestMapping(value = "/manager/Questionnaire")
public class QuestionnaireController {

	@Autowired
	private QuestionnaireService questionnaireService;

	private Map<String, Object> map;

	@LogClass(method="查看", module="问卷调查")
	@RequestMapping(value = "/getQuestionnaireList", method = RequestMethod.GET)
	public String getNoticeList(HttpServletRequest request) {
		return "back/basedata/questionnaireList";
	}

	@RequestMapping(value = "/getQuestionnaireList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getNoticeList(
			@RequestParam(value = "page") int pageNumber,
			@RequestParam(value = "rows") int pageSize, Model model,
			HttpServletRequest request) {
		Map<String, Object> searchParamMap = Servlets
				.getParametersStartingWith(request, "search-");
		Page<QuestionnaireEntity> page = questionnaireService
				.getQuestionnaireList(pageNumber, pageSize, searchParamMap,
						request);
		map = new HashMap<String, Object>();
		map.put("rows", page.getContent());
		map.put("total", page.getTotalElements());
		return map;
	}

	@RequestMapping(value = "deleteQuestionnaire", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteQuestionnaire(
			@RequestBody List<QuestionnaireEntity> questionnaireList) {
		Map<String, Object> map = new HashMap<String, Object>();
		questionnaireService.abolish(questionnaireList);
		map.put("success", true);
		return map;
	}

	@RequestMapping(value = "releaseQuestionnaire", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> releaseQuestionnaire(
			@RequestBody List<QuestionnaireEntity> questionnaireList) {
		Map<String, Object> map = new HashMap<String, Object>();
		questionnaireService.releaseQuestionnaire(questionnaireList);
		map.put("success", true);
		return map;
	}

	@RequestMapping(value = "delsQuestionnaire", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delsQuestionnaire(
			@RequestBody List<QuestionnaireEntity> questionnaireList) {
		Map<String, Object> map = new HashMap<String, Object>();
		questionnaireService.delsQuestionnaire(questionnaireList);
		map.put("success", true);
		return map;
	}

	@RequestMapping(value = "addquestionnaire", method = RequestMethod.POST)
	@ResponseBody
	public String addquestionnairePost(HttpServletRequest request) {
		String qid = request.getParameter("qid");
		String sxlzongyang = request.getParameter("sxlzongyang");
		String[] sxltitles = request.getParameterValues("sxltitle");
		String[] sxltitletypes = request.getParameterValues("sxltitletype");
		String[] idss = request.getParameterValues("ids");
		String endTime = request.getParameter("endTime");
		if (null == sxltitles ||sxltitles.equals("")) {
			return "请添加题目";
		}
		List<String[]> list=new ArrayList<String[]>();
		for (String id : idss) {
			id = id.split("-")[1];
			String[] sxltitlesa = request.getParameterValues("sxltitlesaa"
					+ id);
			if(sxltitlesa!=null)
			{
				list.add(sxltitlesa);
			}
		}
		return questionnaireService.addquestionnairePost(sxlzongyang,
				sxltitles, sxltitletypes, idss, list,qid,endTime);
	}

	@RequestMapping(value = "/updatequestionnaire/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String updatequestionnaire(@PathVariable("id") Long id) {
		return questionnaireService.updatequestionnaireGet( id);
	}
	@RequestMapping(value = "/looksquestionnaire/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String looksquestionnaire(@PathVariable("id") Long id) {
		return questionnaireService.looksquestionnaire( id);
	}

	@RequestMapping(value = "/updatequestionnairePost", method = RequestMethod.POST)
	public String updatequestionnairePost(HttpServletRequest request) {
		questionnaireService.updatequestionnairePost(request);
		return "back/basedata/questionnaireList";
	}

	@RequestMapping(value = "/addquestionnaires")
	@ResponseBody
	public String addnoties(String ctx) {

		return "<iframe src='"+ctx+"/manager/Questionnaire/addquestionnaire' width='100%' height='99%' frameborder='0'></iframe>";
	}
	@RequestMapping(value="/getQuestions")
	@ResponseBody
	public String  getQuestionsStars(String ctx){
		return "<iframe src='"+ctx+"/manager/Questionnaire/getQuestionsStars' width='99%' style='min-height:98%;'frameborder='0'></iframe>";
	}
	@RequestMapping(value="/getQuestionsStars")
	public String getQuestionsStars(HttpServletRequest httpServletRequest)
	{
		httpServletRequest.setAttribute("list", questionnaireService.getQuestionsStars());
		return "back/questionnaire";
	}
	@RequestMapping(value="/getQuestionsS/{id}")
	public String getQuestions(@PathVariable("id") Long id, HttpServletRequest httpServletRequest)
	{
		if(questionnaireService.juctRol())//判断是否是采购方
		{
			httpServletRequest.setAttribute("bs",1);
		}
		else
		{
			questionnaireService.looknumber(id);
		}
		httpServletRequest.setAttribute("questionnaire", questionnaireService.getQuestionnaire(id));
		httpServletRequest.setAttribute("subjectAnswer",questionnaireService.getSubjectAnswer(id));
		return "/back/lookQuestionnaire";
	}
	@RequestMapping(value="/getQuestionsSS/{id}")
	public String getQuestionsS(@PathVariable("id") Long id, HttpServletRequest httpServletRequest)
	{
		httpServletRequest.setAttribute("bs",1);
		httpServletRequest.setAttribute("questionnaire", questionnaireService.getQuestionnaire(id));
		httpServletRequest.setAttribute("subjectAnswer",questionnaireService.getSubjectAnswer(id));
		return "/back/lookQuestionnaire";
	}
	@RequestMapping(value="/sumblicUserQuestions")
	@ResponseBody
	public String sumblicUserQuestions(HttpServletRequest httpServletRequest)
	{
		int i=0;
		String quesId=(String)(httpServletRequest.getParameter("quesId"));
		questionnaireService.annumber(Long.parseLong(quesId));
		if(null==quesId||quesId.equals(""))
		{
			return "问卷ID为空";
		}
		Map<SubjectEntity, List<AnswerEntity>> linkedHashMap=questionnaireService.getSubjectAnswer(Long.parseLong(quesId));
		Iterator<Entry<SubjectEntity, List<AnswerEntity>>> it = linkedHashMap.entrySet().iterator();
		while(it.hasNext())
		{
			i++;
			SubjectEntity su=it.next().getKey();
			 List<AnswerEntity> answerEntitys=linkedHashMap.get(su);
			 if(answerEntitys.get(0).getType()==1)
			 {
				 String anr=(String)(httpServletRequest.getParameter("anr"+su.getId()));
				 if(null==anr||anr.equals(""))
				 {
					return "问题编号"+i+",没有填写答案！！";
				 }
				 questionnaireService.sumblicUserQuestions1(anr);
			 }
			 if(answerEntitys.get(0).getType()==2)
			 {
				 String[] anc=httpServletRequest.getParameterValues("anc"+su.getId());
				 if(null==anc||anc.length==0)
				 {
					 return "问题编号"+i+",没有填写答案！！";
				 }
				 questionnaireService.sumblicUserQuestions2(anc);
			 }
			 if(answerEntitys.get(0).getType()==3)
			 {
				 String[] ant=httpServletRequest.getParameterValues("ant"+su.getId());
				 if(null==ant||ant.length==0)
				 {
					 return "问题编号"+i+",没有填写答案！！";
				 }
				 questionnaireService.sumblicUserQuestions3(ant,su);
			 }
		}
		return "1";
	}
}
