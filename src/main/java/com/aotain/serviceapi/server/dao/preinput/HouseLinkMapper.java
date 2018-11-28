package com.aotain.serviceapi.server.dao.preinput;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.model.HouseGatewayInformation;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/20
 */
@MyBatisDao
public interface HouseLinkMapper {

	/**
	 * 根据linkNo和记录id查询记录
	 * @param dto
	 * @return
	 */
	HouseGatewayInformation findByLinkNoAndId(HouseGatewayInformationDTO dto);

    /**
     * 根据linkNo查询记录
     * @param linkNo
     * @return
     */
    HouseGatewayInformation findByLinkNo(String linkNo);

    /**
     * 根据linkNo更新记录
     * @param houseGatewayInformation
     * @return
     */
    int updateHouseGatewayByLinkNo(HouseGatewayInformation houseGatewayInformation);

    /**
     * 新增链路信息
     * 
     */
	int insertLinkInformation(HouseGatewayInformation dto);

    /**
     * 插入数据
     * @param houseGatewayInformation
     * @return
     */
    int insertSelective(HouseGatewayInformation houseGatewayInformation);

    /**
     * 机房链路信息查询
     * @param dto
     * @return
     */
    List<HouseGatewayInformationDTO> getIndexHouseLink(HouseGatewayInformationDTO dto);

    /**
     * 根据机房id删除机房下的链路信息
     * 
     */
	int deleteByHouseId(long houseId);

	/**
	 * 根据linkid删除链路信息
	 * 
	 */
	int deleteByLinkIdOrLinkNo(@Param("linkId")long linkId, @Param("linkNo")String linkNo);

	/**
	 * 根据linkId查询链路信息
	 * 
	 */
	HouseGatewayInformation findByLinkIdOrLinkNo(@Param("linkId")long linkId, @Param("linkNo")String linkNo);

	/**
	 * 根据linkId修改链路状态信息
	 * 
	 */
	int updateLinkStatusByLinkId(Map<String, Object> map);

	HouseGatewayInformation findByLinkId(@Param("linkId")Long linkId);
}
