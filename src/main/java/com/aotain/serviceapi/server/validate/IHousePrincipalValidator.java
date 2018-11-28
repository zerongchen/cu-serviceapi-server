package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.model.HouseInformation;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
public interface IHousePrincipalValidator extends IBaseValidator{
    /**
     * 查询此经营者id对应的服务运营商是否存在
     * @param dto
     * @return
     */
    boolean existJyzId(HouseInformationDTO dto);

    /**
     * 查询此经营者名称对应的服务运营商是否存在
     * @param dto
     * @return
     */
    boolean existIdcName(HouseInformationDTO dto);

    /**
     * 查询机房名称是否存在
     * @param dto
     * @return
     */
    boolean uniqueHouseName(HouseInformation info);

    /**
     * 查询机房编号是否存在
     * @param dto
     * @return
     */
    boolean uniqueHouseIdStr(HouseInformation info);
}
