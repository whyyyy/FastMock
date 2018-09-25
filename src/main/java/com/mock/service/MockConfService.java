package com.mock.service;

import com.mock.bean.MockResult;
import com.mock.model.SysConf;
import com.mock.mapper.SysConfMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MockConfService {
	
	@Autowired
    private SysConfMapper sysConfMapper;
	
	public MockResult getMockStatus(){
		
		SysConf scf = new SysConf();
		scf.setCode("mock");
		try{
			List<SysConf> l = sysConfMapper.select(scf);
			if(l!=null&&l.size()>0){
				SysConf rs = l.get(0);
				String v = rs.getValue();
				if(!v.isEmpty()){
					return new MockResult<>(MockResult.SUCCESS, rs);
				}
			}
		}catch(Exception e){
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new MockResult<>(MockResult.ERROR, e);
		}
		return new MockResult(MockResult.FAIL);
	}
	
	public MockResult updateMockStatus(boolean value){
		
		SysConf scf = new SysConf();
		scf.setCode("mock");
		scf.setValue(value?"1":"0");

		try{
			int i = sysConfMapper.update(scf);
			if(i>0){
				return new MockResult(MockResult.SUCCESS);
			}
			return new MockResult(MockResult.FAIL);
		}catch(Exception e){
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new MockResult<>(MockResult.ERROR, e);
		}
	}
	
	public boolean checkMockStatus(){
		
		SysConf scf = new SysConf();
		scf.setCode("mock");
		
		try{
			List<SysConf> l = sysConfMapper.select(scf);
			if(l!=null&&l.size()>0){
				SysConf rs = l.get(0);
				String v = rs.getValue();
				if(!v.isEmpty()&&"0".equals(v)){
					return false;
				}
			}
		}catch(Exception e){
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return true;
	}
}
