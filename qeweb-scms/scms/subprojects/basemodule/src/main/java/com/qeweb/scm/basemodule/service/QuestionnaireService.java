package com.qeweb.scm.basemodule.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.entity.AnswerEntity;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.QuestionnaireEntity;
import com.qeweb.scm.basemodule.entity.SubjectEntity;
import com.qeweb.scm.basemodule.entity.UserAnswerEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.AnswerDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.QuestionnaireDao;
import com.qeweb.scm.basemodule.repository.SubjectDao;
import com.qeweb.scm.basemodule.repository.UserAnswerDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class QuestionnaireService {
	
	@Autowired
	private QuestionnaireDao questionnaireDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private AnswerDao answerDao;
	@Autowired
	private UserAnswerDao userAnswerDao;
	@Autowired
	private OrganizationDao orgDao;

	public Page<QuestionnaireEntity> getQuestionnaireList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap,
			HttpServletRequest request) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		searchParamMap.put("EQ_abolished","0");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<QuestionnaireEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), QuestionnaireEntity.class);
		Page<QuestionnaireEntity> page = questionnaireDao.findAll(spec,pagin);
		return page;
	}

	public void abolish(List<QuestionnaireEntity> questionnaires) {
		
		for(QuestionnaireEntity questionnaire:questionnaires)
		{
			questionnaireDao.abolish(questionnaire.getId());
		}
	}

	public String addquestionnairePost(String sxlzongyang, String[] sxltitles, String[] sxltitletypes, String[] idss, List<String[]> list, String qid, String endTime) 
	{
		if(null==list||list.size()!=sxltitles.length)
		{
			return "有题目没有添加答案";
		}
		QuestionnaireEntity questionnaire=new QuestionnaireEntity();
		if(qid==null)
		{
			questionnaire.setTitle(sxlzongyang);
			questionnaire.setEndTime(Timestamp.valueOf(endTime));
			questionnaire.setLookNumber(0);
			questionnaire.setAnswerNumber(0);
		}
		else
		{
			questionnaire=questionnaireDao.findOne(Long.parseLong(qid));
			questionnaire.setTitle(sxlzongyang);
			questionnaire.setEndTime(Timestamp.valueOf(endTime));
			List<SubjectEntity> lists=subjectDao.findByQuesId(questionnaire.getId());
			subjectDao.delete(lists);
			for(SubjectEntity ss:lists)
			{
				answerDao.delete(answerDao.findBySubjectId(ss.getId()));
			}
		}
		questionnaireDao.save(questionnaire);
		List<AnswerEntity> answerEntities=new ArrayList<AnswerEntity>();
		for(int i=0;i<sxltitles.length;i++)
		{
			SubjectEntity subject=new SubjectEntity();
			subject.setTitle(sxltitles[i]);
			subject.setQuesId(questionnaire.getId());
			subjectDao.save(subject);
			
			for(int j=0;j<list.get(i).length;j++)
			{
				AnswerEntity answerEntity=new AnswerEntity();
				if(!(list.get(i)[j].equals("")))
					answerEntity.setTitle(list.get(i)[j]);
				else if(!(sxltitletypes[i].equals("3")))
				{
					return "有题目没有添加答案";
				}
				answerEntity.setSubjectId(subject.getId());
				answerEntity.setType(Integer.parseInt(sxltitletypes[i]));
				answerEntity.setChoiceNumber(0);
				answerEntities.add(answerEntity);
			}
			answerDao.save(answerEntities);
		}
		return "1";
	}

	public void releaseQuestionnaire(List<QuestionnaireEntity> questionnaireList) {
		BaseEntity baseEntity=new BaseEntity();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return ;
		}
		String loginName=user.loginName;
		UserEntity userentiy=userDao.findByLoginName(loginName);
		for(QuestionnaireEntity questionnaire:questionnaireList)
		{
			questionnaireDao.updateQuestionnaire(userentiy.getName(),DateUtil.getCurrentTimestamp(),1,questionnaire.getId());
		}
	}
	public void delsQuestionnaire(List<QuestionnaireEntity> questionnaireList) {
		for(QuestionnaireEntity questionnaire:questionnaireList)
		{
			questionnaireDao.updateQuestionnaire(DateUtil.getCurrentTimestamp(),-1,questionnaire.getId());
		}
	}

	public String updatequestionnaireGet(Long id) {
		
		String s="";
		
		List<SubjectEntity> subjects=subjectDao.findByQuesId(id);
		
		for(SubjectEntity subject:subjects)
		{
			List<AnswerEntity> list=answerDao.findBySubjectId(subject.getId());
			s=s+"<tr id='p"+subject.getId()+"'>"
			+"<td><input class='easyui-textbox' data-options='required:true' style='width:100%' name='sxltitle' value='"+subject.getTitle()+"'/></td>"
			+"<td><select name='sxltitletype'class='easyui-combobox' style='width:100%' data-options=\"required:true,editable:false,value:'"+list.get(0).getType()+"'\"><option value='1'>单选题目</option><option value='2'>多选题目</option> <option value='3'>文本题目</option></select></td><td><table id='a"+subject.getId()+"'>";
			for(AnswerEntity a:list)
			{
				if(null==a.getTitle()||a.getTitle().equals(""))
				{
					a.setTitle("");
				}
				s=s+"<tr id=\"xtd'"+subject.getId()+a.getId()+"\"><td><input class=\"easyui-textbox\" name=\"sxltitlesaa"+subject.getId()+"\" style=\"width:100%\" value=\""+a.getTitle()+"\"/></td></tr>";
			}
			s=s+"</table><a  onclick='addAwent(\"a"+subject.getId()+"\")' class='easyui-linkbutton' data-options='iconCls:\"icon-add\"'>添加答案</a></td>"
			+"<td><a href='javascript:;' id='sc"+subject.getId()+"' class='easyui-linkbutton' onclick='deletep(\"p"+subject.getId()+"\")'>删除</a>"
			+"<input type='hidden' name='ids' value='s-"+subject.getId()+"'/><input type='hidden' id='ipsa"+subject.getId()+"' value='0'/></td>"
			+"</tr>";
		}
		return s;
	}
	public String looksquestionnaire(Long id) {
		
		String s="";
		
		List<SubjectEntity> subjects=subjectDao.findByQuesId(id);
		
		for(SubjectEntity subject:subjects)
		{
			List<AnswerEntity> list=answerDao.findBySubjectId(subject.getId());
			s=s+"<tr id='p"+subject.getId()+"'>"
					+"<td><input class='easyui-textbox' data-options='required:true,disabled:true' style='width:100%' name='sxltitle' value='"+subject.getTitle()+"'/></td>"
					+"<td><select name='sxltitletype'class='easyui-combobox' style='width:100%' data-options=\"required:true,disabled:true,value:'"+list.get(0).getType()+"'\"><option value='1'>单选题目</option><option value='2'>多选题目</option> <option value='3'>文本题目</option></select></td><td id='a"+subject.getId()+"'><table>";
			for(AnswerEntity a:list)
			{
				if(null==a.getTitle()||a.getTitle().equals(""))
				{
					a.setTitle("");
				}
				s=s+"<tr id=\"xtd'"+subject.getId()+a.getId()+"\"><td><input class=\"easyui-textbox\" name=\"sxltitlesaa"+subject.getId()+"\" style=\"width:100%\" value=\""+a.getTitle()+"\" data-options='required:true,disabled:true'/></td></tr>";
			}
			s=s+"</table></td>"
					+"<td>"
					+"</td>"
					+"</tr>";
		}
		return s;
	}
	public void updatequestionnairePost(HttpServletRequest request) {
		String changT= request.getParameter("changT");
		String changS= request.getParameter("changS");
		String changA= request.getParameter("changA");
		String zongTitleid= request.getParameter("zongTitleid");
		if(changT!=null&&!(changT.equals("")))
		{
			BaseEntity baseEntity=new BaseEntity();
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if(user==null){
				baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
				baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
				return ;
			}
			String loginName=user.loginName;
			UserEntity userentiy=userDao.findByLoginName(loginName);
			String zongTitle= request.getParameter("zongTitle");
			questionnaireDao.updateQuestionnaire(zongTitle,userentiy.getId(),userentiy.getName(),DateUtil.getCurrentTimestamp(),Long.parseLong(zongTitleid));
		}
		if(changS!=null&&!(changS.equals("")))
		{
			for(String s:changS.split(","))
			{
				String subjects= request.getParameter("subjects"+s);
				subjectDao.updatesubject(subjects,Long.parseLong(s));
			}
		}
		if(changA!=null&&!(changA.equals("")))
		{
			for(String a:changA.split(","))
			{
				String answer= request.getParameter("answer"+a);
				answerDao.updateanswer(answer,Long.parseLong(a));
			}
		}
	}

	public List<QuestionnaireEntity> getQuestionsStars() {
		List<QuestionnaireEntity>  list=questionnaireDao.getQuestionnaireEntityAndStatus(DateUtil.getCurrentTimestamp(),1);
		for(int i=0;i<list.size();i++)
		{
			BaseEntity baseEntity=new BaseEntity();
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if(user==null){
				baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
				baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
				return null;
			}
			String loginName=user.loginName;
			UserEntity userentiy=userDao.findByLoginName(loginName);
			List<UserAnswerEntity> lists=userAnswerDao.findByQuestionnaireIdAndCreateUserId(list.get(i).getId(),userentiy.getId());
			if(null!=lists&&lists.size()>0)
			{
				list.remove(i);
				i--;
			}
		}
		return list;
	}

	public void looknumber(Long id)
	{
		QuestionnaireEntity  questionnaireEntity=questionnaireDao.findOne(id);
		if(questionnaireEntity.getAnswerNumber()==null)
			questionnaireEntity.setLookNumber(1);
		else
			questionnaireEntity.setLookNumber(questionnaireEntity.getLookNumber()+1);
		questionnaireDao.save(questionnaireEntity);
	}
	public void annumber(Long id)
	{
		QuestionnaireEntity  questionnaireEntity=questionnaireDao.findOne(id);
		if(questionnaireEntity.getAnswerNumber()==null)
			questionnaireEntity.setAnswerNumber(1);
		else
			questionnaireEntity.setAnswerNumber(questionnaireEntity.getAnswerNumber()+1);
		questionnaireDao.save(questionnaireEntity);
	}
	
	public QuestionnaireEntity getQuestionnaire(Long id) {
		QuestionnaireEntity  questionnaireEntity=questionnaireDao.findOne(id);
		return questionnaireEntity;
	}
	
	public boolean juctRol()
	{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		OrganizationEntity org = orgDao.findOne(user.orgId);
		if(OrgType.ROLE_TYPE_BUYER.equals(org.getRoleType())) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Map<SubjectEntity, List<AnswerEntity>> getSubjectAnswer(Long id) {
		List<SubjectEntity> list=subjectDao.findByQuesId(id);
		Map<SubjectEntity, List<AnswerEntity>> linkedHashMap =new LinkedHashMap<SubjectEntity, List<AnswerEntity>>();
		for(SubjectEntity subjectEntity:list)
		{
			List<AnswerEntity> answer=answerDao.findBySubjectId(subjectEntity.getId());
			for(AnswerEntity an:answer)
			{
				if(an.getType()==3)
				{
					an.setAnswerEntitys(userAnswerDao.findByAnswerIdAndQuestionnaireId(an.getId(),id));
				}
			}
			linkedHashMap.put(subjectEntity, answer);
		}
		return linkedHashMap;
	}
	public void sumblicUserQuestions1(String anr) {
		
		
		AnswerEntity am=answerDao.findOne(Long.parseLong(anr));
		am.setChoiceNumber(am.getChoiceNumber()+1);
		answerDao.save(am);
		
		SubjectEntity su=subjectDao.findOne(am.getSubjectId());
		
		UserAnswerEntity ua=new UserAnswerEntity();
		ua.setQuestionnaireId(su.getQuesId());
		ua.setAnswerId(am.getId());
		userAnswerDao.save(ua);
	}

	public void sumblicUserQuestions2(String[] anc) {
		for(String id:anc)
		{
			AnswerEntity am=answerDao.findOne(Long.parseLong(id));
			am.setChoiceNumber(am.getChoiceNumber()+1);
			answerDao.save(am);
			SubjectEntity su=subjectDao.findOne(am.getSubjectId());
			
			UserAnswerEntity ua=new UserAnswerEntity();
			ua.setQuestionnaireId(su.getQuesId());
			ua.setAnswerId(am.getId());
			userAnswerDao.save(ua);
		}
	}

	public void sumblicUserQuestions3(String[] ant, SubjectEntity su2) {
		List<AnswerEntity> answerEntitys=answerDao.findBySubjectId(su2.getId());

		int i=0;
		for(AnswerEntity am:answerEntitys)
		{
			am.setChoiceNumber(am.getChoiceNumber()+1);
			answerDao.save(am);
			
			UserAnswerEntity ua=new UserAnswerEntity();
			ua.setQuestionnaireId(su2.getQuesId());
			ua.setAnswerId(am.getId());
			ua.setContent(ant[i]);
			userAnswerDao.save(ua);
		}
	}

}
