package com.crec.control.utile;

import net.sf.json.JSONObject;

public class JsonValueHelper {
	
	public static Object getValueByParamString(JSONObject json, String[] paramString){ 
		 
		if(paramString.length == 1){
			return json.get(paramString[0]);
		}else{
			//todo 加入对JSONArray 的支持  
			JSONObject nextObj = json.getJSONObject(paramString[0]);
			if(nextObj == null){
				return "";
			} 
		    String[] paths = new String[paramString.length - 1];
		    System.arraycopy(paramString, 1, paths, 0, paramString.length -1); 
			return getValueByParamString(nextObj, paths); 
		}
	} 
	
	public static void main(String[] args) {
		JSONObject obj = JSONObject.fromObject("{a:{\"b\":2}}");
		System.out.println(getValueByParamString(obj, "a.b".split("\\.")));
	}
}
