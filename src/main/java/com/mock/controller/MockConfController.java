package com.mock.controller;

import com.mock.bean.MockResult;
import com.mock.service.MockConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mock")
@RestController
public class MockConfController {
	
	@Autowired
	private MockConfService mcs;
	
	@RequestMapping(value="/conf")
	public MockResult mockconfig(@RequestParam(value = "method")String method, @RequestParam(value = "value", required = false, defaultValue = "1")int value){
		MockResult rs = null;
		if("getMockStatus".equals(method)){
			rs = mcs.getMockStatus();
		}else if("updateMockStatus".equals(method)){
			rs = mcs.updateMockStatus(value);
		}
		return rs;
	}
}
