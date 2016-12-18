package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "realtimedata")
public class RealTimeDataModel extends Model<RealTimeDataModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final RealTimeDataModel dao = new RealTimeDataModel();
	
	
	/*public Page<RealTimeDataModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("Project.pager", pager.getParamsMap(), param),
				param.toArray());
	}*/
	
}
