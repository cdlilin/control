package com.crec.control.utile;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.crec.control.event.action.RestAction;

public class XMLBeanUtil {
	 /**
	  * 对象数组向XML转换
	  *
	  * @param beanList
	  * @return
	  * @throws SecurityException
	  * @throws NoSuchMethodException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public static<T> String beanListToXml(List<T> beanList) throws SecurityException,
	   NoSuchMethodException,
	   IllegalAccessException, InvocationTargetException {
	  // NULL检查
	  if(beanList == null)return null;
	  
	  Document document = DocumentHelper.createDocument();
	  Object obj = beanList.get(0);
	  // 获得类名
	  String rootname = obj.getClass().getSimpleName();
	  // 添加根节点
	  Element root = document.addElement(rootname + "s");
	  
	  Field[] properties = obj.getClass().getDeclaredFields();
	        for (Object t : beanList) {                               
	         //递归实体                  
	         Element secondRoot = root.addElement(rootname);
	         for (int i = 0; i < properties.length; i++) {
	          // 反射get方法
	          Method meth = obj.getClass().getMethod(
	            "get" + properties[i].getName().substring(0, 1).toUpperCase()
	            + properties[i].getName().substring(1));
	          // 为二级节点添加属性，属性值为对应属性的值
	          secondRoot.addElement(properties[i].getName()).setText(meth.invoke(t).toString());
	         }
	        }
	  return document.asXML();
	 }
	 /**
	  * 对象向XML转换
	  *
	  * @param bean
	  * @return
	  * @throws SecurityException
	  * @throws NoSuchMethodException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public static<T> String beanToXml(T bean) throws SecurityException,
	   NoSuchMethodException,
	   IllegalAccessException, InvocationTargetException {
	  // NULL检查
	  if(bean == null)return null;
	  
	  Document document = DocumentHelper.createDocument();
	  // 获得类名
	  String rootname = bean.getClass().getSimpleName();
	  // 添加根节点
	  Element root = document.addElement(rootname);
	  
	  Field[] properties = bean.getClass().getDeclaredFields();
	     for (int i = 0; i < properties.length; i++) {
	      // 反射get方法
	      Method meth = bean.getClass().getMethod(
	        "get" + properties[i].getName().substring(0, 1).toUpperCase()
	        + properties[i].getName().substring(1));
	      // 为二级节点添加属性，属性值为对应属性的值
	      root.addElement(properties[i].getName()).setText(meth.invoke(bean).toString());
	     }
	  return document.asXML();
	 }

	 /**
	  * 由XML字符串向BeanList转换。
	  *
	  * @param <T>
	  * @param xml
	  * @param t
	  * @return
	  * @throws DocumentException
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * @throws SecurityException
	  * @throws NoSuchMethodException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  */
	 @SuppressWarnings("unchecked")
	 public static<T> List<T> xmlToBeanList(String xml, T t) throws DocumentException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
	  List<T> list = new ArrayList<T>();
	  Document doc = DocumentHelper.parseText(xml);
	  
	  Element root = doc.getRootElement();
	  Element foo ; // 二级节点
	  Field[] properties = t.getClass().getDeclaredFields();
	  Method setMeth;
	  
	  for(Iterator<Element> iter = root.elementIterator(t.getClass().getSimpleName()); iter.hasNext();){
	   foo = (Element)iter.next();
	   t = (T)t.getClass().newInstance();
	   for(int j = 0; j < properties.length; j++){
	    setMeth = t.getClass().getMethod("set" + properties[j].getName().substring(0,1).toUpperCase()
	           + properties[j].getName().substring(1), properties[j].getType());
	    setMeth.invoke(t, foo.elementText(properties[j].getName()));
	   }
	   list.add(t);
	  }
	  return list;
	 }
	 

	 /**
	  * 由XML字符串向Bean转换。
	  * 前提：XML文件只有一层。
	  *
	  * @param <T>
	  * @param xml
	  * @param t
	  * @return
	  * @throws DocumentException
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * @throws SecurityException
	  * @throws NoSuchMethodException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  */
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
	 
	
	 
	 public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IntrospectionException, InvocationTargetException {
		 
	  
	  RestAction rest = new RestAction();
	  
	  PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(rest);
	  
	  for(PropertyDescriptor pd : origDescriptors){
		  System.out.println(pd.getName());
	  }
	   
	   BeanUtils.setProperty(rest, "name", 123);
	   System.out.println(rest.getName());
	   
	  
	 
	 
	}
}
 
class MyBean{
	private String name;
	public String age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	 
}