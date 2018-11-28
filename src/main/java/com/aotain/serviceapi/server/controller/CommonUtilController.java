package com.aotain.serviceapi.server.controller;

import com.aotain.cu.serviceapi.dto.ReportFile;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.WaitApproveProcess;
import com.aotain.serviceapi.server.service.CommonUtilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value="/common")
@Api(value="CommonUtilController",description="工具类")
public class CommonUtilController {


    @Autowired
    private CommonUtilService commonUtilService;

    @ApiOperation(value="获取地市码", notes="根据地市代码获取地市码")
    @RequestMapping(value = "getAreaCode", method = { RequestMethod.POST })
    @ResponseBody
    public List<IdcJcdmXzqydm> getAreaCode(@RequestBody Integer code) {
        return commonUtilService.getAreaCode(code);
    }

    @ApiOperation(value="获取地市码", notes="根据地市代码获取地市码")
    @RequestMapping(value = "getAreaDic", method = { RequestMethod.POST })
    @ResponseBody
    public List<String> getAreaDic() {
        return commonUtilService.getDetailAreaDic();
    }


    @ApiOperation(value="插入处理状态流水表", notes="插入处理状态流水表")
    @RequestMapping(value = "insertApproveProcess", method = { RequestMethod.POST })
    @ResponseBody
    public int insertApproveProcess(WaitApproveProcess domain) {
        return commonUtilService.insertApproveProcess(domain);
    }

    
    @ApiOperation(value="获取上报文件信息", notes="根据submitId获取上报文件信息")
    @RequestMapping(value = "getReportFileInfo", method = { RequestMethod.POST })
    @ResponseBody
    public PageResult<ReportFile> getReportFileInfo(@RequestBody String submitId) {
        return commonUtilService.getReportFileInfo(submitId);
    }
    
}
