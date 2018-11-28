package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.dto.UserVirtualInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.dic.DictionaryMapper;
import com.aotain.serviceapi.server.dao.preinput.UserInfoMapper;
import com.aotain.serviceapi.server.dao.preinput.UserVirtualMachineMapper;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.PreUserVirtualService;
import com.aotain.serviceapi.server.util.StringUtil;
import com.aotain.serviceapi.server.validate.IUserVirtualMachineValidator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class PreUserVirtualServiceImpl extends BaseService implements PreUserVirtualService {
	
	@Autowired
	private UserVirtualMachineMapper userVirtualMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	private Logger logger = Logger.getLogger(PreUserVirtualServiceImpl.class);

	public PreUserVirtualServiceImpl(IUserVirtualMachineValidator baseValidator) {
		super(baseValidator);
	}

	@Override
	protected int insert(BaseModel baseModel) {
		UserVirtualInformation dto = (UserVirtualInformation) baseModel;
		return userVirtualMapper.insertUserVirtualInfo(dto);
	}

	@Override
	protected int update(BaseModel baseModel) {
		UserVirtualInformation virtual = (UserVirtualInformation) baseModel;
		UserVirtualInformation dto = userVirtualMapper.findByVirtualId(virtual.getVirtualId());
		if (dto.getDealFlag() == 1 && dto.getCzlx() == 1) {// 如果是新增上报成功则修改成变更状态
			virtual.setCzlx(2);
		}
		return userVirtualMapper.updateVirtualInfoById(virtual);
	}

	@Override
	protected int delete(BaseModel baseModel) {
		return 0;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public ResultDto updateData(List<UserVirtualInformation> dtos) throws Exception {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.MODIFY.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			logger.info("validate user virtual success!");
			return updateVirtualData(dtos);
		} else {
			logger.info("validate user virtual failed!");
			return validateResult;
		}
		/*ResultDto resultDto = new ResultDto();
		if(virtual==null){
			return resultDto;
		}
		UserVirtualInformation dto=userVirtualMapper.findByVirtualId(virtual.getVirtualId());
		if(dto.getDealFlag()==1&&dto.getCzlx()==1){//如果是新增上报成功则修改成变更状态
			virtual.setCzlx(2);
		}
		int result = update(virtual);
		if(result<0){
			return getErrorResult(MODEL+ErrorCodeConstant.ERROR_1014);
		}
		updateUserMainInfoWhieSubsetInfoChange(virtual.getUserId());
		return getSuccessResult();*/
	}

	private ResultDto updateVirtualData(List<UserVirtualInformation> dtos) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (UserVirtualInformation dto : dtos) {
				try {
					int flag = update(dto);
					if (flag <= 0) {
						// 插入或新增失败
						return getErrorResult("更新记录失败");
					}
					updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
				} catch (Exception e) {
					logger.error("update user virtual failed!", e);
					return getErrorResult("更新记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

	@Override
	public ResultDto batchDeleteDatas(List<UserVirtualInformation> dtos) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos == null || dtos.size() <= 0) {
			return validateResult;
		}
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.DELETE.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			logger.info("validate user virtual success!");
			return deleteUserVirtuals(dtos);
		} else {
			logger.info("validate user virtual failed!");
			return validateResult;
		}
		/*if (virtualIds == null || virtualIds.size() <= 0) {
			return null;
		}
		List<ResultDto> resultDtos = new ArrayList<ResultDto>();
		ResultDto resultDto = null;
		for (long virtualId : virtualIds) {
			resultDto = new ResultDto();
			deleteVirtualInfoByVirtualId(resultDto, virtualId);
			resultDtos.add(resultDto);
		}
		return getBatchResult(JSONArray.toJSONString(resultDtos));*/
	}

	private ResultDto deleteUserVirtuals(List<UserVirtualInformation> dtos) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		List<Integer> successList = new ArrayList<Integer>();
		if (dtos != null && dtos.size() > 0) {
			for (UserVirtualInformation info : dtos) {
				try {
					deleteVirtualInfoByVirtualId(result, info.getVirtualId());
					successList.add(ResultDto.ResultCodeEnum.SUCCESS.getCode());
				} catch (Exception e) {
					logger.error("delete user virtual virtual id:" + info.getVirtualId() + ", userid: " + info.getUserId() + " ERROR", e);
					result.setResultMsg("删除记录失败");
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

	private void deleteVirtualInfoByVirtualId(ResultDto resultDto, long virtualId) {
		UserVirtualInformation dto = userVirtualMapper.findByVirtualId(virtualId);
		if (dto.getCzlx() == 1 && dto.getDealFlag() == 0) {// 新增未上报
			// 仅删除虚拟主机信息
			int result = userVirtualMapper.deleteByVirtulId(virtualId);
			if (result <= 0) {
				resultDto.setResultMsg("delete virtualId: " + virtualId + " ERROR");
			}
		} else {
			try {
				// 1.虚拟机的状态变更为删除未上报
				dto.setCzlx(3);// 删除
				dto.setDealFlag(0);// 未上报
				userVirtualMapper.updateVirtualInfoById(dto);

				// 2.将用户主体修改成变更未预审状态
				updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
			} catch (Exception e) {
				resultDto.setResultMsg("delete virtualId: " + virtualId + " ERROR");
			}
		}
	}

	@Override
	public PageResult<UserVirtualInformationDTO> getUserVirtualList(UserVirtualInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<UserVirtualInformationDTO> pageResult = new PageResult<UserVirtualInformationDTO>();
		// 两个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				|| StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return pageResult;
		}
		 List<UserVirtualInformationDTO> info = new ArrayList<UserVirtualInformationDTO>();
	        if(dto.getIsPaging().equals(1)){
	            PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
	            info = userVirtualMapper.getIndexUserVirtual(dto);
	            PageInfo<UserVirtualInformationDTO> result = new PageInfo<UserVirtualInformationDTO>(info);
	            pageResult.setTotal(result.getTotal());
	        }else{
	            info = userVirtualMapper.getIndexUserVirtual(dto);
	        }
	    pageResult.setRows(info);
	    return pageResult;
	}

	@Override
	public  Map<Integer,ResultDto>  imporUserVirData(  Map<Integer,UserVirtualInformationDTO> dtos ) {

		if (dtos!=null && !dtos.isEmpty()) {
			Map<Integer,ResultDto> resultDtoMap = new HashMap<>();
			Set<Integer> ks = dtos.keySet();
			for (Integer key:ks) {
				try {

					UserVirtualInformationDTO dto = dtos.get(key);
					UserInformationDTO userInformation = userInfoMapper.getDataByUserName(dto.getUserName());
					if (userInformation != null) {
						if (userInformation.getNature()==1) {//提供互联网应用服务的用户
							try {
								if (!StringUtil.isEmptyString(dto.getHouseName())) {
									Long houseId = dictionaryMapper.getHouseIdByHouseName(dto.getHouseName());
									if (houseId==null){
										resultDtoMap.put(key, getErrorResult("机房"+dto.getHouseName()+"在系统中找不到"));
										continue;
									}else {
										dto.setHouseId(houseId);
									}
								}else{
									resultDtoMap.put(key, getErrorResult("机房为空"));
									continue;
								}
								dto.setUserId(userInformation.getUserId());

								resultDtoMap.put(key, batchInsertDatas(Arrays.asList(dto),userInformation.getUserId(),true));
							} catch (Exception e) {
								logger.error("imporUserVirData error", e);
								resultDtoMap.put(key, getErrorResult("导入出错，请重试"));
							}
						}
					} else {
						resultDtoMap.put(key,getErrorResult("关联的用户主体信息导入异常，无法关联导入此子节信息"));
					}
				}catch (Exception e){
					logger.error("import single user virtual error ",e );
					resultDtoMap.put(key,getErrorResult("网络异常"));
				}
			}
			return resultDtoMap;
		}
		return null;
	}

	@Transactional
	public ResultDto batchInsertDatas(List<UserVirtualInformation> virtuals , Long userId, boolean allowInsert) throws Exception {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (virtuals == null || virtuals.size() == 0) {
			return result;
		}
		//重复性校验
		Map<String,Object> virtualMap = new HashMap<String,Object>();
        for(UserVirtualInformation dto:virtuals){
        	if(virtualMap.containsKey("CODE"+dto.getVirtualNo())){
        		result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
        		result.setResultMsg("存在重复虚拟机编号:"+dto.getVirtualNo());
				return result;
        	}else{
        		virtualMap.put("CODE"+dto.getVirtualNo(), "CODE"+dto.getVirtualNo());
        	}
        	if(virtualMap.containsKey("NAME_"+dto.getName())){
        		result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
				result.setResultMsg("存在重复主机名:"+dto.getName());
				return result;
        	}else{
        		virtualMap.put("NAME_"+dto.getName(), "NAME_"+dto.getName());
        	}
        }
		
		//校验
		boolean success = false;
		List<Integer> successList = new ArrayList<Integer>(virtuals.size());
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>(virtuals.size());
		for (int i = 0; i < virtuals.size(); i++) {
			UserVirtualInformation dto = virtuals.get(i);
			dto.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
			if (userId != null) {
				dto.setUserId(userId);
			}
			ResultDto validateResult = validateResult(dto);
			validateResultMap.put(String.valueOf(i), validateResult.getAjaxValidationResult());
			successList.add(validateResult.getResultCode());
			
			//核验成功的对象加入允许持久化
			if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
				if (allowInsert) {
					result = insertData(dto);
					logger.info("insert user service success!");
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
		result.setAjaxValidationResultMap(validateResultMap);
		if (success && !allowInsert) {
			int index = 0;
			for (UserVirtualInformation dto : virtuals) {
				if (userId != null) {
					dto.setUserId(userId);
				}
				ResultDto result2 = insertData(dto);
				if(result2.getResultCode()!=0){
					return result2;
				}
				result.getAjaxValidationResultMap().get((index++) + "").setPid(dto.getVirtualId());
			}
		} 
		return result;
		/*if (virtuals == null || virtuals.size() <= 0) {
			return getErrorResult("提交数据" + ErrorCodeConstant.ERROR_1009.getErrorMsg());
		}
		List<ResultDto> resultDtos = new ArrayList<ResultDto>();
		ResultDto resultDto = null;
		for (UserVirtualInformation dto : virtuals) {
			resultDto = insertData(dto);
			resultDtos.add(resultDto);
		}
		return getBatchResult(JSONArray.toJSONString(resultDtos));*/
	}

	@Transactional(rollbackFor = Exception.class)
	public ResultDto insertData(UserVirtualInformation dto) throws Exception {
		int result = insert(dto);
		if (result <= 0) {
			return getErrorResult("新增记录失败");
		}
		// 更新用户主体信息
		if (dto.getUserId() == null) {
			return getErrorResult("新增记录失败");
		}
		updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
		return getSuccessResult();
	}

	@Override
	public ResultDto insertData(List<UserVirtualInformation> dtos, Long userId) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (UserVirtualInformation dto : dtos) {
				try {
					if (userId != null) {
						dto.setUserId(userId);
					}
					resultDto = insertData(dto);
					logger.info("insert user bandwidth success!");
				} catch (Exception e) {
					logger.error("insert user bandwidth data failed!", e);
					return getErrorResult("新增记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

	public int updateRelativeDataInHouseDeleteByUserIdAndHouseId(long userId,long houseId){
		return userVirtualMapper.updateUserVirtualInfoByUserIdAndHouseId(userId,houseId);
	}

}
