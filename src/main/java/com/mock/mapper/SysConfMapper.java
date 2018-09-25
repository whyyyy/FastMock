package com.mock.mapper;

import com.mock.model.SysConf;
import java.util.List;

public interface SysConfMapper {
	
	int update(SysConf sc);
	
	List<SysConf> select(SysConf sc);
}
