package com.ych.tools;

import java.util.Arrays;
import java.util.List;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class SysConstants {

	private static final Prop prop = PropKit.use(DevConstants.SYS_CONFIG_FILENAME);

	/**
	 * controller不限制的url(不带方法名)
	 */
	public static final List<String> CONTROLLER_UNLIMIT_URL_LIST = Arrays.asList(prop.get("controllerUnLimitUrl", ";").split(";"));

	/**
	 * action不限制的url(带方法名)
	 */
	public static final List<String> ACTION_UNLIMIT_URL_LIST = Arrays.asList(prop.get("actionUnLimitUrl", ";").split(";"));

	/**
	 * 默认用户密码
	 */
	public static final String DEFAULT_PASSWORD = "111111";
	
	/**
	 * 门店后台登录默认密码
	 */
	public static final String BACK_DEFAULT_PASSWORD = "222222";

	/**
	 * DEBUG模式
	 */
	public static final boolean DEBUG = false;

	/**
	 * 是否压缩HTML代码
	 */
	public static final boolean HTML_IS_COMPRESS = false;

	/**
	 * session user
	 */
	public static final String SESSION_USER = "sessionUser";

	/**
	 * 视图默认路径
	 */
	public static final String VIEW_BASE_PATH = "biz-logic";

	
	/**
	 * 图片Dir 图片文件的存放地址
	 */
	public static final String IMG_DIR = prop.get("imgDir");


	/**
	 * 
	 * 文件上传大小限制 默认200M
	 * 
	 */
	public static final int MAX_POST_SIZE = prop.getInt("maxPostSize", 500 * 1024 * 1024);

	
	/**
	 * 文章图片路径前缀
	 */
	public static final String IMGPRE = prop.get("img.pre");
	
	/**
	 * 微信项目名称
	 */
	public static final String YCH_WX = prop.get("ych.wx");
	
	
	//百度lbs开发者ak码
	public static final String AK = prop.get("baidu.ak");

	//百度lbs数据库id
	public static final String GEOID = prop.get("baidu.geoid");

	//百度lbs搜索范围(半径  单位:米)
	public static final String RADIUS = prop.get("baidu.radius");
	
	
	
}
