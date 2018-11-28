package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;
import com.aotain.serviceapi.server.constant.ValidateResult;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
public interface IHouseIpSegmentValidator extends IBaseValidator{
    /**
     * 对应的机房id是否存在
     * @param dto
     * @return
     */
    boolean existHouseId(HouseIPSegmentInformation dto);

    /**
     * 对应的机房名称是否存在
     * @param dto
     * @return
     */
    boolean existHouseName(HouseIPSegmentInforDTO dto);

    /**
     * ip地址段冲突校验
     *  -- 非专线机房下不能有专线ip；
     *  -- 不同使用方式的IP地址段不能有交集；
     *  -- 专线或者云虚拟内的不同用户占用的IP段可以产生交集
     * @param dto
     * @return
     */
    ValidateResult ipConflictCheck(HouseIPSegmentInformation dto);
}
