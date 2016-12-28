package com.ych.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.ych.base.common.Pager;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "userProject")
public class UserProjectModel extends Model<UserProjectModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final UserProjectModel dao = new UserProjectModel();
	
	
	public Page<UserProjectModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("UserProject.pager", pager.getParamsMap(), param),
				param.toArray());
	}
	
	public Page<UserProjectModel> getPagerByUserId(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("UserProject.pagerByUserId", pager.getParamsMap(), param),
				param.toArray());
	}
	
	public Record getUserProjectDataByProjectId(int pid){
		return Db.findFirst("select t2.projectName,t1.completeTime,t1.fureShape,t1.jireScale,t1.jireType,t1.jireZone,t1.projectAddress,t1.projectType,t1.tankConfigure,t1.waterZone from project t1 JOIN userproject t2 ON t2.accessTag = t1.accessTag AND t2.codeId = t1.codeId AND t2.id = ?", pid);
	}
	
}
