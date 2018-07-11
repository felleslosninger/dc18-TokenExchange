package com.dc18TokenExchange.OIDCclient.Controllers;


import com.dc18TokenExchange.OIDCclient.OpenIdConnectUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Controller
public class HomeController implements WebMvcConfigurer {


    @GetMapping(value = {"/", "/home"})
    public String getHome(final Model model){

        if((SecurityContextHolder.getContext().getAuthentication() != null) && !(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_ANONYMOUS"))) {
            OpenIdConnectUserDetails opid = (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String first_name = opid.getFirstName();
            model.addAttribute("first_name", first_name);
        }
        return "home";
        
    }

    @GetMapping("/workplace")
    public String getWorkplace(final Model model){
        OpenIdConnectUserDetails opid = (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String work_name = opid.getWorkplaceName();
        String first_name = opid.getFirstName();


        model.addAttribute("work_name", work_name);
        model.addAttribute("first_name", first_name);

        return "workplace";
    }



    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/home").setViewName("home");
        //registry.addViewController("/").setViewName("home");
        //registry.addViewController("/error").setViewName("error");
        //registry.addViewController("/workplace").setViewName("workplace");
        //registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logoutpage").setViewName("logoutpage");
    }
}