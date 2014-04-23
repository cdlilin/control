package com.crec.control.event.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.crec.control.engine.InnerMessageHandler;
import com.crec.control.engine.MessageHandler;
import com.crec.control.event.validate.Validate;
import com.crec.control.utile.ParameterHelper;
import com.crec.control.utile.SpringBeanUtils;

public class Process implements Callable<Object>{
	
	private static Logger log = Logger.getLogger(MessageHandler.class);
	
	private String uuid;
	private Event event;   
	private String username;   
	
	@Autowired
	private  AmqpTemplate replyTemplate; 
	
	private Map<String, JSONObject> resources = new HashMap<String, JSONObject>() ;  
	
	public Process(Event event, JSONObject param){
		this.event = event;
		this.currentAction = event.getStartAction();
		this.setUsername(param.getJSONObject(MessageHandler.HEAD_PROPERTY).getString("user")); 
		this.resources.put(event.getParameter(), param.getJSONObject(MessageHandler.PARAM_PROPERTY)); 
		this.uuid = param.getJSONObject(MessageHandler.HEAD_PROPERTY).getString("requestId");
	} 

	private String currentAction; 
	 
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	} 
	
	public JSONObject getResource(){
		JSONObject json = new JSONObject();
		 if (this.resources != null && this.resources.size() > 0) {
	            for (String key : this.resources.keySet()) {
	            	json.put(key, this.resources.get(key));
	            } 
	     } 
		 return json;
	}
	
	public String getResultParam(){
		return this.event.getResult();
	}  
	
	public String getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}  
	
	public void putResult(String resultKey,JSONObject result){
		resources.put(resultKey, result); 
	}
	
	public AbstractAction getAction(String name){
		return  this.event.getAction(name);
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public Object call() throws Exception {
		if("END".equals(this.currentAction)){
			this.sendResult();
			return null;
		}
		AbstractAction action = this.event.getAction(this.currentAction); 
		log.debug("proccess:[" + this.uuid + "]running event[" + this.event.getName() + "] [" + action.getDescription() + "]start");
		JSONObject result = action.doAction(this); 
		
		log.info("proccess:[" + this.uuid + "]running event[" + this.event.getName() + "] [" + action.getDescription() + "]getresult");
		log.info(result.toString());
		this.putResult(action.getResultKey(), result);  
		ArrayList<Validate>  arr = action.getValidates();
		//如果一个单人分成多任务这个地方会出错吧
		boolean flag = false;
		for(Validate v : arr){
			if(v.validate(result)){
				this.setCurrentAction(v.getNext());
				log.debug("验证通过:[" + action.getDescription() + "]");
				InnerMessageHandler.fireEvent(this);
				flag = true;
				break;
			} 
		} 
		//处理异常
		if(!flag){
			this.exceptionHandle();
		}
		log.debug("proccess:[" + this.uuid + "]run event[" + this.event.getName() + "] [" + action.getDescription() + "]end");
		return null;
	}
	
	
	public void sendResult(){
		log.debug("proccess:[" + this.uuid + "]返回用户需要的结果:");
		String resultParam = this.getResultParam();
		if(resultParam != null && !"".equals(resultParam)){
//			this.getAmqpTemp().convertAndSend(this.getRoutingKey(), "{\"reuqestId\": \"" + this.uuid + "\"}".getBytes());// send
			String result =  new StringBuffer("{\"reuqestId\": \"")
								.append(this.uuid)
								.append("\", \"result\":{\"userparam\":\"")
								.append(ParameterHelper.getReplacement(this.getResultParam(), this.getResource()).replaceAll("\"", "\\\\\\\""))
								.append("\"}}").toString();
			this.getAmqpTemp().convertAndSend(this.getRoutingKey(), result.getBytes()); 
			log.debug(result);
		}
	} 
	
	public  AmqpTemplate getAmqpTemp(){
		return (AmqpTemplate)SpringBeanUtils.getBean(this.event.getAmqpTemp());
	}
	
	public String getRoutingKey(){
		return this.event.getRoutingKey();
	} 

	public void exceptionHandle(){ 
		AbstractAction action = this.event.getAction(this.currentAction);   
		log.debug("proccess:[" + this.uuid + "]当前处理遇到错误:[" + action.getDescription() + "]"); 
		log.debug("proccess:[" + this.uuid + "]期望结果:["+ action.getValidate() + "]"); 
		log.debug("proccess:[" + this.uuid + "]处理结果:[" + this.resources.get(action.getResultKey()) + "]");
		String result =  new StringBuffer("{\"reuqestId\": \"")
							.append(this.uuid)
							.append("\",\"result\":{\"code\":\"-1\", \"message\":\"处理遇到错误:[")
							.append(action.getDescription()).append("]\",\"userparam\":\"")
							.append(this.resources.get(this.event.getParameter()).toString().replaceAll("\"", "\\\\\\\""))
							.append("\"}}").toString();
		this.getAmqpTemp().convertAndSend(this.getRoutingKey(), result.getBytes());
	}

}
