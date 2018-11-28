package com.aotain.serviceapi.server.service.report;

import com.alibaba.fastjson.JSON;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.dao.report.IdcInfoMapper;
import com.aotain.serviceapi.server.dao.report.RptBaseUserMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseMapper;
import com.aotain.serviceapi.server.service.CommonService;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class IdcInfoServiceImpl extends CommonService implements IdcInfoService{

    private Logger logger = LoggerFactory.getLogger(IdcInfoServiceImpl.class);

    @Autowired
    private IdcInfoMapper idcInfoMapper;

    @Autowired
    private RptUserInfoService rptUserInfoService;

    @Autowired
    private RptBaseUserMapper rptBaseUserMapper;

    @Autowired
    private RptHouseService rptHouseService;

    @Autowired
    private RptIsmsBaseHouseMapper rptIsmsBaseHouseMapper;



    @Override
    public ResultDto add( IdcInformation idcInformation ) throws Exception {
		if (idcInformation != null) {
			int count = idcInfoMapper.existCount(idcInformation);
			if (count > 0) {
				logger.info("the idc info had already exit", JSON.toJSONString(idcInformation));
			}
			idcInformation.setUpdateTime(new Date());
			idcInfoMapper.insert(idcInformation);

			// 机房实体列表
			List<HouseInformation> houseList = idcInformation.getHouseList();
			if (houseList != null && !houseList.isEmpty()) {
				houseList.forEach(rptHouseService::insert);
			}
			// 用户实体列表
			List<UserInformation> userList = idcInformation.getUserList();
			if (userList != null && !userList.isEmpty()) {
				userList.forEach(userInformation -> {
					try {
						rptUserInfoService.insert(userInformation);
					} catch (Exception e) {
						logger.error("insert user info error", e);
						logger.error("user info is " + JSON.toJSONString(userInformation));
					}
				});
			}
		}
		return successRep();
    }

    @Override
    public ResultDto update( IdcInformation idcInformation ) throws Exception {
        if (idcInformation!=null){
            int count = idcInfoMapper.existCount(idcInformation);
            if(count!=1){
                logger.error("the idc info is not exit", JSON.toJSONString(idcInformation));
                throw new Exception("the idc info is not exit");
            }
            idcInformation.setUpdateTime(new Date());
            idcInfoMapper.update(idcInformation);
        }

        //机房实体列表
        List<HouseInformation> houseList = idcInformation.getHouseList();
        if(houseList!=null &&!houseList.isEmpty()){
            houseList.forEach(houseInformation -> {
                try {
                    rptHouseService.update(houseInformation);
                } catch (Exception e) {
                    logger.error("update house info error",e);
                }
            });
        }
        //用户实体列表
        List<UserInformation> userList = idcInformation.getUserList();
        if(userList!=null &&!userList.isEmpty()){
            userList.forEach(userInformation -> {
                try {
                    rptUserInfoService.modify(userInformation);
                } catch (Exception e) {
                    logger.error("update user info error",e);
                }
            });
        }

        return successRep();
    }

    @Override
    public ResultDto delete( Integer jyzid ) {
        if (jyzid!=null){
            List<UserInformation> userInformations = rptBaseUserMapper.selectByJyzId(jyzid.longValue());

            //用户
            if(userInformations!=null && !userInformations.isEmpty()){
                userInformations.forEach(userInformation -> {
                    try {
                        rptUserInfoService.delete(userInformation.getUserId());
                    } catch (Exception e) {
                        logger.error("delete user info error ",e);
                    }
                });
            }

            List<Integer> houseIds = rptIsmsBaseHouseMapper.selectByJyzId(jyzid);
            if(houseIds!=null && !houseIds.isEmpty()){
                houseIds.forEach(houseId -> {
                    try {
                        rptHouseService.delete(houseId);
                    } catch (Exception e) {
                        logger.error("delete user info error ",e);
                    }
                });
            }

            idcInfoMapper.delete(jyzid);
            return successRep();
        }
        logger.error("the jyz{id: "+jyzid+" } is not exit ");
        return successRef();
    }


}
