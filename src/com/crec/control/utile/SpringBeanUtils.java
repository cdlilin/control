package com.crec.control.utile;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.crec.control.engine.MessageHandler;

public class SpringBeanUtils implements ServletContextListener {
    
	private static Logger log = Logger.getLogger(MessageHandler.class);
    private static ApplicationContext springContext;
    
    public SpringBeanUtils() {
        super();
    }
    
    public void contextInitialized(ServletContextEvent event) {
        springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
    }
    

    public void contextDestroyed(ServletContextEvent event) {
    }
    
    public static ApplicationContext getApplicationContext() {
        return springContext;
    }
    
    public static Object getBean(String name){
    	log.info("当前获取类:" + name);
    	return springContext.getBean(name);
    }
    
    public static void setSpringContext(ApplicationContext app){
    	SpringBeanUtils.springContext = app;
    }

    
}