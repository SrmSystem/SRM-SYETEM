	<%@ page contentType="text/html;charset=UTF-8" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
	<!-- 创建发货单 -->  	
	<div id="win-delivery-addoredit" class="easyui-window" title='<spring:message code="purchase.delivery.CreateInvoice"/>' style="width:80%;height:100%" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-delivery-addoredit" method="post" enctype="multipart/form-data">
				<div id="deliveryListToolbar" style="padding:5px;">
					<div>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="saveDelivery(0)"><spring:message code="purchase.delivery.SubmitAudit"/></a>
					    
					</div>
					<div>
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td><spring:message code="purchase.delivery.vendorCode"/>：</td>
							<td>
								<input type="hidden" id="dlvId" name="id"/>
								<input type="hidden" id="shipType" name="shipType"/>
								<!-- <input class="easyui-textbox" name="vendor.code" type="text" data-options="required:true,editable:false"/> -->
								<input name="vendor.code" style="border: 0px" type="text" readonly="readonly"/>
							</td>
							</td>
							<td><spring:message code="purchase.delivery.VendorName"/>:</td>
							<td>
								<!-- <input class="easyui-textbox" name="vendor.name" type="text" data-options="required:true,editable:false"/> -->
								<input name="vendor.name" style="border: 0px;width:200px;" type="text" readonly="readonly"/>
							</td>
							<td><spring:message code="purchase.delivery.TypeOfShipping"/>:</td>
							<td>
								<select class="easyui-combobox" style="width:148px;" id="transport_type" name="transportType" type="text" data-options="required:true,editable:false"></select>
							</td>
						</tr>
						<tr>
							<td><spring:message code="purchase.delivery.ReceivingCompanyCode"/>：</td>
							<td>
								<!-- <input class="easyui-textbox" name="buyer.code" type="text" data-options="required:true,editable:false"/> -->
								<input id = "companyCode"  name="companyCode" style="border: 0px" type="text" readonly="readonly"/>
							</td>
							<td><spring:message code="purchase.delivery.ReceivingCompanyName"/>：</td>
							<td>
								<!-- <input class="easyui-textbox" name="buyer.name" type="text" data-options="required:true,editable:false"/> -->
									<input id = "companyName" name="companyName" style="border: 0px;width:200px;" type="text" readonly="readonly"/>
							</td>
							<td><spring:message code="purchase.delivery.ReceivingContact"/>：</td><td><input id="deliveryContacter" name="deliveryContacter" class="easyui-textbox"/></td>
						</tr>
						<tr>
							<td><spring:message code="purchase.delivery.ReceivingAddress"/>：</td><td><input id="deliveryAddress" name="deliveryAddress" class="easyui-textbox"/></td>
							<td><spring:message code="purchase.delivery.LogisticsCompany"/>：</td><td><input id="logisticsCompany" name="logisticsCompany" class="easyui-textbox"/></td>
							<td><spring:message code="purchase.delivery.LogisticsContact"/>：</td><td><input id="logisticsContacter" name="logisticsContacter" class="easyui-textbox"/></td>
						</tr>
						<tr>
							<td><spring:message code="purchase.delivery.ReceivingTelephone"/>：</td><td><input id="deliveryTel" name="deliveryTel"  data-options="validType:'isPhone'" class="easyui-textbox"/></td>
							<td><spring:message code="purchase.delivery.LogisticsTelephone"/>：</td><td><input id="logisticsTel" name="logisticsTel" data-options="validType:'isPhone'" class="easyui-textbox"/>
							<td><spring:message code="purchase.delivery.TheTotalNumberOfLargePackages"/>:</td><td><input class="easyui-numberbox" id="anzpk" name="anzpk" type="text" data-options="required:true,min:0,precision:0"/></td>
						</tr>
						<tr> 
						     <td><spring:message code="purchase.delivery.EstimatedDeliveryTime"/>:</td><td><input id="planDeliveryDate" name="planDeliveryDate" class="easyui-datebox" data-options="required:true,editable:false, 
						     onSelect: function(date){
						    	 //预计到货时间=预计发货时间+物流天数
						         var ysts=$('#ysts').numberbox('getValue'); 
						             if(null!=ysts && ''!=ysts){
						                var planDeliveryDate=$('#planDeliveryDate').datebox('getValue');
						                var expectedArrivalTime=new Date(new Date(planDeliveryDate).getTime() + ysts*24*60*60*1000);  //后推几天
						                var year = expectedArrivalTime.getFullYear();
         								var month = expectedArrivalTime.getMonth() + 1;
         								var date = expectedArrivalTime.getDate();
						                var strDate = year + '-' + month + '-' + date;
						                $('#expectedArrivalTimeId').datebox('setValue',strDate);//由于是disable需要隐藏一个值
						                document.getElementById('expectedArrivalTime').value=strDate;
						              } 
						      }" style="width:140px"/></td>
						     <td><spring:message code="purchase.delivery.LogisticsDays"/>:</td><td><input class="easyui-numberbox" id="ysts" name="ysts" data-options="required:true,min:0,precision:0,
						     onChange: function(date){
						             //预计到货时间=预计发货时间+物流天数
							         var planDeliveryDate=$('#planDeliveryDate').datebox('getValue');
							         var ysts=$('#ysts').numberbox('getValue'); 
						             if(null!=ysts && ''!=ysts && null!=planDeliveryDate && ''!=planDeliveryDate){
						                var expectedArrivalTime=new Date(new Date(planDeliveryDate).getTime() + ysts*24*60*60*1000);  //预计到货时间后推几天
						                var year = expectedArrivalTime.getFullYear();
         								var month = expectedArrivalTime.getMonth() + 1;
         								var date = expectedArrivalTime.getDate();
						                var strDate = year + '-' + month + '-' + date;
						                $('#expectedArrivalTimeId').datebox('setValue',strDate);
						                document.getElementById('expectedArrivalTime').value=strDate;
						              } 
						      }"/></td>
						     <td><spring:message code="purchase.delivery.EstimatedTimeOfArrival"/>:</td><td>
						     <input id="expectedArrivalTimeId" class="easyui-datebox" data-options="required:true,editable:false,disabled:true" style="width:140px"/>
						     <input id="expectedArrivalTime" name="expectedArrivalTime" type="hidden"/>
						     </td>
						</tr>
						<tr>
							<td><spring:message code="purchase.delivery.Enclosure"/>：</td><td><input class="easyui-filebox" name="files" data-options="prompt:'选择...'" style="width:100%"></td>
							<td><spring:message code="purchase.delivery.ASNNumbers"/>:</td>
							<td>
							<!-- <input class="easyui-textbox" id="deliveryCode" name="deliveryCode" type="text" data-options="editable:false" style="border:0px"/> -->
								<input id="deliveryCode" name="deliveryCode" style="border: 0px" type="text" readonly="readonly"/>
							</td>
							<td></td><td></td>					
						</tr>
						<tr>
						   <td><spring:message code="purchase.delivery.Remarks"/>：</td>
						   <td colspan="5">
						      <textarea style="width: 100%;height:40px" id="deliveryRemark" name="remark" ></textarea>
						   </td>
						</tr>
					</table>
					</div>
				</div>
			</form>
				<table id="datagrid-delivery-item-addoredit-list" title="发货单详情" class="easyui-datagrid" style="width:100%"
					data-options="iconCls: 'icon-edit',method:'post',onClickRow: onClickRow2,onLoadSuccess:initData">
					<thead data-options="frozen:true">
			         <tr>
			         	<th data-options="field:'id',checkbox:true"></th>      
					    <th data-options="field:'opt',formatter:operateFmt"></th>  
					    <th data-options="field:'orderCode'"><spring:message code="purchase.delivery.PurchaseOrderNumbers"/></th>
						<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.delivery.MaterialCoding"/></th>
						<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.delivery.MaterialName"/></th>
						<th data-options="field:'orderQty'"><spring:message code="purchase.delivery.QuantityOfGoodsToBePurchased"/></th>   
						<th data-options="field:'shouldQty'"><spring:message code="purchase.delivery.PayableQuantity"/></th>    
			         </tr>
					</thead>
					<thead>
					 <tr>
                    <th data-options="field:'itemNo'"><spring:message code="purchase.delivery.LineNumber"/></th>
					<th data-options="field:'sendQty',width:80,align:'right',editor:{type:'numberbox',options:{required:true,min:0,precision:'3'}}"><spring:message code="purchase.delivery.QuantityShipped"/></th>
					<th data-options="field:'standardBoxNum',editor:{type:'numberbox',options:{editable:true,required:true,min:0,precision:'3'}}"><spring:message code="purchase.delivery.QuantityOfLargePackage"/></th>
					<th data-options="field:'boxNum',formatter:boxNumFmt,editor:{type:'numberbox',options:{editable:false}}"><spring:message code="purchase.delivery.TheTotalNumberOfLargePackages"/></th>
					<th data-options="field:'minPackageQty',editor:{type:'numberbox',options:{editable:true,required:true,min:0,precision:'3'}}"><spring:message code="purchase.delivery.QuantityOfSmallPackages"/></th>  
					<th data-options="field:'minBoxNum',formatter:minBoxNumFmt,editor:{type:'numberbox',options:{editable:true,required:true}}"><spring:message code="purchase.delivery.TheTotalNumberOfSmallPackages"/></th>
					<th data-options="field:'requestTime'"><spring:message code="purchase.delivery.RequiredArrivalTime"/></th>   
					<th data-options="field:'manufactureDate',editor:{type:'datebox',options:{required:true,editable:false,validType:'isTrueTime'}}, width: 95"><spring:message code="purchase.delivery.DateOfManufacture"/></th> 
					<th data-options="field:'version',width:160,editor:{type:'combobox',		
							options:{
							   url:'${ctx}/manager/basedata/dict/getDictItemSelect/'+'VERSION',    
                               cache : false,
                               method:'post',                 
                               valueField:'name',
                               textField:'name'
							}
						}"><spring:message code="purchase.delivery.Edition"/></th>

					<th data-options="field:'charg',width:160,editor:{type:'textbox',options:{required:true,
					 onChange :function(data){
                        if(null!=data){
                        var edit = $('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', { index:editIndex, field: 'charg' });
                        if(edit!= null){
                        	edit.target.next('span').find('input').val(data.toUpperCase());
                        }
                       }
                      }
					}}"><spring:message code="purchase.delivery.BatchNumber"/></th> 
					<th data-options="field:'vendorCharg',width:160,editor:{type:'textbox',options:{required:true,
					 onChange :function(data){
                        if(null!=data){
                         var edit = $('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', { index:editIndex, field: 'vendorCharg' });
                         if(edit!= null){
                        	edit.target.next('span').find('input').val(data.toUpperCase());
                        }
                       }
                      }
					}}"><spring:message code="purchase.delivery.TraceabilityBatchNumber"/></th> 
					
					<th data-options="field:'unitName'"><spring:message code="purchase.delivery.Company"/></th>
					<th data-options="field:'remark',width:160,editor:'textbox'"><spring:message code="purchase.delivery.Remarks"/></th>    
					<th data-options="field:'inspectionReport',width:250,formatter:inspectionReportFmt"><spring:message code="purchase.delivery.InspectionReport"/></th>   
					 
					</tr></thead>
				</table> 
		</div>
	</div>



	<!-- 上传文件 -->
	<div id="win-file-import" class="easyui-window" title="<spring:message code="purchase.delivery.UploadInspectionReport"/>" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-file-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/purchaseorder/filesUpload"> 
				<div style="margin-bottom:20px">
					<spring:message code="purchase.delivery.File"/>：<input type=file id="file" name="inspectfiles"   class="easyui-validatebox input150" data-options="required:true"/><br>
						<input type=hidden id="fids" name="fids"/>   
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitFile();"><spring:message code="purchase.delivery.Submit"/></a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-file-import').form('reset')"><spring:message code="purchase.delivery.Reset"/></a>
				</div>
			</form>  
		</div>
	</div>

<script>

$(function () {
    $("#deliveryContacter").textbox('textbox').bind({
         focus: function () {
        	 refreshDic('deliveryContacter');
         }
    })
    $("#deliveryAddress").textbox('textbox').bind({
         focus: function () {
        	 refreshDic('deliveryAddress');
         }
    })
    $("#logisticsCompany").textbox('textbox').bind({
         focus: function () {
        	 refreshDic('logisticsCompany');
         }
    })
    $("#logisticsContacter").textbox('textbox').bind({
         focus: function () {
        	 refreshDic('logisticsContacter');
         }
    })
    $("#deliveryTel").textbox('textbox').bind({
         focus: function () {
        	 refreshDic('deliveryTel');
         }
    })
    $("#logisticsTel").textbox('textbox').bind({
         focus: function () {
        	 refreshDic('logisticsTel');
         }
    })
});

function refreshDic(id){
	var strCookie=document.cookie;
    var arrCookie=strCookie.split(";")[0];
    if(arrCookie.indexOf('deliveryContacter')!=-1){
    	var arr = arrCookie.split(":");
    	 for(var i=0;i<arr.length;i++){
             var str=arr[i].split("=");
             if(id==str[0]){
            	 $("#"+id).textbox('setValue',str[1]);
             }
       }
    }
}

function operateFmt(v,r,i){
	var s;
	var dlvId=document.getElementById("dlvId").value;
	//新增
	if(0==dlvId){
		s=' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="removeRow('+i+');"><spring:message code="vendor.deleting"/><!-- 删除 --></a>';
	}
		return  s;
}

function boxNumFmt(v,r,i){
	if(null!=r.standardBoxNum && null!=r.sendQty){
		return Math.ceil(r.sendQty/r.standardBoxNum);//向上取整，有小数则加1
	}
	return "";
}

function minBoxNumFmt(v,r,i){
	if(null!=r.minPackageQty && null!=r.sendQty){
		return Math.ceil(r.sendQty/r.minPackageQty);//向上取整，有小数则加1
	}
	return "";
}

function removeRow(index){
	$("#datagrid-delivery-item-addoredit-list").datagrid('deleteRow',index);
}

$.extend($.fn.validatebox.defaults.rules, {
    isPhone: {
        validator: function (value, param) {//param为默认值
        	//验证手机号
        	 var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
        	 var temp1 = reg.test(value);
        	 //验证固定号码
        	 var re = /^0\d{2,3}-?\d{7,8}$/;
        	 var temp2 = re.test(value)

        	 if(temp1 || temp2){
        		 return true;
        	 }else{
        		 return false;
        	 }
        },
        message: '<spring:message code="purchase.delivery.PleaseInputTheCorrectTelephoneNumber"/>'/* 请输入正确的联系电话 */
    }
});

//上传检验报告-显示窗口 
function toUploadInspectionReport(orderItemPlanId){
	$('#form-file-import').form('clear');   
	$('#win-file-import').window('open');  
	$('#fids').val(orderItemPlanId);
}

//保存上传的检验报告
function submitFile(){
	$('#form-file-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:ctx+'/manager/order/delivery/inspectUploadFile?fids='+$('#fids').val(),
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
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.UploadSuccessfully"/>'/* 上传成功 */,'info');
				$('#win-file-import').window('close');
				//将下载附件处重新显示
				appendInspectionReportFmt($('#fids').val(),result.fileName,result.filePath);
			}else{
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.msg,'error');
			}
			}catch (e) {  
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data,'error');
			}
		}
	});
}

//上传检验报告后，以供下载
function appendInspectionReportFmt(id,fileName,filePath){
	var fileSpan=document.getElementById(id);
	fileSpan.innerHTML='<a style="margin-right:10px" href="javascript:;" onclick="File.download(\''+filePath+'\',\'\')">'+fileName+'</a>';
    document.getElementById("fileName_"+id).value=fileName;
	document.getElementById("filePath_"+id).value=filePath;
}

var editIndex = undefined;
function initData(){
	endEditing();
}
function endEditing() {
	if (editIndex == undefined) {
		return true
	}
	if ($('#datagrid-delivery-item-addoredit-list').datagrid('validateRow', editIndex)) {
		$('#datagrid-delivery-item-addoredit-list').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}


/**
 * 将整张表格进行可编辑
 */
 function onClickRow2(index, data){
	 var rows = $('#datagrid-delivery-item-addoredit-list').datagrid('getRows');
	 var i;
	 for(i = 0;i < rows.length;i++) {
		 editRow(i,rows[i]);
	 }
}

function editRow(index, data) {
	    //modify by chao.gu 20171020 整行都认为是开始编辑
		/* if (editIndex != index) {
			if (endEditing()) {
				$('#datagrid-delivery-item-addoredit-list').datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-delivery-item-addoredit-list').datagrid('selectRow', editIndex);
			}
		}else{
			$('#datagrid-delivery-item-addoredit-list').datagrid('beginEdit', index);
			editIndex = index;
		} */
		//modify end
		
		$('#datagrid-delivery-item-addoredit-list').datagrid('beginEdit', index);
		editIndex = index;
		
		var sendQty = $('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {
			index : index,
			field : 'sendQty'
		});
		var standardBoxNum = $('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {
			index : index,
			field : 'standardBoxNum'
		});
		var boxNum = $('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {
			index : index,
			field : 'boxNum'
		});
	
	
		
		var minPackageQty = $('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {
			index : index,
			field : 'minPackageQty'
		});
		var minBoxNum = $('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {
			index : index,
			field : 'minBoxNum'
		});
		
		$(boxNum.target).numberbox('setValue', Math.ceil(sendQty.target.val()/standardBoxNum.target.val()));
		$(minBoxNum.target).numberbox('setValue', Math.ceil(sendQty.target.val()/minPackageQty.target.val()));
		
		$(sendQty.target).numberbox({
			onChange : function() {
				caculate();
			}
		});
		
		$(standardBoxNum.target).numberbox({
			onChange : function() {
				caculate();
			}
		});
		
		$(minPackageQty.target).numberbox({
			onChange : function() {
				caculate();
			}
		});
		
		function caculate(){
			$(boxNum.target).numberbox('setValue', Math.ceil(sendQty.target.val()/standardBoxNum.target.val()));
			$(minBoxNum.target).numberbox('setValue', Math.ceil(sendQty.target.val()/minPackageQty.target.val()));
		}
}

$.extend($.fn.validatebox.defaults.rules, {
    isTrueTime: {
        validator: function (value, param) {//验证生产日期的时间是否正确
        	var planDeliveryDateVal = $('#planDeliveryDate').datebox('getValue');
        	var date=(new Date).format('yyyy-MM-dd');
        	if(planDeliveryDateVal !=null && planDeliveryDateVal !=''){
        		if((value <= planDeliveryDateVal) && (value <= date)){
        			return true;
        		}else{
        			return false;
        		}
        	}else{
        		if(value <= date){
        			return true;
        		}else{
        			return false;
        		}
        	}
        	
        },
        message: '<spring:message code="purchase.delivery.TheProductionDateShallNotExceedTheCurrentTimeAndShallNotExceedTheExpectedDeliveryDate"/>'/* 生产日期不能超过当前时间，并且不能超过预计发货日期！ */
    }
});

</script>
