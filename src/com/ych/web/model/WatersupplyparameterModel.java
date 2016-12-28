package com.ych.web.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "watersupplyparameter")
public class WatersupplyparameterModel extends Model<WatersupplyparameterModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final WatersupplyparameterModel dao = new WatersupplyparameterModel();
	
	
	public Record getWatersupplyparameterByProjectId(int pid){
		return Db.findFirst("SELECT t1.*,t3.projectName FROM watersupplyparameter t1 LEFT JOIN project t2 ON t2.codeId = t1.id JOIN userproject t3 ON t3.accessTag = t2.accessTag AND t3.codeId = t2.codeId AND t3.id = ?", pid);
	}
	
}
