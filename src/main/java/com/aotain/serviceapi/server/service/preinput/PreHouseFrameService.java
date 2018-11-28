package com.aotain.serviceapi.server.service.preinput;

import java.util.List;

import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;

public interface PreHouseFrameService {

	/**
	 * 
	 */
	/**
	 * 新增校验成功的机架信息
	 * @param frameDto	待新增的机架信息
	 * @param houseId	机架信息所属的机房ID
	 * @return
	 */
	ResultDto insertData(List<HouseFrameInformation> frameDto, Long houseId);

	/**
	 * 批量插入机架信息
	 * @param frameList	待插入的机架信息集合
	 * @param houseId	机架所属的机房id
	 * @param allowInsert	是否允许持久化数据。true：待插入的信息集合中校验合法的数据持久化；false：待插入的信息集合中的所有数据校验都合法后才允许持久化
	 * @return
	 */
	ResultDto batchInsertFrameInfos(List<? extends HouseFrameInformation> frameList, Long houseId, boolean allowInsert);

	ResultDto batchUpdate(List<? extends HouseFrameInformation> dtos);

	/**
	 * 批量删除机架信息
	 * 
	 */
	ResultDto batchDelete(List<? extends HouseFrameInformation> racks);

	/**
	 * 新增机架数据
	 * @param dto
	 * @return
	 */
	ResultDto insertData(HouseFrameInformationDTO dto);

	/**
	 * 更新机架数据
	 * @param dto
	 * @return
	 */
	ResultDto updateData(HouseFrameInformationDTO dto);

}
