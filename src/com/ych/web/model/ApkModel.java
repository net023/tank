package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "apk_res")
public class ApkModel extends Model<ApkModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final ApkModel dao = new ApkModel();
	
}
