package com.mock.controller;

import com.mock.bean.MockResult;
import com.mock.bean.RequestBean;
import com.mock.bean.StrategyFlowBean;
import com.mock.service.MockStrategyService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mock")
@RestController
public class MockTestController {

    @Autowired
    private MockStrategyService mss;

    @RequestMapping("/test")
    public MockResult mockTest(HttpServletRequest httpRequest){

        log.info("===@@@===receive test request===@@@===");

        RequestBean requestBean = new RequestBean(httpRequest);
        requestBean.setPath(requestBean.getHeader().get("mockuri"));
        List<StrategyFlowBean> strategies = mss.getPossibleStrategies(requestBean);
        if(strategies == null || strategies.size() == 0){
            return new MockResult(MockResult.NOTFOUND);
        }

        boolean first = true;
        StringBuilder rsStr = new StringBuilder();
        String ctntype="";
        String ctn ="";
        String stdCode ="";
        appendResult(rsStr,0,"Possible strategies：");
        for(StrategyFlowBean slb : strategies){
            Map<String, Object> responseMap = slb.getStrategyResp();
            if(first && responseMap != null){
                first = false;
                ctn = responseMap.get("content").toString();
                ctntype = responseMap.get("contenttype").toString();
                stdCode = responseMap.get("status").toString();
                slb.setHit("(Hit)");
            }
            appendResult(rsStr,1,getStrategyFlow(slb));
        }
        appendResult(rsStr,0,"Response：");
        appendResult(rsStr,1,"statusCode："+ stdCode);
        appendResult(rsStr,1,"Content-Type："+ ctntype);
        appendResult(rsStr,1,"content："+ ctn);
        log.debug("===@@@===test result: {} ===@@@===",rsStr.toString());
        return new MockResult<>(MockResult.SUCCESS, "success", rsStr.toString());
    }

    private void appendResult(StringBuilder sb, int tab, String msg){
        String warp = "\n";
        String t = "    ";
        for(int i=0;i<tab;i++){
            sb.append(t);
        }
        sb.append(msg);
        sb.append(warp);
    }

    private String getStrategyFlow(StrategyFlowBean slb){
        return slb.getProjectName()+"->"+slb.getUriName()+"->"+slb.getStrategyName()+"->"+slb.getStrategyResult()+slb.getHit();
    }

}
