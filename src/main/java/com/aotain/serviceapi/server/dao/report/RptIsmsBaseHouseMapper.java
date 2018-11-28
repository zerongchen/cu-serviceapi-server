package com.aotain.serviceapi.server.dao.report;


import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.serviceapi.server.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface RptIsmsBaseHouseMapper extends BaseMapper<HouseInformation> {

	HouseInformation findByHouseId(HouseInformation dto);
	HouseInformation findByHouseName(HouseInformation dto);
	HouseInformation findByHouseIdStr(HouseInformation dto);
	List<Integer> selectByJyzId(@Param("jyzId") Integer jyzId);

	List<HouseInformationDTO> getDtoList(HouseInformationDTO dto);
	HouseInformationDTO getHouseInfoById(HouseInformationDTO dto);
}
