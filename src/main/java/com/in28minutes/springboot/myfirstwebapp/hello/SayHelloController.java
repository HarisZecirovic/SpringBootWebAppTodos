package com.in28minutes.springboot.myfirstwebapp.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//We have to tell spring that this is a Spring Bean
//Kako to radimo? Tako sto dodamo Anotation
// This bean is managing request and we will use @Controller
@Controller
public class SayHelloController {
	
	// Using @RequestMapping we are mapping a request URL to a method
	@RequestMapping("say-hello")
	// By default spring MVC is looking for a view when we return a string back, it will not return this string
	// It will be looking for a view 
	// zato dodajemo @ResponseBody
	// @ResponseBody will return whatever is returned by this message to the browser
	@ResponseBody
	public String sayHello() {
		return "Hello! What are you leaning today?";
	}
	
	@RequestMapping("say-hello-html")
	@ResponseBody
	public String sayHelloHtml() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title> My First HTML page </title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("My first html page with body");
		sb.append("</body>");
		sb.append("</html>");
		
		return sb.toString();
	}
	
	// /src/main/resources/META-INF/resources/WEB-INF/jsp/sayHello.jsp
	// Ovde nam ne treba @ResponseBody jer koristimo jsp, i ne bi moglo da radi ako bi koristili @ResponseBody onda bi samo
	// vracalo sayHello kao string 
	@RequestMapping("say-hello-jsp")
	public String sayHelloJsp() {
		return "sayHello";
	}

}
