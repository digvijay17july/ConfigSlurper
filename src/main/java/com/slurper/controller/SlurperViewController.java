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
    public ModelAndView viewAndEditProperties() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Map properties = null;
        properties = configSlurperServiceImpl.getConfigurations(true);
        modelAndView.addObject("properties", properties);
        modelAndView.setViewName("loadProperties");
        return modelAndView;
    }

    @RequestMapping(value = "/addProperties", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public ModelAndView addProperties(@RequestBody MultiValueMap<String, String> properties) throws Exception {
        configSlurperServiceImpl.writeConfigurations(properties);
        return new ModelAndView("redirect:/api/v1/view/configurations");
    }
}
