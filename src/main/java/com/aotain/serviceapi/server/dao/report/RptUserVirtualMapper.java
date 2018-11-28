package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserVirtualInformationDTO;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface RptUserVirtualMapper {
    int deleteByPrimaryKey(Long virtualid);

    int insert(UserVirtualInformation record);

    UserVirtualInformation selectByPrimaryKey(Long virtualid);

    int updateByPrimaryKeySelective(UserVirtualInformation record);

    public int selectCountById(UserVirtualInformation record);

    int insertList(List<UserVirtualInformation> record);

    public int deleteByIds(Map<String,Object> query);

    public void updateOrAdd(List<UserVirtualInformation> record);

    public int deleteByIdList(List<UserVirtualInformation> record);
    
    UserVirtualInformation findByVirtualNo(UserVirtualInformation dto);

    UserVirtualInformation findByName(UserVirtualInformation dto);

    List<UserVirtualInformationDTO> getDtoList(UserVirtualInformationDTO dto);
}