package com.aotain.serviceapi.server.util;

import com.aotain.serviceapi.server.constant.CheckFieldType;
import com.aotain.serviceapi.server.constant.CheckFieldTypeConstant;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 基础数据核验工具类 方法中涉及到字段长度的，都为field-length.properties中的键
 * 
 * @author Jason
 *
 */
public class BasicDataValidateUtils extends DataValidateUtils {

	/**
	 * 身份证校验码：{ "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" }
	 */
	public static String[] idcardValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
	/**
	 * 身份证检验位权重：{ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 }
	 */
	public static int[] idcardWi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	/**
	 * 组织机构代码校验正则：^[0-9A-Z]{8}-[0-9X]$
	 */
	public static String orgReg = "^[0-9A-Z]{8}-[0-9X]$";
	/**
	 * 组织机构校验位权重：{ 3, 7, 9, 10, 5, 8, 4, 2 }
	 */
	public static int[] orgWi = { 3, 7, 9, 10, 5, 8, 4, 2 };
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
	 * 事业法人证书正则：[\\dA-HJ-NP-RT-UW-Y]{18}
	 */
	public static String enterpriseCertificateReg = "[\\dA-HJ-NP-RT-UW-Y]{18}";
	/**
	 * 事业法人校验码和校验码对应值的map
	 */
	public static Map<Character, Integer> enterpriseCertificateMap = new HashMap<Character, Integer>();
	/**
	 * 事业法人证书校验位权重：{ 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30,
	 * 28 }
	 */
	public static int enterpriseCertificateWi[] = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };
	/**
	 * 区域码map
	 */
	public static Map<String, String> areaCodeMap = new HashMap<String, String>();
	/**
	 * 日期的正则：^((((19|20)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((19
	 * |20)\\d{2})-(0?[469]|11)-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-0?2-(0?[1
	 * -9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-
	 * (0?[1-9]|[12]\\d)))$
	 */
	public static String dateReg = "^((((19|20)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((19|20)\\d{2})-(0?[469]|11)-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-(0?[1-9]|[12]\\d)))$";
	/**
	 * 军队代号的正则：^[1-9][0-9]{3,4}$
	 */
	public static String armyReg = "^[1-9][0-9]{3,4}$";
	/**
	 * 护照的正则：^G|P|S|D|E[0-9]{8}$
	 */
	//public static String passsportReg = "^(G|P|S|D|E)[0-9]{8}$";
	public static String passsportReg = "^[\\dA-Za-z]{9}$";
	/**
	 * 军官证的正则：^军[0-9]{7}$
	 */
	public static String officerReg = "^军[0-9]{7}$";
	/**
	 * 台胞证的正则：^[0-9]{8}$
	 */
	public static String taiwReg = "^[0-9]{8}$";
	/**
	 * 工商营业执照号码：1
	 */
	public static final int TYPE_ID_1 = 1;
	/**
	 * 工商营业执照号码长度：15
	 */
	public static final int TYPE_ID_1_LENGTH = 15;
	/**
	 * 身份证：3
	 */
	public static final int TYPE_ID_2 = 2;
	/**
	 * 身份证长度：15
	 */
	public static final int TYPE_ID_2_LENGTH_15 = 15;
	/**
	 * 身份证长度：18
	 */
	public static final int TYPE_ID_2_LENGTH_18 = 18;
	/**
	 * 组织机构代码证书：3
	 */
	public static final int TYPE_ID_3 = 3;
	/**
	 * 组织机构代码证书长度：10
	 */
	public static final int TYPE_ID_3_LENGTH = 10;
	/**
	 * 事业法人证书：4
	 */
	public static final int TYPE_ID_4 = 4;
	/**
	 * 事业法人证书长度：18
	 */
	public static final int TYPE_ID_4_LENGTH = 18;
	/**
	 * 军队代号：5
	 */
	public static final int TYPE_ID_5 = 5;
	/**
	 * 军队代号长度：4
	 */
	public static final int TYPE_ID_5_LENGTH4 = 4;
	/**
	 * 军队代号长度：5
	 */
	public static final int TYPE_ID_5_LENGTH5 = 5;
	/**
	 * 社团法人证书：6
	 */
	public static final int TYPE_ID_6 = 6;
	/**
	 * 社团法人证书长度：18
	 */
	public static final int TYPE_ID_6_LENGTH = 18;
	/**
	 * 护照：7
	 */
	public static final int TYPE_ID_7 = 7;
	/**
	 * 护照长度：9
	 */
	public static final int TYPE_ID_7_LENGTH = 9;
	/**
	 * 军官证:8
	 */
	public static final int TYPE_ID_8 = 8;
	/**
	 * 军官证长度：9
	 */
	public static final int TYPE_ID_8_LENGTH = 9;
	/**
	 * 台胞证：9
	 */
	public static final int TYPE_ID_9 = 9;
	/**
	 * 台胞证长度：8
	 */
	public static final int TYPE_ID_9_LENGTH = 8;
	/**
	 * 其他：999
	 */
	public static final int TYPE_ID_999 = 999;
	/**
	 * 其他长度：32
	 */
	public static final int TYPE_ID_999_LENGTH = 32;
	/**
	 * 默认的类型分隔符：-
	 */
	public static final String DEFAULT_TYPE_SPLIT = "-";
	/**
	 * 域名的分隔符
	 */
	public static final String DOMAIN_SPLIT = ";"; 
	/**
	 * 单位名称的分隔符
	 */
	public static final String UNITNAME_SPLIT = ";"; 
	/**
	 * 省级错误
	 */
	public static final int PROVINCE_ERROR = 0;
	/**
	 * 市级错误
	 */
	public static final int CITY_ERROR = 1;
	/**
	 * 县级错误
	 */
	public static final int COUNTRY_ERROR = 2;
	/**
	 * 邮政编码表表名
	 */
	public static final String IDC_JCDM_YZBM = "IDC_JCDM_YZBM";
	/**
	 * 机房专线标识
	 */
	public static final String IDC_JCDM_ZXBS = "IDC_JCDM_ZXBS";
	/**
	 * 机房性质表名
	 */
	public static final String IDC_JCDM_JFXZ = "IDC_JCDM_JFXZ";
	/**
	 * 证件类型表
	 */
	public static final String IDC_JCDM_ZJLX = "IDC_JCDM_ZJLX";
	/**
	 * 服务内容类型表
	 */
	public static final String IDC_JCDM_FWNR = "IDC_JCDM_FWNR";
	/**
	 * 应用服务类型表
	 */
	public static final String IDC_JCDM_YYFWLX = "IDC_JCDM_YYFWLX";
	/**
	 * 接入方式表
	 */
	public static final String IDC_JCDM_JRFS = "IDC_JCDM_JRFS";
	/**
	 * 虚拟主机状态表
	 */
	public static final String IDC_JCDM_XNZJZT = "IDC_JCDM_XNZJZT";
	/**
	 * 虚拟主机类型表
	 */
	public static final String IDC_JCDM_XNZJLX = "IDC_JCDM_XNZJLX";
	/**
	 * 业务类型表
	 */
	public static final String IDC_JCDM_YWLX = "IDC_JCDM_YWLX";
	/**
	 * 用户属性表
	 */
	public static final String IDC_JCDM_YHSX = "IDC_JCDM_YHSX";
	/**
	 * 用户标识
	 */
	public static final String IDC_JCDM_YHBS = "IDC_JCDM_YHBS";
	/**
	 * 单位属性表
	 */
	public static final String IDC_JCDM_DWSX = "IDC_JCDM_DWSX";
	/**
	 * 分配状态表
	 */
	public static final String IDC_JCDM_FPZT = "IDC_JCDM_FPZT";
	/**
	 * 占用类型表
	 */
	public static final String IDC_JCDM_ZYLX = "IDC_JCDM_ZYLX";
	/**
	 * 使用类型表
	 */
	public static final String IDC_JCDM_SYLX = "IDC_JCDM_SYLX";
	/**
	 * 链路类型
	 */
	public static final String IDC_JCDM_LLLX = "IDC_JCDM_LLLX";
	/**
	 * IP地址使用方式
	 */
	public static final String IDC_JCDM_IPDZSYFS = "IDC_JCDM_IPDZSYFS";
	/**
	 * 网站备案类型
	 */
	public static final String IDC_JCDM_WZBALX = "IDC_JCDM_WZBALX";
	/**
	 * 地址省份
	 */
	public static final String IDC_JCDM_DZSF = "IDC_JCDM_DZSF";
	/**
	 * 行政区域代码
	 */
	public static final String IDC_JCDM_XZQYDM = "IDC_JCDM_XZQYDM";
	/**
	 * 规则管理的规则来源
	 */
	public static final String IDC_JCDM_GZLY = "IDC_JCDM_GZLY";
	
	/**
	 * 关键词等级表
	 */
	public static final String IDC_JCDM_GJCDJ = "IDC_JCDM_GJCDJ";
	/**
	 * 规则管理的与或关系
	 */
	public static final String IDC_JCDM_YHGX = "IDC_JCDM_YHGX";
	/**
	 * 规则管理的匹配范围
	 */
	public static final String IDC_JCDM_PPFW = "IDC_JCDM_PPFW";
	/**
	 * 规则管理的协议类型
	 */
	public static final String IDC_JCDM_XYLX = "IDC_JCDM_XYLX";
	/**
	 * 汇聚型机房
	 */
	public static final String IDC_JCDM_HJXJF = "IDC_JCDM_HJXJF";
	/**
	 * 行政区域代码1级：省级
	 */
	public static final String CODE_LEVEL_PROVINCE = "1";
	/**
	 * 行政区域代码2级：市级
	 */
	public static final String CODE_LEVEL_CITY = "2";
	/**
	 * 行政区域代码3级：县级
	 */
	public static final String CODE_LEVEL_COUNTRY = "3";
	/**
	 * 行政区域代码4级：街道
	 */
	public static final String CODE_LEVEL_STREET = "4";
	/**
	 * 如果是广东分公司，行政区域代码需要校验合法性<br/>
	 * 广东行政区域代码：440000
	 */
	public static final String GDXZQYDM_CODE = "440000";

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
	 * 校验null或者空串或者空格
	 * 
	 * @param data
	 * @return
	 */
	public static boolean rejectIfEmptyOrWhitespace(String data) {
		return StringUtil.isEmptyString(data);
	}

	/**
	 * 校验是否为合法的类型：只有一个"-"分割，且前后有值
	 * 
	 * @return 如何校验不通过，返回new Object[] {false}; 如果校验通过，返回new Object[]{true, 类型id,
	 *         类型名称};
	 */
	public static Object[] rejectIfLegalTypeSplit(String value) {
		if (StringUtil.isBlank(value)) {
			return new Object[] { false };
		}
		if (!value.contains("-")) {
			return new Object[] { false };
		}
		String[] arr = value.split("-");
		if (arr.length != 2) {
			return new Object[] { false };
		}
		return new Object[] { true, arr[0], arr[1] };
	}

	/**
	 * 校验字段长度
	 * 
	 * @param value 需要校验的值
	 * @param field 数据库字段
	 * @return 如果value的长度大于数据库的字段长度，返回Object[] {false, 字段实际长度, 字段的最长长度}
	 *         如果value的长度小于或等于数据库的字段长度，则返回Object[] {true}
	 */
	public static Object[] rejectMoreThanMaxLength(String value, String field) {
		int valueLength = getValueCharLength(value);
		int maxLength = FieldInfoProps.instance.getLength(field);
		if (valueLength > maxLength) {
			return new Object[] { false, valueLength, maxLength };
		}
		return new Object[] { true };
	}

	/**
	 * 字符串是否超过字段的最大长度
	 * 
	 * @param value 字段值
	 * @param field 数据库的字段名
	 * @return
	 */
	public static boolean rejectIfMoreThanMaxLength(String value, String field) {
		return rejectIfMoreThanMaxLength(value, FieldInfoProps.instance.getLength(field));
	}

	/**
	 * 字符串是否超过字段的最大长度
	 * 
	 * @param value 字段值
	 * @param maxLength 数据库的字段长度
	 * @return
	 */
	public static boolean rejectIfMoreThanMaxLength(String value, int maxLength) {
		if (value == null) {
			return false;
		}
		return getValueCharLength(value) > maxLength;
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
				length += 2;
			} else {
				length++;
			}
		}
		return length;
	}

	/**
	 * 字符串是否含有中文汉字
	 * 
	 * @param value
	 * @return
	 */
	public static boolean rejectIfHasChinese(String value) {
		if (value == null || value.trim().equals("")) {
			return false;
		}
		char[] chars = value.toCharArray();
		for (int index = 0; index < chars.length; index++) {
			if (isUnicode(chars[index])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否unicode，判断是否为汉字
	 * 
	 * @param c 需要校验的字符
	 * @return
	 */
	private static boolean isUnicode(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public static boolean rejectIfLengthNotEnoughOrBeyond(CheckFieldType type, String data) {
		switch (type.getValue()) {
		case CheckFieldTypeConstant.ZJLX:
			return CheckFieldTypeConstant.ZJLX_MAP.containsKey(data);
		case CheckFieldTypeConstant.RYZJLX:
			return CheckFieldTypeConstant.RYZJLX_MAP.containsKey(data);
		default:
			break;
		}
		return false;
	}

	/**
	 * 校验是否URL
	 * 
	 * @param url
	 * @return
	 */
	public static boolean rejectIfURL(String url) {
		return Tools.isURL(url);
	}

	/**
	 * 校验是否domain
	 * 
	 * @param valueStart
	 * @return
	 */
	public static boolean rejectIfDomain(String domain) {
		return Tools.isDomain(domain);
	}

	/**
	 * 校验证件长度
	 * 
	 * @param value 证件号码
	 * @param typeId 证件类型
	 * @return
	 */
	public static boolean validateTypeIdLength(String value, int typeId) {
		if (typeId == TYPE_ID_1) { // 15位
			return getValueCharLength(value) == TYPE_ID_1_LENGTH;
		}
		if (typeId == TYPE_ID_2) { // 15位或者18位
			return getValueCharLength(value) == TYPE_ID_2_LENGTH_15 || getValueCharLength(value) == TYPE_ID_2_LENGTH_18;
		}
		if (typeId == TYPE_ID_3) { // 10位
			return getValueCharLength(value) == TYPE_ID_3_LENGTH;
		}
		if (typeId == TYPE_ID_4) { // 18位
			return getValueCharLength(value) == TYPE_ID_4_LENGTH;
		}
		if (typeId == TYPE_ID_5) { // 4位或者5位
			return getValueCharLength(value) == TYPE_ID_5_LENGTH4 || getValueCharLength(value) == TYPE_ID_5_LENGTH5;
		}
		if (typeId == TYPE_ID_6) { // 同TYPE_ID_4（事业法人证书） 18位
			return getValueCharLength(value) == TYPE_ID_6_LENGTH;
		}
		if (typeId == TYPE_ID_7) { // 9位
			return getValueCharLength(value) == TYPE_ID_7_LENGTH;
		}
		if (typeId == TYPE_ID_8) { // 9位
			return getValueCharLength(value) == TYPE_ID_8_LENGTH;
		}
		if (typeId == TYPE_ID_9) { // 8位
			return getValueCharLength(value) == TYPE_ID_9_LENGTH;
		}
		return getValueCharLength(value) <= TYPE_ID_999_LENGTH;
	}

	/**
	 * 校验证件的合法性
	 * 
	 * @param value 证件号码
	 * @param typeId 证件类型
	 * @return
	 */
	public static boolean validateTypeIdLegal(String value, int typeId) {
		if (typeId == TYPE_ID_1) { // 15位
			return industrialAndCommercialNumberCheck(value);
		}
		if (typeId == TYPE_ID_2) { // 15位或者18位
			return idCardNumberCheck(value);
		}
		if (typeId == TYPE_ID_3) { // 10位
			return organizationCodeNumberCheck(value);
		}
		if (typeId == TYPE_ID_4) { // 18位
			return getValueCharLength(value) == TYPE_ID_4_LENGTH;
			//return enterpriseCertificateNumberCheck(value);
		}
		if (typeId == TYPE_ID_5) { // 15位
			return armyIDCheck(value);
		}
		if (typeId == TYPE_ID_6) { // 15位或者18位
			return enterpriseCertificateNumberCheck(value); // 方法同：事业法人证书
		}
		if (typeId == TYPE_ID_7) { // 10位
			return passportIDCheck(value);
		}
		if (typeId == TYPE_ID_8) { // 18位
			return officerIDCheck(value);
		}
		if (typeId == TYPE_ID_9) { // 18位
			return taiIDCheck(value);
		}
		return true;
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
	 * 身份证号码校验方法
	 * 
	 * @param data 待核验的身份证号码信息
	 * @return 校验成功，返回true，否则，返回false
	 */
	public static boolean idCardNumberCheck(String data) {
		if (StringUtil.isBlank(data) || (data.length() != 15 && data.length() != 18)) {
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
	public static boolean isNumericID(String str) {
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
		return Pattern.compile(dateReg).matcher(strDate).matches();
	}

	/**
	 * 组织机构代码校验方法
	 * 
	 * @param data 待校验的组织机构代码信息
	 * @return 校验成功，返回true，否则，返回false
	 */
	public static boolean organizationCodeNumberCheck(String data) {
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
		if (Pattern.compile(enterpriseCertificateReg).matcher(data).matches() == false) {
			return false;
		}
		char[] values = data.toCharArray();
		int parity = 0;
		for (int i = 0; i < enterpriseCertificateWi.length; i++) {
			parity += enterpriseCertificateWi[i] * (Integer) enterpriseCertificateMap.get(values[i]);
		}
		int check = 31 - parity % 31;
		int val = values[17] == '0' ? 31 : (Integer) enterpriseCertificateMap.get(values[17]);
		return check == val;
	}

	/**
	 * 军队代号的校验
	 * 
	 * @param strArmy
	 * @return
	 */
	public static boolean armyIDCheck(String strArmy) {
		return Pattern.compile(armyReg).matcher(strArmy).matches();
	}

	/**
	 * 护照的校验
	 * 
	 * @param strPassport
	 * @return
	 */
	public static boolean passportIDCheck(String strPassport) {
		return Pattern.compile(passsportReg).matcher(strPassport).matches();
	}

	/**
	 * 军官证的校验
	 * 
	 * @param strOfficer
	 * @return
	 */
	public static boolean officerIDCheck(String strOfficer) {
		return Pattern.compile(officerReg).matcher(strOfficer).matches();
	}

	/**
	 * 台胞证的校验
	 * 
	 * @param strTaiw
	 * @return
	 */
	public static boolean taiIDCheck(String strTaiw) {
		return Pattern.compile(taiwReg).matcher(strTaiw).matches();
	}

	/**
	 * 验证地址
	 * 
	 * @param address
	 * @return
	 */
	public static Object[] validateAddress(String address) {
		// XX省（自治区）XX市（省直辖行政单位|自治州|地区|盟）XX区（市|县|旗）
		if (address.startsWith("香港特别行政区")) { 
			String others = address.substring(address.indexOf("香港特别行政区") + "香港特别行政区".length());
			return new Object[] { true, "香港特别行政区", "香港特别行政区", "", "", others }; 
		} else if (address.startsWith("澳门特别行政区")) { 
			String others = address.substring(address.indexOf("澳门特别行政区") + "澳门特别行政区".length());
			return new Object[] { true, "澳门特别行政区", "澳门特别行政区", "", "", others }; 
		} else if (address.startsWith("北京市") || address.startsWith("天津市") || address.startsWith("上海市") || address.startsWith("重庆市")) {
			String province = address.substring(0, address.indexOf("市") + "市".length());
			String others = address.substring(address.indexOf("市") + "市".length());
			if (!others.contains("区")) {
				return new Object[] { false };
			} else {
				String area = others.substring(0, others.indexOf("区") + "区".length());
				String mc = province + area;
				others = others.substring(others.indexOf("区") + "区".length());
				return new Object[] { true, mc, province, "", area, others }; // 直辖市，没有city
			}
		} else {
			if (!address.contains("省") && !address.contains("自治区")) {
				return new Object[] { false };
			}
			String province = null;
			String others = null;
			if(address.contains("自治区")){
					province = address.substring(0, address.indexOf("自治区") + "自治区".length());
					others = address.substring(address.indexOf("自治区") + "自治区".length());
			}else  {
				province = address.substring(0, address.indexOf("省") + "省".length());
				others = address.substring(address.indexOf("省") + "省".length());
			}
			if (!others.contains("市") && !others.contains("省直辖行政单位") && !others.contains("自治州") && !others.contains("地区") && !others.contains("盟")) {
				return new Object[] { false };
			}
			String city = null;
			if (others.contains("自治州")) {
				city = others.substring(0, others.indexOf("自治州") + "自治州".length());
				others = others.substring(others.indexOf("自治州") + "自治州".length());
			}else if (others.contains("地区")) {
				city = others.substring(0, others.indexOf("地区") + "地区".length());
				others = others.substring(others.indexOf("地区") + "地区".length());
			} else if (others.contains("市")) {
				city = others.substring(0, others.indexOf("市") + "市".length());
				others = others.substring(others.indexOf("市") + "市".length());
			} else if (others.contains("省直辖行政单位")) {
				city = others.substring(0, others.indexOf("省直辖行政单位") + "省直辖行政单位".length());
				others = others.substring(others.indexOf("省直辖行政单位") + "省直辖行政单位".length());
			}  else if (others.contains("盟")) {
				city = others.substring(0, others.indexOf("盟") + "盟".length());
				others = others.substring(others.indexOf("盟") + "盟".length());
			}
			if (province.equals("甘肃省") && city.equals("嘉峪关市") || province.equals("广东省") && city.equals("东莞市") || province.equals("广东省") && city.equals("中山市") || province.equals("海南省")
					&& city.equals("三沙市") || province.equals("海南省") && city.equals("儋州市")) { // 五个不设区的地级市
				return new Object[] { true, province + city, province, city, "", others };
			}
			
			if (!others.contains("区") && !others.contains("市") && !others.contains("县") && !others.contains("旗")) {
				/*if(!"新疆维吾尔自治区".equals(province)){
					return new Object[] { false };
				}*/
			}
			String area = null;
			 if (others.contains("县")) {
				area = others.substring(0, others.indexOf("县") + "县".length());
				others = others.substring(others.indexOf("县") + "县".length());
			}else if (others.contains("市")) {
				area = others.substring(0, others.indexOf("市") + "市".length());
				others = others.substring(others.indexOf("市") + "市".length());
			}else if (others.contains("区")) {
				area = others.substring(0, others.indexOf("区") + "区".length());
				others = others.substring(others.indexOf("区") + "区".length());
			} else if (others.contains("旗")) {
				area = others.substring(0, others.indexOf("旗") + "旗".length());
				others = others.substring(others.indexOf("旗") + "旗".length());
			}  
			String mc = province + city + area;
			return new Object[] { true, mc, province, city, area, others }; // 省市县都不为空
		}
	}

	/**
	 * 是否比当前时间晚（后）
	 * 
	 * @param time
	 * @return
	 */
	public static boolean laterThanCurrDate(String time) {
		Calendar calender = Calendar.getInstance();
		Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+).*");
		Matcher matcher = pattern.matcher(time);
		if (matcher.matches()) {
			String year = matcher.group(1);
			String month = matcher.group(2);
			String day = matcher.group(3);
			if (StringUtils.isBlank(month) == false && month.trim().length() == 1) {
				month = "0" + month;
			}
			if (StringUtils.isBlank(day) == false && day.trim().length() == 1) {
				day = "0" + day;
			}
			time = year + "-" + month + "-" + day;
		}
		calender.setTime(new Date());
		String currDateStr = calender.get(Calendar.YEAR) + "-" + (calender.get(Calendar.MONTH) + 1 < 10 ? "0" + (calender.get(Calendar.MONTH) + 1) : calender.get(Calendar.MONTH) + 1) + "-"
				+ (calender.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calender.get(Calendar.DAY_OF_MONTH) : calender.get(Calendar.DAY_OF_MONTH));
		return time.compareTo(currDateStr) > 0;
	}

	/**
	 * 判断起始IP是否大于结束IP<br/>
	 * 不进行参数的非空校验
	 * 
	 * @param startIP
	 * @param endIP
	 * @return
	 */
	public static boolean ifStartIPgtEndIP(Long startIP, Long endIP) {
		return startIP.longValue() > endIP.longValue();
	}

	/**
	 * 是否超出IP段范围
	 * @param startIP
	 * @param endIP
	 * @return 超出返回true，不超出返回false
	 */
	public static boolean ifExceedIPRange(Long startIP, Long endIP) {
		return endIP.longValue() - startIP.longValue() >= 256 * 256;
	} 
	
	/**
	 * 通过ip获取前缀<br>
	 * ex:192.168.21.162 -> 192.168
	 * @param ip 不进行ip的合法性校验，即认为ip是合法的ip
	 * @return
	 */
	public static String getPrefIP(String ip) { 
		String[] ips = ip.split("\\."); 
		return ips[0] + "." + ips[1]; 
	}

	/**
	 * 获取类型的id或者值
	 * 
	 * @param value
	 * @return
	 */
	public static String getTypeOfIdOrValue(String value) {
		if (value.startsWith(BasicDataValidateUtils.DEFAULT_TYPE_SPLIT)) {
			return null;
		}
		String temp = null;
		if (value.contains(BasicDataValidateUtils.DEFAULT_TYPE_SPLIT)) {
			String[] arr = value.split(BasicDataValidateUtils.DEFAULT_TYPE_SPLIT);
			if (arr.length != 2) { // 多于两个分隔符不识别
				return null;
			}
			temp = arr[0];
		} else {
			temp = value;
		}
		return temp;
	}

	/**
	 * 校验省市县三级的行政区域代码
	 * 
	 * @param houseProvince 省级行政区域代码
	 * @param houseCity 地市行政区域代码
	 * @param houseCountry 县级行政区域代码
	 * @return Object[] 如果校验成功，返回Object[] {true} 如果校验失败，返回Object[] {false, 错误类型}
	 */
	public static Object[] validateProviceCityCountryZipCode(String houseProvince, String houseCity, String houseCountry) {
		Map<String, String> houseProvinceMap = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, houseProvince);
		Map<String, String> houseCityMap = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, houseProvince + houseCity);
		Map<String, String> houseCountryMap = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, houseProvince + houseCity + houseCountry);
		if (houseProvinceMap == null || !houseProvinceMap.get("CODELEVEL").equals("1")) {
			return new Object[] { false, PROVINCE_ERROR };
		}
		if (houseCityMap == null || !houseCityMap.get("PARENTCODE").equals(houseProvince) || !houseCityMap.get("CODELEVEL").equals("2")) {
			return new Object[] { false, CITY_ERROR };
		}
		if (houseCountryMap == null || !houseCountryMap.get("PARENTCODE").equals(houseCity) || !houseCountryMap.get("CODELEVEL").equals("3")) {
			return new Object[] { false, COUNTRY_ERROR };
		}
		return new Object[] { true };
	}

	/**
	 * 根据省市县获取行政区域代码，逻辑要与validateAddress结合
	 * 
	 * @param provinceMC
	 * @param cityMC 市为空表示为直辖市
	 * @param countryMC 县(区)为空表示不设县(区)的地级市
	 * @return
	 */
	public static Object[] validateProvinceCityCountry(String provinceMC, String cityMC, String countryMC) {
		Map<String, String> houseProvinceMap = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, provinceMC);
		if (houseProvinceMap != null && houseProvinceMap.containsKey("MC") && houseProvinceMap.containsKey("CODE") 
				&& (houseProvinceMap.get("MC").equals("香港特别行政区") || houseProvinceMap.get("MC").equals("澳门特别行政区"))) { // 湾湾等回归再加吧
			return new Object[] { true, houseProvinceMap.get("CODE"), houseProvinceMap.get("CODE"), houseProvinceMap.get("CODE")}; 
		} 
		String newCityMC = StringUtils.isBlank(cityMC) ? "市辖区" : cityMC;
		Map<String, String> houseCityMap = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, provinceMC + newCityMC); 
		if (houseCityMap == null && StringUtils.isBlank(cityMC)) {
			newCityMC = StringUtils.isBlank(cityMC) ? "县" : cityMC;
			houseCityMap = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, provinceMC + newCityMC);
		} 
		if (houseProvinceMap == null || !houseProvinceMap.containsKey("CODELEVEL") || !houseProvinceMap.containsKey("CODE") 
				|| !houseProvinceMap.get("CODELEVEL").equals("1") || StringUtils.isBlank(houseProvinceMap.get("CODE"))) {
			return new Object[] { false };
		}
		if (houseCityMap == null || !houseProvinceMap.containsKey("CODE") || !houseProvinceMap.containsKey("CODELEVEL") || !houseProvinceMap.containsKey("PARENTCODE")
				|| StringUtils.isBlank(houseCityMap.get("CODE")) || !houseCityMap.get("CODELEVEL").equals("2") || !houseCityMap.get("PARENTCODE").equals(houseProvinceMap.get("CODE"))) {
			return new Object[] { false };
		}
		if (StringUtils.isEmpty(countryMC)) {
			return new Object[] { true, houseProvinceMap.get("CODE"), houseCityMap.get("CODE"), houseCityMap.get("CODE") };
		}
		Map<String, String> houseCountryMap = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, provinceMC + newCityMC + countryMC);
		if (houseCountryMap == null || !houseProvinceMap.containsKey("CODE") || !houseProvinceMap.containsKey("CODELEVEL") || !houseProvinceMap.containsKey("PARENTCODE") 
				|| StringUtils.isBlank(houseCountryMap.get("CODE")) || !houseCountryMap.get("CODELEVEL").equals("3") || !houseCountryMap.get("PARENTCODE").equals(houseCityMap.get("CODE"))) {
			/*//新疆维吾尔自治区 不作第三级校验
			if(!"新疆维吾尔自治区".equals(provinceMC)){
				return new Object[] { false };
			}*/
		}
		if (houseCityMap.get("MC").equals("直辖市")) { // 直辖市用省级地市码
			return new Object[] { true, houseProvinceMap.get("CODE"), houseProvinceMap.get("CODE"), houseCountryMap.get("CODE") };
		}
		return new Object[] { true, houseProvinceMap.get("CODE"), houseCityMap.get("CODE"), houseCountryMap.get("CODE") };
	}

	/**
	 * 校验邮编是否存在于邮政编码表IDC_JCDM_YZBM中
	 * 
	 * @param zipCode
	 * @return
	 */
	public static boolean validateZipCode(String zipCode) {
		Set<String> set = BMDataUtil.instance.getYzbmPostCodeSet(); // 邮政编码的POST_CODE集合
		if (set != null && set.size() > 0) {
			return set.contains(zipCode);
		}
		return false;
	}

	/**
	 * 由于数据库idc_jcdm_yzbm（邮政编码表）里没有省级和市级的邮编，只能通过地址来匹配模糊的邮编
	 * 
	 * @param address
	 * @param fuzzyZipCode
	 * @return
	 */
	public static boolean validateAddressAndFuzzyZipCode(String address, String fuzzyZipCode) {
		Set<String> set = BMDataUtil.instance.getYzbmMcMapForPostCodeSet(address);
		if (set != null && set.size() > 0) {
			for (String item : set) {
				if (fuzzyZipCode.endsWith("0000") && item.startsWith(fuzzyZipCode.substring(0, 2))) { // 如果fuzzyZipCode为省级的邮编，只要匹配邮编前两位
					return true;
				} else if (fuzzyZipCode.endsWith("00") && item.startsWith(fuzzyZipCode.substring(0, 4))) { // 如果fuzzyZipCode为市级的邮编，只要匹配邮编的前四位
					return true;
				} else {
					if (fuzzyZipCode.equals(item)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param addressMc
	 * @return
	 */
	public static String getZipCodeByAddress(String addressMc) { 
		Set<String> set = BMDataUtil.instance.getYzbmMcMapForPostCodeSet(addressMc);
		if (set == null || set.size() == 0) { 
			return null; 
		} 
		if (set.size() == 1) { 
			return set.iterator().next(); 
		} 
		for (String postCode : set) { 
			if (postCode.endsWith("0000")) { // 有省级邮编
				return postCode; 
			} 
		} 
		for (String postCode : set) { 
			if (postCode.endsWith("00")) { // 有市级邮编
				return postCode; 
			} 
		} 
		return set.iterator().next(); 
	} 
	
	/**
	 * 如果是广东分公司，校验隶属分公司的区域代码
	 * 
	 * @param subordinateunitAreacode
	 * @return
	 */
	public static Object[] validateGdXzqydm(String value) {
		return baseValidateBMMap("gdXzqydmMap", value, "CODE");
	}

	/**
	 * 校验机房专线类型是否存在于机房性质表IDC_JCDM_ZXBS中
	 * 
	 * @param zxbs
	 * @return
	 */
	public static Object[] validateZxbs(String value) {
		return baseValidateBMMap(IDC_JCDM_ZXBS, value, "ID");
	}
	
	/**
	 * 校验机房性质类型是否存在于机房性质表IDC_JCDM_JFXZ中
	 * 
	 * @param jfxz
	 * @return
	 */
	public static Object[] validateJfxz(String value) {
		return baseValidateBMMap(IDC_JCDM_JFXZ, value, "ID");
	}

	/**
	 * 校验证件类型是否存在于证件类型表IDC_JCDM_ZJLX中
	 * 
	 * @param zjlx
	 * @return
	 */
	public static Object[] validateZjlx(String value) {
		return baseValidateBMMap(IDC_JCDM_ZJLX, value, "ID");
	}

	/**
	 * 校验服务内容类型是否存在于服务内容类型表IDC_JCDM_FWNR中
	 * 
	 * @param fwnr
	 * @return
	 */
	public static Object[] validateFwnr(String value) {
		return baseValidateBMMap(IDC_JCDM_FWNR, value, "ID");
	}

	/**
	 * 校验应用服务类型是否存在于应用服务类型表IDC_JCDM_YYFWLX中
	 * 
	 * @param yyfflx
	 * @return
	 */
	public static Object[] validateYyfwlx(String value) {
		return baseValidateBMMap(IDC_JCDM_YYFWLX, value, "ID");
	}

	/**
	 * 校验接入方式是否存在于接入方式表IDC_JCDM_JRFS中
	 * 
	 * @param jrfs
	 * @return
	 */
	public static Object[] validateJrfs(String value) {
		return baseValidateBMMap(IDC_JCDM_JRFS, value, "ID");
	}

	/**
	 * 校验虚拟主机状态是否存在于虚拟主机状态表IDC_JCDM_XNZJZT中
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateXnzjzt(String value) {
		return baseValidateBMMap(IDC_JCDM_XNZJZT, value, "ID");
	}

	/**
	 * 校验虚拟主机类型是否存在于虚拟主机类型表IDC_JCDM_XNZJLX中
	 * 
	 * @param xnzjlx
	 * @return
	 */
	public static Object[] validateXnzjlx(String value) {
		return baseValidateBMMap(IDC_JCDM_XNZJLX, value, "ID");
	}

	/**
	 * 校验业务类型是否存在于业务类型表IDC_JCDM_YWLX中
	 * 
	 * @param ywlx
	 * @return
	 */
	public static Object[] validateYwlx(String value) {
		return baseValidateBMMap(IDC_JCDM_YWLX, value, "ID");
	}

	/**
	 * 校验用户属性
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateYhsx(String value) {
		return baseValidateBMMap(IDC_JCDM_YHSX, value, "ID");
	}
	
	/**
	 * 校验用户标识
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateYhbs(String value) {
		return baseValidateBMMap(IDC_JCDM_YHBS, value, "ID");
	}

	/**
	 * 校验单位属性是否存在于单位属性表IDC_JCDM_DWSX中
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateDwsx(String value) {
		return baseValidateBMMap(IDC_JCDM_DWSX, value, "ID");
	}

	/**
	 * 校验分配状态是否存在于分配状态表IDC_JCDM_FPZT中
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateFpzt(String value) {
		return baseValidateBMMap(IDC_JCDM_FPZT, value, "ID");
	}

	/**
	 * 校验占用类型是否存在于占用类型表IDC_JCDM_ZYLX中
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateZylx(String value) {
		return baseValidateBMMap(IDC_JCDM_ZYLX, value, "ID");
	}

	/**
	 * 校验使用类型是否存在于使用类型表IDC_JCDM_SYLX中
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateSylx(String value) {
		return baseValidateBMMap(IDC_JCDM_SYLX, value, "ID");
	}

	/**
	 * 校验链路类型是否存在于链路类型表IDC_JCDM_LLLX中
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateLllx(String value) {
		return baseValidateBMMap(IDC_JCDM_LLLX, value, "ID");
	}

	/**
	 * 校验IP地址使用方式是否存在于IP地址使用方式表IDC_JCDM_IPDZSYFS中
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateIpdzsyfs(String value) {
		return baseValidateBMMap(IDC_JCDM_IPDZSYFS, value, "ID");
	}

	/**
	 * 校验地址中的省份：
	 * 
	 * @param province
	 * @return
	 */
	public static boolean validateDzsf(String province) {
		Map<String, String> map = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_DZSF, province);
		return map != null && map.size() > 0;
	}

	/**
	 * 校验网站备案类型
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateWzbalx(String value) {
		return baseValidateBMMap(IDC_JCDM_WZBALX, value, "ID");
	}

	/**
	 * 校验域名是否存在非法分隔符
	 * 
	 * @param domain
	 * @return
	 */
	public static boolean containDomainSeparator(String domain) {
		return domain.contains(",") || domain.contains("，") || domain.contains("；");
	}

	/**
	 * 校验行政区域代码
	 * 
	 * @param level
	 * @param houseProvince
	 * @return
	 */
	public static Object[] validateXzqydm(String codeLevel, String xzqydm) {
		Map<String, String> map = BMDataUtil.instance.getDataMapByTableNameAndId(IDC_JCDM_XZQYDM, xzqydm);
		if (map != null && map.containsKey("CODE") && map.get("CODE").equals(xzqydm) && map.containsKey("CODELEVEL") /*&& map.get("CODELEVEL").equals(codeLevel)*/) {
			if (!StringUtils.isBlank("MC")) {
				if (xzqydm.equals("441900") && codeLevel.equals("3") || // 东莞市
						xzqydm.equals("442000") && codeLevel.equals("3") || // 中山市
						xzqydm.equals("620201") && codeLevel.equals("3")) { // 嘉峪关市
																			// 名称为市辖，应该去掉
					// TODO:海南省还有两个不设区的地级市，海南省的暂时没有业务，暂时不做校验
					return new Object[] { true, "" };
				} else {
					return new Object[] { true, map.get("MC") };
				}
			}
		}
		return new Object[] { false };
	}

	/**
	 * 校验规则来源
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateGzly(String value) {
		return baseValidateBMMap(IDC_JCDM_GZLY, value, "ID");
	}
	
	/**
	 * 校验关键词等级
	 * @param value
	 * @return
	 */
	public static Object[] validateGjcdj(String value) {
		return baseValidateBMMap(IDC_JCDM_GJCDJ, value, "ID");
	}

	/**
	 * 校验与或关系
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateYhgx(String value) {
		return baseValidateBMMap(IDC_JCDM_YHGX, value, "ID");
	}

	/**
	 * 校验规则管理的关键字匹配范围
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validatePpfw(String value) {
		return baseValidateBMMap(IDC_JCDM_PPFW, value, "CODE");
	}

	/**
	 * 校验规则管理的协议类型
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateXylx(String value) {
		return baseValidateBMMap(IDC_JCDM_XYLX, value, "ID");
	}

	/**
	 * 校验是否广东分公司
	 * 
	 * @param idcName
	 * @return
	 */
	public static boolean validateIfGdIdcName(String idcName) {
		return !StringUtils.isBlank(idcName) && idcName.equals("中国电信股份有限公司广东分公司");
	}

	/**
	 * 校验是否为汇聚型机房
	 * 
	 * @param houseName
	 * @return
	 */
	public static boolean validateIfHJXJF(String houseName) {
		return BMDataUtil.instance.isHjxjf(houseName);
	}

	/**
	 * 校验隶属单位地市码(单选)
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateSubordinateunitAreacode(String value) {
		if (StringUtils.isBlank(value)) {
			return new Object[] { false };
		}
		return baseValidateBMMap("gdXzqydmMap", value, "CODE");
	}

	/**
	 * 校验隶属单位地市码(多选)
	 * 
	 * @param value
	 * @return
	 */
	public static Object[] validateSubordinateunitAreacodes(String value) {
		if (StringUtils.isBlank(value)) {
			return new Object[] { false };
		}
		String[] arr = value.split(",");
		String result = "";
		List<String> errorList = new ArrayList<String>();
		if (arr.length > 0) {
			for (int index = 0; index < arr.length; index++) {
				String areaCode = arr[index];
				if (!StringUtils.isBlank(areaCode)) {
					Object[] validateResult = baseValidateBMMap("gdXzqydmMap", areaCode, "CODE");
					if (((Boolean) validateResult[0]).booleanValue() == true) {
						arr[index] = (String) validateResult[1];
						if (index == arr.length - 1) {
							result += arr[index];
						} else {
							result += arr[index] + ",";
						}
					} else {
						errorList.add(areaCode);
					}
				}
			}
			if (errorList.isEmpty()) {
				return new Object[] { true, result };
			} else {
				String errors = "";
				for (int index = 0; index < errorList.size(); index++) {
					if (index == errorList.size() - 1) {
						errors += errorList.get(index);
					} else {
						errors += errorList.get(index) + ",";
					}
				}
				return new Object[] { false, errors };
			}
		}
		return new Object[] { false };
	}

	/**
	 * 校验隶属单位地市码是否在汇聚型机房中（单选）
	 * 
	 * @param houseName
	 * @param value
	 * @return
	 */
	public static Object[] validateIfInHjxjf(String houseName, String value) {
		return baseValidateBMMap("IDC_JCDM_HJXJF:" + houseName, value, "CODE");
	}

	/**
	 * 校验隶属单位地市码是否在汇聚型机房中（多选）
	 * 
	 * @param houseName
	 * @param value
	 * @return
	 */
	public static Object[] validateIfInHjxjfs(String houseName, String value) {
		if (StringUtils.isBlank(value)) {
			return new Object[] { false };
		}
		String[] arr = value.split(",");
		String result = "";
		List<String> errorList = new ArrayList<String>();
		if (arr.length > 0) {
			for (int index = 0; index < arr.length; index++) {
				String areaCode = arr[index];
				if (!StringUtils.isBlank(areaCode)) {
					Object[] validateResult = baseValidateBMMap("IDC_JCDM_HJXJF:" + houseName, areaCode, "CODE");
					if (((Boolean) validateResult[0]).booleanValue() == true) {
						arr[index] = (String) validateResult[1];
						if (index == arr.length - 1) {
							result += arr[index];
						} else {
							result += arr[index] + ",";
						}
					} else {
						errorList.add(areaCode);
					}
				}
			}
			if (errorList.isEmpty()) {
				return new Object[] { true, result };
			} else {
				String errors = "";
				for (int index = 0; index < errorList.size(); index++) {
					if (index == errorList.size() - 1) {
						errors += errorList.get(index);
					} else {
						errors += errorList.get(index) + ",";
					}
				}
				return new Object[] { false, errors };
			}
		}
		return new Object[] { false };
	}

	/**
	 * BMMap的基础校验方法
	 * 
	 * @param redisKeyPrefix:redis的key的前缀，也就是tableName
	 * @param value
	 * @param returnField
	 * @return
	 */
	public static Object[] baseValidateBMMap(String redisKeyPrefix, String value, String returnField) {
		String temp = getTypeOfIdOrValue(value);
		if (temp == null) {
			return new Object[] { false };
		}
		Map<String, String> map = BMDataUtil.instance.getDataMapByTableNameAndId(redisKeyPrefix, temp); // 协议类型
		if (map != null && !StringUtils.isBlank(map.get(returnField))) {
			return new Object[] { true, map.get(returnField) };
		}
		return new Object[] { false, temp };
	}

	/**
	 * 根据code来获取完整的name
	 * 
	 * @param tableName
	 * @param code
	 * @param returnField
	 * @return
	 */
	public static String getNameByCode(String tableName, String code, String returnField) {
		Map<String, String> map = BMDataUtil.instance.getDataMapByTableNameAndId(tableName, code);
		if (map != null && !StringUtils.isBlank(map.get(returnField))) {
			return map.get(returnField);
		}
		return code;
	}

	/**
	 * 根据code来获取完整的code和name，使用默认的分隔符-
	 * 
	 * @param tableName
	 * @param code
	 * @param returnField
	 * @return
	 */
	public static String getCodeAndNameByCode(String tableName, String code, String returnField) {
		return getCodeAndNameByCode(tableName, code, returnField, "-");
	}

	/**
	 * 根据code来获取完整的code和name，自定义分隔符
	 * 
	 * @param tableName
	 * @param code
	 * @param returnField
	 * @param separator
	 * @return
	 */
	public static String getCodeAndNameByCode(String tableName, String code, String returnField, String separator) {
		Map<String, String> map = BMDataUtil.instance.getDataMapByTableNameAndId(tableName, code);
		if (map != null && !StringUtils.isBlank(map.get(returnField))) {
			return code + separator + map.get(returnField);
		}
		return code;
	}

	/**
	 * 根据name来获取完整的code和name，使用默认的分隔符-
	 * 
	 * @param tableName
	 * @param name
	 * @param returnField
	 * @return
	 */
	public static String getCodeAndNameByName(String tableName, String name, String returnField) {
		return getCodeAndNameByName(tableName, name, returnField, "-");
	}

	/**
	 * 根据name来获取完整的code和name，自定义分隔符
	 * 
	 * @param tableName
	 * @param name
	 * @param returnField
	 * @param separator
	 * @return
	 */
	public static String getCodeAndNameByName(String tableName, String name, String returnField, String separator) {
		Map<String, String> map = BMDataUtil.instance.getDataMapByTableNameAndId(tableName, name);
		if (map != null && !StringUtils.isBlank(map.get(returnField))) {
			return map.get(returnField) + separator + name;
		}
		return name;
	}

	/**
	 * 根据name来获取完整的areaCode和name
	 * 
	 * @param code
	 * @return
	 */
	public static String getAreaCodeAndNameByCode(String code) {
		return getCodeAndNameByCode(IDC_JCDM_XZQYDM, code, "MC");
	}

	/**
	 * 根据code来获取完整的areaCode和name
	 * 
	 * @param code
	 * @return
	 */
	public static String getNameByCode(String code) {
		return getNameByCode(IDC_JCDM_XZQYDM, code, "MC"); 
	}

	/**
	 * 地市码是否为用户所拥有的地市码<br>
	 * 返回值：
	 * 
	 * @param areaCodes : 不做非空校验和合法性校验
	 * @return
	 *//*
	public static Object[] ifAreaCodesInUserAreaCodes(String areaCodes) {
		String userAreaCode = SecurityUtils.getAuthenticatedUser().getAccount().getCityStrs();
		return ifAreaCodesInUserAreaCodesWithName(areaCodes, userAreaCode);
	}*/
	/**
	 * 地市码是否为用户所拥有的地市码<br>
	 * 返回值：
	 * 
	 * @param areaCodes : 不做非空校验和合法性校验
	 * @return
	 */
//	public static Object[] ifAreaCodesInUserAreaCodes(String areaCodes) {
////		String userAreaCode = SecurityUtils.getAuthenticatedUser().getAccount().getCityStrs();
//		return ifAreaCodesInUserAreaCodesWithName(areaCodes, userAreaCode);
//	}

	/**
	 * 过滤出当前用户有权限的地市码
	 * 
	 * @param areaCodes
	 * @return
	 */
	/*public static String filterAreaCodesForCurrentUser(String areaCodes) {
		if (StringUtil.isBlank(areaCodes)) {
			return null;
		}
		String[] aList = areaCodes.split(",");
		if (null == aList || aList.length < 1) {
			return null;
		}
		String userAreaCode = "," + SecurityUtils.getAuthenticatedUser().getAccount().getCityStrs() + ",";
		List<String> usersAreaCodeList = new ArrayList<String>();
		for (String areaCode : aList) {
			if (StringUtil.isBlank(areaCode)) {
				continue;
			}
			if (userAreaCode.contains("," + areaCode + ",")) {
				usersAreaCodeList.add(areaCode);
			}
		}
		if (usersAreaCodeList.size() == 0) {
			return null;
		}
		return StringUtils.join(usersAreaCodeList, ",");
	}*/

	/**
	 * 地市码是否为用户所拥有的地市码
	 * 
	 * @param areaCodes : 不做非空校验和合法性校验
	 * @param userAreaCodes : 不做合法性校验
	 * @return
	 */
	public static Object[] ifAreaCodesInUserAreaCodes(String areaCodes, String userAreaCodes) {
		if (StringUtils.isBlank(userAreaCodes)) {
			return new Object[] { false, areaCodes };
		}
		String[] areaCodeArr;
		if (areaCodes.contains(",")) {
			areaCodeArr = areaCodes.split(",");
		} else {
			areaCodeArr = new String[] { areaCodes };
		}
		String[] userAreaCodeArr;
		if (userAreaCodes.contains(",")) {
			userAreaCodeArr = userAreaCodes.split(",");
		} else {
			userAreaCodeArr = new String[] { userAreaCodes };
		}
		Set<String> noInSet = new HashSet<String>();
		for (String areaCode : areaCodeArr) {
			boolean inFlag = false;
			for (String userAreaCode : userAreaCodeArr) {
				if (areaCode.equals(userAreaCode)) {
					inFlag = true;
					break;
				}
			}
			if (inFlag == false) {
				noInSet.add(areaCode);
			}
		}
		if (noInSet.isEmpty()) {
			return new Object[] { true };
		}
		return new Object[] { false, noInSet };
	}

	/**
	 * 地市码是否为用户所拥有的地市码
	 * 
	 * @param areaCodes : 不做非空校验和合法性校验
	 * @param userAreaCodes : 不做合法性校验
	 * @return
	 */
	public static Object[] ifAreaCodesInUserAreaCodesWithName(String areaCodes, String userAreaCodes) {
		String[] areaCodeArr;
		if (areaCodes.contains(",")) {
			areaCodeArr = areaCodes.split(",");
		} else {
			areaCodeArr = new String[] { areaCodes };
		}
		if (StringUtils.isBlank(userAreaCodes)) {
			Set<String> noInSetWithName = new HashSet<String>();
			for (String areaCode : areaCodeArr) {
				noInSetWithName.add(areaCode);
			}
			return new Object[] { false, noInSetWithName };
		}
		String[] userAreaCodeArr;
		if (userAreaCodes.contains(",")) {
			userAreaCodeArr = userAreaCodes.split(",");
		} else {
			userAreaCodeArr = new String[] { userAreaCodes };
		} 
		Set<String> noInSet = new HashSet<String>();
		for (String areaCode : areaCodeArr) {
			boolean inFlag = false;
			for (String userAreaCode : userAreaCodeArr) {
				if (areaCode.equals(userAreaCode)) {
					inFlag = true;
					break;
				} else if (userAreaCode.equals("310000") && areaCode.startsWith("31")) { // 直辖市上海市只有一个地市码
					inFlag = true;
					break;
				} else if (userAreaCode.equals("110000") && areaCode.startsWith("11")) { // 直辖市北京市只有一个地市码
					inFlag = true;
					break;
				}
			}
			if (inFlag == false) {
				noInSet.add(areaCode);
			}
		}
		if (noInSet.isEmpty()) {
			return new Object[] { true };
		}
		Set<String> noInSetWithName = new HashSet<String>();
		for (String areaCode : noInSet) {
			String name = getNameByCode(IDC_JCDM_XZQYDM, areaCode, "MC"); 
			if (name.equals("市辖区")) { // 如果是市辖区，
				String parentCode = getNameByCode(IDC_JCDM_XZQYDM, areaCode, "PARENTCODE"); 
				name = getNameByCode(IDC_JCDM_XZQYDM, parentCode, "MC"); 
			} 
			noInSetWithName.add(name); 
		}
		return new Object[] { false, noInSetWithName };
	}

}
