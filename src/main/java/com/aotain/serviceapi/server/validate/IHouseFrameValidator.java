package com.aotain.serviceapi.server.validate;

import java.util.Map;

import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
public interface IHouseFrameValidator extends IBaseValidator{
    /**
     * 对应的机房id是否存在
     * @param dto
     * @return
     */
    boolean existHouseId(HouseFrameInformationDTO dto);

    /**
     * 对应的机房名称是否存在
     * @param dto
     * @return
     */
    boolean existHouseName(HouseFrameInformationDTO dto);

    /**
     * 对应的机架名称是否存在
     * @param dto
     * @return
     */
    boolean uniqueFrameName(HouseFrameInformation dto);

    /**
     * 所属单位是否合法
     * @param dto
     * @return
     */
    void suitableUnitName(HouseFrameInformation dto, Map<String, Object[]> errorsArgsMap);
}
