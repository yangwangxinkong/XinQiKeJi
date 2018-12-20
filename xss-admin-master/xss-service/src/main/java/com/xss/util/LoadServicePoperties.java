package com.xss.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class LoadServicePoperties {
    private static final String application_poperties = "/application.properties";
    private static final String service_poperties = "/config/service%s.properties";
    public static Map<String, String> props;
    private static String active;

    static
    {
        // 先获取运行环境
        active = loadPopertiesMap(application_poperties).get("spring.profiles.active");
        System.out.println("读取application.properties配置文件！  当前环境："+active);
        // 拼接相应环境的properties文件路径
        String popertiesFileUrl = String.format(service_poperties, StringUtils.isBlank(active)? "": "-"+active);

        // 最终读取配置项Map
        props = loadPopertiesMap(popertiesFileUrl);
    }


    public static Map<String, String> loadPopertiesMap(String popertiesFileUrl){
        InputStream is = null;

        Properties prop = new Properties();
        Map map = new HashMap();
        try
        {
            is = new ClassPathResource(popertiesFileUrl).getInputStream();
            prop.load(is);

            Set set = prop.entrySet();

            Iterator it = set.iterator();
            String key = null; String value = null;

            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry)it.next();

                key = String.valueOf(entry.getKey());
                value = String.valueOf(entry.getValue());

                map.put(key, value);
            }
        }
        catch (IOException localIOException)
        {
            if (is != null)
                try {
                    is.close();
                }
                catch (IOException localIOException1)
                {
                }
        }
        catch (Exception localException)
        {
            if (is != null)
                try {
                    is.close();
                }
                catch (IOException localIOException2)
                {
                }
        }
        finally
        {
            if (is != null)
                try {
                    is.close();
                }
                catch (IOException localIOException3) {
                }
        }
        return Collections.unmodifiableMap(map);
    }


}
