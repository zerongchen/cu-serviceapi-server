package com.aotain.serviceapi.server.service.preinput;

public interface PreUserHHService {

	//ResultDto updateData(UserBandwidthInformation bandwidth);

    /**
     * 删除机房时，将关联数据更新为删除未上报
     * @param userId
     */
    int updateRelativeDataInHouseDeleteByUserIdAndHouseId(Long userId,Long houseId);

}
