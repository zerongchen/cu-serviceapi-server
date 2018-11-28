package com.aotain.serviceapi.server.controller.report;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.controller.ExceptionHandle;
import com.aotain.serviceapi.server.service.report.IdcInfoService;
import com.aotain.serviceapi.server.service.report.IdcInfoServiceImpl;
import com.aotain.serviceapi.server.validate.IdcInformationValidator;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report/idc")
@Api(value="IdcInfoController",description="上报模块-经营者")
public class IdcInfoController extends ExceptionHandle{

	private Logger logger = LoggerFactory.getLogger(IdcInfoServiceImpl.class);

	@Autowired
	private IdcInfoService idcInfoService;

	@Autowired
	private IdcInformationValidator idcInformationValidator;
	
//	@ApiOperation(value="查询经营者数据", notes="根据查询条件分页查询经营者数据列表")
//	@ApiImplicitParam(name = "idcInformationDto", value = "经营者详细实体IDCInformationDTO", required = true, dataType = "IDCInformationDTO")
//	@RequestMapping(value = "list", method = RequestMethod.GET)
//	@ResponseBody
//	public List<IdcInformation> listIdcInformation(IDCInformationDTO idcInformationDto) {
//		return Arrays.asList(new IdcInformation());
//	}
//	
//	@ApiOperation(value="统计经营者数据记录数", notes="根据查询条件统计经营者数据记录数")
//	@ApiImplicitParam(name = "idcInformationDto", value = "经营者详细实体IDCInformationDTO", required = true, dataType = "IDCInformationDTO")
//	@RequestMapping(value = "count", method = RequestMethod.POST)
//	@ResponseBody
//	public int countIdcInformation(IDCInformationDTO idcInformationDto) {
//		return 0;
//	}
	
	@ApiOperation(value="创建经营者", notes="根据IDCInformation对象创建经营者")
	@RequestMapping(value="insert" , method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto insert(@RequestBody @ApiParam(required = true,value="经营者实体") IdcInformation idcInformation){
		try{
			return idcInfoService.add(idcInformation);
		}catch (Exception e){
			logger.error("idc info insert error ",e);
			return handleError();
		}
	}
	
	@ApiOperation(value="更新经营者信息", notes="根据IDCInformation对象更新经营者信息")
	@RequestMapping(value="update", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto update(@RequestBody @ApiParam(required = true,value="经营者实体")IdcInformation idcInformation){
		try{
			return idcInfoService.update(idcInformation);
		}catch (Exception e){
			logger.error("idc info update error ",e);
			return handleError();
		}

	}
	
	@ApiOperation(value="删除经营者", notes="根据经营者ID删除经营者")
	@ApiImplicitParam(name = "jyzId", value = "经营者ID", required = true, dataType = "Integer")
	@RequestMapping(value="delete", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto delete(@RequestBody Integer jyzId){
		try {
			return idcInfoService.delete(jyzId);
		}catch (Exception e ){
			logger.error("idc info delete error ",e);
			return handleError();
		}
	}

//	@ApiOperation(value="获取经营者明细信息", notes="根据经营者ID获取经营者明细信息，包括机房，用户明细信息")
//	@ApiImplicitParam(name = "jyzId", value = "经营者ID", required = true, dataType = "String")
//	@RequestMapping(value="getDetail", method = {  RequestMethod.POST })
//	@ResponseBody
//	public IdcInformation getDetail(String jyzId){
//		return null;
//	}
	
//	@ApiOperation(value="获取经营者信息", notes="根据经营者ID获取经营者信息")
//	@ApiImplicitParam(name = "jyzId", value = "经营者ID", required = true, dataType = "String")
//	@RequestMapping(value="getInfo", method = {  RequestMethod.POST })
//	@ResponseBody
//	public IdcInformation getinfo(String jyzId){
//		return null;
//	}
	
//	@ApiOperation(value="加锁经营者", notes="根据经营者ID加锁经营者")
//	@ApiImplicitParam(name = "jyzId", value = "经营者ID", required = true, dataType = "String")
//	@RequestMapping(value="lock", method = {  RequestMethod.POST })
//	@ResponseBody
//	public ResultDto lock(String jyzId){
//		return null;
//	}
//	
//	@ApiOperation(value="解锁经营者", notes="根据经营者ID解锁经营者")
//	@ApiImplicitParam(name = "jyzId", value = "经营者ID", required = true, dataType = "String")
//	@RequestMapping(value="unlock", method = {  RequestMethod.POST })
//	@ResponseBody
//	public ResultDto unlock(String jyzId){
//		return null;
//	}
//	
	@ApiOperation(value="校验经营者", notes="根据经营者ID校验经营者")
	@RequestMapping(value="validate", method = {  RequestMethod.POST })
	@ResponseBody
	public AjaxValidationResult validate(@ApiParam(required = true,value="经营者实体")@RequestBody IDCInformationDTO idcInformation,
										 BindingResult bindingResult){
		return idcInformationValidator.preValidate(idcInformation);
	}

}
