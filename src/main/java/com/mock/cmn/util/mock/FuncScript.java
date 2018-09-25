package com.mock.cmn.util.mock;

import com.mock.cmn.util.CommonUtil;
import com.mock.cmn.util.CmnParseUtil;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

@Slf4j
public class FuncScript {

    public static String execute(String method, String value){
        try {
            Class<FuncScript> funcClass = FuncScript.class;
            Object func = funcClass.getConstructor().newInstance();
            Method m = funcClass.getMethod(method, String.class);
            return m.invoke(func, value.replaceAll("\\\\","")).toString();
        }catch (Exception e){
            log.error(ExceptionUtils.getFullStackTrace(e));
            return "ScriptException: script Error";
        }
    }

    public String wxSign(String json) throws Exception {
        Map<String, Object> paramap = CmnParseUtil.parseObjectStr2Map(json);
        String key = "123456";
        String param = CommonUtil.formatBizQueryParaMap(paramap,false);
        param = param + "&key=" + key;
        return DigestUtils.md5Hex(param.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

}
