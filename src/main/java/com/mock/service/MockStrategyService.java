package com.mock.service;

import com.mock.bean.RequestBean;
import com.mock.bean.StrategyFlowBean;
import com.mock.mapper.MockProjectMapper;
import com.mock.mapper.MockRequestStrategyMapper;
import com.mock.mapper.MockUriMapper;
import com.mock.model.MockProject;
import com.mock.model.MockRequestStrategy;
import com.mock.model.MockUri;
import com.mock.cmn.util.CmnParseUtil;
import com.mock.cmn.util.ScriptExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MockStrategyService {

	private static final String verifyTrue = "true";
	private static final String verifyFalse = "false";
	private static final String verifyError = "error";

	@Autowired
	private AssembleBody ab;
	@Autowired
	private MockUriMapper mockUriMapper;
	@Autowired
	private MockRequestStrategyMapper mockRequestStrategyMapper;
    @Autowired
    private MockProjectMapper mockProjectMapper;

	public MockStrategyService() {
	}

    public Map<String,Object> execStrategy(RequestBean requestBean) {
        try {
            String content = CmnParseUtil.BeanToJson(requestBean);
            log.info("===@@@===MockRequest:{}===@@@===", content);
            String uri = requestBean.getPath();
            MockUri mUri = new MockUri();
            mUri.setIsRun("RUN");
            mUri.setMockMethod(requestBean.getMethod());
            List<MockUri> us = mockUriMapper.select(mUri);

            if (us.size() == 0) {
                return null;
            }
            for (MockUri mu : us) {
                String url = mu.getMockUri();
                String murl = url.replace("{}", "([a-zA-z0-9-_]-*){1,}");
                Pattern pattern = Pattern.compile(murl);
                Matcher matcher = pattern.matcher(uri);
                if (matcher.matches()) {
                    MockRequestStrategy mockRequestCn = new MockRequestStrategy();
                    mockRequestCn.setMockUriId(mu.getId());
                    mockRequestCn.setIsRun("RUN");
                    // 查询对应Uri 下面的策略集合
                    List<MockRequestStrategy> mockRequests = mockRequestStrategyMapper
                            .select(mockRequestCn);
                    for (MockRequestStrategy mockRequest : mockRequests) {
                        String strategyFunction = mockRequest.getVerifyExpect();
                        // 执行预期验证脚本
                        String result = ScriptExecutor.runJsScript(content, strategyFunction);
                        log.info("===@@@===Strategy name:{}, Strategy result:{}===@@@===",mockRequest.getMockRequestName(), result);
                        if (!verifyFalse.equalsIgnoreCase(result) && !verifyError.equalsIgnoreCase(result)) {
                            Map<String, Object> resp = getStrategyResponse(content, mockRequest, verifyTrue.equalsIgnoreCase(result)?"":result);
                            if(resp!=null){
                                return resp;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error(ExceptionUtils.getFullStackTrace(e));
            return null;
        }
        return null;
    }

    public List<StrategyFlowBean> getPossibleStrategies(RequestBean requestBean) {
        try {
            List<StrategyFlowBean> rs = new ArrayList<>();
            String content = CmnParseUtil.BeanToJson(requestBean);
            log.info("===@@@===MockTestRequest:{}===@@@===", content);
            String uri = requestBean.getPath();
            MockUri mUri = new MockUri();
            mUri.setIsRun("RUN");
            mUri.setMockMethod(requestBean.getMethod());
            List<MockUri> us = mockUriMapper.select(mUri);
            if (us.size() == 0) {
                return rs;
            }
            boolean first = true;
            for (MockUri mu : us) {
                String url = mu.getMockUri();
                String murl = url.replace("{}", "([a-zA-z0-9-_]-*){1,}");
                Pattern pattern = Pattern.compile(murl);
                Matcher matcher = pattern.matcher(uri);
                if (matcher.matches()) {
                    MockRequestStrategy mockRequestCn = new MockRequestStrategy();
                    mockRequestCn.setMockUriId(mu.getId());
                    mockRequestCn.setIsRun("RUN");
                    // 查询对应Uri 下面的策略集合
                    List<MockRequestStrategy> mockRequests = mockRequestStrategyMapper.select(mockRequestCn);
                    // 循环区配策略，遇到策略结果为"true" 时，组装返回数据并返回
                    for (MockRequestStrategy mockRequest : mockRequests) {
                        String strategyFunction = mockRequest.getVerifyExpect();
                        // 执行预期验证脚本
                        String result = ScriptExecutor.runJsScript(content, strategyFunction);
                        if (!verifyFalse.equalsIgnoreCase(result) && !verifyError.equalsIgnoreCase(result)) {
                            MockProject mp = new MockProject();
                            mp.setId(mu.getMockProjectId());
                            mp = mockProjectMapper.select(mp).get(0);
                            StrategyFlowBean slb = new StrategyFlowBean(mp.getMockProjectName(),mu.getMockUriName(),mockRequest.getMockRequestName(), result);
                            if(first) {
                                Map<String, Object> resp = getStrategyResponse(content, mockRequest,
                                        verifyTrue.equalsIgnoreCase(result) ? "" : result);
                                if (resp != null) {
                                    slb.setStrategyResp(resp);
                                    first = false;
                                }
                            }
                            rs.add(slb);
                        }
                    }
                }
            }
            return rs;
        }catch (Exception e){
            log.error(ExceptionUtils.getFullStackTrace(e));
            return null;
        }
    }

	/**
	 * 获取 预期 response 的String值
	 */
	private Map<String,Object> getStrategyResponse(String content, MockRequestStrategy mockRequest, String func) {
	    try {
            String Expect = ScriptExecutor.runJsScriptByFunc(content,mockRequest.getResponseExpect(),func);
            //请求等待
            try {
                Thread.sleep(mockRequest.getRequestWait());
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
            log.info("===@@@===response result:{}", Expect);
            // 解析【执行预期返回 json格式】 预组装返回数据 json/html/form(String)
            return ab.AssembleResponseBody(JSONObject.fromObject(Expect));
        }catch (Exception e){
	        log.error(ExceptionUtils.getFullStackTrace(e));
	        return null;
        }
	}

}
