package com.slurper.service;

import groovy.lang.Closure;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;

@Service
public class ConfigSlurperServiceImpl implements ConfigSlurperService {
    @Override
    public Map getConfigurations(boolean closureAsText) throws Exception {
        String bdata = getConfig();
        Map propertiesMap = null;
        propertiesMap = new HashMap();
        ConfigSlurper configSlurper = new ConfigSlurper();
        ConfigObject configObject = configSlurper.parse(bdata);
        if (closureAsText) {
            getPropertiesAsMap(propertiesMap);
        } else {
            Set set = configObject.keySet();
            Closure closure = null;
            Object value = null;
            for (Object key : set) {
                value = configObject.get(key);
                if (value instanceof Closure) {
                    closure = (Closure) value;
                    propertiesMap.put(key, closure.call());
                } else {
                    propertiesMap.put(key, value);
                }
            }
        }
        return propertiesMap;
    }

    @Override
    public void writeConfigurations(Map newPropertiesMap) throws Exception {
        Map propertiesMap=new HashMap();
        propertiesMap.put(((List)newPropertiesMap.get("key")).get(0),((List)newPropertiesMap.get("value")).get(0));
    writeConfig(propertiesMap);
    }


    private String getConfig() throws IOException {
        InputStream in = getInputStream();
        Reader reader = new InputStreamReader(in);
        String scriptData = FileCopyUtils.copyToString(reader);
        reader.close();
        in.close();
        return scriptData;
    }

    private InputStream getInputStream() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:config.groovy");
        return new FileInputStream(file);
    }

    private void getPropertiesAsMap(Map propertiesMap) throws IOException {
        Properties prop = new Properties();
        prop.load(getInputStream());
        for (Object value : prop.keySet()) {
            propertiesMap.put(value, prop.getProperty((String) value));
        }
    }
    private void writeConfig(Map<Object,Object> newProperties) throws IOException {
        StringBuilder line=new StringBuilder(getConfig());
        OutputStream in = getOutputStream();
        Writer writer = new OutputStreamWriter(in);
        for (Map.Entry<Object, Object> entry : newProperties.entrySet())
        {
            line.append("\n");
            line.append(entry.getKey()+"="+entry.getValue());
            writer.write(line.toString());
        }
        writer.flush();
        writer.close();
        in.close();
    }
    private OutputStream getOutputStream() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:config.groovy");
        return new FileOutputStream(file);
    }
}
