package com.mock.cmn.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CmnParseUtil {

    public static Map<String, Object> parseObjectStr2Map(String jsonStr){
        if(jsonStr == null){
            return null;
        }
        JSONObject json = JSONObject.fromObject(jsonStr);
        return parseJsonobj2Map(json);
    }

    public static Map<String, Object> parseJsonobj2Map(JSONObject json){
        if(json == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        for(Object k : json.keySet()){
            Object v = json.get(k);
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<>();
                for (Object json2 : ((JSONArray) v)) {
                    list.add(parseObjectStr2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    public static String JsonStrToForm(String jsonStr){
        if(jsonStr == null){
            return "";
        }
        JSONObject json = JSONObject.fromObject(jsonStr);
        return JsonToForm(json);
    }

    public static String JsonToForm(JSONObject job) {
        if(job == null){
            return "";
        }
        StringBuilder FormStr = new StringBuilder();
        @SuppressWarnings("unchecked")
        Iterator<String> iterator = job.keys();
        while(iterator.hasNext()){
            String key = iterator.next();
            FormStr.append(key).append("=").append(job.get(key)).append("&");
        }
        return FormStr.substring(0, FormStr.length()-1);
    }

    public static String BeanToJson(Object object) throws JsonProcessingException {
        if(object == null){
            return "";
        }
        ObjectMapper m = new ObjectMapper();
        m.setSerializationInclusion(Include.NON_NULL);
        return m.writeValueAsString(object);
    }

    public static String MapToForm(Map<String, Object> maps) {
        if (maps == null){
            return "";
        }
        StringBuilder FormStr = new StringBuilder();
        for (String varKey : maps.keySet()) {
            String varValue = (String)maps.get(varKey);
            FormStr.append(varKey).append("=").append(varValue).append("&");
        }
        String rs = FormStr.toString();
        return rs.substring(0, rs.length()-1);
    }
}
