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
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class OidcController implements WebMvcConfigurer {

    @RequestMapping("/hello")
    @ResponseBody
    public String welcome() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Welcome, " + username;
    }

    @RequestMapping(value="/welcome", params={"code","state"})
    @ResponseBody
    public String getAT(@RequestParam("code") String codeNum, @RequestParam("state") String stateNum) throws Exception {

        System.out.println("dette er koden: " +codeNum);
        OidcPostRequest or = new OidcPostRequest(codeNum);
        or.sendPost();

        /*
      foreslår å putte metodekall via at man trykker på knapp eller redirectes ellernoe,
      fra netverksdelen av nettsiden ser det ikke ut som om det noen gang blir sendt en post request
      ikke så rart kanksj med tanke på error 400, men da kansje vi ser litt tydeligere
         */
        return "Herro!";
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");
        //registry.addViewController("/login").setViewName("login");
    }

}
