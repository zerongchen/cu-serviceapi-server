package com.aotain.serviceapi.server.controller;

import com.aotain.cu.serviceapi.dto.ResultDto;

/**
 * 带有异常处理公共控制器
 * 
 * @author liuz@aotian.com
 * @date 2018年8月25日 下午4:21:58
 */
public class CommonControllerWithExceptionHandler extends ExceptionHandle {
	 /**
     * 获取成功返回结果
     * @return
     */
    public ResultDto getSuccessResult(){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
        resultDto.setResultMsg("success");
        return resultDto;
    }

    /**
     * 获取失败返回结果
     * @return
     */
    public ResultDto getErrorResult(){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
        resultDto.setResultMsg("error");
        return resultDto;
    }

    /**
     * 获取失败返回结果
     * @param msg  失败msg
     * @return
     */
    public ResultDto getErrorResult(String msg){
        ResultDto resultDto = new ResultDto();
        resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
        resultDto.setResultMsg(msg);
        return resultDto;
    }
}
