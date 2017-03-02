package com.ych.web.model;

import java.util.LinkedList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.ych.base.common.Pager;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "irradiate")
public class IrradiateModel extends Model<IrradiateModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final IrradiateModel dao = new IrradiateModel();
	
	
	public IrradiateModel getModel(String province,String city,Double latitude,Double mj){
		return dao.findFirst("select * from irradiate where province=? and city=? and latitude=? and mj=?", province,city,latitude,mj);
	}
	
	public IrradiateModel getModelByProvinceCity(String province,String city){
		return dao.findFirst("select * from irradiate where province=? and city=? ", province,city);
	}
	
	
	public List<Record> getProvinces(){
		return Db.find("select province from irradiate");
	}
	
	
	public List<Record> getCities(){
		return Db.find("select city from irradiate");
	}
	
	
	
	public Page<IrradiateModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("Irradiate.pager", pager.getParamsMap(), param),
				param.toArray());
	}
	
}
