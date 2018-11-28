package com.aotain.serviceapi.server.validate.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.HouseGatewayInformation;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.JCDMRedisConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.preinput.HouseLinkMapper;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.validate.IHouseLinkValidator;

/**
 * 机房链路校验器
 *
 * @author bang
 * @date 2018/07/06
 */
@Service
public class HouseLinkValidatorImpl extends CommonValidator  implements IHouseLinkValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_HOUSE_GATEWAY_PREFIX;

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private HouseLinkMapper houseLinkMapper;

    @Autowired
    private RptIsmsBaseHouseMapper rptHouseMainMapper;

	@Override
	public boolean existHouseId(HouseGatewayInformation dto) {
		if (dto.getHouseId() == null) {
			return false;
		}
		HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
		houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
		return housePrincipalMapper.findByHouseId(houseInformationDTO) != null ? true : false;
	}

    @Override
    public boolean existHouseName(HouseGatewayInformationDTO dto) {
        if (dto.getHouseName()==null){
            return false;
        }
        HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
        houseInformationDTO.setHouseName(dto.getHouseName());
        return housePrincipalMapper.findByHouseName(houseInformationDTO)!=null?true:false;
    }

	@Override
	public boolean uniqueLinkNo(HouseGatewayInformation info) {
		if (info.getLinkNo() == null) {
			return false;
		}
		HouseGatewayInformationDTO dto = new HouseGatewayInformationDTO();
		dto.setLinkNo(info.getLinkNo());
		dto.setGatewayId(info.getGatewayId());
		return houseLinkMapper.findByLinkNoAndId(dto) != null ? false : true;
	}

	@Override
	public AjaxValidationResult preValidate(BaseModel baseModel) {
		HouseGatewayInformation dto = (HouseGatewayInformation) baseModel;
		AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
		Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
		if (dto.getAddType() != null && dto.getAddType() == 1 && !existHouseId(dto)) {
			if (errorsArgsMap.get("houseId") == null) {
				errorsArgsMap.put("houseId", new String[] { "[机房ID" + ErrorCodeConstant.ERROR_1008.getErrorMsg() + "]" });
			} else {
				Object[] errorMsg = errorsArgsMap.get("houseId");
				arrayExpand(errorMsg, "[机房ID" + ErrorCodeConstant.ERROR_1008.getErrorMsg() + "]");
				errorsArgsMap.put("houseId", errorMsg);
			}
		}
		/*if (!uniqueLinkNo(dto)) {
			if (errorsArgsMap.get("linkNo") == null) {
				errorsArgsMap.put("linkNo", new String[] { "[链路编号" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
			} else {
				Object[] errorMsg = errorsArgsMap.get("linkNo");
				arrayExpand(errorMsg, "[链路编号" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]");
				errorsArgsMap.put("linkNo", errorMsg);
			}
		}*/
		return ajaxValidationResult;
		// return validateBean(baseModel);
	}

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
    	HouseGatewayInformationDTO dto = (HouseGatewayInformationDTO)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        if ( dto.getAddType()!=null && dto.getAddType()==1 && !ifExistHouseId(dto) ){
            if (errorsArgsMap.get("houseId")==null){
                errorsArgsMap.put("houseId", new String[]{"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseId");
                arrayExpand(errorMsg,"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("houseId", errorMsg);
            }
        }

        return ajaxValidationResult;
    }

	@Override
	public AjaxValidationResult validateBean(BaseModel baseModel) {
		List<ObjectError> errors = new ArrayList<ObjectError>(0);
		Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
		HouseGatewayInformation dto = (HouseGatewayInformation) baseModel;
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
			if (dto.getGatewayId() == null) {
				errorsArgsMap.put("gatewayId", new Object[] { "链路ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} 
		}
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
			if (dto.getLinkNo() == null) {
				//链路编号为空
				errorsArgsMap.put("linkNo", new Object[] { "链路编号" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				//链路编号超过长度限制
				result = validateLinkNo(dto.getLinkNo().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("linkNo", new Object[] { result.getMsg() });
				} else {
					if (!uniqueLinkNo(dto)) {
						errorsArgsMap.put("linkNo", new String[] { "[链路编号" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
					}
				}
			}
			if (dto.getBandWidth() == null) {
				errorsArgsMap.put("bandWidth", new Object[] { "机房互联网出入口带宽" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				result = validateBandWidth(dto.getBandWidth().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("bandWidth", new Object[] { result.getMsg() });
				}
			}
			if (dto.getGatewayIP() == null) {
				errorsArgsMap.put("gatewayIP", new Object[] { "机房出入口网关IP地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				result = validateGatewayIP(dto.getGatewayIP().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("gatewayIP", new Object[] { result.getMsg() });
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

    private ValidateResult validateHouseId(String houseId){
        String propertyName = "机房ID";
        return BasicValidateUtils.validateMaxLength(propertyName,houseId,getFiledLengthByPropertyName(prefix,"houseId"));
    }

    private ValidateResult validateLinkNo(String gatewayId){
        String propertyName = "链路编号";
        return BasicValidateUtils.validateMaxLength(propertyName,gatewayId,getFiledLengthByPropertyName(prefix,"gatewayId"));
    }

    private ValidateResult validateBandWidth(String bandWidth){
        String propertyName = "机房互联网出入口带宽";
        if(Long.parseLong(bandWidth)<0){
        	return ValidateResult.getErrorResult("["+propertyName+"不能为负数"+"]");
        }
        return BasicValidateUtils.validateEmpty(propertyName,bandWidth);
    }

    private ValidateResult validateGatewayIP(String gatewayIP){
        String propertyName = "机房出入口网关IP地址";
        return validateStartIp(propertyName,gatewayIP);
    }

    private ValidateResult validateAccessUnit(String accessUnit){
        String propertyName = "链路接入单位信息";
        return BasicValidateUtils.validateMaxLength(propertyName,accessUnit,getFiledLengthByPropertyName(prefix,"accessUnit"));
    }

    private ValidateResult validateLinkType(String linkType){
        String propertyName = "链路类型";
        String tableName = JCDMRedisConstant.IDC_JCDM_LLLX;
        return validateInRange(propertyName,tableName+":"+linkType,linkType.toString());
    }

    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }
    
    public boolean ifExistHouseId(HouseGatewayInformationDTO dto) {
        if (dto.getHouseId()==null){
            return false;
        }
        HouseInformation houseInformationDTO = new HouseInformation();
        houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
        return rptHouseMainMapper.findByHouseId(houseInformationDTO)!=null?true:false;
    }
}
