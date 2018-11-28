package com.aotain.serviceapi.server.service.preinput;

import java.util.List;
import java.util.Map;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserVirtualInformationDTO;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;

public interface PreUserVirtualService {

	ResultDto updateData(List<UserVirtualInformation> virtual) throws Exception;

	ResultDto batchDeleteDatas(List<UserVirtualInformation> virtuals);

	PageResult<UserVirtualInformationDTO> getUserVirtualList(UserVirtualInformationDTO virtual);

	Map<Integer,ResultDto> imporUserVirData(  Map<Integer,UserVirtualInformationDTO> dto);

	ResultDto insertData(List<UserVirtualInformation> virtualList, Long userId);

	/**
	 * 删除机房时，将关联数据更新为删除未上报
	 * @param userId
	 */
	int updateRelativeDataInHouseDeleteByUserIdAndHouseId(long userId, long houseId);

}
