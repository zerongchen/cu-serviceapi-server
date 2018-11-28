package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.model.UserServiceInformation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface RptUserServiceMapper {
    int deleteByPrimaryKey(Long serviceid);

    int insert(UserServiceInformation record);

    UserServiceInformation selectByPrimaryKey(Long serviceid);

    int updateByPrimaryKeySelective(UserServiceInformation record);

    public int selectCountById(UserServiceInformation record);

    int insertList(@Param("list") List<UserServiceInformation> list);

    public int deleteByIds(Map<String,Object> query);

    public void updateOrAdd(List<UserServiceInformation> record);

    public int deleteByIdList(List<UserServiceInformation> record);


    List<UserServiceInformationDTO> getDtoList(UserServiceInformationDTO dto);
}