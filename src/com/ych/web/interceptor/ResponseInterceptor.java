package com.ych.web.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.ych.web.listenner.SessionListenner;

public class ResponseInterceptor implements Interceptor {

	/* (non-Javadoc)
	 * @see com.jfinal.aop.Interceptor#intercept(com.jfinal.core.ActionInvocation)
	 */
	@Override
	public void intercept(ActionInvocation arg) {
		// TODO Auto-generated method stub
		arg.getController().getResponse().addHeader("Access-Control-Allow-Origin", "*");
		
		//获取session中是否存在 如果不存在user 就返回错误 让调用方去登录
		String actionKey = arg.getActionKey();
		if(!"/jk/wxLogin".equals(actionKey)){
			if(!"/jk/updateProjectData".equals(actionKey)){
				Object user = arg.getController().getSession().getAttribute(SessionListenner.SESSION_USER_KEY);
				if(null==user){
					arg.getController().renderJson("code", 110);
					return;
				}
			}
		}
		arg.invoke();
	}

}
