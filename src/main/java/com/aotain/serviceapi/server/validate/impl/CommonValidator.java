package com.aotain.serviceapi.server.validate.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aotain.common.config.ContextUtil;
import com.aotain.common.config.redis.BaseRedisService;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.serviceapi.server.constant.JCDMRedisConstant;
import com.aotain.serviceapi.server.constant.ResultCodeEnum;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.service.CommonUtilService;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.util.FieldLengthProps;
import com.aotain.serviceapi.server.util.IpUtil;
import com.google.common.collect.Lists;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
public class CommonValidator {

    public String getErrorMsg(String propertyName,String errorMsg){
        return String.format("[%s%s]",propertyName,errorMsg);
    }

    public Object[] getErrorMsgObject(String propertyName,String errorMsg){
        return new Object[]{String.format("[%s%s]",propertyName,errorMsg)};
    }

    public Object[] getErrorMsgObject(String msg){
        return new Object[]{msg};
    }

    /**
     * 根据前缀和属性名获取属性长度
     * @param prefix
     * @param propertyName
     * @return
     */
    public Integer getFiledLengthByPropertyName(String prefix,String propertyName){
        String key = prefix.toUpperCase()+"."+propertyName.toUpperCase();
        return FieldLengthProps.instance.getLength(key);
    }

    public Object[] arrayExpand(Object[] source,Object target){
        source= Arrays.copyOf(source, source.length+1);
        source[source.length-1]=target;
        return source;
    }

    public boolean vaildateSuccess(ValidateResult validateResult){
        return validateResult.getCode() == ResultCodeEnum.SUCCESS.getCode();
    }

    /**
     * 校验开始ip，校验ip长度和ip格式
     * @param propertyName
     * @param startIp
     * @return
     */
    public ValidateResult validateStartIp(String propertyName,String startIp){
        int code = 200;
        StringBuilder msg = new StringBuilder("");
        if (!vaildateSuccess(BasicValidateUtils.validateEmpty(propertyName,startIp))){
            return BasicValidateUtils.validateEmpty(propertyName,startIp);
        }
        ValidateResult lengthValidateResult = BasicValidateUtils.validateMaxLength(propertyName,startIp,64);
        if (lengthValidateResult.getCode()!=ResultCodeEnum.SUCCESS.getCode()){
            code = lengthValidateResult.getCode();
            msg.append(lengthValidateResult.getMsg());
        }
        ValidateResult validateResult = BasicValidateUtils.validateStartIp(propertyName,startIp);
        if (validateResult.getCode() != ResultCodeEnum.SUCCESS.getCode()){
            code = validateResult.getCode();
            msg.append(validateResult.getMsg());
        }
        return new ValidateResult(code,msg.toString());
    }

    /**
     * 结束ip校验，  -- ip长度 ip格式 以及与起始ip关系
     * @param propertyName
     * @param startIp
     * @param endIp
     * @return
     */
    public ValidateResult validateEndIp(String propertyName,String startIp,String endIp){
//        if (validateStartIp(propertyName,startIp).getCode() != ResultCodeEnum.SUCCESS.getCode()){
//            return validateStartIp("起始IP地址",startIp);
//        }
        if (IpUtil.isIpAddress(endIp)){
            if (IpUtil.isIpAddress(startIp)&&IpUtil.isIpAddress(endIp)){
                return BasicValidateUtils.validateEndIp(propertyName,startIp,endIp);
            } else {
                return validateStartIp(propertyName,endIp);
            }
        } 
        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }

    /**
     * 校验时间  --校验日期格式
     * @param propertyName
     * @param format
     * @param value
     * @return
     */
	public ValidateResult validateTime(String propertyName, String format, String value) {
		int code = 200;
		StringBuilder msg = new StringBuilder("");
		if (BasicValidateUtils.validateEmpty(propertyName, value).getCode() != ResultCodeEnum.SUCCESS.getCode()) {
			return BasicValidateUtils.validateEmpty(propertyName, value);
		}
		ValidateResult lengthValidateResult = BasicValidateUtils.validateMaxLength(propertyName, value, 10);
		if (lengthValidateResult.getCode() != ResultCodeEnum.SUCCESS.getCode()) {
			code = lengthValidateResult.getCode();
			msg.append(lengthValidateResult.getMsg());
		}
		ValidateResult dateValidateResult = BasicValidateUtils.validateDateWithDefinedFormat(propertyName, value, format);
		if (dateValidateResult.getCode() != ResultCodeEnum.SUCCESS.getCode()) {
			code = dateValidateResult.getCode();
			msg.append(dateValidateResult.getMsg());
		}
		return new ValidateResult(code, msg.toString());
	}

    /**
     * 校验value是否在指定的取值范围内
     * @param key
     * @param value
     * @return
     */
    public static ValidateResult validateInRange(String propertyName,String key,String value){
        BaseRedisService<String,String,String> baseRedisService = ContextUtil.getContext().getBean(BaseRedisService.class);
        String result = baseRedisService.getHash(key,"ID");
        if (result==null||!result.equals(value)){
            return ValidateResult.getErrorResult("["+propertyName+"不合法,不在指定取值范围内]");
        }
        return ValidateResult.getSuccessResult();
    }

    /**
     * 校验证件号码
     * @param propertyName
     * @param idCard
     * @return
     */
    public ValidateResult validateIdCard(String propertyName,String idCard,int length){
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult cardValidateResult = BasicValidateUtils.validateCardNum(propertyName,idCard);
        if (!vaildateSuccess(cardValidateResult)){
            code = cardValidateResult.getCode();
            stringBuilder.append(cardValidateResult.getMsg());
        }
        ValidateResult lengthValidateResult = BasicValidateUtils.validateMaxLengthWithoutEmptyCheck(propertyName,idCard,length);
        if (!vaildateSuccess(lengthValidateResult)){
            code = lengthValidateResult.getCode();
            stringBuilder.append(lengthValidateResult.getMsg());
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    /**
     * 校验单位证件号码
     * @param propertyName
     * @param idCard
     * @return
     */
    public ValidateResult validateUnitIdCard(String propertyName,String idCard,int length){
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult cardValidateResult = BasicValidateUtils.validateUnitCardNum(propertyName,idCard);
        if (!vaildateSuccess(cardValidateResult)){
            code = cardValidateResult.getCode();
            stringBuilder.append(cardValidateResult.getMsg());
        }
        ValidateResult lengthValidateResult = BasicValidateUtils.validateMaxLengthWithoutEmptyCheck(propertyName,idCard,length);
        if (!vaildateSuccess(lengthValidateResult)){
            code = lengthValidateResult.getCode();
            stringBuilder.append(lengthValidateResult.getMsg());
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    /**
     * 校验固定电话格式
     * @param telephoneNum
     * @return
     */
    public ValidateResult validateTelephoneNum(String propertyName,String telephoneNum,int length){

        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult cardValidateResult = BasicValidateUtils.validateTelephoneNum(propertyName,telephoneNum);
        if (!vaildateSuccess(cardValidateResult)){
            code = cardValidateResult.getCode();
            stringBuilder.append(cardValidateResult.getMsg());
        }
        ValidateResult lengthValidateResult = BasicValidateUtils.validateMaxLengthWithoutEmptyCheck(propertyName,telephoneNum,length);
        if (!vaildateSuccess(lengthValidateResult)){
            code = lengthValidateResult.getCode();
            stringBuilder.append(lengthValidateResult.getMsg());
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    /**
     * 校验移动电话格式
     * @param propertyName
     * @param mobileNum
     * @return
     */
    public ValidateResult validateMobileNum(String propertyName,String mobileNum,int length){
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult cardValidateResult = BasicValidateUtils.validateMobileNum(propertyName,mobileNum);
        if (!vaildateSuccess(cardValidateResult)){
            code = cardValidateResult.getCode();
            stringBuilder.append(cardValidateResult.getMsg());
        }
        ValidateResult lengthValidateResult = BasicValidateUtils.validateMaxLengthWithoutEmptyCheck(propertyName,mobileNum,length);
        if (!vaildateSuccess(lengthValidateResult)){
            code = lengthValidateResult.getCode();
            stringBuilder.append(lengthValidateResult.getMsg());
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    /**
     * 校验email格式
     * @param propertyName
     * @param email
     * @param length
     * @return
     */
    public ValidateResult validateEmail(String propertyName,String email,int length){
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        ValidateResult cardValidateResult = BasicValidateUtils.validateEmailNum(propertyName,email);
        if (!vaildateSuccess(cardValidateResult)){
            code = cardValidateResult.getCode();
            stringBuilder.append(cardValidateResult.getMsg());
        }
        ValidateResult lengthValidateResult = BasicValidateUtils.validateMaxLengthWithoutEmptyCheck(propertyName,email,length);
        if (!vaildateSuccess(lengthValidateResult)){
            code = lengthValidateResult.getCode();
            stringBuilder.append(lengthValidateResult.getMsg());
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    /**
     * 校验个人证件类型是否合法
     * @param propertyName
     * @param idCardType
     * @return
     */
    public ValidateResult validatePersonalCardType(String propertyName,String idCardType){
        int type = Integer.valueOf(idCardType);
        if (type==2||type==7||type==8||type==9){
            return ValidateResult.getSuccessResult();
        }
        return ValidateResult.getErrorResult(getErrorMsg(propertyName,"不合法"));
    }

    /**
     * 校验证件类型是否合法
     * @param propertyName
     * @param idCardType
     * @return
     */
    public ValidateResult validateIdCardType(String propertyName,String idCardType){
        String tableName = JCDMRedisConstant.IDC_JCDM_ZJLX;
        return validateInRange(propertyName,tableName+":"+idCardType,idCardType.toString());
    }

    /**
     * 校验ip地址使用方式是否合法
     * @param propertyName
     * @param useType
     * @return
     */
    public ValidateResult validateIpUseType(String propertyName,String useType){
        String tableName = JCDMRedisConstant.IDC_JCDM_IPDZSYFS;
        return validateInRange(propertyName,tableName+":"+useType,useType.toString());
    }

    /**
     * 校验服务内容是否合法       格式--1,5,6
     * @param propertyName
     * @param ids
     * @return
     */
    public ValidateResult validateContent(String propertyName,String ids){
        String tableName = JCDMRedisConstant.IDC_JCDM_FWNR;
        String[] idArray = ids.split(",");
        ValidateResult validateResult = null;
        int code = 200;
        StringBuilder stringBuilder = new StringBuilder("");
        // 只包含一条记录
        if (idArray==null){
            validateResult = validateInRange(propertyName+":"+ids,tableName+":"+ids,ids.toString());
            if (!vaildateSuccess(validateResult)){
                code = validateResult.getCode();
                stringBuilder.append(validateResult.getMsg());
            }
            return new ValidateResult(code,stringBuilder.toString());
        }
        for (int i=0;i<idArray.length;i++){
            validateResult = validateInRange(propertyName+":"+idArray[i],tableName+":"+idArray[i],idArray[i].toString());
            if (!vaildateSuccess(validateResult)){
                code = validateResult.getCode();
                stringBuilder.append(validateResult.getMsg());
            }
        }
        return new ValidateResult(code,stringBuilder.toString());
    }

    /**
     * 校验应用服务类型是否合法
     * @param propertyName
     * @param serviceType
     * @return
     */
    public ValidateResult validateServiceType(String propertyName,String serviceType){
        String tableName = JCDMRedisConstant.IDC_JCDM_YYFWLX;
        return validateInRange(propertyName,tableName+":"+serviceType,serviceType.toString());
    }

    /**
     * 校验业务类型是否合法
     * @param propertyName
     * @param businessType
     * @return
     */
    public ValidateResult validateBusinessType(String propertyName,String businessType){
        String tableName = JCDMRedisConstant.IDC_JCDM_YWLX;
        return validateInRange(propertyName,tableName+":"+businessType,businessType.toString());
    }

    /**
     * 校验网站备案类型是否合法
     * @param propertyName
     * @param regType
     * @return
     */
    public ValidateResult validateRegType(String propertyName,String regType){
        String tableName = JCDMRedisConstant.IDC_JCDM_WZBALX;
        return validateInRange(propertyName,tableName+":"+regType,regType.toString());
    }

    /**
     * 校验接入方式是否合法
     * @param propertyName
     * @param setMode
     * @return
     */
    public ValidateResult validateSetmode(String propertyName,String setMode){
        String tableName = JCDMRedisConstant.IDC_JCDM_JRFS;
        return validateInRange(propertyName,tableName+":"+setMode,setMode.toString());
    }

    /**
     * 校验虚拟主机状态是否合法
     * @param propertyName
     * @param status
     * @return
     */
    public ValidateResult validateVirtualStatus(String propertyName,String status){
        String tableName = JCDMRedisConstant.IDC_JCDM_XNZJZT;
        return validateInRange(propertyName,tableName+":"+status,status.toString());
    }

    /**
     * 校验虚拟主机类型是否合法
     * @param propertyName
     * @param type
     * @return
     */
    public ValidateResult validateVirtualType(String propertyName,String type){
        String tableName = JCDMRedisConstant.IDC_JCDM_XNZJLX;
        return validateInRange(propertyName,tableName+":"+type,type.toString());
    }

    /**
     * 校验用户属性是否合法
     * @param propertyName
     * @param nature
     * @return
     */
    public ValidateResult validateUserNature(String propertyName,String nature){
        String tableName = JCDMRedisConstant.IDC_JCDM_YHSX;
        return validateInRange(propertyName,tableName+":"+nature,nature.toString());
    }

    /**
     * 校验用户标识是否合法
     * @param propertyName
     * @param identify
     * @return
     */
    public ValidateResult validateUserIdentify(String propertyName,String identify){
        String tableName = JCDMRedisConstant.IDC_JCDM_YHBS;
        return validateInRange(propertyName,tableName+":"+identify,identify.toString());
    }

    /**
     * 校验单位属性是否合法
     * @param propertyName
     * @param nature
     * @return
     */
    public ValidateResult validateUnitNature(String propertyName,String nature){
        String tableName = JCDMRedisConstant.IDC_JCDM_DWSX;
        return validateInRange(propertyName,tableName+":"+nature,nature.toString());
    }

    private String transfer(List<String> list, Map<String, String> mapping) {
    	StringBuffer resultBuffer = new StringBuffer();
    	if (list != null && list.size() > 0) {
    		for (String key : list) {
				if (mapping.containsKey(key)) {
					resultBuffer.append(mapping.get(key)).append(",");
				}
			}
    		resultBuffer.replace(resultBuffer.lastIndexOf(","), resultBuffer.length(), "");
    	}
		return resultBuffer.toString();
	}
    
    /**
     * 校验地市码是否合法
     * @param propertyName
     * @param baseModel
     * @return
     */
	public ValidateResult validateAreaCode(String propertyName, BaseModel baseModel) {
		CommonUtilService commonUtilService = ContextUtil.getContext().getBean(CommonUtilService.class);
		Map<String, String> subordinateMapping = commonUtilService.getSubordinateMappingMap();
		int code = 200;
		StringBuilder msg = new StringBuilder("");
		if (BasicValidateUtils.validateEmpty(propertyName, baseModel.getAreaCode()).getCode() != ResultCodeEnum.SUCCESS.getCode()) {
			return BasicValidateUtils.validateEmpty(propertyName, baseModel.getAreaCode());
		}

		List<String> areaCodeList = baseModel.getCityCodeList();
		String[] areaCodeArray = baseModel.getAreaCode().split(",");
		if (areaCodeList == null || areaCodeList.size() == 0) {
			return new ValidateResult(500, "[账户携带地市码信息为空]");
		}
		// 只包含一条记录
		if (areaCodeArray == null) {
			if (!areaCodeList.contains(baseModel.getAreaCode())) {
				code = 500;
				msg.append("[" + propertyName + ":" + subordinateMapping.get(baseModel.getAreaCode()) + ",不在授权范围内,有效范围:" + transfer(areaCodeList, subordinateMapping) + "]");
			}
			return new ValidateResult(code, msg.toString());
		}

		for (int i = 0; i < areaCodeArray.length; i++) {
			if (!areaCodeList.contains(areaCodeArray[i])) {
				code = 500;
				msg.append("[" + propertyName + ":" + subordinateMapping.get(areaCodeArray[i]) + ",不在授权范围内,有效范围:" + transfer(areaCodeList, subordinateMapping) + "]");
			}
		}
		return new ValidateResult(code, msg.toString());
	}
	
	/**
	 * 校验用户携带的机房信息
	 * @param propertyName
	 * @param baseModel
	 * @return
	 */
	public ValidateResult validateAuthHouse(long houseId, BaseModel baseModel) {
		CommonUtilService commonUtilService = ContextUtil.getContext().getBean(CommonUtilService.class);
		Map<String, String> houseMapping = commonUtilService.getHouseMappingMap();
		int code = 200;
		StringBuilder msg = new StringBuilder("");
		List<String> authHouseList = baseModel.getUserAuthHouseList();
		if (authHouseList == null || authHouseList.size() == 0) {
			return new ValidateResult(500, "[账户携带的机房ID信息为空]");
		}
		if (!authHouseList.contains(houseId + "")) {
			code = 500;
			msg.append("[" + houseMapping.get(houseId + "") + ",不在授权范围内,有效范围:" + transfer(authHouseList, houseMapping) + "]");
		}
		return new ValidateResult(code, msg.toString());
	}
	
	/**
	 * 校验用户携带的专线机房标识信息
	 * @param identity
	 * @param baseModel
	 * @return
	 */
	public ValidateResult validateAuthIdentity(int identity, BaseModel baseModel) {
		CommonUtilService commonUtilService = ContextUtil.getContext().getBean(CommonUtilService.class);
		Map<String, String> mapping = commonUtilService.getIdentityMappingMap();
		int code = 200;
		StringBuilder msg = new StringBuilder("");
		List<String> authIdentityList = baseModel.getUserAuthIdentityList();
		if (authIdentityList == null) {
			return new ValidateResult(500, "[账户携带的专线标识信息为空]");
		}
		if (!authIdentityList.contains(identity + "")) {
			code = 500;
			msg.append("[" + mapping.get(identity + "") + ",不在授权范围内,有效范围:" + transfer(authIdentityList, mapping) + "]");
		}
		return new ValidateResult(code, msg.toString());
	}

    public static void main(String[] args) {
        System.out.println("1,2,3".split(",").length);
        CommonValidator commonValidator = new CommonValidator();
        BaseModel baseModel = new BaseModel();
        baseModel.setAreaCode("440300");
        List<String> list = Lists.newArrayList();
        list.add("440100");
        list.add("440200");
        baseModel.setCityCodeList(list);
        ValidateResult result = commonValidator.validateAreaCode("areaCode", baseModel);
        System.out.println(result.getMsg());
        System.out.println(list.toString());
        
        Map<String, String> houseMapping = new HashMap<String, String>();
        houseMapping.put("1", "1");
        houseMapping.put("2", "2");
        houseMapping.put("3", "3");
        System.out.println(houseMapping.values());
    }
}
