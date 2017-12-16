var ExportHandler = {
		doExport:function(formId,tableId,button){
			var isValid = $('#'+formId).form('validate');
			if(!isValid){
				return false;
			}
			var cols = $('#'+tableId).datagrid('options').columns;
			var reqUrl = $('#'+tableId).datagrid('options').url;
			if(reqUrl==null){
				alert('必须先执行查询！');
				return false;
			}
			var btnName = '导出';
			if(button){
				btnName = $(button).text();
				$(button).linkbutton("disable");
				$(button).linkbutton({text:'导出中...'});
			}
			var searchParamArray = $('#'+formId).serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
		 	searchParams.page = 1;
			searchParams.rows = 10000;
			$.ajax({
				url:reqUrl,
				type:'POST',
				data: searchParams,
				dataType:"json",
				contentType : 'application/x-www-form-urlencoded',
				success:function(data){
					var col2 = new Array();
					var j = 0;
					for(var i in cols[0]){
						if(cols[0][i].title){
							
							if(cols[0][i].title.lastIndexOf('附件')<0&&cols[0][i].title.lastIndexOf('操作')<0){
								col2[j++] = cols[0][i];
							}
						}
					}
					
					for(var i in col2){
						var col = col2[i];
						if(col.formatter){
							
								var formatter_func;
								var formatter_string = col.formatter.toString();
								if(formatter_string.indexOf("StatusRender")>0){
									formatter_string = formatter_string.replace("true","false");
									formatter_func = (new Function('return '+formatter_string))();
								}else{
									formatter_func = col.formatter;
								}
								
								for(var j in data.rows){
									var row = data.rows[j];
									var rs = formatter_func(eval('row.'+col.field),row,j);
									eval('row.'+col.field+"='"+rs+"'");
								}
							
							
						}
					}
					var data1 = JSON.stringify(data.rows);
					var data2 = JSON.stringify(col2);
					
					var form_ming_zi_bu_neng_chong_fu = $("<form>");
					form_ming_zi_bu_neng_chong_fu.attr('style', 'display:none');
					form_ming_zi_bu_neng_chong_fu.attr('target', '');
					form_ming_zi_bu_neng_chong_fu.attr('enctype','multipart/form-data');
					form_ming_zi_bu_neng_chong_fu.attr('method', 'post');
					form_ming_zi_bu_neng_chong_fu.attr('action', ctx+'/manager/export/exp');

		            var input1 = $('<input>');
		            input1.attr('type', 'hidden');
		            input1.attr('name', 'cols');
		            input1.attr('value', data2);
		            
		            var input2 = $('<input>');
		            input2.attr('type', 'hidden');
		            input2.attr('name', 'rows');
		            input2.attr('value', data1);
		            $('body').append(form_ming_zi_bu_neng_chong_fu);
		            form_ming_zi_bu_neng_chong_fu.append(input1);
		            form_ming_zi_bu_neng_chong_fu.append(input2);
		            form_ming_zi_bu_neng_chong_fu.submit();
		            form_ming_zi_bu_neng_chong_fu.remove();
		            
					if(button){
						$(button).linkbutton("enable");
						$(button).linkbutton({text:btnName});
					}
				}, 
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			}); 
		}
}