package com.shsxt.crm.controller;

import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @RequestMapping("index")
    public String index(HttpServletRequest request){
        System.out.println(request.getContextPath());
        request.setAttribute("ctx",request.getContextPath());
        return "index";
    }
}
