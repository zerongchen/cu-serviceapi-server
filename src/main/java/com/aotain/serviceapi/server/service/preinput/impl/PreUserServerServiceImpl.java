package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.*;

import com.aotain.serviceapi.server.util.StringUtil;
import javafx.scene.effect.SepiaTone;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.ServiceDomainInformation;
import com.aotain.cu.serviceapi.model.UserServiceInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.preinput.UserInfoMapper;
import com.aotain.serviceapi.server.dao.preinput.UserServiceMapper;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.PreUserServerService;
import com.aotain.serviceapi.server.validate.impl.UserServiceValidatorImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName PreUserServerService
 * @Author tanzj
 * @Date 2018/8/6
 **/
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class PreUserServerServiceImpl extends BaseService implements PreUserServerService {

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;
    
    private Logger log = Logger.getLogger(PreUserInfoServiceImpl.class);

    public PreUserServerServiceImpl(UserServiceValidatorImpl baseValidator) {
        super(baseValidator);
    }

	@Override
	public PageResult<UserServiceInformationDTO> getServerInfoList(UserServiceInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<UserServiceInformationDTO> result = new PageResult<UserServiceInformationDTO>();
		// 两个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				|| StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return result;
		}
		List<UserServiceInformationDTO> info = new ArrayList<UserServiceInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = userServiceMapper.getUserServiceInfoList(dto);
			PageInfo<UserServiceInformationDTO> pageResult = new PageInfo<UserServiceInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = userServiceMapper.getUserServiceInfoList(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public ResultDto insertData(List<? extends UserServiceInformation> services, Long userId, boolean allowInsert) throws Exception {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (services == null || services.size() == 0) {
			return result;
		}
		//重复性校验
		Map<String,Object> userServiceMap = new HashMap<String,Object>();
        for(UserServiceInformation dto :services){
        	List<ServiceDomainInformation> domainList = dto.getDomainList();
        	if(domainList!=null&&domainList.size()>0){
        		for(ServiceDomainInformation domain:domainList){
        			if(userServiceMap.containsKey(domain.getDomainName())){
            			result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
            			result.setResultMsg("存在重复域名："+domain.getDomainName());
            			return result;
            		}else{
            			userServiceMap.put(domain.getDomainName(), domain.getDomainName());
            		}
        		}
        	}
        }
		
		//校验
		boolean success = false;
		List<Integer> successList = new ArrayList<Integer>(services.size());
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>(services.size());
		for (int i = 0; i < services.size(); i++) {
			UserServiceInformation dto = services.get(i);
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
					int flag = insert(dto);
					if (flag <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert user service success!");
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
			for (UserServiceInformation dto : services) {
				if (userId != null) {
					dto.setUserId(userId);
				}
				int flag = insert(dto);
				if (flag <= 0) {
					return getErrorResult("新增记录失败");
				}
				result.getAjaxValidationResultMap().get((index++) + "").setPid(dto.getServiceId());
			}
		} 
		return result;
		
		/*ResultDto resultInfo = new ResultDto();
		for (UserServiceInformation dto : services) {
			// 1.校验
			if (!validateSuccess(dto)) {
				resultInfo.setResultMsg("输入的数据格式校验不通过");
				resultInfo.setResultCode(1);
				return resultInfo;
			}
			dto.setCzlx(1);
			dto.setDealFlag(0);
			int result = userServiceMapper.insertUserService(dto);
			if (result <= 0) {
				throw new Exception("用户域名信息新增失败");
			}
			for (ServiceDomainInformation domain : dto.getDomainList()) {
				domain.setServiceId(dto.getServiceId());
				domain.setCzlx(1);
				domain.setDealFlag(0);
				result = userServiceMapper.insertUserServiceDomain(domain);
				if (result <= 0) {
					throw new Exception("用户域名信息新增失败");
				}
			}
			updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
		}
		return getSuccessResult();*/
	}

	@Override
	public ResultDto updateData(List<UserServiceInformationDTO> dtos) throws Exception {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		
		Map<String, Object> userServiceMap = new HashMap<String, Object>();
		for (UserServiceInformationDTO dto : dtos) {
			List<ServiceDomainInformation> domainList = dto.getDomainList();
			if (domainList != null && domainList.size() > 0) {
				for (ServiceDomainInformation domain : domainList) {
					if (userServiceMap.containsKey(domain.getDomainName())) {
						validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
						validateResult.setResultMsg("存在重复域名：" + domain.getDomainName());
						return validateResult;
					} else {
						userServiceMap.put(domain.getDomainName(), domain.getDomainName());
					}
				}
			}
		}
		
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.MODIFY.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate user service success!");
			return updateServiceData(dtos);
		} else {
			log.info("validate user service failed!");
			return validateResult;
		}
		
		/*ResultDto resultInfo = new ResultDto();
		// 1.校验
		if (!validateSuccess(dto)) {
			resultInfo.setResultMsg("输入的数据格式校验不通过");
			resultInfo.setResultCode(1);
			return resultInfo;
		}
		// 操作类型如果没有上报则为1，否则为2
		UserServiceInformationDTO oldDto = userServiceMapper.getUserServiceInfoById(dto.getServiceId());
		if (oldDto.getDealFlag() == 1) {
			dto.setCzlx(2);
		} else {
			dto.setCzlx(1);
		}
		dto.setDealFlag(0);
		dto.setUpdateTime(new Date());
		int result = userServiceMapper.updateUserService(dto);
		if (result <= 0) {
			return getErrorResult("[" + dto.getServiceId() + "]:" + "user service insert to DB ERROR ");
		}
		// 域名信息保存
		List<Long> ids = new ArrayList<>();
		for (ServiceDomainInformation domain : dto.getDomainList()) {
			domain.setServiceId(dto.getServiceId());
			domain.setUpdateTime(dto.getCreateTime());
			domain.setCzlx(1);
			domain.setDealFlag(0);
			domain.setUpdateTime(new Date());
			if (domain.getDomainId() != null) {
				result = userServiceMapper.updateUserServiceDomain(domain);
			} else {
				domain.setCreateTime(new Date());
				result = userServiceMapper.insertUserServiceDomain(domain);
			}
			if (result <= 0) {
				return getErrorResult("[" + dto.getServiceId() + "]:" + "user service domain update to DB ERROR ");
			}
			if (domain.getDomainId() != null) {
				ids.add(domain.getDomainId());
			}
		}
		// 删除不需要的域名
		Map<String, Object> query = new HashMap<>();
		query.put("serviceId", dto.getServiceId());
		query.put("ids", ids);
		result = userServiceMapper.deleteByIds(query);
		updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
		return getSuccessResult();*/
	}

    private ResultDto updateServiceData(List<UserServiceInformationDTO> dtos) {
    	ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (UserServiceInformationDTO dto : dtos) {
				try {
					int flag = update(dto);
					if (flag <= 0) {
						// 插入或新增失败
						return getErrorResult("更新记录失败");
					}
				} catch (Exception e) {
					log.error("update user service failed!", e);
					return getErrorResult("更新记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

	@Override
	public ResultDto deleteData(List<UserServiceInformationDTO> dtos) throws Exception {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos == null || dtos.size() <= 0) {
			return validateResult;
		}
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.DELETE.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate user service success!");
			return deleteUserServices(dtos);
		} else {
			log.info("validate user service failed!");
			return validateResult;
		}
		/*for (Long serviceId : ids) {
			UserServiceInformationDTO oldDto = userServiceMapper.getUserServiceInfoById(serviceId);
			if (oldDto != null) {
				if (oldDto.getDealFlag() == 0) {
					userServiceMapper.deleteServerDomainByServiceId(serviceId);
					userServiceMapper.deleteById(serviceId);
				} else {
					UserServiceInformationDTO dto = new UserServiceInformationDTO();
					dto.setServiceId(oldDto.getServiceId());
					dto.setCzlx(3);
					dto.setDealFlag(0);
					dto.setUpdateTime(new Date());
					int result = userServiceMapper.updateUserService(dto);
				}
				updateUserMainInfoWhieSubsetInfoChange(oldDto.getUserId());
			}
		}
		return getSuccessResult();*/
	}

	private ResultDto deleteUserServices(List<UserServiceInformationDTO> dtos) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		List<Integer> successList = new ArrayList<Integer>();
		if (dtos != null && dtos.size() > 0) {
			for (UserServiceInformationDTO info : dtos) {
				try {
					UserServiceInformationDTO oldDto = userServiceMapper.getUserServiceInfoById(info.getServiceId());
					List<String> exitsAreacodes = new ArrayList<>(Arrays.asList(oldDto.getAreaCode().split(",")));
					if(!StringUtil.isEmptyString(oldDto.getAreaCode())){
						if(info.getCityCodeList()!=null || info.getCityCodeList().size()>=0){
							boolean flag = false;
							for(String areacode:oldDto.getAreaCode().split(",")){
								for(String nowAreacode:info.getCityCodeList()){
									if(areacode.equals(nowAreacode)){
										exitsAreacodes.remove(areacode);
										flag = true;
										break;
									}
								}
							}
							if(flag && exitsAreacodes.size()>0){
								oldDto.setUpdateTime(new Date());
								oldDto.setUpdateUserId(info.getUpdateUserId());
								oldDto.setAreaCode(StringUtils.join(exitsAreacodes,","));
								userServiceMapper.updateUserService(oldDto);
							}else if(flag && exitsAreacodes.size()<=0){
								if (oldDto != null) {
									if (oldDto.getCzlx() == 1 && oldDto.getDealFlag() == 0) {
										userServiceMapper.deleteServerDomainByServiceId(info.getServiceId());
										userServiceMapper.deleteById(info.getServiceId());
									} else {
										UserServiceInformationDTO dto = new UserServiceInformationDTO();
										dto.setServiceId(oldDto.getServiceId());
										dto.setCzlx(3);
										dto.setDealFlag(0);
										dto.setUpdateTime(new Date());
										userServiceMapper.updateUserService(dto);
									}
									updateUserMainInfoWhieSubsetInfoChange(oldDto.getUserId());
								}
							}
						}
					}
					successList.add(ResultDto.ResultCodeEnum.SUCCESS.getCode());
				} catch (Exception e) {
					log.error("delete user service:" + info.getServiceId() + ", userid: " + info.getUserId() + " ERROR", e);
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
	

	@Override
	public Map<Integer,ResultDto> imporUserServiceData(Map<Integer,UserServiceInformationDTO> dtos) {

    	if (dtos!=null && !dtos.isEmpty()){
			Map<Integer,ResultDto> resultDtoMap = new HashMap<>();
			Set<Integer> ks = dtos.keySet();
    		for (Integer key:ks){
    			try {
					UserServiceInformationDTO dto = dtos.get(key);
					UserInformationDTO userInformation = userInfoMapper.getDataByUserName(dto.getUserName());
					if (userInformation != null) {
						if (userInformation.getNature()==1){//提供互联网应用服务的用户
							try {
								dto.setUserId(userInformation.getUserId());
								UserServiceInformationDTO result = userServiceMapper.findByRegisterIdAndUserId(dto);
								if(result!=null){
									dto.setServiceId(result.getServiceId());
									resultDtoMap.put(key,updateData(Arrays.asList(dto)));
								}else{
									resultDtoMap.put(key,insertData(Arrays.asList(dto), dto.getUserId(), true));
								}
							} catch (Exception e) {
								log.error("", e);
								resultDtoMap.put(key,validateResult(dto));
								break;
							}
						}
					} else {
						resultDtoMap.put(key,getErrorResult("关联的用户主体信息导入异常，无法关联导入此子节信息"));
					}
				}catch (Exception e){
    				log.error("import single user service error",e);
					resultDtoMap.put(key,getErrorResult("网络异常"));
				}
			}
			return resultDtoMap;
		}
		return null;
	}

	@Transactional
    @Override
    protected int insert(BaseModel baseModel) {
    	try {
    		UserServiceInformation dto = (UserServiceInformation) baseModel;
			dto.setCzlx(1);
			dto.setDealFlag(0);
			int result = userServiceMapper.insertUserService(dto);
			if (result <= 0) {
				return result;
			}
			if (dto.getDomainList() != null && dto.getDomainList().size() > 0) {
				for (ServiceDomainInformation domain : dto.getDomainList()) {
					domain.setUserId(dto.getUserId());
					domain.setServiceId(dto.getServiceId());
					domain.setCzlx(1);
					domain.setDealFlag(0);
					result = userServiceMapper.insertUserServiceDomain(domain);
					if (result <= 0) {
						log.error("新增用户服务信息失败");
						return result;
					}
				}
			}
			updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
			log.info("新增记录成功");
			return 1;
		} catch (Exception e) {
			log.error("新增记录失败", e);
			return 0;
		}
    }

	@Transactional
    @Override
    protected int update(BaseModel baseModel) {
		try {
			UserServiceInformationDTO dto = (UserServiceInformationDTO) baseModel;
			UserServiceInformationDTO oldDto = userServiceMapper.getUserServiceInfoById(dto.getServiceId());
			if (oldDto.getCzlx() == 1 && oldDto.getDealFlag() == 1) {// 新增已上报
				dto.setCzlx(2);
			} else {
				dto.setCzlx(1);
			}
			dto.setDealFlag(0);
			dto.setUpdateTime(new Date());
			int result = userServiceMapper.updateUserService(dto);
			if (result <= 0) {
				log.error("更新用户服务信息失败");
				return result;
			}
			List<ServiceDomainInformation> domains = userServiceMapper.findDomainNameByServiceId(dto.getServiceId());
			Map<String, ServiceDomainInformation> domainMap = new HashMap<String, ServiceDomainInformation>();
			List<Long> ids = new ArrayList<Long>();
			if (domains != null && domains.size() > 0) {
				for (ServiceDomainInformation domain : domains) {
					domainMap.put(domain.getDomainName(), domain);
					ids.add(domain.getDomainId());
				}
			}
			
			// 删除不需要的域名
			if (ids != null && ids.size() > 0) {
				Map<String, Object> query = new HashMap<>();
				query.put("serviceId", dto.getServiceId());
				query.put("ids", ids);
				result = userServiceMapper.deleteByIds(query);
				// 已上报的域名更新其状态
				Map<String, Object> updateMap = new HashMap<>();
				updateMap.put("serviceId", dto.getServiceId());
				updateMap.put("ids", ids);
				result = userServiceMapper.updateByIds(updateMap);
			}
			
			// 域名信息保存
			if (dto.getDomainList() != null && dto.getDomainList().size() > 0) {
				for (ServiceDomainInformation domain : dto.getDomainList()) {
					domain.setUserId(dto.getUserId());
					domain.setServiceId(dto.getServiceId());
					domain.setUpdateTime(dto.getCreateTime());
					domain.setDealFlag(0);
					if (domainMap.containsKey(domain.getDomainName())) {
						ServiceDomainInformation info = domainMap.get(domain.getDomainName());
						Long domainId = info.getDomainId();
						ids.remove(domainId);
						domain.setDomainId(domainId);
						domain.setCzlx(info.getCzlx());
						domain.setDealFlag(info.getDealFlag());
						domain.setUpdateTime(new Date());
						result = userServiceMapper.updateUserServiceDomain(domain);
					} else {
						domain.setCzlx(1);
						domain.setDealFlag(0);
						domain.setCreateTime(new Date());
						result = userServiceMapper.insertUserServiceDomain(domain);
					}
					if (result <= 0) {
						log.error("更新用户服务信息失败");
						return result;
					}
				}
			}
			
			updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
			return 1;
		} catch (Exception e) {
			log.error("更新用户服务信息失败", e);
			return 0;
		}
    }

	@Transactional
    @Override
    protected int delete(BaseModel baseModel) {
        return 0;
    }

	@Override
	public ResultDto insertData(List<UserServiceInformation> dtos, Long userId) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (UserServiceInformation dto : dtos) {
				try {
					if (userId != null) {
						dto.setUserId(userId);
					}
					int result = insert(dto);
					if (result <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert user service success!");
				} catch (Exception e) {
					log.error("insert user service data failed!", e);
					return getErrorResult("新增记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

}
