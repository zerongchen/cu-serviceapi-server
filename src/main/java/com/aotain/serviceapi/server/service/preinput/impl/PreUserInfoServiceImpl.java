package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.*;

import com.aotain.cu.serviceapi.dto.*;
import com.aotain.cu.serviceapi.model.*;
import com.aotain.serviceapi.server.dao.CommonUtilMapper;
import com.aotain.serviceapi.server.dao.preinput.*;
import com.aotain.serviceapi.server.dao.report.IdcInfoMapper;
import com.aotain.serviceapi.server.util.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.aotain.common.config.LocalConfig;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.CacheIsmsBaseInfo;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;
import com.aotain.cu.serviceapi.model.HouseUserFrameInformation;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.cu.serviceapi.model.UserServiceInformation;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.GlobalParams;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.IdcJcdmXzqydmMapper;
import com.aotain.serviceapi.server.dao.dic.DictionaryMapper;
import com.aotain.serviceapi.server.model.msg.JobQueue;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.PreUserBandWidthService;
import com.aotain.serviceapi.server.service.preinput.PreUserInfoService;
import com.aotain.serviceapi.server.service.preinput.PreUserServerService;
import com.aotain.serviceapi.server.service.preinput.PreUserVirtualService;
import com.aotain.serviceapi.server.validate.IUserNetworkValidator;
import com.aotain.serviceapi.server.validate.IUserPrincipalValidator;
import com.aotain.serviceapi.server.validate.IUserServiceValidator;
import com.aotain.serviceapi.server.validate.IUserVirtualMachineValidator;
import com.aotain.serviceapi.server.validate.impl.UserPrincipalValidatorImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import scala.annotation.meta.param;


@Service
public class PreUserInfoServiceImpl extends BaseService implements PreUserInfoService{

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Autowired
    private UserVirtualMachineMapper userVirtualMachineMapper;
    
    @Autowired
    private UserHHMapper userHHMapper;

    @Autowired
    private UserBandWidthMapper userBandWidthMapper;

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
	private CacheIsmsBaseInfoMapper cacheMapper;


    @Autowired
    private IdcJcdmXzqydmMapper idcJcdmXzqydmMapper;

    @Autowired
    private PreCommonMapper preCommonMapper;

    @Autowired
    private DictionaryMapper dictionaryMapper;
    
    @Autowired
    private PreUserServerService preUserServerServiceImpl;
    
    @Autowired
    private PreUserBandWidthService preUserBandWidthServiceImpl;
    
    @Autowired
    private PreUserVirtualService preUserVirtualServiceImpl;
    
    @Autowired
    private HouseIpSegmentMapper houseIpSegmentMapper;

    @Autowired
    private IdcInfoMapper idcInfoMapper;

	@Autowired
	private CommonUtilMapper commonMapper;
    
    @Autowired
	private HouseFrameMapper frameMapper;
    
    private Logger log = Logger.getLogger(PreUserInfoServiceImpl.class);

    public PreUserInfoServiceImpl(UserPrincipalValidatorImpl baseValidator) {
        super(baseValidator);
    }

    @Override
    public List<UserInformation> findListByJyz(Integer jyzId) {
        return userInfoMapper.findListByJyz(jyzId);
    }

    @Override
//    @Transactional
    public ResultDto changeUserNature( UserInformation dto ) {
        try {
            //1——提供应用服务、2—— 其他 (1:1-->2 ,2:2-->1)
            switch (dto.getNature()){
                case 1:
                    //新增 上报成功的
                    if (dto.getOperateType().intValue()== HouseConstant.OperationTypeEnum.ADD.getValue().intValue()
                            && dto.getDealFlag()== HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue()){
                            //czlx是新增上报成功的
							updateUserAndWirteServiceDeleteLog(dto);
                            UserInformation info = new UserInformation();
                            info.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
                            info.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());
                            info.setUserId(dto.getUserId());
                            info.setNature(2);
                            userInfoMapper.updateUserInfo(info);
                    }else {
                        //修改的
                        if(dto.getOperateType().intValue()!= HouseConstant.OperationTypeEnum.MODIFY.getValue()){
                            //czlx 不是变更的情况下(实际上就是只有新增，未上报的),删除用户下所有服务信息、虚拟主机信息,物理删除
                            userServiceMapper.deleteByUserId(dto.getUserId());
                            userServiceMapper.deleteServerDomainByUserId(dto.getUserId());
                            userServiceMapper.deleteVirtualByUserId(dto.getUserId());
                        }else {
							updateUserAndWirteServiceDeleteLog(dto);
                        }
                        UserInformation info = new UserInformation();
                        info.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
                        info.setUserId(dto.getUserId());
                        info.setNature(2);
                        userInfoMapper.updateUserInfo(info);
                    }
                    return getSuccessResult();
                case 2:
                    //新增上报成功的 || 变更的
                    if ((dto.getOperateType().intValue()== HouseConstant.OperationTypeEnum.ADD.getValue().intValue()
                            && dto.getDealFlag()== HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue())
                            || dto.getOperateType().intValue() == HouseConstant.OperationTypeEnum.MODIFY.getValue()){
                        //czlx是新增上报成功的
                        updateUserAndWirteBandwithLog(dto);
                        UserInformation info = new UserInformation();
                        info.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
                        info.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());
                        info.setUserId(dto.getUserId());
                        info.setNature(1);
                        userInfoMapper.updateUserInfo(info);
                    }else {
                        //用户主体变更未上报
                        UserInformation info = new UserInformation();
                        info.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
                        info.setUserId(dto.getUserId());
                        info.setNature(1);
                        userInfoMapper.updateUserInfo(info);
                    }
                    return getSuccessResult();
            }
        }catch (Exception e){
            log.error("用户属性变更失败,请检查传入参数...",e);
            return getErrorResult("用户属性变更失败,请检查传入参数");
        }
        return null;
    }

    /**
     *
     */
    public void updateUserAndWirteBandwithLog( UserInformation dto) throws Exception {
        List<UserBandwidthInformation> bands = userHHMapper.getHHByUserId(dto.getUserId());
            //操作表带宽变成新增 未上报
            Map<String,Object> map =new HashMap<>();
            map.put("czlx", HouseConstant.OperationTypeEnum.ADD.getValue());
            map.put("dealFlag", HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
            map.put("userId",dto.getUserId());
            userHHMapper.UpdateDAndCByUserId(map);

            //写等带表
            UserInformation param = new UserInformation();
            preCommonMapper.writeLog(param);
            param.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());
            param.setUserId(dto.getUserId());
            param.setJyzId(dto.getJyzId());
            param.setNature(dto.getNature());
            preCommonMapper.writeUserLog(param);
            if (bands!=null && !bands.isEmpty()){
                bands.forEach(band->{
                    band.setUserSeqId(param.getUserSeqId());
                    band.setOperateType(3);
                    preCommonMapper.writeUserBandLog(band);
                });
            }
				writeKafkaMag(param.getSubmitId());
    }
    /**
     * 变更操作变并且写kafka 等待表(用户服务)
     * @param dto
     */
    public void updateUserAndWirteServiceDeleteLog( UserInformation dto) throws Exception {

    	/**
    	 * 新增已上报或变更的服务信息、虚拟主机信息逻辑删除，新增未上报的直接物理删除干掉，删除未上报的不予处理
    	 */
    	
        //操作表逻辑删除用户服务
        userServiceMapper.deleteUserServiceLogic(dto.getUserId());
        userServiceMapper.deleteUserServiceDomainLogic(dto.getUserId());
        //操作表逻辑删除虚拟主机
        userVirtualMachineMapper.deleteVirByUserIdByLogic(dto.getUserId());
        
        //操作表物理删除新增未上报的服务信息
        userServiceMapper.deleteUserServiceWhichNotReport(dto.getUserId());
        userServiceMapper.deleteUserServiceDomainWhichNotReport(dto.getUserId());
        //操作表物理删除新增未上报的虚拟主机信息
        userVirtualMachineMapper.deleteVirByUserIdWhichNotReport(dto.getUserId());
        
        
        List<UserServiceInformation> services = userServiceMapper.getServiceByUserId(dto.getUserId());
        List<UserVirtualInformation> virtuals = userVirtualMachineMapper.getVirtualByUserId(dto.getUserId());

//            //操作表带宽变成新增 未上报
//            Map<String,Object> map =new HashMap<>();
//            map.put("czlx", HouseConstant.OperationTypeEnum.ADD.getValue());
//            map.put("dealFlag", HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
//            map.put("userId",dto.getUserId());
//            userHHMapper.UpdateDAndCByUserId(map);

		UserInformation param = new UserInformation();
		//写WAIT_ISMS_SUBMIT_REPORTLOG
		preCommonMapper.writeLog(param);
		param.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());
		param.setUserId(dto.getUserId());
		param.setJyzId(dto.getJyzId());
		param.setNature(dto.getNature());
		//写等待表
		preCommonMapper.writeUserLog(param);
		if (services!=null && !services.isEmpty()){
			services.forEach(service->
			{
				service.setUserSeqId(param.getUserSeqId());
				service.setOperateType(3);
				preCommonMapper.writeUserServiceLog(service);
			});
		}
		if (virtuals!=null && !virtuals.isEmpty()){
			virtuals.forEach(vir->
			{
				vir.setUserSeqId(param.getUserSeqId());
				vir.setOperateType(3);
				preCommonMapper.writeUserVirtualLog(vir);
			});
		}
			writeKafkaMag(param.getSubmitId());
    }


	@Override
	protected int insert(BaseModel baseModel) {
		UserInformation dto = (UserInformation) baseModel;
		dto.setJyzId(Integer.valueOf(obtainJyzId() + ""));
		dto.setUserCode(dto.getUnitName());
		// zipCode
		IdcJcdmXzqydm idcJcdmXzqydm;
		if (dto.getUnitAddressAreaCode() != null && dto.getUnitZipCode()==null) {
			idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(dto.getUnitAddressAreaCode());
			dto.setUnitZipCode(idcJcdmXzqydm.getPostCode());
			if (StringUtil.isEmptyString(dto.getUnitAddressAreaName())) {
				dto.setUnitAddressAreaName(idcJcdmXzqydm.getMc());
			}
		}
		// czlx
		dto.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
		dto.setCzlx(HouseConstant.OperationTypeEnum.ADD.getValue());
		// dealFlag
		dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
		dto.setUpdateTime(new Date());
		int result = 0;
		try {
			result = userInfoMapper.insertUserInfo(dto);
		} catch (Exception e) {
			log.error("insert preUserMainInfo to DB ERROR.dto：" + dto.toString(), e);
		}
		return result;
	}

    @Override
    protected int update( BaseModel baseModel ) {
        return 0;
    }

    @Override
    protected int delete( BaseModel baseModel ) {
        return 0;
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto insertData(UserInformation dto) {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		long userId = 0;
		int[] serviceSetMode = new int[1];
		try {
			// 校验机房主体信息
			boolean userMainSuccess = false;
			boolean userServiceSuccess = false;
			boolean userBandwidthSuccess = false;
			boolean userVirtualSuccess = false;
			if (dto.getUserId()!=null){
				//id存在的话证明新增的用户在数据库中已经存在,采取更新的手段
				if (dto.isOverrideUser()){
					//覆盖用户->更新所有
					// 用户编号
					dto.setUserCode(dto.getUnitName());
					// zipCode
					if (!StringUtil.isEmptyString(dto.getUnitAddressAreaCode())) {
						IdcJcdmXzqydm idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(dto.getUnitAddressAreaCode());
						dto.setUnitZipCode(idcJcdmXzqydm.getPostCode());
					}
					// dealFlag
					dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
					dto.setUpdateTime(new Date());
					//UserInformation info = rptBaseUserMapper.selectByPrimaryKey(dto.getUserId());
					UserInformation info = userInfoMapper.findUserInfoByUserId(dto.getUserId());
					if (HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue().equals(info.getDealFlag())) {// 上报成功时更改状态
						dto.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());// 变更状态
						dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
					} else if (HouseConstant.DealFlagEnum.REPORTING.getValue().equals(info.getDealFlag())) {
						// 用户处于提交上报
						CacheIsmsBaseInfo caBaseInfo = new CacheIsmsBaseInfo();
						caBaseInfo.setUserId(dto.getUserId());
						caBaseInfo.setJyzId(obtainJyzId());
						CacheIsmsBaseInfo resultCahce = cacheMapper.findByUserId(dto.getUserId());
						if (resultCahce == null) {// 若缓存表中没有有该机房记录则写入
							cacheMapper.insert(caBaseInfo);
						}
					} else {
						dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
					}
					validateResult = validateResult(dto);
					if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
						userMainSuccess = true;
						log.info("validate user success!");
					}
				}else {
					//不覆盖用户->只更新隶属单位和用户标识等权限控制 ,只看用户的隶属单位和用户标识是否为空即可
					if (!StringUtil.isEmptyString(dto.getAreaCode()) && !StringUtil.isEmptyString(dto.getIdentify())){
						userMainSuccess=true;
					}
				}
//				updateData(dto);
			}else {
				dto.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
				resetAreaCode(dto);
				validateResult = validateResult(dto);
				if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
					userMainSuccess = true;
					log.info("validate user main success!");
				} else {
					log.info("validate user main failed!");
					return validateResult;
				}
			}
			if(userMainSuccess) {
				dto.setAreaCodeValidateType(2);
				List<UserServiceInformation>  serviceInformations = dto.getServiceList();
				if (serviceInformations != null && serviceInformations.size() > 0) {
					serviceSetMode[0] = serviceInformations.get(0).getSetmode();

					// 用户服务信息校验
					serviceInformations.forEach(sdto->{
						sdto.setCreateUserId(dto.getCreateUserId());
						sdto.setCityCodeList(dto.getCityCodeList());
						sdto.setAreaCode(dto.getAreaCode());
						sdto.setUserAuthHouseList(dto.getUserAuthHouseList());
						sdto.setUserAuthIdentityList(dto.getUserAuthIdentityList());
					});
					validateResult = validateResult(userServiceValidator, serviceInformations, HouseConstant.OperationTypeEnum.ADD.getValue());
					if (validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
						userServiceSuccess = true;
					} else {
						log.info("validate user service failed!");
						return validateResult;
					}
				} 
				if (dto.getBandwidthList() != null && dto.getBandwidthList().size() > 0) {
					// 用户带宽信息校验
					List<UserBandwidthInformation> bands = dto.getBandwidthList();
					bands.forEach(band->{
						band.setCreateUserId(dto.getCreateUserId());
						band.setCityCodeList(dto.getCityCodeList());
						band.setAreaCode(dto.getAreaCode());
						band.setUserAuthHouseList(dto.getUserAuthHouseList());
						band.setUserAuthIdentityList(dto.getUserAuthIdentityList());
						band.setUnitName(dto.getUnitName());
					});
					validateResult = validateResult(userNetworkValidator, bands, HouseConstant.OperationTypeEnum.ADD.getValue());
					if (validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
						userBandwidthSuccess = true;
					} else {
						log.info("validate user bandwidth failed!");
						return validateResult;
					}
				}
				if (dto.getVirtualList() != null && dto.getVirtualList().size() > 0) {
					// 用户虚拟主机信息校验
					List<UserVirtualInformation> userVirtualInformations = dto.getVirtualList();
					userVirtualInformations.forEach(vir->{
						vir.setCreateUserId(dto.getCreateUserId());
						vir.setCityCodeList(dto.getCityCodeList());
						vir.setAreaCode(dto.getAreaCode());
						vir.setUserAuthHouseList(dto.getUserAuthHouseList());
						vir.setUserAuthIdentityList(dto.getUserAuthIdentityList());
						vir.setSetmode(serviceSetMode[0]);
					});
					validateResult = validateResult(userVirtualMachineValidator, userVirtualInformations, HouseConstant.OperationTypeEnum.ADD.getValue());
					if (validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
						userVirtualSuccess = true;
					} else {
						log.info("validate house ip failed!");
						return validateResult;
					}
				}
				//用户主体新增入库//或者更新
				if (dto.getUserId()!=null){
					UserInformationDTO userInformation = userInfoMapper.getUserById(dto.getUserId());
					//重置用户标识,追加
					String ify = userInformation.getIdentify();
					Set<String> set = new HashSet<>();
					set.addAll(Arrays.asList(ify.split(",")));
					set.addAll(Arrays.asList(dto.getIdentify().split(",")));
					dto.setIdentify(StringUtils.join(set.iterator(),","));

					//重置隶属单位，追加
					String areacodes = userInformation.getAreaCode();
					Set<String> codeSet = new HashSet<>();
					codeSet.addAll(Arrays.asList(areacodes.split(",")));
					codeSet.addAll(Arrays.asList(dto.getAreaCode().split(",")));
					dto.setAreaCode(StringUtils.join(codeSet.iterator(),","));
					try {
						if (dto.isOverrideUser()){
							//覆盖更新
							userInfoMapper.updateUserInfo(dto);
						}else {
							//不覆盖
							UserInformation paramInfo = new UserInformation();
							paramInfo.setUserId(dto.getUserId());
							paramInfo.setUpdateUserId(dto.getUpdateUserId());
							paramInfo.setIdentify(dto.getIdentify());
							paramInfo.setAreaCode(dto.getAreaCode());
							paramInfo.setOperateType(dto.getOperateType()!=null?dto.getOperateType():userInformation.getOperateType());
							paramInfo.setDealFlag(dto.getDealFlag());
							userInfoMapper.updateUserInfo(paramInfo);
						}
					}catch (Exception e){
						log.error("更新用户主体失败",e);
						return getErrorResult("更新用户主体失败");
					}
				}else {
					int result = insert(dto);
					if (result <= 0) {// 用户主体新增失败
						return getErrorResult("新增记录失败");
					}
				}
				userId = dto.getUserId();
				if (userServiceSuccess) {
					preUserServerServiceImpl.insertData(dto.getServiceList(), userId);
				}
				if (userBandwidthSuccess) {
					preUserBandWidthServiceImpl.insertData(dto.getBandwidthList(), userId);
				}
				if (userVirtualSuccess) {
					preUserVirtualServiceImpl.insertData(dto.getVirtualList(), userId);
				}
				return getSuccessResult(Integer.valueOf(userId + ""));
			}
		} catch (Exception e) {
			log.error("insert user failed!", e);
			return getErrorResult("新增记录失败");
		}
		return validateResult;
    }

	@Override
	public ResultDto updateData(UserInformationDTO dto) {

		// 获取经jyzid
		dto.setJyzId((int)obtainJyzId());
		// 用户编号
		dto.setUserCode(dto.getUnitName());
		// zipCode
		if (!StringUtil.isEmptyString(dto.getUnitAddressAreaCode())) {
			IdcJcdmXzqydm idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(dto.getUnitAddressAreaCode());
			dto.setUnitZipCode(idcJcdmXzqydm.getPostCode());
		}
		// dealFlag
		dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
		dto.setUpdateTime(new Date());
		// czlx
		//UserInformation info = rptBaseUserMapper.selectByPrimaryKey(dto.getUserId());
		UserInformation info = userInfoMapper.findUserInfoByUserId(dto.getUserId());
		if (HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue().equals(info.getDealFlag())) {// 上报成功时更改状态
			dto.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());// 变更状态
			dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
		} else if (HouseConstant.DealFlagEnum.REPORTING.getValue().equals(info.getDealFlag())) {
			// 机房处于提交上报
			CacheIsmsBaseInfo caBaseInfo = new CacheIsmsBaseInfo();
			caBaseInfo.setUserId(dto.getUserId());
			caBaseInfo.setJyzId(obtainJyzId());
			CacheIsmsBaseInfo resultCahce = cacheMapper.findByUserId(dto.getUserId());
			if (resultCahce == null) {// 若缓存表中没有有该机房记录则写入
				cacheMapper.insert(caBaseInfo);
			}
		} else {
			dto.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());// 未预审
		}

		// 1.校验用户主体信息
		boolean success = false;
		dto.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());
		ResultDto validateResult = validateResult(dto);
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			success = true;
			log.info("validate user success!");
		}
		if ( !success ) {
			return validateResult;
		}
		// 2.用户主体更新
		try {
			updateUserAreaCode(dto);
			int result = userInfoMapper.updateUserInfo(dto);
			if (result <= 0) {// 机房主体新增失败
				return getErrorResult("[" + dto.getUnitName() + "]:" + "UserMain update to DB ERROR ");
			}
		} catch (Exception e) {
			log.error("UserMain update to DB ERROR", e);
			return getErrorResult("[" + dto.getUnitName() + "]:" + "UserMain update to DB ERROR ");
		}
		return getSuccessResult(String.valueOf(dto.getUserId()));
	}

	private void updateUserAreaCode(UserInformationDTO dto) {
		UserInformation userInformation = userInfoMapper.findUserInfoByUserId(dto.getUserId());
		String[] dbAreaCodeArr = userInformation.getAreaCode().split(",");
		List<String> authAreaList = dto.getCityCodeList();
		Set<String> areas = new HashSet<>();
		areas.addAll(authAreaList);
		areas.addAll(Arrays.asList(dbAreaCodeArr));
//		String areaAppend = "";
//		for(int i=0;i<dbAreaCodeArr.length;++i){
//			boolean equalFlag = false;
//			for(String authArea:authAreaList){
//				if(dbAreaCodeArr[i].equals(authArea)){
//					equalFlag = true;
//				}
//			}
//			if(!equalFlag){
//				areaAppend += dbAreaCodeArr[i] + ",";
//			}
//		}
//		if(areaAppend!=""){
//			areaAppend = areaAppend.substring(0, areaAppend.lastIndexOf(","));
//			dto.setAreaCode(dto.getAreaCode()+","+areaAppend);
//		}
		StringBuilder stringBuilder = new StringBuilder();
		for (String area:areas){
			stringBuilder.append(area).append(",");
		}
		dto.setAreaCode(stringBuilder.substring(0,stringBuilder.length()-1));
	}

	@Transactional
	@Override
	public ResultDto batchDeleteUserInfo(List<UserInformationDTO> dtos) {
		if (dtos == null || dtos.size() <= 0) {
			return getErrorResult("用户数据为空");
		}
//		boolean isSuccess = true;

		List<String> errorUserIds = Lists.newArrayList();
		for (UserInformationDTO dto : dtos) {
			UserInformation userInfo = userInfoMapper.findUserInfoByUserId(dto.getUserId());
			if (userInfo.getDealFlag() == 4) {
				// 用户属于提交上报状态时，不能删除
				continue;
			}
			try {
				//用户隶属地市码判断
				List<String> exitsAreacodes = new ArrayList<>(Arrays.asList(userInfo.getAreaCode().split(",")));
				if(!StringUtil.isEmptyString(userInfo.getAreaCode())){
					if(dto.getCityCodeList()!=null || dto.getCityCodeList().size()>=0){
						for(String areacode:userInfo.getAreaCode().split(",")){
							for(String nowAreacode:dto.getCityCodeList()){
								if(areacode.equals(nowAreacode)){
									exitsAreacodes.remove(areacode);
									break;
								}
							}
						}
					}
				}
				//单个用户删除,如果删除最后一个隶属地市码要走上报流程
				if(exitsAreacodes.size()>0){
					userInfo.setUpdateUserId(dto.getUpdateUserId());
					userInfo.setUpdateTime(new Date());
					userInfo.setAreaCode(StringUtils.join(exitsAreacodes,","));
					userInfoMapper.updateUserInfo(userInfo);
					updataUserDeal(userInfo.getUserId(),dto.getCityCodeList(),dto.getUpdateUserId());

				}else {
					if ((userInfo.getCzlx() == 1 && userInfo.getDealFlag() == 5) || userInfo.getCzlx() == 2) {// 用户新增上报或者已上报
						// 1.未报备的机架、链路、ip物理删除
						//deleteUserInfosByUserId(dto.getUserId());
						// 2.已报备的修改成删除未上报
						updateUserInfosByUserId(dto.getUserId());
						// 3.用户资源释放
						dto = userInfoMapper.findByUserId(dto.getUserId().toString());
						releaseUserResourcesWhileDelUser(dto);

					} else {// 物理级联删除用户信息
						if (userInfo.getCzlx() == HouseConstant.CzlxEnum.ADD.getValue()
								&& (userInfo.getDealFlag() != HouseConstant.DealFlagEnum.REPORTING.getValue()
								|| userInfo.getDealFlag() != HouseConstant.DealFlagEnum.CHECKING.getValue())) {
							deleteUserInfosByUserId(dto.getUserId());
						}
					}
				}
				// 级联处理机房信息
				dealHouseInfoWhileUserUpdate(userInfo);
			} catch (Exception e) {
				errorUserIds.add(dto.getUserId().toString());
				log.error("[delete userInfo  ERROR.userId=" + dto.getUserId() + "]", e);
			}
		}
		if (errorUserIds.isEmpty()){
			return getSuccessResult();
		} else {
			return getErrorResult("一共删除记录"+(dtos==null?0:dtos.size())+"条，其中"+formatListToString(errorUserIds,"、")+"用户删除失败！");
		}
	}

	/**
	 * 当用户删除时，根据开关控制是否释放用户关联的IP和机架资源信息
	 * 
	 * @author : songl
	 */
	private void releaseUserResourcesWhileDelUser(UserInformationDTO dto) {
		//当用户ip释放开关打开
		String userIpFlag = LocalConfig.getInstance().getHashValueByHashKey("userIp_release_enabled");
		if(userIpFlag!=null&&"true".equals(userIpFlag)){
			//已上报的IP修改成修改未上报
			houseIpSegmentMapper.updateIpStatuByUserId(dto.getUserId());
			//将关联用户的ip地址段释放修改成保留状态，删除状态的IP不做修改
			houseIpSegmentMapper.dealIpsegWhileUserDelete(dto.getUserId());
			//与ip关联的机房做级联处理
			List<Long> houseIdList = houseIpSegmentMapper.findIpsegHouseIdsByUserId(dto.getUserId());
			for(Long hosueId:houseIdList){
				dealHouseMainWhileISubInfoUpdate(hosueId);
			}
			
			
			//删除用户机架表关联的用户机架信息
			List<HouseUserFrameInformation> userFrameList = frameMapper.findUserFrameByUserInfo(dto);
			for(HouseUserFrameInformation userFrame:userFrameList){
				int result = frameMapper.deleteUserFrameByUserInfo(dto);
				//int result = frameMapper.deleteUserFrameByFrameId(userFrame.getFrameId());
				if(result>0){
					List<HouseUserFrameInformation> houseFrameList = frameMapper.findUserFrameByFrameId(userFrame.getFrameId());
					if(houseFrameList!=null&&houseFrameList.size()>0){
						continue;
					}else{//用户占用的机架没有在分给其他用户，机架的分配状态更改为未分配状态
						HouseFrameInformation frame = frameMapper.findFrameInfoByFrameIdOrFrameName(userFrame.getFrameId(), null);
						frame.setDistribution(1);//未分配状态
						frameMapper.updateHouseFrameInfomation(frame);
						//与机架相关的机房主体做级联处理
						dealHouseMainWhileISubInfoUpdate(frame.getHouseId());
					}
				}
			}
		}
	}
	
	//用户更新删除处理子节点信息
	public void updataUserDeal(Long userId,List<String> areaCodes,Integer updateUserId){
    	//记录用户主体信息是否需要变更状态
    	boolean changeFlag = false;
		UserServiceInformationDTO query = new UserServiceInformationDTO();
		query.setUserId(userId);
		try {
		List<UserServiceInformationDTO> services = userServiceMapper.getUserServiceInfoList(query);
		for (UserServiceInformationDTO service : services){
			if(!StringUtil.isEmptyString(service.getAreaCode())){
				List<String> exitsAreacodes = new ArrayList<>(Arrays.asList(service.getAreaCode().split(",")));
				if(areaCodes!=null || areaCodes.size()>=0){
					boolean flag = false;
					for(String areacode:service.getAreaCode().split(",")){
						for(String nowAreacode:areaCodes){
							if(areacode.equals(nowAreacode)){
								exitsAreacodes.remove(areacode);
								flag = true;
								break;
							}
						}
					}
					if(flag && exitsAreacodes.size()>0){
						service.setUpdateTime(new Date());
						service.setUpdateUserId(updateUserId);
						service.setAreaCode(StringUtils.join(exitsAreacodes,","));
						userServiceMapper.updateUserService(service);
					}else if(flag && exitsAreacodes.size()<=0){
						if(service.getDealFlag().equals(HouseConstant.ChildDealFlagEnum.UPLOADED.getValue())){
							changeFlag=true;
							//1.根据用户Id将服务域名更改成删除未上报
							ServiceDomainInformation domain = new ServiceDomainInformation();
							domain.setDealFlag(HouseConstant.ChildDealFlagEnum.UN_UPLOAD.getValue());
							domain.setCzlx(HouseConstant.CzlxEnum.DELETE.getValue());
							domain.setUpdateUserId(updateUserId);
							domain.setUpdateTime(new Date());
							domain.setServiceId(service.getServiceId());
							userServiceMapper.updateDomainByServiceId(domain);
							//2.根据用户Id将用户服务服务更改成删除未上报
							service.setDealFlag(HouseConstant.ChildDealFlagEnum.UN_UPLOAD.getValue());
							service.setCzlx(HouseConstant.CzlxEnum.DELETE.getValue());
							service.setUpdateUserId(updateUserId);
							service.setUpdateTime(new Date());
							userServiceMapper.updateUserService(service);
						}else {
							userServiceMapper.deleteServerDomainByServiceId(service.getServiceId());
							userServiceMapper.deleteById(service.getServiceId());
						}
					}
				}
			}
		}
		if(areaCodes.size()>0){
			UserVirtualInformationDTO virtual = new UserVirtualInformationDTO();
			virtual.setUserId(userId);
			virtual.setAreaCode(StringUtils.join(areaCodes,","));
			List<UserVirtualInformationDTO> virtuals = userVirtualMachineMapper.getIndexUserVirtualByUserId(virtual);
			for(UserVirtualInformationDTO temp : virtuals){
				if(temp.getDealFlag().equals(HouseConstant.ChildDealFlagEnum.UPLOADED.getValue())){
					changeFlag=true;
					//3.根据用户Id将用户虚拟主机更改成删除未上报
					UserVirtualInformation virDto = new UserVirtualInformation();
					virDto.setDealFlag(HouseConstant.ChildDealFlagEnum.UN_UPLOAD.getValue());
					virDto.setCzlx(HouseConstant.CzlxEnum.DELETE.getValue());
					virDto.setUpdateUserId(updateUserId);
					virDto.setUpdateTime(new Date());
					virDto.setVirtualId(temp.getVirtualId());
					userVirtualMachineMapper.updateVirtualInfoById(virDto);
				}else {
					//删除用户虚拟机信息
					userVirtualMachineMapper.deleteByVirtulId(temp.getVirtualId());
				}
			}

			UserBandwidthInformationDTO hh = new UserBandwidthInformationDTO();
			hh.setUserId(userId);
			hh.setAreaCode(StringUtils.join(areaCodes,","));
			List<UserBandwidthInformationDTO> hhs = userBandWidthMapper.getUserBandWidthByUserId(hh);
			for(UserBandwidthInformationDTO tempHhs: hhs){
				if(tempHhs.getDealFlag().equals(HouseConstant.ChildDealFlagEnum.UPLOADED.getValue())){
					changeFlag=true;
					//4.根据用户Id将用户带宽更改成删除未上报
					UserBandwidthInformation hhDto = new UserBandwidthInformation();
					hhDto.setDealFlag(HouseConstant.ChildDealFlagEnum.UN_UPLOAD.getValue());
					hhDto.setCzlx(HouseConstant.CzlxEnum.DELETE.getValue());
					hhDto.setUpdateUserId(updateUserId);
					hhDto.setUpdateTime(new Date());
					hhDto.setHhId(tempHhs.getHhId());
					userHHMapper.updateUserHHInfoByHHid(hhDto);
				}else{
					userBandWidthMapper.deleteByHHId(tempHhs.getHhId());
				}
			}
		}
		if(changeFlag){
			updateUserMainInfoWhieSubsetInfoChange(userId);
		}
		} catch (Exception e) {
			log.error("删除处理子节点失败："+e.toString());
		}
	}


	/**
     * 
     * 
     */
	private void updateUserInfosByUserId(long userId)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dealFlag", HouseConstant.DealFlagEnum.CHECKING.getValue());
		map.put("czlx", HouseConstant.CzlxEnum.DELETE.getValue());
		map.put("userId", userId);
		//1.根据用户Id将服务域名更改成删除未上报
		userServiceMapper.UpdateServerDomainDealFlagAndCzlxByUserId(map);
		//2.根据用户Id将用户服务服务更改成删除未上报
		userServiceMapper.UpdateDealFlagAndCzlxByUserId(map);
		//3.根据用户Id将用户虚拟主机更改成删除未上报
		userVirtualMachineMapper.UpdateDealFlagAndCzlxByUserId(map);
		//4.根据用户Id将用户带宽更改成删除未上报
		userHHMapper.UpdateDealFlagAndCzlxByUserId(map);
		//5.根据用户Id将用户主体更改成删除未上报
		userInfoMapper.UpdateDealFlagAndCzlxByUserId(map);
	}

	/**
	 * 
	 * 物理删除未报备的用户主体以及子信息
	 */
	private void deleteUserInfosByUserId(long userId){
		//1.物理删除用户服务域名信息
		userServiceMapper.deleteServerDomainByUserId(userId);
		//2.物理删除用户服务信息
		userServiceMapper.deleteByUserId(userId);
		//3.物理删除虚拟主机信息
		userVirtualMachineMapper.deleteByUserId(userId);
		//4.物理删除用户网络资源信息
		userHHMapper.deleteByUserId(userId);
		//5.用户资源释放
		UserInformationDTO dto = userInfoMapper.findByUserId(userId+"");
		releaseUserResourcesWhileDelUser(dto);
		//6.物理删除用户主体信息
		userInfoMapper.deleteByUserId(userId);
	}

	@Override
	public ResultDto approve(String userId) {
		if (userId == null || userId == "") {
			return getErrorResult("用户Id" + ErrorCodeConstant.ERROR_1009.getErrorMsg());
		}
		try {
			UserInformation dto = userInfoMapper.findUserInfoByUserId(Long.parseLong(userId));
			if (dto == null) {
				return getErrorResult("用户" + ErrorCodeConstant.ERROR_1008.getErrorMsg());
			}
			int dealFlag = 0;
			ResultDto resultDto = new ResultDto();
			if (HouseConstant.CzlxEnum.DELETE.getValue().equals(dto.getCzlx())) {
				dealFlag = HouseConstant.DealFlagEnum.CHECKING.getValue();
			} else {
				resultDto = integrityVerification(Long.valueOf(userId), dto.getNature());
				if (ResultDto.ResultCodeEnum.ERROR.getCode().equals(resultDto.getResultCode())) {
					//用户信息不完整，预审不通过
					dealFlag = HouseConstant.DealFlagEnum.CHECK_FAIL.getValue();
				} else {
					dealFlag = HouseConstant.DealFlagEnum.CHECKING.getValue();
				}
			}
			if (HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue().equals(dto.getDealFlag())) {
				// 上报成功时更改状态
				UserInformation info = new UserInformation();
				info.setUserId(dto.getUserId());
				info.setCzlx(HouseConstant.OperationTypeEnum.MODIFY.getValue());
				info.setDealFlag(dealFlag);
				if (resultDto.getAjaxValidationResult() != null) {
					info.setVerificationResultWithTimestamp(resultDto.getAjaxValidationResult().getErrorsToString());
				} else {
					info.setVerificationResult("");
				}
				int result = userInfoMapper.updateDealFlagByUserId(info);
				if (result <= 0) {
					getErrorResult(ErrorCodeConstant.ERROR_1018.getErrorMsg());
				}
			} else if (HouseConstant.DealFlagEnum.REPORTING.getValue().equals(dto.getDealFlag())) {
				// 机房处于提交上报，不能预审，返回成功
				return getErrorResult("预审不通过");
			} else {
				UserInformation info = new UserInformation();
				info.setUserId(dto.getUserId());
				info.setDealFlag(dealFlag);
				if (resultDto.getAjaxValidationResult() != null) {
					info.setVerificationResultWithTimestamp(resultDto.getAjaxValidationResult().getErrorsToString());
				} else {
					info.setVerificationResult("");
				}
				int result = userInfoMapper.updateDealFlagByUserId(info);
				if (result <= 0) {
					getErrorResult(ErrorCodeConstant.ERROR_1018.getErrorMsg());
				}
				return getSuccessResult("预审通过");
			}
		} catch (Exception e) {
			log.error("预审不通过", e);
			return getErrorResult("预审不通过");
		}
		return getSuccessResult("预审通过");
		
		/*if (userId == null || userId == "") {
			getErrorResult("用户Id" + ErrorCodeConstant.ERROR_1009.getErrorMsg());
		}
		try {
			UserInformation dto = userInfoMapper.findUserInfoByUserId(Long.parseLong(userId));
			if (dto == null) {
				getErrorResult("用户" + ErrorCodeConstant.ERROR_1008.getErrorMsg());
			}
			if (dto.getDealFlag() == 2 || dto.getDealFlag() == 4) {// 当用户处于上报审核中或者提交上报时不预审
				return getSuccessResult();
			}
			dto.setAreaCodeValidateType(2);
			if (!validateSuccess(dto)) {
				return validateResult(dto);
			}
			dto.setDealFlag(HouseConstant.DealFlagEnum.CHECKING.getValue());// 上报审核中
			int result = userInfoMapper.updateDealFlagByUserId(dto);
			if (result <= 0) {
				getErrorResult(ErrorCodeConstant.ERROR_1018.getErrorMsg());
			}
		} catch (NumberFormatException e) {
			getErrorResult(ErrorCodeConstant.ERROR_1018.getErrorMsg());
		}
		return getSuccessResult();*/
	}

	private ResultDto integrityVerification(Long userId, int nature) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		AjaxValidationResult ajaxValidationResult = new AjaxValidationResult();
		int bandwidthCount = userInfoMapper.integrityVerificationForUserBandwidth(userId);
		if (bandwidthCount > 0 && nature == 2) {
			//其他用户没有服务信息
			result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
			return result;
		} else {
			boolean passed = true;
			if (bandwidthCount == 0) {
				ajaxValidationResult.getErrorsArgsMap().put("user.bandwidth", new String[] {DateUtils.getCurrentyyyMMddHHmmss()+" 缺失用户网络资源信息,请补充后再预审"});
				passed = false;
			}
			if (nature == 1) {
				int serviceCount = userInfoMapper.integrityVerificationForUserService(userId);
				//提供互联网服务的客户必须要有服务信息
				if (serviceCount == 0) {
					ajaxValidationResult.getErrorsArgsMap().put("user.service", new String[] {DateUtils.getCurrentyyyMMddHHmmss()+" 缺失用户服务信息,请补充后再预审"});
					passed = false;
				}
				List<UserServiceInformation> list = userServiceMapper.getByUserIdAndSetmode(userId, 1L);
				if (list != null && list.size() > 0) {
					//存在虚拟主机接入方式
					int virtualCount = userInfoMapper.integrityVerificationForUserVirtuals(userId);
					if (virtualCount == 0) {
						ajaxValidationResult.getErrorsArgsMap().put("user.virtual", new String[] {DateUtils.getCurrentyyyMMddHHmmss()+" 缺失虚拟主机信息,请补充后再预审"});
						passed = false;
					}
				}
			} 
			result.setResultCode(passed ? ResultDto.ResultCodeEnum.SUCCESS.getCode() : ResultDto.ResultCodeEnum.ERROR.getCode());
			result.setAjaxValidationResult(ajaxValidationResult);
		}
		return result;
	}

	@Override
	public ResultDto revertApprove(String userId) {
		if(userId==null||userId==""){
			getErrorResult("用户Id"+ErrorCodeConstant.ERROR_1009.getErrorMsg());
		}
		try {
			UserInformation dto = userInfoMapper.findUserInfoByUserId(Long.parseLong(userId));
			if(dto==null){
				getErrorResult("用户"+ErrorCodeConstant.ERROR_1008.getErrorMsg());
			}
			if(dto.getDealFlag()==4){//当用户处于提交上报时不能撤销
				return getSuccessResult();
			}
			dto.setDealFlag(0);//未预审
			int result = userInfoMapper.updateDealFlagByUserId(dto);
			if(result<=0){
				getErrorResult(ErrorCodeConstant.ERROR_1018.getErrorMsg());
			}
		} catch (NumberFormatException e) {
			getErrorResult(ErrorCodeConstant.ERROR_1018.getErrorMsg());
		}
		return getSuccessResult();
	}

    /**
     * 写Kafka信息
     */
	private void writeKafkaMag(Long submitId) throws Exception {

		IdcInformation idc = obtainJyz();
		if (idc!=null){
			IdcInformation mation = new IdcInformation();
			mation.setJyzId(idc.getJyzId());
			mation.setCzlx(idc.getCzlx());
			mation.setIdcId(idc.getIdcId());
			mation.setSubmitId(submitId);
			idcInfoMapper.insertIdcWaitInfo(mation);
		}
       // write kafka
        JobQueue jobQueue = new JobQueue();
        //param 是 submitId
        Map<String ,Long> map=new HashMap<String ,Long>();
        map.put("submitId",submitId);
        jobQueue.setParams(JSON.toJSONString(map));
        RedisTaskStatus redisTaskStatus = new RedisTaskStatus();
        Long taskId = TaskIdUtil.getInstance().getTaskId();
        jobQueue.setJobtype(GlobalParams.UPLOAD_BASIC_DATTA_RPT);
        jobQueue.setTaskid(taskId);
        jobQueue.setToptaskid(0l);
        jobQueue.setIsretry(0);
        jobQueue.setCreatetime(System.currentTimeMillis()/1000);
        JobQueueUtil.sendMsgToKafkaJobQueue(jobQueue);

//         写入redis任务信息hash
        redisTaskStatus.setToptaskid(taskId);
        redisTaskStatus.setTaskid(taskId);
        redisTaskStatus.setTasktype(1); // JOB任务
        redisTaskStatus.setContent(JSON.toJSONString(jobQueue));
        redisTaskStatus.setCreatetime(System.currentTimeMillis()/1000);
        redisTaskStatus.setStatus(1); // 开始
        redisTaskStatus.setTimes(1);  // 从1开始
        TaskMessageUtil.getInstance().setTask(taskId, redisTaskStatus);

    }


    @Override
    public UserInformationDTO findByUserId(String userId) {
        return userInfoMapper.findByUserId(userId);
    }

	@Override
	public PageResult<UserInformationDTO> listUserData(UserInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<UserInformationDTO> result = new PageResult<UserInformationDTO>();
		// 两个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				||StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return result;
		}
		List<UserInformationDTO> info = null;
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = userInfoMapper.listData(dto);
			PageInfo<UserInformationDTO> pageResult = new PageInfo<UserInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = userInfoMapper.listData(dto);
		}
		result.setRows(info);
		return result;
	}

    /**
     * 用户主体导入
     * @param dtos
     * @return
     */
    @Override
    public Map<Integer,ResultDto> imporUserData( Map<Integer,UserInformationDTO> dtos) {
    	if (dtos!=null && !dtos.isEmpty()) {
			Map<Integer,ResultDto> resultDtoMap = new HashMap<>();
			Set<Integer> ks = dtos.keySet();
			for (Integer key :ks) {
				try {
					UserInformationDTO dto = dtos.get(key);
					resetAreaCode(dto);
					UserInformationDTO exit = userInfoMapper.getDataByUserName(dto.getUnitName());

					if (StringUtil.isEmptyString(dto.getUnitAddressProvinceCode())){
						if (StringUtil.isEmptyString(dto.getUnitAddressCityCode())){
							resultDtoMap.put(key,getErrorResult("省（自治区）填写有误，市（自治州|地区|盟）填写有误"));
							continue;
						}
						resultDtoMap.put(key,getErrorResult("省（自治区）填写有误"));
						continue;
					}
					if (StringUtil.isEmptyString(dto.getUnitAddressCityCode())){
						resultDtoMap.put(key,getErrorResult("市（自治州|地区|盟）填写有误"));
						continue;
					}

					if (!StringUtil.isEmptyString(dto.getUnitAddressAreaName())
							&& StringUtil.isEmptyString(dto.getUnitAddressAreaCode())){
						resultDtoMap.put(key,getErrorResult("区（市|县|旗）填写有误"));
						continue;
					}
					if (exit != null) {
						//已经存在
						String exitAreaCode = exit.getAreaCode();
						if (exitAreaCode != null) {
							String[] areas = exitAreaCode.split(",");
							String[] importArea = dto.getAreaCode().split(",");
							Set<String> areaSet = new HashSet<String>();
							for (String areaCode : areas) {
								if (!StringUtil.isEmptyString(areaCode)) {
									areaSet.add(areaCode);
								}
							}
							for (String areaCode : importArea) {
								if (!StringUtil.isEmptyString(areaCode)) {
									areaSet.add(areaCode);
								}
							}
							StringBuilder sb = new StringBuilder();
							for (String areaCode : areaSet) {
								sb.append(areaCode).append(",");
							}
							dto.setAreaCode(sb.toString().substring(0, sb.lastIndexOf(",")));
						}
						dto.setUserId(exit.getUserId());
						Boolean identifyFlag = false;
						if (dto.getUserAuthIdentityList() != null) {
							for (String str : dto.getUserAuthIdentityList()) {
								if (dto.getIdentify().equals(str)) {
									identifyFlag = true;
									break;
								}
							}
						}
						if (!identifyFlag) {//该用户没有此用户标识导入权限
							resultDtoMap.put(key,getSuccessResult());
							break;
						}
						if (exit.getIdentify().indexOf(dto.getIdentify()) < 0) {//当用户标识不包含时，需要根据'是否覆盖'来判断主体信息的更新
							if (dto.getCover() != null && dto.getCover() != 1) {//不覆盖主体信息，仅追加用户标识
								exit.setIdentify(exit.getIdentify() + "," + dto.getIdentify());
								exit.setUserAuthHouseList(dto.getUserAuthHouseList());
								exit.setCityCodeList(dto.getCityCodeList());
								exit.setUserAuthIdentityList(dto.getUserAuthIdentityList());
								resultDtoMap.put(key,updateData(exit));
								break;
							} else {
								dto.setIdentify(exit.getIdentify() + "," + dto.getIdentify());
							}
						} else {
							dto.setIdentify(exit.getIdentify());
						}
						resultDtoMap.put(key,updateData(dto));
					} else {
						//新增
						resultDtoMap.put(key,insertData(dto));
					}
				}catch (Exception e){
					log.error("inset import single user error ",e);
					resultDtoMap.put(key,getErrorResult("网络异常"));
				}
			}
			return resultDtoMap;
		}
		return null;
    }

	/**
	 * 重置省，区，县
	 * @param dto
	 */
	private void resetAreaCode(UserInformation dto){
		//用户携带的地市码
		if (StringUtil.isEmptyString(dto.getUnitAddressProvinceCode())
				&& !StringUtil.isEmptyString(dto.getUnitAddressProvinceName())){
			String code = dictionaryMapper.getProvinceArreaCode(dto.getUnitAddressProvinceName());
			if (!StringUtil.isEmptyString(code)) dto.setUnitAddressProvinceCode(code);
		}
		//4个直辖市的二级单位等于第一级
		if (!StringUtil.isEmptyString(dto.getUnitAddressProvinceName())
				&& "北京市,上海市，天津市，重庆市".indexOf(dto.getUnitAddressProvinceName())>-1){
			dto.setUnitAddressCityName(dto.getUnitAddressProvinceName());
			dto.setUnitAddressCityCode(dto.getUnitAddressProvinceCode());
		}else if (StringUtil.isEmptyString(dto.getUnitAddressCityCode())
				&& !StringUtil.isEmptyString(dto.getUnitAddressCityName())
				&& !StringUtil.isEmptyString(dto.getUnitAddressProvinceCode())){

			if (dto.getUnitAddressCityName().equals(dto.getUnitAddressProvinceName())){
				dto.setUnitAddressCityCode(dto.getUnitAddressProvinceCode());
			}else {
				String code = dictionaryMapper.getCityArreaCode(dto.getUnitAddressProvinceCode(),dto.getUnitAddressCityName());
				if (!StringUtil.isEmptyString(code)) dto.setUnitAddressCityCode(code);
			}
		}
		if (StringUtil.isEmptyString(dto.getUnitAddressAreaCode())
				&& !StringUtil.isEmptyString(dto.getUnitAddressAreaName())
				&& !StringUtil.isEmptyString(dto.getUnitAddressCityCode())){
			String code = dictionaryMapper.getCountyArreaCode(dto.getUnitAddressCityCode(),dto.getUnitAddressAreaName());
			if (!StringUtil.isEmptyString(code)) {
				dto.setUnitAddressAreaCode(code);
			}
//			else {
//				dto.setUnitAddressAreaCode(dto.getUnitAddressCityCode());
//			}
		}
	}

	@Override
	public List<ApproveResultDto> getApproveResult(String approveId) {
		ApproveResultDto dto = new ApproveResultDto();
		dto.setApproveId(Long.valueOf(approveId));
		return commonMapper.getApproveResult(dto);
	}

}
