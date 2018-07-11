package com.dc18TokenExchange.OIDCclient.Controllers;


import com.dc18TokenExchange.OIDCclient.OpenIdConnectUserDetails;
import com.dc18TokenExchange.OIDCclient.ResourceGetterServices.GetWorkplaceResources;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.image.BufferedImage;
import java.io.IOException;


@Controller
public class HomeController implements WebMvcConfigurer {

    @Autowired
    private GetWorkplaceResources getWorkplaceResources;


    @GetMapping(value = {"/", "/home"})
    public String getHome(final Model model){

        if((SecurityContextHolder.getContext().getAuthentication() != null) && !(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_ANONYMOUS"))){
            OpenIdConnectUserDetails opid = (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String first_name = opid.getFirstName();
            model.addAttribute("first_name", first_name);

            return "home";
        }
        else{
            return "home";
        }
    }

    @GetMapping("/workplace")
    public String getWorkplace(final Model model) throws IOException {
        OpenIdConnectUserDetails opid = (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String work_name = opid.getWorkplaceName();
        String first_name = opid.getFirstName();
        BufferedImage workplace_logo = getWorkplaceResources.getWorkplaceLogo(opid.getWorkplaceNum());

        getWorkplaceResources.saveImageWithBytes("../../../resources/static/img/logo.png", workplace_logo);

        model.addAttribute("work_name", work_name);
        model.addAttribute("first_name", first_name);
        model.addAttribute("workplace_logo", "/img/logo.png");

        return "workplace";
    }

    @GetMapping("/workplace/logo")
    public BufferedImage getWorkplaceImage() throws IOException{
        OpenIdConnectUserDetails opid = (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getWorkplaceResources.getWorkplaceLogo(opid.getWorkplaceNum());
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