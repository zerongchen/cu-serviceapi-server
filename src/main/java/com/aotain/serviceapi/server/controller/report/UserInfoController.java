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

import com.alibaba.fastjson.JSON;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.dto.UserVirtualInformationDTO;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.cu.utils.ResponseConstant;
import com.aotain.serviceapi.server.controller.CommonController;
import com.aotain.serviceapi.server.controller.CommonControllerWithExceptionHandler;
import com.aotain.serviceapi.server.controller.ExceptionHandle;
import com.aotain.serviceapi.server.service.report.RptUserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value="/report/user")
@Api(value="UserInfoManageController",description="上报模块-用户信息")
public class UserInfoController extends CommonControllerWithExceptionHandler {

	private Logger log = LoggerFactory.getLogger(UserInfoController.class);

	@Autowired
	private RptUserInfoService rptUserInfoService;


	@ApiOperation(value="查询用户数据", notes="根据查询条件分页查询用户数据")
	@RequestMapping(value = "listUserInfo", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<UserInformationDTO> listUserInfo(@RequestBody @ApiParam(required = true, name = "dto", value="查询实体")UserInformationDTO dto) {
		PageResult<UserInformationDTO> result = new PageResult<UserInformationDTO>();
		try {
			result = rptUserInfoService.getUserInfoList(dto);
		} catch (Exception e) {
			log.error("UserInfoController listUserInfo error ",e);
		}
		return result;
	}

	@ApiOperation(value="获取用户明细信息", notes="根据用户Id查找用户信息")
	@RequestMapping(value="getDetail", method = {  RequestMethod.POST })
	@ResponseBody
	public UserInformationDTO getDetail(@RequestParam @ApiParam(required = true, name = "userId", value= "用户ID")String userId){
		UserInformationDTO dto = new UserInformationDTO();
		try {
			dto.setUserId(Long.valueOf(userId));
			dto = rptUserInfoService.getUserInfoById(dto);
		} catch (Exception e) {
			log.error("UserInfoController getDetail error ",e);
		}
		return dto;
	}

	@ApiOperation(value="查询用户服务数据", notes="根据查询条件分页查询用户服务数据")
	@RequestMapping(value = "/service/query", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<UserServiceInformationDTO> getUserServiceList(@RequestBody @ApiParam(required = true, name = "dto", value="查询实体")UserServiceInformationDTO dto) {
		PageResult<UserServiceInformationDTO> result = new PageResult<UserServiceInformationDTO>();
		try {
			result = rptUserInfoService.getUserServiceList(dto);
		} catch (Exception e) {
			log.error("UserInfoController getUserServiceList error ",e);
		}
		return result;
	}

	@ApiOperation(value="查询用户宽带数据", notes="根据查询条件分页查询用户宽带数据")
	@RequestMapping(value = "/bandWidth/query", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<UserBandwidthInformationDTO> getUserBandWidthList(@RequestBody @ApiParam(required = true, name = "dto", value="查询实体")UserBandwidthInformationDTO dto) {
		PageResult<UserBandwidthInformationDTO> result = new PageResult<UserBandwidthInformationDTO>();
		try {
			result = rptUserInfoService.getUserBandWidthList(dto);
		} catch (Exception e) {
			log.error("UserInfoController getUserBandWidthList error ",e);
		}
		return result;
	}

	@ApiOperation(value="查询用户虚拟主机数据", notes="根据查询条件分页查询用户虚拟主机数据")
	@RequestMapping(value = "/virtual/query", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<UserVirtualInformationDTO> getUserVirtualList(@RequestBody @ApiParam(required = true, name = "dto", value="查询实体")UserVirtualInformationDTO dto) {
		PageResult<UserVirtualInformationDTO> result = new PageResult<UserVirtualInformationDTO>();
		try {
			result = rptUserInfoService.getUserVirtualList(dto);
		} catch (Exception e) {
			log.error("UserInfoController getUserVirtualList error ",e);
		}
		return result;
	}




	//*******************

	@ApiOperation(value="新增用户完整信息", notes="新增用户信息")
	@RequestMapping(value="insert", method = {  RequestMethod.POST })
	@ResponseBody
//	@HystrixCommand(fallbackMethod = "callback")
	public List<ResultDto> insert(@RequestBody @ApiParam(required = true,value="用户信息实体")List<UserInformation> list){
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (UserInformation userInformation : list) {
				ResultDto result = new ResultDto();
				try {
					result = rptUserInfoService.insert(userInformation);
				} catch (Exception e) {
					result.setResultCode(ResponseConstant.RESPONSE_ERROR);
					log.error("UserInfoController insert error : "+JSON.toJSONString(userInformation),e);
				}
				resultList.add(result);
			}
		}else {
			resultList.add(getErrorResult("传入的待新增的数据集合为空"));
		}
		return resultList;
	}
	
	@ApiOperation(value="更新用户信息", notes="更新用户信息")
	@RequestMapping(value="update", method = {  RequestMethod.POST })
	@ResponseBody
//	@HystrixCommand(fallbackMethod = "callback")
	public List<ResultDto> update(@RequestBody @ApiParam(required = true,value="用户信息实体")List<UserInformation> list){
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (UserInformation userInformation : list) {
				ResultDto result = new ResultDto();
				try {
					result = rptUserInfoService.modify(userInformation);
				} catch (Exception e) {
					result.setResultCode(ResponseConstant.RESPONSE_ERROR);
					log.error("UserInfoController update error : "+JSON.toJSONString(list),e);
				}
				resultList.add(result);
			}
		}else {
			resultList.add(getErrorResult("传入的待修改的数据集合为空"));
		}
		return resultList;
	}
	
	@ApiOperation(value="删除用户", notes="删除用户")
	@RequestMapping(value="delete", method = {  RequestMethod.POST })
	@ResponseBody
//	@HystrixCommand(fallbackMethod = "callback")
	public List<ResultDto> delete(@RequestBody @ApiParam(required = true,value="删除用户数据对象")List<Long> list){
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (Long userId : list) {
				ResultDto result = new ResultDto();
				try {
					result = rptUserInfoService.delete(userId);
				} catch (Exception e) {
					result.setResultCode(ResponseConstant.RESPONSE_ERROR);
					log.error("delete user infor exception : userId="+ userId,e);
				}
				resultList.add(result);
			}
		}else {
			resultList.add(getErrorResult("传入的待删除的数据集合为空"));
		}
		return resultList;
	}
	
//	@ApiOperation(value="加锁用户", notes="根据用户ID加锁用户")
//	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")
//	@RequestMapping(value="lock", method = {  RequestMethod.POST })
//	@ResponseBody
//	public ResultDto lock(String userId){
//		return null;
//	}
//	
//	@ApiOperation(value="解锁用户", notes="根据用户ID解锁用户")
//	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")
//	@RequestMapping(value="unlock", method = {  RequestMethod.POST })
//	@ResponseBody
//	public ResultDto unlock(String userId){
//		return null;
//	}
//
//	@ApiOperation(value="查询用户服务数据", notes="根据查询条件分页查询用户服务数据列表")
//	@ApiImplicitParam(name = "dto", value = "用户服务查询实体UserServiceInformationDTO", required = true, dataType = "UserServiceInformationDTO")
//	@RequestMapping(value = "listUserServiceInfo", method = RequestMethod.POST)
//	@ResponseBody
//	public List<UserServiceInformationDTO> listUserServiceInfo(UserServiceInformationDTO dto) {
//		return null;
//	}
//	
//	@ApiOperation(value="统计用户服务数据记录数", notes="根据查询条件统计用户服务数据记录数")
//	@ApiImplicitParam(name = "dto", value = "用户服务查询实体UserServiceInformationDTO", required = true, dataType = "UserServiceInformationDTO")
//	@RequestMapping(value = "countUserServiceInfo", method = RequestMethod.POST)
//	@ResponseBody
//	public int countUserServiceInfo(UserServiceInformationDTO dto) {
//		return 0;
//	}
//	
//	@ApiOperation(value="查询用户ip数据", notes="根据查询条件分页查询用户ip数据列表")
//	@ApiImplicitParam(name = "userIpInformationDTO", value = "用户ip详细实体UserIpInformationDTO", required = true, dataType = "UserIpInformationDTO")
//	@RequestMapping(value = "listUserIpInfo", method = RequestMethod.POST)
//	@ResponseBody
//	public List<UserIpInformationDTO> listUserIpInfo(UserIpInformationDTO userIpInformationDTO) {
//		return null;
//	} 
//	
//	@ApiOperation(value="统计用户ip数据记录数", notes="根据查询条件统计用户ip数据记录数")
//	@ApiImplicitParam(name = "userIpInformationDTO", value = "用户ip详细实体UserIpInformationDTO", required = true, dataType = "UserIpInformationDTO")
//	@RequestMapping(value = "countUserIpInfo", method = RequestMethod.POST)
//	@ResponseBody
//	public long countUserIpInfo(UserIpInformationDTO userIpInformationDTO) {
//		return 0;
//	} 
//	
//	@ApiOperation(value="查询用户网络资源", notes="根据查询条件分页查询用户网络资源列表")
//	@ApiImplicitParam(name = "userNetworksourceDTO", value = "用户网络资源查询实体UserNetworksourceDTO", required = true, dataType = "UserNetworksourceDTO")
//	@RequestMapping(value = "listUserNetworksourceInfo", method = RequestMethod.POST)
//	@ResponseBody
//	public List<UserIpInformationDTO> listUserNetworksourceInfo(UserNetworksourceDTO userNetworksourceDTO) {
//		return null;
//	} 
//	
//	@ApiOperation(value="统计用户网络资源记录数", notes="根据查询条件统计用户网络资源记录数")
//	@ApiImplicitParam(name = "userNetworksourceDTO", value = "用户网络资源查询实体UserNetworksourceDTO", required = true, dataType = "UserNetworksourceDTO")
//	@RequestMapping(value = "countUserNetworksourceInfo", method = RequestMethod.POST)
//	@ResponseBody
//	public int countUserNetworksourceInfo(UserNetworksourceDTO userNetworksourceDTO) {
//		return 0;
//	} 
}
