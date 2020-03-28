package com.slurper.controller;

import com.slurper.service.ConfigSlurperService;
import groovy.util.ConfigObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/slurper")
public class SlurperController {
    @Autowired
    ConfigSlurperService configSlurperServiceImpl;

    @GetMapping(path = "/configurations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map getConfiguration() throws Exception {
        return configSlurperServiceImpl.getConfigurations(false);
    }
}
