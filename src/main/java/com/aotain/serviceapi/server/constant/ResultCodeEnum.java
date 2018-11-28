package com.aotain.serviceapi.server.constant;

/**
 * 公共resultCode实体类
 *
 * @author bang
 * @date 2018/07/09
 */
public enum ResultCodeEnum {
    SUCCESS("success",200),
    ERROR("error",500);

    private String codeMsg;
    private int code;
    ResultCodeEnum(String codeMsg,int code){
        this.codeMsg = codeMsg;
        this.code = code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

