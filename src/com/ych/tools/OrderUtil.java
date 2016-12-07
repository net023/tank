package com.ych.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 蔡陶军
 *
 */
public class OrderUtil {
	// map
	private static Map<String, Integer> ORDER = new HashMap<String, Integer>();
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

	// 获取订单号,然后自增1
	public synchronized static String getOrderNum(){
		//获取当前日期 yyyymmdd
		Calendar curCal = Calendar.getInstance();
		String curDateStr = SDF.format(curCal.getTime());
		//删除昨天的记录
		curCal.add(Calendar.DAY_OF_MONTH, -1);
		String yesterDay  = SDF.format(curCal.getTime());
		ORDER.remove(yesterDay);
		if(ORDER.get(curDateStr)==null){
			ORDER.put(curDateStr, 1);
		}else{
			ORDER.put(curDateStr, ORDER.get(curDateStr)+1);
		}
		Integer value = ORDER.get(curDateStr);
		String result = "";
		if(value.toString().length()<4){
			result = result.concat(curDateStr);
			int i = 4-value.toString().length();
			for(int k=0;k<i;k++){
				result = result.concat("0");
			}
			result = result.concat(value+"");
		}else{
			result = curDateStr+value;
		}
		return result;
	}
}
