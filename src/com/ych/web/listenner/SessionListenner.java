package com.ych.web.listenner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ych.web.model.WXUserModel;

public class SessionListenner implements HttpSessionAttributeListener,HttpSessionListener {
	
	public static final String SESSION_USER_KEY = "user";
	private static Map<Integer, WXUserModel> sessions = new HashMap<>();

	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		if(arg0.getName().equals(SESSION_USER_KEY)){
			WXUserModel model = (WXUserModel)arg0.getValue();
			sessions.put(model.getInt("id"), model);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		if(arg0.getName().equals(SESSION_USER_KEY)){
			sessions.remove(((WXUserModel)arg0.getValue()).getInt("id"));
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("attributeReplaced");
	}

	@Override
	public void sessionCreated(HttpSessionEvent paramHttpSessionEvent) {
		System.out.println("sessionCreated");
//		paramHttpSessionEvent.getSession().setMaxInactiveInterval(30);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent paramHttpSessionEvent) {
		System.out.println("sessionDestroyed");
	}
	
	public static List<WXUserModel> getSessionWXUsers(){
		Collection<WXUserModel> values = sessions.values();
		List<WXUserModel> data = new ArrayList<>();
		Iterator<WXUserModel> iterator = values.iterator();
		while(iterator.hasNext()){
			data.add(iterator.next());
		}
		return data;
	}

}
