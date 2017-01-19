package com.ych.web.controller.backEnd;

import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.model.OemModel;

@Control(controllerKey="/oem")
public class OemTitleController extends BaseController {
	private static Logger LOG = Logger.getLogger(OemTitleController.class);
	
	public void index(){
		render("oem_title/oem_index");
	}
	
	public void list(){
		Pager pager = createPager();
		Page<?> page = OemModel.dao.getPager(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	
	@Before(Tx.class)
	public void add() {
		Map<String, Object> result = getResultMap();
		try {
			OemModel app = getModelWithOutModelName(OemModel.class, true);
			Integer id = app.getInt("id");
			if(id==null){
				app.save();
			}else{
				app.update();
			}
			result.put(RESULT, true);
			result.put(MESSAGE, "标题添加成功！");
		} catch (Exception e) {
			LOG.debug("标题添加失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "标题添加失败！");
		}
		renderJson(result);
	}
	
	public void delete(){
		Map<String, Object> result = getResultMap();
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = OemModel.dao.deleteById(id);
			if (deleteOK) {
				result.put(RESULT, true);
				result.put(MESSAGE, "标题删除成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "标题删除失败！");
			}
		} catch (Exception e) {
			LOG.debug("标题删除失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "标题删除失败！");
		}
		renderJson(result);
	}
	
}
