package com.ych.web.controller.backEnd;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.tools.excel.XxlsPrint;
import com.ych.web.model.AppMgrModel;
import com.ych.web.model.IrradiateModel;
import com.ych.web.model.PdfModel;

@Control(controllerKey="/irradiate")
public class IrradiateController extends BaseController {
	private static Logger LOG = Logger.getLogger(IrradiateController.class);
	
	public void index(){
		List<Record> provinces = IrradiateModel.dao.getProvinces();
		List<Record> cities = IrradiateModel.dao.getCities();
		List<Record> temp_provinces = new ArrayList<>(provinces);
		List<Record> temp_cities = new ArrayList<>(cities);
		Record defaultProvince = new Record();
		defaultProvince.set("province", "  ");
		temp_provinces.add(defaultProvince);
		Record defaultCity = new Record();
		defaultCity.set("city", "  ");
		temp_cities.add(defaultCity);
		setAttr("provinces", JsonKit.toJson(temp_provinces).replace("\"", "'"));
		setAttr("cities", JsonKit.toJson(temp_cities).replace("\"", "'"));
		render("irradiate/irradiate_index");
	}
	
	public void list(){
		Pager pager = createPager();
		if (getPara("province") != null && !"".equals(getPara("province"))) {
			pager.addParam("province", getPara("province"));
		}
		if (getPara("city") != null && !"".equals(getPara("city"))) {
			pager.addParam("city", getPara("city"));
		}
		Page<?> page = IrradiateModel.dao.getPager(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	
	public void importExcel(){
		Map<String, Object> result = getResultMap();
		try {
			File file = getFile().getFile();
			String absolutePath = file.getAbsolutePath();
			XxlsPrint howto = new XxlsPrint();
			howto.processOneSheet(absolutePath, 1);
			List<List> data = howto.getMsg();
			List<String> row = null;
			DecimalFormat df = new DecimalFormat("00.00");
			df.setRoundingMode(RoundingMode.HALF_UP);
			for (int i = 2; i < data.size(); i++) {
				row = data.get(i);
				String province = row.get(0);
				String city = row.get(1);
				String latitudeStr = row.get(2);
				if(null==latitudeStr||"".equals(latitudeStr)){
					latitudeStr="0";
				}
				Double latitude = Double.valueOf(latitudeStr);
				latitude = Double.valueOf(df.format(latitude));
				
				String mjStr = row.get(3);
				Double mj = Double.valueOf(mjStr);
				mj = Double.valueOf(df.format(mj));
				IrradiateModel irradiateModel = IrradiateModel.dao.getModelByProvinceCity(province, city);
//				IrradiateModel irradiateModel = IrradiateModel.dao.getModel(province, city, latitude, mj);
				if(irradiateModel==null){
					irradiateModel = new IrradiateModel();
					irradiateModel.set("province", province).set("city", city).set("latitude", latitude).set("mj", mj).save();
				}else{
					irradiateModel.set("latitude", latitude).set("mj", mj).update();
				}
			}
			result.put(RESULT, true);
			result.put(MESSAGE, "上传成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put(RESULT, false);
			result.put(MESSAGE, "上传失败！");
		}
		renderJson(result);
	}
	
	
	@Before(Tx.class)
	public void add() {
		Map<String, Object> result = getResultMap();
		try {
			String contentType = getRequest().getHeader("Content-Type");
			UploadFile file = null;
			if(contentType.contains("multipart/form-data")){
				file = getFile();
			}
			Integer id = getParaToInt("id");
			String pdfName = getPara("pdfName");
			if(id==null){
				byte[] bs = FileUtils.readFileToByteArray(file.getFile());
				PdfModel  pdf = new PdfModel();
				pdf.set("pdf_name", pdfName).set("pdf_res", bs).save();
			}else{
				PdfModel pdfModel = PdfModel.dao.findById(id);
				if(file==null){
					pdfModel.set("pdf_name", pdfName).update();
				}else{
					pdfModel.set("pdf_name", pdfName).set("pdf_res", FileUtils.readFileToByteArray(file.getFile())).update();
				}
			}
			result.put(RESULT, true);
			result.put(MESSAGE, "pdf添加成功！");
		} catch (Exception e) {
			LOG.debug("pdf添加失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "pdf添加失败！");
		}
		renderJson(result);
	}
	
	public void delete(){
		Map<String, Object> result = getResultMap();
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = IrradiateModel.dao.deleteById(id);
			if (deleteOK) {
				result.put(RESULT, true);
				result.put(MESSAGE, "删除成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "删除失败！");
			}
		} catch (Exception e) {
			LOG.debug("日照量删除失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "日照量删除失败！");
		}
		renderJson(result);
	}
	
	public void update(){
		Map<String, Object> result = getResultMap();
		try {
			IrradiateModel irradiateModel = getModelWithOutModelName(IrradiateModel.class, true);
			irradiateModel.update();
			result.put(RESULT, true);
			result.put(MESSAGE, "更新成功！");
		} catch (Exception e) {
			LOG.debug("日照量更新失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "日照量版本添加失败！");
		}
		renderJson(result);
	}
	
	public void download(){
		String templatePath = getRequest().getServletContext().getRealPath("/template.xlsx");
		renderFile(new File(templatePath));
	}
	
}
