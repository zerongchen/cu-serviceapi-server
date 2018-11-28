package com.aotain.serviceapi.server.service.preinput;

import java.util.List;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;

public interface PreHouseIpsegService {

	/**
	 * 新增IP地址段信息
	 * @param ipsegDto	待新增的机房IP地址段信息
	 * @param houseId	IP地址段所属的机房ID
	 * @return
	 */
	ResultDto insertData(List<HouseIPSegmentInformation> ipsegDto, Long houseId);

	/**
	 * 批量插入机房IP地址段信息
	 * @param ipSegList	待插入的机房IP地址段信息集合
	 * @param houseId	机房IP地址段所属的机房ID
	 * @param allowInsert 是否允许持久化数据。true：待插入的信息集合中校验合法的数据持久化；false：待插入的信息集合中的所有数据校验都合法后才允许持久化
	 * @return
	 */
	ResultDto batchInsertHouseIpsegInfos(List<? extends HouseIPSegmentInformation> ipSegList, Long houseId, boolean allowInsert);

	/**
	 * 
	 * 批量删除机房IP地址段信息
	 */
	ResultDto batchDelete(List<? extends HouseIPSegmentInformation> ips);
	
	/**
	 * 批量更新机房IP地址段信息
	 * @param dtos
	 * @return
	 */
	ResultDto batchUpdate(List<? extends HouseIPSegmentInformation> dtos);

}
