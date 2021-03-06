package com.ych.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.ych.base.common.Pager;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "pdf")
public class PdfModel extends Model<PdfModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final PdfModel dao = new PdfModel();
	
	
	public Page<PdfModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("Pdf.pager", pager.getParamsMap(), param),
				param.toArray());
	}
	
	public Page<Record> getPager2(Pager pager){
		return Db.paginate(pager.getPageNo(), pager.getPageSize(), "select id,pdf_name "," from pdf");
	}
	
}
