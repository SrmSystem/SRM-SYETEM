/** 调查表管理对象 */
var Survey = {
	init : function(cfgId,version,title){//调查表的初始化
		var ctId = 'survey-'+cfgId+'-ct';
		var $ct = $('#'+ctId);
		var $tbCheck = $ct.find('.tableCheck:eq(0)');
		$tbCheck.bind('click',function(){
			var $tb = $(this).parentsUntil('table').parent();
			if($(this).is(':checked')){
				$tb.find(':checkbox[name="rowCk"]').prop('checked',true);
			}else{
				$tb.find(':checkbox[name="rowCk"]').prop('checked',false);
			}
		});
		var $selectArray = $ct.find('.select-url');
		$selectArray.each(function(){
			var $select = $(this);
			var val = $select.attr('value');
			var url = $select.attr('url');
			if(val=='' && url=='')return true;
			url = Survey.convertSelectUrl(url);
			$.ajax({
				url : ctx+url,
				//data : {id:$select.val()},
				method: 'post',
				dataType:'json',
				success : function(data){
					$.each(data,function(i,n){
						var selected = '';
						if(n.value==val){
							selected = 'selected="selected"';
						}
						$select.append('<option '+selected+' value="'+n.value+'">'+n.text+'</option>');
					});
					
					
				}
			}); 
			
		});
		
		var $selectRelArray = $ct.find('.select-rel');
		$.each($selectRelArray,function(){
			var obj = this;
			$(this).bind('focus',function(){Survey.selectRelOnClick(obj)});
		});
		Survey.initValidate(ctId);
		initFormula();
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
	},
	clearValidate : function(ctId){
		var $ct = $('#'+ctId);
		var $reqArray = $.merge($ct.find('input[required="true"]'),$ct.find('select[required="true"]'));
		$reqArray.each(function(i){
			$(this).removeClass('validatebox-invalid');
			$(this).parent().removeClass('textbox-invalid');
		});
	},
	dealValidate : function($obj){
		if($obj.prop('required')==true){
			if($obj.val()!=''){
				$obj.removeClass('validatebox-invalid');
				$obj.parent().removeClass('textbox-invalid');
			}else{
				$obj.addClass('validatebox-invalid');
				$obj.parent().addClass('textbox-invalid');
			}
		}
	},
	initValidate : function(ctId){//对各种类型的初始化
		var $ct = $('#'+ctId);
		var $reqArray = $.merge($ct.find('input[required="true"]'),$ct.find('select[required="true"]'));
		$reqArray.each(function(i){
			var requiredOpts = {
				content : '该输入项为必输项',
				showEvent:"none",hideEvent:"none",showDelay:0,hideDelay:0,zIndex:"",
				onShow: function(){$(this).tooltip('tip').css({color:"#000",borderColor:"#CC9933",backgroundColor:"#FFFFCC"});},
			    onHide:function(){$(this).tooltip("destroy");}
			};
			var relVal = Survey.getValidateVal(this);
			
			if(relVal==null || relVal==''){
				$(this).addClass('validatebox-invalid');
				$(this).parent().addClass('textbox-invalid');
			}
			$(this).bind('keyup blur focus change',function(){
				var relVal = Survey.getValidateVal(this);
				if(relVal==null || relVal==''){
					$(this).addClass('validatebox-invalid');
					$(this).parent().addClass('textbox-invalid');
					$(this).tooltip(requiredOpts).tooltip('show');
				}else{
					$(this).removeClass('validatebox-invalid');
					$(this).parent().removeClass('textbox-invalid');
					$(this).tooltip('hide');
				}
				
			});
			$(this).bind('blur',function(){
				$(this).tooltip('hide');
			});
			$(this).bind('mouseenter',function(){
				var relVal = Survey.getValidateVal(this);
				if(relVal==null || relVal==''){
					$(this).tooltip(requiredOpts).tooltip('show');
				}
			});
			$(this).bind('mouseleave',function(){
				$(this).tooltip('hide');
			});
			
			
		});
	},
	getValidateVal : function(obj){//主要是为了获得各种标签的真实值
		var val = $(obj).val();
		if($(obj).is('select')){
			val = $(obj).attr('value');
			if(val==null || val==''){
				var valObj = $(obj).find('option:selected')[0];
				val = (valObj==null?'':valObj.value);
			}
		}
		return val;
	},
	selectOnClick : function(obj){//下拉数据库的触发时间
		var url = $(obj).attr('url');
		if(url==null || url=='')return;
		url = Survey.convertSelectUrl(url);
		var html = $(obj).html();
		if(html==''){
		$.ajax({
			async : false,
			cache : false,
			url : ctx+url,
			dataType : 'json',
			success : function(data){
				$.each(data,function(i,n){
					$(obj).append('<option value="'+n.value+'">'+n.text+'</option>');
				});
			}
			
		});
		}
	},	
	selectRelOnClick : function(obj){//引用下拉的触发事件
		var url = $(obj).attr('url');
		if(url==null || url=='')return;
		var paramsArray = url.split('/');
		if(paramsArray.length!=2){
			$.messager.alert('提示','该模版该项配置不对!请联系管理员','warning');
			return false;
		}
		var ctId = paramsArray[0];
		var colName = paramsArray[1]
		var $ct = $('#'+ctId);
		var $colArray = $.merge($ct.find('input[name="'+colName+'"]'),$ct.find('select[name="'+colName+'"]'));
		if($colArray.length<=0){
			$.messager.alert('提示','前置数据未填','warning');
			return false;
		}
		var colType = $($colArray[0]).attr('type');
		var options = [];
		$colArray.each(function(){
			
			var option = {};
			if(colType=='text'){
				option.value = $(this).value();
				option.text = $(this).value();
			}else if(colType=='select'){
				option.value = $(this).find('option:selected').text();
				option.text = $(this).find('option:selected').text();
			}
			
			var reFlag = false;
			$.each(options,function(i,n){
				if(n.value == option.value){
					//重复
					reFlag = true;
					return false;
				}
				
			});
			if(!reFlag)
			options.push(option);
		});
		$(obj).html('');
		$.each(options,function(i,n){
			$(obj).append('<option value="'+n.value+'">'+n.text+'</option>');
		});
		
	},	
	saveBase : function(formId){
		var temp = validateInfo();
	if(temp){
		$("#hiddenMainBU").val($('#testsetset').combobox('getValues'));
	  var $ct = $('#'+formId+'-ct');
	  //校验附件大小
	  var flag = validateAttachmentSize($ct.prop("id"));
	  if(!flag)return false;
	  var item = Survey.getFormItem($ct);
	  var $baseInfo = $('#'+formId).getCmp('baseInfo');
	  $.messager.progress();
	  $baseInfo.val($.toJSON(item));
	  $('#'+formId).form('submit',{
		  url : ctx+'/manager/vendor/admittance/saveBaseInfo',
		  success : function(data){
			  $.messager.progress('close');
			  data = $.parseJSON(data);
			  if(data.success){//通过之后需要根据状态重置组件
				$.messager.alert('提示','保存成功','info');
				var surveyCode = 'base';
				var surveyCfgId = data.surveyCfgId;
				var node = $('#tree-survey').tree('find',surveyCfgId);
				if(node){
					$('#tree-survey').tree('update',{
						target : node.target,
						iconCls : 'icon-bullet_edit'
					});
				}
				Survey.setStatusValue('base',data);
				Survey.initComponent('base');
				Survey.refreshHis('base',data.orgId,data.currentId);
			}
			  
		  }
	  });
	}
	},
	submitBase : function(formId){
		$("#hiddenMainBU").val($('#testsetset').combobox('getValues'));
		var $ct = $('#'+formId+'-ct');
		var $validateForm = $ct.getCmp('validate-form');
		var item = Survey.getFormItem($ct);
		var $baseInfo = $('#'+formId).getCmp('baseInfo');
		$baseInfo.val($.toJSON(item));
		var validate = $validateForm.form('validate');
		if(!validate){
			$.messager.alert('提示','有必填项未填!','warning');
			return false;
		}
		//校验文件大小
		validate = validateAttachmentSize($ct.prop("id"));
		if(!validate)return false;
		$.messager.progress();
		$('#'+formId).form('submit',{
			url : ctx+'/manager/vendor/admittance/submitVendorBase',
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				data = $.parseJSON(data);
				//通过之后，需要将按钮隐藏，改变树的状态。将所有input变为disabled
				if(data.success){
					$.messager.alert('提示','提交成功','info');
					var surveyCfgId = data.surveyCfgId;
					var node = $('#tree-survey').tree('find',surveyCfgId);
					if(node){
						$('#tree-survey').tree('update',{
							target : node.target,
							iconCls : 'icon-hourglass'
						});
					}
					Survey.setStatusValue('base',data);
					Survey.initComponent('base');
					Survey.refreshHis('base',data.orgId,data.currentId);
				}else{
					$.messager.fail(data.msg);
				}
			}
		});
	},
	save : function(formId){
	  var ctId = formId+'-ct';
	  Survey.clearValidate(ctId);
	  var flag = Survey.setSurveyValue(formId,'surveyBase');
	  if(!flag)return;
	  var $ct = $('#'+ctId);
	//校验附件大小
	  flag = validateAttachmentSize($ct.prop("id"));
	  if(!flag)return false;
	//校验必填项
//	  var vali = surveyValidation(formId);
//	  if(!vali)return false;
	  $.messager.progress();
	  $('#'+formId).form('submit',{
		  url : ctx+'/manager/vendor/admittance/saveSurvey',
		  novalidate : false,
		  success : function(data){
			  Survey.initValidate(ctId);
			  $.messager.progress('close');
			  data = $.parseJSON(data);
			  if(data.success){
				  $.messager.alert('提示','保存成功','info');
				  var surveyCfgId = data.surveyBase.vendorCfgId;
				  var node = $('#tree-survey').tree('find',surveyCfgId);
				  if(node){
					$('#tree-survey').tree('update',{
						target : node.target,
						iconCls : 'icon-bullet_edit'
					});
				  }
				  Survey.setStatusValue(surveyCfgId,data);
				  Survey.initComponent(surveyCfgId);
				  Survey.refreshHis(surveyCfgId,surveyCfgId,data.currentId);
			  }else{
				  $.messager.alert('提示',data.msg,'error');
				  
			  }
		  }
	  });
		
	},
	submit : function(formId){
		//校验必填项
	  var flag = surveyValidation(formId);
	  if(!flag)return false;
	  //校验附件大小
	  var ctId = formId+'-ct';
	  flag = validateAttachmentSize(ctId);
	  if(!flag)return false;
	  //构造报价对象
	  flag = Survey.setSurveyValue(formId,'surveyBase');
	  if(!flag)return false;
	  $.messager.progress();
	  $('#'+formId).form('submit',{
		  url : ctx+'/manager/vendor/admittance/submitVendorSurvey',
		  dataType : 'json',
		  success : function(data){
			$.messager.progress('close');
			data = $.parseJSON(data);
			//通过之后，需要将按钮隐藏，改变树的状态。将所有input变为disabled
			if(data.success){
				$.messager.alert('提示','提交成功','info');
				var surveyCfgId = data.surveyBase.vendorCfgId;
				var node = $('#tree-survey').tree('find',surveyCfgId);
				if(node){
					$('#tree-survey').tree('update',{
						target : node.target,
						iconCls : 'icon-hourglass'
					});
				}
				Survey.setStatusValue(surveyCfgId,data);
				Survey.initComponent(surveyCfgId);
			    Survey.refreshHis(surveyCfgId,surveyCfgId,data.currentId);
			}else{
				$.messager.alert('提示',data.msg,'error');
			}
			
		  }
	  });

	},
	setSurveyValue : function(formId,valId){//设置调查表的值
		var surveyBase = {};
		var $surveyForm = $('#'+formId);
		//获得当前调查表的内容容器,规则是formId+"-ct"
		var $surveyCt = $('#'+formId+'-ct');
		surveyBase.templateId = $surveyCt.getCmp('templateId').val();
		surveyBase.templatePath = $surveyCt.getCmp('templatePath').val();
		surveyBase.templateCode = $surveyCt.getCmp('templateCode').val();
		surveyBase.vendorCfgId = $surveyCt.getCmp('surveyCfgId').val();
		surveyBase.id = $surveyCt.getCmp('id').val();
		//获得所有内容类型的容器
	    var $surveyConCt = $surveyCt.find('[name="ct"]');	
	    //循环这些容器，并组装出调查表内容信息。文件类型额外处理
	    var itemList = [];
	    $surveyConCt.each(function(i){
			//要判断是frm还是table
			var type = $(this).attr('type');
			if(type=='frm'){
				var item = Survey.getFormItem($(this));
				item.ctId = $(this).attr('id');
				item.ctCode = "form";
				item.dataType = 0;
				item.templateId = surveyBase.templateId;
				itemList.push(item);
			}else if(type=='tb'){
				//如果是tb，就要获取每个tr
				var $tb = $(this);
				var $trArray = $($(this).find('tbody')[0]).find('tr');
				$trArray.each(function(i){
					var fixed = $(this).attr('fixed');
					var $id = $(this).find(':checkbox:eq(0)');
					var item = {};
					item.fixed = fixed;
					item.dataType = 1;
					item.templateId = surveyBase.templateId;
					item.ctCode = 'tbody';
					item.ctId = $tb.attr('id');
					var $tdTextArray = $(this).find('input[type="text"]');
					var $hiddenArray = $(this).find('input[type="hidden"]');
					var $selectArray = $(this).find('select');
					var $fileArray = $(this).find('input[type="file"]');
					$tdTextArray.each(function(t){
						//先要处理下name
						var field = $(this).attr('name');
						item[field] = $(this).val();
					});
					$hiddenArray.each(function(t){
						//先要处理下name
						var field = $(this).attr('name');
						item[field] = $(this).val();
					});
					$selectArray.each(function(t){
						//先要处理下name
						var field = $(this).attr('name');
						item[field] = $(this).val();
					});
					
					itemList.push(item);
				});
				var $tfArray = $($(this).find('tfoot')[0]).find('tr');
				$tfArray.each(function(i){
					var item = {};
					item.ctCode = "tfoot";
					item.dataType = 1;
					item.ctId = $tb.attr('id');
					var $tdTextArray = $(this).find('input[type="text"]');
					var $hiddenArray = $(this).find('input[type="hidden"]');
					$tdTextArray.each(function(t){
						//先要处理下name
						var field = $(this).attr('name');
						item[field] = $(this).val();
					});
					$hiddenArray.each(function(t){
						//先要处理下name
						var field = $(this).attr('name');
						item[field] = $(this).val();
					});
					itemList.push(item);
				});
			}
	    });
	    
	    surveyBase.itemList = itemList;
	    if(itemList==null || itemList.length<=0){
	    	$.messager.alert('提示','数据不能为空','warning');
	    	return false;
	    }
	    var $surveyBase = $surveyForm.getCmp(valId);
	    $surveyBase.val(JSON.stringify(surveyBase));
	    return true;
	},
	getFormItem : function($obj){//获取form的数据，转成JSON
		var item = {};
		var $textArray = $obj.find('input[type="text"]');
		var $hiddenArray = $obj.find('input[type="hidden"]');
		var $selectArray = $obj.find('select');
		$selectArray.each(function(t){
			//先要处理下name
			var field = $(this).attr('name');
			if(field==null)return true;
			item[field] = $(this).val();
		});
		$textArray.each(function(t){
			//先要处理下name
			var field = $(this).attr('name');
			if(field==null)return true;
			item[field] = $(this).val();
		});
		$hiddenArray.each(function(t){
			//先要处理下name
			var field = $(this).attr('name');
			if(field==null)return true;
			item[field] = $(this).val();
		});
		//处理checkbox
		var $checkBoxArray = $obj.find('input[type="checkbox"]');
		var checkboxItem = {};
		$checkBoxArray.each(function(t){
			//先要处理下name
			var field = $(this).attr('name');
			var checked = this.checked;
			if(!checked)return true;
			//分组
			if(checkboxItem[field]!=null){
				var checkboxV = checkboxItem[field];
				checkboxItem[field]=checkboxV+$(this).val()+',';
			}else{
				checkboxItem[field]=$(this).val()+',';
			}
		});
		for(var p in checkboxItem){
			item[p] = checkboxItem[p];
		}
		//处理radio
		var $radioArray = $obj.find(':radio:checked');
		$radioArray.each(function(){
			var name = $(this).attr('name');
			item[name] = $(this).val();
		});
		return item;
	},
	setStatusValue : function(ctId,obj){//设置状态值，一般是保存或提交后
		var $ct = $('#survey-'+ctId+'-ct');
		$ct.getCmp('id').val(obj.currentId);
		$ct.getCmp('submitStatus').val(obj.submitStatus);
		$ct.getCmp('auditStatus').val(obj.auditStatus);
	},
	change : function(ctId){//变更触发
		$('#btn-group-change-'+ctId).addClass('hidden');
		Survey.enabledComponent(ctId);
	},
	getHis:function(url,datagridId){
		$('#'+datagridId).datagrid('reload');
	},
	disabledComponent : function(ctId){//调查表组件失效
		$('#btn-group-save-'+ctId).addClass('hidden');
		$('.tb-btn-group-'+ctId).addClass('hidden');
		$('#survey-'+ctId+'-ct').find('input,select').attr('disabled',true);
		
	},
	changeComponent : function(ctId){//调查表变更组件控制
		$('#btn-group-change-'+ctId).removeClass('hidden');
	},
	enabledComponent : function(ctId){//调查表组件激活
		$('#btn-group-save-'+ctId).removeClass('hidden');
		$('.tb-btn-group-'+ctId).removeClass('hidden');
		$('#survey-'+ctId+'-ct').find('input,select').attr('disabled',false);
		$('#survey-'+ctId+'-ct').find('.easyui-combobox').each(function(){
			$(this).combobox('enable');
			
		});
		$('#survey-'+ctId+'-ct').find('.easyui-textbox').each(function(){
			$(this).textbox('enable');
			
		});
		$('#survey-'+ctId+'-ct').find('.easyui-datebox').each(function(){
			$(this).datebox('enable');
		});
		
	},
	initComponent : function(ctId){//根据状态初始化调查表的控制组件
		var $ct = $('#survey-'+ctId+'-ct');
		var submitStatus = $ct.getCmp('submitStatus').val();
		var auditStatus = $ct.getCmp('auditStatus').val();
		if(submitStatus==1)
		Survey.disabledComponent(ctId);
		if(submitStatus==1 && auditStatus!=0 && auditStatus!=1)
		Survey.changeComponent(ctId);
	},
	refreshHis : function(surveyCfgId,ctId,currentId){//刷新历史记录
		var $hisDatagrid = $('#survey-his-'+surveyCfgId);
		$hisDatagrid.datagrid('load',{
			ctId : ctId,
			currentId : currentId
		});
	}
		
		
}

$(function(){
	initStatus();
	initFormula();
	initFeeFormula();
	//监听无税单价和税费
    $('input.number').bind('keyup',function(event){
    $(this).val($(this).val().replace(/[^\d.]/g,''));
    $(this).val($(this).val().replace(/\.{2,}/g, '.'));
    }).bind('blur',function(){
	    if(!$.isNumeric($(this).val())){
		  $(this).focus();
		  //Msg.notifyWarning("非有效数字");
		}
    });
    //调查表列表的初始化
    $('#tree-survey').tree({
		onClick: function(node){
			if(node.attributes.surveyCode){
				toSurvey(node.attributes.surveyName,node.id);
			}
			
		}
	});
    
});

//调查表点击查看详情
function toSurvey(title,id){
	var $tabs = $('#tabs-survey');
	if($tabs.tabs('exists',title)){
		$tabs.tabs('select',title);
		return false;
	}
	var url = ctx+'/manager/vendor/admittance/getSurvey/'+id;
	$tabs.tabs('add',{
		title:title,
		href:url,
		closable:true,
	});
		
}



function initFeeFormula(){
	var $feeArray_fee = $('#feesum').find('input[feeFormula]');
	$feeArray_fee.each(function(i){
		var feeFormula = $(this).attr('feeFormula');
		var feeFormulaParam = feeFormula.match(/[A-Za-z]+/gi);
		for(var i=0;i<feeFormulaParam.length;i++){
			$('#'+feeFormulaParam[i]).on('blur',function(){
				if($(this).attr('pType')=='frm')
					feeFieldListen($(this));

			});
		}

	});
}

/**
 * 初始化公式的监听
 */
function initFormula(){
	var $formulaArray_tr = $('input[formula!=""][pType="tr"]');
	var $formulaArray_frm = $('input[formula!=""][pType="frm"]');
	//处理表单的公式监听
	$formulaArray_frm.each(function(i){
		var formula = $(this).attr('formula');
		var $ct = $(this).parentsUntil('div[name="fee"]').parent();
		//匹配字符
		var varNameArray = formula.match(/[A-Z]+/ig);
		for(var i=0;i<varNameArray.length;i++){
			var varField = $ct.find('[varName="'+varNameArray[i]+'"]').get(0);
			$(varField).on('blur',function(){
				formFieldListen($(this));
			});
		}
	});
	//处理表格的监听
	$formulaArray_tr.each(function(i){
		var formula = $(this).attr('formula');
		if(formula==null)return;
		var sum = $(this).attr('sum');
		var $tr = $(this).parentsUntil('tr').parent();//表格行
		//匹配字符
		var varNameArray = formula.match(/[A-Z]+/ig);
		//开始寻找变量
		for(var i=0;i<varNameArray.length;i++){
			var varField = $tr.find('[varName="'+varNameArray[i]+'"]').get(0);
			//监听该变量
			$(varField).on('blur',function(){
				tdFieldListen($(this));
			});
		}
	});
}

//表单字段监听
function formFieldListen(formField){
	var curVar = formField.attr('varName');
	var $ct = formField.parentsUntil('div[name="fee"]').parent();
	//获得容器下所有的公式
	var $ct_formulaArray = $ct.find('input[formula!=""][pType="frm"]');
	//循环公式，先判断公式中是否包含该变量
//	var $change_fieldArray = [];
//
//	$ct_formulaArray.each(function(i){
//		var formula = $(this).attr('quoteFormula');
//		var varNameArray = formula.match(/[A-Z]+/ig);
//		var isExist = false;
//		$.each(varNameArray,function(i){
//			var varName = this[0];
//			if(varName==curVar){
//				flag = true;
//				return false;
//			}
//		});
//		if(flag)$change_fieldArray.push($(this));
//	});
	$ct_formulaArray.each(function(i){
		var formula = $(this).attr('formula');
		var varNameArray = formula.match(/[A-Z]+/ig);
		var flag = true;
		for(var j=0;j<varNameArray.length;j++){
			var varName = varNameArray[j];
			var varField = $ct.find('input[varName="'+varName+'"]').get(0);
			var varVal = $(varField).val();
			if(varVal==''){
				flag = false;
				return false;
			}
			var reg=new RegExp(varName,'gmi');
			formula = formula.replace(reg,varVal);
		}
		if(!flag)return false;
		var sum = eval(formula);
		sum = sum.toFixed(4);
		//如果是hidden需要显示到label里面
		$(this).val(sum);
		if($(this).attr('type')=='hidden'){
			var labelName = $(this).attr('name')+'-label';
			$(this).parent().find('label[name="'+labelName+'"]').html(sum);
		}
		//校验必填项
		
		//需要显示小计
		var $feect = $(this).parentsUntil('div[name="fee"]').parent();
		var feeField = $feect.attr('feeField');
		var priceId  = $feect.attr('priceId');
		//TODO 要找小计
		var sumCt = $feect.find('#'+priceId).get(0);
		var sumField = $(sumCt).find('input[sum="true"]').get(0);
		var sum = $(sumField).val();
		$('#feesum').find('input[name="'+feeField+'"]').val(sum);
		$('#feesum').find('label[name="'+feeField+'-label"]').html(sum);
		//顶部的各种计算
		topFeeCount();
	});
}

//表单字段监听
function feeFieldListen(formField){
	//顶部的各种计算
	topFeeCount();
}

function tdFieldListen(tdField){
	//找到公式列
	var $tr = tdField.parentsUntil('tr').parent();
    //计算某个fee
	tbTrCount($tr);
}

function tbTrCount($tr){
	//获得该行所有的公式
	var $tr_formulaArray = $tr.find('input[formula!=""][pType="tr"]');
	$tr_formulaArray.each(function(i){
		var $formulaCol = $(this);
		var formula = $formulaCol.attr('formula');
		if(formula==null || formula=='')return true;
		//匹配字符
		var varNameArray = formula.match(/[A-Z]+/ig);
		var flag = true;
		for(var j=0;j<varNameArray.length;j++){
			var varName = varNameArray[j];
			var varField = $tr.find('input[varName="'+varName+'"]').get(0);
			var varVal = $(varField).val();
			if(varVal==''){
				flag = false;
				return false;
			}
			var reg=new RegExp(varName,'gmi');
			formula = formula.replace(reg,varVal);
			//改变改行的公式列
		};
		if(!flag)return false;
		var sum = eval(formula);
		sum = sum.toFixed(4);
		//如果是hidden需要显示到label里面
		$formulaCol.val(sum);
		if($formulaCol.attr('type')=='hidden'){
			var labelName = $formulaCol.attr('name')+'-label';
			$formulaCol.parent().find('label[name="'+labelName+'"]').html(sum);
		}
		//处理校验问题
		Survey.dealValidate($formulaCol);
		
		//开始处理小计问题,注意行的小计是指向tfoot中的sumId.
		//var sumId = $formulaCol.attr('sum');
		//if(sumId==null || sumId=='')return true;
		//去tfoot中寻找sumId
		//var $table = $formulaCol.parentsUntil('table').parent();
		//feeTotalCount_tb($table,sumId);
		//顶部的各种计算
		//topFeeCount();
	});
}

function feeTotalCount_tb($table,sumId){
	var $tfoot = $table.find('tfoot');
	var $sumId = $tfoot.find('input[sumId="'+sumId+'"]');
	//计算sumId的值
	var $sumArray = $table.find('input[sum="'+sumId+'"]');
	var sumValue = 0;
	$sumArray.each(function(i){
		if($(this).val()!=null && $(this).val()!=''){
			sumValue += parseFloat($(this).val());
		}
	});
	sumValue = sumValue.toFixed(4);
	$sumId.val(sumValue);
	//给label赋值
	var labelName = $sumId.attr('name')+'-label';
	$sumId.parent().find('label[name="'+labelName+'"]').html(sumValue);
	//顶部小计
	//需要显示小计
	var $feect = $table.parentsUntil('div[name="fee"]').parent();
	var feeField = $feect.attr('feeField');
	var priceId  = $feect.attr('priceId');
	//TODO 要找小计
	var sumTable = $feect.find('#'+priceId).get(0);
	var sumField = $(sumTable).find('input[isFee="true"]').get(0);
	var topSum = $(sumField).val();
	$('#feesum').find('input[name="'+feeField+'"]').val(topSum);
	$('#feesum').find('label[name="'+feeField+'-label"]').html(topSum);

}

function topFeeCount(){
  var $feeSum = $('#feesum');
  var $feeFormulaArray = $feeSum.find('input[feeFormula]');
  $feeFormulaArray.each(function(i){
	  var formula = $(this).attr('feeFormula');
	  formulaParamArray = formula.match(/[A-Za-z]+/ig);
	  for(var i=0;i<formulaParamArray.length;i++){
		  var formulaParam = formulaParamArray[i];
		  var paramValue = $('#'+formulaParam).val();
		  if(paramValue==''){
			  paramValue = 0;
		  }
		  var reg=new RegExp(formulaParam,'gmi');
		  formula = formula.replace(reg,paramValue);
	  };
	  var feeVal = eval(formula);
	  feeVal = feeVal.toFixed(4);
	  $(this).val(feeVal);
	  if($(this).attr('type')=='hidden'){
		  var labelName = $(this).attr('name')+'-label';
		  $(this).parent().find('label[name="'+labelName+'"]').html(feeVal);
	  }

  });
}

function addTbRow(tbId,cfgId){
	var $tb = $('#'+tbId);
	var $thead = $($tb.find('thead').get(0));
	var $thArray = $thead.find('th');
	var dateFlag = new Date().getTime();
	var tr = '<tr class="datagrid-row">';
	var rowCk = '<input name="rowCk" type="checkbox"/>';
	tr+='<td>'+rowCk+'</td>';
	$thArray.each(function(i){
		if(i==0)return true;
		var type = $(this).attr('thType');
		var required = $(this).attr("required")?'required="true"':'';
		var colName = $(this).attr('name');
		var sum = $(this).attr('sum');
		var formula = $(this).attr('formula');
		var varName = $(this).attr('varName');
		var tabId = $(this).attr('tabId');
		var url = $(this).attr('url');
		var dataSource = $(this).attr('dataSource');
		var hideInfo = '';
		//如果又是label又有计算
		if(type=='label' && formula!=null && formula!=''){
			hideInfo = '<input pType="tr" class="input-sm" style="width:98%!important" tabId="'+tabId+'" varName="'+varName+'" sum="'+sum+'" formula="'+formula+'" type="hidden" name="'+colName+'" required="'+required+'"/><label name="'+colName+'-label"></label>';
		}
		var ct = '';
		if(type=='label'){
			tr+='<td>'+hideInfo+'</td>';
		}else if(type=='day'){
			tr+='<td><span style="width:140px;height:22px;"  class="textbox"><input pType="tr" style="width:140px;line-height: normal;" class="Wdate textbox-text textbox-text-ex" onFocus="WdatePicker({isShowClear:false,readOnly:true})" tabId="'+tabId+'" varName="'+varName+'" sum="'+sum+'" formula="'+formula+'" type="text" name="'+colName+'" '+required+'/></span></td>';
		}else if(type=='file'){
			tr+='<td><span style="width:200px;height:22px;"  class="textbox"><input type="hidden" id="trFiles-'+colName+'-'+dateFlag+'-v" tabId="'+tabId+'" varName="'+varName+'" sum="'+sum+'" formula="'+formula+'"  name="'+colName+'" /><input type="'+type+'" pType="tr" onchange="setTrFileV(this)" vid="trFiles-'+colName+'-'+dateFlag+'-v" name="trFiles" style="height:22px;" '+required+'/></span></td>';
		}else if(type=='select'){//数据库下拉类型
		    tr+='<td><span style="width:140px;height:22px;"  class="textbox combo"><select style="width:140px;line-height: normal;"  class="textbox-text" onfocus="Survey.selectOnClick(this)" url="'+url+'" tabId="'+tabId+'" varName="'+varName+'" sum="'+sum+'" formula="'+formula+'" type="'+type+'" name="'+colName+'" '+required+'></select></span></td>';
		}else if(type=='selectrel'){//引用下拉类型
			tr+='<td><span style="width:140px;height:22px;"  class="textbox combo"><select style="width:140px;line-height: normal;"  class="textbox-text" onfocus="Survey.selectRelOnClick(this)" url="'+url+'" tabId="'+tabId+'" varName="'+varName+'" sum="'+sum+'" formula="'+formula+'" type="'+type+'" name="'+colName+'" '+required+'></select></span></td>';
		}else{
			tr+='<td><span style="width:140px;height:22px;"  class="textbox"><input pType="tr" style="width:140px;" class="textbox-text"  tabId="'+tabId+'" varName="'+varName+'" sum="'+sum+'" formula="'+formula+'" type="'+type+'" name="'+colName+'" '+required+'/></span></td>';
		}

	})
	tr+='</tr>';
	var $tbody = $($tb.find('tbody').get(0));
	$tbody.append(tr);
	Survey.initValidate('survey-'+cfgId+'-ct');
	initFormula();
	$.parser.parse($('#survey-'+cfgId+'-ct').parent());
}

function delTbRow(tbId){
	var $tb = $('#'+tbId);
	var $tbody = $($tb.find('tbody').get(0));
	var $checkedBoxArray = $tbody.find('input[name="rowCk"]:checked');
	if($checkedBoxArray == null || $checkedBoxArray.length<=0){
		$.messager.alert('提示','没有选择记录','warning');
		return false;
	}
	$('#'+tbId+'-alert').addClass('hidden');
	$checkedBoxArray.each(function(i){
		$(this).parent().parent('tr').remove();
	});
	//找到总计行
	var $sum = $tb.find('tfoot').find('input[sumId][isFee="true"]');
	if($sum!=null){
	feeTotalCount_tb($tb,$sum.attr('sumId'));
	topFeeCount();
	}
}



function surveyValidation(ctIdPre){
	var $ct = $('#'+ctIdPre+'-ct');
	var flag = true;
	var $inputArray = $ct.find('input[required="true"]');
	var $selectArray = $ct.find('select[required="true"]');
	var $checkbox = {};
	$inputArray.each(function(i){
		//校验了text
		if($(this).val()==null || $(this).val()==''){
			//获得必填项名称
			//var tabId = $(this).attr('tabId');
//			Msg.notifyWarning($('#'+tabId).attr('title')+"必填项不能为空");
//			$('#tabs').tabs('option','active',parseInt($(this).attr('tabId').split('-')[1]+1));
			$(this).focus();
			flag = false;
			return false;
		}
		//对checkbox分组
		if($(this).attr('type')=='checkbox'){
			var name = $(this).attr('name');
			var checked = this.checked;
			if($checkbox[name]!=null){
				if(checked)
				$checkbox[name].push($(this).val());
			}else{
				$checkbox[name] = [];
				if(checked)
				$checkbox[name].push($(this).val());
			}

		}

	});
	
	$selectArray.each(function(i){
		//下拉选择
		if($(this).attr('type')=='select'||$(this).attr('type')=='text'){
			var name = $(this).attr('name');
			var sel = $(this).val();
			if(sel==null){
				$(this).focus();
				flag = false;
				return false;
			}

		}
	});

	//校验checkbox
	for(var box in $checkbox){
		if($checkbox[box].length<=0){
//			Msg.notifyWarning("有checkbox为必选");
			flag = false;
			return false;
		}
	}

	return flag;
}


//文件发生改变时给当前列赋值，以便后台对应使用
function setTrFileV(file){
	var fileName = $(file).val();
	var fileVId = $(file).attr('vid');
	var $fileV = $('#'+fileVId);
	var fileNameTypeArray = fileName.split('.');
	if(fileNameTypeArray.length<=1){
		$.messager.alert('提示','非有效文件！','warning');
		$(file).get(0).reset();
		$fileV.val('');
	}
	fileName = fileName.replace(/\\/ig,'/');
	var fileNameArray = fileName.split('/');
	$fileV.val(fileNameArray[fileNameArray.length-1]);
}

function validateAttachmentSize(ctId){
	var flag = true;
	var sizeLimit = $('#attachmentSize').val();
	if(sizeLimit==null || sizeLimit=='0')return flag;
	var $fileArray = $('#'+ctId).find('input[type="file"]');
	$fileArray.each(function(){
		if($(this)[0].files==null || $(this)[0].files.length<=0)return true;
		var size = $(this)[0].files[0].size;
		var sizeM = size/1024/1024;
		if(sizeM>sizeLimit){
			$.messager.alert('提示','附件大小不能大于'+sizeLimit+'M','warning');
			$(this).focus();
			flag = false;
			return false;
		}
	});
	return flag;
	
}

//保存或提交时校验邓白氏编码、网址、联系电话、年份
function validateInfo(){
	//邓白氏编码
	var dunsValue = $('#dunsValue').textbox('getValue');
	if(dunsValue != ""){
		var reg = /^[0-9]+.?[0-9]*$/;	//是否是数字
		if(reg.test(dunsValue) && dunsValue.length == 9 ){
			//return true;
		}else{
			$.messager.alert('提示','邓白氏编码为9位数字，请填写正确的编码','warning');
			return false;
		}
		
	}
	//网址
	var webAddrValue = $('#webAddrValue').textbox('getValue');
	var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
	var objExp=new RegExp(Expression); 
	if(webAddrValue != ""){
	    if (objExp.test(webAddrValue)){
	        //return true;
	    }else{
	    	$.messager.alert('提示','请填写正确的网址','warning');
			return false;
	    }
	}
	//联系电话
	var num1 = $('#num1').textbox('getValue');
	var num2 = $('#num2').textbox('getValue');
	var num3 = $('#num3').textbox('getValue');
	var num4 = $('#num4').textbox('getValue');
	//验证手机号
	 var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
	 //验证固定号码
	 var re = /^0\d{2,3}-?\d{7,8}$/;
	 if(num1 != ""){
		 var temp1 = reg.test(num1);
		 var temp2 = re.test(num1)
		 if(temp1 || temp2){
			// return true;
		 }else{
			 $.messager.alert('提示','请填写正确的联系方式','warning');
			 return false;
		 } 
	 }
	 
	 if(num2 != ""){
		 var temp1 = reg.test(num2);
		 var temp2 = re.test(num2)
		 if(temp1 || temp2){
			// return true;
		 }else{
			 $.messager.alert('提示','请填写正确的联系方式','warning');
			 return false;
		 } 
	 }
	 
	 if(num3 != ""){
		 var temp1 = reg.test(num3);
		 var temp2 = re.test(num3)
		 if(temp1 || temp2){
			// return true;
		 }else{
			 $.messager.alert('提示','请填写正确的联系方式','warning');
			 return false;
		 } 
	 }
	 
	 if(num4 != ""){
		 var temp1 = reg.test(num4);
		 var temp2 = re.test(num4)
		 if(temp1 || temp2){
			 //return true;
		 }else{
			 $.messager.alert('提示','请填写正确的联系方式','warning');
			 return false;
		 } 
	 }

	//年份
	 var startTime =$('#startTime').datebox('getValue');
	 var endTime =$('#endTime').datebox('getValue');
	 if(startTime !="" && endTime !="" && startTime>endTime){
		 $.messager.alert('提示','进入年份不能大于离开年份','warning');
		 return false;
	 }
	 
	 return true;
}

