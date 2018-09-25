package com.mock.controller;

import com.mock.bean.MockResult;
import com.mock.bean.RequestBean;
import com.mock.http.HttpResponse;
import com.mock.service.HttpForwardService;
import com.mock.service.MockConfService;
import com.mock.service.MockStrategyService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class MockController {

    @Autowired
    private HttpForwardService hfs;
    @Autowired
    private MockConfService mcs;
    @Autowired
    private MockStrategyService mss;

    @RequestMapping(value = "/{namespace:(?!static)(?!index\\.html).*}/**")
    public void getRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        log.info("===@@@===receive request===@@@===");
        int responseStatus = 200;
        String ContentType;
        String requestUrl = httpRequest.getRequestURL().toString();
        String requestType = httpRequest.getMethod();

        if("GET".equals(requestType.toUpperCase()) || httpRequest.getContentType() == null) {
            ContentType = "application/x-www-form-urlencoded";
        } else {
            ContentType = httpRequest.getContentType().toLowerCase();
        }

        log.debug("===@@@===in RequestURL	:	{}===@@@===", requestUrl);
        log.debug("===@@@===in content type	:	{}===@@@===", ContentType);
        log.debug("===@@@===in forward-by-mock	:	{}===@@@===", httpRequest.getHeader("forward-by-mock"));

        RequestBean requestBean = new RequestBean(httpRequest);

        try {
            //check sys config
            if(!mcs.checkMockStatus()){
                log.info("===@@@===mock is off, start forward===@@@===");
                if(hfs.checkForward(requestUrl, requestBean.getHeader())){
                    forward(requestType,requestUrl,requestBean.getHeader(),requestBean.getContentType(),requestBean.getContent(),httpResponse);
                    return;
                }
                log.info("===@@@===request to mockserver, no need to forward===@@@===");
            }

            Map<String, Object> responseMap = mss.execStrategy(requestBean);

            if (responseMap == null) {
                if(hfs.checkForward(requestUrl, requestBean.getHeader())){
                    forward(requestType,requestUrl,requestBean.getHeader(),requestBean.getContentType(),requestBean.getContent(),httpResponse);
                    return;
                }
                response(httpResponse, responseStatus, new MockResult(MockResult.NOTFOUND).toString(), null);
                return;
            }
            Map<String,String> header = new HashMap<>();
            header.put("Content-Type", responseMap.get("contenttype").toString());
            response(httpResponse,(Integer) responseMap.get("status"),responseMap.get("content").toString(), header);
        } catch (Exception e){
            log.error(ExceptionUtils.getFullStackTrace(e));
            response(httpResponse, responseStatus, new MockResult(MockResult.ERROR).toString(), null);
        }
    }

    private void forward(String method, String url, Map<String, String> headers, String contentType, Object content, HttpServletResponse httpResponse)
            throws IOException {
        HttpResponse res = hfs.forward(method, url, headers, contentType, content);
        if(res==null){
            log.info("===@@@===forward failed===@@@===");
            response(httpResponse,200,new MockResult(MockResult.NOTFOUND).toString(),null);
        }else{
            log.info("===@@@===forward end===@@@===");
            response(httpResponse,res.Status(),res.Content(),res.getHeaders());
        }
    }

    private void setHeader(HttpServletResponse httpResponse, Map<String, String> header){
        for(String key : header.keySet()){
            httpResponse.setHeader(key, header.get(key));
        }
    }

    private void response(HttpServletResponse httpResponse, int responseStatus, String responseContent, Map<String, String> headers)
            throws IOException {
        httpResponse.setStatus(responseStatus);
        if(headers != null) {
            setHeader(httpResponse, headers);
        }
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.getWriter().print(responseContent);
        httpResponse.getWriter().flush();
    }
}
