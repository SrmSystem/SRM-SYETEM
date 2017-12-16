<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="objName" type="java.lang.Object" required="true"%>
<%@ attribute name="type" type="java.lang.String" required="true"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>

<c:if test="${objName!=null}">
<c:forEach items="${objName.colSceneItem}" var="item" varStatus="itemStatus">
<%--
private String colCode;			//字段编码
	private String name;			//显示名称
	private String range;			//值范围  1=A;2=B 枚举类型时使用
	private String type;			//标签类型input,select,checkbox,radio
	private String statusKey;		//当类型为枚举时设定
	private String way;				//使用场景 query,edit
	private boolean filter;			//是否作为筛选条件
	private boolean required;		//是否必须
	private boolean isShow;			//是否显示
 --%>
<!-- 查询 -->
  <c:if test="${item.show && item.way eq 'query' }">
  	<c:if test="${item.filter && type eq 'queryForm'}">
		  <c:if test="${item.type eq 'input'}">
		  	${item.name }：<input type="text" name="search-LIKE_${item.colCode }" class="easyui-textbox" style="width:80px;"/>
		  </c:if>
		  <c:if test="${item.type eq 'select'}">
		  	<c:set var="keyVal" value="${fn:split(item.range,';') }"/> 
		  	${item.name }：<select class="easyui-combobox" name="search-EQ_${item.colCode }">
		  		<option value="">--请选择--</option>
	 		  	<c:forEach items="${keyVal}" var="kv" varStatus="itemst">  
	 		  		<option value="${fn:split(kv,'=')[0]}">${fn:split(kv,'=')[1]}</option>
	 		  	</c:forEach>  
 		  	</select>
		  </c:if>
  	</c:if>
  	<c:if test="${type eq 'queryTable'}">
  		<c:if test="${item.type eq 'input'}">
	  		<th data-options="field:'${item.colCode }'">${item.name }</th>
  		</c:if>
  		<c:if test="${item.type eq 'select'}">
  		<c:choose>
  			<c:when test="${item.statusKey != null }">
 				<th data-options="field:'${item.colCode }',formatter:function(v,r,i){return StatusRender.render(v,'${item.statusKey }',false);}">${item.name }</th>   
  			</c:when>
  			<c:otherwise>
 				<c:set var="keyVal" value="${fn:split(item.range,';') }"/> 
 				<th data-options="field:'${item.colCode }',formatter:function(v,r,i){<c:forEach items="${keyVal}" var="kv" varStatus="itemst">if(v == ${fn:split(kv,'=')[0]}) return '${fn:split(kv,'=')[1]}';</c:forEach>}">${item.name }</th>   
  			</c:otherwise>
  		</c:choose>
  		</c:if>
  	</c:if>
  </c:if>
  <!-- 编辑 -->
  <c:if test="${item.show && item.way eq 'edit' }">
  	<c:if test="${type eq 'editForm'}">
  		<c:if test="${item.type eq 'input'}">
		  	${item.name }：<input type="text" name="${item.colCode }" class="easyui-textbox" style="width:80px;"/>
		</c:if>
		<c:if test="${item.type eq 'select'}">
		  	<c:set var="keyVal" value="${fn:split(item.range,';') }"/> 
		  	${item.name }：<select class="easyui-combobox" name="search-EQ_${item.colCode }">
		  		<option value="">--请选择--</option>
	 		  	<c:forEach items="${keyVal}" var="kv" varStatus="itemst">  
	 		  		<option value="${fn:split(kv,'=')[0]}">${fn:split(kv,'=')[1]}</option>
	 		  	</c:forEach>  
 		  	</select>
		</c:if>
		<!-- TODO:: checkbox,radio -->
  	</c:if>
  	<c:if test="${type eq 'editTable'}">
  		<c:if test="${item.type eq 'input'}">
		  	<th data-options="field:'${item.colCode }',width:80,align:'right',editor:'textbox',required:${item.required }">${item.name }</th> 
		</c:if>
  		<c:if test="${item.type eq 'select'}">
  		<!-- TODO:: -->
  		</c:if>
  	</c:if>
  </c:if>
</c:forEach>
</c:if>