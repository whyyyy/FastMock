package com.mock.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mock.cmn.util.CmnParseUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

@Slf4j
public class MockResult<T> {
    public static final int ERROR = 500;
    public static final int SUCCESS = 0;
    public static final int FAIL = -1;
    public static final int NOTFOUND = 404;
    private static final Map<Integer,String> MESSAGE =  new HashMap<>();

    static {
        MESSAGE.put(ERROR, "Internal error");
        MESSAGE.put(SUCCESS, "Success");
        MESSAGE.put(FAIL, "Failed");
        MESSAGE.put(NOTFOUND, "No request strategy matches and failed on forwarding");
    }

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public MockResult(int code) {
        this.code = code;
        this.msg = MESSAGE.get(code);
    }

    public MockResult(int code, T data) {
        this.code = code;
        this.msg = MESSAGE.get(code);
        this.data = data;
    }

    public MockResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        try {
            return CmnParseUtil.BeanToJson(this);
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return "";
    }
}
