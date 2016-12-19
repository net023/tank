package com.ych.web.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "realtimedata")
public class RealTimeDataModel extends Model<RealTimeDataModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final RealTimeDataModel dao = new RealTimeDataModel();
	
	
	public Record getRealTimeDataByProjectId(int pid){
		return Db.findFirst("select t1.* FROM realtimedata t1 LEFT JOIN project t2 ON t1.id=t2.codeId WHERE t2.id = ?", pid);
	}
	
}
