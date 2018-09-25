package com.mock.bean;

import java.util.Map;

public class StrategyFlowBean {

    private String projectName;
    private String uriName;
    private String strategyName;
    private String strategyResult;
    private Map<String,Object> strategyResp;
    private String hit = "";

    public StrategyFlowBean(String projectName, String uriName, String strategyName, String strategyResult) {
        this.projectName = projectName;
        this.uriName = uriName;
        this.strategyName = strategyName;
        this.strategyResult = strategyResult;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUriName() {
        return uriName;
    }

    public void setUriName(String uriName) {
        this.uriName = uriName;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyResult() {
        return strategyResult;
    }

    public void setStrategyResult(String strategyResult) {
        this.strategyResult = strategyResult;
    }

    public Map<String,Object> getStrategyResp() {
        return strategyResp;
    }

    public void setStrategyResp(Map<String,Object> strategyResp) {
        this.strategyResp = strategyResp;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }
}
