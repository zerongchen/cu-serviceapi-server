package com.aotain.serviceapi.server.controller.report;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.utils.ResponseConstant;
import com.aotain.serviceapi.server.controller.CommonController;
import com.aotain.serviceapi.server.service.report.RptHouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/report/house")
@Api(value = "HouseInfoController", description = "上报模块-机房信息")
public class HouseInfoController extends CommonController {

	private Logger log = LoggerFactory.getLogger(HouseInfoController.class);

	@Autowired
	private RptHouseService rptHouseService;

	@ApiOperation(value = "查询机房数据", notes = "根据查询条件分页查询机房数据")
	@RequestMapping(value = "listHouseInfo", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseInformationDTO> listHouseInfo(
			@RequestBody @ApiParam(required = true, name = "dto", value = "机房查询实体HouseInformationDTO") HouseInformationDTO dto) {
		PageResult<HouseInformationDTO> result = new PageResult<HouseInformationDTO>();
		try {
			result = rptHouseService.listHouseInfo(dto);
		} catch (Exception e) {
			log.error("HouseInfoController listHouseInfo error ", e);
		}
		return result;
	}

	@ApiOperation(value = "获取机房明细信息", notes = "根据机房ID获取明细信息")
	@RequestMapping(value = "getDetail", method = { RequestMethod.POST })
	@ResponseBody
	public HouseInformationDTO getDetail(
			@RequestParam @ApiParam(required = true, name = "houseId", value = "机房ID") String houseId) {
		HouseInformationDTO dto = new HouseInformationDTO();
		try {
			dto.setHouseId(Long.valueOf(houseId));
			dto = rptHouseService.getHouseInfoById(dto);
		} catch (Exception e) {
			log.error("HouseInfoController getDetail error ", e);
		}
		return dto;
	}

	@ApiOperation(value = "查询机房IP地址段信息", notes = "根据机房IP地址段对象删除机房IP地址段信息")
	@RequestMapping(value = "/ipSegment/query", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseIPSegmentInforDTO> queryIPSegment(
			@RequestBody @ApiParam(required = true, name = "ipSegment", value = "机房IP地址段对象") HouseIPSegmentInforDTO ipSegment) {
		PageResult<HouseIPSegmentInforDTO> result = new PageResult<HouseIPSegmentInforDTO>();
		try {
			result = rptHouseService.getIndexIpSegment(ipSegment);
		} catch (Exception e) {
			log.error("HouseInfoController queryIPSegment error ", e);
		}
		return result;
	}

	@ApiOperation(value = "查询机架信息", notes = "根据机房机架对象查询机房机架信息")
	@RequestMapping(value = "/rack/query", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseFrameInformationDTO> queryRack(
			@RequestBody @ApiParam(required = true, name = "rack", value = "机房机架对象") HouseFrameInformationDTO rack) {
		PageResult<HouseFrameInformationDTO> result = new PageResult<HouseFrameInformationDTO>();
		try {
			result = rptHouseService.getIndexHouseFrame(rack);
		} catch (Exception e) {
			log.error("HouseInfoController queryRack error ", e);
		}
		return result;
	}

	@ApiOperation(value = "查询机房链路信息", notes = "根据机房链路对象查询机房链路信息")
	@RequestMapping(value = "/link/query", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseGatewayInformationDTO> queryLink(
			@RequestBody @ApiParam(required = true, name = "link", value = "机房链路对象") HouseGatewayInformationDTO link) {
		PageResult<HouseGatewayInformationDTO> result = new PageResult<HouseGatewayInformationDTO>();
		try {
			result = rptHouseService.getIndexLink(link);
		} catch (Exception e) {
			log.error("HouseInfoController queryLink error ", e);
		}
		return result;
	}

	//
	// @ApiOperation(value="统计机房数据记录数", notes="根据查询条件统计机房数据记录数")
	// @ApiImplicitParam(name = "dto", value = "机房查询实体HouseInformationDTO",
	// required = true, dataType = "HouseInformationDTO")
	// @RequestMapping(value = "countHouseInfo", method = { RequestMethod.POST
	// })
	// @ResponseBody
	// public int countHouseInfo(HouseInformationDTO dto) {
	// return 0;
	// }
	//
	// @ApiOperation(value="校验机房数据", notes="校验机房数据是否正确")
	// @RequestMapping(value = "validate", method = { RequestMethod.POST})
	// @ResponseBody
	// public AjaxValidationResult validate(@ApiParam(required =
	// true,value="机房实体")HouseInformation dto,
	// BindingResult bindingResult){
	// return null;
	// }

	@ApiOperation(value = "新增完整机房数据，包括机架，ip，链路明细信息", notes = "根据对象新增完整机房数据")
	@RequestMapping(value = "insert", method = { RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> insert(@RequestBody @ApiParam(required = true, value = "机房实体") List<HouseInformation> list) {
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (HouseInformation houseInformation : list) {
				ResultDto result = new ResultDto();
				try {
					result = rptHouseService.insert(houseInformation);
				} catch (Exception e) {
					result.setResultCode(ResponseConstant.RESPONSE_ERROR);
					log.error("house insert error ", e);
				}
				resultList.add(result);
			}
		} else {
			resultList.add(getErrorResult("传入的待插入的数据集合为空"));
		}
		return resultList;
	}

	@ApiOperation(value = "更新机房数据", notes = "根据对象更新机房")
	@RequestMapping(value = "update", method = { RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> update(@RequestBody @ApiParam(required = true, value = "机房实体") List<HouseInformation> list) {
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (HouseInformation houseInformation : list) {
				ResultDto result = new ResultDto();
				try {
					result = rptHouseService.update(houseInformation);
				} catch (Exception e) {
					result.setResultCode(ResponseConstant.RESPONSE_ERROR);
					log.error("house update error ", e);
				}
				resultList.add(result);
			}
		} else {
			resultList.add(getErrorResult("传入的待更新的数据集合为空"));
		}
		return resultList;
	}

	@ApiOperation(value = "删除机房数据", notes = "根据机房ID删除机房")
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> delete(@RequestBody @ApiParam(required = true, value = "删除机房数据对象") List<Integer> list) {
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (Integer houseId : list) {
				ResultDto result = new ResultDto();
				try {
					result = rptHouseService.delete(houseId);
				} catch (Exception e) {
					result.setResultCode(ResponseConstant.RESPONSE_ERROR);
					log.error("house delete error ", e);
				}
				resultList.add(result);
			}
		} else {
			resultList.add(getErrorResult("传入的待删除的数据集合为空"));
		}
		return resultList;
	}

	// @ApiOperation(value="获取机房明细信息", notes="根据经营者ID获取机房明细信息，包括机架，ip，链路明细信息")
	// @RequestMapping(value="getDetail", method = { RequestMethod.POST })
	// @ResponseBody
	// public IdcInformation getDetail(@ApiParam(required = true,value=
	// "机房ID")String houseId){
	// return null;
	// }
	//
	// @ApiOperation(value="加锁机房", notes="根据经营者ID加锁经营者")
	// @RequestMapping(value="lock", method = { RequestMethod.POST })
	// @ResponseBody
	// public ResultDto lock(@ApiParam(required = true,value= "机房ID")String
	// houseId){
	// return null;
	// }
	//
	// @ApiOperation(value="解锁机房", notes="根据经营者ID解锁经营者")
	// @RequestMapping(value="unlock", method = { RequestMethod.POST })
	// @ResponseBody
	// public ResultDto unlock(@ApiParam(required = true,value= "机房ID")String
	// houseId){
	// return null;
	// }
	//
	// @ApiOperation(value="查询机房机架数据", notes="根据查询条件分页查询机房机架数据列表")
	// @ApiImplicitParam(name = "dto", value =
	// "机房机架查询实体HouseFrameInformationDTO", required = true, dataType =
	// "HouseFrameInformationDTO")
	// @RequestMapping(value = "listHouseFrameInformation", method = {
	// RequestMethod.POST })
	// @ResponseBody
	// public List<HouseFrameInformationDTO>
	// listHouseFrameInformation(HouseFrameInformationDTO dto) {
	// return null;
	// }
	//
	// @ApiOperation(value="统计机房机架数据记录数", notes="根据查询条件统计机房机架数据记录数")
	// @ApiImplicitParam(name = "dto", value =
	// "机房机架查询实体HouseFrameInformationDTO", required = true, dataType =
	// "HouseFrameInformationDTO")
	// @RequestMapping(value = "countHouseFrameInformation", method = {
	// RequestMethod.POST })
	// @ResponseBody
	// public int countHouseFrameInformation(HouseFrameInformationDTO dto) {
	// return 0;
	// }
	//
	// @ApiOperation(value="查询机房链路数据", notes="根据查询条件分页查询机房链路数据列表")
	// @ApiImplicitParam(name = "dto", value =
	// "机房链路查询实体HouseGatewayInformationDTO", required = true, dataType =
	// "HouseGatewayInformationDTO")
	// @RequestMapping(value = "ListHouseGatewayInformation", method = {
	// RequestMethod.POST })
	// @ResponseBody
	// public List<HouseGatewayInformationDTO>
	// ListHouseGatewayInformation(HouseGatewayInformationDTO dto) {
	// return null;
	// }
	//
	// @ApiOperation(value="统计经营者数据记录数", notes="根据查询条件统计经营者数据记录数")
	// @ApiImplicitParam(name = "idcInformationDto", value =
	// "经营者详细实体IDCInformationDTO", required = true, dataType =
	// "IDCInformationDTO")
	// @RequestMapping(value = "countHouseGatewayInformation", method = {
	// RequestMethod.POST })
	// @ResponseBody
	// public int countHouseGatewayInformation(HouseGatewayInformationDTO dto) {
	// return 0;
	// }
	//
	// @ApiOperation(value="查询机房IP地址段数据", notes="根据查询条件分页查询机房IP地址段数据列表")
	// @ApiImplicitParam(name = "dto", value = "机房IP地址段查询实体IDCInformationDTO",
	// required = true, dataType = "HouseIPSegmentInforDTO")
	// @RequestMapping(value =
	// "listHouseIPSegmentInfo",method=RequestMethod.POST)
	// @ResponseBody
	// public List<HouseIPSegmentInforDTO>
	// listHouseIPSegmentInfo(HouseIPSegmentInforDTO dto) {
	// return null;
	// }
	//
	// @ApiOperation(value="统计机房IP地址段数据记录数", notes="根据查询条件机房IP地址段数据记录数")
	// @ApiImplicitParam(name = "dto", value = "机房IP地址段查询实体IDCInformationDTO",
	// required = true, dataType = "HouseIPSegmentInforDTO")
	// @RequestMapping(value =
	// "countHouseIPSegmentInfo",method=RequestMethod.POST)
	// @ResponseBody
	// public int countHouseIPSegmentInfo(HouseIPSegmentInforDTO dto) {
	// return 0;
	// }

}
