package com.aotain.serviceapi.server.dao.preinput;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.model.ServiceDomainInformation;
import com.aotain.cu.serviceapi.model.UserServiceInformation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/16
 */
@MyBatisDao
public interface UserServiceMapper {

    /**
     * 是否存在相同的域名
     * @param dto
     * @return
     */
    ServiceDomainInformation findByDomainName(ServiceDomainInformation dto);

    /**
     * 同一用户是否占用相同备案号
     * @param dto
     * @return
     */
    UserServiceInformationDTO findByRegisterIdAndUserId(UserServiceInformationDTO dto);

    /**
     * 新增服务
     * @param dto
     * @return
     */
    int insertUserService(UserServiceInformation dto);

    /**
     * 新增服务域名
     * @param dto
     * @return
     */
    int insertUserServiceDomain(ServiceDomainInformation dto);


    /**
     * 查询用户服务信息多条
     * @param dto
     * @return
     */
    List<UserServiceInformationDTO> getUserServiceInfoList(UserServiceInformationDTO dto);

    /**
     * 更新用户服务信息
     * @param dto
     * @return
     */
    int updateUserService(UserServiceInformationDTO dto);

    /**
     * 更新用户服务域名信息
     * @param dto
     * @return
     */
    int updateUserServiceDomain(ServiceDomainInformation dto);

    /**
     * 根据用户Id删除服务信息
     * 
     */
	int deleteByUserId(long userId);

	/**
	 * 
	 * 根据用户Id删除服务域名信息
	 */
	int deleteServerDomainByUserId(long userId);

    /**
     * 根据domainId删除不存在的
     * @param ids
     * @return
     */
    int deleteByIds(Map query);

	/**
	 * 根据用户Id更改服务域名操作类型和处理状态
	 * 
	 */
	int UpdateServerDomainDealFlagAndCzlxByUserId(Map<String, Object> map);

    /**
     * 根据Id查询详细信息
     * @param serviceId
     * @return
     */
    UserServiceInformationDTO getUserServiceInfoById(long serviceId);

	/**
	 * 根据用户Id更改用户服务操作类型和处理状态
	 * 
	 */
	int UpdateDealFlagAndCzlxByUserId(Map<String, Object> map);

    /**
     * 根据id删除服务信息
     * @param serviceId
     * @return
     */
   int deleteById(Long serviceId);

    /**
     * 根据serviceId删除域名信息
     * @param serviceId
     * @return
     */
    int deleteServerDomainByServiceId(Long serviceId);

    /**
     * 逻辑删除用户服务
     * @param userId
     * @return
     */
    int deleteUserServiceLogic(Long userId);

    /**
     * 逻辑删除用户域名
     * @param userId
     * @return
     */
    int deleteUserServiceDomainLogic(Long userId);

    /**
     * 根据userID获取服务,用户变更属性
     * @param userId
     * @return
     */
    List<UserServiceInformation> getServiceByUserId(@Param("userId") Long userId);
    
    List<UserServiceInformation> getByUserIdAndSetmode(@Param("userId") Long userId, @Param("setmode") Long setmode);

    /**
     * 根据用户Id删除虚拟主机信息
     * 
     * @author : songl
     * @since:2018年10月12日 上午8:51:19
     */
	int deleteVirtualByUserId(Long userId);

	int updateByIds(Map<String, Object> updateMap);
	
	List<ServiceDomainInformation> findDomainNameByServiceId(@Param("serviceId") Long serviceId);

	/**
	 * 操作表根据用户Id删除未上报的服务信息
	 * 
	 */
	int deleteUserServiceWhichNotReport(Long userId);

	/**
	 * 操作表根据用户Id删除新增未上报的域名信息
	 * 
	 */
	int deleteUserServiceDomainWhichNotReport(Long userId);

    /**
     * 根据服务信息ID更新域名信息
     */
    int updateDomainByServiceId(ServiceDomainInformation dto);


}
