/**
 * 反馈
 */
function Feedback () {
	
	/**
	 * 显示反馈详情
	 */
	this.showFeedback = function(url) {
		new dialog().showWin($.i18n.prop('button.feedback'), 600, 480, ctx + url);   
	};   
	
	
	/**
	 * 提交反馈信息
	 * @param formId 表单ID
	 * @param tableId tableID
	 */
	this.feedback = function(formId, tableId) {
		$.messager.progress();
		$('#' + formId).form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/basedata/feedback/addFeedback', 
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				$('#feedbackContent').val(''); 
				$('#form-addfeedback').form('reset')
				try{
					var result = eval('('+data+')');
					if(result.success){
//						$.messager.show({ title:'消息', msg:  result.msg,  timeout:2000, showType:'show',
						$.messager.show({ title:$.i18n.prop('vendor.basedataJs.News'), msg:  result.msg,  timeout:2000, showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						var html = "";
						debugger;
						if(result.feedback.feedbackFileUrl != null && result.feedback.feedbackFileUrl != ""){
							html = "<a     style='margin-right: 10% '  href=\"javascript:;\" onclick=\"File.download( '"+result.feedback.feedbackFileName+"',  '"+result.feedback.feedbackFileName +"')\">"+result.feedback.feedbackFileName+ "</a>"
						}
						var trHtml = "";
						if(result.feedback.roleType == 1){
							trHtml = "<tr><td style='text-align: left'> &nbsp;<b> " + result.feedback.feedbackOrgName 
							+ "&nbsp; " + result.feedback.createUserName + " &nbsp;&nbsp; " + result.feedback.createTime
							+ "</b></td></tr><tr><td style='white-space: normal; color: blue;text-align: left' nowrap>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + result.feedback.feedbackContent
							+ "</td>"
							+"</tr>"
							+"<tr>"
							+"<td style='white-space: normal; text-align: right;  ' >"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "
							+ html
							+"</td>"
							+"</tr>"
							;
						}
						if(result.feedback.roleType == 0){
							trHtml = "<tr><td style='text-align: left'> &nbsp;<b> " + result.feedback.feedbackOrgName 
							+ "&nbsp; " + result.feedback.createUserName + " &nbsp;&nbsp; " + result.feedback.createTime
							+ "</b></td></tr><tr><td style='white-space: normal; text-align: left' nowrap>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + result.feedback.feedbackContent
							+ "</td>"
							+"</tr>"
							+"<tr>"
							+"<td style='white-space: normal; text-align: right;' >"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "
							+ html
							+"</td>"
							+"</tr>"
							;
						}

						$("#" + tableId).append(trHtml);
					}
				}catch (e) {  
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
				}
			}
		});
	}
	
}