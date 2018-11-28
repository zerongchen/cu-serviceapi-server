package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.HouseGatewayInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.preinput.HouseLinkMapper;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.IHouseLinkService;
import com.aotain.serviceapi.server.validate.impl.HouseLinkValidatorImpl;

/**
 * 机房链路预录入service服务类
 *
 * @author bang
 * @date 2018/07/20
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class HouseLinkServiceImpl extends BaseService implements IHouseLinkService{

    private static final Logger logger = LoggerFactory.getLogger(HouseLinkServiceImpl.class);

    @Autowired
    private HouseLinkMapper houseLinkMapper;

    @Autowired
    public HouseLinkServiceImpl(HouseLinkValidatorImpl iHouseLinkValidator){
        super(iHouseLinkValidator);
    }

    @Override
    public ResultDto saveData(HouseGatewayInformationDTO baseModel) {
        return null;
    }

	private ResultDto updateData(List<HouseGatewayInformationDTO> dtos) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (HouseGatewayInformation dto : dtos) {
				try {
					HouseGatewayInformation po = houseLinkMapper.findByLinkId(dto.getGatewayId());
					if(po.getDealFlag()!=null&&po.getDealFlag()==HouseConstant.DealFlagEnum.CHECK_FAIL.getValue()){//链路已报备后修改变更未上报
						dto.setCzlx(HouseConstant.CzlxEnum.MODIFY.getValue());
					}
					int flag = update(dto);
					if (flag <= 0) {
						// 插入或新增失败
						return getErrorResult("更新记录失败");
					}
					logger.info("update house link success!");
					// 判定机房主体状态信息，并同时判断是否需要写机房缓存表
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					logger.info("update house czlx and deal_flag success!");
				} catch (Exception e) {
					logger.error("update house link failed!", e);
					return getErrorResult("更新记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

    @Override
    public ResultDto deleteData(HouseGatewayInformationDTO baseModel){
        return null;
    }

	@Override
	public boolean existRecord(String linkNo) {
		return houseLinkMapper.findByLinkNo(linkNo) == null ? false : true;
	}

	protected int insert(BaseModel dto) {
		HouseGatewayInformation houseGatewayInformationDTO = (HouseGatewayInformation) dto;
		return houseLinkMapper.insertLinkInformation(houseGatewayInformationDTO);
	}

	@Override
	protected int update(BaseModel dto) {
		HouseGatewayInformationDTO houseGatewayInformationDTO = (HouseGatewayInformationDTO) dto;
		return houseLinkMapper.updateHouseGatewayByLinkNo(houseGatewayInformationDTO);
	}

    @Override
    protected int delete(BaseModel dto) {
        return 0;
    }

	@Override
	public ResultDto insertData(List<HouseGatewayInformation> dtos, Long houseId) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (HouseGatewayInformation dto : dtos) {
				try {
					if (houseId != null) {
						dto.setHouseId(houseId);
					}
					// 新增链路信息
					int result = insert(dto);
					if (result <= 0) {
						return getErrorResult("新增记录失败");
					}
					logger.info("insert house link success!");
					
					// 判定机房主体状态信息，并同时判断是否需要写机房缓存表
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					logger.info("update house czlx and deal_flag success!");
				} catch (Exception e) {
					logger.error("insert house link failed!", e);
					return getErrorResult("新增记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

	/**
	 * 批量插入链路信息
	 */
	@Override
	public ResultDto batchInsertHouseLinkInfos(List<? extends HouseGatewayInformation> links, Long houseId, boolean allowInsert) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (links == null || links.size() == 0) {
			return result;
		}
		
		//数据重复性校验
		Map<String, String> map = new HashMap<>();
		for(HouseGatewayInformation dto:links){
			if(map.containsKey(dto.getLinkNo())){
				result =new ResultDto();
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
				result.setResultMsg(dto.getLinkNo());
				return result;
			}else{
				map.put(dto.getLinkNo(), dto.getLinkNo());
			}
		}
		
		
		//校验
		boolean success = false;
		List<Integer> successList = new ArrayList<Integer>(links.size());
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>(links.size());
		for (int i = 0; i < links.size(); i++) {
			HouseGatewayInformation dto = links.get(i);
			dto.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
			if (houseId != null) {
				dto.setHouseId(houseId);
			}
			ResultDto validateResult = validateResult(dto);
			validateResultMap.put(String.valueOf(i), validateResult.getAjaxValidationResult());
			successList.add(validateResult.getResultCode());
			
			//核验成功的对象加入允许持久化
			if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
				if (allowInsert) {
					int flag = insert(dto);
					if (flag <= 0) {
						return getErrorResult("新增记录失败");
					}
					logger.info("insert house link success!");
					
					// 判定机房主体状态信息，并同时判断是否需要写机房缓存表
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					logger.info("update house czlx and deal_flag success!");
				}
			} 
		}
		if (!successList.contains(ResultDto.ResultCodeEnum.ERROR.getCode())) {
			//全部校验通过
			success = true;
			result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
		} else {
			//部分校验通过
			success = false;
			result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		}
		//用于返回新增ID
		result.setAjaxValidationResultMap(validateResultMap);
		if (success && !allowInsert) {
			for (int n = 0; n < links.size(); n++) {
				if (houseId != null) {
					links.get(n).setHouseId(houseId);
				}
				int flag = insert(links.get(n));
				if (flag <= 0) {
					return getErrorResult("新增记录失败");
				}
				logger.info("insert house link success!");
				
				// 判定机房主体状态信息，并同时判断是否需要写机房缓存表
				dealHouseMainWhileISubInfoUpdate(links.get(n).getHouseId());
				//返回新增ID
				result.getAjaxValidationResultMap().get(String.valueOf(n)).setPid(links.get(n).getGatewayId());
				logger.info("update house czlx and deal_flag success!");
			}
		} 
		return result;
	}
	
	@Override
	public ResultDto batchUpdate(List<HouseGatewayInformationDTO> dtos) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.MODIFY.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			logger.info("validate house link success!");
			return updateData(dtos);
		} else {
			logger.info("validate house link failed!");
			return validateResult;
		}
	}
	
	@Override
	public ResultDto batchDelete(List<? extends HouseGatewayInformation> links) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (links == null || links.size() <= 0) {
			return validateResult;
		}
		validateResult = validateResult(links, HouseConstant.OperationTypeEnum.DELETE.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			logger.info("validate house link success!");
			return deleteData(links);
		} else {
			logger.info("validate house link failed!");
			return validateResult;
		}
	}

	/**
	 * 批量处理删除链路
	 */
	private ResultDto deleteData(List<? extends HouseGatewayInformation> links) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		List<Integer> successList = new ArrayList<Integer>();
		if (links != null && links.size() > 0) {
			for (HouseGatewayInformation link : links) {
				try {
					HouseGatewayInformation dto = houseLinkMapper.findByLinkIdOrLinkNo(link.getGatewayId(), link.getLinkNo());
					if ((dto.getCzlx() == 1 && dto.getDealFlag() == 0)) {// 新增未上报
						// 仅删除链路信息
						int flag = houseLinkMapper.deleteByLinkIdOrLinkNo(link.getGatewayId(), link.getLinkNo());
						if (flag <= 0) {
							result.setResultMsg("删除记录失败");
						}
					} else {
						// 1.链路的状态变更为删除未上报
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("linkId", link.getGatewayId());
						map.put("czlx", 3);// 删除
						map.put("dealFlag", 0);// 未上报
						houseLinkMapper.updateLinkStatusByLinkId(map);

						// 2.判定机房主体状态信息，并同时判断是否需要写机房缓存表
						dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					}
					successList.add(ResultDto.ResultCodeEnum.SUCCESS.getCode());
				} catch (Exception e) {
					logger.error("delete linkid:" + link.getGatewayId() + ", linkNo: " + link.getLinkNo() + " ERROR", e);
					result.setResultMsg("删除记录失败");
				}
			}
			if (!successList.contains(ResultDto.ResultCodeEnum.ERROR.getCode())) {
				//全部校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
			} else {
				//部分校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
			}
			return result;
		} else {
			return result;
		}
	}

	/**
	 * 导入机房机架信息
	 * @param dto
	 * @return
	 */
	public List<ResultDto> importHouseLinkData(List<HouseGatewayInformationDTO> dtoList){
		List<ResultDto> resultDtoList = Lists.newArrayList();
		if (dtoList==null||dtoList.isEmpty()){
			return resultDtoList;
		}
		for (int i=0;i<dtoList.size();i++){
			List<HouseGatewayInformationDTO> houseGatewayInformationDTOList = Lists.newArrayList();
			ResultDto resultDto ;
			HouseGatewayInformationDTO dto = dtoList.get(i);
			HouseGatewayInformation result = null;
			if (!StringUtils.isEmpty(dto.getLinkNo())){
				result = houseLinkMapper.findByLinkNo(dto.getLinkNo());
			}
			if (result==null){
				houseGatewayInformationDTOList.add(dto);
				resultDto =  batchInsertHouseLinkInfos(houseGatewayInformationDTOList,dto.getHouseId(),true);
			} else {
				dto.setGatewayId(result.getGatewayId());
				dto.setOperateType(2);
				houseGatewayInformationDTOList.add(dto);
				resultDto = batchUpdate(houseGatewayInformationDTOList);
			}
			resultDtoList.add(resultDto);
		}

		return resultDtoList;
	}

	private ResultDto insertData(HouseGatewayInformationDTO dto){
		ResultDto resultDto = validateResult(dto);
		if (resultDto.getResultCode()==ResultDto.ResultCodeEnum.ERROR.getCode()){
			return resultDto;
		}
		int result = insert(dto);
		if (result>0){
			return getSuccessResult();
		} else {
			return getErrorResult("插入失败");
		}
	}

	private ResultDto updateData(HouseGatewayInformationDTO dto){
		ResultDto resultDto = validateResult(dto);
		if (resultDto.getResultCode()==ResultDto.ResultCodeEnum.ERROR.getCode()){
			return resultDto;
		}
		int result = update(dto);
		if (result>0){
			return getSuccessResult();
		} else {
			return getErrorResult("更新失败");
		}
	}
}
