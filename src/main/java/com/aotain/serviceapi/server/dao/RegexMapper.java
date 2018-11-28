package com.aotain.serviceapi.server.dao;

import com.aotain.common.config.annotation.MyBatisDao;
import org.springframework.cache.annotation.Cacheable;

/**
 * 正则表达式正则
 *
 * @author bang
 * @date 2018/07/16
 */
@MyBatisDao
public interface RegexMapper {
    /**
     * 根据subtype获取对应正则表达式值
     * @param subType
     * @return
     */
    @Cacheable(value = "validateCache",key = "#p0")
    String getValueBySubType(Integer subType);
}
