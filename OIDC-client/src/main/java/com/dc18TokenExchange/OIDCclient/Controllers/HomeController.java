package com.dc18TokenExchange.OIDCclient.Controllers;


import com.dc18TokenExchange.OIDCclient.OpenIdConnectUserDetails;
import com.dc18TokenExchange.OIDCclient.ResourceGetterServices.GetWorkplaceResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;


@Controller
public class HomeController implements WebMvcConfigurer {

    @Autowired
    private GetWorkplaceResources getWorkplaceResources;


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
    public String getWorkplace(final Model model) throws IOException {
        OpenIdConnectUserDetails opid = (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int tempOrgNum = opid.getWorkplaceNum();

        String work_name = opid.getWorkplaceName();
        String first_name = opid.getFirstName();
        String workUrl = getWorkplaceResources.getHomeUrl(tempOrgNum);

        //Gets logo from resource server and converts it to bytes and encodes it with Base64
        BufferedImage workplace_logo = getWorkplaceResources.getWorkplaceImages(tempOrgNum, "logo");
        byte[] bytesLogo = getWorkplaceResources.getImageAsBytes(workplace_logo);
        String stringBase64Logo = Base64.getEncoder().encodeToString(bytesLogo);

        //Gets background from resource server and converts it to bytes and encodes it with Base64
        BufferedImage workplace_background = getWorkplaceResources.getWorkplaceImages(tempOrgNum, "background");
        byte[] bytesBackground = getWorkplaceResources.getImageAsBytes(workplace_background);
        String stringBase64Background = Base64.getEncoder().encodeToString(bytesBackground);

        //getWorkplaceResources.saveImageWithBytes("../../../resources/static/img/logo.png", workplace_logo);

        model.addAttribute("work_name", work_name);
        model.addAttribute("first_name", first_name);
        model.addAttribute("workplace_logo", stringBase64Logo);
        model.addAttribute("workplace_background", stringBase64Background);
        model.addAttribute("work_url", workUrl);

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