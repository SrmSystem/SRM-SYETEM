/**
 * datagrid扩展可编辑
 */
$.extend($.fn.datagrid.methods, {
		editCell: function(jq,param){
			return jq.each(function(){
				var opts = $(this).datagrid('options');
				var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
				for(var i=0; i<fields.length; i++){
					var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor1 = col.editor;
					if (fields[i] != param.field){
						col.editor = null;
				}
				}
				$(this).datagrid('beginEdit', param.index);
				for(var i=0; i<fields.length; i++){
					var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
		});
	}
});

$.extend($.fn.datagrid.defaults.editors, {
	datetimebox : {
		init : function(container, options) {
			var input = $('<input type="text" class="easyui-datetimebox">').appendTo(container);
			//编辑框延迟加载
			window.setTimeout(function() {
				input.datetimebox($.extend({
					editable : false
				}, options));
			}, 10);
			return input;
		},
		getValue : function(target) {
			return $(target).datetimebox('getValue');
		},
		setValue : function(target, value) {
			$(target).val(value);
			window.setTimeout(function() {
				$(target).datetimebox('setValue', value);
			}, 150);
		},
		resize : function(target, width) {
			var input = $(target);
			if ($.boxModel == true) {
				input.width(width - (input.outerWidth() - input.width()));
			} else {
				input.width(width);
			}
		}
	}
});