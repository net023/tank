$(function() {
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
		selectOnCheck:false,
		toolbar: [{
            text : '新建app版本',
            iconCls : 'icon-add',
            handler : function(){
            	$("#appEditForm").resetForm();
            	$('#id').val('');
            	fileUp = false;
            	$('#appDialog').dialog('open').dialog('setTitle','新建app版本');
            }
        }]
	});
	
	$('#appDialog').dialog({
		onBeforeOpen:function(){
			$("#appEditForm").resetForm();
			$("#id").val('');
		},
		buttons:[
		         {id:'saveb',text:'保存',handler:function(){
		        	 if(!$("#appEditForm").form("validate")){
							return;
						}else{
							$("#appEditForm")._ajaxForm(function(r){
								if(r.r){
									$("#appDialog").dialog("close");
									$("#grid").datagrid("reload");
								}else{
									$.messager.alert("操作提示",r.m,"error");
								}
							});
						}
		         }},
		         {text:'关闭',handler:function(){
		        	 	$('#appDialog').dialog('close');
		         }}
		        ]
	});
	
});

var formatter = {
	status:function(value,rowData,rowIndex){
		var states = rowData.status?"已启用":"已禁用";
		var toStates = rowData.status?0:1;
		var color = rowData.status?"blue":"red";
		
		return states && ('<a class="spacing a-'+color+'" onclick="modify('+rowData.id+','+toStates+');" href="javascript:void(0);">'+states+'</a>');
	},
    opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="update('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-red" onclick="delete_('+rowIndex+');" href="javascript:void(0);">删除</a>';
            html+= '<a class="spacing a-blue" onclick="show('+rowIndex+');" href="javascript:void(0);">查看</a>';
        return html;
    },
    updateInfo:function(value, rowData, rowIndex){
    	if(value && value.length>15){
    		return value.substr(0,15)+"...";
    	}else if(value){
    		return value;
    	}
    },
    apk:function(value, rowData, rowIndex){
    	if(value && value.length>15){
    		return value.substr(0,15)+"...";
    	}else if(value){
    		return value;
    	}
    },
    ipa:function(value, rowData, rowIndex){
    	if(value && value.length>15){
    		return value.substr(0,15)+"...";
    	}else if(value){
    		return value;
    	}
    }
};

function update(rowIndex){
    var data = $('#grid').datagrid('getRows')[rowIndex];
    console.info(data);
    $('#appDialog').dialog('open').dialog('setTitle','修改app版本');
    $('#appEditForm')._jsonToForm(data);
    $("#create_time").val(data.create_time);
}

function delete_(rowIndex) {
	$.messager.confirm('操作提示', '确定要删除所选？', function(r){
        if (r){
        	var data = $('#grid').datagrid('getRows')[rowIndex];
        	$._ajaxPost("appmgr/delete",{id:data.id},function(r){
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
	$._ajaxPost("appmgr/modify",{id:i,state:s},function(r){
		if(r.r){
			$("#grid").datagrid('reload');
		}else{
			$.messager.alert('操作失败',r.m,'error');
		}
	});
}


function show(rowIndex){
	var data = $('#grid').datagrid('getRows')[rowIndex];
	$("#info_version").text(data.version);
	$("#info_order").text(data.order);
	$("#info_create_time").text(data.create_time);
	$("#info_apk_url").text(data.apk_url);
	$("#info_ipa_url").text(data.ipa_url);
	$("#info_update_info").val(data.update_info);
	$('#infoDialog').dialog({closed:false,title:'App版本信息'});
}