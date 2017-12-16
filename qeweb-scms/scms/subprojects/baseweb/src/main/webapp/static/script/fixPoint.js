$(function(){
	var fixPoint = function (){
		var easyUiTables = $('.easyui-datagrid');
		for(j=0;j<easyUiTables.length;j++){
			var easyUiTable = easyUiTables[j];
			var tt = $('#'+easyUiTable.id);
			var opts = tt.datagrid('getColumnFields'); //这是获取到所有的FIELD
			var colName=[];
			for(i=0;i<opts.length;i++)
			{
				var col = tt.datagrid( "getColumnOption" , opts[i] );
				//alert(col.formatter);
				if(col.field.indexOf('.')>0){
					if(col.formatter==undefined&&col.editor==undefined){
						col.formatter = function(v,r,i){
							try{
								return eval("(r."+this.field+")");
							}catch(e){
								return '';
							}
							
						};
					}
				}
			}
			tt.datagrid();
		}
	}
	fixPoint();
});