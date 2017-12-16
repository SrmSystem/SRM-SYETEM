<%@page import="com.qeweb.scm.basemodule.utils.UserContext"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="zh-CN" class="panel-fit">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>后台管理</title>

    <!-- Bootstrap -->
    <link href="${ctx}/static/base/bootstrap-3.3.4-dist/css/bootstrap.min.css" rel="stylesheet">

     <!--[if lt IE 9]>
      <script src="${ctx}/static/script/html5shiv.min.js"></script>
      <script src="${ctx}/static/script/respond.min.js"></script>
    <![endif]-->
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${ctx}/static/base/easyui/themes/<%=UserContext.getUserTheme() %>/easyui.css">
<%-- <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${ctx}/static/base/easyui/themes/ui-cupertino/easyui.css"> --%>
<link href="${ctx}/static/base/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/style/base.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/base/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/base/easyui/themes/color.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/style/icons/IconExtension.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/style/iconsbase/icons.css">
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
</script>
<script type="text/javascript" src="${ctx}/static/base/easyui/jquery.min.js"></script>
<script src="${ctx}/static/base/bootstrap-3.3.4-dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery-i18n/jquery.i18n.properties-min-1.0.9.js"></script>
<script type="text/javascript" src="${ctx}/static/base/util/CommonUtil.js"></script>
<script type="text/javascript">
/** 动态加载easyui-lang **/
var lans = {'zh-CN' : 'zh_CN','zh_CN' : 'zh_CN', 'en-us' : 'en', 'en':'en'};
var lan = navigator.language || navigator.userLanguage;
var arrStr = document.cookie.split("; ");
for (var i = 0; i < arrStr.length; i++) {
    var temp = arrStr[i].split("=");
    if (temp[0] == 'Language') {
        lan = unescape(temp[1]);
    }
}
/** 初始化jQuery.i18n **/
jQuery.i18n.properties({
    name:'message',
    path:'${ctx}/static/script/',
    mode:'map',
    language: lans[lan],
    callback: function() {// 加载成功后设置显示内容
    }
});
$.getScript('${ctx}/static/base/easyui/locale/easyui-lang-' + lans[lan] + '.js');
$.getScript('${ctx}/static/base/jquery-validation/1.11.1/messages_bs_' + lans[lan] + '.js');
</script>
<script type="text/javascript" src="${ctx}/static/base/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript">
document.write('<script type="text/javascript" src="${ctx}/static/base/easyui/locale/easyui-lang-'+lans[lan]+'.js"/>');
</script>
<%-- <script type="text/javascript" src="${ctx}/static/base/easyui/locale/easyui-lang-zh_CN.js"></script> --%>
<script type="text/javascript" src="${ctx}/static/base/jquery-cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery-json/jquery.json.min.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery-ex/jquery-ex.js"></script>
<script src="${ctx}/static/base/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<%-- <script src="${ctx}/static/base/jquery-validation/1.11.1/messages_bs_zh_CN.js" type="text/javascript"></script> --%>
<script type="text/javascript" src="${ctx}/static/script/statusrender.js"></script>
<script type="text/javascript" src="${ctx}/static/script/datagrid-ex.js"></script>
<script type="text/javascript" src="${ctx}/static/script/system/file.js"></script>
<%-- <script type="text/javascript" src="${ctx}/static/script/system/theme.js"></script> --%>


<script type="text/javascript">
/** 扩展itemId属性取值，主要是为了区分全局ID */
$.fn.getCmp = function(itemId){
	return this.find('[itemId="'+itemId+'"]:eq(0)');
}
/** 扩展easyui-window,主要为了高度自适应，但是里面必须定义1个itemId=ct的最大容器 */
$.extend($.fn.window.methods, {
	autoSizeMax : function(obj,params){
		var w = $(document).width()-50;
		var h = $(document).height()-80;
		if(params!=null && params.body){
			w = $(document.body).innerWidth()-50;
			h = $(document.body).innerHeight()-40;
		}
		$(obj).window('resize',{
			width : w,
			height : h
		});
		$(obj).window('center');
	},
	autoSize : function(obj,params){
		var $ct = $(obj).getCmp('ct');
		if($ct.length<0){
			$.messager.alert('提示','窗口自适应方法必须提供ItemId=ct','warning');
			return false;
		}
		var realW = $ct.width()+20;
		var realH = $ct.height()+90;//加空白加工具栏
		if(params){
		if(params.h){
			realH+=params.h;
		}
		if(params.w){
			realW+=params.w;
		}
		}
		$(obj).window('resize',{
			width : realW,
			height : realH
		});
		$(obj).window('center');
	}
});

var loginUserId = <%=((ShiroUser) SecurityUtils.getSubject().getPrincipal()).id%>;

var CellEditor = {
		editIndex : undefined,
		endEditing : function($datagird){
			if (CellEditor.editIndex == undefined){return true}
			if ($datagird.datagrid('validateRow', CellEditor.editIndex)){
				$datagird.datagrid('endEdit', CellEditor.editIndex);
				CellEditor.editIndex = undefined;
				return true;
			} else {
				return false;
			}
		},
		onClickCell:function(index, field){
			if (CellEditor.endEditing($(this))){
				$(this).datagrid('selectRow', index)
						.datagrid('editCell', {index:index,field:field});
				CellEditor.editIndex = index;
//				var editor = $(this).datagrid('getEditor',{index:index,field:field});
//				var target = $(this).datagrid('getEditor',{index:index,field:field}).target;
//				if(target)$(target).get(0).focus();
			}
		},
		accept : function(datagridId){
			var $datagird = $('#' + datagridId);
			if (CellEditor.endEditing($datagird)){
				$datagird.datagrid('acceptChanges');
			}
		}  
};


var RowEditor = {
   editIndex:undefined,
   endEditing : function(datagridId){
          if (RowEditor.editIndex == undefined){return true;}
          if ($(datagridId).datagrid('validateRow', RowEditor.editIndex)){
              var eds = $(datagridId).datagrid('getEditors',RowEditor.editIndex);
              $.each(eds,function(i,ed){
            	  if(ed.type=='combobox'){
	            	  var text = $(ed.target).combobox('getText');
	            	  //表格列格式化 ： formatter:function(value,row){ return row.field + 'name';}
	                  $(datagridId).datagrid('getRows')[RowEditor.editIndex][ed.field + 'name'] = text;
	                  $(datagridId).datagrid('endEdit', RowEditor.editIndex);
            	  }
              });
              RowEditor.editIndex = undefined;
              return true;
          } else {
              return false;
          }
   },
   onClickCell : function(index, field){
	   var datagridId = '#'+$(this).attr('id');
	  // alert(datagridId);
	   //alert(RowEditor.editIndex);
	  // alert(index);
          if (RowEditor.editIndex != index){
        	//  alert(1);
              if (RowEditor.endEditing(datagridId)){
            	  RowEditor.editIndex = index;
            	//  alert(2);
                  $(datagridId).datagrid('selectRow', index).datagrid('beginEdit', index);
                  var ed = $(datagridId).datagrid('getEditor', {index:index,field:field});
                  if(ed!=null){
                	//  alert(3);
                  ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                  }
                  
              } else {
            	 // alert(4);
                  $(datagridId).datagrid('selectRow', editIndex);
              }
          }
         // alert(RowEditor.editIndex);
   },
   accept : function(datagridId){
	   datagridId = '#' + datagridId;
         if (RowEditor.endEditing(datagridId)){
             $(datagridId).datagrid('acceptChanges');
         }
	},
   reject : function(datagridId){
	   datagridId = '#' + datagridId;
       $(datagridId).datagrid('rejectChanges');
       RowEditor.editIndex = undefined;
	},
   append : function(datagridId){
	   datagridId = '#' + datagridId;
	   if (RowEditor.endEditing(datagridId)){
           $(datagridId).datagrid('appendRow',{});
           RowEditor.editIndex = $(datagridId).datagrid('getRows').length-1;
           $(datagridId).datagrid('selectRow', RowEditor.editIndex).datagrid('beginEdit', RowEditor.editIndex);
       }
	},
   removeit : function (datagridId){
		datagridId = '#' + datagridId;
	   	var row = $(datagridId).datagrid('getSelected');
	   	if (row) {
	    	var rowIndex = $(datagridId).datagrid('getRowIndex', row);
	    	$(datagridId).datagrid('deleteRow', rowIndex);    
		}
	   	RowEditor.editIndex = undefined;
   }
};

var pageSize = 10;
var pageList=[10,20,30];

/** 树形grid编辑器 */
var TreeGridEditor = {
		/** 行编辑 */
		rowEdit : function(self,tgId,id){
			$(tgId).treegrid('beginEdit',id);
		},
		/** 行编辑后保存 */
		rowEditSave : function(self,tgId,id){
			$(tgId).treegrid('endEdit',id);
		},
		/** 行编辑后保存 */
		rowEditCancel : function(self,tgId,id){
			$(tgId).treegrid('cancelEdit',id);
		}
		
}

/* 默认的成功显示提示 */
$.messager.success = function(msg){
	if(msg=='' || msg==null){
		msg = '处理成功!';
	}
	 $.messager.show({
         title:'处理成功',
         msg:msg,
         showType:'slide',
         style:{
             right:'',
             top:document.body.scrollTop+document.documentElement.scrollTop,
             bottom:''
         }
     });
}

/* 默认的失败显示提示 */
$.messager.fail = function(msg){
	if(msg=='' || msg==null){
		msg = '处理失败!';
	}
	 $.messager.show({
         title:'处理失败',
         msg:'<span style="color:red">'+msg+'<span>',
         showType:'slide',
         style:{
             right:'',
             top:document.body.scrollTop+document.documentElement.scrollTop,
             bottom:''
         }
     });
}


var Dg = {
	getRowById : function(dg,id){
		var row = null;
		var rows = dg.datagrid('getRows');
		$.each(rows,function(i,n){
			if(n.id==id){
				row = n;
				return false;
			}
		});
		return row;
	}	
		
};

</script>
<script type="text/javascript" src="${ctx}/static/script/json2.js"></script>
<%-- <script type="text/javascript" src="${ctx }/static/script/basedata/tab.js"></script>
<script type="text/javascript">
$(function(){
    /*为选项卡绑定右键*/
   $(".tabs-wrap ul li").live('contextmenu',function(e){
       alert("sss");
       /* 选中当前触发事件的选项卡 */
       var subtitle =$(this).text();
       $('#tabs-wrap').tabs('select',subtitle);
       
       //显示快捷菜单
       $('#menu').menu('show', {
           left: e.pageX,
           top: e.pageY
       });
       
       return false;
   });
});
</script> --%>
<sitemesh:head/>
</head>
<body class="panel-fit">

	<sitemesh:body/>

</body>
</html>