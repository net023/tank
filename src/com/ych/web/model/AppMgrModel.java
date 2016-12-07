package com.ych.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ych.base.common.Pager;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "app_mgr")
public class AppMgrModel extends Model<AppMgrModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final AppMgrModel dao = new AppMgrModel();
	
	
	public Page<AppMgrModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("Appmgr.pager", pager.getParamsMap(), param),
				param.toArray());
	}
	
	public boolean modify(Integer status,Integer id) {
		return Db.update("update app_mgr set status = ? where id = ?", status, id) == 1;
	}

	public AppMgrModel checkAppVersion(Integer order, String version) {
		return dao.findFirst(SqlXmlKit.getSql("Appmgr.checkVersion"),order);
	}
	
}
