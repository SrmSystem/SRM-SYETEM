//datagrid 列数据
$('#acc').datagrid({
columns : [ [
    {
    field : 'fee_lend',
    title : '收费A',
    width : 100,
    editor : "numberbox"
}, {
    field : 'fee_loan',
    title : '收费B',
    width : 100,
    editor : "numberbox"
}, 
] ]
})
//编辑费用大于零则另一方赋值为空
onBeginEdit : function(rowIndex, rowData){
    var editors = $('#acc').datagrid('getEditors', rowIndex);     
    var lendEditor = editors[0];
    var loadEditor = editors[1];
    //target属性就用于返回最初触发事件的DOM元素                
    lendEditor.target.numberbox({
    　　onChange:function(newValue,oldValue){
       　　if(newValue > 0){                            
           　　loadEditor.target.numberbox('setValue', null);
          }
     　　}
    });
        
    loadEditor.target.numberbox({
    onChange:function(newValue,oldValue){
    　　if(newValue > 0){    
        　　lendEditor.target.numberbox('setValue', null);
        }
      }
    });
}