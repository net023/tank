package com.ych.web.controller.backEnd;

import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.model.AppMgrModel;

@Control(controllerKey="/appmgr")
public class AppMgrController extends BaseController {
	private static Logger LOG = Logger.getLogger(AppMgrController.class);
	
	public void index(){
		render("appmgr/appmgr_index");
	}
	
	public void list(){
		Pager pager = createPager();
		Page<?> page = AppMgrModel.dao.getPager(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	
	@Before(Tx.class)
	public void add() {
		Map<String, Object> result = getResultMap();
		try {
			AppMgrModel app = getModelWithOutModelName(AppMgrModel.class, true);
			Integer id = app.getInt("id");
			if(id==null){
				app.save();
			}else{
				app.update();
			}
			result.put(RESULT, true);
			result.put(MESSAGE, "app版本添加成功！");
		} catch (Exception e) {
			LOG.debug("app版本添加失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "app版本添加失败！");
		}
		renderJson(result);
	}
	
	public void modify(){
		Integer id = getParaToInt("id");
		Integer status = getParaToInt("state");
		boolean b = AppMgrModel.dao.modify(status,id);
		Map<String, Object> result = getResultMap();
		result.put(RESULT, b);
		result.put(MESSAGE, b ? "app版本状态保存成功" : "app版本状态保存失败，请联系管理员！");
		renderJson(result);
	}
	
	public void delete(){
		Map<String, Object> result = getResultMap();
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = AppMgrModel.dao.deleteById(id);
			if (deleteOK) {
				result.put(RESULT, true);
				result.put(MESSAGE, "app版本删除成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "app版本删除失败！");
			}
		} catch (Exception e) {
			LOG.debug("app版本删除失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "app版本删除失败！");
		}
		renderJson(result);
	}
	
}
