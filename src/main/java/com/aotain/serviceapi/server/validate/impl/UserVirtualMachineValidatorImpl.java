package com.aotain.serviceapi.server.validate.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.UserValidateConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import com.aotain.serviceapi.server.dao.preinput.UserPrincipalMapper;
import com.aotain.serviceapi.server.dao.preinput.UserVirtualMachineMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.validate.IUserVirtualMachineValidator;

/**
 * 虚拟主机校验类
 *
 * @author bang
 * @date 2018/07/09
 */
@Service
public class UserVirtualMachineValidatorImpl extends CommonValidator implements IUserVirtualMachineValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_USER_SERVICE_VIRTUAL_PREFIX;

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private UserPrincipalMapper userPrincipalMapper;

    @Autowired
    private UserVirtualMachineMapper userVirtualMachineMapper;
    
    @Override
    public boolean existHouseId(UserVirtualInformation dto) {
        if (dto.getHouseId()==null){
            return false;
        }
        HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
        houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
        return housePrincipalMapper.findByHouseId(houseInformationDTO)!=null?true:false;
    }

    @Override
    public boolean existUserId(UserVirtualInformation dto) {
        if (dto.getUserId()==null){
            return false;
        }
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(Long.valueOf(dto.getUserId()));
        return userPrincipalMapper.findByUserId(userInformationDTO)!=null?true:false;
    }

	@Override
	public boolean uniqueVirtualNo(UserVirtualInformation dto) {
		if (dto.getVirtualNo() == null) {
			return false;
		}
		return userVirtualMachineMapper.findByVirtualNo(dto) == null ? true : false;
	}

	@Override
	public boolean uniqueName(UserVirtualInformation dto) {
		if (dto.getName() == null) {
			return false;
		}
		return userVirtualMachineMapper.findByName(dto) == null ? true : false;
	}

    @Override
    public AjaxValidationResult preValidate(BaseModel baseModel) {
        //UserVirtualInformationDTO dto = (UserVirtualInformationDTO)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        //Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
//        if ( !existHouseId(dto) ){
//            if (errorsArgsMap.get("houseId")==null){
//                errorsArgsMap.put("houseId", new String[]{"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
//            } else {
//                Object[] errorMsg = errorsArgsMap.get("houseId");
//                arrayExpand(errorMsg,"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
//                errorsArgsMap.put("houseId", errorMsg);
//            }
//        }
//        if ( !existUserId(dto) ){
//            if (errorsArgsMap.get("userId")==null){
//                errorsArgsMap.put("userId", new String[]{"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
//            } else {
//                Object[] errorMsg = errorsArgsMap.get("userId");
//                arrayExpand(errorMsg,"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
//                errorsArgsMap.put("userId", errorMsg);
//            }
//        }
        /*if ( !uniqueVirtualNo(dto) ){
            if (errorsArgsMap.get("virtualNo")==null){
                errorsArgsMap.put("virtualNo", new String[]{"[虚拟机编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("virtualNo");
                arrayExpand(errorMsg,"[虚拟机编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("virtualNo", errorMsg);
            }
        }
        if ( !uniqueName(dto) ){
            if (errorsArgsMap.get("name")==null){
                errorsArgsMap.put("name", new String[]{"[虚拟机名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("name");
                arrayExpand(errorMsg,"[虚拟机名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("name", errorMsg);
            }
        }*/
        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
    	//UserVirtualInformation dto = (UserVirtualInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        //Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        /*if ( !ifExistHouseId(dto) ){
            if (errorsArgsMap.get("houseId")==null){
                errorsArgsMap.put("houseId", new String[]{"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseId");
                arrayExpand(errorMsg,"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("houseId", errorMsg);
            }
        }
        if ( !ifExistUserId(dto) ){
            if (errorsArgsMap.get("userId")==null){
                errorsArgsMap.put("userId", new String[]{"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("userId");
                arrayExpand(errorMsg,"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("userId", errorMsg);
            }
        }
        if ( !ifRepeatVirtualNo(dto) ){
            if (errorsArgsMap.get("virtualNo")==null){
                errorsArgsMap.put("virtualNo", new String[]{"[虚拟机编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("virtualNo");
                arrayExpand(errorMsg,"[虚拟机编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("virtualNo", errorMsg);
            }
        }
        if ( !ifRepeatVirtualName(dto) ){
            if (errorsArgsMap.get("name")==null){
                errorsArgsMap.put("name", new String[]{"[虚拟机名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("name");
                arrayExpand(errorMsg,"[虚拟机名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("name", errorMsg);
            }
        }*/
        return ajaxValidationResult;
    }

    @Override
	public AjaxValidationResult validateBean(BaseModel baseModel) {
		UserVirtualInformation dto = (UserVirtualInformation) baseModel;
		List<ObjectError> errors = new ArrayList<ObjectError>(0);
		Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
		ValidateResult result = null;
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.DELETE.getValue()) {
			if (dto.getAddType() != null && dto.getAddType() == 1) {
				if (dto.getUserId() == null) {
					errorsArgsMap.put("userId", new Object[] { "[用户ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
				} else {
					result = validateUserId(dto.getUserId().toString());
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("userId", new Object[] { result.getMsg() });
					}
				}
			}
			if (dto.getVirtualId() == null) {
				errorsArgsMap.put("virtualId", new Object[] { "虚拟主机ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} 
		}
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
        	if (dto.getHouseId() == null) {
    			errorsArgsMap.put("houseId", new Object[] { "[机房ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateHouseId(dto.getHouseId().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
    			}
    			result = validateAuthHouse(dto.getHouseId(), baseModel);
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
				}
				//接入方式为虚拟主机的校验
				if(dto.getUnitName() == null && dto.getUserId() == null) {
					if (dto.getSetmode() == null) {
						String errorMessage = "[接入方式" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]";
		    			if (errorsArgsMap.get("setmode") == null) {
							errorsArgsMap.put("setmode", new String[] { errorMessage });
						} else {
							Object[] errorMsg = errorsArgsMap.get("setmode");
							arrayExpand(errorMsg, errorMessage);
							errorsArgsMap.put("setmode", errorMsg);
						}
					} else {
						if (!UserValidateConstant.SetMode.VIRTUAL.getValue().equals(dto.getSetmode())) {
							String errorMessage = "[该用户服务的接入方式不为虚拟主机方式]";
							if (errorsArgsMap.get("setmode") == null) {
								errorsArgsMap.put("setmode", new String[] { errorMessage });
							} else {
								Object[] errorMsg = errorsArgsMap.get("setmode");
								arrayExpand(errorMsg, errorMessage);
								errorsArgsMap.put("setmode", errorMsg);
							}
						}
					}
				} else {
					result = validateRightVirtual(dto);
					if(!vaildateSuccess(result)) {
						if (errorsArgsMap.get("unitName") == null) {
							errorsArgsMap.put("unitName", new String[] { result.getMsg() });
						} else {
							Object[] errorMsg = errorsArgsMap.get("unitName");
							arrayExpand(errorMsg, result.getMsg());
							errorsArgsMap.put("unitName", errorMsg);
						}
					}
				}
    		}
    		if (dto.getVirtualNo() == null) {
    			errorsArgsMap.put("virtualNo", new Object[] { "[虚拟主机编号" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateVirtualNo(dto.getVirtualNo().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("virtualNo", new Object[] { result.getMsg() });
    			} else {
					if (!uniqueVirtualNo(dto)) {
						if (errorsArgsMap.get("virtualNo") == null) {
							errorsArgsMap.put("virtualNo", new String[] { "[虚拟机编号" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
						} else {
							Object[] errorMsg = errorsArgsMap.get("virtualNo");
							arrayExpand(errorMsg, "[虚拟机编号" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]");
							errorsArgsMap.put("virtualNo", errorMsg);
						}
					}
    			}
    		}
    		if (dto.getName() == null) {
    			errorsArgsMap.put("name", new Object[] { "[虚拟主机名" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "]" });
    		} else {
    			result = validateName(dto.getName().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("name", new Object[] { result.getMsg() });
    			} else {
    				if (!uniqueName(dto)) {
    					if (errorsArgsMap.get("name") == null) {
    						errorsArgsMap.put("name", new String[] { "[虚拟机名称" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
    					} else {
    						Object[] errorMsg = errorsArgsMap.get("name");
    						arrayExpand(errorMsg, "[虚拟机名称" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]");
    						errorsArgsMap.put("name", errorMsg);
    					}
    				}
    			}
    		}
    		if (dto.getNetworkAddress() == null) {
    			errorsArgsMap.put("networkAddress", new Object[] { "虚拟主机网络地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateNetworkAddress(dto.getNetworkAddress().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("networkAddress", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getMgnAddress() == null) {
    			errorsArgsMap.put("mgnAddress", new Object[] { "虚拟主机管理地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateMgnAddress(dto.getMgnAddress().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("mgnAddress", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getStatus() == null) {
    			errorsArgsMap.put("status", new Object[] { "虚拟主机状态" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateStatus(dto.getStatus().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("status", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getType() == null) {
    			errorsArgsMap.put("type", new Object[] { "虚拟主机类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateType(dto.getType().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("type", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getAreaCode() == null) {
    			errorsArgsMap.put("areaCode", new Object[] { "地市码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
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

    private ValidateResult validateRightVirtual(UserVirtualInformation dto) {
    	String errorMessage = "[该用户服务的接入方式不为虚拟主机方式]";
    	int count = userVirtualMachineMapper.judgeRightExist(dto);
    	if (count == 0) {
    		return ValidateResult.getErrorResult(errorMessage);
    	}
		return ValidateResult.getSuccessResult();
	}

	private ValidateResult validateUserId(String userId){
        String propertyName = "用户ID";
        return BasicValidateUtils.validateMaxLength(propertyName,userId,getFiledLengthByPropertyName(prefix,"userId"));
    }

    private ValidateResult validateHouseId(String houseId){
        String propertyName = "机房ID";
        return BasicValidateUtils.validateMaxLength(propertyName,houseId,getFiledLengthByPropertyName(prefix,"houseId"));
    }

    private ValidateResult validateVirtualNo(String virtualNo){
        String propertyName = "虚拟主机编号";
        return BasicValidateUtils.validateMaxLength(propertyName,virtualNo,getFiledLengthByPropertyName(prefix,"virtualNo"));

    }

    private ValidateResult validateName(String name){
        String propertyName = "虚拟主机名";
        return BasicValidateUtils.validateMaxLength(propertyName,name,getFiledLengthByPropertyName(prefix,"name"));
    }

    private ValidateResult validateNetworkAddress(String networkAddress){
        String propertyName = "虚拟主机网络地址";
        return validateStartIp(propertyName,networkAddress);
    }

    private ValidateResult validateMgnAddress(String mgnAddress){
        String propertyName = "虚拟主机管理地址";
        return validateStartIp(propertyName,mgnAddress);
    }

    private ValidateResult validateStatus(String status){
        String propertyName = "虚拟主机状态";
        return validateVirtualStatus(propertyName,status);
    }

    private ValidateResult validateType(String type){
        String propertyName = "虚拟主机类型";
        return validateVirtualType(propertyName,type);
    }

    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }
    
}
