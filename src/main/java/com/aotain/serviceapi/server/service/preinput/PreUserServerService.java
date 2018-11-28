package com.aotain.serviceapi.server.service.preinput;

import java.util.List;
import java.util.Map;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserServiceInformation;

public interface PreUserServerService {

    public PageResult<UserServiceInformationDTO> getServerInfoList(UserServiceInformationDTO dto);

    ResultDto insertData(List<? extends UserServiceInformation> services, Long userId, boolean allowInsert) throws Exception;

    ResultDto updateData(List<UserServiceInformationDTO> dto) throws Exception;

    ResultDto deleteData(List<UserServiceInformationDTO> dtos) throws Exception;

    Map<Integer,ResultDto> imporUserServiceData( Map<Integer,UserServiceInformationDTO> dto);

	ResultDto insertData(List<UserServiceInformation> serviceList, Long userId);

}
