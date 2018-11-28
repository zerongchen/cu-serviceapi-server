package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aotain.cu.serviceapi.dto.*;
import com.aotain.serviceapi.server.dao.CommonUtilMapper;
import com.aotain.serviceapi.server.dao.preinput.*;
import com.aotain.serviceapi.server.service.preinput.*;
import com.aotain.serviceapi.server.util.IpUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.CacheIsmsBaseInfo;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.IdcJcdmXzqydmMapper;
import com.aotain.serviceapi.server.validate.IHouseFrameValidator;
import com.aotain.serviceapi.server.validate.IHouseIpSegmentValidator;
import com.aotain.serviceapi.server.validate.IHouseLinkValidator;
import com.aotain.serviceapi.server.validate.IHousePrincipalValidator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName HouseInformationServiceImpl
 * @Author tanzj
 * @Date 2018/7/23
 **/
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class IHouseInformationServiceImpl extends BaseService implements IHouseInformationService {

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private HouseFrameMapper houseFrameMapper;

    @Autowired
    private HouseLinkMapper houseLinkMapper;

    @Autowired
    private HouseIpSegmentMapper houseIpSegmentMapper;
    
    @Autowired
	private UserPrincipalMapper userPrincipalMapper;
	
    @Autowired
	private HouseLinkServiceImpl houseLinkService;
    
    @Autowired
	private PreHouseFrameServiceImpl preHouseFrameService;
    
    @Autowired
	private PreHouseIpsegServiceImpl preHouseIpsegService;
    
	@Autowired
	private CacheIsmsBaseInfoMapper cacheMapper;
	
	@Autowired
	private IdcJcdmXzqydmMapper idcJcdmXzqydmMapper;
	
	@Autowired
	private IHouseFrameValidator houseFrameValidatorImpl;
	
	@Autowired
	private IHouseLinkValidator houseLinkValidatorImpl;
	
	@Autowired
	private IHouseIpSegmentValidator houseIpSegmentValidatorImpl;

	@Autowired
	private PreUserHHService preUserHHService;

	@Autowired
	private PreUserVirtualService preUserVirtualService;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private CommonUtilMapper commonMapper;
    
    private Logger logger = Logger.getLogger(IHouseInformationServiceImpl.class);

    public IHouseInformationServiceImpl(@Qualifier(value="housePrincipalValidatorImpl") IHousePrincipalValidator housePrincipalValidator){
        super(housePrincipalValidator);
    }

    @Override
    public List<HouseInformationDTO> getHouseByJyzId(HouseInformationDTO dto) {
        return housePrincipalMapper.getHouseIndexList(dto);
    }

    /**
     * 机房主体信息
     * @param dto
     * @return
     */
	@Override
	public PageResult<HouseInformationDTO> listHouseInfo(HouseInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<HouseInformationDTO> result = new PageResult<HouseInformationDTO>();
		// 三个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				||StringUtils.isEmpty(dto.getAuthHouses())
				||StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return result;
		}
		List<HouseInformationDTO> info = new ArrayList<HouseInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = housePrincipalMapper.getHouseIndexList(dto);
			PageInfo<HouseInformationDTO> pageResult = new PageInfo<HouseInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = housePrincipalMapper.getHouseIndexList(dto);
		}
		result.setRows(info);
		return result;
	}
	
    /**
     * 根据机房Id查机房
     * @param dto
     * @return
     */
    @Override
    public HouseInformationDTO getHouseInfoById(HouseInformationDTO dto) {
        return housePrincipalMapper.getHouseInfoById(dto);
    }

    /**
     * 机架主体信息
     * @param dto
     * @return
     */
	@Override
	public PageResult<HouseFrameInformationDTO> getIndexHouseFrame(HouseFrameInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<HouseFrameInformationDTO> result = new PageResult<HouseFrameInformationDTO>();
		// 三个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				||StringUtils.isEmpty(dto.getAuthHouses())
				||StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return result;
		}
		List<HouseFrameInformationDTO> info = new ArrayList<HouseFrameInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = houseFrameMapper.getIndexHouseFrame(dto);
			PageInfo<HouseFrameInformationDTO> pageResult = new PageInfo<HouseFrameInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = houseFrameMapper.getIndexHouseFrame(dto);
		}
		for (HouseFrameInformationDTO tmp : info) {
			Map<String, Long> query = new HashMap<>();
			query.put("houseId", tmp.getHouseId());
			query.put("frameId", tmp.getFrameId());
			List<String> ret = houseFrameMapper.getUnitName(query);
			if (ret != null) {
				tmp.setUnitNameList(ret);
			}
		}
		result.setRows(info);
		return result;
	}

    /**
     * 链路信息查询（分页）
     * @param dto
     * @return
     */
	@Override
	public PageResult<HouseGatewayInformationDTO> getIndexLink(HouseGatewayInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<HouseGatewayInformationDTO> result = new PageResult<HouseGatewayInformationDTO>();
		// 三个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				||StringUtils.isEmpty(dto.getAuthHouses())
				||StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return result;
		}
		List<HouseGatewayInformationDTO> info = new ArrayList<HouseGatewayInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = houseLinkMapper.getIndexHouseLink(dto);
			PageInfo<HouseGatewayInformationDTO> pageResult = new PageInfo<HouseGatewayInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = houseLinkMapper.getIndexHouseLink(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public PageResult<HouseIPSegmentInforDTO> getIndexIpSegment(HouseIPSegmentInforDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<HouseIPSegmentInforDTO> result = new PageResult<HouseIPSegmentInforDTO>();
		// 三个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				||StringUtils.isEmpty(dto.getAuthHouses())
				||StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return result;
		}
		//ip地址查询处理
		if(dto.getStartIP()!=null && !"".equals(dto.getStartIP())){
			if(IpUtil.isIpAddress(dto.getStartIP())){
				if(IpUtil.isIpv4(dto.getStartIP())){
					dto.setStartIPStr(String.valueOf(IpUtil.ipv4ToLong(dto.getStartIP())));
				}else if(IpUtil.isIpv6(dto.getStartIP())){
					dto.setStartIPStr(String.valueOf(IpUtil.ipv6ToBigInteger(dto.getStartIP())));
				}
			}
		}

		List<HouseIPSegmentInforDTO> info = new ArrayList<HouseIPSegmentInforDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = houseIpSegmentMapper.getIndexHouseIpSegment(dto);
			PageInfo<HouseIPSegmentInforDTO> pageResult = new PageInfo<HouseIPSegmentInforDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = houseIpSegmentMapper.getIndexHouseIpSegment(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public ResultDto approve(String houseId) {
		HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
		try {
			houseInformationDTO.setHouseId(Long.valueOf(houseId));
		} catch (Exception e) {
			getErrorResult("参数不合法，houseId=" + houseId);
		}
		HouseInformation houseInformation = housePrincipalMapper.findByHouseId(houseInformationDTO);
		if (houseInformation == null) {
			return getErrorResult("机房不存在");
		}
		int dealFlag = 0;
		ResultDto resultDto = new ResultDto();
		if (HouseConstant.CzlxEnum.DELETE.getValue().equals(houseInformation.getCzlx())) {
			dealFlag = HouseConstant.DealFlagEnum.CHECKING.getValue();
		} else {
			resultDto = integrityVerification(Long.valueOf(houseId), Integer.valueOf(houseInformation.getIdentify()));
			if (ResultDto.ResultCodeEnum.ERROR.getCode().equals(resultDto.getResultCode())) {
				//机房信息不完整，预审不通过
				dealFlag = HouseConstant.DealFlagEnum.CHECK_FAIL.getValue();
			} else {
				dealFlag = HouseConstant.DealFlagEnum.CHECKING.getValue();
			}
		}
		//更新机房状态
		if (HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue().equals(houseInformation.getDealFlag())) {// 机房为上报成功时更改机房状态
			HouseInformation houseDto = new HouseInformation();
			houseDto.setHouseId(Long.valueOf(houseId));
			houseDto.setCzlx(HouseConstant.OperationTypeEnum.MODIFY.getValue());// 变更状态
			houseDto.setDealFlag(dealFlag);
			if (resultDto.getAjaxValidationResult() != null) {
				houseDto.setVerificationResultWithTimestamp(resultDto.getAjaxValidationResult().getErrorsToString());
			} else {
				houseDto.setVerificationResult("");
			}
			housePrincipalMapper.updateDealFlagById(houseDto);
		} else if (HouseConstant.DealFlagEnum.REPORTING.getValue().equals(houseInformation.getDealFlag())) {
			// 机房处于提交上报，不能预审，返回成功
			return getErrorResult("预审不通过");
		} else {
			HouseInformation houseDto = new HouseInformation();
			houseDto.setHouseId(Long.valueOf(houseId));
			houseDto.setDealFlag(dealFlag);
			if (resultDto.getAjaxValidationResult() != null) {
				houseDto.setVerificationResultWithTimestamp(resultDto.getAjaxValidationResult().getErrorsToString());
			} else {
				houseDto.setVerificationResult("");
			}
			housePrincipalMapper.updateDealFlagById(houseDto);
			return getSuccessResult("预审通过");
		}
		return getSuccessResult("预审通过");
	}

	@Override
	public ResultDto revertApprove(String houseId) {
		HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
		try {
			houseInformationDTO.setHouseId(Long.valueOf(houseId));
		} catch (Exception e) {
			getErrorResult("参数不合法，houseId=" + houseId);
		}
		HouseInformation houseInformation = housePrincipalMapper.findByHouseId(houseInformationDTO);
		if (houseInformation == null) {
			return getErrorResult("机房不存在");
		}
		// 修改机房状态为未预审
		if (houseInformation.getDealFlag() == HouseConstant.DealFlagEnum.CHECKING.getValue()) {
			houseInformation.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
			int record = housePrincipalMapper.updateDealFlagById(houseInformation);
			if (record <= 0) {
				return getErrorResult("撤销机房预审失败");
			}
			return getSuccessResult("撤销机房预审成功");
		}
		return getErrorResult("撤销机房预审失败");
	}

    @Override
    public List<ApproveResultDto> findCheckResultById(String approveId){
		/*
        HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
        try{
            houseInformationDTO.setHouseId(Long.valueOf(houseId));
        } catch (Exception e){
            getErrorResult("参数不合法，houseId="+houseId);
        }
        HouseInformation houseInformation = housePrincipalMapper.findByHouseId(houseInformationDTO);
        if (houseInformation==null){
            return getErrorResult("机房不存在");
        }*/
		ApproveResultDto dto = new ApproveResultDto();
		dto.setApproveId(Long.valueOf(approveId));
        return commonMapper.getApproveResult(dto);
    }

    @Override
    protected int insert(BaseModel baseModel) {
		HouseInformation dto = (HouseInformation) baseModel;
		int result = 0;
		try {
			dto.setJyzId(Integer.valueOf(obtainJyzId() + ""));
			result = housePrincipalMapper.insertHouseInformation(dto);
		} catch (Exception e) {
			logger.error("insert preHouseMainInfo to DB ERROR.dto：" + dto.toString() + e);
		}
		return result;
	}

    @Override
    protected int update(BaseModel baseModel) {
		HouseInformation dto = (HouseInformation)baseModel;
		int result = 0;
		try {
			result = housePrincipalMapper.updateHouseInformation(dto);
		} catch (Exception e) {
			logger.error("update preHouseMainInfo to DB ERROR.dto："+dto.toString(),e);
		}
		return result;
	}

    @Override
    protected int delete(BaseModel baseModel) {
		HouseInformation dto = (HouseInformation)baseModel;
		return housePrincipalMapper.delete(dto);
	}

	@Override
	public ResultDto integrityVerification(long houseId, int identity) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		AjaxValidationResult ajaxValidationResult = new AjaxValidationResult();
		int linkCount = housePrincipalMapper.integrityVerificationForHouseLink(houseId);
		int frameCount = housePrincipalMapper.integrityVerificationForHouseFrame(houseId);
		int ipCount = housePrincipalMapper.integrityVerificationForHouseIPSeg(houseId);
		if (linkCount > 0 && ipCount > 0 && identity == HouseConstant.IdentityType.DEDICATED_HOUSE.getValue()) {
			//专线机房没有机架
			result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
			return result;
		} else {
			boolean passed = true;;
			if (linkCount == 0) {
				ajaxValidationResult.getErrorsArgsMap().put("house.link", new String[] {"缺失机房链路信息,请补充后再预审"});
				passed = false;
			}
			if (identity == HouseConstant.IdentityType.IDC_HOUSE.getValue()) {
				//IDC机房必须要有机架
				if (frameCount == 0) {
					ajaxValidationResult.getErrorsArgsMap().put("house.frame", new String[] {"缺失机房机架信息,请补充后再预审"});
					passed = false;
				}
			} 
			if (ipCount == 0) {
				ajaxValidationResult.getErrorsArgsMap().put("house.ip", new String[] {"缺失机房IP地址段信息,请补充后再预审"});
				passed = false;
			}
			result.setResultCode(passed ? ResultDto.ResultCodeEnum.SUCCESS.getCode():ResultDto.ResultCodeEnum.ERROR.getCode());
			result.setAjaxValidationResult(ajaxValidationResult);
		}
		return result;
	}

	@Transactional
	@Override
	public ResultDto insertData(HouseInformation dto) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		long houseId = 0;
		try {
			// 校验机房主体信息
			boolean houseMainSuccess = false;
			boolean houseFrameSuccess = false;
			boolean houseLinkSuccess = false;
			boolean houseIpSuccess = false;
			dto.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
			validateResult = validateResult(dto);
			if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
				houseMainSuccess = true;
				logger.info("validate house main success!");
			} else {
				logger.info("validate house main failed!");
				return validateResult;
			}
			if(houseMainSuccess) {
				dto.setAreaCodeValidateType(2);
				if (dto.getFrameList() != null && dto.getFrameList().size() > 0) {
					// 机房机架信息校验
					validateResult = validateResult(houseFrameValidatorImpl, dto.getFrameList(), HouseConstant.OperationTypeEnum.ADD.getValue());
					if (validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
						houseFrameSuccess = true;
					} else {
						logger.info("validate house frame failed!");
						return validateResult;
					}
				} 
				if (dto.getGatewayInfoList() != null && dto.getGatewayInfoList().size() > 0) {
					// 机房链路信息校验
					validateResult = validateResult(houseLinkValidatorImpl, dto.getGatewayInfoList(), HouseConstant.OperationTypeEnum.ADD.getValue());
					if (validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
						houseLinkSuccess = true;
					} else {
						logger.info("validate house link failed!");
						return validateResult;
					}
				}
				if (dto.getIpSegList() != null && dto.getIpSegList().size() > 0) {
					// 机房IP地址段信息校验
					validateResult = validateResult(houseIpSegmentValidatorImpl, dto.getIpSegList(), HouseConstant.OperationTypeEnum.ADD.getValue());
					if (validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
						houseIpSuccess = true;
					} else {
						logger.info("validate house ip failed!");
						return validateResult;
					}
				}
				//机房主体新增入库
				int result = insert(dto);
				if (result <= 0) {// 机房主体新增失败
					return getErrorResult("新增记录失败");
				}
				houseId = dto.getHouseId();
				try {
					synchronizeAuthHousesToPermission(dto);
					//	机房同步至redis
					synchronizeAuthHousesToRedis(dto, HouseConstant.OperationTypeEnum.ADD.getValue());
				} catch (Exception e) {
					logger.error("synchronize house information failed", e);
					return getErrorResult("机房授权失败");
				}
				if (houseFrameSuccess) {
					preHouseFrameService.insertData(dto.getFrameList(), houseId);
				}
				if (houseLinkSuccess) {
					houseLinkService.insertData(dto.getGatewayInfoList(), houseId);
				}
				if (houseIpSuccess) {
					preHouseIpsegService.insertData(dto.getIpSegList(), houseId);
				}
				return getSuccessResult(Integer.valueOf(houseId + ""));
			}
		} catch (Exception e) {
			logger.error("insert house failed!", e);
			return getErrorResult("新增记录失败");
		}
		return validateResult;
	}
	
	@Override
	public ResultDto updateData(HouseInformationDTO dto) {
		dto.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());
		// 获取经jyzid
		dto.setJyzId(Integer.valueOf(obtainJyzId() + ""));
		// zipCode
		IdcJcdmXzqydm idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(String.valueOf(dto.getHouseCounty()));
		if (idcJcdmXzqydm != null)
			dto.setHouseZipCode(idcJcdmXzqydm.getPostCode());

		// dealFlag
		dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
		dto.setUpdateTime(new Date());
		// czlx
		HouseInformationDTO info = housePrincipalMapper.findByHouseId(dto);
		if (HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue().equals(info.getDealFlag())) {// 机房为上报成功时更改机房状态
			dto.setCzlx(HouseConstant.OperationTypeEnum.MODIFY.getValue());// 变更状态
			dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
		} else if (HouseConstant.DealFlagEnum.REPORTING.getValue().equals(info.getDealFlag())) {
			// 机房处于提交上报
			CacheIsmsBaseInfo caBaseInfo = new CacheIsmsBaseInfo();
			caBaseInfo.setHouseId(dto.getHouseId());
			caBaseInfo.setJyzId(obtainJyzId());
			CacheIsmsBaseInfo resultCahce = cacheMapper.findByHouseId(caBaseInfo);
			if (resultCahce == null) {// 若缓存表中没有有该机房记录则写入
				cacheMapper.insert(caBaseInfo);
			}
		} else {
			dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
		}
		// 1.校验机房主体信息
		boolean success = false;
		ResultDto validateResult = validateResult(dto);
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			success = true;
			logger.info("validate house success!");
		}
		if ( !success ) {
			return validateResult;
		}

		// 2.update主体新增入库
		int result = update(dto);
		if (result <= 0) {// 机房主体更新失败
			logger.error("更新记录失败");
			return validateResult;
		}
		try {
			HouseInformation bean = new HouseInformation();
			bean.setHouseId(dto.getHouseId());
			bean.setHouseIdStr(dto.getHouseIdStr());
			bean.setHouseName(dto.getHouseName());
			bean.setIdentity(dto.getIdentity());
			//	机房同步至权限系统
			synchronizeAuthHousesToPermission(dto);
			//	机房同步至redis
			synchronizeAuthHousesToRedis(dto, HouseConstant.OperationTypeEnum.MODIFY.getValue());
		} catch (Exception e) {
			logger.error("synchronize house information failed", e);
			return getErrorResult("同步机房数据至 权限系统或者redis失败");
		}
		return getSuccessResult(Integer.parseInt(dto.getHouseId() + ""));
	}

	@Override
	public ResultDto deleteHouseInfo(HouseInformationDTO houseDto) {
		try {
			// 1.删除链路信息
			houseLinkMapper.deleteByHouseId(houseDto.getHouseId());
			// 2.删除用户机架信息
			houseFrameMapper.deleteUserFrameByUserId(houseDto.getHouseId());
			// 3.删除机架信息
			houseFrameMapper.deleteByHouseId(houseDto.getHouseId());
			// 4.删除IP地址段信息
			houseIpSegmentMapper.deleteByHouseId(houseDto.getHouseId());
			// 5.删除机房主体信息
			housePrincipalMapper.deleteByHouseId(houseDto.getHouseId());
			
			try {
				HouseInformation bean = new HouseInformation();
				bean.setHouseId(houseDto.getHouseId());
				bean.setHouseIdStr(houseDto.getHouseIdStr());
				bean.setHouseName(houseDto.getHouseName());
				bean.setIdentity(houseDto.getIdentity());
				bean.setUserToken(houseDto.getUserToken());
				bean.setPermissionMethodUrl(houseDto.getPermissionMethodUrl());
				bean.setDataPermissionSettingList(houseDto.getDataPermissionSettingList()); 
				bean.setDataPermissionId(houseDto.getDataPermissionId());
				bean.setDataPermissionDesc(houseDto.getDataPermissionDesc());
				bean.setDataPermissionName(houseDto.getDataPermissionName());
				bean.setDataPermissionToken(houseDto.getDataPermissionToken());
				bean.setAppId(houseDto.getAppId());
				bean.setOperateType(HouseConstant.OperationTypeEnum.DELETE.getValue());
				//  机房同步至权限系统
				synchronizeAuthHousesToPermission(bean);
				//	机房同步至redis
				synchronizeAuthHousesToRedis(bean, HouseConstant.OperationTypeEnum.DELETE.getValue());
			} catch (Exception e) {
				logger.error("synchronize house information failed", e);
				return getErrorResult("同步机房数据至 权限系统或者redis失败");
			}
		} catch (Exception e) {
			logger.error("[delete houseInfo from DB ERROR.houseId=" + houseDto.getHouseId() + "]", e);
			return getErrorResult("删除机房主体信息失败");
		}
		return getSuccessResult();
	}

	@Override
	public ResultDto batchDeleteHouseInfos(List<HouseInformationDTO> deleteList) {
		if (deleteList == null || deleteList.size() == 0) {
			return getErrorResult("删除集合参数对象为空");
		}
		HouseInformationDTO houseResult = null;

		List<String> errorHouseIds = Lists.newArrayList();
		for (HouseInformationDTO houseDto : deleteList) {
			try{
				houseResult = housePrincipalMapper.findByHouseId(houseDto);
				houseDto.setHouseIdStr(houseResult.getHouseIdStr());
				if (houseResult.getDealFlag() == HouseConstant.DealFlagEnum.REPORTING.getValue()) {
					// 查询机房状态，提交上报时的机房主体不做删除
					continue;
				}
				ResultDto resultDto = new ResultDto();
				if ((houseResult.getCzlx() == HouseConstant.CzlxEnum.ADD.getValue()
						&& houseResult.getDealFlag() == HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue())
						|| houseResult.getCzlx() == HouseConstant.CzlxEnum.MODIFY.getValue()) {
					resultDto = dealHouseInfoWhischHaveReport(houseDto.getHouseId(), houseResult.getCzlx());
				} else {// 物理级联删除机房信息
					if (houseResult.getCzlx() == HouseConstant.CzlxEnum.ADD.getValue()
							&& (houseResult.getDealFlag() != HouseConstant.DealFlagEnum.CHECKING.getValue()
							|| houseResult.getDealFlag() == HouseConstant.DealFlagEnum.REPORTING.getValue())) {
						resultDto = deleteHouseInfo(houseDto);
					}
				}
				// 此机房删除失败
				if (resultDto.getResultCode()==ResultDto.ResultCodeEnum.ERROR.getCode()){
					errorHouseIds.add(houseDto.getHouseId().toString());
				}
			} catch (Exception e){
				logger.info("delete house fail ",e);
				errorHouseIds.add(houseDto.getHouseId().toString());
			}

		}
		if (errorHouseIds.isEmpty()){
			return getSuccessResult();
		} else {
			return getErrorResult("一共删除记录"+(deleteList==null?0:deleteList.size())+"条，其中"+formatListToString(errorHouseIds,"、")+"机房删除失败！");
		}

	}
	
	private ResultDto dealHouseInfoWhischHaveReport(long houseId, int czlx) {
		// 查询该机房下已上报的机架和IP关联的用户信息
		List<UserInformation> userList = housePrincipalMapper.findHaveReportFrameAndIPsegConnectUserInfoByHouseId(houseId);
		if (userList != null && userList.size() > 0) {
			for (UserInformation userInfo : userList) {
				// 未分配的机架不处理
				if (userInfo==null||StringUtils.isEmpty(userInfo.getUnitName())){
					continue;
				}
				UserInformation userInformation = userPrincipalMapper.findByUnitNameAndIdTypeAndNumber(userInfo);
				if (userInformation != null) {
					CacheIsmsBaseInfo userCache = new CacheIsmsBaseInfo();
					userCache.setHouseId(houseId);
					userCache.setUserId(userInformation.getUserId());
					// 查看缓存信息表是否有该用户的等待记录
					CacheIsmsBaseInfo cacheResult = cacheMapper.findByUserId(userInformation.getUserId());
					if (userInformation.getDealFlag() == 4 && cacheResult == null) {// 机架的所属客户为提交上报，并且缓存中没有相应用户的缓存信息，写等待缓存表
						try {
							cacheMapper.insert(userCache);
						} catch (Exception e) {
							logger.error("insert UserMainInfo to DB Cache table ERROR.cache=" + userCache.toString(), e);
							return getErrorResult(" insert UserCacheINfo ERROR");
						}
					} else if (userInformation.getDealFlag() == 5) {// 机架的所属客户为上报成功，用户自动提交预审
						// 同时将已上报的带宽和虚拟机删除上报，并将用户置为未上报状态
						preUserHHService.updateRelativeDataInHouseDeleteByUserIdAndHouseId(userInformation.getUserId(),houseId);
						preUserVirtualService.updateRelativeDataInHouseDeleteByUserIdAndHouseId(userInformation.getUserId(),houseId);
						userInformation.setDealFlag(0);
						userInformation.setCzlx(2);
						userInformation.setOperateType(2);
						userInfoMapper.updateUserInfo(userInformation);
					}
				}
			}
		}
		// 机房主体改成删除上报审核中
		HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
		houseInformationDTO.setHouseId(houseId);
		HouseInformationDTO houseResult = housePrincipalMapper.findByHouseId(houseInformationDTO);
		houseResult.setCzlx(HouseConstant.CzlxEnum.DELETE.getValue());// 变更状态
		houseResult.setDealFlag(HouseConstant.DealFlagEnum.CHECKING.getValue());// 未预审
		updateHouseDealFlagByHouseId(houseResult);
		return getSuccessResult();
	}

	/**
	 * 导入数据
	 * @param dtoList
	 * @return
	 */
	public List<ResultDto> importHouseData(List<HouseInformationDTO> dtoList){
		if (dtoList==null || dtoList.size()==0){
			getSuccessResult();
		}
		List<ResultDto> resultDtoList = Lists.newArrayList();
		for (int i=0;i<dtoList.size();i++){
			HouseInformationDTO houseInformation = dtoList.get(i);
			HouseInformationDTO result = housePrincipalMapper.findByHouseId(dtoList.get(i));
			ResultDto resultDto = new ResultDto();
			if (result==null){
				resultDto = insertData(houseInformation);
			} else {
//				houseInformation.setCzlx(2);
				resultDto = updateData(houseInformation);
			}
			resultDtoList.add(resultDto);
		}
		return resultDtoList;
	}

	/**
	 * 同步机房数据
	 * @return
	 */
	public ResultDto synchHouse(BaseModel baseModel){
		try{
			synchronizeAllHousesToPermission(baseModel);
		} catch (Exception e){
			logger.info("synchronized house to auth fail...");
			return getErrorResult("同步机房数据失败");
		}
		return getSuccessResult();
	}
}
