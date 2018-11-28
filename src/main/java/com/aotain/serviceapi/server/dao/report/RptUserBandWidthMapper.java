package com.aotain.serviceapi.server.dao.report;


import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface RptUserBandWidthMapper {
    int deleteByPrimaryKey(Long hhid);

    int insert(UserBandwidthInformation record);

    UserBandwidthInformation selectByPrimaryKey(Long hhid);

    int updateByPrimaryKeySelective(UserBandwidthInformation record);

    public int selectCountById(UserBandwidthInformation record);

    public int insertList(List<UserBandwidthInformation> record);

    public int deleteByIds(Map<String,Object> query);

    public void updateOrAdd(List<UserBandwidthInformation> record);

    public int deleteByIdList(List<UserBandwidthInformation> record);

    List<UserBandwidthInformationDTO> getDtoList(UserBandwidthInformationDTO dto);
}