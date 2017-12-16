/**
 * 弹窗口
 */
function dialog() {
	
	this.showWin = function(title, width, height, url,dialogId) {
		$dialog = $('<div/>').dialog({   
			id:dialogId,
            title: title,     
            width: width,     
            height: height,     
            iconCls : 'pag-search',    
            closed: true,     
            cache: false,     
            href: url,     
            modal: true,  
            //maximizable:true,
            toolbar:'#toolbar',  
            onLoad:function(){  
            	
            }, 
            onClose:function(){
                $(this).dialog('destroy');
            },
            buttons : [ 
             /*{    
                 text : '关闭',    
                 iconCls : 'ope-close',    
                 handler : function() {    
                     $dialog.dialog('close');    
                 }    
             } */
             ]  

       });    
        
	  $dialog.dialog('open');  
	};
	
}