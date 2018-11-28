package com.aotain.serviceapi.server.constant;

public enum DealFlagEnum {

    STATUS1(1,"预审不通过"),
    STATUS2(2,"上报审核中"),
    STATUS3(3,"上报审核不通过"),
    STATUS4(4,"提交上报"),
    STATUS5(5,"上报成功"),
    STATUS6(6,"上报失败");

    private int code;
    private String name;

    DealFlagEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

