package com.crec.control.event.validate;
import net.sf.json.JSONObject;

public abstract class Operation {
	
	private String value;
	
	private String paramName; 
	
	public Operation(){} 
	
	public Operation(String value, String paramName) { 
		super();
		this.value = value;
		this.paramName = paramName;
	} 

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	} 
	
	abstract boolean compare(Object obj);
	
	
	public static void main(String[] args) { 
		 
		System.out.println("123".compareTo("adfa"));
		System.out.println("------------------------------------");
		JSONObject obj = JSONObject.fromObject("{a:[{\"b\":2},{\"b\":3}]}".getBytes());
		System.out.println(obj.getJSONArray("a").getJSONObject(0).get("b")); 
	}

}
