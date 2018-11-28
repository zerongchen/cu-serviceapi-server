package com.aotain.serviceapi.server.validate.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.HouseUserFrameInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.HouseValidateConstant;
import com.aotain.serviceapi.server.constant.JCDMRedisConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.preinput.HouseFrameMapper;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.validate.IHouseFrameValidator;

/**
 * 机房机架校验器
 *
 * @author bang
 * @date 2018/07/09
 */
@Service
public class HouseFrameValidatorImpl extends CommonValidator implements IHouseFrameValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_HOUSE_FRAME_PREFIX;

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private HouseFrameMapper houseFrameMapper;
    
    @Autowired
    private RptIsmsBaseHouseMapper rptHouseMainMapper;

    @Override
    public boolean existHouseId(HouseFrameInformationDTO dto) {
        if (dto.getHouseId()==null){
            return false;
        }
        HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
        houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
        return housePrincipalMapper.findByHouseId(houseInformationDTO)!=null?true:false;
    }

    @Override
    public boolean existHouseName(HouseFrameInformationDTO dto) {
        if (dto.getHouseName()==null){
            return false;
        }
        HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
        houseInformationDTO.setHouseName(dto.getHouseName());
        return housePrincipalMapper.findByHouseName(houseInformationDTO)!=null?true:false;
    }

	@Override
	public boolean uniqueFrameName(HouseFrameInformation info) {
		if (info.getFrameName() == null) {
			return false;
		}
		HouseFrameInformationDTO dto = new HouseFrameInformationDTO();
		dto.setFrameId(info.getFrameId());
		dto.setFrameName(info.getFrameName());
		return houseFrameMapper.getByFrameName(dto) != null ? false : true;
	}

	@Override
	public void suitableUnitName(HouseFrameInformation dto, Map<String, Object[]> errorsArgsMap) {
		if (dto.getDistribution() != null && dto.getDistribution().equals(HouseValidateConstant.HouseOccupancyType.ASSIGN.getValue())) {
			if (dto.getUserFrameList() == null || dto.getUserFrameList().size() == 0) {
				errorsArgsMap.put("userName", new Object[] { "所属客户" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				ValidateResult result = validateUnitName(dto.getUserFrameList());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("userName", new Object[] { result.getMsg() });
				}
			}
		}
	}

	@Override
	public AjaxValidationResult preValidate(BaseModel baseModel) {
		HouseFrameInformation dto = (HouseFrameInformation) baseModel;
		AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
		Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
		/*if (dto.getAddType() != null && dto.getAddType() == 1 && !existHouseId(dto)) {
			if (errorsArgsMap.get("houseId") == null) {
				errorsArgsMap.put("houseId", new String[] { "[机房ID" + ErrorCodeConstant.ERROR_1008.getErrorMsg() + "]" });
			} else {
				Object[] errorMsg = errorsArgsMap.get("houseId");
				arrayExpand(errorMsg, "[机房ID" + ErrorCodeConstant.ERROR_1008.getErrorMsg() + "]");
				errorsArgsMap.put("houseId", errorMsg);
			}
		}*/

		/*if (!uniqueFrameName(dto)) {
			if (errorsArgsMap.get("frameName") == null) {
				errorsArgsMap.put("frameName", new String[] { "[机架名称" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
			} else {
				Object[] errorMsg = errorsArgsMap.get("frameName");
				arrayExpand(errorMsg, "[机房名称" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]");
				errorsArgsMap.put("frameName", errorMsg);
			}
		}*/

		// 校验所属客户
		//suitableUnitName(dto, errorsArgsMap);
		return ajaxValidationResult;
		// return validateBean(baseModel);
	}

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
    	HouseFrameInformationDTO dto = (HouseFrameInformationDTO)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        if ( !ifExistHouseId(dto) ){
            if (errorsArgsMap.get("houseId")==null){
                errorsArgsMap.put("houseId", new String[]{"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseId");
                arrayExpand(errorMsg,"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("houseId", errorMsg);
            }
        }

        // 校验所属客户
        suitableUnitName(dto,errorsArgsMap);
        return ajaxValidationResult;
    }

	@Override
	public AjaxValidationResult validateBean(BaseModel baseModel) {
		List<ObjectError> errors = new ArrayList<ObjectError>(0);
		Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
		HouseFrameInformation dto = (HouseFrameInformation) baseModel;
		ValidateResult result = null;
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.DELETE.getValue()) {
			if (dto.getAddType() != null && dto.getAddType() == 1) {
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
				}
			}
			if (dto.getFrameId() == null) {
				errorsArgsMap.put("frameId", new Object[] { "机架ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			}
		}
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
			if (dto.getFrameName() == null) {
				errorsArgsMap.put("frameName", new Object[] { "机架名称" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				result = validateFrameName(dto.getFrameName().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("frameName", new Object[] { result.getMsg() });
				} else {
					// 校验机架名称是否重复
					if (!uniqueFrameName(dto)) {
						errorsArgsMap.put("frameName", new String[] { "[机架名称" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
					}
				}
			}
			/*if (dto.getDistribution() == null) {
				errorsArgsMap.put("distribution", new Object[] { "分配状态" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				result = validateDistribution(dto.getDistribution().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("distribution", new Object[] { result.getMsg() });
				}
			}*/
			if (dto.getOccupancy() == null) {
				errorsArgsMap.put("occupancy", new Object[] { "占用状态" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				result = validateOccupancy(dto.getOccupancy().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("occupancy", new Object[] { result.getMsg() });
				}
			}
			if (dto.getUseType() == null) {
				errorsArgsMap.put("useType", new Object[] { "使用类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				result = validateUseType(dto.getUseType().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("useType", new Object[] { result.getMsg() });
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
			suitableUnitName(dto, errorsArgsMap);
			validateIdTypeAndIdNumber(dto.getUserFrameList(), errorsArgsMap);
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

    private ValidateResult validateHouseId(String houseId){
        String propertyName = "机房ID";
        return BasicValidateUtils.validateMaxLength(propertyName,houseId,getFiledLengthByPropertyName(prefix,"houseId"));
    }

//    private ValidateResult validateHouseName(String houseName){
//        String propertyName = "机房名称";
//        return BasicValidateUtils.validateMaxLength(propertyName,houseName,getFiledLengthByPropertyName(prefix,"houseId"));
//    }

    private ValidateResult validateFrameName(String frameName){
        String propertyName = "机架名称";
        return BasicValidateUtils.validateMaxLength(propertyName,frameName,getFiledLengthByPropertyName(prefix,"frameName"));
    }

    private ValidateResult validateDistribution(String distribution){
        String propertyName = "分配状态";
        String tableName = JCDMRedisConstant.IDC_JCDM_FPZT;
        return validateInRange(propertyName,tableName+":"+distribution,distribution.toString());
    }

    private ValidateResult validateOccupancy(String occupancy){
        String propertyName = "占用状态";
        String tableName = JCDMRedisConstant.IDC_JCDM_ZYLX;
        return validateInRange(propertyName,tableName+":"+occupancy,occupancy.toString());
    }

    private ValidateResult validateUseType(String useType){
        String propertyName = "使用类型";
        String tableName = JCDMRedisConstant.IDC_JCDM_SYLX;
        return validateInRange(propertyName,tableName+":"+useType,useType.toString());
    }

    private ValidateResult validateUnitName(List<HouseUserFrameInformation> userFrameList){
        String propertyName = "所属客户";
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        for(HouseUserFrameInformation userFrame:userFrameList){
        	ValidateResult result = BasicValidateUtils.validateMaxLength(propertyName,userFrame.getUserName(),getFiledLengthByPropertyName(prefix,"userName"));
        	if(!vaildateSuccess(result)){
        		code = result.getCode();
        		stringBuilder.append(result.getMsg());
        	}
        }
        return new ValidateResult(code, stringBuilder.toString());
    }
    
	private void validateIdTypeAndIdNumber(List<HouseUserFrameInformation> userFrameList, Map<String, Object[]> errorsArgsMap) {
		if (userFrameList != null && userFrameList.size() > 0) {
			for (HouseUserFrameInformation userFrame : userFrameList) {
				/*if(!StringUtils.isEmpty(userFrame.getUserName())){
					
				}*/
				if (!StringUtils.isEmpty(userFrame.getIdNumber()) && StringUtils.isEmpty(userFrame.getIdType())) {
					errorsArgsMap.put("idType", new Object[] { "证件类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
				}
				if (StringUtils.isEmpty(userFrame.getIdNumber()) && !StringUtils.isEmpty(userFrame.getIdType())) {
					errorsArgsMap.put("idNumber", new Object[] { "证件号码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
				}
				if (!StringUtils.isEmpty(userFrame.getIdNumber()) && !StringUtils.isEmpty(userFrame.getIdType())) {
					ValidateResult result = validateIdType(userFrame.getIdType().toString());
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("idType", new Object[] { result.getMsg() });
					}
					result = validateIdNumber(userFrame.getIdNumber().toString());
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("idNumber", new Object[] { result.getMsg() });
					}
					if (!BasicValidateUtils.idNumMatchType(userFrame.getIdType(), userFrame.getIdNumber())) {
						if (errorsArgsMap.get("idType") == null) {
							errorsArgsMap.put("idType", new String[] { "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]" });
						} else {
							Object[] errorMsg = errorsArgsMap.get("idType");
							arrayExpand(errorMsg, "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
							errorsArgsMap.put("idType", errorMsg);
						}
					}else{
						if(userFrame.getUserName()==null||userFrame.getUserName()==""){
							errorsArgsMap.put("userName", new Object[] { "所属客户" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
						}
					}
				}
			}
		}
	}

    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }
    
    public boolean ifExistHouseId(HouseFrameInformationDTO dto) {
        if (dto.getHouseId()==null){
            return false;
        }
        HouseInformation houseInformationDTO = new HouseInformation();
        houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
        return rptHouseMainMapper.findByHouseId(houseInformationDTO)!=null?true:false;
    }
    
	private ValidateResult validateIdType(String idType) {
		String propertyName = "证件类型";
		return validateIdCardType(propertyName, idType);
	}

	private ValidateResult validateIdNumber(String idNumber) {
		String propertyName = "证件号码";
		return validateIdCard(propertyName, idNumber, getFiledLengthByPropertyName(prefix, "idNumber"));
	}
}
