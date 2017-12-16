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
                valueAxis.title = "供应商数量统计图 (by 省份、供应商性质)";
                valueAxis.axisAlpha = 0;
                chart.addValueAxis(valueAxis);
                
                var graph = new AmCharts.AmGraph();
                graph.valueField = "注册供应商";
                graph.balloonText = "注册供应商: <b>[[value]]</b>";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 0.8;
                chart.addGraph(graph);
                
                var graph1 = new AmCharts.AmGraph();
                graph1.valueField = "体系外备选供应商";
                graph1.balloonText = "体系外备选供应商: <b>[[value]]</b>";
                graph1.type = "column";
                graph1.lineAlpha = 0;
                graph1.fillAlphas = 0.8;
                chart.addGraph(graph1);
                var graph2 = new AmCharts.AmGraph();
                graph2.valueField = "体系内供应商";
                graph2.balloonText = "体系内供应商: <b>[[value]]</b>";
                graph2.type = "column";
                graph2.lineAlpha = 0;
                graph2.fillAlphas = 0.8;
                chart.addGraph(graph2);
                
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
    url:'${ctx}/manager/vendor/statistics/statiVendorCountList/2',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt',fit:true
  ">
    <thead>
      <tr>
        <th data-options="field:'provinceText'">省份</th>
        <th data-options="field:'col1'">注册供应商</th>
        <th data-options="field:'col2'">体系外备选供应商</th>
        <th data-options="field:'col3'">体系内供应商</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
  	<div>
		<a href="javascript:;" class="easyui-linkbutton" onclick="toStatisticsCharts()">查看图表</a>
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
</script>

</body>
</html>