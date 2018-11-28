package com.aotain.serviceapi.server.dao;

import com.aotain.cu.serviceapi.model.CacheIsmsBaseInfo;
import com.aotain.serviceapi.server.BaseTest;
import com.aotain.serviceapi.server.dao.preinput.CacheIsmsBaseInfoMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/23
 */
public class CacheIsmsInfoMapperTest extends BaseTest{
    @Autowired
    private CacheIsmsBaseInfoMapper cacheIsmsBaseInfoMapper;

    @Test
    public void save(){
        CacheIsmsBaseInfo cacheIsmsBaseInfo = new CacheIsmsBaseInfo();
        cacheIsmsBaseInfo.setCreateTime(new Date());
        cacheIsmsBaseInfo.setHouseId(12345L);
        cacheIsmsBaseInfo.setUserId(222L);
        cacheIsmsBaseInfo.setJyzId(333L);
        cacheIsmsBaseInfo.setCreateUserId(444L);
        cacheIsmsBaseInfoMapper.insert(cacheIsmsBaseInfo);
    }

    @Test
    public void findByHouseId(){
        CacheIsmsBaseInfo cacheIsmsBaseInfo = new CacheIsmsBaseInfo();
        cacheIsmsBaseInfo.setHouseId(12345L);
        CacheIsmsBaseInfo result = cacheIsmsBaseInfoMapper.findByHouseId(cacheIsmsBaseInfo);
        System.out.println(result+"======");
    }

    @Test
    public void updateByHouseId(){
        CacheIsmsBaseInfo cacheIsmsBaseInfo = new CacheIsmsBaseInfo();
        cacheIsmsBaseInfo.setHouseId(12345L);
        cacheIsmsBaseInfo.setCreateTime(new Date());
        int result = cacheIsmsBaseInfoMapper.updateByHouseId(cacheIsmsBaseInfo);
        System.out.println(result+"======");
    }
}
