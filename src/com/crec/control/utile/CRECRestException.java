package com.crec.control.utile;

public class CRECRestException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private String message;
	public CRECRestException(int code){
		this.code = code;
	}
	public CRECRestException(int code, String message){
		this.code = code;
		this.message = message;
		
	}

}
