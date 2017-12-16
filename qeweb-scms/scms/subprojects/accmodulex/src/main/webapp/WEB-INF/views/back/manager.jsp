<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.qeweb.scm.basemodule.context.ProjectContextUtil"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
</script>

<html>
<head>
<title><spring:message code="<%=ProjectContextUtil.getProjectName() %>"/></title>
<script type="text/javascript" src="${ctx}/static/base/easyui/extension/datagrid-scrollview.js"></script>
 	<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/css/smoothness/jquery.ui.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctx }/static/cuslibs/notie/css/css.css"/>
    <link rel="stylesheet" href="${ctx }/static/cuslibs/notie/themes/default/default.css" />
	<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/plugins/code/prettify.css" />
	<script charset="utf-8" src="${ctx }/static/cuslibs/notie/kindeditor-min.js"></script>
	<script charset="utf-8" src="${ctx }/static/cuslibs/notie/lang/zh_CN.js"></script>
	<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="commentArea"]', {
				cssPath : ctx+'/static/cuslibs/notie/plugins/code/prettify.css',
				uploadJson : ctx+'/static/cuslibs/notie/upload_json.jsp',
				fileManagerJson : ctx+'/static/cuslibs/notie/file_manager_json.jsp',
				allowFileManager : true,
				readonlyMode :true,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
				},
				afterBlur: function(){this.sync();}
			});
		});
	</script>
<style type="text/css">
#manager-menu a{
text-decoration: none;
color: #333;
}
</style>


</head>
<body>

<div id="manager-menu" data-options="region:'west',split:true,iconCls:'icon-house'" style="width: 205px; padding: 1px;" title="<spring:message code="sys.myapp"/>">
        <ul id="tree-menu" class="easyui-tree" data-options="lines:false,animate:true">
          <c:forEach items="${menuList}" var="menu" varStatus="menuStatus">
             <li>
               <span><spring:message code="${menu.viewName }"/></span>
               <c:if test="${menu.itemList!=null}">
			      <ul>
			        <tags:menuRe list="${menu.itemList}"></tags:menuRe>
			      </ul>
			   </c:if>
             </li>
          </c:forEach>
        </ul>	

</div>
<div id="manager-content" data-options="region:'center',border:true">
	<div class="easyui-tabs" id="manager-tab" data-options="fit:true, border:false,
	tools:'#manager-content-tabs-tool'
	">
		<div id="desk" title="<spring:message code="sys.desk"/>" data-options="iconCls:'icon-computer'">
		<div class="easyui-layout" data-options="fit: true">
		<div data-options="region: 'center', border: false" style="overflow: hidden;">
		  <div id="desk-pp" style="position: relative;">
		  
			<div style="width:30%;">
				<div title="<spring:message code="sys.backlog"/>" data-options="iconCls:'icon-clock',collapsible:true,closable:true,tools:'#manager-content-backlogs-tool'" style="height:250px;">
			    	<table id="datagrid-backlogcfg" class="easyui-datagrid"
						data-options="fit:true,
						url:'${ctx}/manager/backlog/list',method:'post',singleSelect:false,
						view:scrollview,rownumbers:true,queryParams:{page:1,rows:10},
				        autoRowHeight:false,pageSize:10"			       
						>
						<thead><tr>
						<th data-options="field:'id',hidden:true"></th>
						<th data-options="field:'viewId',hidden:true"></th>
						<th data-options="field:'viewName',width:100"><spring:message code="sys.backlog.module"/></th>
						<th data-options="field:'content',width:130"><spring:message code="sys.backlog.content"/></th>
						<th data-options="field:'count',formatter:backlogFmt"><spring:message code="sys.backlog.count"/></th>
						</tr></thead>
					</table>
			    </div>
			    
			    
			    <div title="文件协同" data-options="iconCls:'icon-note',collapsible:true,closable:true,tools:'#manager-content-files-tool'" style="height:250px;">
			    	<table id="datagrid-filelogcfg" class="easyui-datagrid"
						data-options="fit:true,
						url:'  ${ctx}/manager/file/fileCollaboation/getViewFileCollaborationList',method:'post',singleSelect:false,
						view:scrollview,rownumbers:true,queryParams:{page:1,rows:10},
				        autoRowHeight:false,pageSize:10"			       
						>
						<thead><tr>
						<th data-options="field:'title',width:80">文件协同类型</th>
						<th data-options="field:'typeName',width:60,formatter:function(v,r,i){return r.collaborationType.name;}">协同类型</th>
						<th data-options="field:'download',width:80,formatter:fileDownloadFmt">模板下载</th>
						<th data-options="field:'upload',width:80 ,formatter:fileuploadFmt">文件上传</th>
						<th data-options="field:'opt', width:50 ,formatter:fileFmt">操作</th>
						</tr></thead>
					</table>
			    </div>
			    

			</div> 

			 <div style="width:30%;">
			 

			  <div title="预警提醒" data-options="iconCls:'icon-comment',closable:true,tools:'#manager-content-warn-tool'" style="height:250px;">
                   <ul id="warnListWarn">

					</ul>
				</div> 
				
               <div title="异常反馈" data-options="iconCls:'icon-note',collapsible:true,closable:true,tools:'#manager-content-exceptions-tool'" style="height:250px;">
			    	<table id="datagrid-abnormalBack" class="easyui-datagrid"
						data-options="fit:true,
						url:'  ${ctx}/manager/abnormal/abnormalFeedback/getViewabnormalFeedbackList',method:'post',singleSelect:false,
						view:scrollview,rownumbers:true,queryParams:{page:1,rows:10},
				        autoRowHeight:false,pageSize:10"			       
						>
						<thead><tr>
						<th data-options="field:'abnormalFeedbackName',formatter:abnormalFeedbackNameFmt,width:100">异常反馈名称</th>
						<th data-options="field:'effectiveStartDate',width:80">开始日期</th>
						<th data-options="field:'effectiveEndDate',width:80">结束日期</th>
						<th data-options="field:'publishName', width:80 ">发布人</th>
						</tr></thead>
					</table>
			    </div>

				
				
				
				
			</div>
					

			 <div style="width:30%;">
			    <div title="<spring:message code="sys.message"/>" data-options="iconCls:'icon-comment',closable:true,tools:'#manager-content-messages-tool'" style="height:250px;">
                   <ul id="warnListInfo">

					</ul>
				</div> 
				
				<div id="gg" title="<spring:message code="sys.notice"/>" data-options="iconCls:'icon-note',closable:true,tools:'#manager-content-notics-tool'" style="min-height:250px;">
				</div> 
				

			</div>


		</div>
		</div>
		</div>

		</div>
	</div>
</div>
<div id="manager-content-tabs-tool">
	<a href="javascript:;" title="刷新页面" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadTab()"></a>
	<a href="javascript:;" title="关闭所有选项卡" class="easyui-linkbutton" plain="true" iconCls="icon-cancel" onclick="closeTab()"></a>
</div>

<div id="manager-content-backlogs-tool">
	<a href="javascript:;" title="刷新待办" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadDesk(1)"></a>
</div>

<div id="manager-content-files-tool">
	<a href="javascript:;" title="刷新文件协同" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadDesk(2)"></a>
</div>

<div id="manager-content-notics-tool">
	<a href="javascript:;" title="刷新内部公告" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadDesk(6)"></a>
</div>

<div id="manager-content-exceptions-tool">
	<a href="javascript:;" title="刷新异常反馈" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadDesk(3)"></a>
</div>

<div id="manager-content-messages-tool">
	<a href="javascript:;" title="刷新消息提醒" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadDesk(4)"></a>
</div>

<div id="manager-content-warn-tool">
	<a href="javascript:;" title="刷新预警提醒" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadDesk(5)"></a>
	</div>(5)"></a>
</div>

<div id="dd" class="easyui-dialog" title="公告详细内容" style="width:90%;height:90%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true">   
</div> 
				 <div id="w" class="easyui-window" title="Window Layout" data-options="iconCls:'icon-save',closed:true,resizable:true,modal:true" style="width:80%;height:80%;">

		</div>
		
		
	<div id="win-upload-addoredit" class="easyui-window" title="上传文件"
		style="width: 400px; height: 200px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-vendor'">
		<div align="center">
		<form id="form-upload-addoredit" method="post" enctype="multipart/form-data">
		<input type="hidden"  id="fileCollaborationId" name="fileCollaborationId"  />
			<table style="text-align: right; padding: 5px; margin: auto;"
				cellpadding="5">
				<tr>
						<td>附件:</td>
						<td><input type=file id="file" name="planfiles"   /></td>
				</tr>
			</table>
			<div id="dialog-adder-vendor">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="submitFile()">提交</a>
		  </div>
		</form>
		</div>
	</div>
	
	<div id="win-fileCollaboation-addoredit" class="easyui-dialog" title="文件协同内容" style="width:40%;height:40%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true">   
        
        <table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>文件协同名称:</td><td style="text-align:left">   <label  id="fileTitle"></label>  </td>
				</tr>
				<tr>
					<td>协同类型:</td><td style="text-align:left"><label  id="fileType"    ></label></td>
				</tr>
				<tr>
					<td>开始日期:</td><td style="text-align:left"><label  id="startTime"  ></label></td>
				</tr>
				<tr>
					<td>结束日期:</td><td style="text-align:left"><label  id="endTime"  ></label></td>
				</tr>
				<tr>
				    <td>描述：</td>
					<td style="text-align:left" >
						<label id="fileContent"  ></label>
					</td>
				</tr>
				</table>
</div> 	


	 <div id="win-abnormal-addoredit" class="easyui-dialog" title="详情异常反馈" style="width:40%;height:80%"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-abnormal-addoredit" method="post"  enctype="multipart/form-data">
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>异常反馈名称:</td><td style="text-align: left"><input class="easyui-textbox" id="abnormalFeedbackName" name="abnormalFeedbackName"  type="text" 
						data-options="required:true"  width="150px" />
				    </td>
				</tr>
				<tr>
					<td>是否置顶:</td>
					<td style="text-align:left">
					<select class="easyui-combobox" id="topYn" name="topYn"  style="width:150px;"
						 data-options="required:true,editable:false">
						 <option value="1">置顶</option>
						 <option value="0">不置顶</option>
					 </select>
				    </td>
				</tr>
				<tr>
					<td>开始日期:</td><td style="text-align:left"><input id="effectiveStartDate" name="effectiveStartDate" class="easyui-textbox" data-options="required:true,editable:false" style="width:150px"/></td>
				</tr>
				<tr>
					<td>结束日期:</td><td style="text-align:left"><input id="effectiveEndDate" name="effectiveEndDate" class="easyui-textbox" data-options="required:true,editable:false" style="width:150px"/></td>
				</tr>
				<tr>
				<td>
				</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center">
						<textarea id="commentArea" name="commentArea" style="width: 100%;height:100%;"></textarea>
					</td>
				</tr>
				</table>
			</form>
		</div>
	</div> 




	
<script type="text/javascript">
setInterval("myInterval()",300000);//1000为1秒钟
function myInterval()
{
	$.post(ctx+"/manager/vendor/notice/getNoticeStars",{"ctx":ctx},function(data){
		$("#gg").html(data);
	},"text");	
}
	function toManager(title,url){
		if($('#manager-tab').tabs('exists',title)){
			$('#manager-tab').tabs('select',title);
			return false;
		}
		url = '${ctx}/'+url;
		$('#manager-tab').tabs('add',{
			title:title,
			content:'<iframe src="'+url+'" frameborder="0" style="width:100%;height:99%;"></iframe>',
			closable:true
		});
		
	}
	
	function backlogFmt(v,r,i){
		if(v=='0'){
			return v;
		}
		return '<a href="javascript:;" onclick="toManager(\''+r.viewName+'\',\''+r.viewUrl+'\')">'+v+'</a>';
	}
	
	//文件操作
	//下载
	function fileDownloadFmt(v,r,i){
		return '<a href="javascript:;" onclick="fileDownload(\''+r.filePath+'\',\''+r.fileName+'\')">下载</a>';
	}
	
	function fileDownload (filePath,fileName){
		if(filePath==null || filePath==''){
			$.messager.alert('提示','文件路径不能为空','warning');
			return false;
		}
		var url = ctx+'/common/download';
		var fileName = fileName==null?"":fileName;
		var inputs = '<input type="hidden" name="filePath" value="'+filePath+'"><input type="hidden" name="fileName" value="'+fileName+'">';
		jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
        .appendTo('body').submit().remove();
	}
	
	//上传
	function fileuploadFmt(v,r,i){
		return '<a href="javascript:;" onclick="addFile('+r.id+')">上传</a>';
	}
	//查看
	function fileFmt(v,r,i){
		return '<a href="javascript:;" onclick="viewFileCollaboation('+r.id+')">详情</a>';
	}
	//异常管理
	function abnormalFeedbackNameFmt(v,r,i){
		var  title = "" 
		if(r.topYn == 1){
			title = '<a href="javascript:;"    style="text-decoration:none;"        onclick="abnormalNameDetail(\''+ r.id+'\');"><label style = "color : red " >【置顶】 </label> ' + r.abnormalFeedbackName +'    </a>';
		}else{
			title = '<a href="javascript:;"   style="text-decoration:none;"    onclick="abnormalNameDetail(\''+ r.id+'\');"><label>【普通】 </label>' + r.abnormalFeedbackName +'     </a>';
		}
		return title;
	}
	
	function abnormalNameDetail(id){
		
		//获取
		$.ajax({
			url:ctx + '/manager/abnormal/abnormalFeedback/getAbnormal/' + id,
			type:'POST',
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				if(data){
					//赋值
					$("#id").val(data.id);
					$("#abnormalFeedbackName").textbox("setValue", data.abnormalFeedbackName);//标题
  				    $("#topYn").combobox("setValue", data.topYn);//是否置顶	
					$("#effectiveStartDate").textbox("setValue",data.effectiveStartDate);
					$("#effectiveEndDate").textbox("setValue", data.effectiveEndDate);
					$("#vendorIds").val(data.vendorIds);
					$("#companyName").textbox("setValue", data.vendorNames);
					KindEditor.instances[0].html(data.commentArea);	
				}
			}
		});

		$('#abnormalFeedbackName').textbox('disable');
		$('#topYn').combobox('disable');
		 $('#effectiveStartDate').textbox('disable');
		 $('#effectiveEndDate').textbox('disable');
		
		
		$('#win-abnormal-addoredit').dialog({    
		    title: '异常详细内容',      
		    closed: false,    
		    cache: false,    
		    modal: true   
		});
		
		
	}
	
	function addFile(id){
		$('#win-upload-addoredit').dialog({
			iconCls:'icon-edit',
			title:'上传文件'
		});
		$('#form-upload-addoredit').form('clear');
		$('#fileCollaborationId').val(id);
		$('#win-upload-addoredit').dialog('open');
	
	}
	
	
	function viewFileCollaboation(id){
 		$.ajax({
			url:ctx +'/manager/file/fileCollaboation/get/' + id,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
                 //页面填写
                 $("#fileTitle").html(data.title);
                 $("#fileType").html(data.collaborationType.name);
                 $("#startTime").html(data.validStartTime);
                 $("#endTime").html(data.validEndTime);
                 $("#fileContent").html(data.describe);
			}
		});

		$('#win-fileCollaboation-addoredit').dialog({    
		    title: '文件协同详细内容',      
		    closed: false,    
		    cache: false,    
		    modal: true   
		});

	}
	
	
	
	  function downFile(id){
			
			var url = ctx+'/manager/file/fileCollaboation/downloadFile';
			var inputs = '<input type="hidden" name="billId" value="'+id+'">';
			
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
		    .appendTo('body').submit().remove(); 
		} 
	
	
		function submitFile(){
				var url = ctx+'/manager/file/fileCollaboation/fileFeedbackUpload';
				$.messager.progress({
					title:'提示',
					msg : '提交中...'
				});
				$('#form-upload-addoredit').form('submit',{
					ajax:true,
					url:url,
					onSubmit:function(){
						var isValid = $(this).form('validate');
						if(!isValid){
							$.messager.progress('close');
						}
						return isValid;
					},
					success:function(data){
						$.messager.progress('close');
						try{
						var result = eval('('+data+')');
						if(result.success){
							$.messager.alert('提示',result.msg,'info');
						}else{
							$.messager.alert('提示',result.msg,'error');
						}
						}catch (e) {
							$.messager.alert('提示',data,'error');
						}
						$('#win-upload-addoredit').dialog('close');
					}
				});
			}

	function lookNotice(id)
	{
		var os;
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        var s;
        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
 
        //以下进行测试
        if (Sys.ie) os='IE: ' + Sys.ie;
        if (Sys.firefox) os='Firefox: ' + Sys.firefox;
        if (Sys.chrome) os='Chrome: ' + Sys.chrome;
        if (Sys.opera) os='Opera: ' + Sys.opera;
        if (Sys.safari) os='Safari: ' + Sys.safari;
        
		$('#dd').dialog({    
		    title: '公告详细内容',      
		    closed: false,    
		    cache: false,    
		    href: ctx+'/manager/vendor/notice/getNotice/'+id,   
		    modal: true   
		}); 
	}
	
	$(function(){	
		var os;
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        var s;
        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
 
        //以下进行测试
        if (Sys.ie) os='IE: ' + Sys.ie;
        if (Sys.firefox) os='Firefox: ' + Sys.firefox;
        if (Sys.chrome) os='Chrome: ' + Sys.chrome;
        if (Sys.opera) os='Opera: ' + Sys.opera;
        if (Sys.safari) os='Safari: ' + Sys.safari;

		$('#dd').window('close');
		$('#desk-pp').portal({
			border:false,
			fit:false
		});

		$.post(ctx+"/manager/vendor/notice/getNoticeStars",{"ctx":ctx,"os":os},function(data){
			$("#gg").html(data);
		},"text");
		$.post(ctx+"/manager/basedata/msg/getMessage",function(data){
			$("#msglist").html(data);
			$.parser.parse($('#msglist'));
		},"text");
		//消息-预警-晋级预警
		$.post(ctx+"/manager/common/warning/getWarningInfo",function(data){
			$("#warnListInfo").html(data);
			$.parser.parse($('#warnListInfo'));
		},"text");
		
		$.post(ctx+"/manager/common/warning/getWarningWarn",function(data){
			$("#warnListWarn").html(data);
			$.parser.parse($('#warnListWarn'));
		},"text");
		
		$('#desk-pp').portal('resize');
	});
	
	//设置消息已读
	function operateWarn(id) {
		$.ajax({
			url:'${ctx}/manager/common/warning/setIsRead/' + id,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.show({
							title:'消息',
							msg:  data.message, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$("#warn_" + id).hide();   
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
			}
		});
	}


	
	
	
	function closeTab(){
		$(".tabs-closable").each(function(index, obj) {
            //获取所有可关闭的选项卡  
            var tab = this.textContent;  
            $(".easyui-tabs").tabs('close', tab);  
      	});
		/* var currTab =  $('#manager-tab').tabs('getSelected'); 
		if("desk" == currTab.panel('options').id)
			return false;
		
		var title = currTab.panel('options').title;
		$('#manager-tab').tabs('close',title); */
	}	

	function reloadTab() {
		var currTab =  $('#manager-tab').tabs('getSelected'); //获得当前tab
		if("desk" == currTab.panel('options').id)
			return false;
		
	    var url = $(currTab.panel('options').content).attr('src');
	    $('#manager-tab').tabs('update', {
	      tab : currTab,
	      options : {
	       content : '<iframe src="'+url+'" frameborder="0" style="width:100%;height:99%;"></iframe>'
	      }
		});
	}
	function myWidow(title,url){
		$('#w').window({
			title:title
		});
		 $('#w').html('<iframe src="'+url+'" frameBorder=0 scrolling=no  style="width:100%;height:99%;" ></iframe>');
		 $('#w').window('open');
	}
	function closeWindow(){
		$('#w').window('close');
	}
	
	 function outTimeFmt(v,r,i){
		if(r.isOutTime == 0){
			return '预警';
		}else{
			return '提醒';
		}
	}
	function warnMessageFmt(v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showMessage('+r.id+','+r.billId+','+r.billType+');">'+r.billType+'</a>';
	}
	
	 function showMessage(id,billId,billType){
		
		$.messager.alert(billType,'业务单号为'+billId+'闲置，请马上处理','info');
		$.ajax({
			url:ctx+'/manager/common/warning/setIsRead/'+id,
			success:function(){
				$('#datagrid-warn').datagrid('reload');
			}
		})
	}  
	 
	 
	//刷新工作台
	function reloadDesk(refushType){
		if(refushType==1){
			$('#datagrid-backlogcfg').datagrid('reload');
		}else if(refushType==2){
			$('#datagrid-filelogcfg').datagrid('reload');
		}else if(refushType==3){
			$('#datagrid-abnormalBack').datagrid('reload');
		}else if(refushType==4){
			$.post(ctx+"/manager/common/warning/getWarningInfo",function(data){
				$("#warnListInfo").html(data);
				$.parser.parse($('#warnListInfo'));
			},"text");

		}else if(refushType==5){
			$.post(ctx+"/manager/common/warning/getWarningWarn",function(data){
				$("#warnListWarn").html(data);
				$.parser.parse($('#warnListWarn'));
			},"text");

		}
		else if(refushType==6){
			
			var os;
	        var Sys = {};
	        var ua = navigator.userAgent.toLowerCase();
	        var s;
	        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
	        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
	        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
	        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	 
	        //以下进行测试
	        if (Sys.ie) os='IE: ' + Sys.ie;
	        if (Sys.firefox) os='Firefox: ' + Sys.firefox;
	        if (Sys.chrome) os='Chrome: ' + Sys.chrome;
	        if (Sys.opera) os='Opera: ' + Sys.opera;
	        if (Sys.safari) os='Safari: ' + Sys.safari;

			$('#dd').window('close');
			$('#desk-pp').portal({
				border:false,
				fit:false
			});
			
			
			$.post(ctx+"/manager/vendor/notice/getNoticeStars",{"ctx":ctx,"os":os},function(data){
				$("#gg").html(data);
			},"text");
		}
		
	}
	
	
	
</script>
</body>
</html>