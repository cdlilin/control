package com.crec.control.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.crec.control.event.action.Process;

public class InnerMessageHandler extends MessageHandler{
	 private static Logger log = Logger.getLogger(InnerMessageHandler.class);
	 private static ExecutorService innerPool = Executors.newFixedThreadPool(10);   
	/**
	 * {head:{user:"test", evnet: "testevent"}, url:"", params:""}
	 * @param message
	 */
	public static void fireEvent(Process work){  
		log.debug("-----------------------innerhandle--------------------------"); 
		 // 创建一个执行任务的服务    
       try {   
    	   //把当前处理流程放到预处理池中
    	   innerPool.submit(work);    
       } catch (Exception e){  
    	   log.error(e.toString()); 
       }   
	}

}
