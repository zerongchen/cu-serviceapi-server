package com.aotain.serviceapi.server.service.preinput;

import com.alibaba.fastjson.JSON;
import com.aotain.common.config.ContextUtil;
import com.aotain.common.config.model.IdcHouses;
import com.aotain.common.config.redis.BaseRedisService;
import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.*;
import com.aotain.cu.serviceapi.model.permission.DataPermission;
import com.aotain.cu.serviceapi.model.permission.DataPermissionSetting;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.preinput.*;
import com.aotain.serviceapi.server.service.preinput.impl.IHouseInformationServiceImpl;
import com.aotain.serviceapi.server.util.SpringUtil;
import com.aotain.serviceapi.server.validate.IBaseValidator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


/**
 * 新增 修改 删除公共操作
 *
 * @author bang
 * @date 2018/07/20
 */
public abstract class BaseService {

    private IBaseValidator baseValidator;
    
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    public BaseService(IBaseValidator baseValidator){
        this.baseValidator = baseValidator;
    }

    public boolean validateSuccess(BaseModel baseModel){
        AjaxValidationResult ajaxValidationResult = baseValidator.preValidate(baseModel);
        if (ajaxValidationResult.getErrorsArgsMap()==null ||
                ajaxValidationResult.getErrorsArgsMap().size()==0){
            return true;
        }
        return false;
    }

    public ResultDto validateResult(BaseModel baseModel) {
    	ResultDto resultDto = new ResultDto();
    	resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
    	AjaxValidationResult result = baseValidator.preValidate(baseModel);
    	if (result != null) {
    		if (result.validateIsSuccess()) {
    			 resultDto.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
    		} 
    		 resultDto.setResultMsg(JSON.toJSONString(result.getErrorsArgsMap().values()));
    	     resultDto.setAjaxValidationResult(result);
    	}
        return resultDto;
    }
    
    public ResultDto validateResult(BaseModel baseModel, IBaseValidator baseValidator) {
    	ResultDto resultDto = new ResultDto();
    	resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
    	AjaxValidationResult result = null;
    	if (baseValidator != null) {
    		result = baseValidator.preValidate(baseModel);
    	} else {
    		return validateResult(baseModel);
    	}
    	if (result != null) {
    		if (result.validateIsSuccess()) {
    			 resultDto.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
    		} 
    		 resultDto.setResultMsg(JSON.toJSONString(result.getErrorsArgsMap().values()));
    	     resultDto.setAjaxValidationResult(result);
    	}
        return resultDto;
    }
    
    public ResultDto validateResult(IBaseValidator baseValidator, List<? extends BaseModel> baseModels, Integer operateType) {
    	ResultDto result = new ResultDto();
    	result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (baseModels != null && baseModels.size() > 0) {
			//校验
			List<Integer> successList = new ArrayList<Integer>(baseModels.size());
			Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>(baseModels.size());
			for (int i = 0; i < baseModels.size(); i++) {
				BaseModel dto = baseModels.get(i);
				dto.setOperateType(operateType);
				ResultDto validateResult = validateResult(dto, baseValidator);
				validateResultMap.put(String.valueOf(i), validateResult.getAjaxValidationResult());
				successList.add(validateResult.getResultCode());
			}
			if (!successList.contains(ResultDto.ResultCodeEnum.ERROR.getCode())) {
				//全部校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
			} else {
				//部分校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
				result.setAjaxValidationResultMap(validateResultMap);
			}
		}
		return result;
    }
    
    public ResultDto validateResult(List<? extends BaseModel> baseModels, Integer operateType) {
    	return validateResult(null, baseModels, operateType);
    }

    /**
     * 获取成功返回结果
     * @return
     */
    public ResultDto getSuccessResult(String msg){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
        resultDto.setResultMsg(msg);
        return resultDto;
    }

    /**
     * 获取成功返回结果
     * @return
     */
    public ResultDto  getSuccessResult(Integer pid){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
        resultDto.setPid(pid);
        return resultDto;
    }

    /**
     * 获取成功返回结果
     * @return
     */
    public ResultDto getSuccessResult(){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
        resultDto.setResultMsg("success");
        return resultDto;
    }

    /**
     * 返回失败结果
     * @return
     */
    public ResultDto getErrorResult(){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
        resultDto.setResultMsg("error");
        return resultDto;
    }

    /**
     * 返回失败结果
     * @return
     */
    public ResultDto getErrorResult(String msg){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
        resultDto.setResultMsg(msg);
        return resultDto;
    }

    /**
     * 更新机房主体信息
     * @param houseInformation
     * @return
     */
    public int updateHouseDealFlagByHouseId(HouseInformation houseInformation) {
        HousePrincipalMapper housePrincipalMapper = ContextUtil.getContext().getBean(HousePrincipalMapper.class);
        return housePrincipalMapper.updateHouseDealFlagByHouseId(houseInformation);
    }


    /**
     * 根据机房信息获取缓存bean实体
     * @param houseInformation
     * @return
     */
    public CacheIsmsBaseInfo getCacheInfoBean(HouseInformation houseInformation){
        CacheIsmsBaseInfo cacheIsmsBaseInfo = new CacheIsmsBaseInfo();
        if (!StringUtils.isEmpty(houseInformation.getHouseId())){
            cacheIsmsBaseInfo.setHouseId(houseInformation.getHouseId());
        }
        if (!StringUtils.isEmpty(houseInformation.getJyzId())){
            cacheIsmsBaseInfo.setJyzId(Long.valueOf(houseInformation.getJyzId()));
        }
        cacheIsmsBaseInfo.setCreateUserId(SpringUtil.getCurrentUserId());
        cacheIsmsBaseInfo.setCreateTime(new Date());
        return cacheIsmsBaseInfo;
    }

    /**
     * 根据用户信息获取缓存bean实体
     * @param userInformation
     * @return
     */
    public CacheIsmsBaseInfo getCacheInfoBean(UserInformation userInformation){
        CacheIsmsBaseInfo cacheIsmsBaseInfo = new CacheIsmsBaseInfo();
        if (!StringUtils.isEmpty(userInformation.getUserId())){
            cacheIsmsBaseInfo.setUserId(userInformation.getUserId());
        }
        if (!StringUtils.isEmpty(userInformation.getJyzId())){
            cacheIsmsBaseInfo.setJyzId(Long.valueOf(userInformation.getJyzId()));
        }
        cacheIsmsBaseInfo.setCreateUserId(SpringUtil.getCurrentUserId());
        cacheIsmsBaseInfo.setCreateTime(new Date());
        return cacheIsmsBaseInfo;
    }

    /**
     * 写缓存数据库表
     * @param cacheIsmsBaseInfo
     * @return
     */
    public int writeCacheInfo(CacheIsmsBaseInfo cacheIsmsBaseInfo) {
        CacheIsmsBaseInfoMapper cacheIsmsBaseInfoMapper = ContextUtil.getContext().getBean(CacheIsmsBaseInfoMapper.class);
        if (cacheIsmsBaseInfoMapper.findByHouseId(cacheIsmsBaseInfo)==null){
            return cacheIsmsBaseInfoMapper.insert(cacheIsmsBaseInfo);
        } else {
            return cacheIsmsBaseInfoMapper.updateByHouseId(cacheIsmsBaseInfo);
        }
    }
    
    /**
     * 获取经营者id
     * @return
     */
    public long obtainJyzId() {
    	IdcInformationMapper idcInformationMapper = ContextUtil.getContext().getBean(IdcInformationMapper.class);
    	List<IdcInformation> idcs = idcInformationMapper.getList(null);
    	if (idcs != null && !idcs.isEmpty()) {
			return idcs.get(0).getJyzId();
		}
		return 0;
    }
	/**
	 * 获取经营者id
	 * @return
	 */
	public IdcInformation obtainJyz() {
		IdcInformationMapper idcInformationMapper = ContextUtil.getContext().getBean(IdcInformationMapper.class);
		List<IdcInformation> idcs = idcInformationMapper.getList(null);
		if (idcs != null && !idcs.isEmpty()) {
			return idcs.get(0);
		}
		return null;
	}
    
    /**
     * 返回批量操作结果
     * @return
     */
    public ResultDto getBatchResult(String msg){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.BATCH_OPERATION.getCode());
        resultDto.setResultMsg(msg);
        return resultDto;
    }
    
    public ResultDto getBatchResult(AjaxValidationResult result){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.BATCH_OPERATION.getCode());
        resultDto.setAjaxValidationResult(result);
        return resultDto;
    }

    protected abstract int insert(BaseModel baseModel);

    protected abstract int update(BaseModel baseModel);

    protected abstract int delete(BaseModel baseModel);
    
    /**
     * 更新用户主体信息
     * @param houseInformation
     * @return
     */
    public int UpdateUserStatuByUserId(UserInformationDTO userInformationDTO) {
    	UserPrincipalMapper userPrincipalMapper = ContextUtil.getContext().getBean(UserPrincipalMapper.class);
        return userPrincipalMapper.UpdateUserStatusByUserId(userInformationDTO);
    }
    
    /**
     * 当用户子集信息发生变更时，级联更新用户主体信息
     * @throws Exception 
     * 
     */
    public void updateUserMainInfoWhieSubsetInfoChange(long userId) throws Exception {
		try {
			UserInformationDTO po = new UserInformationDTO();
			po.setUserId(userId);
			UserPrincipalMapper userPrincipalMapper = ContextUtil.getContext().getBean(UserPrincipalMapper.class);
			CacheIsmsBaseInfoMapper cacheIsmsBaseInfoMapper = ContextUtil.getContext().getBean(CacheIsmsBaseInfoMapper.class);
			UserInformationDTO userDto = userPrincipalMapper.findByUserId(po);
			
			if(userDto.getDealFlag()==HouseConstant.DealFlagEnum.REPORTING.getValue()||userDto.getDealFlag()==HouseConstant.DealFlagEnum.CHECKING.getValue()){//如果用户是提交上报或者上报审核中,写入缓存
				//查看缓存信息表是否有该用户的等待记录
				CacheIsmsBaseInfo  userCache = new CacheIsmsBaseInfo();
				userCache.setUserId(userId);
				CacheIsmsBaseInfo  cacheResult = cacheIsmsBaseInfoMapper.findByUserId(userId);
				if(cacheResult==null){
					userCache.setCreateUserId(SpringUtil.getCurrentUserId());
					userCache.setCreateTime(new Date());
					userCache.setJyzId(userDto.getJyzId().longValue());
					cacheIsmsBaseInfoMapper.insert(userCache);
				}
			}else if(userDto.getDealFlag()==HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue()){//用户主体为上报成功，更新主体用户信息为变更未预审
				userDto.setCzlx(HouseConstant.OperationTypeEnum.MODIFY.getValue());//变更
				userDto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());//未预审
				UpdateUserStatuByUserId(userDto);
			}else{
				userDto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());//未预审
				UpdateUserStatuByUserId(userDto);
			}
		} catch (BeansException e) {
			throw new RuntimeException("更新用户主体异常");
		}
	}
    
    /**
	 * 
	 * 当机房子信息变更时级联处理机房主体信息
	 */
    public void dealHouseMainWhileISubInfoUpdate(Long houseId) {
		HousePrincipalMapper housePrincipalMapper = ContextUtil.getContext().getBean(HousePrincipalMapper.class);
		CacheIsmsBaseInfoMapper cacheMapper = ContextUtil.getContext().getBean(CacheIsmsBaseInfoMapper.class);
		HouseInformationDTO hDto = new HouseInformationDTO();
		hDto.setHouseId(houseId);
		hDto = housePrincipalMapper.findByHouseId(hDto);
		if (HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue().equals(hDto.getDealFlag())) {// 机房为上报成功时更改机房状态
			HouseInformation houseDto = new HouseInformation();
			houseDto.setHouseId(houseId);
			houseDto.setCzlx(HouseConstant.OperationTypeEnum.MODIFY.getValue());// 变更状态
			houseDto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
			housePrincipalMapper.updateHouseDealFlagByHouseId(houseDto);
		} else if (HouseConstant.DealFlagEnum.REPORTING.getValue().equals(hDto.getDealFlag())||HouseConstant.DealFlagEnum.CHECKING.getValue().equals(hDto.getDealFlag())) {//机房主体为上报审核中或者提交上报，写入缓存表
			// 机房处于提交上报
			CacheIsmsBaseInfo caBaseInfo = new CacheIsmsBaseInfo();
			caBaseInfo.setHouseId(hDto.getHouseId());
			caBaseInfo.setJyzId(hDto.getJyzId().longValue());
			CacheIsmsBaseInfo resultCahce = cacheMapper.findByHouseId(caBaseInfo);
			if (resultCahce == null) {// 若缓存表中没有有该机房记录则写入
				cacheMapper.insert(caBaseInfo);
			}
		} else {
			HouseInformation houseDto = new HouseInformation();
			houseDto.setHouseId(houseId);
			houseDto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
			housePrincipalMapper.updateHouseDealFlagByHouseId(houseDto);
		}
	}
	
	/**
     * 
     * 当已报备用户信息变动时级联更新机房主体
     */
	public void dealHouseInfoWhileUserUpdate(UserInformation dto) {
		IHouseInformationServiceImpl houseInformationServiceImpl = ContextUtil.getContext().getBean(IHouseInformationServiceImpl.class);
		CacheIsmsBaseInfoMapper cacheMapper = ContextUtil.getContext().getBean(CacheIsmsBaseInfoMapper.class);
		UserInfoMapper userInfoMapper = ContextUtil.getContext().getBean(UserInfoMapper.class);
		List<HouseInformation> houseList = userInfoMapper.findHaveReportFrameAndIPsegConnectUserInfoByUserInfo(dto);
		for(HouseInformation houseInformation:houseList){
			if(houseInformation.getDealFlag()==HouseConstant.DealFlagEnum.REPORTING.getValue()||houseInformation.getDealFlag()==HouseConstant.DealFlagEnum.CHECKING.getValue()){
				//机房处于提交上报
				CacheIsmsBaseInfo caBaseInfo = new CacheIsmsBaseInfo();
				caBaseInfo.setHouseId(houseInformation.getHouseId());
				caBaseInfo.setJyzId(dto.getJyzId().longValue());
				CacheIsmsBaseInfo resultCahce = cacheMapper.findByHouseId(caBaseInfo);
				if(resultCahce==null){//若缓存表中没有有该机房记录则写入
					cacheMapper.insert(caBaseInfo);
				}
			}else if(houseInformation.getDealFlag()!=0&&houseInformation.getDealFlag()!=2){//不是未预审和上报审核中
				//机房提交预审
				houseInformationServiceImpl.approve(houseInformation.getHouseId()+"");
			}
		}
	}
	
	public void synchronizeAuthHousesToRedis(BaseModel baseModel, int operationType) {
		HouseInformation dto = (HouseInformation) baseModel;
		BaseRedisService<String, String, String> baseRedisService = ContextUtil.getContext().getBean(BaseRedisService.class);
		
		if (HouseConstant.OperationTypeEnum.ADD.getValue() == operationType) {
			IdcHouses idcHouse = new IdcHouses();
			idcHouse.setHouseId(dto.getHouseId());
			idcHouse.setHouseIdStr(dto.getHouseIdStr());
			idcHouse.setHouseName(dto.getHouseName());
			idcHouse.setClusterId(0);
			idcHouse.setIsReport(1);
			idcHouse.setIdcId("A2.B1.B2-20090001");
			idcHouse.setIdentity(dto.getIdentity());
			baseRedisService.putHash("IdcHouses", dto.getHouseIdStr(), JSON.toJSONString(idcHouse));
		}else if(HouseConstant.OperationTypeEnum.MODIFY.getValue() == operationType){
			String dataStr = baseRedisService.getHashValueByHashKey("IdcHouses", dto.getHouseIdStr());
			IdcHouses idcHouse = JSON.parseObject(dataStr,IdcHouses.class);
			idcHouse.setHouseId(dto.getHouseId());
			idcHouse.setHouseIdStr(dto.getHouseIdStr());
			idcHouse.setHouseName(dto.getHouseName());
			idcHouse.setIdentity(dto.getIdentity());
			baseRedisService.putHash("IdcHouses", dto.getHouseIdStr(), JSON.toJSONString(idcHouse));
		} else if (HouseConstant.OperationTypeEnum.DELETE.getValue() == operationType) {
			baseRedisService.removeHash("IdcHouses", dto.getHouseIdStr());
		}
	}

	/**
	 * 同步机房信息
	 * @param baseModel
	 */
	public void synchronizeAllHousesToPermission(BaseModel baseModel){
		HousePrincipalMapper housePrincipalMapper = ContextUtil.getContext().getBean(HousePrincipalMapper.class);
		List<IdcHouses> idcHouses = housePrincipalMapper.findAllHouseIdAndName();
		List<DataPermissionSetting> dataPermissionSettingList = encapsulateDataPermission(baseModel.getDataPermissionSettingList(), idcHouses,baseModel.getDataPermissionId());

		DataPermission dataPermission = new DataPermission();
		dataPermission.setAppId(baseModel.getAppId());
		dataPermission.setAppName("");
		dataPermission.setDataPermissionToken(baseModel.getDataPermissionToken());
		dataPermission.setDataPermissionId(baseModel.getDataPermissionId());
		dataPermission.setDataPermissionName(baseModel.getDataPermissionName());
		dataPermission.setDataPermissionDesc(baseModel.getDataPermissionDesc());
		dataPermission.setSettings(dataPermissionSettingList);
		logger.info("[HouseSynchronize info]:"+JSON.toJSONString(dataPermission));
		getPaassportHttpResult(baseModel.getPermissionMethodUrl()+"/"+baseModel.getUserToken()+"/true", JSON.toJSONString(dataPermission));
	}

	public void synchronizeAuthHousesToPermission(BaseModel baseModel) {
		// 同步机房数据至权限系统
		HouseInformation dto = (HouseInformation) baseModel;
		HousePrincipalMapper housePrincipalMapper = ContextUtil.getContext().getBean(HousePrincipalMapper.class);
		List<IdcHouses> idcHouses = housePrincipalMapper.findAllHouseIdAndName();
		IdcHouses idcHouse = new IdcHouses();
		idcHouse.setHouseId(dto.getHouseId());
		idcHouse.setHouseName(dto.getHouseName());
		boolean isContain = false;
		if (idcHouses != null && idcHouses.size() > 0) {
			for (IdcHouses h : idcHouses) {
				if (h.getHouseId().longValue() == dto.getHouseId().longValue()) {
					isContain = true;
					break;
				}
			}
		}
		if(!(dto.getOperateType()!=null&&dto.getOperateType()==HouseConstant.OperationTypeEnum.DELETE.getValue())&&!isContain){
			idcHouses.add(idcHouse);
		}
		List<DataPermissionSetting> dataPermissionSettingList = encapsulateDataPermission(dto.getDataPermissionSettingList(), idcHouses,dto.getDataPermissionId());
		
		DataPermission dataPermission = new DataPermission();
		dataPermission.setAppId(dto.getAppId());
		dataPermission.setAppName("");
		dataPermission.setDataPermissionToken(dto.getDataPermissionToken());
		dataPermission.setDataPermissionId(dto.getDataPermissionId());
		dataPermission.setDataPermissionName(dto.getDataPermissionName());
		dataPermission.setDataPermissionDesc(dto.getDataPermissionDesc());
		dataPermission.setSettings(dataPermissionSettingList);
		logger.info("[HouseSynchronize info]:" + JSON.toJSONString(dataPermission));
		String token = dto.getUserToken();
		boolean flag = false;
		if (token != null && token != "") {
			flag = true;
		}
		logger.info("[UserToken]:" + dto.getUserToken());
		logger.info("[PermissionMethodUrl]:" + dto.getPermissionMethodUrl());
		getPaassportHttpResult((dto.getPermissionMethodUrl() + "/" + token == null) ? "" : (dto.getPermissionMethodUrl() + "/" + token + "/" + flag), JSON.toJSONString(dataPermission));
	}

	public static JsonObject getPaassportHttpResult(String path, String post) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			printWriter.write(post);// post的参数 xx=xx&yy=yy
			// flush输出流的缓冲
			printWriter.flush();
			// 开始获取数据
			StringBuffer rBuffer = new StringBuffer();
			String lines;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			while ((lines = reader.readLine()) != null) {
				rBuffer.append(lines);
				System.out.println(lines);
			}
			JsonParser parse = new JsonParser();
			return (JsonObject) parse.parse(rBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<DataPermissionSetting> encapsulateDataPermission(List<DataPermissionSetting> settingList, List<IdcHouses> idcHouses,Integer dataPermissionId) {
		List<DataPermissionSetting> dataPermissionSettingList = new ArrayList<DataPermissionSetting>();
		if (idcHouses != null && idcHouses.size() > 0) {
			for (IdcHouses idcHouse : idcHouses) {
				DataPermissionSetting setting = new DataPermissionSetting();
				if(settingList!=null&&settingList.size()>0){
					for(DataPermissionSetting dataPermissionSetting:settingList){
						setting.setDataPermissionId(dataPermissionSetting.getDataPermissionId());
						if(dataPermissionSetting.getSettingValue()!=null&&dataPermissionSetting.getSettingId()!=null&&dataPermissionSetting.getSettingKey().equals(idcHouse.getHouseId().toString())){
							setting.setSettingId(dataPermissionSetting.getSettingId());
							break;
						}
					} 
				}
				setting.setDataPermissionId(dataPermissionId);
				setting.setSettingKey(idcHouse.getHouseId() + "");
				setting.setSettingValue(idcHouse.getHouseName());
				dataPermissionSettingList.add(setting);
			}
		}
		return dataPermissionSettingList;
	}

	public String formatListToString(List<String> ids,String splitStr){
		StringBuilder stringBuilder = new StringBuilder("");
		String result = "";
		if (ids!=null && !ids.isEmpty()){
			for (int i=0;i<ids.size();i++){
				stringBuilder.append(ids.get(i)+"、");
			}
		}
		result = stringBuilder.toString();
		if (result.contains(splitStr)){
			return result.substring(0,result.lastIndexOf(splitStr));
		} else {
			return result;
		}

	}

}
