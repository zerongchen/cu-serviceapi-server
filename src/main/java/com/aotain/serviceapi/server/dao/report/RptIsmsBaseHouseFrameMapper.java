package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;
import com.aotain.serviceapi.server.dao.BaseMapper;

import java.util.List;

@MyBatisDao
public interface RptIsmsBaseHouseFrameMapper extends BaseMapper<HouseFrameInformation> {

    List<HouseFrameInformationDTO> getDtoList(HouseFrameInformationDTO dto);
}
