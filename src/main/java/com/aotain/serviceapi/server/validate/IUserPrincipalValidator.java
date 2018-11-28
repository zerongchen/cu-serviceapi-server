package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/12
 */
public interface IUserPrincipalValidator extends IBaseValidator{

    /**
     * 查询此经营者id对应的服务运营商是否存在
     * @param dto
     * @return
     */
    boolean existJyzId(UserInformationDTO dto);
}
