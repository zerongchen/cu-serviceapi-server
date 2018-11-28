package com.aotain.serviceapi.server.service.preinput;

import com.aotain.cu.serviceapi.dto.ApproveResultDto;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.IdcInformation;

import java.util.List;

public interface IdcInformationService {

    IDCInformationDTO findByJyzId(IDCInformationDTO ass);
    IdcInformation getData(IdcInformation ass);
    List<IdcInformation> getList();
    ResultDto insertData(IdcInformation ass);
    ResultDto updateData(IdcInformation ass);
    ResultDto deleteData(Integer jyzId);
    ResultDto preValidate(Integer jyzId);
    ResultDto revokeValid(Integer jyzId);
    List<ApproveResultDto> getApproveResult(String approveId);
}
