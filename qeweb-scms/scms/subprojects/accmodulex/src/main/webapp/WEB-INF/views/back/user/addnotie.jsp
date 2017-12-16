<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/notie.js"></script>
		<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/css/smoothness/jquery.ui.css" type="text/css" />
	    <link rel="stylesheet" type="text/css" href="${ctx }/static/cuslibs/notie/css/css.css"/>
	    <link rel="stylesheet" href="${ctx }/static/cuslibs/notie/themes/default/default.css" />
		<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/plugins/code/prettify.css" />
		<script charset="utf-8" src="${ctx }/static/cuslibs/notie/kindeditor-min.js"></script>
		<script charset="utf-8" src="${ctx }/static/cuslibs/notie/lang/zh_CN.js"></script>
		<script charset="utf-8" src="${ctx }/static/cuslibs/notie/plugins/code/prettify.js"></script>
		<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="content1"]', {
				cssPath : ctx+'/static/cuslibs/notie/plugins/code/prettify.css',
				uploadJson : ctx+'/static/cuslibs/notie/upload_json.jsp',
				fileManagerJson : ctx+'/static/cuslibs/notie/file_manager_json.jsp',
				allowFileManager : true,
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
			prettyPrint();
		});
	</script>
</head>
<body style="width: 100%;height: 100%;">
<style>
.tag {
    -moz-border-radius: 2px;
    -webkit-border-radius: 2px;
    border-radius: 2px;
    display: block;
    float: left;
    padding: 5px;
    text-decoration: none;
    background: #3AAA55;
    color: #FFF;
    margin-right: 5px;
    margin-bottom: 5px;
    font-family: tahoma, sans-serif;
    font-size: 13px;
    max-width: 90%;
}
.tag button {
    font-weight: bold;
    color: #FFF;
    text-decoration: none;
    font-size: 11px;
    background: none;
    border: none;
    cursor: pointer;
}
</style>
	 <div class="stay-list-left" style="overflow:auto;">
	 	<input type="hidden" id="addids" name="addids" value="">
	 	<input type="hidden" id="ntype" name="ntype" value="">
        <form>
            <div class="sea-div">
                <label class="search-lab"><spring:message code="vendor.user.announcementName"/><!-- 公告名称 --></label><input type="text" class="hotel-name" id="disHotel"/>
            </div>
            <div class="sea-div">
                <label class="search-lab"><spring:message code="vendor.user.whetherComments"/><!-- 是否评论 --></label>
                <select id="commentPower"  style="width: 30%;height: 33px;line-height: 33px;font-family: arial;border: 1px solid #dcdcdc;border-radius: 5px;margin-left: 28px;text-indent: 10px;display: block;float: left;">
                	<option value="0"><spring:message code="vendor.allow"/><!-- 允许 --></option>
                	<option value="1"><spring:message code="vendor.noAllow"/><!-- 不允许 --></option>
                </select>
                <label class="search-lab" style="width: 10px"></label>
                <label class="search-lab"><spring:message code="vendor.user.IsPlacedTop"/><!-- 是否置顶 --></label>
                <select id="noticeType"  style="width: 30%;height: 33px;line-height: 33px;font-family: arial;border: 1px solid #dcdcdc;border-radius: 5px;margin-left: 28px;text-indent: 10px;display: block;float: left;">
                	<option value="2"><spring:message code="vendor.notPlacedTop"/><!-- 不置顶 --></option>
                	<option value="1"><spring:message code="vendor.topper"/><!-- 置顶 --></option>
                </select>
            </div>
            <div class="sea-div">
                <label class="search-lab"><spring:message code="vendor.startDate"/><!-- 开始日期 --></label><input type="text" readonly  id="startDate" style="width: 30%"/>
                <label class="search-lab" style="width: 10px"></label>
                <label class="search-lab"><spring:message code="vendor.endDate"/><!-- 结束日期 --></label><input type="text" readonly  id="endDate" style="width: 30%"/>
					 <a href="javascript:;" class="easyui-linkbutton" onclick="ktuo()"><spring:message code="vendor.user.setPermanent"/><!-- 设置永久 --></a>
            </div>
            <div class="sea-div">
           	    <label class="search-lab"><spring:message code="vendor.user.access"/><!-- 浏览权限 -->：</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:;" class="easyui-linkbutton" onclick="window.parent.lookBuyerUser()"><spring:message code="vendor.user.selectBuyerPersonnel"/><!-- 选择采购商--></a>
            	<a href="javascript:;" class="easyui-linkbutton" onclick="window.parent.lookUser()"><spring:message code="vendor.user.selectPersonnel"/><!-- 选择供应商 --></a>
                <a href="javascript:;" class="easyui-linkbutton" onclick="window.parent.lookR()"><spring:message code="vendor.user.selectRole"/><!-- 选择角色 --></a>
           		（<spring:message code="vendor.user.noSystemAll"/><!-- 不选择！系统默认全部 -->）
            </div>
            <div id="lookpower" style="width: 100%;height:50px;margin-top: 5px;">
            </div>
            <div class="sea-div">
            <br/>
					<textarea id="content1" name="content1" style="width: 100%"></textarea>
					 <a href="javascript:;" class="easyui-linkbutton" style="width: 100px;height:50px;
               			display: block;float: left;margin: 10px 0px 0px 45%;cursor: pointer;" onclick="addnotie()"><spring:message code="vendor.user.savePublish"/><!-- 保存并发布 --></a>
            </div>
        </form>
    </div>

<script type="text/javascript" src="${ctx }/static/cuslibs/notie/js/jquery.js"></script>
<script type="text/javascript" src="${ctx }/static/cuslibs/notie/js/jquery.ui.js"></script>
<script type="text/javascript" src="${ctx }/static/cuslibs/notie/js/moment.min.js"></script>
<script type="text/javascript" src="${ctx }/static/cuslibs/notie/js/hotel/hotel.search.js"></script>
<script type="text/javascript" src="${ctx }/static/cuslibs/notie/js/stay.js"></script>
<script type="text/javascript">
function addnotie()
{
	var ntype =$("#ntype").val();
	var noticeTypeNames =$("#lookpower").html();
	var content =$("#content1").val();
	var title =$("#disHotel").val();
	var commentPower =$("#commentPower").val();
	var noticeType =$("#noticeType").val();
	var addids =$("#addids").val();
	var validStartTime =$("#startDate").val();
	var validEndTime =$("#endDate").val();
	window.parent.addposts(ntype,noticeTypeNames,content,title,commentPower,noticeType,addids,validStartTime,validEndTime);
}
function chongzhi()
{
	$("#addids").val("");
	$("#lookpower").val("");
}
</script>
</body>

</html>
