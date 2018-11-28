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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aotain.serviceapi.server.controller.CommonController;
import com.aotain.serviceapi.server.service.preinput.PreUserBandWidthService;
import com.aotain.serviceapi.server.service.preinput.PreUserInfoService;
import com.aotain.serviceapi.server.service.preinput.PreUserServerService;
import com.aotain.serviceapi.server.service.preinput.impl.PreUserHHServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.PreUserVirtualServiceImpl;
import com.aotain.serviceapi.server.validate.IUserNetworkValidator;
import com.aotain.serviceapi.server.validate.IUserPrincipalValidator;
import com.aotain.serviceapi.server.validate.IUserServiceValidator;
import com.aotain.serviceapi.server.validate.IUserVirtualMachineValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value="/pre/user")
@Api(value="PreUserInfoController",description="预录入模块-用户信息")
public class PreUserInfoController extends CommonController {

	private Logger logger = LoggerFactory.getLogger(PreUserInfoController.class);

	@Autowired
	private PreUserInfoService preUserInfoService;

	@Autowired
	@Qualifier(value="userNetworkValidatorImpl")
	private IUserNetworkValidator userNetworkValidator;

	@Autowired
	@Qualifier(value="userPrincipalValidatorImpl")
	private IUserPrincipalValidator userPrincipalValidator;

	@Autowired
	@Qualifier(value="userServiceValidatorImpl")
	private IUserServiceValidator userServiceValidator;

	@Autowired
	@Qualifier(value="userVirtualMachineValidatorImpl")
	private IUserVirtualMachineValidator userVirtualMachineValidator;
	
	@Autowired
	@Qualifier("preUserHHServiceImpl")
	private PreUserHHServiceImpl preUserHHService;
	
	@Autowired
	@Qualifier("preUserVirtualServiceImpl")
	private PreUserVirtualServiceImpl preUserVirtualService;

	@Autowired
	private PreUserServerService preUserServerService;

	@Autowired
	private PreUserBandWidthService preUserBandWidthService;

	@Autowired
	private CommonUtilService commonUtilService;

	private Logger log = LoggerFactory.getLogger(PreUserInfoController.class);

	@ApiOperation(value="查询用户信息数据", notes="根据查询条件分页查询用户信息数据列表")
	@RequestMapping(value = "listUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public PageResult<UserInformationDTO> listUserInfo(@RequestBody @ApiParam(required = true, name = "dto", value="用户信息详细实体UserInformationDTO")UserInformationDTO dto) {
		return preUserInfoService.listUserData(dto);
	}
	
	@ApiOperation(value="统计用户信息数据记录数", notes="根据查询条件统计用户信息数据记录数")
	@RequestMapping(value = "countUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public long countUserInfo(@ApiParam(required = true, name = "dto", value="用户信息详细实体UserInformationDTO")UserInformationDTO dto) {
		return 0;
	}

	@ApiOperation(value = "新增用户完整信息", notes = "新增用户信息")
	@RequestMapping(value = "insert", method = { RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> insert(@RequestBody @ApiParam(required = true, name = "user", value = "用户信息实体") List<UserInformationDTO> users) {
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (users != null && users.size() > 0) {
			for (UserInformationDTO user : users) {
				resultList.add(preUserInfoService.insertData(user));
			}
		} else {
			resultList.add(getErrorResult("传入的待插入的数据集合为空"));
		}
		return resultList;
		
		/*try {
			return preUserInfoService.insertData(user);
		} catch (Exception e) {
			logger.error("insert user info to DB error", e);
			ResultDto dto = new ResultDto();
			dto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
			dto.setResultMsg("新增失败，请刷新页面重试");
			return dto;
		}*/
	}
	
	@ApiOperation(value="更新用户信息", notes="更新用户信息")
	@RequestMapping(value="update", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ResultDto> update(@RequestBody @ApiParam(required = true, name = "list", value="用户信息实体")List<UserInformationDTO> list){
		List<ResultDto> resultList = new ArrayList<ResultDto>();
		if (list != null && list.size() > 0) {
			for (UserInformationDTO user : list) {
				resultList.add(preUserInfoService.updateData(user));
			}
		} else {
			resultList.add(getErrorResult("传入的待插入的数据集合为空"));
		}
		return resultList;
		/*try {
			return preUserInfoService.updateData(user);
		}catch (Exception e){
			logger.error("update user info to DB error",e);
			return new ResultDto();
		}*/
	}
	
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	@ResponseBody
	public ResultDto delete(@RequestBody @ApiParam(required = true, name = "deleteList", value = "用户ID集合") List<UserInformationDTO> deleteList) {
		return preUserInfoService.batchDeleteUserInfo(deleteList);
		/*List<Long> idlist = new ArrayList<Long>();
		String[] arr = ids.split(",");
		for (String str : arr) {
			idlist.add(Long.valueOf(str));
		}
		return preUserInfoService.batchDeleteUserInfo(idlist);*/
	}
	
	@ApiOperation(value="校验用户信息", notes="校验用户信息")
	@RequestMapping(value = "validate", method = { RequestMethod.POST})
	@ResponseBody
	public AjaxValidationResult validate(@ApiParam(required = true, name = "user", value="用户信息实体")@RequestBody UserInformationDTO user, BindingResult bindingResult){
		try {
			return  userPrincipalValidator.preValidate(user);
		}catch (Exception e){
			logger.error("validate user info error",e);
		}
		return new AjaxValidationResult();
	}
	
	@ApiOperation(value="获取用户完整信息", notes="根据用户Id查找用户信息")
	@RequestMapping(value="getDetail", method = {  RequestMethod.POST })
	@ResponseBody
	public UserInformationDTO getDetail(@ApiParam(required = true, name = "userId", value="用户Id")@RequestBody String userId){
		return preUserInfoService.findByUserId(userId);
	}


	@ApiOperation(value="用户审核结果", notes="用户审核结果")
	@RequestMapping(value="userValidateMsg", method = {  RequestMethod.POST })
	@ResponseBody
	public List<ApproveResultDto> userValidateMsg(@RequestBody @ApiParam(required = true, name = "approveId", value= "审核结果ID")String approveId){
		try {
			return preUserInfoService.getApproveResult(approveId);
		} catch (Exception e) {
			log.error("user idcValidateMsg error ",e);
		}
		return null;
	}

	@ApiOperation(value="预审用户信息", notes="根据用户ID预审机房的相应信息")
	@RequestMapping(value="approve", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto approve(@ApiParam(required = true, name = "userId", value= "用户ID")String userId) {
		ResultDto result=preUserInfoService.approve(userId);

		//写redis信息
		Long approveId=ApproveIdUtil.getInstance().getApproveId();
		DataApproveUtil.getInstance().setDataApprove("3"+userId,approveId);
		//写流水表信息
		WaitApproveProcess ass=new WaitApproveProcess();
		ass.setApproveId(approveId);
		ass.setType(3);
		ass.setDataId(Long.valueOf(userId));
		if(result.getResultCode()==0){
			ass.setDealFlag(DealFlagEnum.STATUS2.getCode());
		}else{
			ass.setDealFlag(DealFlagEnum.STATUS1.getCode());
		}
		UserInformationDTO dto = new UserInformationDTO();
		dto.setUserId(Long.valueOf(userId));
		dto = preUserInfoService.findByUserId(userId);
		ass.setWarnData(dto.getVerificationResult());
		ass.setDealTime(DateUtils.getCurrentyyyMMddHHmmss());
		Date date=new Date();
		ass.setCreateTime(date);
		ass.setUpdateTime(date);
		int i=commonUtilService.insertApproveProcess(ass);
		if(i<=0){
			logger.error("user approve insertApproveProcess error,userId="+userId);
		}

		return result;
	}
	
	@ApiOperation(value="撤销预审机房信息", notes="根据机房ID撤销对应的预审机房信息")
	@RequestMapping(value="revertApprove", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto revertApprove(@RequestBody @ApiParam(required = true, name = "userId", value= "用户ID")String userId) {
		return preUserInfoService.revertApprove(userId);
	}
	
	
	/**
	 * 用户服务信息的操作API
	 */
	@ApiOperation(value="新增用户服务信息", notes="根据用户服务对象集合新增用户服务信息")
	@RequestMapping(value="/service/insert", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto insertService(@RequestBody @ApiParam(required = true, name = "services", value="用户服务对象集合")List<UserServiceInformationDTO> services) {
		ResultDto result = new ResultDto();
		try {
			result = preUserServerService.insertData(services, null, false);
		}catch (Exception e){
			log.error("save Service fail",e);
		}
		return result;
	}
	
	@ApiOperation(value="修改用户服务信息", notes="根据用户服务对象集合修改用户服务信息")
	@RequestMapping(value="/service/update", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto updateService(@RequestBody @ApiParam(required = true, name = "service", value="用户服务对象集合")List<UserServiceInformationDTO> service) {
		ResultDto result = new ResultDto();
		try {
			result = preUserServerService.updateData(service);
		}catch (Exception e){
			log.error("update Service fail",e);
		}
		return result;
	}
	
	@ApiOperation(value = "删除用户服务信息", notes = "根据用户服务ID集合数据删除用户服务信息")
	@RequestMapping(value = "/service/delete", method = { RequestMethod.POST })
	@ResponseBody
	public ResultDto deleteService(@RequestBody @ApiParam(required = true, name = "services", value = "用户服务集合") List<UserServiceInformationDTO> services) {
		ResultDto result = new ResultDto();
		try {
			result = preUserServerService.deleteData(services);
		} catch (Exception e) {
			log.error("update Service fail", e);
		}
		return result;
	}
	
	@ApiOperation(value = "查询用户服务信息", notes = "根据用户服务对象查询用户服务信息")
	@RequestMapping(value = "/service/query", method = { RequestMethod.POST })
	@ResponseBody
	public PageResult<UserServiceInformationDTO> queryService(@RequestBody @ApiParam(required = true, name = "service", value = "用户服务对象") UserServiceInformationDTO service) {
		PageResult<UserServiceInformationDTO> result = new PageResult<UserServiceInformationDTO>();
		try {
			result = preUserServerService.getServerInfoList(service);
		} catch (Exception e) {
			log.error("queryService fail", e);
		}
		return result;
	}
	
	@ApiOperation(value="统计用户服务信息", notes="根据用户服务对象统计用户服务信息")
	@RequestMapping(value="/service/count", method = {  RequestMethod.POST })
	@ResponseBody
	public long countService(@ApiParam(required = true, name = "service", value= "用户服务对象")UserServiceInformation service) {
		return 0;
	}
	
	@ApiOperation(value="校验用户服务数据", notes="校验用户服务信息是否正确")
	@RequestMapping(value = "/service/validate", method = { RequestMethod.POST})
	@ResponseBody 
	public AjaxValidationResult validateService(@ApiParam(required = true, name = "service", value= "用户服务对象")@RequestBody UserServiceInformationDTO service, BindingResult bindingResult){
		return userServiceValidator.preValidate(service);
	}
	
	/**
	 * 用户网络资源（带宽）的操作API
	 */
	@ApiOperation(value="新增用户带宽信息", notes="根据用户带宽对象集合新增用户带宽信息")
	@RequestMapping(value="/bandwidth/insert", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto insertBandwidth(@RequestBody @ApiParam(required = true, name = "bandwidths", value="用户服务对象集合")List<UserBandwidthInformationDTO> bandwidths) {
		ResultDto result = new ResultDto();
		try {
			result = preUserBandWidthService.insertData(bandwidths, null, false);
		} catch (Exception e) {
			log.error("save userbandwidth fail", e);
		}
		return result;
	}
	
	@ApiOperation(value="修改用户带宽信息", notes="根据用户带宽对象集合修改用户带宽信息")
	@RequestMapping(value="/bandwidth/update", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto updateBandwidth( @RequestBody @ApiParam(required = true, name = "bandwidth", value="用户服务对象集合")List<UserBandwidthInformationDTO> bandwidth) {
		ResultDto result = new ResultDto();
		try {
			result = preUserBandWidthService.updateData(bandwidth);
		} catch (Exception e) {
			log.error("save userbandwidth fail", e);
		}
		return result;
	}
	
	@ApiOperation(value = "删除用户带宽信息", notes = "根据用户带宽ID集合数据删除用户带宽信息")
	@RequestMapping(value = "/bandwidth/delete", method = { RequestMethod.POST })
	@ResponseBody
	public ResultDto deleteBandwidth(@RequestBody @ApiParam(required = true, name = "bandwidths", value = "用户带宽ID集合") List<UserBandwidthInformationDTO> bandwidths) {
		ResultDto result = new ResultDto();
		try {
			result = preUserBandWidthService.deleteData(bandwidths);
		} catch (Exception e) {
			log.error("delete user bandWidth fail", e);
		}
		return result;
	}
	
	@ApiOperation(value="查询用户带宽信息", notes="根据用户带宽对象查询用户带宽信息")
	@RequestMapping(value="/bandwidth/query", method = {  RequestMethod.POST })
	@ResponseBody
	public PageResult<UserBandwidthInformationDTO> queryBandwidth(@RequestBody @ApiParam(required = true, name = "bandwidth", value= "用户带宽对象")UserBandwidthInformationDTO bandwidth) {
		PageResult<UserBandwidthInformationDTO> result = new PageResult<UserBandwidthInformationDTO>();
		try {
			result = preUserBandWidthService.getServerInfoList(bandwidth);
		}catch (Exception e){
			e.printStackTrace();
			log.error("queryService fail");
		}
		return result;
	}
	
	@ApiOperation(value="统计用户带宽信息", notes="根据用户带宽对象统计用户带宽信息")
	@RequestMapping(value="/bandwidth/count", method = {  RequestMethod.POST })
	@ResponseBody
	public long countBandwidth(@ApiParam(required = true, name = "bandwidth", value= "用户带宽对象")UserBandwidthInformationDTO bandwidth) {
		return 0;
	}
	
	@ApiOperation(value="校验用户带宽数据", notes="校验用户带宽信息是否正确")
	@RequestMapping(value = "/bandwidth/validate", method = { RequestMethod.POST})
	@ResponseBody 
	public AjaxValidationResult validateBandwidth(@ApiParam(required = true, name = "bandwidth", value= "用户带宽对象")@RequestBody UserBandwidthInformationDTO bandwidth, BindingResult bindingResult){
		return userNetworkValidator.preValidate(bandwidth);
	}
	
	/**
	 * 用户IP信息的操作API
	 */
	@ApiOperation(value="查询用户IP信息", notes="根据用户IP对象查询用户IP信息")
	@RequestMapping(value="/userip/query", method = {  RequestMethod.POST })
	@ResponseBody
	public PageResult<UserIpInformationDTO> queryUserIps(@ApiParam(required = true, name = "userIps", value= "用户带宽对象")UserIpInformationDTO userIps) {
		return null;
	}
	
	@ApiOperation(value="统计用户IP信息", notes="根据用户IP对象统计用户IP信息")
	@RequestMapping(value="/userip/count", method = {  RequestMethod.POST })
	@ResponseBody
	public long countUserIps(@ApiParam(required = true, name = "userIps", value= "用户带宽对象")UserIpInformationDTO userIps) {
		return 0;
	}
	
	/**
	 * 虚拟主机信息的操作API
	 */
	@ApiOperation(value = "新增用户虚拟主机信息", notes = "根据用户虚拟主机对象集合新增用户虚拟主机信息")
	@RequestMapping(value = "/virtual/insert", method = { RequestMethod.POST })
	@ResponseBody
	public ResultDto insertVirtualHost(@RequestBody @ApiParam(required = true, name = "virtuals", value = "用户虚拟主机对象集合") List<UserVirtualInformation> virtuals) {
		ResultDto resultDto = new ResultDto();
		try {
			resultDto = preUserVirtualService.batchInsertDatas(virtuals, null, false);
		} catch (Exception e) {
			resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
			resultDto.setResultMsg("insert Error");
		}
		return resultDto;
	}
	
	@ApiOperation(value="修改用户虚拟主机信息", notes="根据用户虚拟主机对象集合修改用户虚拟主机信息")
	@RequestMapping(value="/virtual/update", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto updateVirtualHost(@RequestBody @ApiParam(required = true, name = "virtuals", value="用户虚拟主机对象集合")List<UserVirtualInformation> virtuals) {
		ResultDto resultDto = new ResultDto();
		try {
			resultDto= preUserVirtualService.updateData(virtuals);
		} catch (Exception e) {
			resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
			resultDto.setResultMsg("update Error");
		}
		return resultDto;
	}
	
	@ApiOperation(value="删除用户虚拟主机信息", notes="根据用户虚拟主机ID集合数据删除用户虚拟主机信息")
	@RequestMapping(value="/virtual/delete", method = {  RequestMethod.POST })
	@ResponseBody
	public ResultDto deleteVirtualHost(@RequestBody @ApiParam(required = true, name = "virtuals", value= "用户虚拟主机ID集合")List<UserVirtualInformation> virtuals) {
		return preUserVirtualService.batchDeleteDatas(virtuals);
	}
	
	@ApiOperation(value="查询用户虚拟主机信息", notes="根据用户虚拟主机对象查询用户虚拟主机信息")
	@RequestMapping(value="/virtual/query", method = {  RequestMethod.POST })
	@ResponseBody
	public PageResult<UserVirtualInformationDTO> queryVirtualHost(@RequestBody @ApiParam(required = true, name = "virtual", value= "用户虚拟主机对象")UserVirtualInformationDTO virtual) {
		return preUserVirtualService.getUserVirtualList(virtual);
	}
	
	@ApiOperation(value="统计用户虚拟主机信息", notes="根据用户虚拟主机对象统计用户虚拟主机信息")
	@RequestMapping(value="/virtual/count", method = {  RequestMethod.POST })
	@ResponseBody
	public long countVirtualHost(@ApiParam(required = true, name = "virtual", value= "用户虚拟主机对象")UserVirtualInformationDTO virtual) {
		return 0;
	}
	
	@ApiOperation(value="校验用户虚拟主机数据", notes="校验用户虚拟主机信息是否正确")
	@RequestMapping(value = "/virtual/validate", method = { RequestMethod.POST})
	@ResponseBody 
	public AjaxValidationResult validateVirtualHost(@ApiParam(required = true, name = "virtual", value= "用户虚拟主机对象")@RequestBody UserVirtualInformationDTO virtual){
		return userVirtualMachineValidator.preValidate(virtual);
	}

	@ApiOperation(value="用户属性变更", notes="用户属性变更")
	@RequestMapping(value = "/changeUserNature", method = { RequestMethod.POST})
	@ResponseBody
	public ResultDto changeUserNature(@ApiParam(required = true, name = "userInformation", value= "用户对象")@RequestBody UserInformation userInformation){
		try {
			return preUserInfoService.changeUserNature(userInformation);
		}catch (Exception e){
			logger.error("change user nature error ",e);
			return null;
		}
	}

	@ApiOperation(value="导入用户完整信息", notes="导入用户信息")
	@RequestMapping(value="/importUser", method = {  RequestMethod.POST })
	@ResponseBody
	public Map<Integer,ResultDto> importUser( @RequestBody @ApiParam(required = true, name = "user", value="用户信息实体")Map<Integer,UserInformationDTO> user ){

		try {
			return preUserInfoService.imporUserData(user);
		}catch (Exception e){
			logger.error("importUser error",e);
		}
		return null;
	}

	@ApiOperation(value="导入用户服务信息", notes="导入用户服务信息")
	@RequestMapping(value="/importUserService", method = {  RequestMethod.POST })
	@ResponseBody
	public Map<Integer,ResultDto> importUserService( @RequestBody @ApiParam(required = true, name = "userServiceInformation", value="用户服务信息实体")Map<Integer,UserServiceInformationDTO> user){

		try {
			return preUserServerService.imporUserServiceData(user);
		}catch (Exception e){
			logger.error("importUserService error",e);
		}
		return null;
	}

	@ApiOperation(value="导入用户带宽信息", notes="导入用户带宽信息")
	@RequestMapping(value="/importUserBand", method = {  RequestMethod.POST })
	@ResponseBody
	public Map<Integer,ResultDto> importUserBand( @RequestBody @ApiParam(required = true, name = "userBandwidthInformation", value="用户带宽信息实体")Map<Integer,UserBandwidthInformationDTO> user){

		try {
			return preUserBandWidthService.imporUserBandData(user);
		}catch (Exception e){
			logger.error("importUserBand error",e);
		}
		return null;
	}

	@ApiOperation(value="导入用户虚拟主机信息", notes="导入用户虚拟主机信息")
	@RequestMapping(value="/importUserVir", method = {  RequestMethod.POST })
	@ResponseBody
	public Map<Integer,ResultDto> importUserVir( @RequestBody @ApiParam(required = true, name = "userVirtualInformation", value="用户虚拟主机信息实体")Map<Integer,UserVirtualInformationDTO> user ){

		try {
			return preUserVirtualService.imporUserVirData(user);
		}catch (Exception e){
			logger.error("importUserVir error",e);
		}
		return null;
	}
	
}
