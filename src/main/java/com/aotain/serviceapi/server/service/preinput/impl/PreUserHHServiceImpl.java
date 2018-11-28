package com.aotain.serviceapi.server.service.preinput.impl;

import com.aotain.serviceapi.server.dao.preinput.UserInfoMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import com.aotain.serviceapi.server.dao.preinput.UserHHMapper;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.PreUserHHService;
import com.aotain.serviceapi.server.validate.IUserNetworkValidator;


@Service
public class PreUserHHServiceImpl extends BaseService implements PreUserHHService {

	private static final String MODEL = "占用机房信息";
	@Autowired
	private UserHHMapper userHHMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private Logger log = Logger.getLogger(PreUserHHServiceImpl.class);
	
	public PreUserHHServiceImpl(IUserNetworkValidator baseValidator) {
		super(baseValidator);
	}

	@Override
	protected int insert(BaseModel baseModel) {
		return 0;
	}

	@Override
	protected int update(BaseModel baseModel) {
		UserBandwidthInformation dto = (UserBandwidthInformation)baseModel;
		return userHHMapper.updateUserHHInfoByHHid(dto);
	}

	@Override
	protected int delete(BaseModel baseModel) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*@Override
	public ResultDto updateData(UserBandwidthInformation dto) {
		try {
			ResultDto resultDto = new ResultDto();
			if(dto==null){
				return resultDto;
			}
			//
			UserBandwidthInformation information = userHHMapper.findByHHid(dto.getHhId());
			if(information.getCzlx()==1&&information.getDealFlag()==1){//新增已上报
				dto.setCzlx(2);//修改成变更状态
			}
			int result = update(dto);
			if(result<0){
				return getErrorResult(MODEL+ErrorCodeConstant.ERROR_1014);
			}
			
			updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
		} catch (Exception e) {
			log.error("insert UserVirtual info to DB ERROR.dto="+dto.getHhId(),e);
			return getErrorResult();
		}
		
		return getSuccessResult();
	}*/




	/**
	 * 用户是否重复占用机房
	 * 
	 */
	private boolean isExistUserHHInfo(UserBandwidthInformation dto) {
		int result = userHHMapper.findUserHHInfoCount(dto);
		if(result>0){
			return true;
		}
		return false;
	}

	public int updateRelativeDataInHouseDeleteByUserIdAndHouseId(Long userId,Long houseId){
		return userHHMapper.updateUserHHInfoByUserIdAndHouseId(userId,houseId);
	}

}
