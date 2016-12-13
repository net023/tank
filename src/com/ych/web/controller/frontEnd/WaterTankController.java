package com.ych.web.controller.frontEnd;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.interceptor.ResponseInterceptor;
import com.ych.web.listenner.SessionListenner;
import com.ych.web.model.AppMgrModel;
import com.ych.web.model.CompanyProfileModel;
import com.ych.web.model.CustomServiceModel;
import com.ych.web.model.IrradiateModel;
import com.ych.web.model.PdfModel;
import com.ych.web.model.Result;
import com.ych.web.model.WXUserModel;
import com.ych.web.validator.WtValidator;

@Control(controllerKey = "/jk")
@Before({ResponseInterceptor.class,WtValidator.class})
public class WaterTankController extends BaseController{
	private static final Logger LOG = Logger.getLogger(WaterTankController.class);
	
	/**
	 * 用户登录
	 * 移动客户端通过微信sdk 调用api转向到微信登录授权页面 然后移动客户端得到对应的code 然后移动客户端通过sdk的api通过code和appid
	 * 以及secret、grant_type来得到对应的access_token、refresh_token等信息  最后通过access_token 来访问sdk的访问用户信息api
	 * 得到用户的openid
	 */
//	@Before({WtValidator.class})
	public void wxLogin(){
		Result rj = new Result(0);
		String openid = getPara("openid");
		try {
			WXUserModel model = WXUserModel.dao.isRegistered(openid);
			Date date = Calendar.getInstance().getTime();
			if(model==null){
				//没有注册过
				model = new WXUserModel();
				model.set("openid", openid);
				model.set("reg_time", new Date());
				model.set("name", getPara("name"));
				model.set("phone", getPara("phone"));
				model.set("login_time", date);
				model.save();
			}else{
				model.set("login_time", date);
				model.update();
			}
			model = WXUserModel.dao.isRegistered(openid);
			//存入session
			HttpSession session = getSession();
			session.setMaxInactiveInterval(30*60);
			session.setAttribute(SessionListenner.SESSION_USER_KEY, model);
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->login[微信登录失败]", e);
		}
		renderJson(rj);
	}
	
	
	//获取太阳辐射量
	public void getIrradiatePageList(){
		Result rj = new Result(0);
		try {
			Pager pager = createPager();
			Page<IrradiateModel> page = IrradiateModel.dao.getPager(pager);
			rj.setData(page);
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->getIrradiatePageList[获取太阳辐射量失败]", e);
		}
		renderJson(rj);
	}
	
	
	//app版本检查更新
	public void checkAppVersion(){
		Result rj = new Result(0);
		try {
			Integer order = getParaToInt("order");
			String version = getPara("version");
			AppMgrModel model = AppMgrModel.dao.checkAppVersion(order,version);
			if(model!=null){
				rj.setData(model);
			}
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->checkAppVersion[检查app版本失败]", e);
		}
		renderJson(rj);
	}
	
	//查看客服电话号码
	public void getCustomServiceTelePhone(){
		Result rj = new Result(0);
		try {
			rj.setData(CustomServiceModel.dao.find("select * from custom_service"));
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->getCustomServiceTelePhone[获取客服电话失败]", e);
		}
		renderJson(rj);
	}
	
	
	//下载pdf文件
	public void downloadPdf(){
		try {
			Integer id = getParaToInt("id");
			PdfModel pdfModel = PdfModel.dao.findById(id);
			File tempFile = File.createTempFile(pdfModel.getStr("pdf_name"), ".pdf");
			FileUtils.writeByteArrayToFile(tempFile, pdfModel.getBytes("pdf_res"));
			renderFile(tempFile);
		} catch (Exception e) {
			LOG.error("WaterTankController->downloadPdf[下载pdf文件失败]", e);
		}
	}
	
	
	//pdf查询
	public void getPdfList(){
		Result rj = new Result(0);
		try {
			Pager pager = createPager();
			if (getPara("pdfName") != null && !"".equals(getPara("pdfName"))) {
				pager.addParam("pdfName", getPara("pdfName"));
			}
			Page<?> page = PdfModel.dao.getPager2(pager);
			rj.setData(page);
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->getPdfList[pdf查询失败]", e);
		}
		renderJson(rj);
	}
	
	
	//获取企业官网
	public void getCompanyUrl(){
		Result rj = new Result(0);
		try {
			CompanyProfileModel companyProfileModel = CompanyProfileModel.dao.findFirst("select * from company_profile");
			rj.setCode(1);
			rj.setData(companyProfileModel.getStr("url"));
		} catch (Exception e) {
			LOG.error("WaterTankController->getCompanyUrl[获取企业官网失败]", e);
		}
		renderJson(rj);
	}
	
	
	//获取企业简介
	public void getCompanyInfo(){
		Result rj = new Result(0);
		try {
			CompanyProfileModel companyProfileModel = CompanyProfileModel.dao.findFirst("select * from company_profile");
			rj.setCode(1);
			rj.setData(companyProfileModel);
		} catch (Exception e) {
			LOG.error("WaterTankController->getCompanyInfo[获取企业简介失败]", e);
		}
		renderJson(rj);
	}
	
	
	public static void main(String[] args) {
		Map<String, String> ss = new HashMap<String, String>();
		String put = ss.put("aa", "bbb");
		System.out.println(put);
	}
}
