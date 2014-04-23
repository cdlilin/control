package com.crec.control.utile;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


/**
 * 
 * @author daiw
 */
public class RestClientUtils {
	private static final int errorCode = -999; 
	private static String X_Auth_Token = "";	//与iaas门户会话token 初始值为空
	
	
	private static final Logger log = Logger.getLogger(RestClientUtils.class);
	
	static {
		resetPlatToken();
	}
	
	/**
	 * Retrieve a representation by doing a GET on the specified URL.
	 * The response (if any) is converted and returned.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * @param url the URL
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand the template
	 * @return the converted object
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static <T> T get(String url, Class<T> responseType, Object... urlVariables) throws CRECRestException {
		
		if (!url.toLowerCase().startsWith("http")) {
			url = Constants.CREC_URL + url;
		}
		log.info("调用接口(GET)："+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(Constants.CONNECT_TIME_OUT);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
//			headers.add("X-Consumer-key", X_Consumer_key);
//			headers.add("X-Auth-Token", X_Auth_Token);
			HttpEntity httpEntity = new HttpEntity(headers);
			
//			log.debug("X-Consumer-key:"+X_Consumer_key);
//			log.debug("X-Auth-Token:"+X_Auth_Token);
			
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.GET, httpEntity, responseType, urlVariables);
//			log.debug("调用接口返回状态码(GET):"+responseEntity.getStatusCode().value());
			if (responseEntity.getStatusCode().value() == 401) {
				log.debug("token认证失效");
				//TODO 重新获取token;
				resetPlatToken();
				//重新调用该方法
				get(url, responseType, urlVariables);
			} else if (responseEntity.getStatusCode().value() >= 400) {
				log.error("调用接口(GET)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new CRECRestException(responseEntity.getStatusCode().value());
			} else {
				//将返回结果转换为指定对象
				log.debug("调用接口(GET)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回状态码(GET):"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new CRECRestException(e.getStatusCode().value(), e.getStatusText());
		}finally{
			if (factory != null){
				try {
					factory.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return t;
	}

	/**
	 * Retrieve a representation by doing a GET on the specified URL.
	 * The response (if any) is converted and returned.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * @param url the URL
	 * @param request the Object to be POSTed, may be {@code null}
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand the template
	 * @return the converted object
	 * @throws Exception 
	 */
	public static <T> T get(String url,Object request,  Class<T> responseType, Object... urlVariables) throws CRECRestException {
		
		if (!url.toLowerCase().startsWith("http")) {
			url = Constants.CREC_URL + url;
		}
		log.info("调用接口(GET)："+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(Constants.CONNECT_TIME_OUT);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
//			headers.add("X-Consumer-key", X_Consumer_key);
//			headers.add("X-Auth-Token", X_Auth_Token);
			HttpEntity httpEntity = new HttpEntity(request,headers);
			
//			log.debug("X-Consumer-key:"+X_Consumer_key);
//			log.debug("X-Auth-Token:"+X_Auth_Token);
			
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.GET, httpEntity, responseType, urlVariables);
//			log.debug("调用接口返回状态码(GET):"+responseEntity.getStatusCode().value());
			if (responseEntity.getStatusCode().value() == 401) {
				log.debug("token认证失效");
				//TODO 重新获取token;
				resetPlatToken();
				//重新调用该方法
				get(url,request, responseType, urlVariables);
			} else if (responseEntity.getStatusCode().value() >= 400) {
				log.error("调用接口(GET)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new CRECRestException(responseEntity.getStatusCode().value());
			} else {
				//将返回结果转换为指定对象
				log.debug("调用接口(GET)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回状态码(GET):"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new CRECRestException(e.getStatusCode().value(), e.getStatusText());
		}finally{
			if (factory != null){
				try {
					factory.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return t;
	}
	
	/**
	 * Create a new resource by POSTing the given object to the URI template,
	 * and returns the representation found in the response.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * <p>The {@code request} parameter can be a {@link HttpEntity} in order to
	 * add additional HTTP headers to the request.
	 * @param url the URL
	 * @param request the Object to be POSTed, may be {@code null}
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand the template
	 * @return the converted object
	 * @see HttpEntity
	 */
	public static <T> T post(String url, Object request, Class<T> responseType, Object... urlVariables) throws CRECRestException {
		if (!url.toLowerCase().startsWith("http")) {
			url = Constants.CREC_URL + url;
		}
		log.debug("调用接口(POST):"+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(Constants.CONNECT_TIME_OUT);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
//			headers.add("X-Consumer-key", X_Consumer_key);
//			headers.add("X-Auth-Token", X_Auth_Token);
			HttpEntity httpEntity = new HttpEntity(request, headers);
			
//			log.debug("X-Consumer-key:"+X_Consumer_key);
//			log.debug("X-Auth-Token:"+X_Auth_Token);
			
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.POST, httpEntity, responseType, urlVariables);
			
//			log.debug("调用接口返回状态码(POST):"+responseEntity.getStatusCode().value());
			
			if (responseEntity.getStatusCode().value() == 401) {
				log.debug("token认证失效");
				//TODO 重新获取token;
				resetPlatToken();
				//重新调用该方法
				post(url, request, responseType, urlVariables);
			} else if (responseEntity.getStatusCode().value() >= 400) {
				log.error("调用接口(POST)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new CRECRestException(responseEntity.getStatusCode().value());
			} else {
				//将返回结果转换为指定对象
				log.debug("调用接口(POST)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回Post 状态码:"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new CRECRestException(e.getStatusCode().value(), e.getStatusText());
		} finally{
			if (factory != null){
				try {
					factory.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return t;
	}

	/**
	 * Delete the resources at the specified URI.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * @param url the URL
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand in the template
	 * @return the converted object
	 * @throws Exception 
	 */
	public static <T> T delete(String url, Object request, Class<T> responseType, Object... urlVariables) throws CRECRestException {
		if (!url.toLowerCase().startsWith("http")) {
			url = Constants.CREC_URL + url;
		}
		log.debug("调用接口(DELETE):"+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(Constants.CONNECT_TIME_OUT);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
//			headers.add("X-Consumer-key", X_Consumer_key);
//			headers.add("X-Auth-Token", X_Auth_Token);
			HttpEntity httpEntity = new HttpEntity(request, headers);
			
//			log.debug("X-Consumer-key:"+X_Consumer_key);
//			log.debug("X-Auth-Token:"+X_Auth_Token);
			
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.DELETE, httpEntity, responseType, urlVariables);
			
//			log.debug("调用接口返回状态码(DELETE):"+responseEntity.getStatusCode().value());
			
			if (responseEntity.getStatusCode().value() == 401) {
				log.debug("token认证失效");
				//TODO 重新获取token;
				resetPlatToken();
				//重新调用该方法
				delete(url, request, responseType, urlVariables);
			} else if (responseEntity.getStatusCode().value() >= 400) {
				log.error("调用接口(DELETE)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new CRECRestException(responseEntity.getStatusCode().value());
			} else {
				//将返回结果转换为指定对象
				log.debug("调用接口(DELETE)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回DELETE 状态码:"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new CRECRestException(e.getStatusCode().value(), e.getStatusText());
		} finally{
			if (factory != null){
				try {
					factory.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return t;
	}


	
	public static <T> T put(String url, Object request, Class<T> responseType, Object... urlVariables) throws CRECRestException {
		
		if (!url.toLowerCase().startsWith("http")) {
			url = Constants.CREC_URL + url;
		}
		log.debug("调用接口(PUT):"+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			HttpEntity<?> requestEntity = new HttpEntity<Object>(request);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
//			headers.add("X-Consumer-key", X_Consumer_key);
//			headers.add("X-Auth-Token", X_Auth_Token);
			HttpEntity httpEntity = new HttpEntity(request,headers);
			
//			log.debug("X-Consumer-key:"+X_Consumer_key);
//			log.debug("X-Auth-Token:"+X_Auth_Token);
			
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.PUT, httpEntity, responseType, urlVariables);
			
//			log.debug("调用接口返回状态码(PUT):"+responseEntity.getStatusCode().value());
			
			if (responseEntity.getStatusCode().value() == 401) {
				log.debug("token认证失效");
				//TODO 重新获取token;
				resetPlatToken();
				//重新调用该方法
				put(url, request,responseType, urlVariables);
			} else if (responseEntity.getStatusCode().value() >= 400) {
				log.error("调用接口(PUT)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new CRECRestException(responseEntity.getStatusCode().value());
			} else {
				//将返回结果转换为指定对象
				log.debug("调用接口(PUT)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回PUT 状态码:"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new CRECRestException(e.getStatusCode().value(), e.getStatusText());
		} finally{
			if (factory != null){
				try {
					factory.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return t;
	}
	
	public static void resetPlatToken(){
		String url = Constants.CREC_URL + "/auth/token?grant_type=authorization_code";
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		try {
			factory.setConnectTimeout(Constants.CONNECT_TIME_OUT);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
//			headers.add("X-Consumer-key", X_Consumer_key);
//			headers.add("X-Consumer-Secret", X_Consumer_Secret);
			
//			log.debug("X-Consumer-key:"+X_Consumer_key);
//			log.debug("X-Auth-Token:"+X_Auth_Token);
			
			HttpEntity httpEntity = new HttpEntity(headers);
			Map response = template.exchange(url, HttpMethod.GET, httpEntity, Map.class).getBody();
			log.debug("调用接口(获取token)请求发送成功    接口返回信息:"+response);
			X_Auth_Token = ((Map)response.get("token")).get("id")+"";
			
		} catch(Exception e){
			log.error("调用接口(GET)平台访问认证发生异常:", e);
		}finally{
			if (factory != null){
				try {
					factory.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
//	public static class HttpEntityEnclosingDeleteRequest extends HttpEntityEnclosingRequestBase {
//
//		public HttpEntityEnclosingDeleteRequest(final URI uri) {
//			super();
//			setURI(uri);
//		}
//
//		@Override
//		public String getMethod() {
//			return "DELETE";
//		}
//	}
	
	
	public static void main(String[] s) {
//		try {
//			//System.err.println(get("http://192.168.0.51:8889/v1/volumes", Map.class));
//			RestClientUtils.resetPlatToken();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		
//		http://10.1.191.135:7003/rail/template/TemplateSchemes
		Map map = new HashedMap();
		map.put("action", 1);
		map.put("source", 1);
		map.put("souceEntityId", "942be8cd-7df1-42c9-a5e0-3aaa82561a97");
		map.put("time", "2014-04-24");
		
		try {
			Map obj = RestClientUtils.post("/plan/Trainlines", map, Map.class);
			System.out.println("-----:"+obj);
		} catch (CRECRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
