package com.crec.control.event.validate;

import org.apache.log4j.Logger;

public class LTOperation extends Operation{
	
	private static Logger log = Logger.getLogger(LTOperation.class);
	  
	public LTOperation(){}

	public LTOperation(String value, String paramString) {
		super(value, paramString); 
	}

	@Override
	public boolean compare(Object obj) {
		// TODO Auto-generated method stub
		log.debug("operation[" + obj + "]<[" + this.getValue() + "]"); 
		
		return String.valueOf(obj).compareTo(this.getValue()) < 0;
	}

}
