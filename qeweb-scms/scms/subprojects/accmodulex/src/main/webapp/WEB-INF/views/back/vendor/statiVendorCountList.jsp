<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>统计</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script src="${ctx}/static/base/amcharts-3-14-5Free/amcharts.js" type="text/javascript"></script>
    <script src="${ctx}/static/base/amcharts-3-14-5Free/serial.js" type="text/javascript"></script>
    
<script type="text/javascript">
            var chart;

            var chartData ;


            AmCharts.ready(function () {
                chart = new AmCharts.AmSerialChart();
            });
        </script>
    
</head>

<body style="margin:0;padding:0;">
<div style="height:50%">
  <table id="datagrid-vendorCount-list" title="报表统计" class="easyui-datagrid" data-options="
    url:'${ctx}/manager/vendor/statistics/statiVendorCountList/0',method:'post',singleSelect:false,
    	toolbar:'#tt',fit:true,
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
    <thead>
      <tr>
        <th data-options="field:'provinceText'">省份</th>
        <th data-options="field:'col1'">供应商性质</th>
        <th data-options="field:'col2'">工厂供货距离</th>
        <th data-options="field:'col3'">系统</th>
        <th data-options="field:'col4'">供应商分类</th>
        <th data-options="field:'col5'">业务类型</th>
        <th data-options="field:'col6'">零部件类别</th>
        <th data-options="field:'statisticsCount'">供应商总数</th>
      </tr>
    </thead>
  </table>
  <div id="tt" style="padding:5px;">
  	<div>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-piechart',plain:true" onclick="toManager('省份','manager/vendor/statistics/redirect/0')">查看省份统计</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-piechart',plain:true" onclick="toManager('省份、性质','manager/vendor/statistics/redirect/1')">查看省份、性质统计</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-piechart',plain:true" onclick="toManager('省份、零部件','manager/vendor/statistics/redirect/2')">查看省份、零部件统计</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-piechart',plain:true" onclick="toManager('品牌','manager/vendor/statistics/redirect/3')">查看品牌统计</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-piechart',plain:true" onclick="toManager('品牌、产品线','manager/vendor/statistics/redirect/4')">查看品牌、产品线统计</a>
		 -->
	</div>
	<div>
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-chart_bar',plain:true" onclick="toStatisticsCharts()">查看图表</a>
			<a href="javascript:;" class="easyui-linkbutton" id="countExpBtn" data-options="iconCls:'icon-download',plain:true" onclick="exp()">导出</a>
		</div>
      <form id="form-search">
       	        品牌<input class="easyui-textbox" name="search-LIKE_msrBrandName" type="text" style="width:80px;"/>
       	        产品线<input class="easyui-textbox" name="search-LIKE_msrProductLineName" type="text" style="width:80px;"/>
       	        业务类型<input class="easyui-textbox" name="search-LIKE_msrBussinessName" type="text" style="width:80px;"/>
       	        系统<input class="easyui-textbox" name="search-LIKE_mattName" type="text" style="width:80px;"/>
       	        零部件类别<input class="easyui-textbox" name="search-LIKE_matPartsType" type="text" style="width:80px;"/>
       	        供应商性质<input class="easyui-textbox" name="search-LIKE_vPhaseId" type="text" style="width:80px;"/>
                             省份<input class="easyui-textbox" name="search-LIKE_vProvinceText" type="text" style="width:80px;"/>
                             工厂<input class="easyui-textbox" name="search-LIKE_msrFactoryName" type="text" style="width:80px;"/>
       		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPro()">查询</a>          
           <div style="padding:1px">
            <input type="checkbox" style="width: 20px;" name="checkbox-type1" checked="checked" value="PROV"><span style="padding-right: 10px;">省份</span>
            <input type="checkbox" style="width: 20px;" name="checkbox-type2" value="PHASE"><span style="padding-right: 10px;">供应商性质</span>
            <input type="checkbox" style="width: 20px;" name="checkbox-type3" value="PARTS_TYPE"><span style="padding-right: 10px;">零部件类别</span>
            <!-- <input type="checkbox" style="width: 20px;" name="checkbox-type4" value="DISTANCE"><span style="padding-right: 10px;">工厂供货距离</span> -->
            <input type="checkbox" style="width: 20px;" name="checkbox-type5" value="BUSSINESS_TYPE"><span style="padding-right: 10px;">业务类型</span>
            <input type="checkbox" style="width: 20px;" name="checkbox-type6" value="CLASSIFY2"><span style="padding-right: 10px;">供应商分类</span>
            <input type="checkbox" style="width: 20px;" name="checkbox-type7" value="SYSTEM"><span style="padding-right: 10px;">系统</span>
        	</div>
      </form>
    </div>
  </div>
  </div>
<div id="chartdiv" style="height:50%;bottom: 0;"></div>

<script type="text/javascript">
function toStatisticsCharts(){
	chart = new AmCharts.AmSerialChart();
    chart.dataProvider = chartData;
    chart.categoryField = "x-axis";
    chart.startDuration = 1;

    var categoryAxis = chart.categoryAxis;
    categoryAxis.labelRotation = 90;
    categoryAxis.gridPosition = "start";

    //y轴标题
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.dashLength = 5;
    valueAxis.title = "供应商数量统计图";
    valueAxis.axisAlpha = 0;
    chart.addValueAxis(valueAxis);
    
    
    var chartCursor = new AmCharts.ChartCursor();
    chartCursor.cursorAlpha = 0;
    chartCursor.zoomable = false;
    chartCursor.categoryBalloonEnabled = false;
    chart.addChartCursor(chartCursor);
    chart.creditsPosition = "top-right";
    
    chart.write("chartdiv");
    
    
	var arr=$('#datagrid-vendorCount-list').datagrid('getData');
	
	var searchParamArray = $('#form-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	var flag = 0;
	for(var o in searchParams){
		if(o.indexOf("checkbox-type")!=-1){
			flag++;
		}
	}
	if(flag>1){
		chartData = arr['chartData'];
        var flag = "";
        for(var i=0;i < chartData.length; i++){
        	for(var str in chartData[i]){
        		if(flag.indexOf(str+",")!=-1){
        			continue;
        		}
        		if(str=="x-axis"||str=="y-axis"){
        			continue;
        		}
        		var graph = new AmCharts.AmGraph();
                graph.valueField = str;
                graph.balloonText = str+": <b>[[value]]</b>";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 0.8;
                chart.addGraph(graph);
                flag+=str+",";
        	}
        }
	}else{
		var graph = new AmCharts.AmGraph();
		graph.valueField = 'y-axis';
		graph.balloonText = '[[category]]: <b>[[value]]</b>';
		graph.type = 'column';
		graph.lineAlpha = 0;
		graph.fillAlphas = 0.8;
		chart.addGraph(graph);
        
	}
	
	chartData = arr['chartData'];
	chart.dataProvider = chartData;
	chart.validateNow();  
	chart.validateData();  
	
}

function searchPro(){
	var searchParamArray = $('#form-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	var flag = 0;
	for(var o in searchParams){
		if(o.indexOf("checkbox-type")!=-1){
			flag++;
		}
	}
	if(flag>2){
		$.messager.alert('提示','最多只可选择两项维度!','warning');
		return;
	}
	if(flag==0){
		$.messager.alert('提示','请选择相应维度!','warning');
		return;
	}
	$('#datagrid-vendorCount-list').datagrid('load', searchParams);
	$("#countExpBtn").linkbutton('enable');
}

function exp(){
	$("#countExpBtn").linkbutton('disable');
	//导出
	$('#form-search').form('submit',{
		url:'${ctx}/manager/vendor/statistics/exportExcel', 
		success:function(data){
			$.messager.progress('close');
		}
	});
	
}
</script>

<script type="text/javascript">
function toManager(title,url){
	 window.parent.toManager(title,url);
}



</script>

</body>
</html>