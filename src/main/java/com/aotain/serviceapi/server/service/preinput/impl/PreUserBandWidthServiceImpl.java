package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.dic.DictionaryMapper;
import com.aotain.serviceapi.server.dao.preinput.UserBandWidthMapper;
import com.aotain.serviceapi.server.dao.preinput.UserInfoMapper;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.PreUserBandWidthService;
import com.aotain.serviceapi.server.util.StringUtil;
import com.aotain.serviceapi.server.validate.impl.UserNetworkValidatorImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName PreUserBandWidthServiceImpl
 * @Author tanzj
 * @Date 2018/8/9
 **/
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class PreUserBandWidthServiceImpl extends BaseService implements PreUserBandWidthService {

    private Logger log = Logger.getLogger(PreUserBandWidthServiceImpl.class);

    @Autowired
    private UserBandWidthMapper userBandWidthMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private DictionaryMapper dictionaryMapper;

    public PreUserBandWidthServiceImpl(UserNetworkValidatorImpl baseValidator) {
        super(baseValidator);
    }

    @Override
    public PageResult<UserBandwidthInformationDTO> getServerInfoList(UserBandwidthInformationDTO dto) {
    	dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
        PageResult<UserBandwidthInformationDTO> result = new PageResult<UserBandwidthInformationDTO>();
		// 三个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes())
				|| StringUtils.isEmpty(dto.getAuthHouses())
				|| StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty){
			return result;
		}
        List<UserBandwidthInformationDTO> info = new ArrayList<UserBandwidthInformationDTO>();
        if(dto.getIsPaging().equals(1)){
            PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
            info = userBandWidthMapper.getUserBandWidthList(dto);
            PageInfo<UserBandwidthInformationDTO> pageResult = new PageInfo<UserBandwidthInformationDTO>(info);
            result.setTotal(pageResult.getTotal());
        }else{
            info = userBandWidthMapper.getUserBandWidthList(dto);
        }
        result.setRows(info);
        return result;
    }

    @Transactional
    @Override
	public ResultDto insertData(List<? extends UserBandwidthInformation> services, Long userId, boolean allowInsert) throws Exception {
		ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (services == null || services.size() == 0) {
			return result;
		}
		//重复性校验
		Map<String,Object> userBandMap = new HashMap<String,Object>();
        for(UserBandwidthInformation dto :services){
        	String key = dto.getUserId()+dto.getHouseId()+"";
        	if(userBandMap.containsKey(key)){
        		result.setResultCode(ResultDto.ResultCodeEnum.ERROR_CONFLICT.getCode());
        		try {
					result.setResultMsg("用户["+dto.getUnitName()+"]重复占用机房："+dto.getUserName());
				} catch (Exception e) {
					result.setResultMsg("selected UserInfo or HouseInfo haven deleted!"); 
				}
        		return result;
        	}else{
        		userBandMap.put(key,key);
        	}
        }
		
		//校验
		boolean success = false;
		List<Integer> successList = new ArrayList<Integer>(services.size());
		Map<String, AjaxValidationResult> validateResultMap = new HashMap<String, AjaxValidationResult>(services.size());
		for (int i = 0; i < services.size(); i++) {
			UserBandwidthInformation dto = services.get(i);
			dto.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
			if (userId != null) {
				dto.setUserId(userId);
			}
			ResultDto validateResult = validateResult(dto);
			validateResultMap.put(String.valueOf(i), validateResult.getAjaxValidationResult());
			successList.add(validateResult.getResultCode());
			
			//核验成功的对象加入允许持久化
			if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
				if (allowInsert) {
					int flag = insert(dto);
					if (flag <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert user bandwidth success!");
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
		result.setAjaxValidationResultMap(validateResultMap);
		if (success && !allowInsert) {
			int index = 0;
			for (UserBandwidthInformation dto : services) {
				if (userId != null) {
					dto.setUserId(userId);
				}
				int flag = insert(dto);
				if (flag <= 0) {
					return getErrorResult("新增记录失败");
				}
				result.getAjaxValidationResultMap().get((index++) + "").setPid(dto.getHhId());
			}
		} 
		return result;
		/*ResultDto resultInfo = new ResultDto();
		for (UserBandwidthInformationDTO dto : services) {
			// 1.校验
			if (!validateSuccess(dto)) {
				resultInfo.setResultMsg("输入的数据格式校验不通过");
				resultInfo.setResultCode(1);
				return resultInfo;
			}
			dto.setCzlx(1);
			dto.setDealFlag(0);
			int result = userBandWidthMapper.insert(dto);
			if (result <= 0) {
				throw new Exception("用户域名信息新增失败");
			}
			insertDataeUserMainInfoWhieSubsetInfoChange(dto.getUserId());
		}
		return getSuccessResult();*/
	}

	@Override
	public ResultDto updateData(List<UserBandwidthInformationDTO> dtos) throws Exception {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.MODIFY.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate user bandwidth success!");
			return updateBandwidthData(dtos);
		} else {
			log.info("validate user bandwidth failed!");
			return validateResult;
		}
		/*ResultDto resultInfo = new ResultDto();
		// 1.校验
		if (!validateSuccess(dto)) {
			resultInfo.setResultMsg("输入的数据格式校验不通过");
			resultInfo.setResultCode(1);
			return resultInfo;
		}
		// 操作类型如果没有上报则为1，否则为2
		if (dto.getDealFlag() == 1) {
			dto.setCzlx(2);
		} else {
			dto.setCzlx(1);
		}
		dto.setDealFlag(0);
		int result = userBandWidthMapper.update(dto);
		if (result <= 0) {
			return getErrorResult("[" + dto.getUserId() + "]:" + "user service insert to DB ERROR ");
		}
		updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
		return getSuccessResult();*/
	}

    private ResultDto updateBandwidthData(List<UserBandwidthInformationDTO> dtos) {
    	ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (UserBandwidthInformationDTO dto : dtos) {
				try {
					int flag = update(dto);
					if (flag <= 0) {
						// 插入或新增失败
						return getErrorResult("更新记录失败");
					}
				} catch (Exception e) {
					log.error("update user bandwidth failed!", e);
					return getErrorResult("更新记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}

	@Override
    public ResultDto deleteData(List<UserBandwidthInformationDTO> dtos) throws Exception {
		ResultDto validateResult = new ResultDto();
		validateResult.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos == null || dtos.size() <= 0) {
			return validateResult;
		}
		validateResult = validateResult(dtos, HouseConstant.OperationTypeEnum.DELETE.getValue());
		if (validateResult != null && validateResult.getResultCode() == ResultDto.ResultCodeEnum.SUCCESS.getCode()) {
			log.info("validate user bandwidth success!");
			return deleteUserBandwidth(dtos);
		} else {
			log.info("validate user bandwidth failed!");
			return validateResult;
		}
        /*for(Long tmpId:ids){
            UserBandwidthInformationDTO oldDto = userBandWidthMapper.getUserBandWidthInfoById(tmpId);

            if(oldDto!=null){
                if(oldDto.getDealFlag()==0){
                    userBandWidthMapper.deleteByHHId(tmpId);
                }else{
                    UserBandwidthInformationDTO userband = new UserBandwidthInformationDTO();
                    userband.setHhId(tmpId);
                    userband.setCzlx(3);
                    userband.setDealFlag(0);
                    int result = userBandWidthMapper.update(userband);
                }
                updateUserMainInfoWhieSubsetInfoChange(oldDto.getUserId());
            }
        }
        return getSuccessResult();*/
    }

    private ResultDto deleteUserBandwidth(List<UserBandwidthInformationDTO> dtos) {
    	ResultDto result = new ResultDto();
		result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		List<Integer> successList = new ArrayList<Integer>();
		if (dtos != null && dtos.size() > 0) {
			for (UserBandwidthInformationDTO info : dtos) {
				try {
					UserBandwidthInformationDTO oldDto = userBandWidthMapper.getUserBandWidthInfoById(info.getHhId());
					if (oldDto != null) {
						if (oldDto.getCzlx() == 1 && oldDto.getDealFlag() == 0) {
							userBandWidthMapper.deleteByHHId(info.getHhId());
						} else {
							UserBandwidthInformationDTO userband = new UserBandwidthInformationDTO();
							userband.setHhId(info.getHhId());
							userband.setCzlx(3);
							userband.setDealFlag(0);
							userBandWidthMapper.update(userband);
						}
						updateUserMainInfoWhieSubsetInfoChange(oldDto.getUserId());
					}
					successList.add(ResultDto.ResultCodeEnum.SUCCESS.getCode());
				} catch (Exception e) {
					log.error("delete user band hhid:" + info.getHhId() + ", userid: " + info.getUserId() + " ERROR", e);
					result.setResultMsg("删除记录失败");
				}
			}
			if (!successList.contains(ResultDto.ResultCodeEnum.ERROR.getCode())) {
				//全部校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.SUCCESS.getCode());
			} else {
				//部分校验通过
				result.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
			}
			return result;
		} else {
			return result;
		}
	}

	@Override
    public Map<Integer,ResultDto> imporUserBandData( Map<Integer,UserBandwidthInformationDTO> dtos) {
        //用户携带的地市码
		if (dtos!=null && !dtos.isEmpty()) {
			Map<Integer,ResultDto> resultDtoMap = new HashMap<>();
			Set<Integer> ks = dtos.keySet();
			for(Integer key:ks) {
				try {
					UserBandwidthInformationDTO dto = dtos.get(key);
					UserInformationDTO userInformation = userInfoMapper.getDataByUserName(dto.getUserName());
					if (userInformation != null) {
						if (!StringUtil.isEmptyString(dto.getHouseName())) {
							Long houseId=dictionaryMapper.getHouseIdByHouseName(dto.getHouseName());
							if (houseId==null){
								resultDtoMap.put(key,getErrorResult("机房不存在"));
								break;
							}
							dto.setHouseId(houseId);
						}else {
							resultDtoMap.put(key,getErrorResult("机房为空"));
						}
						dto.setUserId(userInformation.getUserId());
						try {
							UserBandwidthInformationDTO exit = userBandWidthMapper.getUserBindInfoByHouseIdAndUserId(dto);
							if (exit!=null){
								dto.setUserId(exit.getUserId());
								dto.setHhId(exit.getHhId());
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
								resultDtoMap.put(key,updateData((Arrays.asList(dto))));
							}else{
								resultDtoMap.put(key,insertData(Arrays.asList(dto), dto.getUserId(), true));
							}
						} catch (Exception e) {
							log.error("imporUserBandData error ", e);
							resultDtoMap.put(key,getErrorResult("导入出错，请重试"));
						}
					} else {
						resultDtoMap.put(key,getErrorResult("关联的用户主体信息导入异常，无法关联导入此子节信息"));
					}
				}catch (Exception e){
					log.error("import single user bandwith error ",e);
					resultDtoMap.put(key,getErrorResult("网络异常"));
				}
			}
			return resultDtoMap;
		}
		return null;
    }

	@Transactional
	@Override
	protected int insert(BaseModel baseModel) {
		try {
			UserBandwidthInformation dto = (UserBandwidthInformation) baseModel;
			dto.setCzlx(1);
			dto.setDealFlag(0);
			int result = userBandWidthMapper.insert(dto);
			if (result <= 0) {
				throw new Exception("用户域名信息新增失败");
			}
			updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
			log.info("新增记录成功");
			return 1;
		} catch (Exception e) {
			log.error("新增记录失败", e);
			return 0;
		}
	}

    @Override
    protected int update(BaseModel baseModel) {
    	try {
    		UserBandwidthInformation dto = (UserBandwidthInformation) baseModel;
    		UserBandwidthInformationDTO oldDto = userBandWidthMapper.getUserBandWidthInfoById(dto.getHhId());
    		if (oldDto != null ) {
    			// 操作类型如果没有上报则为1，否则为2
    			if (oldDto.getCzlx() == 1 && oldDto.getDealFlag() == 1) {
    				dto.setCzlx(2);
    			} else {
    				dto.setCzlx(1);
    			}
    			dto.setDealFlag(0);
    			int result = userBandWidthMapper.update(dto);
    			if (result <= 0) {
    				return result;
    			}
    			updateUserMainInfoWhieSubsetInfoChange(dto.getUserId());
    			return 1;
    		} else {
    			log.error("没有找到带宽信息");
    			return 0;
    		}
		} catch (Exception e) {
			log.error("更新记录失败", e);
			return 0;
		}
    }

    @Override
    protected int delete(BaseModel baseModel) {
        return 0;
    }

	@Override
	public ResultDto insertData(List<UserBandwidthInformation> dtos, Long userId) {
		ResultDto resultDto = new ResultDto();
		resultDto.setResultCode(ResultDto.ResultCodeEnum.ERROR.getCode());
		if (dtos != null && dtos.size() > 0) {
			for (UserBandwidthInformation dto : dtos) {
				try {
					if (userId != null) {
						dto.setUserId(userId);
					}
					int result = insert(dto);
					if (result <= 0) {
						return getErrorResult("新增记录失败");
					}
					log.info("insert user bandwidth success!");
				} catch (Exception e) {
					log.error("insert user bandwidth data failed!", e);
					return getErrorResult("新增记录失败");
				}
			}
			return getSuccessResult();
		}
		return resultDto;
	}
}
