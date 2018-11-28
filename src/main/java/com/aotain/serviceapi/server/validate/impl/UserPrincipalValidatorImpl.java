package com.aotain.serviceapi.server.validate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.ResultCodeEnum;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.IdcJcdmXzqydmMapper;
import com.aotain.serviceapi.server.dao.dic.DictionaryMapper;
import com.aotain.serviceapi.server.dao.preinput.IdcInformationMapper;
import com.aotain.serviceapi.server.dao.preinput.UserPrincipalMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.util.StringUtil;
import com.aotain.serviceapi.server.validate.IUserPrincipalValidator;

/**
 * 用户主体校验类
 *
 * @author bang
 * @date 2018/07/09
 */
@Service
public class UserPrincipalValidatorImpl extends CommonValidator  implements IUserPrincipalValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_USER_PREFIX;

    @Autowired
    private IdcInformationMapper idcInformationMapper;
    
    @Autowired
    private UserPrincipalMapper userPrincipalMapper;
    
    @Autowired
    private IdcJcdmXzqydmMapper idcJcdmXzqydmMapper;
    
    @Autowired
    private DictionaryMapper dictionaryMapper;
    
    @Override
    public boolean existJyzId(UserInformationDTO dto) {
        if (dto.getJyzId()==null){
            return false;
        }
        IDCInformationDTO idcInformationDTO = new IDCInformationDTO();
        idcInformationDTO.setJyzId(dto.getJyzId());
        return idcInformationMapper.findByJyzId(idcInformationDTO)!=null?true:false;
    }

    @Override
    public AjaxValidationResult preValidate(BaseModel baseModel) {
        UserInformation dto = (UserInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        //检验用户主体的隶属单位是否都包含子节点的隶属单位
        validateAreaContainsSubInfo(dto, errorsArgsMap);
//        if ( !existJyzId(dto) ){
//            if (errorsArgsMap.get("jyzId")==null){
//                errorsArgsMap.put("jyzId", new String[]{"[经营者ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
//            } else {
//                Object[] errorMsg = errorsArgsMap.get("jyzId");
//                arrayExpand(errorMsg,"[经营者ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
//                errorsArgsMap.put("jyzId", errorMsg);
//            }
//        }
		/*if (dto.getOfficerIdType() != null && dto.getOfficerId() != null) {
			if (!BasicValidateUtils.idNumMatchType(dto.getOfficerIdType(), dto.getOfficerId())) {
				if (errorsArgsMap.get("officerId") == null) {
					errorsArgsMap.put("officerId", new String[] { "[网络信息安全责任人" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]" });
				} else {
					Object[] errorMsg = errorsArgsMap.get("officerId");
					arrayExpand(errorMsg, "[网络信息安全责任人" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
					errorsArgsMap.put("officerId", errorMsg);
				}
			}
		}
		if (dto.getIdType() != null && dto.getIdNumber() != null) {
			if (!BasicValidateUtils.idNumMatchType(dto.getIdType(), dto.getIdNumber())) {
				if (errorsArgsMap.get("idNumber") == null) {
					errorsArgsMap.put("idNumber", new String[] { "[单位" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]" });
				} else {
					Object[] errorMsg = errorsArgsMap.get("idNumber");
					arrayExpand(errorMsg, "[单位" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
					errorsArgsMap.put("idNumber", errorMsg);
				}
			}
		}
		// 用户属性为其它用户时才需要校验注册时间
		if (dto.getNature() != null && dto.getNature() == 2) {
			if (dto.getServiceRegTime() == null) {
				errorsArgsMap.put("serviceRegTime", new Object[] { "服务开通日期" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				ValidateResult result = validateServiceRegTime(dto.getServiceRegTime().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("serviceRegTime", new Object[] { result.getMsg() });
				}
			}
		}*/
		return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
    	UserInformationDTO dto = (UserInformationDTO)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        /*if ( !ifExistJyzId(dto) ){
            if (errorsArgsMap.get("jyzId")==null){
                errorsArgsMap.put("jyzId", new String[]{"[经营者ID"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("jyzId");
                arrayExpand(errorMsg,"[经营者ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("jyzId", errorMsg);
            }
        }*/
        //TODO:用户完整性校验
        
        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult validateBean(BaseModel baseModel) {
    	UserInformation dto = (UserInformation)baseModel;
        List<ObjectError> errors = new ArrayList<ObjectError>(0);
        Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
        int flag = 0;
        String msg = "";
        ValidateResult result = null;
        IdcJcdmXzqydm idcJcdmXzqydm = null;
//        if (dto.getJyzId()==null){
//            errorsArgsMap.put("jyzId",new Object[]{"经营者ID"+ErrorCodeConstant.ERROR_1009.getErrorMsg()});
//        } else {
//            if (!vaildateSuccess(validateProviderId(dto.getJyzId().toString()))){
//                errorsArgsMap.put("jyzId",new Object[]{validateProviderId(dto.getJyzId().toString()).getMsg()});
//            }
//        }
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.DELETE.getValue()) {
        	if (dto.getUserId() == null) {
				errorsArgsMap.put("userId", new Object[] { "用户ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} 
        }
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
        	if (dto.getUnitName() == null) {
    			errorsArgsMap.put("unitName", new Object[] { "单位名称" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateUnitName(dto.getUnitName().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("unitName", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getNature() == null) {
    			errorsArgsMap.put("nature", new Object[] { "用户属性" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateNature(dto.getNature().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("nature", new Object[] { result.getMsg() });
    			} 
    		}
			// 用户属性为其它用户时才需要校验注册时间
			if (dto.getNature() != null && dto.getNature() == 2) {
				if (dto.getServiceRegTime() == null) {
					errorsArgsMap.put("serviceRegTime", new Object[] { "服务开通日期" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
				} else {
					result = validateServiceRegTime(dto.getServiceRegTime().toString());
					if (!vaildateSuccess(result)) {
						errorsArgsMap.put("serviceRegTime", new Object[] { result.getMsg() });
					}
				}
			}
    		if (dto.getIdentify() == null) {
    			errorsArgsMap.put("identify", new Object[] { "用户标识" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateIdentify(dto.getIdentify());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("identify", new Object[] { result.getMsg() });
    			} else {
    				//校验专线标识信息是否在用户账号携带的专线标识中
    				String[] identifyArr = dto.getIdentify().split(",");
    				if (identifyArr != null && identifyArr.length > 0) {
    					for (String identify : identifyArr) {
    						result = validateAuthIdentity(Integer.parseInt(identify), baseModel);
    						if (!vaildateSuccess(result)) {
    							if (errorsArgsMap.get("identify") == null) {
    								errorsArgsMap.put("identify", new String[] { result.getMsg() });
    							} else {
    								Object[] errorMsg = errorsArgsMap.get("identify");
    								arrayExpand(errorMsg, result.getMsg());
    								errorsArgsMap.put("identify", errorMsg);
    							}
            				}
						}
    				}
    			}
    		}
    		if (dto.getUnitNature() == null) {
    			errorsArgsMap.put("unitNature", new Object[] { "单位属性" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateUnitNature(dto.getUnitNature().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("unitNature", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getIdType() == null) {
    			errorsArgsMap.put("idType", new Object[] { "单位证件类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateUnitIdType(dto.getIdType().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("idType", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getIdNumber() == null) {
    			errorsArgsMap.put("idNumber", new Object[] { "单位证件号码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateUnitIdNumber(dto.getIdNumber().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("idNumber", new Object[] { result.getMsg() });
    			}
    		}
			if (dto.getIdType() != null && dto.getIdNumber() != null) {
        		if (dto.getIdType()==999){
        			errorsArgsMap.remove("idNumber");
				}
				if (!BasicValidateUtils.idNumMatchType(dto.getIdType(), dto.getIdNumber())) {
					if (errorsArgsMap.get("idNumber") == null) {
						errorsArgsMap.put("idNumber", new String[] { "[单位证件号码" + ErrorCodeConstant.ERROR_1004.getErrorMsg() + "]" });
					} else {
						Object[] errorMsg = errorsArgsMap.get("idNumber");
						arrayExpand(errorMsg, "[单位证件号码" + ErrorCodeConstant.ERROR_1004.getErrorMsg() + "]");
						errorsArgsMap.put("idNumber", errorMsg);
					}
				} else {
					//单位名称+证件类型+证件号码重复检验
					if (!dto.getCoverUser() && ifExistUnitName(dto)) {
						flag = -2;
						if (!StringUtils.isEmpty(userPrincipalMapper.findByUnitNameAndIdTypeAndNumber(dto).getUserId())){
							UserInformation userInformation = userPrincipalMapper.findByUnitNameAndIdTypeAndNumber(dto);
							msg = userInformation.getUserId()+"-"+userInformation.getIdentify();
							if(!userInformation.getNature().equals(dto.getNature())){
								if(userInformation.getNature().equals(1)){
									errorsArgsMap.put("nature", new String[] { "[新增的用户名称属性为提供互联网应用服务的用户，不能再新增属性为其它用户，请进行用户属性变更！]" });
								}else if(userInformation.getNature().equals(2)){
									errorsArgsMap.put("nature", new String[] { "[新增的用户名称属性为其它用户，不能再新增属性为提供互联网应用服务的用户，请进行用户属性变更！]" });
								}
							}
						}

						if (errorsArgsMap.get("unitName") == null) {
							errorsArgsMap.put("unitName", new String[] { "[单位" + ErrorCodeConstant.ERROR_1011.getErrorMsg() + "]" });
						} else {
							Object[] errorMsg = errorsArgsMap.get("unitName");
							arrayExpand(errorMsg, "[单位" + ErrorCodeConstant.ERROR_1011.getErrorMsg() + "]");
							errorsArgsMap.put("unitName", errorMsg);
						}
					}


				}
			}
    		if (dto.getUnitAddress() == null) {
    			errorsArgsMap.put("unitAddress", new Object[] { "单位地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateUnitAddress(dto.getUnitAddress().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("unitAddress", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getUnitAddressProvinceCode() == null) {
    			errorsArgsMap.put("unitAddressProvinceCode", new Object[] { "单位所在省或直辖市" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateUnitAddressProvinceCode(dto.getUnitAddressProvinceCode().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("unitAddressProvinceCode", new Object[] { result.getMsg() });
    			} else {
    				idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(dto.getUnitAddressProvinceCode());
					if (idcJcdmXzqydm != null) {
						if(!StringUtils.isEmpty(idcJcdmXzqydm.getPostCode())){
							dto.setUnitZipCode(idcJcdmXzqydm.getPostCode());
						}
						dto.setUnitAddressProvinceName(idcJcdmXzqydm.getMc());
					}
    			}
    		}
    		if (dto.getUnitAddressCityCode() == null||dto.getUnitAddressCityCode() =="") {
    			//errorsArgsMap.put("unitAddressCityCode", new Object[] { "单位所在市或区(县)" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateUnitAddressCityCode(dto.getUnitAddressCityCode().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("unitAddressCityCode", new Object[] { result.getMsg() });
    			} else {
    				idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(dto.getUnitAddressCityCode());
					if (idcJcdmXzqydm != null) {
						if(!StringUtils.isEmpty(idcJcdmXzqydm.getPostCode())){
							dto.setUnitZipCode(idcJcdmXzqydm.getPostCode());
						}
						dto.setUnitAddressCityName(idcJcdmXzqydm.getMc());
					}
    			}
    		}
			if (dto.getUnitAddressAreaCode() == null||dto.getUnitAddressAreaCode()=="") {
				//errorsArgsMap.put("unitAddressAreaCode", new Object[] { "单位所在县" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
			} else {
				result = validateUnitAddressAreaCode(dto.getUnitAddressAreaCode().toString());
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("unitAddressAreaCode", new Object[] { result.getMsg() });
				} else {
					idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(dto.getUnitAddressAreaCode());
					if (idcJcdmXzqydm != null && !StringUtils.isEmpty(idcJcdmXzqydm.getPostCode())) {
						dto.setUnitZipCode(idcJcdmXzqydm.getPostCode());
						if (StringUtil.isEmptyString(dto.getUnitAddressAreaName())) {
							dto.setUnitAddressAreaName(idcJcdmXzqydm.getMc());
						}
					}
					if (dto.getUnitZipCode() == null) {
						errorsArgsMap.put("unitZipCode", new Object[] { "[机房邮编不在行政区域代码表中]" });
					} else {
						result = validateUnitZipCode(dto.getUnitZipCode().toString());
						if (!vaildateSuccess(result)) {
							errorsArgsMap.put("unitZipCode", new Object[] { result.getMsg() });
						}
					}
				}
			}
//            if (dto.getUnitZipCode()==null){
//                errorsArgsMap.put("unitZipCode",new Object[]{"邮政编码"+ErrorCodeConstant.ERROR_1008.getErrorMsg()});
//            } else {
//                if (!vaildateSuccess(validateUnitZipCode(dto.getUnitZipCode().toString()))){
//                    errorsArgsMap.put("unitZipCode",new Object[]{validateUnitZipCode(dto.getUnitZipCode().toString()).getMsg()});
//                }
//            }
    		if (dto.getRegisteTime() == null) {
    			errorsArgsMap.put("registeTime", new Object[] { "注册日期" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateRegisteTime(dto.getRegisteTime().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("registeTime", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getOfficerName() == null) {
    			errorsArgsMap.put("officerName",
    					new Object[] { "联系人姓名" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateOfficerName(dto.getOfficerName().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("officerName", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getOfficerIdType() == null) {
    			errorsArgsMap.put("officerIdType", new Object[] { "联系人证件类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateOfficerIdType(dto.getOfficerIdType().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("officerIdType", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getOfficerId() == null) {
    			errorsArgsMap.put("officerId", new Object[] { "联系人证件号码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateOfficerId(dto.getOfficerId().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("officerId", new Object[] { result.getMsg() });
    			}
    		}
			if (dto.getOfficerIdType() != null && dto.getOfficerId() != null) {
				if (!BasicValidateUtils.idNumMatchType(dto.getOfficerIdType(), dto.getOfficerId())) {
					if (errorsArgsMap.get("officerId") == null) {
						errorsArgsMap.put("officerId", new String[] { "[联系人" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]" });
					} else {
						Object[] errorMsg = errorsArgsMap.get("officerId");
						arrayExpand(errorMsg, "[联系人" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
						errorsArgsMap.put("officerId", errorMsg);
					}
				}
			}
    		if (dto.getOfficerTelphone() == null) {
    			errorsArgsMap.put("officerTelphone", new Object[] { "联系人固定电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateOfficerTelphone(dto.getOfficerTelphone().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("officerTelphone", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getOfficerMobile() == null) {
    			errorsArgsMap.put("officerMobile", new Object[] { "联系人移动电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateOfficerMobile(dto.getOfficerMobile().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("officerMobile", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getOfficerEmail() == null) {
    			errorsArgsMap.put("officerEmail", new Object[] { "联系人Email" + ErrorCodeConstant.ERROR_1009.getErrorMsg() });
    		} else {
    			result = validateOfficerEmail(dto.getOfficerEmail().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("officerEmail", new Object[] { result.getMsg() });
    			}
    		}
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
		return new AjaxValidationResult(errors, errorsArgsMap,flag,msg);
    }

	private void validateAreaContainsSubInfo(UserInformation dto, Map<String, Object[]> errorsArgsMap) {
		UserInformationDTO po = new UserInformationDTO();
		UserInformation userInformation = null;
		//List<ChinaArea> list = dictionaryMapper.g
		if(dto.getUserId()!=null){
			po.setUserId(dto.getUserId());
			userInformation = userPrincipalMapper.findByUserId(po);
			String[] dbAreaArr =  userInformation.getAreaCode().split(",");//单位原有地市信息
			String[] updateAreaArr = dto.getAreaCode().split(",");//用户更新地市信息
			List<String> authCodeList = dto.getCityCodeList();//账号地市信息
			List<String> userShowCodeList = new ArrayList();//用户页面展示地市信息
			for(String areaCode:dbAreaArr){
				for(String authArea:authCodeList){
					if(areaCode==authArea){
						userShowCodeList.add(areaCode);
						break;
					}
				}
			}
			List<String> lostAreaList = new ArrayList();
			for(String dbStr:userShowCodeList){
				Boolean areaFlag = false;
				for(String updateStr:updateAreaArr){
					if(dbStr.equals(updateStr)){
						areaFlag = true;
						break;
					}
				}
				if(!areaFlag){
					lostAreaList.add(dbStr);
				}
			}
			String userServiceStr = "";
			String userHHStr = "";
			String virtualStr = "";
			UserInformation uDto = null;
			StringBuffer areaErrMsg = new StringBuffer("该用户");
			for(String area:lostAreaList){
				uDto = new UserInformation();
				uDto.setUserId(dto.getUserId());
				uDto.setAreaCode(area);
				if(!isContainUserServiceArea(uDto)){
					userServiceStr+= "["+area+"]";
				}
				if(!isContainUserNetWorkArea(uDto)){
					userHHStr+= "["+area+"]";
				}
				if(!isContainUserVirtualArea(uDto)){
					virtualStr+= "["+area+"]";
				}
			}
			if(userServiceStr!=""){
				areaErrMsg.append("服务节点存在以下地市："+userServiceStr+";");
			}
			if(userHHStr!=""){
				areaErrMsg.append("网络资源存在以下地市："+userHHStr+";");
			}
			if(virtualStr!=""){
				areaErrMsg.append("虚拟主机节点存在以下地市："+virtualStr+";");
			}
			if(userServiceStr!=""||userHHStr!=""||virtualStr!=""){
				areaErrMsg.append("请删除子节点拥有的隶属单位记录后才可变更！");
				errorsArgsMap.put("areaCode", new Object[] { areaErrMsg.toString() });
			}
		}
	}

    private boolean isContainUserVirtualArea(UserInformation dto) {
		return userPrincipalMapper.findCountNotContainUserVirutalArea(dto)>0?false:true;
	}

	private boolean isContainUserNetWorkArea(UserInformation dto) {
		return userPrincipalMapper.findCountNotContainUserNetWorkArea(dto)>0?false:true;
	}

	private boolean isContainUserServiceArea(UserInformation dto) {
		return userPrincipalMapper.findCountNotContainUserServerArea(dto)>0?false:true;
	}

	private ValidateResult validateProviderId(String jyzId){
        String propertyName = "经营者ID";
        return BasicValidateUtils.validateMaxLength(propertyName,jyzId,getFiledLengthByPropertyName(prefix,"jyzId"));
    }

    private ValidateResult validateUnitName(String unitName){
        String propertyName = "单位名称";
        return BasicValidateUtils.validateMaxLength(propertyName,unitName,getFiledLengthByPropertyName(prefix,"unitName"));
    }

    private ValidateResult validateUserCode(String userCode){
        String propertyName = "用户编号";
        return BasicValidateUtils.validateMaxLength(propertyName,userCode,getFiledLengthByPropertyName(prefix,"userCode"));
    }

    private ValidateResult validateNature(String nature){
        String propertyName = "用户属性";
        return validateUserNature(propertyName,nature);
    }

    private ValidateResult validateIdentify(String identify){
        String propertyName = "用户标识";
        ValidateResult result=null;
        if (identify.indexOf(",")>0){
            String[] ides = identify.split(",");
            for (String ind :ides){
                result = validateUserIdentify(propertyName,ind);
                if (result.getCode() != ResultCodeEnum.SUCCESS.getCode()){
                    return result;
                }
            }
        }else {
            return validateUserIdentify(propertyName,identify);
        }
        return result;
    }

    private ValidateResult validateUnitNature(String unitNature){
        String propertyName = "单位属性";
        return validateUnitNature(propertyName,unitNature);
    }

    private ValidateResult validateUnitIdType(String idType){
        String propertyName = "单位证件类型";
        return validateIdCardType(propertyName,idType);
    }

    private ValidateResult validateUnitIdNumber(String idNumber){
        String propertyName = "单位证件号码";
        return validateUnitIdCard(propertyName,idNumber,getFiledLengthByPropertyName(prefix,"idNumber"));
    }

    private ValidateResult validateUnitAddress(String unitAddress){
        String propertyName = "单位地址";
        return BasicValidateUtils.validateMaxLength(propertyName,unitAddress,getFiledLengthByPropertyName(prefix,"unitAddress"));
    }

    private ValidateResult validateUnitAddressProvinceCode(String unitAddressProvinceCode){
        String propertyName = "单位所在省或直辖市";
        return BasicValidateUtils.validateFixedLength(propertyName,unitAddressProvinceCode,getFiledLengthByPropertyName(prefix,"unitAddressProvinceCode"));
    }

    private ValidateResult validateUnitAddressCityCode(String unitAddressCityCode){
        String propertyName = "单位所在市或区(县)";
        return BasicValidateUtils.validateFixedLength(propertyName,unitAddressCityCode,getFiledLengthByPropertyName(prefix,"unitAddressCityCode"));
    }

    private ValidateResult validateUnitAddressAreaCode(String unitAddressAreaCode){
        String propertyName = "单位所在县";
        return BasicValidateUtils.validateFixedLength(propertyName,unitAddressAreaCode,getFiledLengthByPropertyName(prefix,"unitAddressAreaCode"));
    }

    private ValidateResult validateUnitZipCode(String unitZipCode){
        String propertyName = "邮政编码";
        return BasicValidateUtils.validateFixedLength(propertyName,unitZipCode,getFiledLengthByPropertyName(prefix,"unitZipCode"));
    }

    private ValidateResult validateRegisteTime(String registeTime){
        String propertyName = "注册日期";
        return validateTime(propertyName,"yyyy-MM-dd",registeTime);
    }

    private ValidateResult validateServiceRegTime(String serviceRegTime){
        String propertyName = "服务开通日期";
        return validateTime(propertyName,"yyyy-MM-dd",serviceRegTime);
    }

    private ValidateResult validateOfficerName(String officerName){
        String propertyName = "联系人姓名";
        return BasicValidateUtils.validateMaxLength(propertyName,officerName,getFiledLengthByPropertyName(prefix,"officerName"));
    }

    private ValidateResult validateOfficerIdType(String officerIdType){
        String propertyName = "联系人证件类型";
        return validatePersonalCardType(propertyName,officerIdType);
    }

    private ValidateResult validateOfficerId(String officerId){
        String propertyName = "联系人证件号码";
        return validateIdCard(propertyName,officerId,getFiledLengthByPropertyName(prefix,"officerId"));
    }

    private ValidateResult validateOfficerTelphone(String officerTelphone){
        String propertyName = "联系人固定电话";
        return validateTelephoneNum(propertyName,officerTelphone,getFiledLengthByPropertyName(prefix,"officerTelphone"));
    }

    private ValidateResult validateOfficerMobile(String officerMobile){
        String propertyName = "联系人移动电话";
        return validateMobileNum(propertyName,officerMobile,getFiledLengthByPropertyName(prefix,"officerMobile"));
    }

    private ValidateResult validateOfficerEmail(String officerEmail){
        String propertyName = "联系人Email";
        return validateEmail(propertyName,officerEmail,getFiledLengthByPropertyName(prefix,"officerEmail"));
    }

    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }
    
	public boolean ifExistUnitName(UserInformation dto) {
		if (dto.getUnitName() == null) {
			return false;
		}
		return userPrincipalMapper.findByUnitNameAndIdTypeAndNumber(dto) != null ? true : false;
	}
}
