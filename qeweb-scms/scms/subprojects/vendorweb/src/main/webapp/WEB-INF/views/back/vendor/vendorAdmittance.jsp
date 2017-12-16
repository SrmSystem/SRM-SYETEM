<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<html>
<head>
	<title>供应商准入创建</title>
	<script type="text/javascript">
	  var vendorPropertyConstants = <%=VendorModuleTypeConstant.getVendorPropertyJson()%>;
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorAdmittance.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/component/surveyView.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorInfor.js"></script>
</head>

<body style="margin:0;padding:0;">
<div id="vendorAdmittance-tabs" class="easyui-tabs"  data-options="tabPosition:'left',fit:true,onSelect:VendorAdmittance.refreshTabDatagrid">
  <c:forEach items="${phaseList}" var="phase">
    <div title="${phase.name}" itemId="${phase.id}"  style="padding:10px">
      <table id="datagrid-${phase.id}" class="easyui-datagrid" data-options="fit:true,
        url:'${ctx}/manager/vendor/vendorBaseInfo',method:'post',singleSelect:false,
        queryParams:{'search-EQ_phaseId':${phase.id},'search-EQ_currentVersion':1},
        pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
        toolbar:'#tt-${phase.id}'
      ">
        <thead>
          <tr>
            <th data-options="field:'id',checkbox:true">供应商代码</th>
            <th data-options="field:'operate',formatter:VendorAdmittance.vendorOptFmt">操作</th>
            <th data-options="field:'code'">供应商代码</th>
            <th data-options="field:'shortName',formatter:BaseVendor.viewFmt">供应商名称</th>
            <th data-options="field:'property',formatter:function(v,r,i){return vendorPropertyConstants[v];}">企业属性</th>
            <th data-options="field:'countryText'">国家</th>
            <th data-options="field:'provinceText'">省份</th>
            <th data-options="field:'regtime'">注册时间</th>
            <th data-options="field:'surveySubmitInfo',align:'center',styler:VendorAdmittance.cellRender.survey">调查表提交</th>
            <th data-options="field:'surveyAuditInfo',align:'center',styler:VendorAdmittance.cellRender.survey,formatter:VendorAdmittance.auditSurveyFmt">调查表审核</th>
            <th data-options="field:'enableStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(r.org.enableStatus,'enable',true);}">状态</th>
          </tr>
        </thead>
      </table>
      <div id="tt-${phase.id}">
        <div>
<%--           <a class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="VendorAdmittance.promote(${phase.id})">晋级</a> --%>
			<a class="easyui-linkbutton" data-options="iconCls:'icon-download',plain:true" onclick="exp(${phase.id})">导出</a>
        </div>
        <div>
          <form id="form-${phase.id}">
                                 供应商代码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                                 供应商名称:<input class="easyui-textbox" name="search-LIKE_shortName" type="text" style="width:80px;"/>
                                 状态:<input class="easyui-combobox" data-options="
	               url:'${ctx}/manager/vendor/vendorBaseInfo/getSturst2',    
	               valueField:'id',    
	               textField:'text'" 
	               name="search-EQ_org.confirmStatus"  style="width:80px;"/>
                       <input  name="search-EQ_phaseId" type="hidden" style="width:80px;" value="${phase.id}"/>
                       <input  name="search-EQ_currentVersion" type="hidden" style="width:80px;" value="1"/>
           <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchVendorPhase(${phase.id})">查询</a>          
          </form>
        </div>
      </div>
    </div>	  
  </c:forEach>
</div>
	          <div id="addd" class="easyui-dialog" title="供应商" style="width:60%;height:90%;"   
        data-options="closed:true">   
</div> 
<div id="win-materialSupplyRel" class="easyui-dialog" title="My Dialog" style="width:70%;height:70%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true">   
	   <table id="datagrid-materialSupplyRel-list" title="供货关系详情列表" class="easyui-datagrid"
					data-options="method:'post',singleSelect:true,
					pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
					>
						<thead><tr>
						<th data-options="field:'materialName',formatter:function(v,r){if(r.material) return r.material.code; else ''}">物料图号</th>
						<th data-options="field:'vendorName',formatter:function(v,r){if(r.material) return r.material.name; else ''}">物料名称</th>
						<th data-options="field:'material',formatter:function(v,r){if(r.material) return r.material.partsCode; else ''}">所属车型</th>
						<th data-options="field:'vendorId',formatter:function(v,r){if(r.material) return r.material.partsName; else ''}">所属零部件</th>
						<th data-options="field:'dataFrom'">数据来源</th>
						<th data-options="field:'nopos',formatter:BaseVendor.vfmt8">操作</th>
						</tr></thead>
				</table>   
				<div id="gonghuoxishu">
					<table id="tb" type="tb" class="table table-bordered table-condensed" >
							<thead  class="datagrid-header"><tr class="datagrid-cell">
								<th>业务范围</th>
								<th>业务类型</th>
								<th>品牌</th>
								<th>产品线</th>
								<th>工厂</th>
								<th>是否主供</th>
								<th>供货系数</th>
							</tr></thead>
					        <tbody id="fixedsid">
					        <tr> 
					        </tr>
					        </tbody>   
					</table>
				</div>
</div> 
<div id="dialog-audit" class="easyui-dialog" data-options="modal:true,closed:true,title:'调查表审核',width:400,height:200">
</div>
<div id="dialog-change" class="easyui-dialog" 
data-options="modal:true,closed:true,title:'供应商变更',
toolbar:'#dialog-change-tt'
">
  <div itemId="ct">
    <form id="dialog-change-form" action="">
    <input type="hidden" itemId="phaseId" name="phaseId"/>
    <input type="hidden" itemId="orgId" name="orgId"/>
    <input type="hidden" itemId="enableStatus" name="enableStatus"/>
    <input type="hidden" itemId="changeType" name="changType"/>
    <input type="hidden" itemId="changeTypeText" name="changTypeText"/>
	<div style="text-align:center;padding:10px;">
	  <div>
	        说明：<input class="easyui-textbox" itemId="changeReason" name="changeReason" data-options="multiline:true,height:100"/>
	  </div>
	</div>
	</form>
	<div id="dialog-change-tt">
      <a class="easyui-linkbutton" href="javascript:;" data-options="iconCls:'icon-ok'" onclick="VendorAdmittance.enableSubmit()">提交</a>
    </div>
  </div>

</div>



<!-- 基本信息审核弹出框 -->
<div id="window-auditSubmit-tt">
  <a class="easyui-linkbutton" href="javascript:;" data-options="iconCls:'icon-ok'" onclick="VendorAdmittance.auditBaseSubmit()">提交</a>
</div>
<div id="window-auditSubmit" title="审核" class="easyui-dialog" data-options="modal:true,width:350,height:300,
closed:true,
toolbar:'#window-auditSubmit-tt'">
<form id="form-auditSubmit" action="${ctx}/manager/vendor/admittance/auditSurvey">
<input type="hidden" name="id" value="${vendorSurveyCfg.id}"/>
<div style="text-align:center;padding:10px;">
  <div>
        通过<input type="radio" style="width: 20px;" name="auditStatus" value="1" checked="checked"/>驳回<input name="auditStatus" style="width: 20px;" value="-1" type="radio"/>
  </div>
  <div>
        原因：<input class="easyui-textbox" name="auditReason" data-options="multiline:true,height:100"/>
  </div>
</div>
</form>
</div>



<!-- 审核弹出框 -->
<div id="window-survey-auditSubmit-tt">
  <a class="easyui-linkbutton" href="javascript:;" data-options="iconCls:'icon-ok'" onclick="VendorAdmittance.auditSubmit()">提交</a>
</div>
<div id="window-survey-auditSubmit" title="审核" class="easyui-dialog" data-options="width:350,height:300,
closed:true,
modal:true,
toolbar:'#window-survey-auditSubmit-tt'">
<form id="form-survey-auditSubmit" action="${ctx}/manager/vendor/admittance/auditSurvey" method="post">
<input type="hidden" itemId="id" name="id"/>
<div style="text-align:center;padding:10px;">
  <div>
        通过<input type="radio" id="auditStatus" name="auditStatus" value="1"/>驳回<input id="auditStatus1" name="auditStatus" value="-1" type="radio"/>
  </div>
  <div>
        原因：<input class="easyui-textbox" name="auditReason" data-options="multiline:true,height:100"/>
  </div>
</div>
</form>
</div>

</div>

<div id="dialog-survey-hisview" class="easyui-dialog"  data-options="modal:true,closed:true,title:'历史查看'">

</div>

<!-- 降级弹出框 -->
<div id="dialog-demotion" class="easyui-dialog" data-options="
  modal:true,closed:true,title:'供应商降级',iconCls:'icon-application',
  buttons:[{
   text:'提交',
   iconCls:'icon-save',
   handler:function(){VendorAdmittance.demotionSubmit();}
  }]
">
 <input type="hidden" itemId="phaseId" name="phaseId"/>
 <input type="hidden" itemId="vendorId" name="vendorId"/>
 <input type="hidden" itemId="orgId" name="orgId"/>
 <input type="hidden" itemId="changeType" name="changType"/>
 <input type="hidden" itemId="changeTypeText" name="changTypeText"/>
  <div itemId="ct" class="text-center">
    <table id="datagrid-demotion-phase" class="easyui-datagrid" data-options="
        queryParams:{'vendorId':${vendor.id}},
        method:'post',singleSelect:true,title:'可降级阶段',
        width:400,style:{margin:'0 auto'}
    "
    
    
    >
      <thead>
        <tr>
          <th data-options="field:'id',checkbox:true"></th>
          <th data-options="field:'phaseSn'">阶段顺序</th>
          <th data-options="field:'phaseName'">阶段名称</th>
        </tr>
      </thead>
    </table>
    <div style="margin-top:2px;width:80%;">
              降级说明:<input class="easyui-textbox" itemId="changeReason" name="changeReason" data-options="multiline:true,height:100"/>
    </div>
  </div>

  
</div>
<script type="text/javascript">
function exp(phaseId){
	$('#form-'+phaseId).form('submit',{
		url:ctx+'/manager/vendor/vendorBaseInfo/exportExcel/'+phaseId, 
		success:function(data){
			$.messager.progress('close');
		}
	});
}
function searchVendorPhase(id) {
	var searchParamArray = $('#form-'+id).serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-'+id).datagrid('load', searchParams);
	
}
</script>
</body>
</html>
