package com.ych.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ych.base.common.Pager;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "wxuser")
public class WXUserModel extends Model<WXUserModel> {

	private static final long serialVersionUID = 4831252371504108418L;
	public static final WXUserModel dao = new WXUserModel();
	
	/**
	 * 判断是否注册过
	 * @param openid 微信用户的openid
	 * @return
	 */
	public WXUserModel isRegistered(String openid){
		WXUserModel wxUserModel = dao.findFirst("select * from wxuser where openid = ?", openid);
		return wxUserModel;
	}
	
	public WXUserModel accountLogin(String userName,String password){
		WXUserModel wxUserModel = dao.findFirst("select * from wxuser where name = ? and password = ?", userName,password);
		return wxUserModel;
	}
	
	
	public Page<WXUserModel> getPager(Pager pager) {
		LinkedList<Object> param = new LinkedList<Object>();
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" select * ",
				SqlXmlKit.getSql("WXUser.pager", pager.getParamsMap(), param),
				param.toArray());
	}
	
	public boolean modify(Integer id,Integer status){
		return Db.update("update wxuser set status = ? where id = ?",status,id)==1;
	}
}
