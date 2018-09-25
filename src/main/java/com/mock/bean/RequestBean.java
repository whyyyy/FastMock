package com.mock.bean;

import com.mock.cmn.util.CmnParseUtil;
import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.exception.ExceptionUtils;

@Slf4j
public class RequestBean {

    private Map<String,String> header;
    private Map<String,Object> content;
    private String contentType = "application/x-www-form-urlencoded";
    private String path;
    private String method;

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String,String> getHeader() {
        return header;
    }

    public Map<String,Object> getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public RequestBean(HttpServletRequest req) {
        this.header = new HashMap<>();
        this.path = req.getPathInfo() != null && req.getContextPath() != null ? req.getPathInfo() : req.getRequestURI();
        this.method = req.getMethod().toLowerCase();
        setHeader(req);
        setContent(req);
    }

    private void setHeader(HttpServletRequest req){
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerkey = headerNames.nextElement();
            this.header.put(headerkey, req.getHeader(headerkey));
        }
    }

    private void setContent(HttpServletRequest req) {

        if ("get".equalsIgnoreCase(req.getMethod())) {
            this.contentType = "application/x-www-form-urlencoded";
        } else {
            this.contentType = req.getContentType();
        }

        if (this.contentType.contains("application/x-www-form-urlencoded")) {
            Enumeration<?> pNames = req.getParameterNames();
            this.content = new HashMap<>();
            while (pNames.hasMoreElements()) {
                String name = (String) pNames.nextElement();
                this.content.put(name, req.getParameter(name));
            }
        } else if (this.contentType.contains("application/json")) {
            StringBuilder jb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null) {
                    jb.append(line);
                }
            } catch (Exception e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
            this.content = CmnParseUtil.parseObjectStr2Map(jb.toString());
        } else if (this.contentType.contains("application/xml")) {
            StringBuilder jb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null) {
                    jb.append(line);
                }
            } catch (Exception e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
            XMLSerializer xmlserializer = new XMLSerializer();
            xmlserializer.setRootName("xml");
            this.content = CmnParseUtil.parseObjectStr2Map(xmlserializer.read(jb.toString()).toString());
        }
    }
}
