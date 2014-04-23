package com.crec.control.utile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Table.Cell;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public class RestClient {

	private static List<Cell> getCount() {
		List<Cell> list = new ArrayList<Cell>();
		ObjectMapper objectMapper = new ObjectMapper();
		LocalDate now = LocalDate.now();
		Map<String, String> params = new HashMap<String, String>();
//		params.put("sourceTime", now.toString("yyyy-MM-dd") + " 00:00:00");
//		params.put("targetTime", now.plusDays(40).toString("yyyy-MM-dd")
//				+ " 00:00:00");
//		params.put("code", "01");
//		params.put("timeFormat", "yyyy-MM-dd hh:mm:ss");
		
		params.put("action", "1");
		params.put("source", "1");
		params.put("sourceEntityId", "942be8cd-7df1-42c9-a5e0-3aaa82561a97");
		params.put("time", "2014-04-24");
		// groupByDay:false

		Client client = Client.create();

		WebResource webResource = client
				.resource("http://10.1.191.135:7003/rail/plan/Trainlines");

		String values = null;
		try {
			values = objectMapper.writeValueAsString(params);
			System.out.println(values);
			
			ClientResponse response = webResource.type("application/json")
					.accept("application/json").method("POST", ClientResponse.class, values);
			
			System.out.println(response.getEntity(String.class));
			 

//			ClientResponse response = webResource.type("application/json")
//					.accept("application/json")
//					.post(ClientResponse.class, values);
//
//			if (response.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : "
//						+ response.getStatus());
//			}

//			String resp = response.getEntity(String.class);
//
//			Map<String, Object> result = objectMapper
//					.readValue(resp, Map.class);
//
//			List<Map<String, Object>> data = (List<Map<String, Object>>) result
//					.get("data");
			
//			System.out.println(data.size());

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void main(String[] args) {
		getCount();
	}

}
