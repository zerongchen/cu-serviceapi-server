package com.aotain.serviceapi.server.service.preinput;

import java.util.List;

import com.aotain.cu.serviceapi.dto.*;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.PageResult;

/**
 * @ClassName HouseInformationService
 * @Author tanzj
 * @Date 2018/7/23
 **/
public interface IHouseInformationService {

    public PageResult<HouseInformationDTO> listHouseInfo(HouseInformationDTO dto);

    public HouseInformationDTO getHouseInfoById(HouseInformationDTO dto);

    public PageResult<HouseFrameInformationDTO> getIndexHouseFrame(HouseFrameInformationDTO dto);

    public PageResult<HouseGatewayInformationDTO> getIndexLink(HouseGatewayInformationDTO link);

    public PageResult<HouseIPSegmentInforDTO> getIndexIpSegment(HouseIPSegmentInforDTO dto);

    List<HouseInformationDTO> getHouseByJyzId(HouseInformationDTO dto);
    /**
     * 机房信息预审
     * @param houseId
     * @return
     */
    ResultDto approve(String houseId);

    /**
     * 撤销机房预审
     * @param houseId
     * @return
     */
    ResultDto revertApprove(String houseId);

    /**
     * 查询指定id的机房预审信息
     * @param houseId
     * @return
     */
    List<ApproveResultDto> findCheckResultById(String houseId);
    
    ResultDto integrityVerification(long houseId, int identity);
    
    ResultDto insertData(HouseInformation dto);

	ResultDto updateData(HouseInformationDTO dto);

	ResultDto deleteHouseInfo(HouseInformationDTO dto);
	
	/**
	 * 批量删除机房信息
	 * 
	 */
	ResultDto batchDeleteHouseInfos(List<HouseInformationDTO> deleteList);
}
