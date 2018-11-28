package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.model.ServiceDomainInformation;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface RptBaseServiceDomainMapper {
    int deleteByPrimaryKey(Long domainid);

    int insert(ServiceDomainInformation record);

    ServiceDomainInformation selectByPrimaryKey(Long domainid);

    int updateByPrimaryKeySelective(ServiceDomainInformation record);

    public int selectCountById(ServiceDomainInformation record);

    public int insertList(List<ServiceDomainInformation> record);

    public int deleteByIds(Map<String,Object> query);

    public int deleteByIdList(List<ServiceDomainInformation> record);

    public void updateOrAdd(List<ServiceDomainInformation> record);

}