package com.aotain.serviceapi.server.validate.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

import com.aotain.common.config.LocalConfig;
import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.HouseValidateConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.preinput.HouseIpSegmentMapper;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.util.IpUtil;
import com.aotain.serviceapi.server.util.Tools;
import com.aotain.serviceapi.server.validate.IHouseIpSegmentValidator;


/**
 * 机房ip地址段校验器
 *
 * @author bang
 * @date 2018/07/06
 */
@Service
public class HouseIpSegmentValidatorImpl extends CommonValidator  implements IHouseIpSegmentValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_HOUSE_IPSEG_PREFIX;

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private RptIsmsBaseHouseMapper rptHouseMainMapper;

    @Autowired
    private HouseIpSegmentMapper houseIpSegmentMapper;

	@Override
	public boolean existHouseId(HouseIPSegmentInformation dto) {
		if (dto.getHouseId() == null) {
			return false;
		}
		HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
		houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
		return housePrincipalMapper.findByHouseId(houseInformationDTO) != null ? true : false;
	}

	@Override
	public boolean existHouseName(HouseIPSegmentInforDTO dto) {
		if (dto.getHouseName() == null) {
			return false;
		}
		HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
		houseInformationDTO.setHouseName(dto.getHouseName());
		return housePrincipalMapper.findByHouseName(houseInformationDTO) != null ? true : false;
	}

	@Override
	public ValidateResult ipConflictCheck(HouseIPSegmentInformation dto) {
		if (Tools.isIp(dto.getStartIP()) && Tools.isIp(dto.getEndIP()) && dto.getIpType() != null && vaildateSuccess(validateEndHouseIp(dto.getStartIP(), dto.getEndIP()))) {
			// 起始和结束ip都不能为空
			if (StringUtils.isEmpty(dto.getStartIPStr())) {
				if (IpUtil.isIpv6(dto.getStartIP())) {
					dto.setStartIPStr(IpUtil.ipv6ToBigInteger(dto.getStartIP()) + "");
				} else {
					dto.setStartIPStr(Tools.ip2long(dto.getStartIP()) + "");
				}
			}
			if (StringUtils.isEmpty(dto.getEndIPStr())) {
				if (IpUtil.isIpv6(dto.getEndIP())) {
					dto.setEndIPStr(IpUtil.ipv6ToBigInteger(dto.getEndIP()) + "");
				} else {
					dto.setEndIPStr(Tools.ip2long(dto.getEndIP()) + "");
				}
			}
			StringBuilder msg = new StringBuilder("");
			boolean flag = false;
			if (HouseValidateConstant.IpSegmentUseType.SPECIAL.getValue().equals(dto.getIpType()) || HouseValidateConstant.IpSegmentUseType.CLOUD_VIRTUAL.getValue().equals(dto.getIpType())) {
				if (houseIpSegmentMapper.countConflictIpSegment(dto) > 0) {
					flag = true;
					msg = new StringBuilder("[" + dto.getStartIP() + "-" + dto.getEndIP() + ErrorCodeConstant.ERROR_1013.getErrorMsg() + "]");
				} else if (houseIpSegmentMapper.countIpSegmentSpecialForSameUnit(dto) > 0) {
					flag = true;
					msg = new StringBuilder("[" + dto.getStartIP() + "-" + dto.getEndIP() + ErrorCodeConstant.ERROR_1013.getErrorMsg() + "]");
				}
				if (houseIpSegmentMapper.countIpSegmentForSpecial(dto) > 0) {
					flag = true;
					msg.append("[" + dto.getStartIP() + "-" + dto.getEndIP() + ErrorCodeConstant.ERROR_1013.getErrorMsg() + "]");
				}
				if (flag) {
					return ValidateResult.getErrorResult(msg.toString());
				}
			} else {
				return houseIpSegmentMapper.countConflictIpSegment(dto) > 0 ? ValidateResult.getErrorResult("[" + dto.getStartIP() + "-" + dto.getEndIP() + ErrorCodeConstant.ERROR_1013.getErrorMsg() + "]") : ValidateResult.getSuccessResult();
			}

		}
		return ValidateResult.getSuccessResult();
	}

    @Override
    public AjaxValidationResult preValidate(BaseModel baseModel) {
    	HouseIPSegmentInformation dto = (HouseIPSegmentInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
		ValidateResult result = null;
		if (dto.getAddType() != null && dto.getAddType() == 1 && !existHouseId(dto)) {
			if (errorsArgsMap.get("houseId") == null) {
				errorsArgsMap.put("houseId", new String[] { "[机房ID" + ErrorCodeConstant.ERROR_1008.getErrorMsg() + "]" });
			} else {
				Object[] errorMsg = errorsArgsMap.get("houseId");
				arrayExpand(errorMsg, "[机房ID" + ErrorCodeConstant.ERROR_1008.getErrorMsg() + "]");
				errorsArgsMap.put("houseId", errorMsg);
			}
			result = validateAuthHouse(dto.getHouseId(), baseModel);
			if (!vaildateSuccess(result)) {
				errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
			}
		}

		if (dto.getIpType() != null && !HouseValidateConstant.IpSegmentUseType.REMAIN.getValue().equals(dto.getIpType())) {
			if (dto.getUserName() == null) {
				errorsArgsMap.put("userName", new Object[] { "单位名称" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				result = validateUserName(dto.getUserName());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("userName", new Object[] { result.getMsg() });
				}
			}
		}

		// 判断是否是专线机房
		if (existHouseId(dto) && dto.getIpType() != null ) {
			HouseInformationDTO houseInformationDTO = new HouseInformationDTO();
			houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
			HouseInformationDTO resultHouse = housePrincipalMapper.findByHouseId(houseInformationDTO);
			if (resultHouse.getIdentity() != null && resultHouse.getIdentity() == 1) {
				// 专线
				if (!dto.getIpType().equals(HouseConstant.IpUseTypeEnum.REMAIN.getValue()) ||
						!dto.getIpType().equals(HouseConstant.IpUseTypeEnum.SPECIAL.getValue())){
					errorsArgsMap.put("ipType", new Object[] { "[专线机房只有保留和专线的IP地址段]" });
				}

			} else if (resultHouse.getIdentity() != null && resultHouse.getIdentity() == 2){
				// IDC机房
				if (dto.getIpType().equals(HouseConstant.IpUseTypeEnum.SPECIAL.getValue())){
					errorsArgsMap.put("ipType", new Object[] { "[IDC机房下不能有专线IP]" });
				}
			}
		}
		
		result = ipConflickAndRepeatValidate(dto);
		if (!vaildateSuccess(result)) {
			errorsArgsMap.put("confickIp", new Object[] { result.getMsg() });
		}
        return ajaxValidationResult;
    }


    /**
     * 冲突和重复IP校验
     * 
     * @author : songl
     */
	private ValidateResult ipConflickAndRepeatValidate(HouseIPSegmentInformation dto) {
		if (IpUtil.isIpAddress(dto.getStartIP()) && IpUtil.isIpAddress(dto.getEndIP()) && dto.getIpType() != null && vaildateSuccess(validateEndHouseIp(dto.getStartIP(), dto.getEndIP()))) {
			if (IpUtil.isIpv6(dto.getStartIP())) {
				dto.setStartIPStr(IpUtil.ipv6ToBigInteger(dto.getStartIP()) + "");
			} else {
				dto.setStartIPStr(Tools.ip2long(dto.getStartIP()) + "");
			}
			if (IpUtil.isIpv6(dto.getEndIP())) {
				dto.setEndIPStr(IpUtil.ipv6ToBigInteger(dto.getEndIP()) + "");
			} else {
				dto.setEndIPStr(Tools.ip2long(dto.getEndIP()) + "");
			}
			int limitNum = 5;//限制展示条数
			StringBuffer ipErrMsg = new StringBuffer("");
			
			//冲突IP校验
			String IpConflickSwitch = LocalConfig.getInstance().getHashValueByHashKey("ip_import_enabled");//IP导入冲突是否强制覆盖开关
			if (dto.getOperateType() != HouseConstant.OperationTypeEnum.DELETE.getValue()&&"false".equals(IpConflickSwitch)){
				List<HouseIPSegmentInformation> ipList = houseIpSegmentMapper.findConflickIp(dto);
				if(ipList!=null&&ipList.size()>0){
					ipErrMsg.append("["+dto.getStartIP()+"-"+dto.getEndIP()+"与现有IP段冲突];");
					ipErrMsg.append("有"+ipList.size()+"条IP冲突,最多展示"+limitNum+"条;");
					for(int i=0;i< ipList.size();i++){
						if(i<limitNum){
							if(i==(ipList.size()-1)||i==(limitNum-1)){
								ipErrMsg.append(ipList.get(i).getStartIP()+"-"+ipList.get(i).getEndIP()+";");
							}else{
								ipErrMsg.append(ipList.get(i).getStartIP()+"-"+ipList.get(i).getEndIP()+",");
							}
						}
					}
				}
			}
				
			//重复IP校验(仅限专线或云虚拟类型)
			String IpRepeatSwitch = LocalConfig.getInstance().getHashValueByHashKey("ip_repeat_enabled");//IP重复录入开关
			if (dto.getOperateType() != HouseConstant.OperationTypeEnum.DELETE.getValue()&&"false".equals(IpRepeatSwitch)&&(dto.getIpType()==3||dto.getIpType()==999)){
				List<HouseIPSegmentInformation> ipList = houseIpSegmentMapper.findRepeatIp(dto);
				if(ipList!=null&&ipList.size()>0){ 
					ipErrMsg.append("["+dto.getStartIP()+"-"+dto.getEndIP()+"与现有IP段重复];");
					ipErrMsg.append("有"+ipList.size()+"条IP重复,最多展示"+limitNum+"条;");
					for(int i=0;i< ipList.size();i++){
						if(i<limitNum){
							if(i==(ipList.size()-1)||i==(limitNum-1)){
								ipErrMsg.append(ipList.get(i).getStartIP()+"-"+ipList.get(i).getEndIP());
							}else{
								ipErrMsg.append(ipList.get(i).getStartIP()+"-"+ipList.get(i).getEndIP()+",");
							}
						}
					}
				}
			}
			if(!"".equals(ipErrMsg.toString())){
				return ValidateResult.getErrorResult(ipErrMsg.toString());
			}
		}
		return ValidateResult.getSuccessResult();
	}

	@Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
        HouseIPSegmentInforDTO dto = (HouseIPSegmentInforDTO)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        if ( dto.getAddType()!=null && dto.getAddType()==1 && !ifExistHouseId(dto) ){
            if (errorsArgsMap.get("houseId")==null){
                errorsArgsMap.put("houseId", new String[]{"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseId");
                arrayExpand(errorMsg,"[机房ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("houseId", errorMsg);
            }
        }
        if (dto.getIdType()!=null&&dto.getIdNumber()!=null){
            if (!BasicValidateUtils.idNumMatchType(dto.getIdType(),dto.getIdNumber())){
                if (errorsArgsMap.get("idNumber")==null){
                    errorsArgsMap.put("idNumber", new String[]{"["+ErrorCodeConstant.ERROR_1002.getErrorMsg()+"]"});
                } else {
                    Object[] errorMsg = errorsArgsMap.get("idNumber");
                    arrayExpand(errorMsg,"["+ErrorCodeConstant.ERROR_1002.getErrorMsg()+"]");
                    errorsArgsMap.put("idNumber", errorMsg);
                }
            }
        }
        return ajaxValidationResult;
    }

    @Override
	public AjaxValidationResult validateBean(BaseModel baseModel) {
		List<ObjectError> errors = new ArrayList<ObjectError>(0);
		Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
		HouseIPSegmentInformation dto = (HouseIPSegmentInformation) baseModel;
		ValidateResult result = null;
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.DELETE.getValue()) {
			if (dto.getAddType() != null && dto.getAddType() == 1) {
				if (dto.getHouseId() == null) {
					errorsArgsMap.put("houseId", new Object[] { "机房ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
				} else {
					result = validateHouseId(dto.getHouseId().toString());
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
					}
					result = validateAuthHouse(dto.getHouseId(), baseModel);
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
					}
				}
			}
		}
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
			if (dto.getStartIP() == null) {
				errorsArgsMap.put("startIP", new Object[] { "起始IP地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				result = validateStartHouseIp(dto.getStartIP());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("startIP", new Object[] { result.getMsg() });
				}
			}
			if (dto.getEndIP() == null) {
				errorsArgsMap.put("endIP", new Object[] { "终止IP地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				result = validateEndHouseIp(dto.getStartIP(), dto.getEndIP());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("endIP", new Object[] { result.getMsg() });
				}
			}
			if (dto.getIpType() == null) {
				errorsArgsMap.put("ipType", new Object[] { "IP地址使用方式" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				result = validateIpUseType(dto.getIpType().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("ipType", new Object[] { result.getMsg() });
				}
				if (dto.getIpType() != 2) {
					if (dto.getUserName() == null) {
						errorsArgsMap.put("userName", new Object[] { "单位名称" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
					} else {
						result = validateUserName(dto.getUserName());
						if (!vaildateSuccess(result)) {
							errorsArgsMap.put("userName", new Object[] { result.getMsg() });
						}
						if (StringUtils.isEmpty(dto.getIdType()) && !StringUtils.isEmpty(dto.getIdNumber())) {
							errorsArgsMap.put("idType", new Object[] { "证件类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
						}
						if (!StringUtils.isEmpty(dto.getIdType()) && StringUtils.isEmpty(dto.getIdNumber())) {
							errorsArgsMap.put("idNumber", new Object[] { "证件号码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
						}
						if (!StringUtils.isEmpty(dto.getIdType()) && !StringUtils.isEmpty(dto.getIdNumber())) {
							boolean passed = true;
							result = validateUserIdType(dto.getIdType());
							if (!vaildateSuccess(result)) {
								passed = false;
								errorsArgsMap.put("idType", new Object[] { result.getMsg() });
							}
							result = validateIdCard(dto.getIdNumber());
							if (!vaildateSuccess(result)) {
								passed = false;
								errorsArgsMap.put("idNumber", new Object[] { result.getMsg() });
							}
							if (passed) {
								if (!BasicValidateUtils.idNumMatchType(dto.getIdType(), dto.getIdNumber())) {
									if (errorsArgsMap.get("idType") == null) {
										errorsArgsMap.put("idType", new String[] { "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]" });
									} else {
										Object[] errorMsg = errorsArgsMap.get("idType");
										arrayExpand(errorMsg, "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
										errorsArgsMap.put("idType", errorMsg);
									}
								}
							}
						}
					}
					
				}
			}
//	        if (dto.getUserName()==null){
//	            errorsArgsMap.put("userName",new Object[]{"单位名称"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+"});
//	        } else {
//	            if (!vaildateSuccess(validateUserName(dto.getUserName()))){
//	                errorsArgsMap.put("userName",new Object[]{validateUserName(dto.getUserName()).getMsg()});
//	            }
//	        }
			if (dto.getUseTime() == null) {
				errorsArgsMap.put("useTime", new Object[] { "分配使用日期" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				result = validateUserTime(dto.getUseTime());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("useTime", new Object[] { result.getMsg() });
				}
			}
	        /*if (dto.getAllocationUnit()==null){
	            errorsArgsMap.put("allocationUnit",new Object[]{"分配单位"+ErrorCodeConstant.ERROR_1009.getErrorMsg()});
	        } else {
	            if (!vaildateSuccess(validateAllocationUnit(dto.getAllocationUnit()))){
	                errorsArgsMap.put("allocationUnit",new Object[]{validateAllocationUnit(dto.getAllocationUnit()).getMsg()});
	            }
	        }
	        if (dto.getSourceUnit()==null){
	            errorsArgsMap.put("sourceUnit",new Object[]{"来源单位"+ErrorCodeConstant.ERROR_1009.getErrorMsg()});
	        } else {
	            if (!vaildateSuccess(validateSourceUnit(dto.getSourceUnit()))){
	                errorsArgsMap.put("sourceUnit",new Object[]{validateSourceUnit(dto.getSourceUnit()).getMsg()});
	            }
	        }*/
			if (dto.getAreaCode() == null) {
				errorsArgsMap.put("areaCode", new Object[] { "地市码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				if (dto.getAreaCodeValidateType() == 1) {
					result = validateAreaCode(dto);
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("areaCode", new Object[] { result.getMsg() });
					}
				}
			}
		}
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
			if (dto.getCreateUserId() == null) {
				errorsArgsMap.put("createUserId", new Object[] { "创建者" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			}
		}
		if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()) {
			if (dto.getUpdateUserId() == null) {
				errorsArgsMap.put("updateUserId", new Object[] { "修改者" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			}
		}
        return new AjaxValidationResult(errors, errorsArgsMap);
    }


    private ValidateResult validateHouseId(String houseId){
        String propertyName = "机房ID";
        return BasicValidateUtils.validateMaxLength(propertyName,houseId,getFiledLengthByPropertyName(prefix,"houseId"));
    }

    private ValidateResult validateStartHouseIp(String startIp){
        String propertyName = "起始IP地址";
        return validateStartIp(propertyName,startIp);
    }

    private ValidateResult validateEndHouseIp(String startIp,String endIp){
        String propertyName = "终止IP地址";
        return validateEndIp(propertyName,startIp,endIp);
    }

    private ValidateResult validateIpUseType(String ipUseType){
        String propertyName = "IP地址使用方式";
        return validateIpUseType(propertyName,ipUseType);
    }

    private ValidateResult validateUserName(String userName){
        String propertyName = "单位名称";
        return BasicValidateUtils.validateMaxLength(propertyName,userName,getFiledLengthByPropertyName(prefix,"userName"));
    }

    private ValidateResult validateUserTime(String userTime){
        String propertyName = "分配使用日期";
        return validateTime(propertyName,"yyyy-MM-dd",userTime);
    }

    private ValidateResult validateSourceUnit(String sourceUnit){
        String propertyName = "来源单位";
        return BasicValidateUtils.validateMaxLength(propertyName,sourceUnit,getFiledLengthByPropertyName(prefix,"sourceUnit"));
    }

    private ValidateResult validateAllocationUnit(String allocationUnit){
        String propertyName = "分配单位";
        return BasicValidateUtils.validateMaxLength(propertyName,allocationUnit,getFiledLengthByPropertyName(prefix,"allocationUnit"));
    }

    private ValidateResult validateUserIdType(Integer idType){
        String propertyName = "使用人证件类型";
        return validateIdCardType(propertyName,idType.toString());
    }

    private ValidateResult validateIdCard(String idNumber){
        String propertyName = "使用人证件号码";
        return validateIdCard(propertyName,idNumber,getFiledLengthByPropertyName(prefix,"idNumber"));
    }


    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }

    public static void main(String[] args) {
        HouseIpSegmentValidatorImpl houseIpSegmentValidatorImpl = new HouseIpSegmentValidatorImpl();
        HouseIPSegmentInforDTO houseIPSegmentInforDTO = new HouseIPSegmentInforDTO();
        houseIPSegmentInforDTO.setHouseId(1L);
        houseIPSegmentInforDTO.setIdType(2);
        houseIPSegmentInforDTO.setIdNumber("4210831993456312");
        System.out.println(houseIpSegmentValidatorImpl.validateBean(houseIPSegmentInforDTO)+"===========");
    }

    public boolean ifExistHouseId(HouseIPSegmentInforDTO dto) {
        if (dto.getHouseId()==null){
            return false;
        }
        HouseInformation houseInformationDTO = new HouseInformation();
        houseInformationDTO.setHouseId(Long.valueOf(dto.getHouseId()));
        return rptHouseMainMapper.findByHouseId(houseInformationDTO)!=null?true:false;
    }
}
