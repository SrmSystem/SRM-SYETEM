//根据条件查询
  function searchList(){
  	var searchParamArray = $('#form-epWholeQuo-search').serializeArray();
  	var searchParams = $.jqexer.formToJson(searchParamArray);
  	$('#datagrid-epWholeQuo-report').datagrid('load',searchParams);
  }
  
// 截取时间为YYYY-MM-DD HH 格式
/*  function formatDate(date){
      var month = date.getMonth()+1;
      if( "" != date ){
          if( date.getMonth() +1 < 10 ){
              month = '0' + (date.getMonth() +1);
          }
          var day = date.getDate();
          if( date.getDate() < 10 ){
              day = '0' + date.getDate();
          }
         //返回格式化后的时间
          return date.getFullYear()+'-'+month+'-'+day+" "+date.getHours();
      }else{
          return "";
      }
  }*/

//询比价报表--操作列--查看报价单
function fmtOpt(v,r,i){
	var isVendor = '0';
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openItemWin('+r.epPrice.id+','+r.epMaterial.id+','+r.epVendor.id+','+isVendor+')">查看报价单</a> '
}

//询比价报表--操作列--查看询价单
/*function fmtEpPriceCode(v,r,i){
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="addPriceEpModule('+ r.epPrice.id +');">'+r.epPrice.enquirePriceCode+'</a> '
}*/


//询比价报表--差异率
function fmtDifferVal(v,r,i){
	if(r.quotePrice == 0){
		return '100%';
	}else{
		return ((1-r.negotiatedPrice/r.quotePrice)*100)+'%';
	}
}

//询比价报表--打开查看报价单
function openItemWin(epPriceId,epMaterialId,epVendorId,isVendor){
	var href = ctx + '/manager/ep/epWholeQuo/buyerOpenEpWholeQuoWin?epPriceId='+ epPriceId+'&epMaterialId='+epMaterialId+'&epVendorId='+epVendorId+'&isVendor='+isVendor;
	var title = "整项报价详情";
	parent.myWidow(title,href);	
 }