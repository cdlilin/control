package com.crec.control.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crec.control.engine.MessageHandler;
import com.crec.control.service.CalendarService;

/**
 * Created by star on 4/14/14.
 */
@Controller
@RequestMapping("/message")
public class MessageController {
    private static final Log logger = LogFactory.getLog(MessageController.class);

//    @Autowired
    private CalendarService calendarService;

    @RequestMapping(method=RequestMethod.POST) 
    public String onMessage(HttpServletRequest request,HttpServletResponse response,Principal principal) throws Exception {
//        logger.debug("Positions for " + principal.getName());
        System.out.println("-----------onMessage----------" );
       // MessageHandler.onMessage("{}");
        return "json";
    }

	public CalendarService getCalendarService() {
		return calendarService;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
}
