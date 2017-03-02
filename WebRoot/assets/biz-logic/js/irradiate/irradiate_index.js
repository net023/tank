$(function() {
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
        selectOnCheck:false
	});
	
	$('#irradiateDialog').dialog({
		onBeforeOpen:function(){
			$("#irradiateEditForm").resetForm();
			$("#id").val('');
		},
		buttons:[
		         {id:'saveb',text:'保存',handler:function(){
		        	 if(!$("#irradiateEditForm").form("validate")){
							return;
						}else{
							$("#irradiateEditForm")._ajaxForm(function(r){
								if(r.r){
									$("#irradiateDialog").dialog("close");
									$("#grid").datagrid("reload");
								}else{
									$.messager.alert("操作提示",r.m,"error");
								}
							});
						}
		         }},
		         {text:'关闭',handler:function(){
		        	 	$('#irradiateDialog').dialog('close');
		         }}
		        ]
	});
	
	$('#queryButton').click(function(){
        var params = $('#queryForm')._formToJson();
        $(grid).datagrid('load',params);
    });
	
	var ii;
	$("#excelFile").fileupload({
		url:'irradiate/importExcel',
		dataType: 'json',
		done:function(e,result){
			layer.close(ii);
			if(result.result.r){
				$.messager.alert("上传完成",result.result.m);
				$("#grid").datagrid('reload');
			}else{
				$.messager.alert("上传失败",result.result.m);
			}
		},
		fail:function(){
			layer.close(ii);
			$.messager.alert("错误提示","系统异常请联系管理员。");
		},
		add: function (e, data) {
		   ii = layer.load();
		   data.submit();
        }
	});
	
    $("#downloadTemplate").click(function(){
    	window.open("irradiate/download");
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
	recmd:function(value,rowData,rowIndex){
		var states = {
				0:'已推荐',
				1:'未推荐'
		}[rowData.recmd];
		var toStates = {
				0:1,
				1:0
		}[rowData.recmd];
		var color = {
				0:'green',
				1:'red'
		}[rowData.recmd];
		
		return states && ('<a class="spacing a-'+color+'" onclick="recmd('+rowData.id+','+toStates+');" href="javascript:void(0);">'+states+'</a>');
	},
	editPrice:function(value,rowData,rowIndex){
		return '<a class="spacing a-blue" onclick="editPrice('+rowIndex+');" href="javascript:void(0);">修改价格</a>';
	},
	opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="update('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-red" onclick="delete_('+rowIndex+');" href="javascript:void(0);">删除</a>';
        return html;
    }
};

function update(rowIndex){
    var data = $('#grid').datagrid('getRows')[rowIndex];
    $('#irradiateDialog').dialog('open').dialog('setTitle','修改');
    $('#irradiateEditForm')._jsonToForm(data);
}

function delete_(rowIndex) {
	$.messager.confirm('操作提示', '确定要删除所选？', function(r){
        if (r){
        	var data = $('#grid').datagrid('getRows')[rowIndex];
        	$._ajaxPost("irradiate/delete",{id:data.id},function(r){
        		if(r.r){
        			$("#grid").datagrid('reload');
        		}else{
        			$.messager.alert('操作失败',r.m,'error');
        		}
        	});
        }
    });
}