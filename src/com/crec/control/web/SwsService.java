package com.crec.control.web; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller  
@RequestMapping("/welcome") 
public class SwsService { 
	
	@Autowired
	private SimpMessageSendingOperations temple;
	
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
         return getGreeting();
   }

    public String getGreeting() {
        return "Hello, you are in!";
    }
    @RequestMapping(value="/hello",method = RequestMethod.GET)  
    public String printWelcome(ModelMap model) {   
        model.addAttribute("message", "Spring 3 MVC Hello World");  
        return "dashboard";  
    } 
    
    @RequestMapping(value="/dashboard",method = RequestMethod.GET)  
    public String dashboard(ModelMap model) {  
    	 this.temple.convertAndSend("/topic/price.stock.Train", "afadfadfafafafafdafa");
        model.addAttribute("message", "Spring 3 MVC Hello World");  
        return "dashboard";  
    }  

} 
