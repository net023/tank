$(function() {
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
		selectOnCheck:false,
		toolbar: [{
            text : '添加项目',
            iconCls : 'icon-add',
            handler : function(){
            	$('#ProjectDialog').dialog('open').dialog('setTitle','添加项目');
            }
        }]
	});
	
	$('#ProjectDialog').dialog({
		onBeforeOpen:function(){
			$("#ProjectEditForm").resetForm();
			$("#id").val('');
		},
		buttons:[
		         {id:'saveb',text:'保存',handler:function(){
		        	 if(!$("#ProjectEditForm").form("validate")){
							return;
						}else{
							$("#ProjectEditForm")._ajaxForm(function(r){
								if(r.r){
									$("#ProjectDialog").dialog("close");
									$("#grid").datagrid("reload");
								}else{
									$.messager.alert("操作提示",r.m,"error");
								}
							});
						}
		         }},
		         {text:'关闭',handler:function(){
		        	 	$('#ProjectDialog').dialog('close');
		         }}
		        ]
	});
	
});

var formatter = {
    opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="update('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-red" onclick="delete_('+rowIndex+');" href="javascript:void(0);">删除</a>';
        return html;
    }
};

function update(rowIndex){
    var data = $('#grid').datagrid('getRows')[rowIndex];
    $('#ProjectDialog').dialog('open').dialog('setTitle','修改项目');
    $('#ProjectEditForm')._jsonToForm(data);
//    $("#tankType").combo("setText","aaaa");
}

function delete_(rowIndex) {
	$.messager.confirm('操作提示', '确定要删除所选？', function(r){
        if (r){
        	var data = $('#grid').datagrid('getRows')[rowIndex];
        	$._ajaxPost("project/delete",{id:data.id},function(r){
        		if(r.r){
        			$("#grid").datagrid('reload');
        		}else{
        			$.messager.alert('操作失败',r.m,'error');
        		}
        	});
        }
    });
}
