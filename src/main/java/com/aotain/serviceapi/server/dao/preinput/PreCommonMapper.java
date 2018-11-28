package com.aotain.serviceapi.server.dao.preinput;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.model.*;

@MyBatisDao
public interface PreCommonMapper {

    /**
     * 等待流水表
     * @param baseModel
     */
    public void writeLog( BaseModel baseModel );

    /**
     * 写等待表基础
     */
    public void writeUserLog(UserInformation userInformation);
    public void writeUserBandLog( UserBandwidthInformation userBandwidthInformation);
    public void writeUserServiceLog( UserServiceInformation userServiceInformation);
    public void writeUserServiceDomainLog(ServiceDomainInformation serviceDomainInformation);
    public void writeUserVirtualLog(UserVirtualInformation userVirtualInformation);
}
