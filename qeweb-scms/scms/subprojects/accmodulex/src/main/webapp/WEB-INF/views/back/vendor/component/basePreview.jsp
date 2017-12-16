<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<body>
  <div title="基本信息" class="container-fluid text-right">   
   <fieldset><legend class="text-left"><span class="label label-primary"><small>企业所有权信息</small></span></legend>
    <div class="row row-form">
          <div class="col-md-4">供应商名称:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">供应商简称:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">邓白氏编码:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">成立时间:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">企业法人:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">上市公司:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">企业性质:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">股比构成:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">网址:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">税务登记号:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">企业所有权:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">银行名称:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">注册地址:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">主供事业部:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">供应商类别:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    </fieldset>
    <fieldset>
    <legend class="text-left"><span class="label label-primary"><small>企业规模</small></span></legend>
    <div class="row row-form">
          <div class="col-md-4">注册资本(万元):<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">资本总额(万元):<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">流动资金总额(万元):<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">占地面积(万平方米):<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    </fieldset>
    <fieldset>
    <legend class="text-left"><span class="label label-primary"><small>生产地址信息</small></span></legend>
    <div class="row row-form">
          <div class="col-md-4">所在省:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">所在市:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">距离怀柔公里:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">距离密云公里:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">距离潍坊公里:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">距离诸城公里:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">距离长沙公里:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">距离南海公里:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">距离昌平公里:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    </fieldset>
    <fieldset>
    <legend class="text-left"><span class="label label-primary"><small>联系人信息</small></span></legend>
    <div class="row row-form">
          <div class="col-md-4">总经理:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">距离南海公里:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">销售负责人:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">质量负责人:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    <div class="row row-form">
          <div class="col-md-4">研发负责人:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    </fieldset>
    <fieldset>
    <legend class="text-left"><span class="label label-primary"><small>其他信息（新供应商可不用填写）</small></span></legend>
    <div class="row row-form">
          <div class="col-md-4">进入神农客公司年份:<input class="easyui-textbox" style="width:160px;"/></div>
          <div class="col-md-4">离开神农客公司年份:<input class="easyui-textbox" style="width:160px;"/></div>
    </div>
    </fieldset>
    </div>



</body>
</html>
