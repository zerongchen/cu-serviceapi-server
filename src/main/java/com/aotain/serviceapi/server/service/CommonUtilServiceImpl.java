package com.aotain.serviceapi.server.service;

import com.aotain.cu.serviceapi.dto.ReportFile;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.WaitApproveProcess;
import com.aotain.serviceapi.server.dao.CommonUtilMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonUtilServiceImpl implements CommonUtilService{

    @Autowired
    private CommonUtilMapper commonUtilMapper;

    @Override
    public List<IdcJcdmXzqydm> getAreaCode( Integer code ) {
        List<IdcJcdmXzqydm> list = commonUtilMapper.getAreaCode(code);
        return list;
    }

    @Override
    public List<String> getDetailAreaDic() {
        return commonUtilMapper.getDetailAreaDic();
    }

	@Override
	public Map<String, String> getHouseMappingMap() {
		Map<String, String> mappingMap = new HashMap<>();
		List<Map<String, Object>> mappingList = commonUtilMapper.getHouseMappingMap();
		if (mappingList != null && mappingList.size() > 0) {
			for (Map<String, Object> map : mappingList) {
				mappingMap.put(map.get("HOUSEID") + "", map.get("HOUSENAME") + "");
			}
		}
		return mappingMap;
	}

	@Override
	public Map<String, String> getIdentityMappingMap() {
		Map<String, String> mappingMap = new HashMap<>();
		List<Map<String, Object>> mappingList = commonUtilMapper.getIdentityMappingMap();
		if (mappingList != null && mappingList.size() > 0) {
			for (Map<String, Object> map : mappingList) {
				mappingMap.put(map.get("ID") + "", map.get("MC") + "");
			}
		}
		return mappingMap;
	}

	@Override
	public Map<String, String> getSubordinateMappingMap() {
		Map<String, String> mappingMap = new HashMap<>();
		List<Map<String, Object>> mappingList = commonUtilMapper.getSubordinateMappingMap();
		if (mappingList != null && mappingList.size() > 0) {
			for (Map<String, Object> map : mappingList) {
				mappingMap.put(map.get("AREA_CODE") + "", map.get("AREA_NAME") + "");
			}
		}
		return mappingMap;
	}

	@Override
	public PageResult<ReportFile> getReportFileInfo(String submitId) {
		PageResult<ReportFile> result = new PageResult<ReportFile>();
		List<ReportFile> info = commonUtilMapper.getReportFileInfo(submitId);
		if (info!=null) {
			result.setRows(info);
		}else{
			result.setRows(new ArrayList<ReportFile>());
		}
		return result;
	}
	
	

	@Override
	public int insertApproveProcess(WaitApproveProcess domain) {
		return commonUtilMapper.insertApproveProcess(domain);
	}
}
