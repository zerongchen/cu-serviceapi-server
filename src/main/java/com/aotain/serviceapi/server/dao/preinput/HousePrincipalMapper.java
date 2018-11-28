package com.aotain.serviceapi.server.dao.preinput;

import java.util.List;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.common.config.model.IdcHouses;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.UserInformation;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
@MyBatisDao
public interface HousePrincipalMapper {

    /**
     * 根据houseId查询记录
     * @param dto
     * @return
     */
    HouseInformationDTO findByHouseId(HouseInformationDTO dto);

    /**
     * 根据houseName查询记录
     * @param dto
     * @return
     */
    HouseInformationDTO findByHouseName(HouseInformationDTO dto);
    /**
     * 根据houseIdStr查询记录
     * @param dto
     * @return
     */
    HouseInformationDTO findByHouseIdStr(HouseInformationDTO dto);

    /**
     * 根据机房id更新机房dealflag信息
     * @param houseInformation
     * @return
     */
    int updateHouseDealFlagByHouseId(HouseInformation houseInformation);

    /**
     * 机房信息提交预审
     * @param houseInformation
     * @return
     */
    int updateDealFlagById(HouseInformation houseInformation);

    /**
     * 
     * 新增机房主体信息
     */
	int insertHouseInformation(HouseInformation dto);

	/**
	 *
	 * 修改机房主体信息
	 */
	int updateHouseInformation(HouseInformation dto);

	/**
	 * 
	 * 删除机房主体信息
	 */
	int delete(HouseInformation dto);

	/**
	 * 
	 * 根据机房id删除机房信息
	 */
	int deleteByHouseId(long houseId);

	List<Long> findFrameIdsByHouseId(long hosueId);

	List<Long> findLinkIdsByHouseId(long hosueId);

	List<Long> findIpsegIdsByHouseId(long hosueId);
	
	int integrityVerificationForHouseFrame(long houseId);
	
	int integrityVerificationForHouseLink(long houseId);
	
	int integrityVerificationForHouseIPSeg(long houseId);

	/**
	 * 根据机房id查询该机房下已报备的机架和IP关联的用户信息
	 * 
	 */
	List<UserInformation> findHaveReportFrameAndIPsegConnectUserInfoByHouseId(long hosueId);
	
	/**
     * 机房主体信息查询
     * @param dto
     * @return
     */
    List<HouseInformationDTO> getHouseIndexList(HouseInformationDTO dto);
    /**
     * 根据ID查询机房主体信息
     * @param dto
     * @return
     */
    HouseInformationDTO getHouseInfoById(HouseInformationDTO dto);

    int updateForJyz(Integer jyzId);
    
    List<IdcHouses> findAllHouseIdAndName();
}
