package com.ych.web.controller.backEnd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.ych.base.common.BaseController;
import com.ych.base.common.Pager;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.model.PdfModel;

import sun.misc.BASE64Encoder;

@Control(controllerKey="/pdf")
public class PdfController extends BaseController {
	private static Logger LOG = Logger.getLogger(PdfController.class);
	
	public void index(){
		render("pdf/pdf_index");
	}
	
	public void list(){
		Pager pager = createPager();
		if (getPara("pdfName") != null && !"".equals(getPara("pdfName"))) {
			pager.addParam("pdfName", getPara("pdfName"));
		}
		Page<?> page = PdfModel.dao.getPager(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
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
	
	public void download(){
		try {
			Integer id = getParaToInt("id");
			PdfModel pdfModel = PdfModel.dao.findById(id);
			File tempFile = File.createTempFile(pdfModel.getStr("pdf_name"), ".pdf");
			FileUtils.writeByteArrayToFile(tempFile, pdfModel.getBytes("pdf_res"));
			renderFile(tempFile);
		} catch (Exception e) {
		}
	}
	
	/**
	 * pdfjs在线阅读下载用
	 */
	public void download2(){
		try {
			Integer id = getParaToInt("id");
			PdfModel pdfModel = PdfModel.dao.findById(id);
			BASE64Encoder encoder = new BASE64Encoder();
			String encode = encoder.encode(pdfModel.getBytes("pdf_res"));
//			HttpServletResponse response = getResponse();
//			response.reset();
//			response.setContentType("application/pdf");
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			bos.writeTo(response.getOutputStream());
//			OutputStream toClient = new BufferedOutputStream(bos);
//            toClient.write(encode.getBytes());
//            toClient.flush();
//            toClient.close();
//            response.flushBuffer();
			
//			File tempFile = File.createTempFile(pdfModel.getStr("pdf_name"), ".pdf");
//			FileUtils.writeByteArrayToFile(tempFile, decodeBuffer);
//			renderFile(tempFile);
			
			
			HttpServletResponse response = getResponse();
//			response.reset();
			response.setContentType("application/pdf");
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			baos.write(encode.getBytes());
			OutputStream bots = new BufferedOutputStream(response.getOutputStream());
			bots.write(encode.getBytes());
			bots.close();
			response.flushBuffer();
			renderNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void show(){
		Integer id = getParaToInt("id");
		setAttr("pdfTitle", PdfModel.dao.findById(id).getStr("pdf_name"));
		setAttr("id", id);
		render("pdf/show_index");
	}
	
	
	public void delete(){
		Map<String, Object> result = getResultMap();
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = PdfModel.dao.deleteById(id);
			if (deleteOK) {
				result.put(RESULT, true);
				result.put(MESSAGE, "pdf删除成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "pdf删除失败！");
			}
		} catch (Exception e) {
			LOG.debug("pdf删除失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "pdf删除失败！");
		}
		renderJson(result);
	}
	
}
