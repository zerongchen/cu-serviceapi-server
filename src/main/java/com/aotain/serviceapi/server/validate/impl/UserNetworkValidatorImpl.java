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
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import com.aotain.serviceapi.server.dao.preinput.UserBandWidthMapper;
import com.aotain.serviceapi.server.dao.preinput.UserPrincipalMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.validate.IUserNetworkValidator;

/**
 * 用户网络资源校验类
 *
 * @author bang
 * @date 2018/07/09
 */
@Service
public class UserNetworkValidatorImpl extends CommonValidator  implements IUserNetworkValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_USER_SERVICE_BANDWIDTH_PREFIX;

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private UserPrincipalMapper userPrincipalMapper;
    
    @Autowired
    private UserBandWidthMapper userBandWidthMapper;
    
	@Override
	public boolean existHouseId(UserBandwidthInformationDTO dto) {
		if (dto.getHouseId() == null) {
			return false;
		}
		HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
		houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
		return housePrincipalMapper.findByHouseId(houseInformationDTO) != null ? true : false;
	}

    @Override
    public boolean existUserId(UserBandwidthInformationDTO dto) {
        if (dto.getUserId()==null){
            return false;
        }
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(Long.valueOf(dto.getUserId()));
        return userPrincipalMapper.findByUserId(userInformationDTO)!=null?true:false;
    }

    @Override
    public AjaxValidationResult preValidate(BaseModel baseModel) {
        UserBandwidthInformation dto = (UserBandwidthInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        /*if ( !existHouseId(dto) ){
            if (errorsArgsMap.get("houseId")==null){
                errorsArgsMap.put("houseId", new String[]{"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseId");
                arrayExpand(errorMsg,"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("houseId", errorMsg);
            }
        }*/
//        if ( !existUserId(dto) ){
//            if (errorsArgsMap.get("userId")==null){
//                errorsArgsMap.put("userId", new String[]{"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
//            } else {
//                Object[] errorMsg = errorsArgsMap.get("userId");
//                arrayExpand(errorMsg,"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
//                errorsArgsMap.put("userId", errorMsg);
//            }
//        }
        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
    	UserBandwidthInformationDTO dto = (UserBandwidthInformationDTO)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        /*if ( !ifExistHouseId(dto) ){
            if (errorsArgsMap.get("houseId")==null){
                errorsArgsMap.put("houseId", new String[]{"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseId");
                arrayExpand(errorMsg,"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("houseId", errorMsg);
            }
        }*/
//        if ( !ifExistUserId(dto) ){
//            if (errorsArgsMap.get("userId")==null){
//                errorsArgsMap.put("userId", new String[]{"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
//            } else {
//                Object[] errorMsg = errorsArgsMap.get("userId");
//                arrayExpand(errorMsg,"[用户ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
//                errorsArgsMap.put("userId", errorMsg);
//            }
//        }
        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult validateBean(BaseModel baseModel) {
		UserBandwidthInformation dto = (UserBandwidthInformation) baseModel;
		List<ObjectError> errors = new ArrayList<ObjectError>(0);
		Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
		ValidateResult result = null;
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
			if (dto.getHhId() == null) {
				errorsArgsMap.put("hhId", new Object[] { "占用机房ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} 
		}
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
        	if (dto.getHouseId() == null) {
    			errorsArgsMap.put("houseId", new Object[] { "机房ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseId(dto.getHouseId().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
    			}
    			result = validateAuthHouse(dto.getHouseId(), baseModel);
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
				}
    			//用户存在此机房的带宽信息的校验
				if (dto.getUnitName() == null && dto.getUserId() == null) {
					String errorMessage = "单位名称" + ErrorCodeConstant.ERROR_1009.getErrorMsg();
	    			if (errorsArgsMap.get("houseId") == null) {
						errorsArgsMap.put("houseId", new String[] { errorMessage });
					} else {
						Object[] errorMsg = errorsArgsMap.get("houseId");
						arrayExpand(errorMsg, errorMessage);
						errorsArgsMap.put("houseId", errorMsg);
					}
	    		} else {
	    			result = validateRightBandwith(dto);
					if(!vaildateSuccess(result)) {
						if (errorsArgsMap.get("houseId") == null) {
							errorsArgsMap.put("houseId", new String[] { result.getMsg() });
						} else {
							Object[] errorMsg = errorsArgsMap.get("houseId");
							arrayExpand(errorMsg, result.getMsg());
							errorsArgsMap.put("houseId", errorMsg);
						}
					}
	    		}
    		}
    		if (dto.getBandWidth() == null) {
    			errorsArgsMap.put("bandWidth", new Object[] { "网络带宽" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateBandWidth(dto.getBandWidth().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("bandWidth", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getDistributeTime() == null) {
    			errorsArgsMap.put("distributeTime", new Object[] { "资源分配日期" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateDistributeTime(dto.getDistributeTime().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("distributeTime", new Object[] { result.getMsg() });
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

    private ValidateResult validateRightBandwith(UserBandwidthInformation dto) {
    	UserBandwidthInformationDTO result = userBandWidthMapper.uniqueBandwidth(dto);
    	if (result != null) {
    		return ValidateResult.getErrorResult("[用户已经占用此机房]");
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

    private ValidateResult validateBandWidth(String bandWidth){
        String propertyName = "网络带宽";
        if(Long.parseLong(bandWidth)<0){
        	return ValidateResult.getErrorResult("["+propertyName+"不能为负数"+"]");
        }
        return BasicValidateUtils.validateMaxLength(propertyName,bandWidth,getFiledLengthByPropertyName(prefix,"bandWidth"));
    }

    private ValidateResult validateFrameIds(String frameIds){
        String propertyName = "机架ID";
        return BasicValidateUtils.validateMaxLength(propertyName,frameIds,getFiledLengthByPropertyName(prefix,"frameIds"));
    }

    private ValidateResult validateDistributeTime(String distributeTime){
        String propertyName = "资源分配日期";
        return validateTime(propertyName,"yyyy-MM-dd",distributeTime);
    }

    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }
    
}
