package com.aotain.serviceapi.server.dao.preinput;

import java.util.List;
import java.util.Map;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserVirtualInformationDTO;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;
import org.apache.ibatis.annotations.Param;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/12
 */
@MyBatisDao
public interface UserVirtualMachineMapper {

    /**
     * 根据虚拟机编号查询记录
     * @param dto
     * @return
     */
    UserVirtualInformation findByVirtualNo(UserVirtualInformation dto);

    /**
     * 根据虚拟机名称查询记录
     * @param dto
     * @return
     */
    UserVirtualInformation findByName(UserVirtualInformation dto);

    /**
     * 根据虚拟主机Id修改虚拟主机信息
     * 
     */
	int updateVirtualInfoById(UserVirtualInformation dto);

    /**
     * insert
     * @param dto
     * @return
     */
    int  insertUserVirtual(UserVirtualInformation dto);

	UserVirtualInformation findByVirtualId(long virtualId);

	int deleteByVirtulId(long virtualId);

	/**
	 * 用户属性变更的时候(互联网用户变更成其他用户)逻辑删除虚拟主机
	 * @param virtualId
	 * @return
	 */
	int deleteVirByUserIdByLogic(long virtualId);
	List<UserVirtualInformation> getVirtualByUserId(long virtualId);

	List<UserVirtualInformationDTO> getIndexUserVirtual(UserVirtualInformationDTO dto);

	int deleteByUserId(long userId);

	/**
	 * 根据用户Id更改用户服务操作类型和处理状态
	 * 
	 */
	int UpdateDealFlagAndCzlxByUserId(Map<String, Object> map);

	int insertUserVirtualInfo(UserVirtualInformation dto);
	
	int judgeRightExist(UserVirtualInformation dto);

	/**
	 * 操作表根据用户Id删除新增未上报的虚拟主机信息
	 * 
	 */
	int deleteVirByUserIdWhichNotReport(Long userId);

	/**
	 * 根据用户Id和携带用户地市码查询
	 * @param dto
	 * @return
	 */
	List<UserVirtualInformationDTO> getIndexUserVirtualByUserId(UserVirtualInformationDTO dto);

	/**
	 * 根据用户id修改虚拟机信息
	 * @param userId
	 * @return
	 */
	int updateUserVirtualInfoByUserIdAndHouseId(@Param(value="userId") Long userId,@Param(value="houseId")  Long houseId);
}
