$(function() {
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
        selectOnCheck:false,
        toolbar: [{
            text : '添加用户',
            iconCls : 'icon-add',
            handler : function(){
            	$('#userDialog').dialog('open').dialog('setTitle','添加用户');
            }
        }],
        queryParams:$('#queryForm')._formToJson()
	});
	
	$('#userDialog').dialog({
		onBeforeOpen:function(){
			$("#userEditForm").resetForm();
			$("#id").val('');
		},
		buttons:[
		         {id:'saveb',text:'保存',handler:function(){
		        	 if(!$("#userEditForm").form("validate")){
							return;
						}else{
							$("#userEditForm")._ajaxForm(function(r){
								if(r.r){
									$("#userDialog").dialog("close");
									$("#grid").datagrid("reload");
								}else{
									$.messager.alert("操作提示",r.m,"error");
								}
							});
						}
		         }},
		         {text:'关闭',handler:function(){
		        	 	$('#userDialog').dialog('close');
		         }}
		        ]
	});
	
    $('#queryButton').click(function(){
    	debugger;
    	if($("#startDate").val() > $("#endDate").val()){
    		$.messager.alert('非法操作',"开始时间不能大于结束时间",'warning');
    		return;
    	}
        var params = $('#queryForm')._formToJson();
        $(grid).datagrid('load',params);
    });
});

var formatter = {
	 status:function(value,rowData,rowIndex){
		var states = {
				0:'已启用',
				1:'已禁用'
		}[rowData.status];
		var toStates = {
				0:1,
				1:0
		}[rowData.status];
		var color = {
				0:'green',
				1:'red'
		}[rowData.status];
		
		return states && ('<a class="spacing a-'+color+'" onclick="modify('+rowData.id+','+toStates+');" href="javascript:void(0);">'+states+'</a>');
	},
	opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="update('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-red" onclick="delete_('+rowIndex+');" href="javascript:void(0);">删除</a>';
        return html;
    }
 }

function update(rowIndex){
    var data = $('#grid').datagrid('getRows')[rowIndex];
    $('#userDialog').dialog('open').dialog('setTitle','修改用户');
    $('#userEditForm')._jsonToForm(data);
}

function delete_(rowIndex) {
	$.messager.confirm('操作提示', '确定要删除所选？', function(r){
        if (r){
        	var data = $('#grid').datagrid('getRows')[rowIndex];
        	$._ajaxPost("wx_user/delete",{id:data.id},function(r){
        		if(r.r){
        			$("#grid").datagrid('reload');
        		}else{
        			$.messager.alert('操作失败',r.m,'error');
        		}
        	});
        }
    });
}

function modify(i,s){
	$._ajaxPost("wx_user/modify",{id:i,state:s},function(r){
		if(r.r){
			$("#grid").datagrid('load');
		}else{
			$.messager.alert('操作失败',r.m,'error');
		}
	});
}