package com.aotain.serviceapi.server.service;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.args.ReqSynDelHouseToPassport;

/**
 * 权限同步服务
 * 
 * @author liuz@aotian.com
 * @date 2018年9月29日 下午2:35:33
 */
public interface SynAuthroityService {
	/**
	 * 机房权限KEY
	 */
	public static final String AUTHORITY_HOUSE_KEY = "AUTHHOUSELIST"; 
	/**
	 * 隶属单位权限KEY
	 */
	public static final String AUTHORITY_CITY_KEY = "AUTHCITYCODELIST"; 
	/**
	 * 专线标识权限KEY
	 */
	public static final String AUTHORITY_IDENTIFY_KEY = "AUTHIDENTIFYLIST"; 
	
	/**
	 * 同步删除权限系统中的机房
	 * @param req
	 * @return
	 */
	public ResultDto synDelHouse(ReqSynDelHouseToPassport req);
	
}
