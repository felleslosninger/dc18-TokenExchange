package com.dc18TokenExchange.OIDCclient;



import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;


@Controller
public class HomeController {
    @RequestMapping("/")
    @ResponseBody
    public String home() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Welcome! Personnummeret ditt er: " +username;
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/error").setViewName("error");
        //registry.addViewController("/login").setViewName("login");
    }
}