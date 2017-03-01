$(function() {
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
        selectOnCheck:false
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
	}
};