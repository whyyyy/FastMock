package com.mock.service;

import com.mock.cmn.util.CmnParseUtil;
import com.mock.cmn.util.mock.FuncScript;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AssembleBody {

	private static final String JOSN = "json";
	private static final String FORM = "form";
	private static final String HTML = "html";
	private static final String XML = "xml";

    @Autowired
    private
    HttpCallbackService cbs;

	/**
	 * 构造返回的String 数据
	 * 
	 * @param sch JSONObject
	 * @return map
	 */
	public Map<String ,Object>  AssembleResponseBody(JSONObject sch){
		Map<String ,Object> responseMap = new HashMap<>();
		String type = sch.get("type").toString().toLowerCase();
		int responseStatus = 200;
		String responseBody = "";
        String contentType = "application/json";
		if(sch.get("status")!=null) {
            responseStatus = sch.getInt("status");
        }

		switch (type) {
            case JOSN:
                // 构造Json(String)类型的返回数据
                responseBody = formatContent(sch);
                break;
            case FORM:
                // 构造Form(String)类型的返回数据
                responseBody = CmnParseUtil.JsonStrToForm(formatContent(sch));
                contentType = "text/html";
                break;
            case HTML:
                // 构造Html(String)类型的返回数据
                String content = formatContent(sch);
                responseBody = JSONObject.fromObject(content).getString("html");
                contentType = "text/html";
                break;
			case XML:
				// 构造Xml(String)类型的返回数据
                String json = formatContent(sch);
                JSONObject jsonobj = JSONObject.fromObject(json);
                String xml = null;
                try {
                    XMLSerializer xmlserializer = new XMLSerializer();
                    xmlserializer.setRootName("xml");
                    xml = xmlserializer.write(jsonobj);
                } catch (Exception e) {
                    log.error("transfer to xml failed: {}", ExceptionUtils.getFullStackTrace(e));
                }
                responseBody = xml;
                contentType = "text/xml";
                break;
            default:
                // 没有匹配的格式数据
                log.info("===@@@==={} is not available===@@@===",type);
                responseStatus = 404;
                responseBody = type + " is not available";
                contentType = "text/html";
                break;
		}

        responseMap.put("status", responseStatus);
        responseMap.put("content", responseBody);
        responseMap.put("contenttype", contentType);

        if(sch.get("callback")!=null) {
            if(sch.get("callback") instanceof JSONArray) {
                cbs.callBack(sch.getJSONArray("callback"));
            }else if(sch.get("callback") instanceof JSONObject){
                cbs.callBack(sch.getJSONObject("callback"));
            }
        }
        return responseMap;
	}

    private Map<String, String> getVarParms(JSONArray vars) {
        Map<String, String> varParams = new HashMap<>();
        // 执行变量中的方法将返回值存储为变量
        for (Object var : vars) {
            JSONObject json = JSONObject.fromObject(var);
            String name = json.getString("name");
            String method = json.getString("method");
            String value = json.getString("value");
            String res = FuncScript.execute(method, value);
            varParams.put(name, res);
        }
        return varParams;
    }

    public String formatContent(JSONObject request){
	    JSONObject content = request.getJSONObject("content");
        String json = content==null?"":content.toString();
        if (request.containsKey("var_")) {
            Map<String, String> varParams = getVarParms(request.getJSONArray("var_"));
            for (String varKey : varParams.keySet()) {
                String varValue = varParams.get(varKey);
                json = json.replaceAll("\\$\\{" + varKey + "}", varValue);
            }
            log.debug("====content after format: {}", json);
        }
        return json;
    }
	
}
