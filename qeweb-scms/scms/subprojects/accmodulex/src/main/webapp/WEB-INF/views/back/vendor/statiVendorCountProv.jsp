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
                chart.dataProvider = chartData;
                chart.categoryField = "province";
                chart.startDuration = 1;

                var categoryAxis = chart.categoryAxis;
                categoryAxis.labelRotation = 90;
                categoryAxis.gridPosition = "start";

                //y轴标题
                var valueAxis = new AmCharts.ValueAxis();
                valueAxis.dashLength = 5;
                valueAxis.title = "供应商数量统计图 (by 省份)";
                valueAxis.axisAlpha = 0;
                chart.addValueAxis(valueAxis);
                
                var graph = new AmCharts.AmGraph();
                graph.valueField = "totalCount";
                graph.balloonText = "[[category]]: <b>[[value]]</b>";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 0.8;
                chart.addGraph(graph);
                var chartCursor = new AmCharts.ChartCursor();
                chartCursor.cursorAlpha = 0;
                chartCursor.zoomable = false;
                chartCursor.categoryBalloonEnabled = false;
                chart.addChartCursor(chartCursor);
                chart.creditsPosition = "top-right";
                
                
             	// 图例
             	/*
                var legend = new AmCharts.AmLegend();
                legend.borderAlpha = 0.2;
                legend.horizontalGap = 10;
                legend.autoMargins = false;
                legend.marginLeft = 20;
                legend.marginRight = 20;
                chart.addLegend(legend);
				*/
                
                chart.write("chartdiv");
            });
        </script>
</head>

<body>
<div title="统计"  style="height:50%;">
  <table id="datagrid-vendorCount" class="easyui-datagrid" data-options="
    url:'${ctx}/manager/vendor/statistics/statiVendorCountList/1',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt',fit:true
  ">
    <thead>
      <tr>
        <th data-options="field:'provinceText'">省份</th>
        <th data-options="field:'statisticsCount'">数量</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
  	<div>
		<a href="javascript:;" class="easyui-linkbutton" onclick="toStatisticsCharts()">查看图表</a>
	</div>
	<div>
      <form id="form-search">
                             省份<input class="easyui-textbox" name="search-LIKE_provinceText" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPro()">查询</a>          
      </form>
    </div>
  </div>
</div>
<div id="chartdiv" style="height:50%;bottom: 0;"></div>

<script type="text/javascript">
function toStatisticsCharts(){
	var arr=$('#datagrid-vendorCount').datagrid('getData');
	chartData = arr['chartData'];
	chart.dataProvider = chartData;
	chart.validateNow();  
	chart.validateData();  
	
}
function searchPro(){
	var searchParamArray = $('#form-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-vendorCount').datagrid('load', searchParams);
}
</script>

</body>
</html>