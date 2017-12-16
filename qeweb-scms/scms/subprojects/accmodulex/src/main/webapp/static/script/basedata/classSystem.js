var classSystem = {
		
		 morningTimeFmt:function(v,r,i){
			 var morningStart = r.morningStart.toFixed(2);
			 var morningEnd = r.morningEnd.toFixed(2);
			return  classSystem.formatDoubleToDate(morningStart+"",morningEnd+"");
	      },
	        
	        afterTimeFmt:function(v,r,i){
	        	 var afterStart = r.afterStart.toFixed(2);
				 var afterEnd = r.afterEnd.toFixed(2);
	        	return classSystem.formatDoubleToDate(afterStart+"",afterEnd+"");
	        },
	        
	        
	        operateFmt:function(v,r,i){
	        	 var s = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="classSystem.deleteHolidays('+r.id+')" >'+$.i18n.prop('vendor.basedataJs.Delete')+'</a>';/*删除*/
	        	 return s;
	         },
	         
	         operateClassFmt:function(v,r,i){
	        	 var edit = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="classSystem.editClassSystem('+r.id+')" >'+$.i18n.prop('vendor.basedataJs.modify')+'</a>';/*修改*/
	        	 var s = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="classSystem.deleteClassSystem('+r.id+')" >'+$.i18n.prop('vendor.basedataJs.Delete')+'</a>';/*删除*/
	        	 return edit +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+ s;
	         },
	        
		searchHoliday:function(){
			var searchParamArray = $('#form-holidays-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-holidays-list').datagrid('load',searchParams);
		},

         addHoliday:function(){
        	debugger;
        	 $('#win-holidays-edit').dialog({
     			iconCls:'icon-add',
     			title:$.i18n.prop('vendor.basedataJs.NewAddWeekSet')/*'新增节假日设置'*/
     		});
     		
     		$('#win-holidays-edit').dialog('open');
     		$('#form-holidays-edit').form('clear');
        	 
         },
         
         
         
         submitHolidays:function(){
        	
        	 $.messager.progress({
        			title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
        			msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
        		});
        	 $('#form-holidays-edit').form('submit',{
        		 ajax:true,
        		 url:ctx+'/manager/common/classSystem/addHolidays',
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
        						
        						$('#win-holidays-edit').dialog('close');
        						$('#datagrid-holidays-list').datagrid('reload');
        					}else{
        						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');
        					}
        					}catch (e) {
        						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');
        					}
        					
        		 }
        	 })
         },
        
         
         deleteHolidays:function(id){
        	 var sucMeg = $.i18n.prop('vendor.basedataJs.OperateSuccess');/*"操作成功"*/
 //       	 $.messager.confirm('提示','确定删除该节加入信息',function(r){
        	 $.messager.confirm($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.SureDeleteDayInfo'),function(r){
        		 if(r){
        			 
        			 $.ajax({
        				 url:ctx+'/manager/common/classSystem/deleteHolidays/'+id,
        				 success:function(){
        					      $.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),sucMeg,'info');
             						$('#datagrid-holidays-list').datagrid('reload');
             					
        				 }
        			 })
        		 }
        	 })
         },
        
         
         editClassSystem:function(id){
        	 $('#win-classSystem-edit').dialog({
        			iconCls:'icon-edit',
        			title:$.i18n.prop('vendor.basedataJs.modifyTeamSet')/*'修改班制设置'*/
        		});
        		
        		$('#form-classSystem-edit').form('clear');
        		$('#win-classSystem-edit').dialog('open');
        		classSystem.timeSelect();
        		$('#form-classSystem-edit').form('load',ctx+'/manager/common/classSystem/getClassSystemById/'+id+"?time="+new Date());
         },
         
        timeSelect:function(){
        	 $('#morningStart').combobox({
        		 url:ctx+'/manager/common/classSystem/setTime',
        		editable:false,
    	        cache: false,
    	        required:true,
    	        valueField:'value',   
    	        textField:'text',
    	   }); 
        	 $('#morningEnd').combobox({
        		 url:ctx+'/manager/common/classSystem/setTime',
        		editable:false,
    	        cache: false,
    	        required:true,
    	        valueField:'value',   
    	        textField:'text',
    	   }); 
        	 $('#afterStart').combobox({
        		 url:ctx+'/manager/common/classSystem/setTime',
        		editable:false,
    	        cache: false,
    	        required:true,
    	        valueField:'value',   
    	        textField:'text',
    	   });
        	 $('#afterEnd').combobox({
        		 url:ctx+'/manager/common/classSystem/setTime',
        		editable:false,
    	        cache: false,
    	        required:true,
    	        valueField:'value',   
    	        textField:'text',
    	   }); 
         },
         
         
         deleteClassSystem:function(id){
        	 var sucMeg = $.i18n.prop('vendor.basedataJs.OperateSuccess');/*"操作成功"*/
//        	 $.messager.confirm('提示','确定删除该班制信息',function(r){
        	 $.messager.confirm($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.MakeSureDeleteUnitInfo'),function(r){
        		 if(r){
        	 
        	 $.ajax({
        		 url:ctx+'/manager/common/classSystem/deleteClassSystem/'+id,
        		 success:function(){
        			 
        			 $.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),sucMeg,'info');
      						$('#datagrid-classSystem-list').datagrid('reload');
      					
        		 }
        	 })
        		 }
        	 })
         },
         
         
         addClassSystem:function(){
        	 $('#win-classSystem-edit').dialog({
     			iconCls:'icon-edit',
     			title:$.i18n.prop('vendor.basedataJs.AddUnitSet')/*'新增班制设置'*/
     		});
     		$('#form-classSystem-edit').form('clear');
     		$('#classSystemId').val(0);
     		$('#win-classSystem-edit').dialog('open');
     		classSystem.timeSelect();
         },
         
         searchClassSystem: function(){
        	 var searchParamArray = $('#form-classSystem-search').serializeArray();
 			var searchParams = $.jqexer.formToJson(searchParamArray);
 			$('#datagrid-classSystem-list').datagrid('load',searchParams);
         },
         
         submitClassSystem: function(){
        	 $.messager.progress({
     			title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
     			msg : $.i18n.prop('vendor.basedataJs.Submission') /*'提交中...'*/
     		});
     	 $('#form-classSystem-edit').form('submit',{
     		 ajax:true,
     		 url:ctx+'/manager/common/classSystem/addClassSystem',
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
     						
     						$('#win-classSystem-edit').dialog('close');
     						$('#datagrid-classSystem-list').datagrid('reload');
     					}else{
     						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');
     					}
     					}catch (e) {
     						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');
     					}
     					
     		 }
     	 })
      },
      
      
        formatDoubleToDate: function(timeStart,timeEnd){
        	var times = timeStart.replace('.',':');
        	var timee = timeEnd.replace('.',':');
        	return times+"-"+timee;
        	
        },
        
       refreshTabClassSystem : function(title,index){
    	   var $tabs = $('#classSystem-tabs');
   		var tab = $tabs.tabs('getTab',index);
   		var phaseId = $(tab).attr('itemId');
   		if(phaseId = 1){
   			$('#datagrid-classSystem-list').datagrid('reload');
   		}else{
   			$('#datagrid-holidays-list').datagrid('reload');
   		}
         
       },
       getYear: function(date){
    	   var arr = (date+"").split('-').item(0);
    	   $('#year').text(arr);
       }
              
         
     
         
         
         
         
}