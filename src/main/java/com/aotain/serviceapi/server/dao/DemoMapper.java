package com.aotain.serviceapi.server.dao;

import com.aotain.common.config.annotation.MyBatisDao;

@MyBatisDao("demoMapper")
public interface DemoMapper {

    String getOne();

}
