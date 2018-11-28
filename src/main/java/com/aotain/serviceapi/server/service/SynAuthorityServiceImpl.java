package com.aotain.serviceapi.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aotain.common.config.ContextUtil;
import com.aotain.common.config.LocalConfig;
import com.aotain.common.config.redis.BaseRedisService;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.ResultDto.ResultDtoBuilder;
import com.aotain.cu.serviceapi.model.args.ReqSynDelHouseToPassport;
import com.aotain.cu.serviceapi.model.permission.ApiResponse;
import com.aotain.cu.serviceapi.model.permission.DataPermission;
import com.aotain.cu.serviceapi.model.permission.DataPermissionSetting;
import com.aotain.serviceapi.server.util.HttpUtils;

/**
 * 同步权限服务的实现
 * 
 * @author liuz@aotian.com
 * @date 2018年9月29日 下午2:36:15
 */
@Service
public class SynAuthorityServiceImpl implements SynAuthroityService {
	private Logger logger = LoggerFactory.getLogger(SynAuthroityService.class);

	@Override
	public ResultDto synDelHouse(ReqSynDelHouseToPassport req) {
		if (req.getHouseId() == null) {
			logger.error("synchronize delete house to passport fail ： houseId is empty");
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：机房ID为空");
		}

		if (req.getSynDelUrl() == null) {
			logger.error("synchronize delete house to passport fail ： synDelUrl is empty");
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：删除服务URL为空");
		}

		if (req.getSynQueryUrl() == null) {
			logger.error("synchronize delete house to passport fail ： synQueryUrl is empty");
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：权限查询URL为空");
		}

		if (req.getAppId() == null) {
			logger.error("synchronize delete house to passport fail ： appId is empty");
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：appId为空");
		}
		ApiResponse<DataPermission> res = null;
		logger.info("start synDelHouse : " + req.toString());
		try {
			res = queryAllPermisionByAppid(req.getSynQueryUrl(), req.getAppId());
		} catch (Exception e) {
			logger.error("query all DataPermission from passport fail - call query url exception : " + req.toString(),
					e);
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用查询接口异常");
		}

		// 接口调用异常返回失败
		if (res == null || StringUtils.isNotBlank(res.getMessage())) {
			logger.error("query all DataPermission from passport fail - call query url fail ("+res.getMessage()+") : " + req.toString()+"");
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用查询接口失败");
		}

		List<DataPermission> dpList = res.getResult();
		if (dpList == null || dpList.size() == 0) {
			logger.error("query all DataPermission from passport fail - call query url return datas is empty : "
					+ req.toString());
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用查询接口返回的数据为空");
		}

		// 查找目标权限数据
		DataPermission target = null;
		for (int i = 0; i < dpList.size(); i++) {
			DataPermission dp = dpList.get(i);
			String name = dp.getDataPermissionToken(); // 权限标识名称
			if (AUTHORITY_HOUSE_KEY.equals(name)) {
				target = dp;
				break;
			}
		}
		if (target == null) {
			logger.error(
					"query all DataPermission from passport fail - call query url return datas can not found AUTHORITY_HOUSE_KEY["
							+ AUTHORITY_HOUSE_KEY + "] : " + req.toString());
			return ResultDto.ResultDtoBuilder
					.createErrorResult("同步删除权限系统机房失败：调用查询接口返回的数据未找到目标权限KEY[" + AUTHORITY_HOUSE_KEY + "]");
		}

		// 查询目标权限数据列表
		if (target.getSettings() == null) {
			logger.error(
					"query all DataPermission from passport fail - call query url return data's house information is empty "
							+ req.toString());
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用查询接口返回的数据中已无任何机房");
		}

		// 查询目标setting
		DataPermissionSetting targetSetting = null;
		List<DataPermissionSetting> targetSettingList = new ArrayList<DataPermissionSetting>();
		for (DataPermissionSetting set : target.getSettings()) {
			if(req.getHouseId().intValue() == 0){ // 删除全部
				DataPermissionSetting dps = new DataPermissionSetting();
				dps.setDataPermissionId(set.getDataPermissionId());
				dps.setSettingId(set.getSettingId());
				targetSettingList.add(dps);
				continue;
			}
			if (set.getSettingKey().equals(String.valueOf(req.getHouseId()))) { // 删除指定的机房
				targetSetting = new DataPermissionSetting();
				targetSetting.setDataPermissionId(set.getDataPermissionId());
				targetSetting.setSettingId(set.getSettingId());
				targetSettingList.add(targetSetting);
				break;
			}
		}

		// 无任何机房
		if (targetSettingList.size() == 0) {
			logger.error(
					"query all DataPermission from passport fail - call query url return data's can not found target house "
							+ req.toString());
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用查询接口返回的数据中无目标机房信息");
		}
		DataPermission dp = new DataPermission();
		dp.setAppId(req.getAppId().intValue());
		dp.setDataPermissionId(target.getDataPermissionId());
		dp.setSettings(targetSettingList);
		try {
			// 删除权限系统中的机房
			ApiResponse<String> delRes = deleteHouseFromPassport(req.getSynDelUrl(), dp);
			if (delRes == null) {
				logger.error("query all DataPermission from passport fail - call delete url return datas is empty : "
						+ req.toString());
				return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用删除接口返回的数据为空");
			}
			if(StringUtils.isNotBlank(delRes.getMessage())){
				logger.error("query all DataPermission from passport fail - call delete url return exception ("+delRes.getMessage()+") : "
						+ req.toString());
				return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用删除接口返回异常");
			}

			// 同步删除redis中的机房
			deleteHouseFromRedis(req.getHouseId());
		} catch (Exception e) {
			logger.error("query all DataPermission from passport fail - call delete url exception : " + req.toString(),
					e);
			return ResultDto.ResultDtoBuilder.createErrorResult("同步删除权限系统机房失败：调用删除接口异常");
		}

		logger.info("end synDelHouse - deal success : " + req.toString());
		return ResultDtoBuilder.createSuccessResult(null);
	}

	/**
	 * 从权限系统查询出所有的权限信息
	 * 
	 * @param queryUrl
	 * @param appid
	 * @throws IOException
	 */
	private ApiResponse<DataPermission> queryAllPermisionByAppid(String queryUrl, Long appid) throws IOException {
		queryUrl += "/" + String.valueOf(appid);
		String jsonStr = HttpUtils.getRequest(queryUrl, "UTF-8");
		try {
			ApiResponse<DataPermission> res = new ApiResponse<DataPermission>();
			res = JSON.parseObject(jsonStr, new TypeReference<ApiResponse<DataPermission>>(){});
			return res;
		} catch (Exception e) {
			logger.error("json parse exception : " + jsonStr, e);
			throw new IOException("json parse fail : " + jsonStr);
		}
	}

	/**
	 * 从权限系统中删除某个机房
	 * 
	 * @param queryUrl
	 * @param appid
	 * @param target 机房权限信息
	 * @param targetSetting 要删除的机房ID
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private ApiResponse<String> deleteHouseFromPassport(String deleteUrl, DataPermission target) throws IOException {
		String jsonStr;
		List<DataPermission> list = new ArrayList<DataPermission>();
		list.add(target);
		String data = JSON.toJSONString(list);
		try {
			jsonStr = HttpUtils.postRequest(deleteUrl, data, "UTF-8");
		} catch (Exception e1) {
			logger.error("request delete url exception : url=" + deleteUrl + ",data=" + data, e1);
			throw new IOException("request delete url exception : url=" + deleteUrl + ",data=" + data, e1);
		}
		try {
			ApiResponse<String> res = new ApiResponse<String>(); // 新建实例为获取带有泛型的数据类型使用
			res = JSON.parseObject(jsonStr, res.getClass());
			return res;
		} catch (Exception e) {
			logger.error("json parse exception : " + jsonStr, e);
			throw new IOException("json parse fail : " + jsonStr);
		}
	}

	/**
	 * 删除机房
	 * 
	 * @param houseIdStr
	 */
	private void deleteHouseFromRedis(Long houseId) {
		if (houseId == null) { // 为空无需删除
			return;
		}
		@SuppressWarnings("unchecked")
		BaseRedisService<String, String, String> baseRedisService = ContextUtil.getContext()
				.getBean(BaseRedisService.class);
		String houseIdStr = LocalConfig.getInstance().getHouseIdStrByHouseId(houseId);
		if (StringUtils.isBlank(houseIdStr)) { // 不存在的机房也无需删除
			return;
		}
		baseRedisService.removeHash("IdcHouses", houseIdStr);
	}
}
