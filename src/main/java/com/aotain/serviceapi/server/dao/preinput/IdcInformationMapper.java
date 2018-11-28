package com.aotain.serviceapi.server.dao.preinput;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.serviceapi.server.dao.BaseMapper;

import java.util.List;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
@MyBatisDao
public interface IdcInformationMapper extends BaseMapper<IdcInformation> {

    /**
     * 根据jyzId查询记录
     * @param dto
     * @return
     */
    IDCInformationDTO findByJyzId(IdcInformation dto);

    /**
     * 根据经营者名称查询记录
     * @param dto
     * @return
     */
    IDCInformationDTO findByProviderName(IdcInformation dto);
    /**
     * 根据idcId查询记录
     * @param dto
     * @return
     */
    IDCInformationDTO findByIdcId(IdcInformation dto);
    /**
     * 根据idcName查询记录
     * @param dto
     * @return
     */
    IDCInformationDTO findByIdcName(IdcInformation dto);
}
