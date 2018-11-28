package com.aotain.serviceapi.server.dao.dic;

import com.aotain.common.config.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

@MyBatisDao
public interface DictionaryMapper {

    @Cacheable(value = "validateCache",key = "#p0")
    String getProvinceArreaCode(String mc);

    @Cacheable(value = "validateCache",key = "#p0+#p1")
    String getCityArreaCode( @Param("provinceCode") String provinceCode,@Param("mc") String mc);

    @Cacheable(value = "validateCache",key = "#p0+#p1")
    String getCountyArreaCode(@Param("cityCode") String cityCode,@Param("mc") String mc);

    @Cacheable(value = "validateCache",key = "#p0")
    Long getHouseIdByHouseName(String houseName);

}
