package com.aotain.serviceapi.server.dao.preinput;

import java.util.List;
import java.util.Map;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface UserHHMapper {

	int updateUserHHInfoByHHid(UserBandwidthInformation dto);

	int findUserHHInfoCount(UserBandwidthInformation dto);

	/**
	 * 
	 * 根据用户Id删除带宽信息
	 */
	int deleteByUserId(long userId);

	/**
	 * 
	 * 根据用户Id更改用户带宽操作类型和处理状态
	 */
	int UpdateDealFlagAndCzlxByUserId(Map<String, Object> map);

	/**
	 *
	 * 根据用户Id更改用户带宽操作类型和处理状态,用户属性变更
	 */
	int UpdateDAndCByUserId(Map<String, Object> map);

	/**
	 * 通过hhId查询带宽信息
	 * 
	 */
	UserBandwidthInformation findByHHid(long hhId);

	List<UserBandwidthInformation> getHHByUserId( long userId);

	/**
	 * 根据条件删除网络资源信息
	 * @param map
	 * @return
	 */
	int deleteByCondition(Map<String, Object> map);

	/**
	 * 根据用户id更新指定的用户带宽
	 * @param userId
	 * @return
	 */
	int updateUserHHInfoByUserIdAndHouseId(@Param(value="userId")Long userId, @Param(value="houseId")Long houseId);

}
