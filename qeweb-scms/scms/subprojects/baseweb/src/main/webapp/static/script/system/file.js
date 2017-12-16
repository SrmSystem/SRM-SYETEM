var File = {
	download : function(filePath,fileName){
		if(filePath==null || filePath==''){
			$.messager.alert('提示','文件路径不能为空','warning');
			return false;
		}
		var url = ctx+'/common/download';
		var fileName = fileName==null?"":fileName;
		var inputs = '<input type="hidden" name="filePath" value="'+filePath+'"><input type="hidden" name="fileName" value="'+fileName+'">';
		jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
        .appendTo('body').submit().remove();
	},
	
	showLog : function(filepath){ 
		var url = ctx + '/common/download/log';
		var newWindow = window.open(url, name);
		if (!newWindow) {
			return false;
		}
		var html = "";
		html += "<html><head></head><body><form id='formid' method='post' action='" + url + "'>";
		html += "<input type='hidden' name='filepath' value='" + filepath + "'/>";
		html += "</form><script type='text/javascript'>document.getElementById(\"formid\").submit()</script></body></html>";
		newWindow.document.write(html);
		return newWindow;  
	}     
}
     
