package com.aotain.serviceapi.server.dao.preinput;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;
import com.aotain.cu.serviceapi.model.HouseUserFrameInformation;

@MyBatisDao
public interface HouseFrameMapper {

	/**
	 * 
	 * 新增机架信息
	 */
	int insertHouseFrameInfomation(HouseFrameInformation dto);

	int updateHouseFrameInfomation(HouseFrameInformation dto);

	public List<HouseFrameInformationDTO> getIndexHouseFrame(HouseFrameInformationDTO dto);

	/**
	 * 
	 * 根据机房id删除机房下的机架信息
	 */
	int deleteByHouseId(long houseId);

	/**
	 * 
	 * 根据frameId删除机架信息
	 */
	int deleteByFrameIdOrFrameName(@Param("frameId")long frameId, @Param("frameName")String frameName);

	/**
	 * 
	 * 根据机房Id删除用户机架信息
	 */
	int deleteUserFrameByUserId(long houseId);

	/**
	 * 
	 * 根据机架Id查询机架信息
	 */
	
	HouseFrameInformation findFrameInfoByFrameIdOrFrameName(@Param("frameId")long frameId, @Param("frameName")String frameName);

	/**
	 * 
	 * 变更机架状态
	 */
	int updateFrameStatusByframeId(Map<String, Object> map);

	/**
	 * 新增用户机架信息
	 * 
	 */
	int insertUserFrame(HouseUserFrameInformation userFrame);
	
	List<String> getUnitName(Map auery);

    HouseFrameInformationDTO getByFrameName(HouseFrameInformationDTO dto);
    
    List<HouseUserFrameInformation> findUserFrameByFrameId(long frameId);
    
    int deleteUserFrameByFrameId(long frameId);

	/**
	 * 根据用户查询用户机架信息
	 * 
	 */
	List<HouseUserFrameInformation> findUserFrameByUserInfo(UserInformationDTO dto);

	/**
	 * 删除用户关联的占用机架信息
	 * 
	 * @author : songl
	 */
	int deleteUserFrameByUserInfo(UserInformationDTO dto);
}
