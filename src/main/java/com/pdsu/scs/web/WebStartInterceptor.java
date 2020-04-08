package com.pdsu.scs.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebStartInterceptor implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//将web应用的名称保存到application范围中
		ServletContext application = sce.getServletContext();
		application.setAttribute("APP_PATH", application.getContextPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
}
