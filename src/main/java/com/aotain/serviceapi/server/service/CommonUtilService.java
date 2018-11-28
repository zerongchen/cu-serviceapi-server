package com.aotain.serviceapi.server.service;

import com.aotain.cu.serviceapi.dto.ReportFile;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.WaitApproveProcess;

import java.util.List;
import java.util.Map;

public interface CommonUtilService {

    public List<IdcJcdmXzqydm> getAreaCode( Integer code);
    public List<String> getDetailAreaDic();
    
    Map<String, String> getHouseMappingMap();
    
    Map<String, String> getIdentityMappingMap();
    
    Map<String, String> getSubordinateMappingMap();
    
	public PageResult<ReportFile> getReportFileInfo(String submitId);

    int insertApproveProcess(WaitApproveProcess domain);
}
