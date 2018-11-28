package com.aotain.serviceapi.server.service.report;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.cu.serviceapi.model.UserInformation;

import java.util.List;

public interface IdcInfoService {

    ResultDto add( IdcInformation idcInformation) throws Exception;
    ResultDto update( IdcInformation idcInformation ) throws Exception;
    ResultDto delete( Integer jyzid);
}
