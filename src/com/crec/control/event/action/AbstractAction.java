package com.crec.control.event.action;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.crec.control.event.validate.Validate;


public abstract class AbstractAction{
	
	private static Logger log = Logger.getLogger(AbstractAction.class);
	
	private String resultKey;   
	
	private String name;
	
	private String description; 
	
	private String parameter;  

	private ArrayList<Validate> validates = new ArrayList<>(); 
	
	public AbstractAction(){
		
	}
	
	public AbstractAction(String name, String description) {
		super(); 
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
	 
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public void begin(Process work){
		//推送数据
		log.debug("begin[" + this.description + "]");
		
	}
	
	public JSONObject doAction(Process work){
		this.begin(work); 
		JSONObject result = this.excutor(work);
		this.end(result);
		return result;
	} 
	
	public abstract JSONObject excutor(Process work);
	
	
	public void end(JSONObject result){
		log.debug("end[" + this.description + "]->[" + result + "]");
	}
	
	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	} 
	
	public ArrayList<Validate> getValidates() {
		return validates;
	}

	public void setValidates(ArrayList<Validate> validates) {
		this.validates = validates;
	}

	public <T extends Validate> void addValidate(T validate){
		this.validates.add(validate);
	}
	
	public String getValidate(){
		StringBuffer sb = new StringBuffer();
		for(Validate v : this.validates){
			if(sb.length() > 0){
				sb.append("\n");
			}
			sb.append(v.getValidate());
		}  
		return sb.toString();
	}

}
