var Iconer = {
	open : function(targetId){
		if($('#window-iconer').length<=0)
			$(document.body).append('<div id="window-iconer"><div itemId="ct"></div></div>');
		$('#window-iconer').window({
			closed:true,
			width:300,
			height:300,
			title:'图标选择(双击选择)',
			modal:true
		});
		$.ajax({
			url : ctx+'/public/component/icon',
			dataType : 'json',
			success : function(data){
				$('#window-iconer').window('open');
				var html = ''
				$.each(data,function(i,n){
					html+='<button ondblclick="Iconer.select(\''+n+'\',\''+ctx+'/static/style/icons/IconsExtension/'+n+'\',\''+targetId+'\')" class="btn btn-link"><img src="'+ctx+'/static/style/icons/IconsExtension/'+n+'"/></button>'
				});
				$('#window-iconer').getCmp('ct').html(html);
			}
			
		});
		
	},
	select : function(icon,src,targetId){
		$('#'+targetId).val(icon);
		$('#'+targetId+'-text').html('<img src="'+src+'"/>');
		$('#window-iconer').window('close');
	}
		
		
}