package com.aotain.serviceapi.server.dao;

import java.util.List;
import java.util.Map;

import com.aotain.cu.serviceapi.dto.ApproveResultDto;
import com.aotain.cu.serviceapi.dto.ReportFile;

import com.aotain.cu.serviceapi.model.WaitApproveProcess;
import org.apache.ibatis.annotations.Param;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;

@MyBatisDao
public interface CommonUtilMapper {

    public List<IdcJcdmXzqydm> getAreaCode(@Param("code") Integer code);
    public List<String> getDetailAreaDic();
    
    List<Map<String, Object>> getHouseMappingMap();
    
    List<Map<String, Object>> getIdentityMappingMap();
    
    List<Map<String, Object>> getSubordinateMappingMap();

    /**
     * 获取审核结果
     * @param dto
     * @return
     */
    public List<ApproveResultDto> getApproveResult(ApproveResultDto dto);
    /**
     * 
     * 获取上报文件信息
     */
	public List<ReportFile> getReportFileInfo(String submitId);

    int insertApproveProcess(WaitApproveProcess domain);
}
