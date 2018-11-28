package com.aotain.serviceapi.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.args.ReqSynDelHouseToPassport;
import com.aotain.serviceapi.server.service.SynAuthroityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 权限系统同步控制器
 * 
 * @author liuz@aotian.com
 * @date 2018年9月29日 下午2:30:21
 */
@Controller
@RequestMapping(value = "/authroity")
@Api(value = "CommonUtilController", description = "权限系统权限数据同步")
public class SynAuthroityController extends CommonController {
	private Logger logger = LoggerFactory.getLogger(SynAuthroityController.class);
	
	@Autowired
	private SynAuthroityService synAuthroityServcie;

	@ApiOperation(value = "删除机房权限", notes = "根据机房ID与AppId删除权限系统中的机房，以及其关联用户的相关权限信息")
	@RequestMapping(value = "synDelHouse", method = { RequestMethod.POST })
	@ResponseBody
	public ResultDto synDelHouse(@RequestBody ReqSynDelHouseToPassport req) {
		try {
			return synAuthroityServcie.synDelHouse(req);
		} catch (Throwable e) {
			logger.error("delete house synchronize to passport exception", e);
			return getErrorResult("同步删除机房执行异常");
		}
	}
}
