package com.ych.web.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class WtValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		String sp = c.getRequest().getServletPath();
		switch (sp) {
			case "/jk/login":
				validateRequiredString("openid", "openid", "openid参数必须");
				break;
			case "/jk/smc":
				validateRequiredString("p", "p", "手机号码参数必须");
				break;
			case "/jk/vc":
				validateRequiredString("p", "p", "手机号码参数必须");
				validateRequiredString("c", "c", "验证码参数必须");
				break;
			case "/jk/oil":
				validateInteger("bid", 0, Integer.MAX_VALUE, "bid", "品牌id不正确");
				break;
			case "/jk/dp":
				validateInteger("pid", 0, Integer.MAX_VALUE, "pid", "图片id不正确");
				break;
			case "/jk/gcbt":
				validateInteger("tid", 0, Integer.MAX_VALUE, "tid", "汽车类型id不正确");
				break;
			case "/jk/gtbs":
				validateInteger("sid", 0, Integer.MAX_VALUE, "sid", "汽车系列id不正确");
				break;
			case "/jk/gsbb":
				validateInteger("bid", 0, Integer.MAX_VALUE, "bid", "汽车品牌id不正确");
				break;
			case "/jk/smi":
				validateRequired("lyid", "lyid", "力洋id参数必须");
				validateRequired("xgls", "xgls", "已行驶的里程数参数必须");
				validateRequired("scgls", "scgls", "上次保养的里程数参数必须");
				break;
			case "/jk/sv":
				validateRequired("vin", "vin", "汽车vin码参数必须");
				break;
			case "/jk/sbd":
			case "/jk/sbg":
			case "/jk/sbq":
			case "/jk/sbs":
				validateInteger("ms_id", 0, Integer.MAX_VALUE, "ms_id", "保养类型id不正确");
				validateInteger("page", 0, Integer.MAX_VALUE, "page", "分页页码不正确");
				break;
			case "/jk/guo":
				validateInteger("wx_id", 0, Integer.MAX_VALUE, "wx_id", "微信用户id不正确");
				validateInteger("page", 0, Integer.MAX_VALUE, "page", "分页页码不正确");
				break;
			default :
				break;
		}
	}

	@Override
	protected void handleError(Controller c) {
		addError("r", "false");
		c.renderJson();
	}
}
