package com.aotain.serviceapi.server.service;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.utils.ResponseConstant;

public class CommonService {


    /**
     * 成功返回
     * @return
     */
    public ResultDto successRep(){
        ResultDto dto = new ResultDto();
        dto.setResultCode(ResponseConstant.RESPONSE_SUCCESS);
        dto.setStatusCode(ResponseConstant.RESPONSE_STATUE_CODE_SUCCESS);
        return dto;
    }

    /**
     * 拒绝操作
     * @return
     */
    public ResultDto successRef(){
        ResultDto dto = new ResultDto();
        dto.setResultCode(ResponseConstant.RESPONSE_REFUSE);
        dto.setStatusCode(ResponseConstant.RESPONSE_STATUE_CODE_SERVICEREFUSE);
        return dto;
    };

    /**
     * 异常
     * @return
     */
    public ResultDto errorResp(){
        ResultDto dto = new ResultDto();
        dto.setResultCode(ResponseConstant.RESPONSE_ERROR);
        dto.setStatusCode(ResponseConstant.RESPONSE_STATUE_CODE_SERVICEERROR);
        return dto;
    }
}
