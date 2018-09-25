package com.mock.http;

import com.mock.cmn.util.CmnParseUtil;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HttpUtil {

    @SuppressWarnings("unchecked")
    public static HttpResponse execute(String URL, String method, Map<String, String> headers, String dataType, JSONObject ctn) {

        String content = formatContent(dataType, ctn);
        HttpResponse response = new HttpResponse();
        boolean IS_HTTPS = false;
        if (URL.contains("https://")) {
            IS_HTTPS = true;
        }

        HttpRequest request = null;
        switch (method.toUpperCase()) {
            case "GET":
                if(!URL.contains("?") && content !=null){
                    URL = URL + "?" + content;
                    content = null;
                }
                request = HttpRequest.get(URL);
                break;
            case "POST":
                request = HttpRequest.post(URL);
                break;
            case "PUT":
                request = HttpRequest.put(URL);
                break;
        }

        if(request == null){
            return null;
        }
        log.debug("===url:{} ====method:{} ====content:{}====",URL, method, content);

        if (headers != null) {
            request.headers(headers);
        }

        if (IS_HTTPS) {
            request.trustAllCerts();
            request.trustAllHosts();
        }

        if (content != null){
            request.send(content.getBytes(StandardCharsets.UTF_8));
        }

        Map<String, List<String>> h = request.headers();
        response.setHeader(h);
        response.setContent(request.body());
        log.debug("[{} {}]{} {} ----body--- {}", response.Status(), response.Reason(), method, URL, response.Content());
        if (response.Status() != 200 && response.Status() != 201) {
            log.error("RESPONSE {} ", response.Content());
        }
        return response;

    }

    private static String formatContent(String dataType, JSONObject content){
        dataType = dataType.toLowerCase();
        if(content == null || content.isEmpty()){
            return null;
        }
        if(dataType.contains("x-www-form-urlencoded")||"form".equals(dataType)){
            return CmnParseUtil.JsonToForm(content);
        }else if(dataType.contains("xml")){
            try {
                XMLSerializer xmlserializer = new XMLSerializer();
                xmlserializer.setRootName("xml");
                return xmlserializer.write(content);
            } catch (Exception e) {
                log.error("transfer to xml failed: {}", ExceptionUtils.getFullStackTrace(e));
                return content.toString();
            }
        }else if(dataType.contains("json")){
            return content.toString();
        }
        return content.toString();
    }
}
