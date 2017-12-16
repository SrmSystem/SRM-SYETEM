/** 调查表管理对象 */
var vendorQEW = {
		search : function() {
			var searchParamArray = $('#form').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagridQWE').datagrid('load', searchParams);
		},
		toemil : function() {
			var selections = $('#datagridQWE').datagrid(
					'getSelections');
			if (selections.length == 0) {
				$.messager.alert('提示', '没有选择任何记录！', 'info');
				return false;
			}
			var params = $.toJSON(selections);
			$.ajax({
				url : ctx + '/manager/vendor/vendorQEW/toemilQWE',
				type : 'POST',
				data : params,
				contentType : 'application/json',
				success : function(data) {

					$.messager.show({
						title : '消息',
						msg : '发送邮件成功',
						timeout : 2000,
						showType : 'show',
						style : {
							right : '',
							top : document.body.scrollTop
									+ document.documentElement.scrollTop,
							bottom : ''
						}
					});

					$('#datagridQWE').datagrid('reload');

				}
			});
		}
}
