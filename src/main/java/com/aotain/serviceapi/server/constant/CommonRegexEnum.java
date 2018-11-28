package com.aotain.serviceapi.server.constant;

/**
 * 常用正则表达式枚举类
 *
 * @author bang
 * @date 2018/07/06
 */
public enum CommonRegexEnum {
    PHONE_REGEXP(8,"手机正则表达式","^(1[0-9])\\d{9}$"),
    TELEPHONE_REGEXP(9,"固话正则表达式","^(\\d{4}\\-?\\d{7})|(\\d{3}\\-?\\d{8})|\\d{7,8}$"),
    POST_PROVINCE_REGEXP(10,"邮政编码省级正则表达式","^[0-9]\\d{1}0{4}$"),
    POST_CITY_REGEXP(11,"邮政编码地市正则表达式","^[1-9]\\d{3}0{2}$"),
    POST_COUNTRY_REGEXP(12,"邮政编码县级正则表达式","^[1-9]\\d{5}$"),
    POST_REGEXP(13,"邮政编码正则表达式","^[0-9]\\d{5}$"),
    EMAIL_REGEXP(14,"邮箱正则表达式","^[a-zA-Z0-9_\\.\\-]+@[a-zA-Z0-9_\\.\\-]*$"),

    //TODO
    BUSINESS_LICENSE_REGEXP(23,"工商营业执照表达式",""),
    IDCARD_REGEXP(15,"身份证正则表达式","^[1-9](\\d{14}|(\\d{16}[0-9xX]))$"),
    ORGANIZATION_REGEXP(1,"组织机构正则表达式","^[0-9A-Z]{8}-[0-9X]$"),
    CORPORATION_REGEXP(2,"事业法人正则表达式","[\\\\dA-HJ-NP-RT-UW-Y]{18}"),
    ARMY_CODE_REGEXP(4,"军队代号正则表达式","^[1-9][0-9]{3,4}$"),
    //TODO
    SOCIETY(24,"社团法人正则","[\\\\dA-HJ-NP-RT-UW-Y]{18}"),
    MTPS_REGEXP(7,"台胞正则表达式","^[0-9]{8}$"),
    ARMY_REGEXP(6,"军队正则表达式","^军[0-9]{7}$"),
    PASSPORT_REGEXP(5,"护照正则表达式","^[\\\\dA-Za-z]{9}$"),

    NUMBER_REGEXP(16,"数字正则表达式","^\\d+$"),
    POSITIVE_NUMBER_REGEXP(17,"正整数正则表达式","^[1-9]{1}\\d+$"),
    IPV4_REGEXP(18,"ipv4正则表达式","^(25[0-5]?|2[0-4]?\\d?|0|1\\d{0,2}|[3-9]\\d?)(\\.(25[0-5]?|2[0-4]?\\d?|0|1\\d{0,2}|[3-9]\\d?)){3}$"),
    IPV6_STD_REGEXP(19,"ipv6正则表达式","^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$"),
    IPV6_HEX_REGEXP(20,"16进制ipv6的正则表达式","^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$"),
    DATE_REGEXP(3,"日期正则表达式","^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1])$"),
    DATA_TIME_REGEXP(21,"yyyy-MM-dd HH:mm:ss格式日期正则表达式","^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$"),

    DOMAIN_REGEXP(22,"域名正则表达式","^(([A-Za-z0-9\u4E00-\u9FA5-~]+)\\.)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$");
    private String desc;
    private Integer subType;
    private String value;

    CommonRegexEnum(int subType,String desc,String value){
        this.subType = subType;
        this.desc = desc;
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public static void main(String[] args) {
        System.out.println(CommonRegexEnum.PHONE_REGEXP.getSubType());
    }
}

