package com.aotain.serviceapi.server.dao;

import java.util.List;

/**
 * @author zouyong
 * @date 2016-11-15
 */
public interface BaseMapper<T> {

	  int insertData(T t);

	  int updateData(T t);
	  
	  int deleteData(T t);

	  int deleteDataByFK(Integer fk);
	  
	  T getData(T t);

	  List<T> getList(T t);

	  int getCount(T t);
}
