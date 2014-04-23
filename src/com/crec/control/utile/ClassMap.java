package com.crec.control.utile;

import java.util.HashMap;
import java.util.Map;

public class ClassMap { 
	
	private static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>(); 
	
	static{
		new ClassMap(Constants.EVENT_CLASS_KEY, com.crec.control.event.action.Event.class);
		new ClassMap("eq" + Constants.OPERATION_CLASS_KEY, com.crec.control.event.validate.EQOperation.class);
		new ClassMap("rest" + Constants.ACTION_CLASS_KEY, com.crec.control.event.action.RestAction.class);
		new ClassMap(Constants.VALIDATE_CLASS_KEY, com.crec.control.event.validate.Validate.class);
		new ClassMap("rabbitmq" + Constants.ACTION_CLASS_KEY, com.crec.control.event.action.RabbitMqAction.class);
		
		
	}
	
	 
	private ClassMap(String name, Class<?> c){
		classMap.put(name, c);
	}
	
	public static Class<?> getClass(String name, String type){
		String key = type == null ? name : type + "_" + name; 
		 
		return classMap.get(key);
		
	} 
	
	public static Class<?> getClass(String name){  
		return classMap.get(name);
		
	}  
	
	public static boolean containsClass(String name, String type){
		String key = type == null ? name : name + "_" + type; 
		return classMap.containsKey(key);
	} 
	
	public static boolean containsClass(String name){ 
		return classMap.containsKey(name);
	}
}
