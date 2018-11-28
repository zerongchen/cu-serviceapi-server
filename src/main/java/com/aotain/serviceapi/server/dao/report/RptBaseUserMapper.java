package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.UserInformation;

import java.util.List;

@MyBatisDao
public interface RptBaseUserMapper {
    public int deleteByPrimaryKey(Long userId);

    public int insert(UserInformation record);

    public UserInformation selectByPrimaryKey(Long userId);

    public List<UserInformation> selectByJyzId( Long jyzId);

    public int updateByPrimaryKeySelective(UserInformation record);

    public int selectCountById(UserInformation record);

    public void updateOrAdd(UserInformation record);
    
    UserInformation findByUserId(UserInformation dto);

    List<UserInformationDTO> getDtoList(UserInformationDTO dto);
    UserInformationDTO getUserInfoById(UserInformationDTO dto);
}