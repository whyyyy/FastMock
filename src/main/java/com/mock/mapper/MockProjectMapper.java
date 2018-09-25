package com.mock.mapper;

import com.mock.model.MockProject;
import java.util.List;

public interface MockProjectMapper {

	int insert(MockProject p);

	int update(MockProject p);

	int delete(MockProject p);

	List<MockProject> select(MockProject p);

}
