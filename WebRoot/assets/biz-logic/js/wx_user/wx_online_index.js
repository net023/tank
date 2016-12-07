function loadData(){
	$._ajaxPost("onlineList",null,function(r){
		if(r.total!=0){
			$("#grid").datagrid('loadData',r);
		}
	});
}

$(function() {
	var grid = $('#grid')._datagrid({
		checkOnSelect:false,
		selectOnCheck:false
	});
	var p = $(grid).datagrid("getPager");
	$(p).pagination({  
		onSelectPage:function(pageNumber, pageSize){
			loadData();
		}  
    });  
	loadData();
});