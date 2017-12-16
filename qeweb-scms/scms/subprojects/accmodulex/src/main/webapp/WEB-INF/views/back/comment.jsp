<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评论</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
</script>
</head>
<body>
<link rel="stylesheet" href="${ctx}/static/cuslibs/notie/css/base.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${ctx}/static/cuslibs/notie/css/template.css" type="text/css" media="screen" />
<input id="noticeId" value="${noticeEntity.id }" type="hidden" />
<h2 class="c_titile">${noticeEntity.title }</h2>
    <p class="box_c"><span class="d_time">有效开始时间：
    <fmt:formatDate value="${noticeEntity.validStartTime }" pattern="yyyy-MM-dd"/>
    </span><span>&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </span><span>有效结束时间：
    <fmt:formatDate value="${noticeEntity.validEndTime }" pattern="yyyy-MM-dd"/>
    </span>
</p>
<div style="width: 80%;margin-top: 40px;margin-left: 10%;margin-right: 10%">
   ${noticeEntity.content }
</div>
<div id="content" style="width: 95%; height: auto;margin-top:80px">
	<div class="wrap">
		<c:if test="${noticeEntity.commentPower==0}">
			<div class="comment">
				<div class="head-face">
					<img src="${ctx}/static/cuslibs/notie/css/images/1.jpg" />
					<p>评论填写》》》</p>
				</div>
				<c:if test="${noticeEntity.commentPower==0}">
					<div class="content">
						<div class="cont-box">
							<textarea id="commentCotent" class="text" placeholder="请输入..."></textarea>
						</div>
						<div class="tools-box">
							<div class="submit-btn"><input type="button" onClick="out()"value="提交评论" /></div>
						</div>
					</div>
				</c:if>
				<c:if test="${noticeEntity.commentPower==1}">
			      <div style="width: 96%;min-height: 200px;background-image: url('images/2.png');text-align: center;font-size: 26px;font-weight: 900;"></div>
			     </c:if>
			</div>
		</c:if>
		<div id="info-show">
			<ul>
			<c:if test="${noticeEntity.commentPower==0}">
			<c:forEach items="${comments }" var="comments">
				<li>
					<div class="head-face">
						<img src="${ctx}/static/cuslibs/notie/css/images/1.jpg"></div>
						<div class="reply-cont">
							<p class="username">${comments.createUserName }</p>
							<p class="comment-body">${comments.content }</p>
							<p class="comment-footer"><fmt:formatDate value="${comments.createTime}" pattern="yyyy-MM-dd"/></p>
						</div>
					</li>
			</c:forEach>
			</c:if>
			</ul>
		</div>
	</div>
</div>

<script type="text/javascript" src="${ctx}/static/cuslibs/notie/css/main.js"></script>
<script type="text/javascript" src="${ctx}/static/cuslibs/notie/css/sinaFaceAndEffec.js"></script>
<script type="text/javascript">
	// 绑定表情
	$('.face-icon').SinaEmotion($('.text'));
	// 测试本地解析
	function out() {
		if($("#commentCotent").val()==null||$("#commentCotent").val()=='')
		{
			$.messager.alert('错误提示','请填写评论','error');
			return false;
		}
		$.post(ctx+"/manager/vendor/notice/addcomment",{"content":$("#commentCotent").val(),"noticeId":$("#noticeId").val()},function(data){
			$('#info-show ul').prepend(reply(AnalyticEmotion(data)));
			$("#commentCotent").val("");
		},"text");	
	}
	
	var html;
	function reply(content){
		html  = '<li>';
		html += '<div class="head-face">';
		html += '<img src="${ctx}/static/cuslibs/notie/css/images/1.jpg" / >';
		html += '</div>';
		html += '<div class="reply-cont">';
		html += content;
		html += '</div>';
		html += '</li>';
		return html;
	}
	
	//点击a标签触发时间
 	$(".ke-insertfile").click(function(e){
		//解决上层的冒泡事件
		e.preventDefault();
		var $this = $(this);        
        var fileName =  $this.html() //附件名称
		var fileUrl =   $this.attr("href")//附件地址
		
		//整理url 
		
		var url = fileUrl.split("/");
        var filePath = url[url.length-2];
        var filexls = url[url.length-1];
		
        //整理文件名
        var name = fileName.split(".");
        fileName = name[name.length-2];
        
		if(fileName==''||fileUrl==''){
			return ;
		}
		var url = ctx+'/manager/vendor/notice/downloadFile/'+filePath+"/"+filexls;
		var inputs = '<input type="hidden" name="fileUrl" value="'+fileUrl+'">'+'<input type="hidden" name="fileName" value="'+fileName+'">';
		
		jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
	    .appendTo('body').submit().remove();
        
    }); 
	
	
	
	
</script>


<%-- <link rel="stylesheet" href="${ctx}/static/cuslibs/notie/css/base.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${ctx}/static/cuslibs/notie/css/template.css" type="text/css" media="screen" />
<style type="text/css">
#container{float: left;width: 100%;height: 800px ;overflow:auto;}
#container a{color:#767676;font-size:13px;font-weight:200}
#container a:hover{color:#565656}
/* container */
.timeline_container{z-index:50;text-align:center;margin:0px auto;width:29px;display:block;height:100%;cursor:pointer}
.timeline{position:absolute;margin:0px auto;width:4px;display:block;background:#eeeeee;float:left;height:100%;font-size:0px;top:0px;left:518px}
.leftcorner{z-index:99;position:absolute;width:36px;display:block;height:26px;overflow:hidden}
.rightcorner{z-index:99;position:absolute;width:36px;display:block;height:26px;overflow:hidden}
.item{border-bottom:#eaeaea 1px solid;position:relative;border-left:#eaeaea 1px solid;padding-bottom:15px;line-height:30px;margin:20px 30px 0px;min-height:60px;padding-left:15px;width: 80%;padding-right:15px;display:block;font-family:"microsoft jhenghei", "microsoft yahei";word-wrap:break-word;background:#ffffff;float:left;font-size:13px;border-top:#eaeaea 1px solid;border-right:#eaeaea 1px solid;box-shadow:0px 1px 8px rgba(202, 202, 202, 0.3)}
.item h3{line-height:30px;width: 80%;display:block;height:30px;font-weight:normal}
.item h3 .fl{float:left;color:#363636;font-size:14px}
.item h3 .fr{text-align:right;float:right;color:#999999;font-size:12px}
.item p.con{border-bottom:#ececec 1px dashed;padding-bottom:15px;line-height:30px;width: 80%;display:block;color:#555555;border-top:#ececec 1px dashed;font-weight:normal;padding-top:15px}
.item p{text-indent:2em}
.item h4{padding-bottom:15px;padding-left:0px;width: 80%;padding-right:0px;display:block;padding-top:15px}
#commentCotent{float:left;width:80%;height: 100%;}
</style>
<input id="noticeId" value="${noticeEntity.id }" type="hidden" />
<article class="blogs">
  <div  id="article" class="index_about">
    <h2 class="c_titile">${noticeEntity.title }</h2>
    <p class="box_c"><span class="d_time">有效开始时间：
    <fmt:formatDate value="${noticeEntity.validStartTime }" pattern="yyyy-MM-dd"/>
    </span><span>&nbsp;&nbsp;&nbsp;</span><span>有效结束时间：
    <fmt:formatDate value="${noticeEntity.validEndTime }" pattern="yyyy-MM-dd"/>
    </span></p>
    <ul class="infos">
      <p>${noticeEntity.content }</p>
    </ul>

    <div class="otherlink">
      <h2>发表评论</h2>
      <br/>
      <br/>
      <c:if test="${noticeEntity.commentPower==0}">
      <textarea id="commentCotent" style="width: 96%;min-height: 200px"></textarea>
      <br/>
      <input type="button" value="提交评论" style="width: 96%;height: 50px" onclick="addcomment()" >
      </c:if>
      <c:if test="${noticeEntity.commentPower==1}">
      <div style="width: 96%;min-height: 200px;background-image: url('images/2.png');text-align: center;font-size: 26px;font-weight: 900;">
      	<br/>
      	<br/>
      	<br/>
      	此公告不能评论
      </div>
      </c:if>
    </div>
  </div>
  <aside id="aside" class="right">
    <div class="news">
      <h3>
        <p>公告<span>评论</span></p>
      </h3>
		<c:if test="${noticeEntity.commentPower==0}">
		<div id="container">
		<c:forEach items="${comments }" var="comments">
			<div class="item">
				<h4><span class="fl">${comments.createUserName }</span><span class="fr">&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatDate value="${comments.createTime}" pattern="yyyy-MM-dd"/></span></h4>
				<p class="con">${comments.content }</p>
			</div>
		</c:forEach>
		</div>
		</c:if>
	</div>
  </aside>
</article>
	<script type="text/javascript">
		function addcomment()
		{
			if($("#commentCotent").val()==null||$("#commentCotent").val()=='')
			{
				$.messager.alert('错误提示','请填写评论','error');
				return false;
			}
			$.post(ctx+"/manager/vendor/notice/addcomment",{"content":$("#commentCotent").val(),"noticeId":$("#noticeId").val()},function(data){
				$("#container").prepend(data);
				$("#commentCotent").val("");
			},"text");	
		}
		$(function(){
			var height=$("#article").css("height");
			$("#container").css("height",height);
		});
</script> --%>
</body>
</html>