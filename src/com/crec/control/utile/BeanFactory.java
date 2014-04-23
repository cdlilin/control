package com.crec.control.utile;

import com.crec.control.event.action.AbstractAction;
import com.crec.control.event.action.Event;
import com.crec.control.event.validate.Operation;
import com.crec.control.event.validate.Validate;

public class BeanFactory {
	
	@SuppressWarnings("unchecked")
	public static<T> T getBean(Class<?> t){
		try { 
			System.out.println("获取类：" + t.getName());
			return (T)t.newInstance(); 
		} catch (IllegalAccessException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public AbstractAction getAction(String action){
		try {
			return (AbstractAction) ClassMap.getClass(action + Constants.ACTION_CLASS_KEY).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Event getEvent(){
		try {
			return (Event) ClassMap.getClass(Constants.EVENT_CLASS_KEY).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Operation getOperation(String name){
		try {
			return (Operation) ClassMap.getClass(name + Constants.OPERATION_CLASS_KEY).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Validate getValidate(){
		try {
			return (Validate) ClassMap.getClass(Constants.VALIDATE_CLASS_KEY).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		ClassMap.getClass("rest" + Constants.ACTION_CLASS_KEY, null);
		AbstractAction a = getBean(ClassMap.getClass("rest" + Constants.ACTION_CLASS_KEY, null));
		a.setName("test");
		System.out.println(a.getName());
	}

}
