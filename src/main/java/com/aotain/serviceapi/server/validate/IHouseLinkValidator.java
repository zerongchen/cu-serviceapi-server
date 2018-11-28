package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.model.HouseGatewayInformation;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
public interface IHouseLinkValidator extends IBaseValidator{
    /**
     * 对应的机房id是否存在
     * @param dto
     * @return
     */
    boolean existHouseId(HouseGatewayInformation dto);

    /**
     * 对应的机房名称是否存在
     * @param dto
     * @return
     */
    boolean existHouseName(HouseGatewayInformationDTO dto);

    /**
     * 对应的链路编号是否唯一
     * @param dto
     * @return
     */
    boolean uniqueLinkNo(HouseGatewayInformation dto);
}
