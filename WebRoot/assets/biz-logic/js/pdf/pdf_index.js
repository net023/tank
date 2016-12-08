$(function() {
	
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
        selectOnCheck:false,
        toolbar: [{
            text : '新建pdf',
            iconCls : 'icon-add',
            handler : function(){
            	$("#pdfEditForm").resetForm();
            	$('#id').val('');
            	$('#pdfFile').val('');
            	$("#si").text('');
            	fileUp = false;
            	$('#pdfDialog').dialog('open').dialog('setTitle','新建pdf');
            }
        }]
	});
	
	$('#queryButton').click(function(){
        var params = $('#queryForm')._formToJson();
        $(grid).datagrid('load',params);
    });
	
	/*$("#batchup").fileupload({
		url:'filter4/batchup',
		dataType: 'json',
		done:function(e,result){
			if(result.result.r){
				$("#grid").datagrid('reload');
				$.messager.alert("上传完成",result.result.m);
			}else{
				$.messager.alert("上传失败",result.result.m);
			}
		},
		fail:function(){
			$.messager.alert("错误提示","系统异常请联系管理员。");
		}
	});*/
	
	var ii;
	$("#pdfFile").fileupload({
		url:'pdf/add',
		dataType:'json',
		acceptFileTypes:/(\.|\/)(pdf)$/i,
		maxNumberOfFiles : 1,
//		formData:{id:$("#id").val(),pdfName:$("#pdfName").val()},
		autoUpload:false,
		add:function(e,data){
			$("#si").text("已经选择文件:"+data.files[0].name);
			$("#saveb").click(function(){
				ii = layer.load();
				data.formData = {id:$("#id").val(),pdfName:$("#pdf_name").val()};
				data.submit();
			}); 
			fileUp = true;
		},
		submit:function(e,data){
			fileUp = true;
		},
		done:function(e,result){
			layer.close(ii);
			if(result.result.r){
				$('#pdfDialog').dialog('close');
//				$("#grid").datagrid('reload');
				window.location.reload();
				$.messager.alert("上传完成",result.result.m);
			}else{
				$.messager.alert("上传失败",result.result.m);
			}
		},
		fail:function(){
			layer.close(ii);
			$.messager.alert("错误提示","系统异常请联系管理员。");
		}
	});
	
	$('#pdfDialog').dialog({
		onBeforeOpen:function(){
//			var file = $("#pdfFile") 
//			file.after(file.clone().val("")); 
//			file.remove(); 
//			var obj = document.getElementById('pdfFile') ;  
//			obj.outerHTML=obj.outerHTML; 
		},
		buttons:[
		         {id:'saveb',text:'保存',handler:function(){
		        	 	console.info(111);
		        	 	var idV = $("#id").val();
		        	 	if(!fileUp && (idV!="")){
		        	 		$.ajax({
		        				dataType:'json',
		        				url:"pdf/add",
		        				data:{id:$("#id").val(),pdfName:$("#pdf_name").val()},
		        				cache:false,
		        				type:"post",
		        				success:function(r){
			        	 			if(r.r){
			        	 				$('#pdfDialog').dialog('close');
//			        	 				$("#grid").datagrid('reload');
			        	 				window.location.reload();
			        	 			}else{
			        	 				$.messager.alert('操作失败',r.m,'error');
			        	 			}
			        	 		}
		        			});
		        	 	}
		        	 }
		         },
		         {text:'关闭',handler:function(){
		        	 	$('#pdfDialog').dialog('close');
		         	}
		         }
		        ]
	});
	
	/*$("#drzsgsl").click(function(){
		$('#changeDialog').dialog('open').dialog('setTitle','导入增删改四滤库excel');
	});*/
	
	
	
	/*$("#xzslb").click(function(){
		//$._ajaxPost("file/df",{fn:"四滤价格表--模版.xlsx"});
		var fileName = encodeURIComponent(encodeURIComponent("四滤价格表--模版.xlsx"));
		window.open("file/df?fn="+fileName);
	});
	
	//--------修改价格
	$('#priceDialog').dialog({
		buttons:[{text:'保存',handler:function(){
			if(!$('#priceForm').form('validate')){return;}
			$('#priceForm')._ajaxForm(function(r){
				if(r.r){
					$('#priceDialog').dialog('close');
					$("#priceMgGrid").datagrid('reload');
					$.messager.alert('操作提示', r.m,'info');
				}else{$.messager.alert('操作提示', r.m,'error');}
			});
			
		}},{text:'关闭',handler:function(){$('#priceDialog').dialog('close');}}]
	});*/
    
});

var formatter = {
    opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="update('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-red" onclick="delete_('+rowIndex+');" href="javascript:void(0);">删除</a>';
            html+= '<a class="spacing a-blue" onclick="show('+rowIndex+');" href="javascript:void(0);">查看</a>';
            html+= '<a class="spacing a-red" onclick="download_('+rowIndex+');" href="javascript:void(0);">下載</a>';
        return html;
    }
};

function update(rowIndex) {
    $('#pdfEditForm').resetForm();
    var data = $('#grid').datagrid('getRows')[rowIndex];
    $('#pdfEditForm')._jsonToForm(data);
    $('#id').val(data.id);
    $("#si").text("已经选择文件:"+data.pdf_name);
    fileUp = false;
    $('#pdfDialog').dialog('open').dialog('setTitle','修改pdf信息');
}


function download_(rowIndex) {
	var data = $('#grid').datagrid('getRows')[rowIndex];
	window.open("pdf/download?id="+data.id);
}

function delete_(rowIndex) {
	$.messager.confirm('操作提示', '确定要删除所选？', function(r){
        if (r){
        	var data = $('#grid').datagrid('getRows')[rowIndex];
        	$._ajaxPost("pdf/delete",{id:data.id},function(r){
        		if(r.r){
        			$("#grid").datagrid('reload');
        		}else{
        			$.messager.alert('操作失败',r.m,'error');
        		}
        	});
        }
    });
}


function show(rowIndex) {
	var data = $('#grid').datagrid('getRows')[rowIndex];
	window.open("pdf/show?id="+data.id);
}