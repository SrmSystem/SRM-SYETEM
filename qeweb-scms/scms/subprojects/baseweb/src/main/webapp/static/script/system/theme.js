function changeTheme(theme){
	 var $easyuiTheme = $('#easyuiTheme');
     var url = $easyuiTheme.attr('href');
     var href = url.substring(0, url.indexOf('themes')) + 'themes/' + theme.value + '/easyui.css';
     $easyuiTheme.attr('href', href);
     changeFrameTheme(href);
     $.cookie('easyuiThemeName', theme.value, {
      	  expires : 7
       });
//     $('#userTheme').val(theme.value);
//     $('#themeForm').form('submit',{
//    	 url:ctx+'/manager/admin/user/setUserTheme',
//    	 ajax:true,
//    	 success:function(data){
//    		 
//    	 }
//    	 
//     });
     $.ajax({
    	 url:ctx+'/manager/setUserTheme',
    	 type:'POST',
 		 data:{userTheme:theme.value},
    	 success:function(data){}
     });
}

function initTheme(){
	if($.cookie('easyuiThemeName')){
		$('#stylecombobox').combobox('select',$.cookie('easyuiThemeName'));
		changeTheme({value:$.cookie('easyuiThemeName')});
	}else{
		changeTheme({value:'default'});
	}
	
}

function changeFrameTheme(href){
	var $iframe = $('iframe');
    if ($iframe.length > 0) {
        for ( var i = 0; i < $iframe.length; i++) {
            var ifr = $iframe[i];
            $(ifr).contents().find('#easyuiTheme').attr('href', href);
        }
    }
}

function initFrameTheme(){
	if($.cookie('easyuiThemeName')){
		 var $easyuiTheme = $('#easyuiTheme');
	     var url = $easyuiTheme.attr('href');
	     var href = url.substring(0, url.indexOf('themes')) + 'themes/' + $.cookie('easyuiThemeName') + '/easyui.css';
	     changeFrameTheme(href);
	}
	
}

function getTheme(){
	if($.cookie('easyuiThemeName')){
		return $.cookie('easyuiThemeName');
	}
	return 'default'
}

$(function(){
	//initTheme();	
});