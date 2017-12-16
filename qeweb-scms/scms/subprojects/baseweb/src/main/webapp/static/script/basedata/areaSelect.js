$(function() {  	
	  // 下拉框选择控件，下拉框的内容是动态查询数据库信息
	  $('#combobox_province').combobox({ 
	          url: ctx+'/public/common/areaselect/getAreaSelect?id=0',
	          editable:false,
	          cache: false,
	          valueField:'value',   
	          textField:'text',
	          
	    onHidePanel: function(){
	        $("#combobox_city").combobox("setValue",'');//市级
	        $("#combobox_area").combobox("setValue",'');//区级
	        var id = $('#combobox_province').combobox('getValue');		
	      	//alert(id);
	      	
	      $.ajax({
	        type: "POST",
	        url: ctx+'/public/common/areaselect/getAreaSelect?id=' + id,
	        cache: false,
	        dataType : "json",
	        success: function(data){
	        	$("#combobox_city").combobox("loadData",data);
	        }
	      }); 	
	    }
});    
	  
	  $('#combobox_city').combobox({ 
	      editable:false,
	      cache: false,
	      panelHeight: '150',//自动高度适合
	      valueField:'value',   
	      textField:'text',
	      onHidePanel: function(){
		        $("#combobox_area").combobox("setValue",'');//区级
		        var id = $('#combobox_city').combobox('getValue');		
		      	//alert(id);
		      	
		      $.ajax({
		        type: "POST",
		        url: ctx+'/public/common/areaselect/getAreaSelect?id=' + id,
		        cache: false,
		        dataType : "json",
		        success: function(data){
		        	$("#combobox_area").combobox("loadData",data);
		        }
		      }); 	
		   }
	     });
	  $('#combobox_area').combobox({ 
	      editable:false, //不可编辑状态
	      cache: false,
	      panelHeight: '150',//自动高度适合
	      valueField:'value',   
	      textField:'text'
	  });
	  
});

/**
 * 区域选择器
 */
var AreaSelector = {
    //在选择时的处理
	onSelect : function(r,parentId,targetComboboxId){
	  $('#'+targetComboboxId).combobox('reload',ctx+'/public/common/areaselect/getAreaSelect?id=' + r.value);
	  $('#'+targetComboboxId).combobox('setValue','');
	}	
		
}
