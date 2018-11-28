package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.CacheIsmsBaseInfo;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;
import com.aotain.cu.serviceapi.model.HouseUserFrameInformation;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.preinput.CacheIsmsBaseInfoMapper;
import com.aotain.serviceapi.server.dao.preinput.HouseFrameMapper;
import com.aotain.serviceapi.server.dao.preinput.UserPrincipalMapper;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.PreHouseFrameService;
import com.aotain.serviceapi.server.service.preinput.PreUserInfoService;
import com.aotain.serviceapi.server.validate.IHouseFrameValidator;


@Service
public class PreHouseFrameServiceImpl extends BaseService implements PreHouseFrameService {

	@Autowired
	private HouseFrameMapper frameMapper;
	
	@Autowired
	private UserPrincipalMapper userPrincipalMapper;
	
	@Autowired
	private CacheIsmsBaseInfoMapper cacheMapper;
	
	@Autowired
	private PreUserInfoService userInfoService;
	
	private Logger log = Logger.getLogger(PreHouseFrameServiceImpl.class);
	
	public PreHouseFrameServiceImpl(IHouseFrameValidator baseValidator) {
		super(baseValidator);
	}
	
	@Transactional
	@Override
	public ResultDto insertData(List<HouseFrameInformation> dtos, Long houseId) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (HouseFrameInformation dto : dtos) {
				try {
					if (houseId != null) {
						dto.setHouseId(houseId);
					}
					int result = insert(dto);
					if (result <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert house rack success!");
					// 判定机房主体状态信息，并同时判断是否需要写机房缓存表
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					log.info("update house czlx and deal_flag success!");
					// 对于已分配的用户要写用户机架信息，当用户处于上报状态需更新用户信息
					if (dto.getUserFrameList() != null && dto.getUserFrameList().size() > 0) {
						for (HouseUserFrameInformation userFrame : dto.getUserFrameList()) {
							// 判定用户状态并更新
							dealFrameUserWhileFrameUpdate(dto, userFrame);
						}
					}
				} catch (Exception e) {
					log.error("insert house frame data failed!", e);
					return getErrorResult("新增记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}


	/**
	 * 
	 * 当机房机架修改时，级联用户作出相应操作
	 */
	private ResultDto dealFrameUserWhileFrameUpdate(HouseFrameInformation dto, HouseUserFrameInformation userFrame) {
		if (dto != null && userFrame != null) {
			UserInformation userInfo = new UserInformation();
			if (userFrame.getUserName() != null) {
				userInfo.setUnitName(userFrame.getUserName());
			}
			if (userFrame.getIdType() != null) {
				userInfo.setIdType(userFrame.getIdType());
			}
			if (userFrame.getId() != null) {
				userInfo.setIdNumber(userFrame.getIdNumber());
			}
			UserInformation userInformation = userPrincipalMapper.findByUnitNameAndIdTypeAndNumber(userInfo);
			if (userInformation != null) {
				CacheIsmsBaseInfo userCache = new CacheIsmsBaseInfo();
				if(dto.getUpdateUserId()!=null){
					userCache.setCreateUserId(dto.getUpdateUserId().longValue());
				}else{
					userCache.setCreateUserId(dto.getCreateUserId().longValue());
				}
				userCache.setHouseId(dto.getHouseId());
				userCache.setUserId(userInformation.getUserId());
				// dealUserWhileHouseInfoChange(userInformation, userCache);
				// 查看缓存信息表是否有该用户的等待记录
				CacheIsmsBaseInfo cacheResult = cacheMapper.findByUserId(userInformation.getUserId());
				if ((userInformation.getDealFlag() == HouseConstant.DealFlagEnum.REPORTING.getValue()||userInformation.getDealFlag() == HouseConstant.DealFlagEnum.CHECKING.getValue()) && cacheResult == null) {// 机架的所属客户为提交上报或者上报审核中，并且缓存中没有相应用户的缓存信息，写等待缓存表
					try {
						cacheMapper.insert(userCache);
					} catch (Exception e) {
						log.error("insert UserMainInfo to DB Cache table ERROR.cache=" + JSON.toJSONString(userCache), e);
						return getErrorResult("新增记录失败");
					}
				} else if (userInformation.getDealFlag() == HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue()) {// 机架的所属客户为上报成功，用户自动提交预审
					userInfoService.approve(userInformation.getUserId() + "");
				}
			}
		}
		return getSuccessResult();
	}

	/**
	 * 
	 * 当机房相关信息修改时对关联用户做级联相应处理
	 *//*
	private ResultDto dealUserWhileHouseInfoChange(UserInformation userInformation, CacheIsmsBaseInfo cache) {
		//查看缓存信息表是否有该用户的等待记录
		CacheIsmsBaseInfo  cacheResult = cacheMapper.findByUserId(userInformation.getUserId());
		if(userInformation.getDealFlag()==1&&cacheResult==null){//机架的所属客户为提交上报，并且缓存中没有相应用户的缓存信息，写等待缓存表
			try {
				cacheMapper.insert(cache);
			} catch (Exception e) {
				log.error("insert UserMainInfo to DB Cache table ERROR.cache="+cache.toString(),e);
				return getErrorResult(" insert UserCacheINfo ERROR");
			}
		}else if(userInformation.getDealFlag()==4){//机架的所属客户为上报成功，用户自动提交预审
		}
		return getSuccessResult();
	}*/

	@Override
	public ResultDto batchInsertFrameInfos(List<? extends HouseFrameInformation> racks, Long houseId, boolean allowInsert) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (racks == null || racks.size() == 0) {
			return result;
		}
		//数据重复性校验
		Map<String,Object> frameMap = new HashMap<String,Object>();
		for (HouseFrameInformation obj:racks) {
			if(frameMap.containsKey(obj.getFrameName())){
				result = new ResultDto();
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
				result.setResultMsg("存在重复机架名称："+obj.getFrameName());
				return result;
			}else{
				frameMap.put(obj.getFrameName(), obj.getFrameName());
			}
			if(obj.getUserFrameList()!=null&&obj.getUserFrameList().size()>0){
				for(HouseUserFrameInformation uFdto:obj.getUserFrameList()){
					if(uFdto.getUserName()!=null&&uFdto.getUserName()!=""){
						String key = obj.getFrameName()+uFdto.getUserName();
						if(frameMap.containsKey(key)){
							result = new ResultDto();
							result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
							result.setResultMsg("机架'"+obj.getFrameName()+"'重复录入单位名称："+uFdto.getUserName());
							return result;
						}else{
							frameMap.put(key,key);
						}
					}
				}
			}
		}
		
		//校验
		boolean success = false;
		List<Integer> successList = new ArrayList<Integer>(racks.size());
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>(racks.size());
		for (int i = 0; i < racks.size(); i++) {
			HouseFrameInformation dto = racks.get(i);
			dto.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
			if (houseId != null) {
				dto.setHouseId(houseId);
			}
			ResultDto validateResult = validateResult(dto);
			validateResultMap.put(String.valueOf(i), validateResult.getAjaxValidationResult());
			successList.add(validateResult.getResultCode());
			
			//核验成功的对象加入允许持久化
			if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
				if (allowInsert) {
					int flag = insert(dto);
					if (flag <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert house rack success!");
					// 判定机房主体状态信息，并同时判断是否需要写机房缓存表
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					log.info("update house czlx and deal_flag success!");
					// 对于已分配的用户要写用户机架信息，当用户处于上报状态需更新用户信息
					if (dto.getUserFrameList() != null && dto.getUserFrameList().size() > 0) {
						for (HouseUserFrameInformation userFrame : dto.getUserFrameList()) {
							// 判定用户状态并更新
							dealFrameUserWhileFrameUpdate(dto, userFrame);
						}
					}
				}
			} 
		}
		if (!successList.contains(ResultDto.ResultCodeEnum.ERROR.getCode())) {
			//全部校验通过
			success = true;
			result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
		} else {
			//部分校验通过
			success = false;
			result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		}
		//用于返回新增ID
		result.setAjaxValidationResultMap(validateResultMap);
		if (success && !allowInsert) {
			for (int n = 0; n < racks.size(); n++) {
				if (houseId != null) {
					racks.get(n).setHouseId(houseId);
				}
				int flag = insert(racks.get(n));
				if (flag <= 0) {
					return getErrorResult("新增记录失败");
				}
				log.info("insert house rack success!");
				// 判定机房主体状态信息，并同时判断是否需要写机房缓存表
				dealHouseMainWhileISubInfoUpdate(racks.get(n).getHouseId());
				log.info("update house czlx and deal_flag success!");
				// 对于已分配的用户要写用户机架信息，当用户处于上报状态需更新用户信息
				if (racks.get(n).getUserFrameList() != null && racks.get(n).getUserFrameList().size() > 0) {
					for (HouseUserFrameInformation userFrame : racks.get(n).getUserFrameList()) {
						// 判定用户状态并更新
						dealFrameUserWhileFrameUpdate(racks.get(n), userFrame);
					}
				}
				//返回新增ID
				result.getAjaxValidationResultMap().get(String.valueOf(n)).setPid(racks.get(n).getFrameId());
			}
		} 
		return result;
	}

	@Override
	public ResultDto batchUpdate(List<? extends HouseFrameInformation> dtos) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.MODIFY.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate house frame success!");
			return updateData(dtos);
		} else {
			log.info("validate house frame failed!");
			return validateResult;
		}
	}
	
	private ResultDto updateData(List<? extends HouseFrameInformation> dtos) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (HouseFrameInformation dto : dtos) {
				try {
					HouseFrameInformation po = frameMapper.findFrameInfoByFrameIdOrFrameName(dto.getFrameId(), null);
					if (po.getDealFlag() != null && po.getDealFlag() == 1) {// 链路已报备后修改变更未上报
						dto.setCzlx(HouseConstant.CzlxEnum.MODIFY.getValue());
					}
					int result = update(dto);
					if (result <= 0) {
						return getErrorResult("更新记录失败");
					}
					log.info("update house rack success!");
					// 3.判定机房主体状态信息，并同时判断是否需要写机房缓存表
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					log.info("update house czlx and deal_flag success!");
					// 4.对于已分配的用户要写用户机架信息，当用户处于上报状态需更新用户信息
					if (dto.getUserFrameList() != null && dto.getUserFrameList().size() > 0) {
						for (HouseUserFrameInformation userFrame : dto.getUserFrameList()) {
							// 判定用户状态并更新
							dealFrameUserWhileFrameUpdate(dto, userFrame);
						}
					}
					//对之前关联的用户进行处理
					if (po.getUserFrameList() != null && po.getUserFrameList().size() > 0) {
						for (HouseUserFrameInformation userFrame : po.getUserFrameList()) {
							// 判定用户状态并更新
							dealFrameUserWhileFrameUpdate(po, userFrame);
						}
					}
				} catch (Exception e) {
					log.error("update house frame data failed!", e);
					return getErrorResult("新增记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

	@Transactional
	@Override
	protected int insert(BaseModel baseModel) {
		HouseFrameInformation dto = (HouseFrameInformation)baseModel;
		int result = 0;
		try {
			if(dto.getUserFrameList()!=null&&dto.getUserFrameList().size()>0){//机架上有用户分配，则机架的分配状态改为已分配
				dto.setDistribution(2);
			} else {
				dto.setDistribution(1);
			}
			result = frameMapper.insertHouseFrameInfomation(dto);
			if(dto.getUserFrameList()!=null&&dto.getUserFrameList().size()>0){//新增用户机架信息
				for(HouseUserFrameInformation userFrame : dto.getUserFrameList()){
					userFrame.setFrameId(dto.getFrameId());
					userFrame.setHouseId(dto.getHouseId());
					userFrame.setCreateUserId(dto.getCreateUserId());
					frameMapper.insertUserFrame(userFrame);
				}
			}
		} catch (Exception e) {
			log.error("insert preHouseFrameInfo to DB ERROR.dto："+dto.toString()+e);
		}
		return result;
	}

	@Transactional
	@Override
	protected int update(BaseModel baseModel) {
		HouseFrameInformation dto = (HouseFrameInformation) baseModel;
		int result = 0;
		try {
			if(dto.getUserFrameList()!=null&&dto.getUserFrameList().size()>0){
				dto.setDistribution(2);
			}else{
				dto.setDistribution(1);
			}
			result = frameMapper.updateHouseFrameInfomation(dto);
			frameMapper.deleteUserFrameByFrameId(dto.getFrameId());
			if (dto.getUserFrameList() != null && dto.getUserFrameList().size() > 0) {// 更新用户机架信息
				for (HouseUserFrameInformation userFrame : dto.getUserFrameList()) {
					userFrame.setFrameId(dto.getFrameId());
					userFrame.setHouseId(dto.getHouseId());
					userFrame.setCreateUserId(dto.getUpdateUserId());
					userFrame.setUpdateUserId(dto.getUpdateUserId());
					frameMapper.insertUserFrame(userFrame);
				}
			}
		} catch (Exception e) {
			log.error("update preHouseFrameInfo to DB ERROR.dto：" + dto.toString() + e);
		}
		return result;
	}

	@Override
	protected int delete(BaseModel baseModel) {
		return 0;
	}

	@Override
	public ResultDto batchDelete(List<? extends HouseFrameInformation> racks) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (racks == null || racks.size() <= 0) {
			return validateResult;
		}
		validateResult = validateResult(racks, HouseConstant.OperationTypeEnum.DELETE.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate house racks success!");
			return deleteData(racks);
		} else {
			log.info("validate house racks failed!");
			return validateResult;
		}
	}

	/**
	 * 
	 * 批量处理删除机架
	 */
	private ResultDto deleteData(List<? extends HouseFrameInformation> racks) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		List<Integer> successList = new ArrayList<Integer>();
		if (racks != null && racks.size() > 0) {
			for (HouseFrameInformation rack : racks) {
				try {
					HouseFrameInformation dto = frameMapper.findFrameInfoByFrameIdOrFrameName(rack.getFrameId(), rack.getFrameName());
					List<HouseUserFrameInformation> userFrames = frameMapper.findUserFrameByFrameId(rack.getFrameId());
					if (dto != null) {
						dto.setUserFrameList(userFrames);
					} else {
						log.info("not found the frame info by the frameId:" + rack.getFrameId());
						return result;
					}
					if ((dto.getCzlx() == 1 && dto.getDealFlag() == 0)) {// 新增未上报
						// 仅删除机架信息
						int flag = frameMapper.deleteByFrameIdOrFrameName(rack.getFrameId(), rack.getFrameName());
						if (flag <= 0) {
							result.setResultMsg("删除记录失败");
						}
					} else {
						// 1.机架的状态变更为删除未上报
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("frameId", rack.getFrameId());
						map.put("czlx", 3);// 删除
						map.put("dealFlag", 0);// 未上报
						frameMapper.updateFrameStatusByframeId(map);

					}
					// 3.将机房修改成变更未预审状态
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					
					if (userFrames != null && userFrames.size() > 0) {
						for (HouseUserFrameInformation userFrame : userFrames) {
							// 判定用户状态并更新
							
							dealFrameUserWhileFrameUpdate(rack, userFrame);
						}
					}
					// 机架分配类型为已分配的，物理删除IDC_ISMS_BASE_USER_FRAME表的相应记录
					if (dto.getDistribution() == 2) {
						int flag = frameMapper.deleteUserFrameByFrameId(rack.getFrameId());
						if (flag <= 0) {
							result.setResultMsg("删除记录失败");
						}
					}
					successList.add(ResultDto.ResultCodeEnum.SUCCESS.getCode());
				} catch (Exception e) {
					result.setResultMsg("删除记录失败");
					log.error("delete user frame,  frameId:" + rack.getFrameId() + " ERROR", e);
				}
			}
			if (!successList.contains(ResultDto.ResultCodeEnum.ERROR.getCode())) {
				//全部校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
			} else {
				//部分校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
			}
			return result;
		} else {
			return result;
		}
	}

	/**
	 * 导入机房机架信息
	 * @param
	 * @return
	 */
	public List<ResultDto> importHouseFrameData(List<HouseFrameInformationDTO> dtoList){
		List<ResultDto> resultDtoList = Lists.newArrayList();
		if (dtoList==null || dtoList.isEmpty()){
			return resultDtoList;
		}

		for (int i=0;i<dtoList.size();i++){
			List<HouseFrameInformation> houseFrameInformationList = Lists.newArrayList();

			ResultDto resultDto ;
			HouseFrameInformationDTO houseFrameInformationDTO = new HouseFrameInformationDTO();
			HouseFrameInformationDTO dto = dtoList.get(i);
			if (!StringUtils.isEmpty(dto.getFrameName())){
				houseFrameInformationDTO.setFrameName(dto.getFrameName());
			}
			HouseFrameInformation result = frameMapper.getByFrameName(houseFrameInformationDTO);
			if (result==null){
				houseFrameInformationList.add(dto);
				resultDto =  batchInsertFrameInfos(houseFrameInformationList,dto.getHouseId(),true);
			} else {
				dto.setFrameId(result.getFrameId());
				dto.setOperateType(2);
				houseFrameInformationList.add(dto);
				resultDto = batchUpdate(houseFrameInformationList);
			}
			resultDtoList.add(resultDto);
		}
		return resultDtoList;
	}

	@Override
	public ResultDto insertData(HouseFrameInformationDTO dto){
		ResultDto resultDto = validateResult(dto);
		if (resultDto.getResultCode()==ResultDto.ResultCodeEnum.ERROR.getCode()){
			return resultDto;
		}
		int result = insert(dto);
		if (result>0){
			return getSuccessResult();
		} else {
			return getErrorResult("插入失败");
		}
	}

	@Override
	public ResultDto updateData(HouseFrameInformationDTO dto){
		ResultDto resultDto = validateResult(dto);
		if (resultDto.getResultCode()==ResultDto.ResultCodeEnum.ERROR.getCode()){
			return resultDto;
		}
		int result = update(dto);
		if (result>0){
			return getSuccessResult();
		} else {
			return getErrorResult("更新失败");
		}
	}
}
