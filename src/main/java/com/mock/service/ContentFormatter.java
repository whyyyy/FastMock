package com.mock.service;

import com.mock.cmn.util.mock.FuncScript;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 内容格式化工具类
 * 用于格式化和处理请求/响应内容，替换变量等
 */
@Slf4j
@Component
public class ContentFormatter {

    /**
     * 格式化内容，替换其中的变量
     *
     * @param request 包含 content 和 var_ 的 JSONObject
     * @return 格式化后的 JSON 字符串
     */
    public String formatContent(JSONObject request) {
        JSONObject content = request.getJSONObject("content");
        String json = content == null ? "" : content.toString();

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

    /**
     * 获取变量参数
     *
     * @param vars 变量数组
     * @return 变量名和值的映射
     */
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
}
