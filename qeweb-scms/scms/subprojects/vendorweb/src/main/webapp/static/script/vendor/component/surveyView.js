var SurveyView = {
	viewBaseHisFmt : function(v,r,i){//查看基本信息历史按钮格式化
		return '<button onclick="SurveyView.viewBase('+r.id+')" class="btn-link">V'+v+'</button>';
	},
	viewHisFmt : function(v,r,i){
		return '<button onclick="SurveyView.view('+r.id+')" class="btn-link">V'+v+'</button>';
	},
	viewAuditTime : function(v,r,i){
		if(r.auditStatus==0)
			return '';
		else
			return v;
	},
	view : function(id){
		var url = ctx+'/manager/vendor/admittance/getSurveyView/'+id;
		var $dialog = $('#dialog-survey-hisview');
		$dialog.dialog({
			  href: url,
			  onOpen : function(){
				  $dialog.dialog('autoSizeMax',{body:true});
			  }
		  });
		$dialog.dialog('open');
	},
	viewBase : function(id){
		var url = ctx+'/manager/vendor/admittance/viewBaseSurveyPage/'+id;
		var $dialog = $('#dialog-survey-hisview');
		$dialog.dialog({
			  href: url,
			  onOpen : function(){
				  $dialog.dialog('autoSizeMax',{body:true});
			  }
		  });
		$dialog.dialog('open');
	},
	initConvertField : function(ctId){//初始化那些需要转换显示的字段
		$(ctId).find('.select-url').each(function(i){//转换select,实际为hidden
			var $select = $(this);
			var val = $select.val();
			var url = $select.attr('url');
			if(val=='' || url=='')return true;
			url = SurveyView.convertSelectUrl(url);
			$.ajax({
				url : ctx+url,
				data : {id:$select.val()},
				cache : false,
				method: 'post',
				dataType:'json',
				success : function(data){
					var item = data[0];
					$select.parent().find('.select-text:eq(0)').html(item.text);
				}
			}); 
		});
	},
	convertSelectUrl : function(url){
		if(url.indexOf('#')!=-1){
			//正则替换
			var match = url.match(/(#[a-zA-z]+)/ig);
			$.each(match,function(i,n){
				var rep = $(n).val();
				url = url.replace(n,rep);
			});
		}
		return url;
	}
}