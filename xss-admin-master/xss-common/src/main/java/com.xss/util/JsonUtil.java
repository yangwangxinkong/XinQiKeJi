package com.xss.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author zzl
 * @since on 2018/5/15.
 */
public class JsonUtil {
    private static Gson gson = null;

    static{
        gson  = new Gson();
    }

    public static synchronized Gson newInstance(){
        if(gson == null){
            gson =  new Gson();
        }
        return gson;
    }

    public static String getJsonString(Object obj){
        return gson.toJson(obj);
    }

    public static <T> T toBean(String json,Class<T> clz){

        return gson.fromJson(json, clz);
    }

    public static <T> Map<String, T> readJson2MapObj(String json,Class<T> clz){
        Map<String, JsonObject> map = gson.fromJson(json, new TypeToken<Map<String,JsonObject>>(){}.getType());
        Map<String, T> result = new HashMap<>();
        for(String key:map.keySet()){
            result.put(key,gson.fromJson(map.get(key),clz) );
        }
        return result;
    }

    public static <T> T json2Obj(String json,Class<T> clz){
        return gson.fromJson(json,clz);
    }

    public static Map<String, Object> toMap(String json){
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String,Object>>(){}.getType());
        return map;
    }

    public static Map<String,String> readJsonStrMap(String json) {
        Map<String, JsonObject> map = gson.fromJson(json, new TypeToken<Map<String,JsonObject>>(){}.getType());
        Map<String,String> result = new HashMap<>();
        for(String key:map.keySet()){
            result.put(key,gson.fromJson(map.get(key),String.class) );
        }
        return result;
    }

    public static  Map<byte[], byte[]> readJsonByteMap(String json) {
        Map<String, JsonPrimitive> map = gson.fromJson(json, new TypeToken<Map<String,JsonPrimitive>>(){}.getType());
        Map<byte[], byte[]> vmap = new HashMap<>();
        for(String key:map.keySet()){
            vmap.put(key.getBytes(),gson.fromJson(map.get(key),String.class).getBytes() );
        }
        return vmap;

    }


    public static <T> List<T> readJson2Array(String json, Class<T> clz){
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        List<T> list  = new ArrayList<>();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, (Type)clz));
        }
        return list;
    }

    /**
     * 对象转为JSONObject
     *
     * @param from
     * @param to
     * @param properties
     * @return
     */
    public static JSONObject toJSONObject(Object from, JSONObject to, String[] properties) {
        List<String> propertyList = Arrays.asList(properties);
        PropertyDescriptor[] fromPds = BeanUtils.getPropertyDescriptors(from.getClass());

        for (PropertyDescriptor fromPd : fromPds) {
            if (propertyList.contains(fromPd.getName())) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(from.getClass(), fromPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(from);
                        if (value == null) {
                            if (Number.class.isAssignableFrom(readMethod.getReturnType())) {
                                to.put(fromPd.getName(), 0);
                            } else if (readMethod.getReturnType().isAssignableFrom(String.class)) {
                                to.put(fromPd.getName(), "");
                            }  else if (readMethod.getReturnType().isAssignableFrom(Boolean.class)) {
                                to.put(fromPd.getName(), false);
                            }
                        } else {
                            to.put(fromPd.getName(), value);
                        }

                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
        return to;
    }

    public static JSONObject toJSONObject(Object from, String[] properties) {
        JSONObject jo = new JSONObject();
        return toJSONObject(from, jo, properties);
    }

    public static JSONArray toJSONArray(Collection<?> collection, JSONArray ja, String[] properties) {
        for (Object obj : collection) {
            ja.add(toJSONObject(obj, properties));
        }
        return ja;
    }

    public static JSONArray toJSONArray(Collection<?> collection, String[] properties) {
        JSONArray ja = new JSONArray();
        return toJSONArray(collection, ja, properties);
    }

    /**
     * 将json字符串转为Map
     *
     * @param json
     */
    public static Map json2Map(String json) {
        LinkedMap map = new LinkedMap();
        net.sf.json.JSONObject js = net.sf.json.JSONObject.fromObject(json);
        populate(js, map);
        return map;
    }

    private static Map populate(net.sf.json.JSONObject jsonObject, Map map) {
        for (Iterator iterator = jsonObject.entrySet().iterator(); iterator
                .hasNext();) {
            String entryStr = String.valueOf(iterator.next());
            String key = entryStr.substring(0, entryStr.indexOf("="));
            String value = entryStr.substring(entryStr.indexOf("=") + 1,
                    entryStr.length());
            if (jsonObject.get(key).getClass().equals(net.sf.json.JSONObject.class)) {
                HashMap _map = new HashMap();
                map.put(key, _map);
                populate(jsonObject.getJSONObject(key), ((Map) (_map)));
            } else if (jsonObject.get(key).getClass().equals(net.sf.json.JSONArray.class)) {
                ArrayList list = new ArrayList();
                map.put(key, list);
                populateArray(jsonObject.getJSONArray(key), list);
            } else {
                map.put(key, jsonObject.get(key));
            }
        }
        return map;
    }

    private static void populateArray(net.sf.json.JSONArray jsonArray, List list) {
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).getClass().equals(net.sf.json.JSONArray.class)) {
                ArrayList _list = new ArrayList();
                list.add(_list);
                populateArray(jsonArray.getJSONArray(i), _list);
            } else if (jsonArray.get(i).getClass().equals(net.sf.json.JSONObject.class)) {
                HashMap _map = new HashMap();
                list.add(_map);
                populate(jsonArray.getJSONObject(i), _map);
            } else {
                list.add(jsonArray.get(i));
            }
        }
    }
}