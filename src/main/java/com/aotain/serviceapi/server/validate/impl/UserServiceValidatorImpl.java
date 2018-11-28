package com.aotain.serviceapi.server.validate.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.ServiceDomainInformation;
import com.aotain.cu.serviceapi.model.UserServiceInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.UserValidateConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.preinput.UserPrincipalMapper;
import com.aotain.serviceapi.server.dao.preinput.UserServiceMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.validate.IUserServiceValidator;

/**
 * 用户服务校验类
 *
 * @author bang
 * @date 2018/07/09
 */
@Service
public class UserServiceValidatorImpl extends CommonValidator implements IUserServiceValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_USER_SERVICE_PREFIX;
    private static final String domainPrefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_USER_SERVICE_DOMAIN_PREFIX;

    @Autowired
    private UserPrincipalMapper userPrincipalMapper;

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Override
    public boolean existUserId(UserServiceInformationDTO dto) {
        if (dto.getUserId()==null){
            return false;
        }
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(Long.valueOf(dto.getUserId()));
        return userPrincipalMapper.findByUserId(userInformationDTO)!=null?true:false;
    }

	@Override
	public boolean existDomain(UserServiceInformation info) {
		if (info.getDomainList() == null || info.getDomainList().size() == 0) {
			return false;
		}
		for (int i = 0; i < info.getDomainList().size(); i++) {
			ServiceDomainInformation serviceDomainInformation = new ServiceDomainInformation();
			serviceDomainInformation.setDomainName(info.getDomainList().get(i).getDomainName());
			serviceDomainInformation.setServiceId(info.getServiceId());
			if (userServiceMapper.findByDomainName(serviceDomainInformation) != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean existRegisterIdForSameUser(UserServiceInformation info) {
		if (info.getRegisterId() == null || info.getUserId() == null) {
			return false;
		}
		UserServiceInformationDTO dto = new UserServiceInformationDTO();
		dto.setUserId(info.getUserId());
		dto.setServiceId(info.getServiceId());
		dto.setRegisterId(info.getRegisterId());
		return userServiceMapper.findByRegisterIdAndUserId(dto) != null ? true : false;
	}

	@Override
	public ValidateResult rightSetMode(UserServiceInformationDTO dto) {
		if (dto.getSetmode() == null || dto.getUserId() == null) {
			return ValidateResult.getSuccessResult();
		}
		if (UserValidateConstant.SetMode.DEDICATED.getValue().equals(dto.getSetmode())) {
			// 接入方式为专线时，业务类型只能为ISP
			if (!UserValidateConstant.BusinessType.ISP.getValue().equals(dto.getBusiness())) {
				return ValidateResult.getErrorResult("[专线接入方式服务类型只能为ISP服务]");
			}
			UserInformationDTO userInformationDTO = new UserInformationDTO();
			userInformationDTO.setUserId(dto.getUserId());
			UserInformationDTO result = userPrincipalMapper.findDedicatedUserById(userInformationDTO);
			if (result == null) {
				return ValidateResult.getErrorResult("[只有专线用户才有专线接入方式]");
			}
		}
		return ValidateResult.getSuccessResult();
	}

	@Override
	public ValidateResult rightBusinessType(UserServiceInformationDTO dto) {
		if (dto.getServiceType() == null || dto.getUserId() == null) {
			return ValidateResult.getSuccessResult();
		}
		UserInformationDTO userInformationDTO = new UserInformationDTO();
		userInformationDTO.setUserId(dto.getUserId());
		UserInformationDTO result = userPrincipalMapper.findByUserId(userInformationDTO);
		if (result != null) {
			String name = result.getUnitName();
			// 查询是否存在此单位名称对应的机架信息

		}
		return null;
	}

    @Override
    public AjaxValidationResult  preValidate(BaseModel baseModel) {
    	UserServiceInformation dto = (UserServiceInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
//        if ( !existUserId(dto) ){
//            if (errorsArgsMap.get("userId")==null){
//                errorsArgsMap.put("userId", new String[]{"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
//            } else {
//                Object[] errorMsg = errorsArgsMap.get("userId");
//                arrayExpand(errorMsg,"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
//                errorsArgsMap.put("userId", errorMsg);
//            }
//        }
        if(dto.getAddType()!=null&&dto.getAddType()==3){
        	
        }else if (dto.getRegisterId() != null && dto.getUserId() != null) { 
			if (existRegisterIdForSameUser(dto)) {
				if (errorsArgsMap.get("registerId") == null) {
					errorsArgsMap.put("registerId", new String[] { "[用户下此备案号已存在]" });
				} else {
					Object[] errorMsg = errorsArgsMap.get("registerId");
					arrayExpand(errorMsg, "[用户下此备案号已存在]");
					errorsArgsMap.put("registerId", errorMsg);
				}
			}
		}
		if(dto.getDomainList()!=null&&dto.getDomainList().size()>0){
			for (int i = 0; i < dto.getDomainList().size(); i++) {
				ServiceDomainInformation serviceDomainInformation = new ServiceDomainInformation();
				serviceDomainInformation.setDomainName(dto.getDomainList().get(i).getDomainName());
				serviceDomainInformation.setServiceId(dto.getServiceId());
				if (userServiceMapper.findByDomainName(serviceDomainInformation) != null) {
					if (errorsArgsMap.get("domainName") == null) {
						errorsArgsMap.put("domainName", new String[] { "["+serviceDomainInformation.getDomainName()+"]域名已存在" });
					} else {
						Object[] errorMsg = errorsArgsMap.get("domainName");
						arrayExpand(errorMsg, "["+serviceDomainInformation.getDomainName()+"]域名已存在");
						errorsArgsMap.put("domainName", errorMsg);
					}
				}
			}
		}
		/*if (existDomain(dto)) {
			if (errorsArgsMap.get("domainName") == null) {
				errorsArgsMap.put("domainName", new String[] { "[域名已存在]" });
			} else {
				Object[] errorMsg = errorsArgsMap.get("domainName");
				arrayExpand(errorMsg, "[域名已存在]");
				errorsArgsMap.put("domainName", errorMsg);
			}
		}*/
		/*ValidateResult result = rightSetMode(dto);
		if (!vaildateSuccess(result)) {
			if (errorsArgsMap.get("setmode") == null) {
				errorsArgsMap.put("setmode", new String[] { result.getMsg() });
			} else {
				Object[] errorMsg = errorsArgsMap.get("setmode");
				arrayExpand(errorMsg, result.getMsg());
				errorsArgsMap.put("setmode", errorMsg);
			}
		}*/
		return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
        return null;
    }

    @Override
    public AjaxValidationResult validateBean(BaseModel baseModel) {
        List<ObjectError> errors = new ArrayList<ObjectError>(0);
        Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
        UserServiceInformation dto = (UserServiceInformation)baseModel;
        ValidateResult result = null;
//        if (dto.getUserId()==null){
//            errorsArgsMap.put("userId",new Object[]{"用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()});
//        } else {
//            if (!vaildateSuccess(validateUserId(dto.getUserId().toString()))){
//                errorsArgsMap.put("userId",new Object[]{validateUserId(dto.getUserId().toString()).getMsg()});
//            }
//        }
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.DELETE.getValue()) {
			if (dto.getAddType() != null && dto.getAddType() == 1) {
				if (dto.getUserId() == null) {
					errorsArgsMap.put("userId", new Object[] { "用户ID" + ErrorCodeConstant.ERROR_1008.getErrorMsg() });
				} else {
					result = validateUserId(dto.getUserId().toString());
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("userId", new Object[] { result.getMsg() });
					}
				}
			}
			if (dto.getServiceId() == null) {
				errorsArgsMap.put("serviceId", new Object[] { "服务ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} 
		}
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
        	if (dto.getServiceContent() == null) {
    			errorsArgsMap.put("serviceContent", new Object[] { "[服务内容" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateServiceContent(dto.getServiceContent().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("serviceContent", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getServiceType() == null) {
    			errorsArgsMap.put("validateServiceType", new Object[] { "[应用服务类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateServiceType(dto.getServiceType().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("serviceType", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getSetmode() == null) {
    			errorsArgsMap.put("setmode", new Object[] { "[接入方式" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateSetMode(dto.getSetmode().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("setmode", new Object[] { result.getMsg() });
    			} else {
    				//校验专线用户的接入方式
    				if (dto.getUserId() != null) {
    					result = validateRightSetmode(dto);
	    				if (!vaildateSuccess(result)) {
	    					if (errorsArgsMap.get("setmode") == null) {
	    						errorsArgsMap.put("setmode", new String[] { result.getMsg() });
	    					} else {
	    						Object[] errorMsg = errorsArgsMap.get("setmode");
	    						arrayExpand(errorMsg, result.getMsg());
	    						errorsArgsMap.put("setmode", errorMsg);
	    					}
	    				} 
    				} else {
    					if (dto.getIdentify() == null) {
							if (errorsArgsMap.get("identify") == null) {
	    						errorsArgsMap.put("identify", new String[] { "用户标识" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
	    					} else {
	    						Object[] errorMsg = errorsArgsMap.get("identify");
	    						arrayExpand(errorMsg, "用户标识" + ErrorCodeConstant.ERROR_1009.getErrorMsg());
	    						errorsArgsMap.put("identify", errorMsg);
	    					}
			    		} else {
							if (dto.getSetmode()==0){
								// 专线接入方式判断是否是专线用户
								if (!dto.getIdentify().contains(UserValidateConstant.IdentifyType.DEDICATED_USER.getValue())) {
									if (errorsArgsMap.get("setmode") == null) {
										errorsArgsMap.put("setmode", new String[] { "[非专线用户不能有专线接入方式]" });
									} else {
										Object[] errorMsg = errorsArgsMap.get("setmode");
										arrayExpand(errorMsg, "[非专线用户不能有专线接入方式]");
										errorsArgsMap.put("setmode", errorMsg);
									}
								}

							}
//			    			if (!dto.getIdentify().contains(UserValidateConstant.IdentifyType.DEDICATED_USER.getValue())) {
//			    				if (UserValidateConstant.IdentifyType.DEDICATED_USER.getValue().toString().equals(dto.getSetmode())){
//									if (errorsArgsMap.get("setmode") == null) {
//										errorsArgsMap.put("setmode", new String[] { "[非专线用户不能有专线接入方式]" });
//									} else {
//										Object[] errorMsg = errorsArgsMap.get("setmode");
//										arrayExpand(errorMsg, "[非专线用户不能有专线接入方式]");
//										errorsArgsMap.put("setmode", errorMsg);
//
//									}
//								}
//			    			}
			    		}
    				}
    			}
    		}
    		if (dto.getBusiness() == null) {
    			errorsArgsMap.put("business", new Object[] { "[业务类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateBusiness(dto.getBusiness().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("business", new Object[] { result.getMsg() });
    			} else {
    				if (UserValidateConstant.SetMode.DEDICATED.getValue().equals(dto.getSetmode())) {
    					// 接入方式为专线时，业务类型只能为ISP
    					if (!UserValidateConstant.BusinessType.ISP.getValue().equals(dto.getBusiness())) {
    						String errorMessage = "[专线接入方式的业务类型只能为ISP业务]";
    						if (errorsArgsMap.get("business") == null) {
    							errorsArgsMap.put("business", new String[] { errorMessage });
    						} else {
    							Object[] errorMsg = errorsArgsMap.get("business");
    							arrayExpand(errorMsg, errorMessage);
    							errorsArgsMap.put("business", errorMsg);
    						}
    					}
    				}
    			}
    		}
//            if (dto.getRegisterId()==null&&dto.getRegType()==null&&(dto.getDomainList()==null||dto.getDomainList().size()==0)){
//                errorsArgsMap.put("registerId",new Object[]{"备案号或许可证号"+ErrorCodeConstant.ERROR_1008.getErrorMsg()});
//            } else {
//                if (!vaildateSuccess(validateRegisterId(dto.getRegisterId().toString()))){
//                    errorsArgsMap.put("registerId",new Object[]{validateRegisterId(dto.getRegisterId().toString()).getMsg()});
//                }
    //
//                if (dto.getDomainList()==null||dto.getDomainList().size()==0){
//                    errorsArgsMap.put("domainName",new Object[]{"域名"+ErrorCodeConstant.ERROR_1008.getErrorMsg()});
//                } else {
//                    if (!vaildateSuccess(validateDomainName(dto.getDomainList()))){
//                        errorsArgsMap.put("domainName",new Object[]{validateDomainName(dto.getDomainList()).getMsg()});
//                    }
//                }
//            }

//            if (dto.getRegType()!=null || dto.getDomainList()!=null) {
//                if (dto.getRegType()!=null) {
//                    if (!vaildateSuccess(validateRegType(dto.getRegType().toString()))){
//                        errorsArgsMap.put("regType",new Object[]{validateRegType(dto.getRegType().toString()).getMsg()});
//                    }
//                    if (!vaildateSuccess(validateDomainName(dto.getDomainList()))){
//                        errorsArgsMap.put("domainName",new Object[]{validateDomainName(dto.getDomainList()).getMsg()});
//                    }
//                } else {
//                    if (!vaildateSuccess(validateDomainName(dto.getDomainList()))){
//                        errorsArgsMap.put("domainName",new Object[]{validateDomainName(dto.getDomainList()).getMsg()});
//                    }
//                }
//                if (dto.getRegisterId()==null){
//                    errorsArgsMap.put("registerId",new Object[]{"备案号或许可证号"+ErrorCodeConstant.ERROR_1008.getErrorMsg()});
//                } else {
//                    if (!vaildateSuccess(validateRegisterId(dto.getRegisterId().toString()))){
//                        errorsArgsMap.put("registerId",new Object[]{validateRegisterId(dto.getRegisterId().toString()).getMsg()});
//                    }
//                }
//            }

    		if (dto.getRegType() == null && (dto.getDomainList() == null||dto.getDomainList().size()==0)) {
    		} else if (dto.getRegType() == null && dto.getDomainList() != null) {
    			errorsArgsMap.put("regType", new Object[] { "[备案类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else if (dto.getRegType() != null && (dto.getDomainList() == null||dto.getDomainList().size()==0)) {
    			errorsArgsMap.put("domainList", new Object[] { "[域名" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateRegType(dto.getRegType().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("regType", new Object[] { result.getMsg() });
    			}
    			result = validateDomainName(dto.getDomainList());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("domainName", new Object[] { result.getMsg() });
    			}
    		}
    		
    		if (dto.getRegisterId() == null||dto.getRegisterId()=="") {
				errorsArgsMap.put("registerId", new Object[] { "[备案号或许可证号" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
			} else {
				result = validateRegisterId(dto.getRegisterId().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("registerId", new Object[] { result.getMsg() });
				}
			}
    		
    		if (dto.getAreaCode() == null) {
    			errorsArgsMap.put("areaCode", new Object[] { "[地市码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			if (dto.getAreaCodeValidateType() == 1) {
	    			result = validateAreaCode(dto);
	    			if (!vaildateSuccess(result)) {
	    				errorsArgsMap.put("areaCode", new Object[] { result.getMsg() });
	    			}
    			}
    		}
        }
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
			if (dto.getCreateUserId() == null) {
				errorsArgsMap.put("createUserId", new Object[] { "创建者" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			}
		}
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()) {
			if (dto.getUpdateUserId() == null) {
				errorsArgsMap.put("updateUserId", new Object[] { "修改者" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			}
		}
		return new AjaxValidationResult(errors, errorsArgsMap);
    }

    private ValidateResult validateUserId(String userId){
        String propertyName = "用户ID";
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult validateResult = BasicValidateUtils.validateMaxLength(propertyName,userId,getFiledLengthByPropertyName(prefix,"userId"));
        if (!vaildateSuccess(validateResult)){
            code = validateResult.getCode();
            stringBuilder.append(validateResult.getMsg());
        }

        return new ValidateResult(code,stringBuilder.toString());
    }

    private ValidateResult validateDomainName(List<ServiceDomainInformation> domainNameList){
        String propertyName = "域名";
        StringBuilder stringBuilder = new StringBuilder("");
        int code = 200;
        for(ServiceDomainInformation serverDomain:domainNameList){
        	ValidateResult result = BasicValidateUtils.validateMaxLength(propertyName,serverDomain.getDomainName(),getFiledLengthByPropertyName(domainPrefix,"domainName"));
    		if(!vaildateSuccess(result)){
        		code = result.getCode();
        		stringBuilder.append(result.getMsg()).append("|");
        	}else{
        		ValidateResult resultDomain = BasicValidateUtils.validateDomainName(propertyName+serverDomain.getDomainName(), serverDomain.getDomainName());
            	if(!vaildateSuccess(resultDomain)){//:域名合法性校验
            		code = resultDomain.getCode();
            		stringBuilder.append(resultDomain.getMsg()).append("|");
            	}
        	}
        }
        return new ValidateResult(code, stringBuilder.toString());
    }

    private ValidateResult validateServiceContent(String serviceContent){
        String propertyName = "服务内容";
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult validateResult = BasicValidateUtils.validateMaxLength(propertyName,serviceContent,getFiledLengthByPropertyName(prefix,"serviceContent"));
        if (!vaildateSuccess(validateResult)){
            code = validateResult.getCode();
            stringBuilder.append(validateResult.getMsg());
        }
        ValidateResult contentValidateResult = validateContent(propertyName,serviceContent);
        if (!vaildateSuccess(contentValidateResult)){
            code = contentValidateResult.getCode();
            stringBuilder.append(contentValidateResult.getMsg());
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    private ValidateResult validateServiceType(String serviceType){
        String propertyName = "应用服务类型";
        return validateServiceType(propertyName,serviceType);
    }

    private ValidateResult validateBusiness(String business){
        String propertyName = "业务类型";
        return validateBusinessType(propertyName,business);
    }

    private ValidateResult validateRegType(String regType){
        String propertyName = "备案类型";
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult validateResult = BasicValidateUtils.validateMaxLengthWithoutEmptyCheck(propertyName,regType,getFiledLengthByPropertyName(prefix,"regType"));
        if (!vaildateSuccess(validateResult)){
            code = validateResult.getCode();
            stringBuilder.append(validateResult.getMsg());
        }
        ValidateResult regTypeValidateResult = validateRegType(propertyName,regType);
        if (!vaildateSuccess(regTypeValidateResult)){
            code = regTypeValidateResult.getCode();
            stringBuilder.append(regTypeValidateResult.getMsg());
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    private ValidateResult validateRegisterId(String registerId){
        String propertyName = "备案号或许可证号";
        return BasicValidateUtils.validateMaxLengthWithoutEmptyCheck(propertyName,registerId,getFiledLengthByPropertyName(prefix,"registerId"));
    }


    private ValidateResult validateSetMode(String setmode){
        String propertyName = "接入方式";
        return validateSetmode(propertyName,setmode);
    }

    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }
    
    private ValidateResult validateRightSetmode(UserServiceInformation dto){
    	if (UserValidateConstant.SetMode.DEDICATED.getValue().equals(dto.getSetmode())) {
    		UserInformationDTO userInformationDTO = new UserInformationDTO();
    		userInformationDTO.setUserId(dto.getUserId());
    		UserInformationDTO result = userPrincipalMapper.findDedicatedUserById(userInformationDTO);
    		if (result == null) {
    			return ValidateResult.getErrorResult("[只有专线用户才有专线接入方式]");
    		}
    	} 
		return ValidateResult.getSuccessResult();
    }
    

}
