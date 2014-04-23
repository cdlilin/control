package com.crec.control.event.validate;

import org.apache.log4j.Logger;

public class EQOperation extends Operation{
	
	private static Logger log = Logger.getLogger(EQOperation.class);
	  
	public EQOperation(){}

	public EQOperation(String value, String paramString) {
		super(value, paramString); 
	}

	@Override
	public boolean compare(Object obj) {
		// TODO Auto-generated method stub
		log.debug("operation[" + obj + "]==[" + this.getValue() + "]");
		return String.valueOf(obj).equals(this.getValue());
	}

}
