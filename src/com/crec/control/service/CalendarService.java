package com.crec.control.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {
	
	@Autowired
	private SimpMessageSendingOperations stemp; 
	
	public SimpMessageSendingOperations getStemp() {
		return stemp;
	}

	public void setStemp(SimpMessageSendingOperations stemp) {
		this.stemp = stemp;
	}

//	@Scheduled(fixedDelay=10000)
	public void sendMessage(){
		System.out.println("------------------------");
		System.out.println(this.stemp == null);
		System.out.println("------------------------");
//		this.stemp.convertAndSend("/topic/test", "");
	}
	 
	
	
	

}
