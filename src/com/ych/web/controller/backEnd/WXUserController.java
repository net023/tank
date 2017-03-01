package com.ych.web.controller.backEnd;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.tools.DateTools;
import com.ych.web.listenner.SessionListenner;
import com.ych.web.model.ProjectModel;
import com.ych.web.model.WXUserModel;

@Control(controllerKey="/wx_user")
public class WXUserController extends BaseController {
	private static Logger LOG = Logger.getLogger(WXUserController.class);
	
	public void index(){
		Calendar c = DateTools.getNowCalendar();
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 7);
		setAttr("sDate", DateTools.formatDate(c.getTime()));
		setAttr("eDate" , DateTools.getDate(new Date()));
		render("wx_user/wx_user_index");
	}
	
	public void onlineUserIndex(){
		render("wx_user/wx_online_index");
	}
	
	public void list(){
		Pager pager = createPager();
		if (getPara("startDate") != null && !"".equals(getPara("startDate"))) {
			pager.addParam("startDate", getPara("startDate"));
		}
		if (getPara("endDate") != null && !"".equals(getPara("endDate"))) {
			pager.addParam("endDate", getPara("endDate"));
		}
		if (getPara("userName") != null && !"".equals(getPara("userName"))) {
			pager.addParam("userName", getPara("userName"));
		}
		Page<?> page = WXUserModel.dao.getPager(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	public void onlineList(){
		List<WXUserModel> sessionWXUsers = SessionListenner.getSessionWXUsers();
		setAttr("total", sessionWXUsers.size());
		setAttr("rows", sessionWXUsers);
		renderJson();
	}
	
	@Before(Tx.class)
	public void modify(){
		Integer id = getParaToInt("id");
		Integer status = getParaToInt("state");
		boolean b = WXUserModel.dao.modify(id, status);
		Map<String, Object> result = getResultMap();
		result.put(RESULT, b);
		result.put(MESSAGE, b ? "保存成功" : "保存失败，请联系管理员！");
		renderJson(result);
	}
	
	@Before(Tx.class)
	public void add() {
		Map<String, Object> result = getResultMap();
		try {
			String userName = getPara("name");
			String password = getPara("password");
			Integer id = getParaToInt("id");
			WXUserModel model = new WXUserModel();
			if(id==null){
				model.set("name", userName);
				model.set("password", password);
				model.set("reg_time", new Date());
				model.save();
			}else{
				model.set("name", userName);
				model.set("password", password);
				model.set("id", id);
				model.update();
			}
			result.put(RESULT, true);
			result.put(MESSAGE, "用户添加成功！");
		} catch (Exception e) {
			LOG.debug("用户添加失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "用户添加失败！");
		}
		renderJson(result);
	}
	
	public void delete(){
		Map<String, Object> result = getResultMap();
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = WXUserModel.dao.deleteById(id);
			if (deleteOK) {
				result.put(RESULT, true);
				result.put(MESSAGE, "用户删除成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "用户删除失败！");
			}
		} catch (Exception e) {
			LOG.debug("用户删除失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "用户删除失败！");
		}
		renderJson(result);
	}
	
}
