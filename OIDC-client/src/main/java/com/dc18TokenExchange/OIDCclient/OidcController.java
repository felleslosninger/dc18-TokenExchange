package com.dc18TokenExchange.OIDCclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class OidcController implements WebMvcConfigurer {

    @RequestMapping("/hello")
    @ResponseBody
    public String welcome() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Welcome, " + username;
    }

    @RequestMapping(value="/welcome", params={"code","state"}, method = GET)
    @ResponseBody
    public String getAT(@RequestParam("code") String codeNum, @RequestParam("state") String stateNum) throws IOException {

        OidcTokenRetrieval otr = new OidcTokenRetrieval(codeNum);

        otr.sendTokenReq(codeNum);

        return "Herro!";
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");
        //registry.addViewController("/login").setViewName("login");
    }

}
