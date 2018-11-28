package com.aotain.serviceapi.server.dao.preinput;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.dao.BaseMapper;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface UserInfoMapper extends BaseMapper<UserInformation> {

    int updateForJyz(Integer jyzId);
	List<UserInformation> findListByJyz(Integer jyzId);

	UserInformationDTO getUserById(long userId);
    /**
     * 新增机房主体
     * @param dto
     * @return
     */
    int insertUserInfo(UserInformation dto);

    /**
     * 更新机房主体
     * @param dto
     * @return
     */
    int updateUserInfo(UserInformation dto);

    /**
     * 新增机房带宽
     * @param dto
     * @return
     */
    int insertUserBand(UserBandwidthInformation dto);

    /**
     * 通过用户Id查找用户信息
     * 
     */
	UserInformation findUserInfoByUserId(long userId);

	/**
	 * 根据用户id查询对比的用户信息
	 * @param userId
	 * @return
	 */
	UserInformationDTO findByUserId(String userId);

	/**
	 * 
	 * 根据用户Id进行删除
	 */
	int deleteByUserId(long userId);

	/**
	 * 
	 * 查询与机房关联的用户
	 */
	List<HouseInformation> findHaveReportFrameAndIPsegConnectUserInfoByUserInfo(UserInformation dto);

	/**
	 * 
	 * 根据用户Id更新用户dealFlag状态
	 */
	int updateDealFlagByUserId(UserInformation dto);

	/**
	 * 根据用户Id更改用户主体操作类型和处理状态
	 * 
	 */
	int UpdateDealFlagAndCzlxByUserId(Map<String, Object> map);

	/**
	 * 根据用户ID获取用户属性
	 * @param userId
	 * @return
	 */
	int getNatureByUserId(long userId);
	/**
	 * 查询用户分页数据
	 * @param dto
	 * @return
	 */
	List<UserInformationDTO> listData(UserInformationDTO dto);

	/**
	 * 根据用户名匹配数据库中的数据
	 * @param userName
	 * @return
	 */
	UserInformationDTO getDataByUserName(String userName);

	/**
	 * 用户名拿用户Id
	 * @param userName
	 * @return
	 */
	@Cacheable(value = "validateCache",key = "#p0")
	Long getUserIdByUserName(String userName);
	
	int integrityVerificationForUserService(long userId);
	
	int integrityVerificationForUserBandwidth(long userId);
	
	int integrityVerificationForUserVirtuals(long userId);
}
