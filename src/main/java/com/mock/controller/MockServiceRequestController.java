package com.mock.controller;

import com.mock.bean.MockResult;
import com.mock.mapper.MockRequestStrategyMapper;
import com.mock.model.MockRequestStrategy;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mock/request")
@RestController
public class MockServiceRequestController{

	@Autowired
	private MockRequestStrategyMapper mockRequestStrategyMapper;
	

	@RequestMapping(value = "", method = RequestMethod.POST)
	public MockResult insert(@RequestBody MockRequestStrategy p) {
		p.setId(UUID.randomUUID().toString());
		int count = mockRequestStrategyMapper.insert(p);
        MockResult rs;
		if (count == 1) {
			rs = new MockResult<>(MockResult.SUCCESS, p.getId());
		} else {
			rs = new MockResult(MockResult.ERROR);
		}
		return rs;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public MockResult get(@PathVariable String id){
		MockRequestStrategy p = new MockRequestStrategy();
		p.setId(id);
        MockResult rs;
		List<MockRequestStrategy> ls = mockRequestStrategyMapper.select(p);
		if(ls.size()>0){
			rs = new MockResult<>(MockResult.SUCCESS, ls);
		}else{
            rs = new MockResult(MockResult.SUCCESS);
		}
		return rs;
	}
	
	@RequestMapping(value = "/uri/{id}", method = RequestMethod.GET)
	public MockResult getByUri(@PathVariable String id) {
		MockRequestStrategy p = new MockRequestStrategy();
		p.setMockUriId(id);
        MockResult rs;
		List<MockRequestStrategy> ls = mockRequestStrategyMapper.select(p);
        if(ls.size()>0){
            rs = new MockResult<>(MockResult.SUCCESS, ls);
        }else{
            rs = new MockResult(MockResult.SUCCESS);
        }
		return rs;
	}
	
	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
	public MockResult save(@RequestBody MockRequestStrategy p) {
		int count = mockRequestStrategyMapper.update(p);
        MockResult rs;
		if (count == 1) {
            rs = new MockResult(MockResult.SUCCESS);
        }else{
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
	}
	
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public MockResult delete(@RequestBody MockRequestStrategy p) {
		MockResult rs;
		int count = mockRequestStrategyMapper.delete(p);
		if (count == 1) {
            rs = new MockResult(MockResult.SUCCESS);
        }else{
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
	}

}
