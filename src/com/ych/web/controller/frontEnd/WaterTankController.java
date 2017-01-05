package com.ych.web.controller.frontEnd;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.listenner.SessionListenner;
import com.ych.web.model.ApkModel;
import com.ych.web.model.AppMgrModel;
import com.ych.web.model.CompanyProfileModel;
import com.ych.web.model.CustomServiceModel;
import com.ych.web.model.IrradiateModel;
import com.ych.web.model.PdfModel;
import com.ych.web.model.UserProjectModel;
import com.ych.web.model.RealTimeDataModel;
import com.ych.web.model.Result;
import com.ych.web.model.WXUserModel;
import com.ych.web.model.WatersupplyparameterModel;
import com.ych.web.validator.WtValidator;

@Control(controllerKey = "/jk")
@Before({WtValidator.class})
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
	
	
	//添加项目
	public void addProject(){
		Result rj = new Result(0);
		try {
			UserProjectModel projectModel = getModelWithOutModelName(UserProjectModel.class, true);
			if(null!=projectModel){
				projectModel.save();
			}
			rj.setCode(1);
			rj.setData(projectModel.getInt("id"));
		} catch (Exception e) {
			LOG.error("WaterTankController->addProject[添加项目失败]", e);
		}
		renderJson(rj);
	}
	
	//更新项目
	public void updateProject(){
		Result rj = new Result(0);
		try {
			UserProjectModel projectModel = getModelWithOutModelName(UserProjectModel.class, true);
			if(null!=projectModel){
				projectModel.update();
			}
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->updateProject[更新项目失败]", e);
		}
		renderJson(rj);
	}
	
	//删除项目
	public void delProject(){
		Result rj = new Result(0);
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = UserProjectModel.dao.deleteById(id);
			if (deleteOK) {
				rj.setCode(1);
			}
		} catch (Exception e) {
			LOG.error("WaterTankController->delProject[删除项目失败]", e);
		}
		renderJson(rj);
	}
	
	//查询项目列表
	public void getProjctList(){
		Result rj = new Result(0);
		try {
			Pager pager = createPager();
			pager.addParam("userId", getPara("userId"));
			Page<?> page = UserProjectModel.dao.getPagerByUserId(pager);
			rj.setData(page);
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->getProjctList[项目列表查询失败]", e);
		}
		renderJson(rj);
	}
	
	
	//添加or更新项目实时运行状态
	public void updateProjectData(){
		Result rj = new Result(0);
		try {
			String dataStr = getPara("dataStr");
			if (null!=dataStr && !"".equals(dataStr) && dataStr.length() == 119) {
				String head = dataStr.substring(0, 4);
				String id = dataStr.substring(4, 9);
				String dateTime = dataStr.substring(9, 23);
				String jobNumber = dataStr.substring(23, 28);
				String temperatureSignal = dataStr.substring(28, 61);
				String percentage = dataStr.substring(61, 67);
				String ioState = dataStr.substring(67, 89);
				String powerConsumption = dataStr.substring(89, 99);
				String furehaoneng = dataStr.substring(99, 109);
				String nengxiao = dataStr.substring(109, 119);
				RealTimeDataModel model = new RealTimeDataModel();
				model.set("head", head).set("id", id).set("dateTime", dateTime).set("jobNumber", jobNumber)
					.set("temperatureSignal", temperatureSignal).set("percentage", percentage).set("ioState", ioState)
					.set("powerConsumption", powerConsumption).set("furehaoneng", furehaoneng).set("nengxiao", nengxiao);
				RealTimeDataModel dataModel = RealTimeDataModel.dao.findById(id);
				if (null==dataModel) {
					model.save();
				}else{
					model.update();
				}
				rj.setCode(1);
			}
		} catch (Exception e) {
			LOG.error("WaterTankController->updateProjectData[更新项目实时运行状态失败]", e);
		}
		renderJson(rj);
	}
	
	
		//添加or更新供水系统参数
		public void updateWaterSupplyParamter(){
			Result rj = new Result(0);
			try {
				String dataStr = getPara("dataStr");
				if (null!=dataStr && !"".equals(dataStr) && dataStr.length() == 190) {
					String head = dataStr.substring(0, 4);
					String id = dataStr.substring(4, 9);
					String dateTime = dataStr.substring(9, 23);
					String afState = dataStr.substring(23, 29);
					String fhxgbfState = dataStr.substring(29, 35);
					String temperatureInSource = dataStr.substring(35, 41);
					String watermeterInSource = dataStr.substring(41, 47);
					String ammeterInSource = dataStr.substring(47, 53);
					String updateStr = dataStr.substring(53, 67);
					String heatingCycle = dataStr.substring(67, 74);
					String heatingStart = dataStr.substring(74, 82);
					String heatingEnd = dataStr.substring(82, 90);
					String fureStart = dataStr.substring(90, 98);
					String fureEnd = dataStr.substring(98, 106);
					String frsw = dataStr.substring(106, 109);
					String fwbswd = dataStr.substring(109, 112);
					String wcswz = dataStr.substring(112, 115);
					String gdhssw = dataStr.substring(115, 118);
					String gdfdsw = dataStr.substring(118, 121);
					String xhfdsw = dataStr.substring(121, 124);
					String swks1 = dataStr.substring(124, 128);
					String swjs1 = dataStr.substring(128, 132);
					String swz1 = dataStr.substring(132, 135);
					String swks2 = dataStr.substring(135, 139);
					String swjs2 = dataStr.substring(139, 143);
					String swz2 = dataStr.substring(143, 146);
					String swks3 = dataStr.substring(146, 150);
					String swjs3 = dataStr.substring(150, 154);
					String swz3 = dataStr.substring(154, 157);
					String swks4 = dataStr.substring(157, 161);
					String swjs4 = dataStr.substring(161, 165);
					String swz4 = dataStr.substring(165, 168);
					String aqsw = dataStr.substring(168, 171);
					String ytbssjcd = dataStr.substring(171, 174);
					String pksjcd = dataStr.substring(174, 177);
					String sostelphone = dataStr.substring(177, 188);
					String multiple = dataStr.substring(188, 190);
					WatersupplyparameterModel model = new WatersupplyparameterModel();
					model.set("head", head).set("id", id).set("dateTime", dateTime).set("afState", afState)
					.set("fhxgbfState", fhxgbfState).set("temperatureInSource", temperatureInSource).set("watermeterInSource", watermeterInSource).set("ammeterInSource", ammeterInSource).set("updateStr", updateStr).set("heatingCycle", heatingCycle).set("heatingStart", heatingStart)
					.set("heatingEnd", heatingEnd).set("fureStart", fureStart).set("fureEnd", fureEnd).set("frsw", frsw).set("fwbswd", fwbswd).set("wcswz", wcswz).set("gdhssw", gdhssw)
					.set("gdfdsw", gdfdsw).set("xhfdsw", xhfdsw).set("swks1", swks1).set("swjs1", swjs1).set("swz1", swz1).set("swks2", swks2).set("swjs2", swjs2).set("swz2", swz2)
					.set("swks3", swks3).set("swjs3", swjs3).set("swz3", swz3).set("swks4", swks4).set("swjs4", swjs4).set("swz4", swz4)
					.set("aqsw", aqsw).set("ytbssjcd", ytbssjcd).set("pksjcd", pksjcd).set("sostelphone", sostelphone).set("multiple", multiple);
					WatersupplyparameterModel dataModel = WatersupplyparameterModel.dao.findById(id);
					if (null==dataModel) {
						model.save();
					}else{
						model.update();
					}
					rj.setCode(1);
				}
			} catch (Exception e) {
				LOG.error("WaterTankController->updateWaterSupplyParamter[添加or更新供水系统参数失败]", e);
			}
			renderJson(rj);
		}
	
	
	//获取水箱界面数据  实时监测数据
	public void getRealTimeData(){
		Result rj = new Result(0);
		try {
			Integer pid = getParaToInt("pid");
			Record record = RealTimeDataModel.dao.getRealTimeDataByProjectId(pid);
			rj.setData(record);
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->getProjctList[获取水箱界面数据失败]", e);
		}
		renderJson(rj);
	}
	
	
	//获取运行参数
	public void getRuntimeParamter(){
		Result rj = new Result(0);
		try {
			Integer pid = getParaToInt("pid");
			Record record = WatersupplyparameterModel.dao.getWatersupplyparameterByProjectId(pid);
			rj.setData(record);
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->getRuntimeParamter[获取运行参数失败]", e);
		}
		renderJson(rj);
	}
	
	
	//获取项目状况
	public void getProjectInfo(){
		Result rj = new Result(0);
		try {
			Integer pid = getParaToInt("pid");
			Record projectModel = UserProjectModel.dao.getUserProjectDataByProjectId(pid);
			rj.setData(projectModel);
			rj.setCode(1);
		} catch (Exception e) {
			LOG.error("WaterTankController->getProjectInfo[获取项目状况失败]", e);
		}
		renderJson(rj);
	}
	
	//下载apk
	public void downloadApk(){
		try {
			Integer id = getParaToInt("id");
			ApkModel apkModel = ApkModel.dao.findById(id);
			File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".apk");
			FileUtils.writeByteArrayToFile(tempFile, apkModel.getBytes("apk_res"));
			renderFile(tempFile);
		} catch (Exception e) {
			LOG.error("WaterTankController->downloadApk[下载apk包失败]", e);
		}
	}
	
	public static void main(String[] args) {
		Map<String, String> ss = new HashMap<String, String>();
		String put = ss.put("aa", "bbb");
		System.out.println(put);
		
		String str = "0123456";
		System.out.println(str.substring(0,4));
		
		String str1 = "01031500220150403164101000000520590580581511510491511511510141001000000000000001110000000000000000000000000000000000000";
		System.out.println(str1.length());
		str1 = "010560620201611182135481100000000100000010000000000000099990001234111111105002359040023500000030000000400065080005035001005070008300201130123002016301730020180019000200200450031868192848301";
		System.out.println(str1.length());
		str1 = "123456";
		System.out.println(str1.length());
		
	}
}
