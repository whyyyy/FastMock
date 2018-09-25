package com.mock.service;

import com.mock.http.HttpUtil;
import com.mock.http.HttpResponse;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HttpForwardService {

    private Map<String, Boolean> exceptLst;

    HttpForwardService() {

        exceptLst = new HashMap<>();
        exceptLst.put("localhost", false);
        exceptLst.put("127.0.0.1", false);
        exceptLst.put("::1", false);
        try {
            String localIp = InetAddress.getLocalHost().getHostAddress();
            log.debug("local ip is {}", localIp);
            exceptLst.put(localIp, false);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    public HttpResponse forward(String method, String url, Map<String, String> headers, String contentType,
            Object content) {

        HttpResponse res = null;

        log.info("===@@@===forward-------start===@@@===");
        log.debug("===@@@===method:{} ====url:{}====ctntype:{}====content:{}====",method,url,contentType,content);
        headers.put("forward-by-mock", "1");

        try {
            res = HttpUtil.execute(url, method, headers, contentType, JSONObject.fromObject(content));
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }

        return res;
    }

    public boolean checkForward(String url, Map<String, String> headers) {

        if (headers.containsKey("forward-by-mock")) {
            log.debug("checkForward already forwarded by mock");
            return false;
        }
        Pattern p = Pattern.compile("http[s]?://([\\w.-]*)?:?(\\d{4})?/?.*");
        Matcher m = p.matcher(url);
        if (m.find()) {
            log.debug("checkForward rex match, host is {}, port is {}", m.group(1), m.group(2));
            return m.group(2) == null && !exceptLst.containsKey(m.group(1));
        }
        log.debug("checkForward rex not match, url is {}", url);
        return false;
    }
}
