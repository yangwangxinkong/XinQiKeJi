package com.xss.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by fan.hu on 2018/8/17.
 */

public final class LoadPopertiesFile {
    private static final String PROP = "/common.properties";
    public static final Map<String, String> PROP_MAP;

    static
    {
        InputStream is = null;

        Properties prop = new Properties();
        Map map = new HashMap();
        try
        {
            is = new ClassPathResource(PROP).getInputStream();
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
        PROP_MAP = Collections.unmodifiableMap(map);
    }
}
