package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.model.UserServiceInformation;
import com.aotain.serviceapi.server.constant.ValidateResult;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/16
 */
public interface IUserServiceValidator extends IBaseValidator{

    boolean existUserId(UserServiceInformationDTO dto);

    /**
     * 是否存在相同的域名
     * @param dto
     * @return
     */
    boolean existDomain(UserServiceInformation dto);

    /**
     * 同一用户是否占用相同备案号
     * @param dto
     * @return
     */
    boolean existRegisterIdForSameUser(UserServiceInformation dto);

    /**
     * 接入方式是否正确
     * @param dto
     * @return
     */
    ValidateResult rightSetMode(UserServiceInformationDTO dto);

    /**
     * 业务类型是否正确
     * @param dto
     * @return
     */
    ValidateResult rightBusinessType(UserServiceInformationDTO dto);
}
