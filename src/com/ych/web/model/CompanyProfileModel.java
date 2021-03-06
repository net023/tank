package com.ych.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ych.base.common.Pager;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "company_profile")
public class CompanyProfileModel extends Model<CompanyProfileModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final CompanyProfileModel dao = new CompanyProfileModel();
	
	
	public Page<CompanyProfileModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("CompanyProfileModel.pager", pager.getParamsMap(), param),
				param.toArray());
	}
	
}
