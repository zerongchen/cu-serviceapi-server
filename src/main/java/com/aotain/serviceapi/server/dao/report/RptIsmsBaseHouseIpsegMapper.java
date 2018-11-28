package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;
import com.aotain.serviceapi.server.dao.BaseMapper;

import java.util.List;

@MyBatisDao
public interface RptIsmsBaseHouseIpsegMapper extends BaseMapper<HouseIPSegmentInformation> {

    List<HouseIPSegmentInforDTO> getDtoList(HouseIPSegmentInforDTO dto);
}
