package com.slurper.service;

import groovy.util.ConfigObject;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface ConfigSlurperService {

    /*
    Method to get List of Configurations
     */
    Map getConfigurations(boolean closureAsText) throws Exception;

    /*
     Method to write configurations
      */
    void writeConfigurations(Map newPropertiesMap) throws Exception;
}
