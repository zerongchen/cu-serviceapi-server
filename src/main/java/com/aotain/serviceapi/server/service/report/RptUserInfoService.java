package com.aotain.serviceapi.server.service.report;

import com.aotain.cu.serviceapi.dto.*;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserInformation;

public interface RptUserInfoService {

    PageResult<UserInformationDTO> getUserInfoList(UserInformationDTO dto);

    UserInformationDTO getUserInfoById(UserInformationDTO dto);

    PageResult<UserServiceInformationDTO> getUserServiceList(UserServiceInformationDTO dto);

    PageResult<UserBandwidthInformationDTO> getUserBandWidthList(UserBandwidthInformationDTO dto);

    PageResult<UserVirtualInformationDTO> getUserVirtualList(UserVirtualInformationDTO dto);


    ResultDto insert(UserInformation userInformation) throws Exception;

    ResultDto modify(UserInformation userInformation) throws Exception;

    ResultDto delete(Long userId) throws Exception;
}
