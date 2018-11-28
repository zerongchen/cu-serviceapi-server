package com.aotain.serviceapi.server.service.preinput;

import java.util.List;
import java.util.Map;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;

public interface PreUserBandWidthService {

    public PageResult<UserBandwidthInformationDTO> getServerInfoList(UserBandwidthInformationDTO dto);

    ResultDto insertData(List<? extends UserBandwidthInformation> bands, Long userId, boolean allowInsert) throws Exception;

    ResultDto updateData(List<UserBandwidthInformationDTO> dto) throws Exception;

    ResultDto deleteData(List<UserBandwidthInformationDTO> dto) throws Exception;

    Map<Integer,ResultDto> imporUserBandData( Map<Integer,UserBandwidthInformationDTO> dto);

    ResultDto insertData(List<UserBandwidthInformation> bandwidthList, Long userId);

}
