package com.crec.control.event.validate; 

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.crec.control.utile.JsonValueHelper;

public class Validate { 
	
	private static Logger log = Logger.getLogger(Validate.class); 
	private String next; 
	private ArrayList<Operation> operations = new ArrayList<Operation>();
	
	public Validate(){}
	
	public Validate(String next) {
		super(); 
		this.next = next == null ? "END" : next; 
	} 
	
	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	} 

	public <T extends Operation> void addOperation(T operation){
		this.operations.add(operation);
	}
    
    public boolean validate(JSONObject result){ 
    	//todo 对组合条件的支持
    	log.debug("  start validate[" + result + "]"); 
    	for(Operation op : operations){
    		log.debug("  start validate[" + op.getValue() + "====" + op.getParamName()+ "]"); 
    		return op.compare(JsonValueHelper.getValueByParamString(result, op.getParamName().split("\\.")));
    	}
    	return true;
    }
    
    public String getValidate(){
    	StringBuffer sb = new StringBuffer("[");
    	for(Operation op : operations){
    		if(sb.length() > 1){
    			sb.append(";");
    		}
    		sb.append(op.getParamName());
    		if(op instanceof EQOperation){
    			sb.append("=");
    		}
    		//todo add other
    		sb.append(op.getValue());
    	}
    	return sb.append("]->next[").append(this.next).append("]").toString();
    }
	

}
