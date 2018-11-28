package com.aotain.serviceapi.server.util;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.aotain.serviceapi.server.constant.CommonRegexEnum;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.ResultCodeEnum;
import com.aotain.serviceapi.server.constant.ValidateResult;

/**
 * 基础数据校验工具类
 *
 * @author bang
 * @date 2018/07/06
 */
public class BasicValidateUtils {


    /**
     * 区域码map
     */
    public static Map<String, String> areaCodeMap = new HashMap<String, String>();

    /**
     * 事业法人校验码和校验码对应值的map
     */
    public static Map<Character, Integer> enterpriseCertificateMap = new HashMap<Character, Integer>();

    /**
     * 事业法人证书校验码：'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
     * 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
     * 'T', 'U', 'W', 'X', 'Y'
     */
    public static char[] enterpriseCertificateCodeNo = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'T', 'U',
            'W', 'X', 'Y' };

    /**
     * 事业法人证书校验码对应的值：{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
     * 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 }
     */
    public static int[] enterpriseCertificateStaVal = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };

    /**
     * 身份证检验位权重：{ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 }
     */
    public static int[] idcardWi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 身份证校验码：{ "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" }
     */
    public static String[] idcardValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

    /**
     * 组织机构校验位权重：{ 3, 7, 9, 10, 5, 8, 4, 2 }
     */
    public static int[] orgWi = { 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 事业法人证书校验位权重：{ 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30,
     * 28 }
     */
    public static int enterpriseCertificateWi[] = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };

    static {
        areaCodeMap.put("11", "北京");
        areaCodeMap.put("12", "天津");
        areaCodeMap.put("13", "河北");
        areaCodeMap.put("14", "山西");
        areaCodeMap.put("15", "内蒙古");
        areaCodeMap.put("21", "辽宁");
        areaCodeMap.put("22", "吉林");
        areaCodeMap.put("23", "黑龙江");
        areaCodeMap.put("31", "上海");
        areaCodeMap.put("32", "江苏");
        areaCodeMap.put("33", "浙江");
        areaCodeMap.put("34", "安徽");
        areaCodeMap.put("35", "福建");
        areaCodeMap.put("36", "江西");
        areaCodeMap.put("37", "山东");
        areaCodeMap.put("41", "河南");
        areaCodeMap.put("42", "湖北");
        areaCodeMap.put("43", "湖南");
        areaCodeMap.put("44", "广东");
        areaCodeMap.put("45", "广西");
        areaCodeMap.put("46", "海南");
        areaCodeMap.put("50", "重庆");
        areaCodeMap.put("51", "四川");
        areaCodeMap.put("52", "贵州");
        areaCodeMap.put("53", "云南");
        areaCodeMap.put("54", "西藏");
        areaCodeMap.put("61", "陕西");
        areaCodeMap.put("62", "甘肃");
        areaCodeMap.put("63", "青海");
        areaCodeMap.put("64", "宁夏");
        areaCodeMap.put("65", "新疆");
        areaCodeMap.put("71", "台湾");
        areaCodeMap.put("81", "香港");
        areaCodeMap.put("82", "澳门");
        areaCodeMap.put("91", "国外");

        for (int i = 0; i < enterpriseCertificateCodeNo.length; i++) {
            enterpriseCertificateMap.put(enterpriseCertificateCodeNo[i], enterpriseCertificateStaVal[i]);
        }
    }

    /**
     * 校验某字段是否为空
     * @param propertyName 属性名
     * @param value 属性值
     * @return
     */
    public static ValidateResult validateEmpty(String propertyName,String value){
        if (StringUtils.isEmpty(value)){
            return ValidateResult.getErrorResult("["+propertyName+"为空]");
        }
        return ValidateResult.getSuccessResult();
    }

    /**
     * 校验开始ip是否合法，只需校验ip格式
     * @param propertyName
     * @param startIp
     * @return
     */
    public static ValidateResult validateStartIp(String propertyName,String startIp){
//        if (BasicValidateUtils.validateEmpty(propertyName,startIp).getCode()== ResultCodeEnum.ERROR.getCode()){
//            return BasicValidateUtils.validateEmpty(propertyName,startIp);
//        }
        if (IpUtil.isIpAddress(startIp)){
            return ValidateResult.getSuccessResult();
        }
        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }

    /**
     * 校验ip是否合法，校验格式和数据合法性
     *      默认起始ip存在且合法
     * @param propertyName
     * @param startIp
     * @param endIp
     * @return
     */
    public static ValidateResult validateEndIp(String propertyName,String startIp,String endIp){
        if (BasicValidateUtils.validateEmpty(propertyName,startIp).getCode()== ResultCodeEnum.ERROR.getCode()){
            return ValidateResult.getSuccessResult();
        }
        if (!IpUtil.isIpAddress(endIp)){
            return ValidateResult.getErrorResult("["+propertyName+"不合法]");
        }
        if(IpUtil.isStartIPOverEndIp(startIp, endIp)){
        	return ValidateResult.getErrorResult("["+ErrorCodeConstant.ERROR_1007.getErrorMsg()+"]");
        }
        
        // 校验开始ip是否小于结束ip
       /* if(Tools.isIpAddress(endIp)){//IPV4地址
        	if ( !validateRangeIp(Tools.ip2long(startIp),Tools.ip2long(endIp) )){
                return ValidateResult.getErrorResult("["+ErrorCodeConstant.ERROR_1007.getErrorMsg()+"]");
            }
        	// 校验ipV4是否超过ip段范围
            if ( ifExceedIPRange(Tools.ip2long(startIp),Tools.ip2long(endIp)) ){
                return ValidateResult.getErrorResult("["+propertyName+ErrorCodeConstant.ERROR_1010.getErrorMsg()+"]");
            }
        }else{//IPv6地址
        	if(!validateIPV6RangeIp(Tools.ipv6toInt(startIp), Tools.ipv6toInt(endIp))){
        		 return ValidateResult.getErrorResult("["+ErrorCodeConstant.ERROR_1007.getErrorMsg()+"]");
        	}
        	//校验ipV4是否超过ip段范围
        	if(ifIPV6ExceedIPRange(Tools.ipv6toInt(startIp), Tools.ipv6toInt(endIp))){
        		 return ValidateResult.getErrorResult("["+propertyName+ErrorCodeConstant.ERROR_1010.getErrorMsg()+"]");
        	}
        }*/
        
        return ValidateResult.getSuccessResult();
    }

    /**
     * 判断起始IP是否大于结束IP<br/>
     * 不进行参数的非空校验
     *
     * @param startIP
     * @param endIP
     * @return
     */
    public static boolean validateRangeIp(Long startIP, Long endIP) {
        return startIP.longValue() <= endIP.longValue();
    }
    
    /**
     * IPV6判断起始IP是否大于结束IP<br/>
     * 不进行参数的非空校验
     *
     * @param startIP
     * @param endIP
     * @return
     */
    public static boolean validateIPV6RangeIp(BigInteger startIP, BigInteger endIP) {
        return startIP.subtract(endIP).signum()==-1;
    }

    
    /**
     * IPV4是否超出IP段范围
     * @param startIP
     * @param endIP
     * @return 超出返回true，不超出返回false
     */
    public static boolean ifExceedIPRange(Long startIP, Long endIP) {
        return endIP.longValue() - startIP.longValue() >= 256 * 256;
    }
    
    /**
     * IPV6是否超出IP段范围
     * @param startIP
     * @param endIP
     * @return 超出返回true，不超出返回false
     */
    public static boolean ifIPV6ExceedIPRange(BigInteger startIP, BigInteger endIP) {
    	long rangIp = 256*256;
        return BigInteger.valueOf(rangIp).subtract(endIP.subtract(startIP)).signum()==-1;
    }

    /**
     * 校验数据长度
     * @param propertyName
     * @param length
     * @return
     */
    public static ValidateResult validateMaxLength(String propertyName,String value,int length){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateEmpty(propertyName,value);
        }
        if (getValueCharLength(value)>length){
            return ValidateResult.getErrorResult("["+propertyName+"超过长度，实际长度"+getValueCharLength(value)+"，最大长度"+length+"]");
        }
        return ValidateResult.getSuccessResult();
    }

    /**
     * 校验数据长度 不进行非空校验
     * @param propertyName
     * @param length
     * @return
     */
    public static ValidateResult validateMaxLengthWithoutEmptyCheck(String propertyName,String value,int length){
        if (getValueCharLength(value)>length){
            return ValidateResult.getErrorResult("["+propertyName+"超过长度，实际长度"+getValueCharLength(value)+"，最大长度"+length+"]");
        }
        return ValidateResult.getSuccessResult();
    }

    /**
     * 校验数据长度 固定长度
     * @param propertyName
     * @param length
     * @return
     */
    public static ValidateResult validateFixedLength(String propertyName,String value,int length){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateEmpty(propertyName,value);
        }
        if (getValueCharLength(value)!=length){
            return ValidateResult.getErrorResult("["+propertyName+"长度不符合要求，实际长度"+value.length()+"，固定长度"+length+"]");
        }
        return ValidateResult.getSuccessResult();
    }


    /**
     * 根据指定时间格式校验字符串
     * @param propertyName
     * @param value
     * @param format
     * @return
     */
	public static ValidateResult validateDateWithDefinedFormat(String propertyName, String value, String format) {
		ValidateResult validateResult = null;
		if (BasicValidateUtils.validateEmpty(propertyName, value).getCode() != ResultCodeEnum.SUCCESS.getCode()) {
			return BasicValidateUtils.validateEmpty(propertyName, value);
		}
		if (StringUtils.isEmpty(format)) {
			format = "yyyy-MM-dd";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringUtils.trimWhitespace(format));
		try {
			Date date = simpleDateFormat.parse(value);
			Date now = new Date();
			if (simpleDateFormat.format(date).equals(value)) {
				if (date.after(now)) {
					validateResult = ValidateResult.getErrorResult("[" + propertyName + "不合法，不能晚于当前日期]");
				} else {
					validateResult = ValidateResult.getSuccessResult();
				}
			} else {
				validateResult = ValidateResult.getErrorResult("[" + propertyName + "不合法，日期格式为yyyy-MM-dd]");
			}
		} catch (ParseException e) {
			validateResult = ValidateResult.getErrorResult("[" + propertyName + "不合法，日期格式为yyyy-MM-dd]");
		}
		return validateResult;
	}

    /**
     * 校验个人证件号码(满足其一即可)
     * @param propertyName
     * @param value
     * @return
     */
    public static ValidateResult validateCardNum(String propertyName,String value){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateEmpty(propertyName,value);
        }
        String regex1 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.IDCARD_REGEXP.getSubType());
        String regex2 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.PASSPORT_REGEXP.getSubType());
        String regex3 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.MTPS_REGEXP.getSubType());
        String regex4 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ARMY_REGEXP.getSubType());

        boolean legalIdCard = (regexValidate(value,regex1) && validateIllegalForIdCard(value) );
        if ( legalIdCard ||
                regexValidate(value,regex2)||
                regexValidate(value,regex3)||
                regexValidate(value,regex4) ){
            return ValidateResult.getSuccessResult();
        }
        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }

    /**
     * 校验单位证件号码(满足其一即可)
     * @param propertyName
     * @param value
     * @return
     */
    public static ValidateResult validateUnitCardNum(String propertyName,String value){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateEmpty(propertyName,value);
        }

        String regex1 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.IDCARD_REGEXP.getSubType());
        String regex2 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ORGANIZATION_REGEXP.getSubType());
        String regex3 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.CORPORATION_REGEXP.getSubType());
        String regex4 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ARMY_CODE_REGEXP.getSubType());
        String regex5 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.SOCIETY.getSubType());
        String regex6 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.MTPS_REGEXP.getSubType());
        String regex7 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ARMY_REGEXP.getSubType());
        String regex8 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.PASSPORT_REGEXP.getSubType());
        // 工商营业执照
        String regex9 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.BUSINESS_LICENSE_REGEXP.getSubType());

        boolean legalIdCard = (regexValidate(value,regex1) && validateIllegalForIdCard(value) );
        boolean legalOrgCard = ( regexValidate(value,regex2) && organizationCodeNumberCheck(value) );
        boolean legalCorpCard = ( regexValidate(value,regex3) && enterpriseCertificateNumberCheck(value) );

        try {
            if ( legalIdCard ||
                    legalOrgCard||
                    legalCorpCard||
                    regexValidate(value,regex4)||
                    regexValidate(value,regex5)||
                    regexValidate(value,regex6)||
                    regexValidate(value,regex7)||
                    regexValidate(value,regex8)||
                    regexValidate(value,regex9)){
                return ValidateResult.getSuccessResult();
            }
        }catch (Exception e){
            return ValidateResult.getSuccessResult();
        }

        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }

    /**
     * 根据证件类型校验证件号码 需要和证件类型匹配
     * @param idType
     * @param idNumber
     * @return
     */
    public static boolean idNumMatchType(int idType,String idNumber){
        if (idType==1){
            String regex1 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.BUSINESS_LICENSE_REGEXP.getSubType());
            if (regexValidate(idNumber,regex1)){
                return true;
            }
        } else if (idType==2){
            String regex2 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.IDCARD_REGEXP.getSubType());
            if (regexValidate(idNumber,regex2)){
                return true;
            }
        } else if (idType==3){
            String regex3 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ORGANIZATION_REGEXP.getSubType());
            if (regexValidate(idNumber,regex3)){
                return true;
            }
        } else if (idType==4){
            String regex4 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.CORPORATION_REGEXP.getSubType());
            if (regexValidate(idNumber,regex4)){
                return true;
            }
        } else if (idType==5){
            String regex5 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ARMY_REGEXP.getSubType());
            if (regexValidate(idNumber,regex5)){
                return true;
            }
        } else if (idType==6){
            String regex6 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.SOCIETY.getSubType());
            if (regexValidate(idNumber,regex6)){
                return true;
            }
        } else if (idType==7){
            String regex7 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.PASSPORT_REGEXP.getSubType());
            if (regexValidate(idNumber,regex7)){
                return true;
            }
        } else if (idType==8){
            String regex8 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ARMY_REGEXP.getSubType());
            if (regexValidate(idNumber,regex8)){
                return true;
            }
        } else if (idType==9){
            String regex9 = RegexUtils.getRegexValueBySubType(CommonRegexEnum.MTPS_REGEXP.getSubType());
            if (regexValidate(idNumber,regex9)){
                return true;
            }
        } else if (idType==999){
            return true;
        }
        return false;
    }

    /**
     * 校验固定电话号码
     * @param propertyName
     * @param value
     * @return
     */
    public static ValidateResult validateTelephoneNum(String propertyName,String value){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateEmpty(propertyName,value);
        }
        String regex = RegexUtils.getRegexValueBySubType(CommonRegexEnum.TELEPHONE_REGEXP.getSubType());
        if (regexValidate(value,regex)){
            return ValidateResult.getSuccessResult();
        }
        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }

    /**
     * 校验手机号
     * @param propertyName
     * @param value
     * @return
     */
    public static ValidateResult validateMobileNum(String propertyName,String value){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateEmpty(propertyName,value);
        }
        String regex = RegexUtils.getRegexValueBySubType(CommonRegexEnum.PHONE_REGEXP.getSubType());
        if (regexValidate(value,regex)){
            return ValidateResult.getSuccessResult();
        }
        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }

    /**
     * 校验email
     * @param propertyName
     * @param value
     * @return
     */
    public static ValidateResult validateEmailNum(String propertyName,String value){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateEmpty(propertyName,value);
        }
        String regex = RegexUtils.getRegexValueBySubType(CommonRegexEnum.EMAIL_REGEXP.getSubType());
        if (regexValidate(value,regex)){
            return ValidateResult.getSuccessResult();
        }
        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }

    /**
     * 校验邮编
     * @param propertyName
     * @param value
     * @return
     */
    public static ValidateResult validateZipCode(String propertyName,String value,int length){
        if (BasicValidateUtils.validateEmpty(propertyName,value).getCode()!= ResultCodeEnum.SUCCESS.getCode()){
            return BasicValidateUtils.validateFixedLength(propertyName,value,length);
        }
        String regex = RegexUtils.getRegexValueBySubType(CommonRegexEnum.POST_REGEXP.getSubType());
        if (regexValidate(value,regex)){
            return ValidateResult.getSuccessResult();
        }
        return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }


    /**
     * 正则匹配
     * @param value
     * @param regex
     * @return
     */
    private static boolean regexValidate(String value,String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(value).matches();
    }

    /**
     * 获取值的字符长度，中文为两个字符长度。
     *
     * @param value 需要计算的字符串
     * @return
     */
    public static int getValueCharLength(String value) {
        if (value == null) {
            return 0;
        }
        char[] chars = value.toCharArray();
        int length = 0;
        for (int index = 0; index < chars.length; index++) {
            if (isUnicode(chars[index])) {
                length += 3;
            } else {
                length++;
            }
        }
        return length;
    }

    /**
     * 是否unicode，判断是否为汉字
     *
     * @param c 需要校验的字符
     * @return
     */
    private static boolean isUnicode(char c) {
//        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
//            return true;
//        }
        if((c >= 0x4e00)&&(c <= 0x9fbb)) {
            return true;
        }
        return false;
    }
    
    /**
     * 校验域名是否合法
     * 
     */
    public static ValidateResult validateDomainName(String propertyName,String domainName){
      if (Tools.isDomain(domainName)){
          return ValidateResult.getSuccessResult();
      }
      return ValidateResult.getErrorResult("["+propertyName+"不合法]");
    }


    /**
     * 校验身份证号码合法性
     * @return
     */
    private static boolean validateIllegalForIdCard(String data){
        if (StringUtils.isEmpty(data) || (data.length() != 15 && data.length() != 18)) {
            return false;
        }
        String Ai = null;
        // 除校验位其它都为数字
        if (data.length() == 18) {
            Ai = data.substring(0, 17);
        } else if (data.length() == 15) {
            Ai = data.substring(0, 6) + "19" + data.substring(6, 15);
        }
        if (isNumericID(Ai) == false) {
            return false;
        }
        // 出生年月是否有效
        String strYear = Ai.substring(6, 10);// 年
        String strMonth = Ai.substring(10, 12);// 月
        String strDay = Ai.substring(12, 14);// 日
        if (isDateID(strYear + "-" + strMonth + "-" + strDay) == false) {
            return false;
        }
        // 地区码是否有效
        if (!areaCodeMap.containsKey(Ai.substring(0, 2))) {
            return false;
        }
        if (data.length() == 15) { // 注：15位的身份证没有校验位，可以通过转化为18位的身份证，但是最后的校验位也是计算出来再校验的，所以15位的不必校验了
            return true;
        }
        // 第18位,校验位
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * idcardWi[i];
        }
        Ai = Ai + idcardValCodeArr[TotalmulAiWi % 11]; // M余数,加上校验位
        if (Ai.equals(data) == true) {// 加上第18位校验位后判断是否一致
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumericID(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDateID(String strDate) {
        String dateReg = RegexUtils.getRegexValueBySubType(CommonRegexEnum.DATE_REGEXP.getSubType());
        return Pattern.compile(dateReg).matcher(strDate).matches();
    }

    /**
     * 工商营业执照号码校验方法
     *
     * @param data 待校验的工商营业执照号码信息
     * @return 校验成功，返回true，否则，返回false
     */
    public static boolean industrialAndCommercialNumberCheck(String data) {
        if (StringUtil.isBlank(data) || data.length() != 15) {
            return false;
        }
        char[] chars = data.substring(0, 14).toCharArray(); // 获取营业执照注册号前14位数字用来计算校验码
        int[] ints = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            if (!Character.isDigit(data.charAt(i))) {
                return false;
            }
            ints[i] = Integer.parseInt(String.valueOf(chars[i]));
        }
        // 比较填写的营业执照注册号的校验码和计算的校验码是否一致
        return data.substring(14, 15).equals(CheckCodeIC(ints) + "");
    }

    /**
     * 计算工商营业执照注册号的校验码
     *
     * @param ints
     * @return
     */
    private static int CheckCodeIC(int[] ints) {
        int pj = 10;// pj=cj|11==0?10:cj|11
        int rj = 0;// （si||10==0？10：si||10）*2
        for (int i = 0; i < ints.length; i++) {
            pj = (rj % 11) == 0 ? 10 : (rj % 11);
            int si = pj + ints[i]; // pi|11+ti
            rj = (0 == si % 10 ? 10 : si % 10) * 2;
        }
        pj = (rj % 11) == 0 ? 10 : (rj % 11); // pj不可能为0 （当且仅当 pj==0 和
        // pj==1时：a1的值直接计算，分别为 1 和
        // 0；其他情况： a1 = 11-pj）
        return pj == 1 ? 0 : 11 - pj; // 返回a1，其中： mod10(p15 + a1) == 1
    }

    /**
     * 组织机构代码校验方法
     *
     * @param data 待校验的组织机构代码信息
     * @return 校验成功，返回true，否则，返回false
     */
    public static boolean organizationCodeNumberCheck(String data) {
        String orgReg = RegexUtils.getRegexValueBySubType(CommonRegexEnum.ORGANIZATION_REGEXP.getSubType());
        if (Pattern.compile(orgReg).matcher(data).matches() == false) {
            return false;
        }
        int a[] = new int[9];
        for (int i = 0; i < 10; i++) {
            if (i == 8)
                continue;
            else if (i < 8) {
                char t = data.charAt(i);
                if (Character.isDigit(t)) {
                    a[i] = t - 48;
                } else {
                    a[i] = t - 55;
                }
            } else {
                char t = data.charAt(i);
                if (t == '0')
                    a[i - 1] = 11;
                else if (t == 'X') {
                    a[i - 1] = 10;
                } else {
                    a[i - 1] = t - 48;
                }
            }
        }
        int total = 0;
        for (int i = 0; i < orgWi.length; i++) {
            total += a[i] * orgWi[i];
        }
        return 11 - total % 11 == a[8];
    }

    /**
     * 事业法人证书校验方法
     *
     * @param data 待校验的事业法人证书信息
     * @return 校验成功，返回true，否则，返回false
     */
    public static boolean enterpriseCertificateNumberCheck(String data) {
        char[] values = data.toCharArray();
        int parity = 0;
        for (int i = 0; i < enterpriseCertificateWi.length; i++) {
            parity += enterpriseCertificateWi[i] * (Integer) enterpriseCertificateMap.get(values[i]);
        }
        int check = 31 - parity % 31;
        int val = values[17] == '0' ? 31 : (Integer) enterpriseCertificateMap.get(values[17]);
        return check == val;
    }

    public static void main(String[] args) {
    	System.out.println(Tools.ip2long("192.168.1.105"));
    	
       /* System.out.println(BasicValidateUtils.validateCardNum("idca","42108304172137"));
        System.out.println(Tools.isIpAddress("ff06:0:0:0:0:0:0:c3"));
        System.out.println(BasicValidateUtils.validateDateWithDefinedFormat("name","2016-02-29",""));
        System.out.println(StringUtils.trimWhitespace(" ab bb "));
        System.out.println(Long.MAX_VALUE);
        System.out.println(regexValidate("123","[a-z|A-Z]"));
        System.out.println(BasicValidateUtils.validateEmpty("name",""));
        System.out.println(BasicValidateUtils.validateMaxLength("name","中文1",5));*/
//    	String reg = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
//                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|(400)-(\\d{3}-\\d{4}|\\d{4}-\\d{3})|(400)"+
//                "[0,1,6,7,8,9]-(\\d{3}-\\d{3}|\\d{2}-\\d{4}|\\d{4}-\\d{2})|^(400)\\d{7}";
//		regexValidate("123456789",reg);
        System.out.println(regexValidate("p1111111","^1[45][0-9]{7}|([P|p|S|s]\\d{7})|([S|s|G|g]\\d{8})|([Gg|Tt|Ss|Ll|Qq|Dd|Aa|Ff]\\d{8})|([H|h|M|m]\\d{8,10})$"));
        System.out.println(getValueCharLength("你是真的ni.。"));
        System.out.println(getValueCharLength("111111111111111111阿斯达打算打算 撒打算打的速度速度111111111111111111111111速度速度的打的撒打算打大的萨达是爱迪生打的萨达萨达撒旦撒"));
    }
}
