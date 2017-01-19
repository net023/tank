package com.ych.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ych.base.common.Pager;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "oem_title")
public class OemModel extends Model<OemModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final OemModel dao = new OemModel();
	
	
	public Page<OemModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("Oem.pager", pager.getParamsMap(), param),
				param.toArray());
	}
	
	public OemModel getMaxRecord(){
		return dao.findFirst("select title from oem_title order by id desc limit 1");
	}
	
	
}
