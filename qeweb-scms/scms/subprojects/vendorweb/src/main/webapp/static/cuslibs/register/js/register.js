//显示提示框，目前三个参数(txt：要显示的文本；time：自动关闭的时间（不设置的话默认1500毫秒）；status：默认0为错误提示，1为正确提示；)
function showTips(txt,time,status)
	{
		var htmlCon = '';
		if(txt != ''){
			if(status != 0 && status != undefined){
				htmlCon = '<div class="tipsBox" style="width:220px;padding:10px;background-color:#4AAF33;border-radius:4px;-webkit-border-radius: 4px;-moz-border-radius: 4px;color:#fff;box-shadow:0 0 3px #ddd inset;-webkit-box-shadow: 0 0 3px #ddd inset;text-align:center;position:fixed;top:25%;left:50%;z-index:999999;margin-left:-120px;"><img src="'+ctx+'/register/images/ok.png" style="vertical-align: middle;margin-right:5px;" alt="OK，"/>'+txt+'</div>';
			}else{
				htmlCon = '<div class="tipsBox" style="width:220px;padding:10px;background-color:#D84C31;border-radius:4px;-webkit-border-radius: 4px;-moz-border-radius: 4px;color:#fff;box-shadow:0 0 3px #ddd inset;-webkit-box-shadow: 0 0 3px #ddd inset;text-align:center;position:fixed;top:25%;left:50%;z-index:999999;margin-left:-120px;"><img src="'+ctx+'/register/images/err.png" style="vertical-align: middle;margin-right:5px;" alt="Error，"/>'+txt+'</div>';
			}
			$('body').prepend(htmlCon);
			if(time == '' || time == undefined){
				time = 1500; 
			}
			setTimeout(function(){ $('.tipsBox').remove(); },time);
		}
	}		
		$(function(){
			//AJAX提交以及验证表单
			$('#nextBtn').click(function(){
				var loginName = $('#loginName').val();
				var name = $('#name').val();
				var password = $('#password').val();
				var confirmPassword = $('#confirmPassword').val();
				var orgName = $('#orgName').val();
				var orgPhone = $('#orgPhone').val();
				var orgEmail = $('#orgEmail').val();
				var EmailReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; //邮件正则
				if(loginName == ''){
					showTips('请填写您的登录名！');
				}else if(name == ''){
					showTips('请填写您的用户名！');
				}else if(password == ''){
					showTips('请填写您的密码！');
				}else if(confirmPassword == ''){
					showTips('请再次输入您的密码！');
				}else if(confirmPassword != confirmPassword || confirmPassword != confirmPassword){
					showTips('两次密码输入不一致！');
				}else if(orgName == ''){
					showTips('请填写您的企业全称！');
				}else if(orgPhone == ''){
					showTips('请填写您的联系电话！');
				}else if(orgEmail == ''){
					showTips('请填写您的邮箱！');
				}else if(!EmailReg.test(orgEmail)){
					showTips('您的邮箱格式错误！');
				}else {
					$("#inputForm").submit();
				}
			});

		});
