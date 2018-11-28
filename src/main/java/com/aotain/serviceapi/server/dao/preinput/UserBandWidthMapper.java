package com.aotain.serviceapi.server.dao.preinput;

import java.util.List;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName 用户带宽资源
 * @Author tanzj
 * @Date 2018/8/9
 **/
@MyBatisDao
public interface UserBandWidthMapper {

    int insert(UserBandwidthInformation record);

    List<UserBandwidthInformationDTO> getUserBandWidthList(UserBandwidthInformationDTO record);

    int update(UserBandwidthInformation record);

    int deleteByHHId(Long hhid);

    UserBandwidthInformationDTO getUserBandWidthInfoById(Long hhid);

	UserBandwidthInformationDTO uniqueBandwidth(UserBandwidthInformation dto);

    UserBandwidthInformationDTO getUserBindInfoByHouseIdAndUserId( UserBandwidthInformationDTO dto);

    /**
     * 根据用户Id和隶属地市码查询
     * @param record
     * @return
     */
    List<UserBandwidthInformationDTO> getUserBandWidthByUserId(UserBandwidthInformationDTO record);

}
