package com.mock.cmn.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;

@Slf4j
public class ScriptExecutor {

	/**
	 * 验证预期 js 结果，将结果返回 String 类型
	 * @param content requst body
	 * @param javaScriptFunction script
	 * @return result
	 */
	public static String runJsScript(String content, String javaScriptFunction) {
		return runJsScriptByFunc(content, javaScriptFunction, "");
	}
	
	public static String runJsScriptByFunc(String content, String javaScriptFunction, String func) {
		try {
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName("JavaScript");
			engine.eval(javaScriptFunction);
			Invocable inv = (Invocable) engine;
			Object obj;
			JSONObject body = JSONObject.fromObject(content);
			func = func.isEmpty()?"exec":func;
			obj = inv.invokeFunction(func, body);
			if(obj!=null){
				return obj.toString();
			}
			return "false";
		}catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return "error";
		}
	}

}
