package com.slurper.service;

import groovy.lang.Closure;

import java.util.Map;

public interface ConfigSlurperService {

    /*
    Method to get List of Configurations
     */
    Map getConfigurations(boolean closureAsText) throws Exception;

    /*
     Method to write configurations
      */
    void writeConfigurations(Map newPropertiesMap) throws Exception;

    /*
  Method to get closure
   */
    Closure getClosure(String closureName) throws Exception;
    
}
