package com.aotain.serviceapi.server.dao;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/30
 */
@MyBatisDao
public interface IdcJcdmXzqydmMapper {
    /**
     * 根据行政区域代码查询记录
     * @param code
     * @return
     */
    IdcJcdmXzqydm getXzqydmCodeByCode(String code);
}
