package com.aotain.serviceapi.server.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 校验结果类
 *
 * @author bang
 * @date 2018/07/06
 */
@Getter
@Setter
public class ValidateResult {
    /**
     * 校验结果code
     * @see ResultCodeEnum
     */
    private int code;
    /**
     * 校验结果msg
     */
    private String msg;

    public ValidateResult(){
        super();
    }

    public ValidateResult(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public static ValidateResult getSuccessResult(){
        ValidateResult validateResult = new ValidateResult();
        validateResult.setCode(ResultCodeEnum.SUCCESS.getCode());
        validateResult.setMsg("校验通过");
        return validateResult;
    }

    public static ValidateResult getErrorResult(String msg){
        ValidateResult validateResult = new ValidateResult();
        validateResult.setCode(ResultCodeEnum.ERROR.getCode());
        validateResult.setMsg(msg);
        return validateResult;
    }

    @Override
    public String toString() {
        return "ValidateResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
