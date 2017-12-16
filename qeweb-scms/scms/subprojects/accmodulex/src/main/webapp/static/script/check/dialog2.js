/**
 * 弹窗口
 */
var $dialog;
function dialog() {
	
	this.showWin = function(title, width, height, url,fresh_gridIds,isMax,load_gridIds) {
		$dialog = $('<div/>').dialog({
            title: title,     
            width: width,     
            height: height,     
            iconCls : 'pag-search',    
            closed: true,   
            onClose:function(){
            	//window.location.reload();
            	//$dialog.dialog('close');
                for(var i=0;i<fresh_gridIds.length;i++){
                	$('#'+fresh_gridIds[i]).datagrid('reload');
                }
                $(this).dialog('destroy');
            },
            cache: false,
            href: url,     
            modal: true,  
            maximizable:true,
            maximized:isMax,
            toolbar:'#toolbar',  
            onLoad:function(){  
            	//var easyUiTables = $('.easyui-datagrid');
        		for(j=0;j<load_gridIds.length;j++){
        			//var easyUiTable = easyUiTables[j];
        			var tt = $('#'+load_gridIds[j]);
        			var opts = tt.datagrid('getColumnFields'); //这是获取到所有的FIELD
        			var colName=[];
        			for(i=0;i<opts.length;i++)
        			{
        				var col = tt.datagrid( "getColumnOption" , opts[i] );
        				//alert(col.formatter);
        				if(col.field.indexOf('.')>0){
        					if(col.formatter==undefined){
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
       });
		$dialog.dialog('open'); 
	};
	
	this.close = function () {
		try{
			info($dialog)
			if($dialog) {
				$dialog.dialog('close');
				$dialog.dialog('destroy');
			}
		}catch(e){
			//do nothing
		}
		
	}
}