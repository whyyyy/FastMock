package com.mock.mapper;

import com.mock.model.MockUri;
import java.util.List;

public interface MockUriMapper {

	int insert(MockUri p);

	int update(MockUri p);

	int delete(MockUri p);

	List<MockUri> select(MockUri p);

}
