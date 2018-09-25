package com.mock.service;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HttpCallbackService {

    @Autowired
    private HttpForwardService hfs;
    @Autowired
    private AssembleBody ab;

    public void callBack(JSONArray ja) {
        for (Object jo : ja) {
            JSONObject json = JSONObject.fromObject(jo);
            callBack(json);
        }
    }

    public void callBack(JSONObject j) {
        log.info("=====start call back=====");
        final String url = j.getString("url");
        final String method = j.getString("method");
        final Map<String, String> header = j.get("header") == null ? new HashMap<>() : j.getJSONObject("header");
        final String datatype;
        if ("get".equalsIgnoreCase(method)) {
            datatype = "form";
        } else {
            datatype = j.get("datatype").toString();
        }

        if ("form".equalsIgnoreCase(datatype)) {
            header.put("Content-Type", "application/x-www-form-urlencoded");
        } else if ("xml".equalsIgnoreCase(datatype)) {
            header.put("Content-Type", "application/xml");
        } else {
            header.put("Content-Type", "application/json");
        }

        String ctn = ab.formatContent(j);
        final net.sf.json.JSONObject content = net.sf.json.JSONObject.fromObject(ctn);
        final int delay = j.get("delay") != null ? j.getInt("delay") : 0;

        log.debug("====callback method:{}, url:{}, header:{}, datatype:{}, content:{}=====", method, url, header, datatype,
                content);
        Thread t = new Thread(new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
            hfs.forward(method, url, header, datatype, content);
        }));
        t.start();
        log.info("=====call back finish=====");
    }
}
