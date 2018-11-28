package com.aotain.serviceapi.server.service.preinput.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.serviceapi.server.dao.preinput.*;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.aotain.common.config.LocalConfig;
import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.CacheIsmsBaseInfo;
import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.PreHouseIpsegService;
import com.aotain.serviceapi.server.util.IpUtil;
import com.aotain.serviceapi.server.util.SpringUtil;
import com.aotain.serviceapi.server.util.Tools;
import com.aotain.serviceapi.server.validate.IHouseIpSegmentValidator;

@Service
public class PreHouseIpsegServiceImpl extends BaseService implements PreHouseIpsegService {

	@Autowired
	private HouseIpSegmentMapper houseIpsegMapper;
	
	@Autowired
	private CacheIsmsBaseInfoMapper cacheMapper;
	
	@Autowired
	private UserPrincipalMapper userPrincipalMapper;
	
	@Autowired
	private PreUserInfoServiceImpl userInfoService;
	
	@Autowired
    private HousePrincipalMapper housePrincipalMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private Logger log = Logger.getLogger(PreHouseIpsegServiceImpl.class);
	
	public PreHouseIpsegServiceImpl(@Qualifier(value = "houseIpSegmentValidatorImpl") IHouseIpSegmentValidator baseValidator) {
		super(baseValidator);
	}

	@Override
	protected int insert(BaseModel baseModel) {
		HouseIPSegmentInformation dto = (HouseIPSegmentInformation)baseModel;
		if(IpUtil.isIpv6(dto.getStartIP())){
			dto.setStartIPStr(IpUtil.ipv6ToBigInteger(dto.getStartIP()).toString());
		}else{
			dto.setStartIPStr(Tools.ip2long(dto.getStartIP())+"");
		}
		
		if(IpUtil.isIpv6(dto.getEndIP())){
			dto.setEndIPStr(IpUtil.ipv6ToBigInteger(dto.getEndIP()).toString());
		}else{
			dto.setEndIPStr(Tools.ip2long(dto.getEndIP())+"");
		}
		
		//当ip冲突强制覆盖开关打开，则先对冲突ip进行处理
		String IpConflickSwitch = LocalConfig.getInstance().getHashValueByHashKey("ip_import_enabled");//IP导入冲突是否强制覆盖开关
		if("true".equals(IpConflickSwitch)){
			dealConfickIpsegs(dto);
		}
		
		int result = 0;
		try {
			result = houseIpsegMapper.insertHouseIpSegment(dto);
		} catch (Exception e) {
			log.error("insert preHouseIpsegInfo to DB ERROR.dto："+dto.toString()+e);
		}
		
		
		return result;
	}

	@Override
	protected int update(BaseModel baseModel) {
		HouseIPSegmentInformation dto = (HouseIPSegmentInformation)baseModel;
		if(IpUtil.isIpv6(dto.getStartIP())){
			dto.setStartIPStr(IpUtil.ipv6ToBigInteger(dto.getStartIP()).toString());
		}else{
			dto.setStartIPStr(Tools.ip2long(dto.getStartIP())+"");
		}
		
		if(IpUtil.isIpv6(dto.getEndIP())){
			dto.setEndIPStr(IpUtil.ipv6ToBigInteger(dto.getEndIP()).toString());
		}else{
			dto.setEndIPStr(Tools.ip2long(dto.getEndIP())+"");
		}
		
		//当ip冲突强制覆盖开关打开，则先对冲突ip进行处理
		String IpConflickSwitch = LocalConfig.getInstance().getHashValueByHashKey("ip_import_enabled");//IP导入冲突是否强制覆盖开关
		if("true".equals(IpConflickSwitch)){
			dealConfickIpsegs(dto);
		}
        return houseIpsegMapper.updateHouseIpSegment(dto);
	}

	@Override
	protected int delete(BaseModel baseModel) {
		return 0;
	}

	@Transactional
	@Override
	public ResultDto insertData(List<HouseIPSegmentInformation> dtos, Long houseId) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (HouseIPSegmentInformation dto : dtos) {
				try {
					if (houseId != null) {
						dto.setHouseId(houseId);
					}
					// 新增机房Ip地址段信息
					int result = insert(dto);
					if (result <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert house IP segment success!");
					
					// 根据机房状态进行相应操作
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					log.info("update house czlx and deal_flag success!");
					// 对于已分配的用户当用户处于上报状态需更新用户信息
					dealIpUserWhileIPUpdate(dto);
				} catch (Exception e) {
					log.error("insert house ip segment data failed!", e);
					return getErrorResult("新增记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

	private void dealConfickIpsegs(HouseIPSegmentInformation dto) {
		List<HouseIPSegmentInformation> conflickIps = houseIpsegMapper.findConflickIp(dto);
		Map<String, Object> houseMap = new HashMap<>();
		for(HouseIPSegmentInformation ipseg : conflickIps){
			try {
				Map<String,Object> ipMap = new HashMap<>();
				if(!IpUtil.isStartIPOverEndIp(ipseg.getEndIP(), dto.getEndIP())&&!IpUtil.isStartIPOverEndIp(dto.getStartIP(), ipseg.getStartIP())){
					/**
					 * 完全覆盖系统的的ip段,上报成功的变更成删除未上报,未上报的直接删除
					 */
					if(ipseg.getDealFlag()==HouseConstant.ChildDealFlagEnum.UPLOADED.getValue()){//新增或者修改上报成功
						ipMap.put("czlx", HouseConstant.OperationTypeEnum.DELETE.getValue());
						ipMap.put("dealFlag", HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
						ipMap.put("ipsegId", ipseg.getIpSegId());
						houseIpsegMapper.updateIPSegtatusByIpsegId(ipMap);
					}else if(ipseg.getCzlx()==HouseConstant.OperationTypeEnum.ADD.getValue()&&ipseg.getDealFlag()==HouseConstant.ChildDealFlagEnum.UN_UPLOAD.getValue()){
						//新增未上报则直接删除
						houseIpsegMapper.deleteByIpsegId(ipseg.getIpSegId());
					}
					houseMap.put(ipseg.getHouseId()+"", ipseg.getHouseId());
				}else if((!IpUtil.isStartIPOverEndIp(dto.getStartIP(), ipseg.getStartIP())&&IpUtil.isStartIPOverEndIp(ipseg.getEndIP(), dto.getEndIP()))||
						(!IpUtil.isStartIPOverEndIp(ipseg.getStartIP(), dto.getStartIP())&&IpUtil.isStartIPOverEndIp(dto.getEndIP(), ipseg.getEndIP()))){
					/**
					 * 部分交集的ip段，上报过的进行数据修改，操作类型为修改未上报,未上报的直接进行数据修改	
					 */
					if(ipseg.getDealFlag()==HouseConstant.ChildDealFlagEnum.UPLOADED.getValue()){//新增或者修改上报成功
						ipMap.put("czlx", HouseConstant.OperationTypeEnum.DELETE.getValue());
						ipMap.put("dealFlag", HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
						ipMap.put("ipsegId", ipseg.getIpSegId());
						houseIpsegMapper.updateIPSegtatusByIpsegId(ipMap);
					}
					if(IpUtil.isStartIPOverEndIp(dto.getEndIP(), ipseg.getEndIP())){//与系统IP段发生左交集
						BigInteger startNum;
						if(IpUtil.isIpv4(dto.getStartIP())){
							long startIP = IpUtil.ipv4ToLong(dto.getStartIP());
							ipseg.setEndIPStr((startIP-1L)+"");
							ipseg.setEndIP(IpUtil.long2ipV4((startIP-1L)));
						}else{
							startNum = IpUtil.ipv6ToBigInteger(dto.getStartIP());
							BigInteger newEndIP = startNum.subtract(new BigInteger("1"));
							ipseg.setEndIP(Tools.int2ipv6(newEndIP));
							ipseg.setEndIPStr(newEndIP+"");
						}
						houseIpsegMapper.updateHouseIpSegment(ipseg);
						
						/**
						 * 	 产生交集的用户已上报的提交预审，正在提交上报的写缓存
						 */
						dealIpUserWhileIPUpdate(ipseg);
					}else{//与系统IP段发生右交集
						BigInteger endNUm;
						if(IpUtil.isIpv4(dto.getEndIP())){
							long startIP = IpUtil.ipv4ToLong(dto.getEndIP());
							ipseg.setStartIPStr((startIP+1L)+"");
							ipseg.setStartIP(IpUtil.long2ipV4((startIP+1L)));
						}else{
							endNUm = IpUtil.ipv6ToBigInteger(dto.getEndIP());
							BigInteger newEndIP = endNUm.add(new BigInteger("1"));
							ipseg.setStartIP(Tools.int2ipv6(newEndIP));
							ipseg.setStartIPStr(newEndIP+"");
						}
						houseIpsegMapper.updateHouseIpSegment(ipseg);
					}
					houseMap.put(ipseg.getHouseId()+"", ipseg.getHouseId());
					/**
					 * 	 产生交集的用户已上报的提交预审，正在提交上报的写缓存
					 */
					dealIpUserWhileIPUpdate(ipseg);
				}else if(IpUtil.isStartIPOverEndIp(ipseg.getEndIP(), dto.getEndIP())&&IpUtil.isStartIPOverEndIp(dto.getStartIP(), ipseg.getStartIP())){
					/**
					 * 包含于系统的ip段，已上报的部分拆分数据做修改，操作类型为修改未上报，部分拆分数据做新增处理
					 */
					
					//左部分做修改操作
					String endIp = ipseg.getEndIP();
					String endIpStr = ipseg.getEndIPStr();
					if(IpUtil.isIpv4(dto.getStartIP())){
						long startIP = IpUtil.ipv4ToLong(dto.getStartIP());
						ipseg.setEndIPStr((startIP-1L)+"");
						ipseg.setEndIP(IpUtil.long2ipV4((startIP-1L)));
					}else{
						BigInteger endNUm = IpUtil.ipv6ToBigInteger(dto.getStartIP());
						BigInteger newEndIP = endNUm.subtract(new BigInteger("1"));
						ipseg.setEndIP(Tools.int2ipv6(newEndIP));
						ipseg.setEndIPStr(newEndIP+"");
					}
					if(ipseg.getDealFlag()==HouseConstant.ChildDealFlagEnum.UPLOADED.getValue()){//上报成功修改成变更未上报
						ipseg.setCzlx(HouseConstant.OperationTypeEnum.MODIFY.getValue());
					}
					houseIpsegMapper.updateHouseIpSegment(ipseg);
					
					//右部分做新增操作
					ipseg.setIpSegId(null);
					ipseg.setEndIP(endIp);
					ipseg.setEndIPStr(endIpStr);
					BigInteger endNUm;
					if(IpUtil.isIpv4(dto.getEndIP())){
						long startIP = IpUtil.ipv4ToLong(dto.getEndIP());
						ipseg.setStartIPStr((startIP+1L)+"");
						ipseg.setStartIP(IpUtil.long2ipV4((startIP+1L)));
					}else{
						endNUm = IpUtil.ipv6ToBigInteger(dto.getEndIP());
						BigInteger newEndIP = endNUm.add(new BigInteger("1"));
						ipseg.setStartIP(Tools.int2ipv6(newEndIP));
						ipseg.setStartIPStr(newEndIP+"");
					}
					houseIpsegMapper.insertHouseIpSegment(ipseg);
					houseMap.put(ipseg.getHouseId()+"", ipseg.getHouseId());
					
					/**
					 * 	 产生交集的用户已上报的提交预审，正在提交上报的写缓存
					 */
					dealIpUserWhileIPUpdate(ipseg);
				}
				log.info("houseIpseg split success!");
				
				/**
				 * 产生交集的机房正在提交则写入缓存,否则修改成变更未预审
				 */
				for(Entry<String, Object> map:houseMap.entrySet()){
					dealHouseMainWhileISubInfoUpdate(Long.parseLong(map.getKey()));
				}
			} catch (Exception e) {
				log.error("houseIpseg occur error while spliting,ipsegId="+ipseg.getIpSegId(),e);
			}
		}
	}

	/**
	 * 当IP发生变更时，对于关联的用户做相应处理
	 * 
	 */
	private ResultDto dealIpUserWhileIPUpdate(HouseIPSegmentInformation dto) {
		if (dto != null && dto.getIpType() != 2) {
			UserInformation userInfo = new UserInformation();
			if (dto.getUserName() != null) {
				userInfo.setUnitName(dto.getUserName());
			}
			if (dto.getIdType() != null) {
				userInfo.setIdType(dto.getIdType());
			}
			if (dto.getIdNumber() != null) {
				userInfo.setIdNumber(dto.getIdNumber());
			}
			UserInformation userInformation = userPrincipalMapper.findByUnitNameAndIdTypeAndNumber(userInfo);
			if (userInformation != null) {
				CacheIsmsBaseInfo userCache = new CacheIsmsBaseInfo();
				if(dto.getUpdateUserId()!=null){
					userCache.setCreateUserId(dto.getUpdateUserId().longValue());
				}else{
					userCache.setCreateUserId(dto.getCreateUserId().longValue());
				}
				userCache.setJyzId(obtainJyzId());
				userCache.setHouseId(dto.getHouseId());
				userCache.setUserId(userInformation.getUserId());
				// dealUserWhileHouseInfoChange(userInformation, userCache);
				// 查看缓存信息表是否有该用户的等待记录
				CacheIsmsBaseInfo cacheResult = cacheMapper.findByUserId(userInformation.getUserId());
				if ((userInformation.getDealFlag() == HouseConstant.DealFlagEnum.CHECKING.getValue()||userInformation.getDealFlag() == HouseConstant.DealFlagEnum.REPORTING.getValue()) && cacheResult == null) {// 机架的所属客户为提交上报，并且缓存中没有相应用户的缓存信息，写等待缓存表
					try {
						cacheMapper.insert(userCache);
					} catch (Exception e) {
						log.error("insert UserMainInfo to DB Cache table ERROR.cache=" + userCache.toString(), e);
						return getErrorResult("更新记录失败");
					}
				} else if (userInformation.getDealFlag() == 5) {// 机架的所属客户为上报成功，用户自动提交预审
					userInfoService.approve(userInformation.getUserId() + "");
				}
			}
		}
		return getSuccessResult();
	}

	
	/*private ResultDto dealUserWhileHouseInfoChange(UserInformation userInformation, CacheIsmsBaseInfo cache) {
		//查看缓存信息表是否有该用户的等待记录
		CacheIsmsBaseInfo  cacheResult = cacheMapper.findByUserId(userInformation.getUserId());
		if(userInformation.getDealFlag()==1&&cacheResult==null){//机架的所属客户为提交上报，并且缓存中没有相应用户的缓存信息，写等待缓存表
			try {
				cacheMapper.insert(cache);
			} catch (Exception e) {
				log.error("insert UserMainInfo to DB Cache table ERROR.cache="+cache.toString(),e);
				return getErrorResult(" insert UserCacheINfo ERROR");
			}
		}else if(userInformation.getDealFlag()==4){//机架的所属客户为上报成功，用户自动提交预审
			//TODO:预审用户信息
		}
		return getSuccessResult();
	}*/

	/**
	 * 批量插入机房IP地址段信息
	 */
	@Override
	public ResultDto batchInsertHouseIpsegInfos(List<? extends HouseIPSegmentInformation> ipSegments, Long houseId, boolean allowInsert) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (ipSegments == null || ipSegments.size() == 0) {
			return result;
		}
		
		//IP数据重复性校验
		List<HouseIPSegmentInformation> ipList = new ArrayList<HouseIPSegmentInformation>();
		for (int i = 0; i < ipSegments.size(); i++) {
			HouseIPSegmentInformation dto = ipSegments.get(i);
			if(IpUtil.isIpAddress(dto.getStartIP())&&IpUtil.isIpAddress(dto.getEndIP())&&!IpUtil.isStartIPOverEndIp(dto.getStartIP(), dto.getEndIP())){
				ipList.add(dto);
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
		
		//校验
		boolean success = false;
		List<Integer> successList = new ArrayList<Integer>(ipSegments.size());
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>(ipSegments.size());
		for (int i = 0; i < ipSegments.size(); i++) {
			HouseIPSegmentInformation dto = ipSegments.get(i);
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
					// 新增机房Ip地址段信息
					int flag = insert(dto);
					if (flag <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert house IP segment success!");
					// 根据机房状态进行相应操作
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					log.info("update house czlx and deal_flag success!");
					// 对于已分配的用户当用户处于上报状态需更新用户信息
					dealIpUserWhileIPUpdate(dto);
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
		//用于返回新增的ID
		result.setAjaxValidationResultMap(validateResultMap);
		if (success && !allowInsert) {
			//循环返回新增的ID
			for (int n = 0; n < ipSegments.size(); n++) {
				if (houseId != null) {
					ipSegments.get(n).setHouseId(houseId);
				}
				
				// 新增机房Ip地址段信息
				int flag = insert(ipSegments.get(n));
				if (flag <= 0) {
					return getErrorResult("新增记录失败");
				}
				log.info("insert house IP segment success!");
				
				// 根据机房状态进行相应操作
				dealHouseMainWhileISubInfoUpdate(ipSegments.get(n).getHouseId());
				log.info("update house czlx and deal_flag success!");
				// 对于已分配的用户当用户处于上报状态需更新用户信息
				dealIpUserWhileIPUpdate(ipSegments.get(n));
				//返回新增ID
				result.getAjaxValidationResultMap().get(String.valueOf(n)).setPid(ipSegments.get(n).getIpSegId());
			}
		} 
		return result;
	}

	@Override
	public ResultDto batchDelete(List<? extends HouseIPSegmentInformation> ips) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (ips == null || ips.size() <= 0) {
			return validateResult;
		}
		validateResult = validateResult(ips, HouseConstant.OperationTypeEnum.DELETE.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate house ip segment success!");
			return deleteData(ips);
		} else {
			log.info("validate house ip segment failed!");
			return validateResult;
		}
	}

	private ResultDto deleteData(List<? extends HouseIPSegmentInformation> ips) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		List<Integer> successList = new ArrayList<Integer>();
		if (ips != null && ips.size() > 0) {
			for (HouseIPSegmentInformation ip : ips) {
				try {
					HouseIPSegmentInformation dto = houseIpsegMapper.findByIpSegId(ip.getIpSegId());
					if ((dto.getCzlx() == 1 && dto.getDealFlag() == 0)) {// 新增未上报
						// 仅删除IP地址段信息
						int flag = houseIpsegMapper.deleteByIpsegId(ip.getIpSegId());
						if (flag <= 0) {
							result.setResultMsg("删除记录失败");
						}
					} else {
						// 1.IP地址段的状态变更为删除未上报
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ipsegId", ip.getIpSegId());
						map.put("czlx", 3);// 删除
						map.put("dealFlag", 0);// 未上报
						houseIpsegMapper.updateIPSegtatusByIpsegId(map);
					}
					
					// 2.对于已分配的用户当用户处于上报状态需更新用户信息
					dealIpUserWhileIPUpdate(dto);

					// 3.将机房修改成变更未预审状态
					dealHouseMainWhileISubInfoUpdate(dto.getHouseId());
					successList.add(ResultDto.ResultCodeEnum.SUCCESS.getCode());
				} catch (Exception e) {
					result.setResultMsg("删除记录失败");
					log.error("delete ipsegId:" + ip.getIpSegId() + " ERROR", e);
				}
			}
			if (!successList.contains(ResultDto.ResultCodeEnum.ERROR.getCode())) {
				// 全部校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
			} else {
				// 部分校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
			}
			return result;
		} else {
			return result;
		}
	}

	@Override
	public ResultDto batchUpdate(List<? extends HouseIPSegmentInformation> dtos) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.MODIFY.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate house ip segment success!");
			return updateData(dtos);
		} else {
			log.info("validate house ip segment failed!");
			return validateResult;
		}
	}

	private ResultDto updateData(List<? extends HouseIPSegmentInformation> dtos) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (HouseIPSegmentInformation dto : dtos) {
				try {
					HouseIPSegmentInformation po = houseIpsegMapper.findByIpSegId(dto.getIpSegId());
					if(po.getDealFlag()!=null&&po.getDealFlag()==HouseConstant.DealFlagEnum.CHECK_FAIL.getValue()){//链路已报备后修改变更未上报
						dto.setCzlx(HouseConstant.CzlxEnum.MODIFY.getValue());
					}
					int flag = update(dto);
					if (flag <= 0) {
						// 插入或新增失败
						return getErrorResult("更新记录失败");
					}
					
					// 查询对应的机房
					HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
					houseInformationDTO.setHouseId(dto.getHouseId());
					HouseInformationDTO result = housePrincipalMapper.findByHouseId(houseInformationDTO);
					if (HouseConstant.DealFlagEnum.REPORTING.getValue().equals(result.getDealFlag())) {
						// 机房主体提交上报中
						CacheIsmsBaseInfo cacheIsmsBaseInfo = getCacheInfoBean(result);
						// 写缓存表
						writeCacheInfo(cacheIsmsBaseInfo);
					} else if (HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue().equals(result.getDealFlag())) {
						// 上报成功
						result.setCzlx(HouseConstant.OperationTypeEnum.MODIFY.getValue());// 变更状态
						result.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
						updateHouseDealFlagByHouseId(result);
					}
					// 用户数据处理
					//变更前关联用户信息处理
					if (!HouseConstant.IpUseTypeEnum.REMAIN.getValue().equals(po.getIpType())) {
						dealIpUserWhileIPUpdate(po);
					}
					//变更后关联用户信息处理
					if (!HouseConstant.IpUseTypeEnum.REMAIN.getValue().equals(dto.getIpType())) {
						dealIpUserWhileIPUpdate(dto);
					}
				} catch (Exception e) {
					log.error("update house ip segment data failed!", e);
					return getErrorResult("更新记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
    }


	/**
	 * 导入机房IP地址段信息
	 * @param dto
	 * @return
	 */
	public List<ResultDto> importHouseIpData(List<HouseIPSegmentInforDTO> dtoList){
		List<ResultDto> resultDtoList = Lists.newArrayList();
		if (dtoList==null||dtoList.isEmpty()){
			return resultDtoList;
		}
		for (int i=0;i<dtoList.size();i++){
			List<HouseIPSegmentInforDTO> houseIPSegmentInforDTOList = Lists.newArrayList();
			ResultDto resultDto ;
			HouseIPSegmentInforDTO houseIPSegmentInformation = new HouseIPSegmentInforDTO();
			HouseIPSegmentInforDTO dto = dtoList.get(i);
			if (!StringUtils.isEmpty(dto.getStartIP())){
				houseIPSegmentInformation.setStartIP(dto.getStartIP());
			}
			if (!StringUtils.isEmpty(dto.getEndIP())){
				houseIPSegmentInformation.setEndIP(dto.getEndIP());
			}
			if (!StringUtils.isEmpty(dto.getIpType())){
				houseIPSegmentInformation.setIpType(dto.getIpType());
			}
			HouseIPSegmentInformation result = houseIpsegMapper.findByIpAndType(houseIPSegmentInformation);
			if (result==null){
				houseIPSegmentInforDTOList.add(dto);
				resultDto = batchInsertHouseIpsegInfos(houseIPSegmentInforDTOList,dto.getHouseId(),true);
			} else {
				dto.setIpSegId(result.getIpSegId());
				dto.setOperateType(2);
				houseIPSegmentInforDTOList.add(dto);
				resultDto = batchUpdate(houseIPSegmentInforDTOList);
			}
			resultDtoList.add(resultDto);
		}

		return resultDtoList;
	}

	private ResultDto insertData(HouseIPSegmentInforDTO dto){
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

	private ResultDto updateData(HouseIPSegmentInforDTO dto){
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
