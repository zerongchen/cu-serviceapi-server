package com.aotain.serviceapi.server.dao.preinput;

import java.util.List;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.UserInformation;


/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/12
 */
@MyBatisDao
public interface UserPrincipalMapper {
    /**
     * 根据houseId查询记录
     * @param dto
     * @return
     */
    UserInformationDTO findByUserId(UserInformationDTO dto);

    /**
     * 根据id查询专线用户
     * @param dto
     * @return
     */
    UserInformationDTO findDedicatedUserById(UserInformationDTO dto);


    /**
     * 根据单位(个人)名称查询用户信息（单位名称可能重名）
     * @param dto
     * @return
     */
    List<UserInformation> findByUnitName(UserInformation dto);

    /**
     * 根据单位(个人)名称和证件类型、证件号码查询用户信息（单位名称可能重名）
     * @param dto
     * @return
     */
   UserInformation findByUnitNameAndIdTypeAndNumber(UserInformation dto);

    /**
     * 根据用户id更新处理状态
     * @param dto
     * @return
     */
   int updateDealFlagByUserId(UserInformation dto);

	int UpdateUserStatusByUserId(UserInformationDTO userInformationDTO);


	/**
	 * 用户服务信息的地市码不包含在用户主体的地市码中的数量
	 * 
	 * @author : songl
	 */
	int findCountNotContainUserServerArea(UserInformation dto);

	/**
	 * 用户网络资源信息的地市码不包含在用户主体的地市码中的数量
	 * 
	 * @author : songl
	 */
	int findCountNotContainUserNetWorkArea(UserInformation dto);

	/**
	 * 虚拟主机信息的地市码不包含在用户主体的地市码中的数量
	 * 
	 * @author : songl
	 */
	int findCountNotContainUserVirutalArea(UserInformation dto);

}
