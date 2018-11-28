package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.model.HouseGatewayInformation;
import com.aotain.serviceapi.server.dao.BaseMapper;

import java.util.List;

@MyBatisDao
public interface RptIsmsBaseHouseGatewayMapper extends BaseMapper<HouseGatewayInformation> {

    List<HouseGatewayInformationDTO> getDtoList(HouseGatewayInformationDTO dto);
}
