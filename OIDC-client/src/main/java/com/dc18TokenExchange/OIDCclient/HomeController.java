package com.dc18TokenExchange.OIDCclient;



import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Controller
public class HomeController {
    @RequestMapping("/")
    @ResponseBody
    public String home(@CookieValue(value = "token", defaultValue = "0") String token,
                       HttpServletResponse response) {

        token = OpenIdConnectUserDetails.getAT();

        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
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