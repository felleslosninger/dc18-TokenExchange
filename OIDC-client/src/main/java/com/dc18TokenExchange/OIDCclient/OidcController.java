package com.dc18TokenExchange.OIDCclient;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class OidcController implements WebMvcConfigurer {

    @RequestMapping("/loggedIn")
    @ResponseBody
    public String home() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Welcome, " + username;
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("login");
    }
}