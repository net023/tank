package com.ych.web.controller.backEnd;

import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.model.CustomServiceModel;

@Control(controllerKey="/customService")
public class CustomServiceController extends BaseController {
	private static Logger LOG = Logger.getLogger(CustomServiceController.class);
	
	public void index(){
		render("customService/customService_index");
	}
	
	public void list(){
		Pager pager = createPager();
		Page<?> page = CustomServiceModel.dao.getPager(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	
	@Before(Tx.class)
	public void add() {
		Map<String, Object> result = getResultMap();
		try {
			CustomServiceModel app = getModelWithOutModelName(CustomServiceModel.class, true);
			Integer id = app.getInt("id");
			if(id==null){
				app.save();
			}else{
				app.update();
			}
			result.put(RESULT, true);
			result.put(MESSAGE, "客服添加成功！");
		} catch (Exception e) {
			LOG.debug("客服添加失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "客服添加失败！");
		}
		renderJson(result);
	}
	
	public void delete(){
		Map<String, Object> result = getResultMap();
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = CustomServiceModel.dao.deleteById(id);
			if (deleteOK) {
				result.put(RESULT, true);
				result.put(MESSAGE, "客服删除成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "客服删除失败！");
			}
		} catch (Exception e) {
			LOG.debug("客服删除失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "客服删除失败！");
		}
		renderJson(result);
	}
	
}
