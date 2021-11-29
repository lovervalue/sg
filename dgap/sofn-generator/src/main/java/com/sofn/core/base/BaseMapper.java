package com.sofn.core.base;

import java.util.List;

/**
 * @author sofn
 * @version 2016年6月3日 下午2:30:14
 */
public interface BaseMapper<T extends BaseModel> {
	List<T> selectAll();

	int deleteByPrimaryKey(String id);

	T selectByPrimaryKey(String id);

	int insert(T record);

	int updateByPrimaryKey(T record);
}
