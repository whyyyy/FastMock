package com.mock.mapper;

import com.mock.model.MockRequestStrategy;
import java.util.List;

public interface MockRequestStrategyMapper {

	int insert(MockRequestStrategy p);

	int update(MockRequestStrategy p);

	int delete(MockRequestStrategy p);

	List<MockRequestStrategy> select(MockRequestStrategy p);

}
