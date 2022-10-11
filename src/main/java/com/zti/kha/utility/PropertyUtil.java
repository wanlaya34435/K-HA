package com.zti.kha.utility;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Windows 8.1 on 30/10/2561.
 */
public class PropertyUtil {
    private static final Logger log = LogManager.getLogger(PropertyUtil.class);
    private static final ConcurrentHashMap<String, String> hash = new ConcurrentHashMap<String, String>();

    /**
     * Load a properties file from the classpath
     *
     * @param propsName
     * @return Properties
     * @throws Exception
     */
    public static Properties load(String propsName) throws Exception {
        InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(propsName);
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        return p;
    }

    public static String getProperty(String fileName, String key) {
        String value = "";
        if (hash.containsKey(key)) {
            value = hash.get(key);
        } else {
            InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName);
            Properties p = new Properties();
            try {
                p.load(is);
                value = p.getProperty(key);
                hash.put(key, value);
            } catch (Exception e) {
//                log.error(e.getMessage(), e);
            }
            return value;
        }
        return value;
    }

    public static String getProperty(String fileName, String key,
                                     String defaultValue) {
        InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName);
        Properties p = new Properties();

        try {

            p.load(is);

        } catch (Exception e) {
//            log.error(e.getMessage(), e);
        }
        return (p.getProperty(key, defaultValue));
    }

    public static HashMap<String, String> parseToMap(String propFile) {
        InputStream is = PropertyUtil.class.getClassLoader()
                .getResourceAsStream(propFile);
        Properties p = new Properties();
        HashMap<String, String> propMap = null;
        try {

            p.load(is);
            propMap = new HashMap<String, String>((Map) p);
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
        }
        return propMap;
    }

    public static Set<?> getKeys(String propFile) {
        InputStream is = PropertyUtil.class.getClassLoader()
                .getResourceAsStream(propFile);
        Properties p = new Properties();
        Set<?> propMap = null;
        try {
            p.load(is);
            propMap = p.keySet();
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
        }
        return propMap;
    }
}