package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/12
 */
public interface IUserNetworkValidator extends IBaseValidator{
    /**
     * 对应的机房id是否存在
     * @param dto
     * @return
     */
    boolean existHouseId(UserBandwidthInformationDTO dto);

    /**
     * 对应的用户id是否存在
     * @param dto
     * @return
     */
    boolean existUserId(UserBandwidthInformationDTO dto);
}
