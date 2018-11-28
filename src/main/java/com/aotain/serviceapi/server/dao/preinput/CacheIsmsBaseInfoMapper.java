package com.aotain.serviceapi.server.dao.preinput;

import org.apache.ibatis.annotations.Param;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.model.CacheIsmsBaseInfo;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/20
 */
@MyBatisDao
public interface CacheIsmsBaseInfoMapper {
    /**
     *
     * @param cacheIsmsBaseInfo
     * @return
     */
    int insert(CacheIsmsBaseInfo cacheIsmsBaseInfo);

    /**
     * 根据houseId查询记录
     * @param cacheIsmsBaseInfo
     * @return
     */
    CacheIsmsBaseInfo findByHouseId(CacheIsmsBaseInfo cacheIsmsBaseInfo);

    /**
     * 根据houseId更新记录
     * @param cacheIsmsBaseInfo
     * @return
     */
    int updateByHouseId(CacheIsmsBaseInfo cacheIsmsBaseInfo);

    /**
     * 根据UserId查找缓存记录
     */
	CacheIsmsBaseInfo findByUserId(@Param("userId")Long userId);
}
