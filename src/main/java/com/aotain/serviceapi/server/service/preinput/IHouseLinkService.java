package com.aotain.serviceapi.server.service.preinput;

import java.util.List;

import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseGatewayInformation;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/20
 */
public interface IHouseLinkService {

    /**
     * 根据linkNo查询记录
     * @param linkNo
     * @return
     */
    boolean existRecord(String linkNo);

    ResultDto saveData(HouseGatewayInformationDTO dto);

    ResultDto batchUpdate(List<HouseGatewayInformationDTO> dtos);

    ResultDto deleteData(HouseGatewayInformationDTO dto);


    /**
     * 
     * 
     */
    /**
     * 新增校验成功的链路信息
     * @param linkDto	待插入的链路信息
     * @param houseId	连路线信息所属的机房ID
     * @return
     */
	ResultDto insertData(List<HouseGatewayInformation> linkDto, Long houseId);
	
	/**
	 * 批量插入链路信息
	 * @param gatewayInfoList	待插入的连路信息
	 * @param houseId	链路所属的机房ID
	 * @param allowInsert	是否允许持久化数据。true：待插入的信息集合中校验合法的数据持久化；false：待插入的信息集合中的所有数据校验都合法后才允许持久化
	 * @return
	 */
	ResultDto batchInsertHouseLinkInfos(List<? extends HouseGatewayInformation> gatewayInfoList, Long houseId, boolean allowInsert);

	/**
	 * 批量删除链路信息
	 * 
	 */
	ResultDto batchDelete(List<? extends HouseGatewayInformation> links);

}
