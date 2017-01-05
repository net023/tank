package com.ych.web.controller.backEnd;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.ych.base.common.BaseController;
import com.ych.core.plugin.annotation.Control;
import com.ych.tools.SysConstants;
import com.ych.web.model.ApkModel;

@Control(controllerKey="/apk")
public class ApkController extends BaseController {
	private static Logger LOG = Logger.getLogger(ApkController.class);
	
	@Before(Tx.class)
	public void add() {
		Map<String, Object> result = getResultMap();
		try {
			UploadFile file = getFile();
			ApkModel apkModel =new ApkModel();
			apkModel.set("apk_res", FileUtils.readFileToByteArray(file.getFile()));
			boolean save = apkModel.save();
			
			if(save){
				result.put(RESULT, true);
				result.put("id", SysConstants.serverIp+"jk/downloadApk?id="+apkModel.getInt("id"));
				result.put(MESSAGE, "apk添加成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "apk添加成功！");
			}
		} catch (Exception e) {
			LOG.debug("apk添加失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "apk添加失败！");
		}
		renderJson(result);
	}
	
	public void download(){
		try {
			Integer id = getParaToInt("id");
			ApkModel apkModel = ApkModel.dao.findById(id);
			File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".apk");
			FileUtils.writeByteArrayToFile(tempFile, apkModel.getBytes("apk_res"));
			renderFile(tempFile);
		} catch (Exception e) {
		}
	}
	
	public void delete(){
		Map<String, Object> result = getResultMap();
		try {
			Integer id = getParaToInt("id");
			boolean deleteOK = ApkModel.dao.deleteById(id);
			if (deleteOK) {
				result.put(RESULT, true);
				result.put(MESSAGE, "apk删除成功！");
			}else{
				result.put(RESULT, false);
				result.put(MESSAGE, "apk删除失败！");
			}
		} catch (Exception e) {
			LOG.debug("apk删除失败！" + e.getMessage());
			result.put(RESULT, false);
			result.put(MESSAGE, "apk删除失败！");
		}
		renderJson(result);
	}
	
}
