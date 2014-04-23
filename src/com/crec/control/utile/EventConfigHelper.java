package com.crec.control.utile;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.crec.control.engine.MessageHandler;
import com.crec.control.event.action.AbstractAction;
import com.crec.control.event.action.Event;

public class EventConfigHelper {
	@SuppressWarnings("unchecked")
	 public static<T> T xmlToBean(String xml, T t) throws DocumentException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
	  Document doc = DocumentHelper.parseText(xml);
	  
	  Element root = doc.getRootElement();
	  Field[] properties = t.getClass().getDeclaredFields();
	  Method setMeth;
	  t = (T)t.getClass().newInstance();
	  for(int j = 0; j < properties.length; j++){
	   setMeth = t.getClass().getMethod("set" + properties[j].getName().substring(0,1).toUpperCase()
	          + properties[j].getName().substring(1), properties[j].getType());
	   setMeth.invoke(t, root.elementText(properties[j].getName()));
	  }
	  return t;
	 }
	public static List<Node> getEvents(Node document){
		return document.selectNodes("/events/event");
	}
	public static List<Node> getAction(Node document){
		return document.selectNodes("action");
	}
	public static List<Node> getValidate(Node document){
		return document.selectNodes("validate");
	}
	public static List<Node> getOperation(Node document){
		return document.selectNodes("./*");
	}
	public static List<Node> getAttribute(String name, Node document){
		return document.selectNodes("./*");
	}
	
	@SuppressWarnings("unchecked")
	public static void initEvent(){
//		SAXReader reader=new SAXReader();
//		Document document = null; 
//		InputStream eventResource = Thread.currentThread().getContextClassLoader().getResourceAsStream("Event.xml");
//		try {
//				document = reader.read(eventResource); 
//				List<Node> eventList= getEvents(document);
//				BeanFactory bf = new BeanFactory();
//				Event event = null;
//				AbstractAction action = null;
//				Validate validate = null;
//				Operation op = null;
//				for(Node node : eventList){
//					Element ee = (Element)node;
//					event = bf.getEvent(); 
//					fillbean(ee, event); 
//					List<Node> actionNodes= getAction(node); 
//					for(Node actionNode : actionNodes){
//						Element ea = (Element)actionNode;
//						action = bf.getAction(ea.attributeValue("type")); 
//						fillbean(ea, action); 
//						for(Node validateNode : getValidate(actionNode)){
//							Element ev = (Element)validateNode;	
//							validate = bf.getValidate();
//							fillbean(ev, validate);
//							for(Node opNode : getOperation(validateNode)){
//								Element eop = (Element)opNode;	
//								op = bf.getOperation(eop.getName());
//								fillbean(eop, op);
//								validate.addOperation(op); 
//							}
//							action.addValidate(validate);
//						}
//						event.addAction(action); 
//					}
//					MessageHandler.addEvent(event);
//				}
//			} catch (DocumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
	}
	
	public static void initEventFromXml(){
		SAXReader reader=new SAXReader();
		Document document = null; 
//		File eventResource = new File("E:/workspace/control/src/Event.xml");
		InputStream eventResource = Thread.currentThread().getContextClassLoader().getResourceAsStream("Event.xml");
		try {
				document = reader.read(eventResource); 
				List<Node> eventList = getEvents(document); 
				Event event = null; 
				for(Node node : eventList){ 
					event = BeanFactory.getBean(ClassMap.getClass(Constants.EVENT_CLASS_KEY));
					Element ee = (Element)node; 
					fillbean(ee, event);   
					simpleFillBeans(node.selectNodes("./*"), event);
					System.out.println(event.getName());
					MessageHandler.addEvent(event);
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
	
	public static void simpleFillBeans(List<Node> list, Object parent){ 
//		Method addMethod = c.getDeclaredMethod("add" + c.getSimpleName(), new Class[]{c}); 
		for(Node node : list){
			Element ee = (Element)node;
			Attribute typeAttr = ee.attribute("type");
			
			String type = typeAttr == null ? null : typeAttr.getValue();
			System.out.println("当前正解析:" +  type + ee.getName());
			System.out.println("当前正解析:" + ee.getName());
			
			Class c = ClassMap.getClass(ee.getName(), type); 
			Object obj = BeanFactory.getBean(c); 
			fillbean(ee, obj); 
//			ArrayList a = simpleFillBeans(node.selectNodes("./*"), obj);
			if(parent != null){
				 try {
					 System.out.println(parent.getClass().getSimpleName() + "-->获取父类的添加函数:" + "add" + StringUtils.capitalise(ee.getName()));				 
					Method addMethod = getAddMethod(parent.getClass(), obj);
					if(addMethod != null){ 
						System.out.println(" 函数获取成功添加目标到父类缓存"); 
						addMethod.invoke(parent, new Object[]{obj}); 
					} 
				} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			List<Node> nodes = (List<Node>)node.selectNodes("./*");
			if(nodes != null){
				simpleFillBeans(nodes, obj); 
			}
		}   	 
	}
	
	public static Method getAddMethod(Class clazz, Object obj){
		Method[] methods = clazz.getMethods();
		Class[] clazzTypes = null;
		for(int i = 0; i < methods.length; i++){
			clazzTypes = methods[i].getParameterTypes();
			if(clazzTypes != null && clazzTypes.length == 1){ 
				if(clazzTypes[0].isAssignableFrom(obj.getClass()) 
						|| clazzTypes[0].isInstance(obj)){
					return methods[i];
				}
			} 
		}
		return null;
	}
//	public static Field[] getFields(Class c){
//		 Field[] fields = c.getClass().getDeclaredFields();
//		 
//		 if(c.getSuperclass() != null){
//			 Field[] sf = c.getSuperclass().getDeclaredFields();
//			 addMethod = c.getDeclaredMethod("add" + c.getSimpleName(), new Class[]{c});
//	if(addMethod != null){
//		
//	}
//	event = bf.getEvent(); 
//	fillbean(ee, event); 
//		 }
//		 
//		 
//		 
//	}
	
	 public static<T> T fillbean(Element e, T t) {
		 PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(t); 
		 for(PropertyDescriptor pd : origDescriptors){ 
			 Attribute att = e.attribute(pd.getName());
			 if(att != null){
				 try {
					BeanUtils.setProperty(t, att.getName(), att.getValue());
				} catch (IllegalAccessException | InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 }
		 } 
		 return t;
	}
	public static void main(String[] args) throws DocumentException {
//		initEventFromXml();
		System.out.println(AbstractAction.class.isInstance(Event.class));
	} 
}
