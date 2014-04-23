package com.crec.control.event.action;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;

import com.crec.control.utile.ParameterHelper;
import com.crec.control.utile.SpringBeanUtils;

public class RabbitMqAction extends AbstractAction{
	
	private static Logger log = Logger.getLogger(RabbitMqAction.class); 
	

	private String routingKey;
	
	private String amqpTemp; 
	
	private String parameter;
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public RabbitMqAction(){
		
	}
	
	public RabbitMqAction(String name, String description) {
		super(name, description);
		// TODO Auto-generated constructor stub
	}  
	
	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getAmqpTemp() {
		return amqpTemp;
	}

	public void setAmqpTemp(String amqpTemp) {
		this.amqpTemp = amqpTemp;
	}  
	
	public JSONObject getPostParameter(JSONObject resource){ 
		//处理提供URL 
		return JSONObject.fromObject(ParameterHelper.getReplacement(this.parameter, resource));
	} 
	
	@Override
	public JSONObject excutor(Process work) { 
		try{
		
			JSONObject postParameter =  getPostParameter(work.getResource()); 
			
			log.debug("发送消息到：" + this.getRoutingKey());
			log.debug("发送消息为：" + postParameter.toString());
			log.debug("发送消息的对象：" + getAmqpTemplate());
			getAmqpTemplate().convertAndSend(this.getRoutingKey(), postParameter.toString().getBytes());
		}catch(Exception e){
			log.error("***************************************************************************"); 
			log.error("发送消息出问题了：", e);
			log.error("***************************************************************************"); 
		}
		return JSONObject.fromObject("{\"code\": 0}");
	}
	
	public  AmqpTemplate getAmqpTemplate(){
		return (AmqpTemplate)SpringBeanUtils.getBean(this.amqpTemp);
	}
	
	public static void main(String[] args) {
		
	}
} 