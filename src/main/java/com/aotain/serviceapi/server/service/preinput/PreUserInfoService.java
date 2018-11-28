package com.aotain.serviceapi.server.service.preinput;

import com.aotain.cu.serviceapi.dto.ApproveResultDto;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserInformation;

import java.util.List;
import java.util.Map;

public interface PreUserInfoService {

    ResultDto insertData(UserInformation dto);

    ResultDto updateData(UserInformationDTO dto);

	ResultDto batchDeleteUserInfo(List<UserInformationDTO> dtos);

	ResultDto approve(String userId);

	ResultDto revertApprove(String userId);

	List<UserInformation> findListByJyz(Integer jyzId);

	ResultDto changeUserNature(UserInformation dto);

	/**
	 * 根据用户id查询用户详细信息
	 * @param userId
	 * @return
	 */
	UserInformationDTO findByUserId(String userId);

	/**
	 * 查询用户分页数据
	 * @param dto
	 * @return
	 */
	PageResult<UserInformationDTO> listUserData(UserInformationDTO dto);

	Map<Integer,ResultDto> imporUserData( Map<Integer,UserInformationDTO> dto);

	List<ApproveResultDto> getApproveResult(String approveId);

}
