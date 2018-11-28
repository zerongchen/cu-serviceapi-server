package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.model.IdcInformation;

/**
 * 经营者校验信息接口类
 *
 * @author bang
 * @date 2018/07/11
 */
public interface IdcInformationValidator extends IBaseValidator{

    /**
     * 经营者id是否唯一
     * @param dto
     * @return
     */
    boolean uniqueIdcId(IdcInformation dto);

    /**
     * 经营者名称是否唯一
     * @param dto
     * @return
     */
    boolean uniqueIdcName(IdcInformation dto);
}
