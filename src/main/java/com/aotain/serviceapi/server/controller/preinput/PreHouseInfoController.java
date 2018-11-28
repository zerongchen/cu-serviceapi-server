package com.aotain.serviceapi.server.controller.preinput;

import java.util.*;

import com.aotain.common.utils.redis.ApproveIdUtil;
import com.aotain.common.utils.redis.DataApproveUtil;
import com.aotain.cu.serviceapi.dto.*;
import com.aotain.cu.serviceapi.model.*;

import com.aotain.serviceapi.server.constant.DealFlagEnum;
import com.aotain.serviceapi.server.service.CommonUtilService;
import com.aotain.serviceapi.server.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aotain.serviceapi.server.controller.CommonController;
import com.aotain.serviceapi.server.service.preinput.impl.HouseLinkServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.IHouseInformationServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.PreHouseFrameServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.PreHouseIpsegServiceImpl;
import com.aotain.serviceapi.server.util.IpUtil;
import com.aotain.serviceapi.server.validate.IHouseFrameValidator;
import com.aotain.serviceapi.server.validate.IHouseIpSegmentValidator;
import com.aotain.serviceapi.server.validate.IHouseLinkValidator;
import com.aotain.serviceapi.server.validate.IHousePrincipalValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value="/pre/house")
@Api(value="PreHouseInfoController",description="预录入模块-机房信息")
public class PreHouseInfoController extends CommonController{

	private static final Logger logger = LoggerFactory.getLogger(PreHouseInfoController.class);

	@Autowired
	@Qualifier("housePrincipalValidatorImpl")
	private IHousePrincipalValidator housePrincipalValidator;

	@Autowired
	@Qualifier("houseFrameValidatorImpl")
	private IHouseFrameValidator houseFrameValidator;

	@Autowired
	@Qualifier("houseIpSegmentValidatorImpl")
	private IHouseIpSegmentValidator houseIpSegmentValidator;

	@Autowired
	@Qualifier("houseLinkValidatorImpl")
	private IHouseLinkValidator houseLinkValidator;

	@Autowired
	@Qualifier(value = "houseLinkServiceImpl")
	private HouseLinkServiceImpl houseLinkService;
	
	@Autowired
	@Qualifier(value = "preHouseFrameServiceImpl")
	private PreHouseFrameServiceImpl preHouseFrameService;
	
	@Autowired
	@Qualifier(value = "preHouseIpsegServiceImpl")
	private PreHouseIpsegServiceImpl preHouseIpsegService;

	@Autowired
	private IHouseInformationServiceImpl iHouseInformationServiceImpl;

	@Autowired
	private CommonUtilService commonUtilService;

	@ApiOperation(value="查询机房数据", notes="根据查询条件分页查询机房数据")
	@RequestMapping(value = "listHouseInfo", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseInformationDTO> listHouseInfo(@RequestBody @ApiParam(required = true, name = "dto", value="机房查询实体HouseInformationDTO")HouseInformationDTO dto) {
		return iHouseInformationServiceImpl.listHouseInfo(dto);
	}

	@ApiOperation(value="查询预审结果", notes="根据ID查询机房预审结果")
	@RequestMapping(value = "findCheckResult", method = { RequestMethod.POST })
	@ResponseBody
	public List<ApproveResultDto> findCheckResult(@RequestBody @ApiParam(required = true, name = "approveId", value="审核结果ID")String approveId) {
		return iHouseInformationServiceImpl.findCheckResultById(approveId);
	}
	
	@ApiOperation(value="统计机房数据记录数", notes="根据查询条件统计机房数据记录数")
	@RequestMapping(value = "countHouseInfo", method = { RequestMethod.POST })
	@ResponseBody
	public long countHouseInfo(@ApiParam(required = true, name = "dto", value="机房查询实体HouseInformationDTO")HouseInformationDTO dto) {
		return 0;
	}
	
	@ApiOperation(value="新增机房数据", notes="根据HouseInformation对象新增完整机房数据")
	@RequestMapping(value="insert", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> insert(@RequestBody @ApiParam(required = true, name = "houseInformation", value = "机房实体") List<HouseInformation> list) {
			List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (HouseInformation house : list) {
				resultList.add(iHouseInformationServiceImpl.insertData(house));
			}
		} else {
			resultList.add(getErrorResult("传入的待插入的数据集合为空"));
		}
		return resultList;
	}
	
	@ApiOperation(value="更新机房数据", notes="根据HouseInformation对象创建机房")
	@RequestMapping(value="update", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> update(@ApiParam(required = true, name = "houseInformation", value="机房实体")@RequestBody List<HouseInformationDTO> list){
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (HouseInformationDTO house : list) {
				resultList.add(iHouseInformationServiceImpl.updateData(house));
			}
		} else {
			resultList.add(getErrorResult("传入的待更新的数据集合为空"));
		}
		return resultList;	
	}
	
	@ApiOperation(value = "删除机房数据", notes = "根据机房ID删除机房")
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public ResultDto delete(@RequestBody @ApiParam(required = true, name = "deleteList", value = "删除的机房集合") List<HouseInformationDTO> deleteList) {
		return iHouseInformationServiceImpl.batchDeleteHouseInfos(deleteList);
	}
	
	@ApiOperation(value="校验机房数据", notes="校验机房数据是否正确")
	@RequestMapping(value = "validate", method = { RequestMethod.POST})
	@ResponseBody 
	public AjaxValidationResult validate(@ApiParam(required = true, name = "dto", value="机房实体")@RequestBody HouseInformationDTO dto, BindingResult bindingResult){
		return housePrincipalValidator.preValidate(dto);
	}
	
	@ApiOperation(value="获取机房明细信息", notes="根据机房ID获取机房明细信息，包括机架、ip、链路明细信息")
	@RequestMapping(value="getDetail", method = {  RequestMethod.POST })
	@ResponseBody
	public HouseInformation getDetail(@RequestBody @ApiParam(required = true, name = "houseId", value= "机房ID")String houseId){
		HouseInformationDTO dto = new HouseInformationDTO();
		dto.setHouseId(Long.valueOf(houseId));
		return iHouseInformationServiceImpl.getHouseInfoById(dto);
	}
	
	@ApiOperation(value="预审机房信息", notes="根据机房ID预审机房的相应信息")
	@RequestMapping(value="approve", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto approve(@RequestBody @ApiParam(required = true, name = "houseId", value= "机房ID")String houseId) {
		ResultDto result=iHouseInformationServiceImpl.approve(houseId);

		//写redis信息
		Long approveId=ApproveIdUtil.getInstance().getApproveId();
		DataApproveUtil.getInstance().setDataApprove("2"+houseId,approveId);
		//写流水表信息
		WaitApproveProcess ass=new WaitApproveProcess();
		ass.setApproveId(approveId);
		ass.setType(2);
		ass.setDataId(Long.valueOf(houseId));
		if(result.getResultCode()==0){
			ass.setDealFlag(DealFlagEnum.STATUS2.getCode());
		}else{
			ass.setDealFlag(DealFlagEnum.STATUS1.getCode());
		}
		HouseInformationDTO dto = new HouseInformationDTO();
		dto.setHouseId(Long.valueOf(houseId));
		dto = iHouseInformationServiceImpl.getHouseInfoById(dto);
		ass.setWarnData(dto.getVerificationResult());
		ass.setDealTime(DateUtils.getCurrentyyyMMddHHmmss());
		Date date=new Date();
		ass.setCreateTime(date);
		ass.setUpdateTime(date);
		int i=commonUtilService.insertApproveProcess(ass);
		if(i<=0){
			logger.error("house approve insertApproveProcess error,houseId="+houseId);
		}

		return result;
	}

	@ApiOperation(value="撤销预审机房信息", notes="根据机房ID撤销对应的预审机房信息")
	@RequestMapping(value="revertApprove", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto revertApprove(@RequestBody @ApiParam(required = true, name = "houseId", value= "机房ID")String houseId) {
		return iHouseInformationServiceImpl.revertApprove(houseId);
	}
	
	/**
	 * 机房机架信息的操作API
	 */
	@ApiOperation(value="新增机房机架信息", notes="根据机房机架数据对象集合新增机房机架信息")
	@RequestMapping(value="/rack/insert", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto insertRack(@RequestBody @ApiParam(required = true, name = "racks", value="机房机架数据对象集合")List<HouseFrameInformationDTO> racks) {
		return preHouseFrameService.batchInsertFrameInfos(racks, null, false);
	}
	
	@ApiOperation(value="修改机房机架信息", notes="根据机房机架数据对象修改机房的机架信息")
	@RequestMapping(value="/rack/update", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto updateRack(@RequestBody @ApiParam(required = true, name = "racks", value="机房机架数据对象")List<HouseFrameInformationDTO> racks) {
		return preHouseFrameService.batchUpdate(racks);
	}
	
	@ApiOperation(value="删除机架信息", notes="根据机架ID集合数据删除机房机架信息")
	@RequestMapping(value="/rack/delete", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto deleteRack(@ApiParam(required = true, name = "racks", value= "机房机架数据对象")@RequestBody  List<HouseFrameInformationDTO> racks) {
		return preHouseFrameService.batchDelete(racks);
	}
	
	@ApiOperation(value="查询机架信息", notes="根据机房机架对象查询机房机架信息")
	@RequestMapping(value="/rack/query", method = {  RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseFrameInformationDTO> queryRack(@RequestBody @ApiParam(required = true, name = "rack", value= "机房机架对象")HouseFrameInformationDTO rack) {
		return iHouseInformationServiceImpl.getIndexHouseFrame(rack);
	}
	
	@ApiOperation(value="统计机架信息", notes="根据机房机架对象统计机房机架信息")
	@RequestMapping(value="/rack/count", method = {  RequestMethod.POST })
	@ResponseBody
	public long countRack(@RequestBody @ApiParam(required = true, name = "rack", value= "机房机架对象")HouseFrameInformationDTO rack) {
		return 0;
	}
	
	@ApiOperation(value="校验机房机架数据", notes="校验机房机架数据是否正确")
	@RequestMapping(value = "/rack/validate", method = { RequestMethod.POST})
	@ResponseBody 
	public ResultDto validateRack(@ApiParam(required = true, name = "dto", value="机房机架实体")@RequestBody List<HouseFrameInformationDTO> dto){
		ResultDto result = new ResultDto();
		result.setResultCode(0);
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>();
		
		//数据重复性校验
		Map<String,Object> frameMap = new HashMap<String,Object>();
		for (int i = 0; i < dto.size(); i++) {
			HouseFrameInformationDTO obj = dto.get(i);
			if(frameMap.containsKey(obj.getFrameName())){
				result = new ResultDto();
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
				result.setResultMsg("存在重复机架名称："+obj.getFrameName());
				return result;
			}else{
				frameMap.put(obj.getFrameName(), obj.getFrameName());
			}
			if(obj.getUserFrameList()!=null&&obj.getUserFrameList().size()>0){
				for(HouseUserFrameInformation uFdto:obj.getUserFrameList()){
					if(uFdto.getUserName()!=null&&uFdto.getUserName()!=""){
						String key = obj.getFrameName()+uFdto.getUserName();
						if(frameMap.containsKey(key)){
							result = new ResultDto();
							result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
							result.setResultMsg("机架'"+obj.getFrameName()+"'重复录入单位名称："+uFdto.getUserName());
							return result;
						}else{
							frameMap.put(key,key);
						}
					}
				}
			}
		}
		
		
		for (int i = 0; i < dto.size(); i++) {
			AjaxValidationResult validateResult = houseFrameValidator.preValidate(dto.get(i));
			validateResultMap.put(String.valueOf(i), validateResult);
			if(!validateResult.getErrorsArgsMap().isEmpty()){
				result.setResultCode(1);
			}
		}
		result.setAjaxValidationResultMap(validateResultMap);
		return result;
	}
	
	/**
	 * 机房链路信息的操作API
	 */
	@ApiOperation(value="新增机房链路信息", notes="根据机房链路数据对象集合新增机房链路信息")
	@RequestMapping(value="/link/insert", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto insertLink(@ApiParam(required = true, name = "links", value="机房链路数据对象集合")@RequestBody List<HouseGatewayInformationDTO> links) {
		return houseLinkService.batchInsertHouseLinkInfos(links, null, false);
	}
	
	@ApiOperation(value="更新机房链路信息", notes="根据机房链路数据对象集合更新机房链路信息")
	@RequestMapping(value="/link/update", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto updateLink(@ApiParam(required = true, name = "links", value="机房链路数据对象集合")@RequestBody List<HouseGatewayInformationDTO> links) {
		return houseLinkService.batchUpdate(links);
	}
	
	@ApiOperation(value = "删除机房链路信息", notes = "根据机房链路ID数据集合删除机房链路信息")
	@RequestMapping(value = "/link/delete", method = { RequestMethod.POST })
	@ResponseBody
	public ResultDto deleteLink(@ApiParam(required = true, name = "links", value = "机房链路数据对象集合")@RequestBody List<HouseGatewayInformationDTO> links) {
		return houseLinkService.batchDelete(links);
	}
	
	@ApiOperation(value="查询机房链路信息", notes="根据机房链路对象查询机房链路信息")
	@RequestMapping(value="/link/query", method = {  RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseGatewayInformationDTO> queryLink(@RequestBody @ApiParam(required = true, name = "link", value= "机房链路对象")HouseGatewayInformationDTO link) {
		return iHouseInformationServiceImpl.getIndexLink(link);
	}
	
	@ApiOperation(value="统计机房链路信息", notes="根据机房链路对象统计机房链路信息")
	@RequestMapping(value="/link/count", method = {  RequestMethod.POST })
	@ResponseBody
	public long countLink(@ApiParam(required = true, name = "link", value= "机房链路对象")HouseGatewayInformationDTO link) {
		return 0;
	}
	
	@ApiOperation(value="校验机房链路数据", notes="校验机房链路数据是否正确")
	@RequestMapping(value = "/link/validate", method = { RequestMethod.POST})
	@ResponseBody 
	public ResultDto validateLink(@ApiParam(required = true, name = "dto", value="机房链路实体")@RequestBody List<HouseGatewayInformationDTO> dto, BindingResult bindingResult){
		ResultDto result = new ResultDto();
		result.setResultCode(0);
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < dto.size(); i++) {
			//重复性校验
			String linkNo = dto.get(i).getLinkNo();
			if(map.containsKey(linkNo)){
				result =new ResultDto();
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
				result.setResultMsg(linkNo);
				return result;
			}else{
				map.put(linkNo, linkNo);
			}
			
			AjaxValidationResult validateResult = houseLinkValidator.preValidate(dto.get(i));
			validateResultMap.put(String.valueOf(i), validateResult);
			if(!validateResult.getErrorsArgsMap().isEmpty()){
				result.setResultCode(1);
			}
		}
		result.setAjaxValidationResultMap(validateResultMap);
		return result;
	}
	
	/**
	 * 机房IP地址段信息的操作API
	 */
	@ApiOperation(value="新增机房IP地址段信息", notes="根据机房IP地址段对象新增机房IP地址段信息")
	@RequestMapping(value="/ipSegment/insert", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto insertIPSegment(@RequestBody @ApiParam(required = true, name = "ipSegments", value="机房IP地址段数据对象集合")List<HouseIPSegmentInforDTO> ipSegments) {
		return preHouseIpsegService.batchInsertHouseIpsegInfos(ipSegments, null, false);
	}
	
	@ApiOperation(value="更新机房IP地址段信息", notes="根据机房IP地址段对象更新机房IP地址段信息")
	@RequestMapping(value="/ipSegment/update", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto updateIPSegment(@RequestBody @ApiParam(required = true, name = "ipSegments", value="机房IP地址段数据对象集合")List<HouseIPSegmentInforDTO> ipSegments) {
		return preHouseIpsegService.batchUpdate(ipSegments);
	}
	
	@ApiOperation(value="删除机房IP地址段信息", notes="根据机房IP地址段对象删除机房IP地址段信息")
	@RequestMapping(value="/ipSegment/delete", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto deleteIPSegment(@ApiParam(required = true, name = "ipSegments", value= "机房IP地址段数据对象集合")@RequestBody List<HouseIPSegmentInforDTO> ipSegments) {
		return preHouseIpsegService.batchDelete(ipSegments);
	}
	
	@ApiOperation(value="查询机房IP地址段信息", notes="根据机房IP地址段对象删除机房IP地址段信息")
	@RequestMapping(value="/ipSegment/query", method = {  RequestMethod.POST })
	@ResponseBody
	public PageResult<HouseIPSegmentInforDTO> queryIPSegment(@RequestBody @ApiParam(required = true, name = "ipSegment", value= "机房IP地址段对象")HouseIPSegmentInforDTO ipSegment) {
		return iHouseInformationServiceImpl.getIndexIpSegment(ipSegment);
	}
	
	@ApiOperation(value="统计机房IP地址段信息", notes="根据机房IP地址段对象统计机房IP地址段信息")
	@RequestMapping(value="/ipSegment/count", method = {  RequestMethod.POST })
	@ResponseBody
	public long countIPSegment(@ApiParam(required = true, name = "ipSegment", value= "机房IP地址段对象")HouseIPSegmentInforDTO ipSegment) {
		return 0;
	}
	
	@ApiOperation(value="校验机房IP地址段数据", notes="校验机房IP地址段数据是否正确")
	@RequestMapping(value = "/ipSegment/validate", method = { RequestMethod.POST})
	@ResponseBody 
	public ResultDto validateIPSegment(@ApiParam(required = true, name = "dto", value="机房IP地址段实体")@RequestBody List<HouseIPSegmentInforDTO> dto){
		ResultDto result = new ResultDto();
		result.setResultCode(0);
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>();
		
		//IP数据重复性校验
		List<HouseIPSegmentInformation> ipList = new ArrayList<HouseIPSegmentInformation>();
		for (int i = 0; i < dto.size(); i++) {
			HouseIPSegmentInforDTO po = dto.get(i);
			if(IpUtil.isIpAddress(po.getStartIP())&&IpUtil.isIpAddress(po.getEndIP())&&!IpUtil.isStartIPOverEndIp(po.getStartIP(), po.getEndIP())){
				ipList.add(po);
			}
		}
		for(int i=0;i<ipList.size()-1;++i){
			HouseIPSegmentInformation preDto = ipList.get(i);
			for(int j=i+1;j<ipList.size();++j){
				HouseIPSegmentInformation afterDto = ipList.get(j);
				if(!(IpUtil.isStartIPOverEndIp(afterDto.getStartIP(),preDto.getEndIP())||IpUtil.isStartIPOverEndIp(preDto.getStartIP(),afterDto.getEndIP()))){
					//前者IP与后者当不满足起始IP比终止IP大或者终止IP比起始IP小时为重复情况
					result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
					result.setResultMsg(afterDto.getStartIP()+"-"+afterDto.getEndIP());
					return result;
				}
			}
		}
		for (int i = 0; i < dto.size(); i++) {
			AjaxValidationResult validateResult = houseIpSegmentValidator.preValidate(dto.get(i));
			if(dto.get(i).getOutIndex()!=null){
				validateResult.setOutIndex(dto.get(i).getOutIndex());
			}
			if(dto.get(i).getInnerIndex()!=null){
				validateResult.setInnerIndex(dto.get(i).getInnerIndex());
			}
			validateResultMap.put(String.valueOf(i), validateResult);
			if(!validateResult.getErrorsArgsMap().isEmpty()){
				result.setResultCode(1);
			}
		}
		result.setAjaxValidationResultMap(validateResultMap);
		return result;
	}

	@ApiOperation(value="导入机房完整信息", notes="导入机房信息")
	@RequestMapping(value="/importHouse", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> importHouse( @RequestBody @ApiParam(required = true, name = "house", value="机房信息实体")List<HouseInformationDTO> houseInformationList ){

		try {
			return iHouseInformationServiceImpl.importHouseData(houseInformationList);
		}catch (Exception e){
			logger.error("import house error",e);
		}
		return null;
	}

	@ApiOperation(value="同步机房信息", notes="同步机房信息")
	@RequestMapping(value="/synchHouse", method = {  RequestMethod.POST })
	@ResponseBody
	@Deprecated
	public ResultDto synchHouse(@RequestBody @ApiParam(required = true, name = "house", value="机房信息实体")BaseModel baseModel ){
		try {
			return iHouseInformationServiceImpl.synchHouse(baseModel);
		}catch (Exception e){
			logger.error(" synchronized house data to auth error",e);
		}
		return null;
	}

	@ApiOperation(value="导入机房机架信息", notes="导入机房机架信息")
	@RequestMapping(value="/importHouseFrame", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> importHouseFrame( @RequestBody @ApiParam(required = true, name = "houseFrame", value="机房机架信息实体")List<HouseFrameInformationDTO> houseFrameInformationDTOList ){

		try {
			return preHouseFrameService.importHouseFrameData(houseFrameInformationDTOList);
		}catch (Exception e){
			logger.error("import houseFrame error",e);
		}
		return null;
	}

	@ApiOperation(value="导入机房链路信息", notes="导入机房链路信息")
	@RequestMapping(value="/importHouseLink", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> importHouseLink( @RequestBody @ApiParam(required = true, name = "houseLink", value="机房链路信息实体")List<HouseGatewayInformationDTO> houseGatewayInformationDTOList ){

		try {
			return houseLinkService.importHouseLinkData(houseGatewayInformationDTOList);
		}catch (Exception e){
			logger.error("import houseLink error",e);
		}
		return null;
	}

	@ApiOperation(value="导入机房IP地址段信息", notes="导入机房IP地址段信息")
	@RequestMapping(value="/importHouseIpSeg", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> importHouseIpSeg( @RequestBody @ApiParam(required = true, name = "houseIpSeg", value="机房IP地址段信息实体")List<HouseIPSegmentInforDTO> houseIPSegmentInformationDTOList ){

		try {
			return preHouseIpsegService.importHouseIpData(houseIPSegmentInformationDTOList);
		}catch (Exception e){
			logger.error("import houseIpSeg error",e);
		}
		return null;
	}

}
