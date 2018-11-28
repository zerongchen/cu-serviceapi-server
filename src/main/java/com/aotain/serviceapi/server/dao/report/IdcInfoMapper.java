package com.aotain.serviceapi.server.dao.report;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.model.IdcInformation;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface IdcInfoMapper{

    void insert( IdcInformation idcInformation);
    void update( IdcInformation idcInformation);
    void delete(@Param("id") Integer id);
    int existCount( IdcInformation idcInformation);

    IdcInformation findByIdcId(IdcInformation dto);
    IdcInformation findByIdcName(IdcInformation dto);
	IdcInformation findByJyzId(IdcInformation dto);

    int insertIdcWaitInfo(IdcInformation idcInformation);


}
