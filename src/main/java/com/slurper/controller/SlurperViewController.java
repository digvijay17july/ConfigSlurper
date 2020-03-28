package com.slurper.controller;

import com.slurper.service.ConfigSlurperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(path = "/api/v1/view")
public class SlurperViewController {
    @Autowired
    ConfigSlurperService configSlurperServiceImpl;

    @RequestMapping("/configurations")
    @ResponseBody
    public ModelAndView viewAndEditProperties() {
        ModelAndView modelAndView = new ModelAndView();
        Map properties = null;
        try {
            properties = configSlurperServiceImpl.getConfigurations(true);
            modelAndView.addObject("properties", properties);
            modelAndView.setViewName("loadProperties");
        } catch (Exception e) {
            modelAndView.addObject("exception", e);
            modelAndView.setViewName("exception");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/addProperties", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public ModelAndView addProperties(@RequestBody MultiValueMap<String, String> properties) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            configSlurperServiceImpl.writeConfigurations(properties);
            modelAndView.setViewName("redirect:/api/v1/view/configurations");
        } catch (Exception e) {
            modelAndView.addObject("exception", e);
            modelAndView.setViewName("exception");
        }
        return modelAndView;
    }
}
