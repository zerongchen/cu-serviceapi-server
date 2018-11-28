package com.aotain.serviceapi.server.controller;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.utils.ResponseConstant;

public abstract class ExceptionHandle {

    public ResultDto handleHystrix() {
        ResultDto dto = new ResultDto();
        dto.setResultCode(ResponseConstant.RESPONSE_ERROR);
        dto.setStatusCode(ResponseConstant.RESPONSE_STATUE_CODE_NETERROR);
        return dto;
    }

    public ResultDto handleError() {
        ResultDto dto = new ResultDto();
        dto.setResultCode(ResponseConstant.RESPONSE_ERROR);
        dto.setStatusCode(ResponseConstant.RESPONSE_STATUE_CODE_SERVICEERROR);
        return dto;
    }
}
