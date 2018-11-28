package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.model.UserVirtualInformation;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/12
 */
public interface IUserVirtualMachineValidator extends IBaseValidator{

    /**
     * 对应的机房id是否存在
     * @param dto
     * @return
     */
    boolean existHouseId(UserVirtualInformation dto);

    /**
     * 对应的用户id是否存在
     * @param dto
     * @return
     */
    boolean existUserId(UserVirtualInformation dto);

    /**
     * 校验主机编号是否唯一
     * @param dto
     * @return
     */
    boolean uniqueVirtualNo(UserVirtualInformation dto);

    /**
     * 校验虚拟主机名是否唯一
     * @param dto
     * @return
     */
    boolean uniqueName(UserVirtualInformation dto);
}
