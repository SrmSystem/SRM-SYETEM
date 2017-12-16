/**
 * 弹窗口
 */
var $dialog;
function dialog() {
	
	/**
	 * 弹出窗口
	 */
	this.showWin = function(title, width, height, url) {
		$dialog = $('<div/>').dialog({     
            title: title,     
            width: width,     
            height: height,     
            iconCls : 'pag-search',    
            closed: true,     
            cache: false,     
            href: url,     
            modal: true,  
            maximizable:true,
            //maximized:true,
            toolbar:'#toolbar',
            onLoad:function(){  
            	
            },               
            onClose:function(){
				 RowEditor.editIndex = undefined;
				 CellEditor.editIndex = undefined;
                $(this).dialog('destroy');
            },
            buttons : [ 
             //{    
             //    text : '关闭',    
             //    iconCls : 'ope-close',    
             //    handler : function() {    
             //        $dialog.dialog('close');    
              //   }    
             //} 
             ]  

       });    
        
	  $dialog.dialog('open');  
	};
	
	this.close = function () {
		try{
			if($dialog) {
				$dialog.dialog('destroy');
				$dialog.dialog('close');
			}
		}catch(e){
			//do nothing
		}
		
	}
}