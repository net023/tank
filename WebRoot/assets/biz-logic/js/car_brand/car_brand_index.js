$(function() {
	$('#queryButton').click(function(){
        var params = $('#queryForm')._formToJson();
        $(grid).datagrid('load',params);
    });
	
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
        selectOnCheck:false
	});
	
	$("#logo").fileupload({
		 url:'file/upload',
		 dataType: 'json',
		 done:function(e,result){  
			 if(result.result.r){
				 
				//对car_brand进行update
				var fid = result.result.fID;
				
				$._ajaxPost("car_brand/setPid",{i:id,f:fid},function(r){
					if(r.r){
						$("#grid").datagrid('reload');
						//关闭upDialog
						$('#upDialog').dialog('close');
						$.messager.alert("上传完成",r.m);
					}else{
						$.messager.alert('上传失败',r.m,'error');
					}
				});
				
			}else{
				$.messager.alert("上传失败",result.result.m);
			}
	    },
	    fail:function(){
	    	$.messager.alert("错误提示","系统异常请联系管理员。");
	    }
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
		
		return states && ('<a class="spacing a-'+color+'" onclick="modifyStatus('+rowData.id+','+toStates+');" href="javascript:void(0);">'+states+'</a>');
	},
	hot:function(value,rowData,rowIndex){
		var states = {
				0:'否',
				1:'是'
		}[rowData.hot];
		var toStates = {
				0:1,
				1:0
		}[rowData.hot];
		var color = {
				0:'green',
				1:'red'
		}[rowData.hot];
		
		return states && ('<a class="spacing a-'+color+'" onclick="modifyHot('+rowData.id+','+toStates+');" href="javascript:void(0);">'+states+'</a>');
	},
	logo:function(value,rowData,rowIndex){
		var html = '';
		if(value){
			html = '<span><image src="file/download?fID='+value+'" style="width:50px;height:50px" onclick="show('+rowIndex+');"/></span>';
		}
		return html;
	},
	up:function(value,rowData,rowIndex){
		var html = '<button onclick="up('+rowIndex+')">上传</button>';
		return html;
	}
};


var id = null;
function up(rowIndex){
	var data = $('#grid').datagrid('getRows')[rowIndex];
	id = data.id;
	$('#upDialog').dialog({closed:false,title:'上传图片'});
}


function modifyStatus(i,s){
	$._ajaxPost("car_brand/modifyStatus",{id:i,state:s},function(r){
		if(r.r){
			$("#grid").datagrid('reload');
		}else{
			$.messager.alert('操作失败',r.m,'error');
		}
	});
}
function modifyHot(i,s){
	$._ajaxPost("car_brand/modifyHot",{id:i,hot:s},function(r){
		if(r.r){
			$("#grid").datagrid('reload');
		}else{
			$.messager.alert('操作失败',r.m,'error');
		}
	});
}

function show(rowIndex){
	//通过fID进行ajax查询图片文件在服务器上的路径进行访问
	var data = $('#grid').datagrid('getRows')[rowIndex];
	$("#imgUrl").attr("src","file/download?fID="+data.p_id);
//	$('#imgShowDialog').dialog('open').dialog('setTitle','图片显示');
	$('#imgShowDialog').dialog({closed:false,title:'图片显示'});
}
