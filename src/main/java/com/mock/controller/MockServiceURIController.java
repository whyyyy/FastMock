package com.mock.controller;

import com.alibaba.fastjson.JSONObject;
import com.mock.bean.MockResult;
import com.mock.mapper.MockRequestStrategyMapper;
import com.mock.mapper.MockUriMapper;
import com.mock.model.MockRequestStrategy;
import com.mock.model.MockUri;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mock/uri")
@RestController
public class MockServiceURIController{

	@Autowired
	private MockUriMapper mockUriMapper;
	@Autowired
    private MockRequestStrategyMapper mockRequestStrategyMapper;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public MockResult insert(@RequestBody MockUri p) {
		p.setId(UUID.randomUUID().toString());
		int count = mockUriMapper.insert(p);
        MockResult rs;
		if (count == 1) {
            rs = new MockResult<>(MockResult.SUCCESS, p.getId());
        } else {
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public MockResult get(@PathVariable String id) {
		MockUri p = new MockUri();
		p.setId(id);
        MockResult rs;
		List<MockUri> ls = mockUriMapper.select(p);
		if(ls.size()>0){
            rs = new MockResult<>(MockResult.SUCCESS, ls);
        }else{
            rs = new MockResult(MockResult.SUCCESS);
        }
        return rs;
	}
	
	@RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
	public MockResult getByProject(@PathVariable String id) {
		MockUri p = new MockUri();
		p.setMockProjectId(id);
        MockResult rs;
		List<MockUri> ls = mockUriMapper.select(p);
		if(ls.size()>0){
            rs = new MockResult<>(MockResult.SUCCESS, ls);
        }else{
            rs = new MockResult(MockResult.SUCCESS);
        }
        return rs;
	}
	
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public MockResult save(@RequestBody MockUri p) {
		int count = mockUriMapper.update(p);
        MockResult rs;
		if (count == 1) {
            rs = new MockResult(MockResult.SUCCESS);
        }else{
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
	}
	
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public MockResult delete(@RequestBody MockUri p) {
        MockResult rs;
		MockRequestStrategy mockRequestStrategyBean  = new MockRequestStrategy();
		mockRequestStrategyBean.setMockUriId(p.getId());

		if (mockRequestStrategyMapper.select(mockRequestStrategyBean).size() >0){
			rs = new MockResult<>(MockResult.ERROR, "please delete the request items",null);
			return rs;
		}
		int count = mockUriMapper.delete(p);
		if (count == 1) {
            rs = new MockResult(MockResult.SUCCESS);
        }else{
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
	}
	
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public MockResult updateStatus(@RequestBody JSONObject j) {
        MockResult rs;
		int count = 0;
		log.debug("updateStatus {}",j);
		try{
			String status = j.getBoolean("state")?"RUN":"STOP";
			String id = j.getString("id");
			if("uri".equalsIgnoreCase(j.getString("type"))){
				MockUri mu = new MockUri();
				mu.setId(id);
				mu.setIsRun(status);
				count = mockUriMapper.update(mu);
			}else if("req".equalsIgnoreCase(j.getString("type"))){
				MockRequestStrategy mr  = new MockRequestStrategy();
				mr.setId(id);
				mr.setIsRun(status);
				count = mockRequestStrategyMapper.update(mr);
			}
		}catch(Exception e){
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		if (count == 1) {
            rs = new MockResult(MockResult.SUCCESS);
        }else{
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
	}

}
