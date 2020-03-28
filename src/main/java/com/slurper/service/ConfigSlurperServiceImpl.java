package com.slurper.service;

import com.slurper.utils.SlurperProperties;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;

@Service
public class ConfigSlurperServiceImpl implements ConfigSlurperService {
    @Autowired
    SlurperProperties slurperProperties;
    private ConfigSlurper configSlurper = new ConfigSlurper();

    @Override
    public Map getConfigurations(boolean closureAsText) throws Exception {
        String bdata = getConfig();
        Map propertiesMap = null;
        propertiesMap = new HashMap();
        ConfigObject configObject = this.configSlurper.parse(bdata);
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
        Map propertiesMap = null;
        if (newPropertiesMap == null) {
            throw new Exception("No New Configuration Added");
        }
        List keys = (List) newPropertiesMap.get("key");
        List values = (List) newPropertiesMap.get("value");

        if (keys == null || values == null || keys.size() == 0 || values.size() == 0) {
            throw new Exception("Error in getting Configurations Or Invalid Configuration Entered");
        }
        Object key, value;
        GroovyShell groovyShell = new GroovyShell();
        propertiesMap = new HashMap();
        String tmpString = null;
        for (int i = 0; i < keys.size(); i++) {
            key = keys.get(i);
            value = values.get(i);
            if (key == null || value == null || key.equals("") || value.equals("")) {
                throw new Exception("Invalid Configurations");
            }
            try {
                tmpString = key + "=" + value;
                groovyShell.evaluate(tmpString);
                propertiesMap.put(key, value);
            } catch (Exception ex) {
                throw new Exception("Invalid Closure or Add single quotes to value!!", ex);
            }

        }
        writeConfig(propertiesMap);
    }

    @Override
    public Closure getClosure(String closureName) throws Exception {
        String bdata = getConfig();
        Map propertiesMap = null;
        propertiesMap = new HashMap();
        ConfigObject configObject = this.configSlurper.parse(bdata);
        Closure closure= (Closure) configObject.get(closureName);
        return closure;
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
        File file = ResourceUtils.getFile(slurperProperties.getFileLocation());
        return new FileInputStream(file);
    }

    private void getPropertiesAsMap(Map propertiesMap) throws IOException {
        Properties prop = new Properties();
        prop.load(getInputStream());
        for (Object value : prop.keySet()) {
            propertiesMap.put(value, prop.getProperty((String) value));
        }
    }

    private void writeConfig(Map<Object, Object> newProperties) throws IOException {
        StringBuilder line = new StringBuilder();
        OutputStream in = getOutputStream();
        Writer writer = new OutputStreamWriter(in);
        for (Map.Entry<Object, Object> entry : newProperties.entrySet()) {
            line.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        writer.write(line.toString());
        writer.flush();
        writer.close();
        in.close();
    }

    private OutputStream getOutputStream() throws FileNotFoundException {
        File file = ResourceUtils.getFile(slurperProperties.getFileLocation());
        return new FileOutputStream(file);
    }
}
