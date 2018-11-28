package com.aotain.serviceapi.server.service.report;

import com.aotain.cu.serviceapi.dto.*;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.PageResult;

public interface RptHouseService {

	PageResult<HouseInformationDTO> listHouseInfo(HouseInformationDTO dto);

	HouseInformationDTO getHouseInfoById(HouseInformationDTO dto);

	PageResult<HouseFrameInformationDTO> getIndexHouseFrame(HouseFrameInformationDTO dto);

	PageResult<HouseGatewayInformationDTO> getIndexLink(HouseGatewayInformationDTO dto);

	PageResult<HouseIPSegmentInforDTO> getIndexIpSegment(HouseIPSegmentInforDTO dto);

	ResultDto insert(HouseInformation ass);
	ResultDto update(HouseInformation ass);
	ResultDto delete(Integer houseId);
}
