package com.aotain.serviceapi.server.validate.impl;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.preinput.IdcInformationMapper;
import com.aotain.serviceapi.server.dao.report.IdcInfoMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.validate.IdcInformationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.*;

/**
 * 经营者信息校验类
 *
 * @author bang
 * @date 2018/07/09
 */
@Service
public class IdcInformationValidatorImpl extends CommonValidator implements IdcInformationValidator {

    private static final String PREFIX = FieldLengthPropertyPrefix.IDC_PREFIX;

    @Autowired
    private IdcInformationMapper idcInformationMapper;
    
    @Autowired
    private IdcInfoMapper rptIdcinfoMapper;
    
    @Override
    public boolean uniqueIdcId(IdcInformation dto) {
        if (dto.getIdcId()==null){
            return false;
        }
        return idcInformationMapper.findByIdcId(dto)==null?true:false;
    }

    @Override
    public boolean uniqueIdcName(IdcInformation dto) {
        if (dto.getIdcName()==null){
            return false;
        }
        return idcInformationMapper.findByIdcName(dto)==null?true:false;
    }

    @Override
    public AjaxValidationResult preValidate(BaseModel baseModel) {
        IdcInformation dto = (IdcInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        if ( !uniqueIdcId(dto) ){
            if (errorsArgsMap.get("idcId")==null){
                errorsArgsMap.put("idcId", new String[]{"[许可证号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("idcId");
                arrayExpand(errorMsg,"[许可证号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("idcId", errorMsg);
            }
        }
        if ( !uniqueIdcName(dto) ){
            if (errorsArgsMap.get("idcName")==null){
                errorsArgsMap.put("idcName", new String[]{"[经营者名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("idcName");
                arrayExpand(errorMsg,"[经营者名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("idcName", errorMsg);
            }
        }

        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
        IdcInformation dto = (IdcInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        if ( !ifRepeatIdcId(dto) ){
            if (errorsArgsMap.get("idcId")==null){
                errorsArgsMap.put("idcId", new String[]{"[许可证号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("idcId");
                arrayExpand(errorMsg,"[许可证号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("idcId", errorMsg);
            }
        }
        if ( !ifRepeatIdcName(dto) ){
            if (errorsArgsMap.get("idcName")==null){
                errorsArgsMap.put("idcName", new String[]{"[经营者名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("idcName");
                arrayExpand(errorMsg,"[经营者名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("idcName", errorMsg);
            }
        }

        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult validateBean(BaseModel baseModel) {
        IdcInformation dto = (IdcInformation)baseModel;
        List<ObjectError> errors = new ArrayList<ObjectError>(0);
        Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
        ValidateResult vr = null;

        if(dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue() || dto.getOperateType() == HouseConstant.OperationTypeEnum.DELETE.getValue()){
            if (dto.getJyzId()==null){
                errorsArgsMap.put("jyzId",new Object[]{"经营者ID"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+""});
            }
        }

        if(dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue() || dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()) {
            if (dto.getIdcId() == null) {
                errorsArgsMap.put("idcId", new Object[]{"许可证号" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateIdcId(dto.getIdcId().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("idcId", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getIdcName() == null) {
                errorsArgsMap.put("idcName", new Object[]{"经营者名称" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = ValidateIdcName(dto.getIdcName().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("idcName", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getCorporater() == null) {
                errorsArgsMap.put("corporater", new Object[]{"企业法人" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = ValidateCorporater(dto.getCorporater().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("corporater", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getIdcZipCode() == null) {
                errorsArgsMap.put("idcZipCode", new Object[]{"通信地址邮编" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = ValidateIdcZipCode(dto.getIdcZipCode().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("idcZipCode", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getIdcAddress() == null) {
                errorsArgsMap.put("idcAddress", new Object[]{"经营者通信地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = ValidateIdcAddress(dto.getIdcAddress().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("idcAddress", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getOfficerName() == null) {
                errorsArgsMap.put("officerName", new Object[]{"网络信息安全责任人姓名" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = ValidateOfficerName(dto.getOfficerName().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("officerName", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getOfficerIdType() == null) {
                errorsArgsMap.put("officerIdType", new Object[]{"网络信息安全责任人证件类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateOfficerIdType(dto.getOfficerIdType().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("officerIdType", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getOfficerId() == null) {
                errorsArgsMap.put("officerId", new Object[]{"网络信息安全责任人证件号码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateOfficerId(dto.getOfficerId().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("officerId", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getOfficerIdType() != null && dto.getOfficerId() != null) {
                if (!BasicValidateUtils.idNumMatchType(dto.getOfficerIdType(), dto.getOfficerId())) {
                    if (errorsArgsMap.get("officerId") == null) {
                        errorsArgsMap.put("officerId", new String[]{"[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]"});
                    } else {
                        Object[] errorMsg = errorsArgsMap.get("officerId");
                        arrayExpand(errorMsg, "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
                        errorsArgsMap.put("officerId", errorMsg);
                    }
                }
            }

            if (dto.getOfficerTelephone() == null) {
                errorsArgsMap.put("officerTelephone", new Object[]{"网络信息安全责任人固定电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateOfficerTelephone(dto.getOfficerTelephone().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("officerTelephone", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getOfficerMobile() == null) {
                errorsArgsMap.put("officerMobile", new Object[]{"网络信息安全责任人移动电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateOfficerMobile(dto.getOfficerMobile().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("officerMobile", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getOfficerEmail() == null) {
                errorsArgsMap.put("officerEmail", new Object[]{"网络信息安全责任人Email" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateOfficerEmail(dto.getOfficerEmail().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("officerEmail", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getEcName() == null) {
                errorsArgsMap.put("ecName", new Object[]{"应急联系人姓名" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateEcName(dto.getEcName().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("ecName", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getEcIdType() == null) {
                errorsArgsMap.put("ecIdType", new Object[]{"应急联系人证件类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateEcIdType(dto.getEcIdType().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("ecIdType", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getEcId() == null) {
                errorsArgsMap.put("ecId", new Object[]{"应急联系人证件号码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateEcId(dto.getEcId().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("ecId", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getEcIdType() != null && dto.getEcId() != null) {
                if (!BasicValidateUtils.idNumMatchType(dto.getEcIdType(), dto.getEcId())) {
                    if (errorsArgsMap.get("ecId") == null) {
                        errorsArgsMap.put("ecId", new String[]{"[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]"});
                    } else {
                        Object[] errorMsg = errorsArgsMap.get("ecId");
                        arrayExpand(errorMsg, "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
                        errorsArgsMap.put("ecId", errorMsg);
                    }
                }
            }

            if (dto.getEcTelephone() == null) {
                errorsArgsMap.put("ecTelephone", new Object[]{"应急联系人固定电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateEcTelephone(dto.getEcTelephone().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("ecTelephone", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getEcMobile() == null) {
                errorsArgsMap.put("ecMobile", new Object[]{"应急联系人移动电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateEcMobile(dto.getEcMobile().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("ecMobile", new Object[]{vr.getMsg()});
                }
            }
            if (dto.getEcEmail() == null) {
                errorsArgsMap.put("ecEmail", new Object[]{"应急联系人Email" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + ""});
            } else {
                vr = validateEcEmail(dto.getEcEmail().toString());
                if (!vaildateSuccess(vr)) {
                    errorsArgsMap.put("ecEmail", new Object[]{vr.getMsg()});
                }
            }
            /*if (dto.getHouseNum()==null){
                errorsArgsMap.put("houseNum",new Object[]{"IDC机房数量"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+""});
            } else {
                if (!vaildateSuccess(validateHouseNum(dto.getHouseNum().toString()))){
                    errorsArgsMap.put("houseNum",new Object[]{validateHouseNum(dto.getHouseNum().toString()).getMsg()});
                }
            }*/

            if (dto.getUpdateUserId()==null){
                errorsArgsMap.put("updateUserId",new Object[]{"修改者"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+""});
            }
        }

        if(dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()){
            if (dto.getCreateUserId()==null){
                errorsArgsMap.put("createUserId",new Object[]{"创建者"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+""});
            }
        }

        return new AjaxValidationResult(errors,errorsArgsMap);
    }

    private ValidateResult validateIdcId(String idcId){
        String propertyName = "许可证号";
        return BasicValidateUtils.validateMaxLength(propertyName,idcId,getFiledLengthByPropertyName(PREFIX,"idcId"));
    }

    private ValidateResult ValidateIdcName(String idcName){
        String propertyName = "经营者名称";
        return BasicValidateUtils.validateMaxLength(propertyName,idcName,getFiledLengthByPropertyName(PREFIX,"idcName"));
    }

    private ValidateResult ValidateCorporater(String corporater){
        String propertyName = "企业法人";
        return BasicValidateUtils.validateMaxLength(propertyName,corporater,getFiledLengthByPropertyName(PREFIX,"corporater"));
    }

    private ValidateResult ValidateIdcZipCode(String idcZipCode){
        String propertyName = "通信地址邮编";
        return BasicValidateUtils.validateZipCode(propertyName,idcZipCode,getFiledLengthByPropertyName(PREFIX,"idcZipCode"));
    }

    private ValidateResult ValidateIdcAddress(String idcAddress){
        String propertyName = "经营者通信地址";
        return BasicValidateUtils.validateMaxLength(propertyName,idcAddress,getFiledLengthByPropertyName(PREFIX,"idcAddress"));
    }

    private ValidateResult ValidateOfficerName(String officerName){
        String propertyName = "网络信息安全责任人姓名";
        return BasicValidateUtils.validateMaxLength(propertyName,officerName,getFiledLengthByPropertyName(PREFIX,"officerName"));
    }

    private ValidateResult validateOfficerIdType(String officerIdType){
        String propertyName = "网络信息安全责任人证件类型";
        return validateIdCardType(propertyName,officerIdType);
    }

    private ValidateResult validateOfficerId(String officerId){
        String propertyName = "网络信息安全责任人证件号码";
        return validateIdCard(propertyName,officerId,getFiledLengthByPropertyName(PREFIX,"officerId"));
    }

    private ValidateResult validateOfficerTelephone(String officerTelephone){
        String propertyName = "网络信息安全责任人固定电话";
        return validateTelephoneNum(propertyName,officerTelephone,getFiledLengthByPropertyName(PREFIX,"officerTelephone"));
    }

    private ValidateResult validateOfficerMobile(String officerMobile){
        String propertyName = "网络信息安全责任人移动电话";
        return validateMobileNum(propertyName,officerMobile,getFiledLengthByPropertyName(PREFIX,"officerMobile"));
    }

    private ValidateResult validateOfficerEmail(String officerEmail){
        String propertyName = "网络信息安全责任人Email";
        return validateEmail(propertyName,officerEmail,getFiledLengthByPropertyName(PREFIX,"officerEmail"));
    }


    private ValidateResult validateEcName(String ecName){
        String propertyName = "应急联系人姓名";
        return BasicValidateUtils.validateMaxLength(propertyName,ecName,getFiledLengthByPropertyName(PREFIX,"ecName"));
    }

    private ValidateResult validateEcIdType(String ecIdType){
        String propertyName = "应急联系人证件类型";
        return validateIdCardType(propertyName,ecIdType);
    }

    private ValidateResult validateEcId(String ecId){
        String propertyName = "应急联系人证件号码";
        return validateIdCard(propertyName,ecId,getFiledLengthByPropertyName(PREFIX,"ecId"));
    }

    private ValidateResult validateEcTelephone(String ecTelephone){
        String propertyName = "应急联系人固定电话";
        return validateTelephoneNum(propertyName,ecTelephone,getFiledLengthByPropertyName(PREFIX,"ecTelephone"));
    }

    private ValidateResult validateEcMobile(String ecMobile){
        String propertyName = "应急联系人移动电话";
        return validateMobileNum(propertyName,ecMobile,getFiledLengthByPropertyName(PREFIX,"ecMobile"));
    }

    private ValidateResult validateEcEmail(String ecEmail){
        String propertyName = "应急联系人Email";
        return validateEmail(propertyName,ecEmail,getFiledLengthByPropertyName(PREFIX,"ecEmail"));
    }

    private ValidateResult validateHouseNum(String houseNum){
        String propertyName = "IDC机房数量";
        return BasicValidateUtils.validateEmpty(propertyName,houseNum);
    }
    
    public boolean ifRepeatIdcName(IdcInformation dto) {
        if (dto.getIdcId()==null){
            return false;
        }
        return rptIdcinfoMapper.findByIdcName(dto)==null?true:false;
    }
    public boolean ifRepeatIdcId(IdcInformation dto) {
        if (dto.getIdcId()==null){
            return false;
        }
        return rptIdcinfoMapper.findByIdcId(dto)==null?true:false;
    }
}
