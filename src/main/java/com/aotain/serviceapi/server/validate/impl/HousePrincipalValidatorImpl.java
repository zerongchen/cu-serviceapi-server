package com.aotain.serviceapi.server.validate.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.serviceapi.server.constant.ErrorCodeConstant;
import com.aotain.serviceapi.server.constant.FieldLengthPropertyPrefix;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.constant.JCDMRedisConstant;
import com.aotain.serviceapi.server.constant.ValidateResult;
import com.aotain.serviceapi.server.dao.IdcJcdmXzqydmMapper;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import com.aotain.serviceapi.server.dao.preinput.IdcInformationMapper;
import com.aotain.serviceapi.server.dao.report.IdcInfoMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseMapper;
import com.aotain.serviceapi.server.util.BasicValidateUtils;
import com.aotain.serviceapi.server.validate.IHousePrincipalValidator;

/**
 * 机房主体校验器
 *
 * @author bang
 * @date 2018/07/06
 */
@Service
public class HousePrincipalValidatorImpl extends CommonValidator  implements IHousePrincipalValidator {

    private static final String prefix = FieldLengthPropertyPrefix.IDC_ISMS_BASE_HOUSE_PREFIX;

    @Autowired
    private IdcInformationMapper idcInformationMapper;

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private IdcJcdmXzqydmMapper idcJcdmXzqydmMapper;
    
    @Autowired
    private IdcInfoMapper rptIdcinfoMapper;
    
    @Autowired
    private RptIsmsBaseHouseMapper rptHouseMainMapper;
    
    @Override
    public boolean existJyzId(HouseInformationDTO dto) {
        if (dto.getJyzId()==null){
            return false;
        }
        IDCInformationDTO idcInformationDTO = new IDCInformationDTO();
        idcInformationDTO.setJyzId(dto.getJyzId());
        return idcInformationMapper.findByJyzId(idcInformationDTO)!=null?true:false;
    }

    @Override
    public boolean existIdcName(HouseInformationDTO dto){
        if (dto.getProviderName()==null){
            return false;
        }
        IDCInformationDTO idcInformationDTO = new IDCInformationDTO();
        idcInformationDTO.setIdcName(dto.getProviderName());
        return idcInformationMapper.findByProviderName(idcInformationDTO)!=null?true:false;
    }

	@Override
	public boolean uniqueHouseName(HouseInformation info) {
		if (info.getHouseName() == null) {
			return false;
		}
		HouseInformationDTO dto = new HouseInformationDTO();
		dto.setHouseId(info.getHouseId());
		dto.setHouseName(info.getHouseName());
		return housePrincipalMapper.findByHouseName(dto) == null ? true : false;
	}

	@Override
	public boolean uniqueHouseIdStr(HouseInformation info) {
		if (info.getHouseIdStr() == null) {
			return false;
		}
		HouseInformationDTO dto = new HouseInformationDTO();
		dto.setHouseId(info.getHouseId());
		dto.setHouseIdStr(info.getHouseIdStr());
		return housePrincipalMapper.findByHouseIdStr(dto) == null ? true : false;
	}

    @Override
    public AjaxValidationResult preValidate(BaseModel baseModel) {
        HouseInformation dto = (HouseInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        /*if ( !existJyzId(dto) ){
            if (errorsArgsMap.get("jyzId")==null){
                errorsArgsMap.put("jyzId", new String[]{"[经营者ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("jyzId");
                arrayExpand(errorMsg,"[经营者ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("jyzId", errorMsg);
            }
        }*/

//        if (ValidateTypeConstant.WEB_ADD.equals(baseModel.getValidateSourceType())){
//
//        } else if (ValidateTypeConstant.EXCEL_IMPORT.equals(baseModel.getValidateSourceType())){
//            if ( !existIdcName(dto) ){
//                if (errorsArgsMap.get("providerName")==null){
//                    errorsArgsMap.put("providerName", new String[]{"[经营者名称"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
//                } else {
//                    Object[] errorMsg = errorsArgsMap.get("providerName");
//                    arrayExpand(errorMsg,"[经营者名称"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
//                    errorsArgsMap.put("providerName", errorMsg);
//                }
//            }
//        }

        /*if ( !uniqueHouseName(dto) ){
            if (errorsArgsMap.get("houseName")==null){
                errorsArgsMap.put("houseName", new String[]{"[机房名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseName");
                arrayExpand(errorMsg,"[机房名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("houseName", errorMsg);
            }
        }
        if ( !uniqueHouseIdStr(dto) ){
            if (errorsArgsMap.get("houseIdStr")==null){
                errorsArgsMap.put("houseIdStr", new String[]{"[机房编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseIdStr");
                arrayExpand(errorMsg,"[机房编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("houseIdStr", errorMsg);
            }
        }
        if (dto.getHouseOfficerIdType()!=null&&dto.getHouseOfficerId()!=null){
            if (!BasicValidateUtils.idNumMatchType(dto.getHouseOfficerIdType(),dto.getHouseOfficerId())){
                if (errorsArgsMap.get("houseOfficerId")==null){
                    errorsArgsMap.put("houseOfficerId", new String[]{"["+ErrorCodeConstant.ERROR_1002.getErrorMsg()+"]"});
                } else {
                    Object[] errorMsg = errorsArgsMap.get("houseOfficerId");
                    arrayExpand(errorMsg,"["+ErrorCodeConstant.ERROR_1002.getErrorMsg()+"]");
                    errorsArgsMap.put("houseOfficerId", errorMsg);
                }
            }
        }*/
        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult repValidate(BaseModel baseModel) {
    	HouseInformation dto = (HouseInformation)baseModel;
        AjaxValidationResult ajaxValidationResult = validateBean(baseModel);
        Map<String, Object[]> errorsArgsMap = ajaxValidationResult.getErrorsArgsMap();
        if ( !ifExistJyzId(dto) ){
            if (errorsArgsMap.get("jyzId")==null){
                errorsArgsMap.put("jyzId", new String[]{"[经营者ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("jyzId");
                arrayExpand(errorMsg,"[经营者ID"+ErrorCodeConstant.ERROR_1008.getErrorMsg()+"]");
                errorsArgsMap.put("jyzId", errorMsg);
            }
        }

        if ( !ifRepeatHouseName(dto) ){
            if (errorsArgsMap.get("houseName")==null){
                errorsArgsMap.put("houseName", new String[]{"[机房名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseName");
                arrayExpand(errorMsg,"[机房名称"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("houseName", errorMsg);
            }
        }
        if ( !ifRepeatHouseIdStr(dto) ){
            if (errorsArgsMap.get("houseIdStr")==null){
                errorsArgsMap.put("houseIdStr", new String[]{"[机房编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]"});
            } else {
                Object[] errorMsg = errorsArgsMap.get("houseIdStr");
                arrayExpand(errorMsg,"[机房编号"+ErrorCodeConstant.ERROR_1001.getErrorMsg()+"]");
                errorsArgsMap.put("houseIdStr", errorMsg);
            }
        }
        if (dto.getHouseOfficerIdType()!=null&&dto.getHouseOfficerId()!=null){
            if (!BasicValidateUtils.idNumMatchType(dto.getHouseOfficerIdType(),dto.getHouseOfficerId())){
                if (errorsArgsMap.get("houseOfficerId")==null){
                    errorsArgsMap.put("houseOfficerId", new String[]{"["+ErrorCodeConstant.ERROR_1002.getErrorMsg()+"]"});
                } else {
                    Object[] errorMsg = errorsArgsMap.get("houseOfficerId");
                    arrayExpand(errorMsg,"["+ErrorCodeConstant.ERROR_1002.getErrorMsg()+"]");
                    errorsArgsMap.put("houseOfficerId", errorMsg);
                }
            }
        }
        //TODO:机房对应的用户是否在上报审核中
        //TODO:机房完整性校验
        
        return ajaxValidationResult;
    }

    @Override
    public AjaxValidationResult validateBean(BaseModel baseModel) {
        HouseInformation dto = (HouseInformation)baseModel;
        List<ObjectError> errors = new ArrayList<ObjectError>(0);
        Map<String, Object[]> errorsArgsMap = new LinkedHashMap<String, Object[]>(0);
        ValidateResult result = null;
        /*if (dto.getJyzId()==null){
            errorsArgsMap.put("jyzId",new Object[]{"经营者ID"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+""});
        } else {
            if (dto.getJyzId() <= 0) {
                errorsArgsMap.put("jyzId",new Object[]{"经营者ID只能为正整数"});
            }
            if (!vaildateSuccess(validateJxzId(dto.getJyzId().toString()))){
                errorsArgsMap.put("jyzId",new Object[]{validateJxzId(dto.getJyzId().toString()).getMsg()});
            }
        }*/
        // 新增时经营者ID不"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+"
//        if (ValidateTypeConstant.WEB_ADD.equals(baseModel.getValidateSourceType())){
//            if (dto.getJyzId()==null){
//                errorsArgsMap.put("jyzId",new Object[]{"经营者ID"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+""});
//            } else {
//                if (!vaildateSuccess(validateJxzId(dto.getJyzId().toString()))){
//                    errorsArgsMap.put("jyzId",new Object[]{validateJxzId(dto.getJyzId().toString()).getMsg()});
//                }
//            }
//        } else if (ValidateTypeConstant.EXCEL_IMPORT.equals(baseModel.getValidateSourceType())){
//            // 导入时经营者名称不"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+"
//            if (dto.getProviderName()==null){
//                errorsArgsMap.put("providerName",new Object[]{"经营者名称"+ErrorCodeConstant.ERROR_1009.getErrorMsg()+""});
//            } else {
//                if (!vaildateSuccess(validateProviderName(dto.getProviderName().toString()))){
//                    errorsArgsMap.put("providerName",new Object[]{validateProviderName(dto.getProviderName().toString()).getMsg()});
//                }
//            }
//        }
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.DELETE.getValue()) {
        	if (dto.getHouseId() == null) {
				errorsArgsMap.put("houseId", new Object[] { "机房ID" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
			} else {
				result = validateAuthHouse(dto.getHouseId(), baseModel);
				if (!vaildateSuccess(result)) {
					errorsArgsMap.put("houseId", new Object[] { result.getMsg() });
				}
			}
		}
        if (dto.getOperateType() == HouseConstant.OperationTypeEnum.MODIFY.getValue()
				|| dto.getOperateType() == HouseConstant.OperationTypeEnum.ADD.getValue()) {
        	if (dto.getHouseName() == null) {
    			errorsArgsMap.put("houseName", new Object[] { "机房名称" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseName(dto.getHouseName().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseName", new Object[] { result.getMsg() });
				} else {
					if (!uniqueHouseName(dto)) {
						if (errorsArgsMap.get("houseName") == null) {
							errorsArgsMap.put("houseName", new String[] { "[机房名称" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
						} else {
							Object[] errorMsg = errorsArgsMap.get("houseName");
							arrayExpand(errorMsg, "[机房名称" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]");
							errorsArgsMap.put("houseName", errorMsg);
						}
					}
				}
    		}
    		if (dto.getHouseIdStr() == null) {
    			errorsArgsMap.put("houseIdStr", new Object[] { "机房编号" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseIdStr(dto.getHouseIdStr().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseIdStr", new Object[] { result.getMsg() });
				} else {
					if (!uniqueHouseIdStr(dto)) {
						if (errorsArgsMap.get("houseIdStr") == null) {
							errorsArgsMap.put("houseIdStr", new String[] { "[机房编号" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]" });
						} else {
							Object[] errorMsg = errorsArgsMap.get("houseIdStr");
							arrayExpand(errorMsg, "[机房编号" + ErrorCodeConstant.ERROR_1001.getErrorMsg() + "]");
							errorsArgsMap.put("houseIdStr", errorMsg);
						}
					}
				}
    		}
    		if (dto.getIdentity() == null) {
    			errorsArgsMap.put("identity", new Object[] { "专线标识" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateIdentify(dto.getIdentity().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("identity", new Object[] { result.getMsg() });
    			} else {
    				//校验专线标识信息是否在用户账号携带的专线标识中
    				result = validateAuthIdentity(dto.getIdentity(), baseModel);
    				if (!vaildateSuccess(result)) {
    					errorsArgsMap.put("identity", new Object[] { result.getMsg() });
    				}
    			}
    		}
    		if (dto.getHouseType() == null) {
    			errorsArgsMap.put("houseType", new Object[] { "机房性质" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseType(dto.getHouseType().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseType", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseAddress() == null) {
    			errorsArgsMap.put("houseAddress", new Object[] { "机房地址" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseAddress(dto.getHouseAddress().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseAddress", new Object[] { result.getMsg() });
    			}
    		}

    		if (dto.getHouseCounty() != null) {
    			IdcJcdmXzqydm idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode(dto.getHouseCounty() + "");
    			if (idcJcdmXzqydm != null && !StringUtils.isEmpty(idcJcdmXzqydm.getPostCode())) {
    				dto.setHouseZipCode(idcJcdmXzqydm.getPostCode());
    			}
    			if (dto.getHouseZipCode() == null) {
    				errorsArgsMap.put("houseZipCode", new Object[] { "[机房邮编不在行政区域代码表中]" });
    			} else {
    				result = validateHouseZipCode(dto.getHouseZipCode().toString());
    				if (!vaildateSuccess(result)) {
    					errorsArgsMap.put("houseZipCode", new Object[] { result.getMsg() });
    				}
    			}
    		}

    		if (dto.getHouseProvince() == null) {
    			errorsArgsMap.put("houseProvince", new Object[] { "机房所在省或直辖市" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseProvince(dto.getHouseProvince().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseProvince", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseCity() == null) {
    			errorsArgsMap.put("houseCity", new Object[] { "机房所在市或区(县)" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseCity(dto.getHouseCity().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseCity",
    						new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseCounty() == null) {
    			errorsArgsMap.put("houseCounty", new Object[] { "机房所在县" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseCounty(dto.getHouseCounty().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseCounty", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseOfficerName() == null) {
    			errorsArgsMap.put("houseOfficerName", new Object[] { "网络信息安全责任人姓名" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseOfficerName(dto.getHouseOfficerName().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseOfficerName", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseOfficerIdType() == null) {
    			errorsArgsMap.put("houseOfficerIdType", new Object[] { "网络信息安全责任人证件类型" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseOfficerIdType(dto.getHouseOfficerIdType().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseOfficerIdType", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseOfficerId() == null) {
    			errorsArgsMap.put("houseOfficerId", new Object[] { "网络信息安全责任人证件号码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseOfficerId(dto.getHouseOfficerId().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseOfficerId", new Object[] { result.getMsg() });
    			}
    		}
			if (dto.getHouseOfficerIdType() != null && dto.getHouseOfficerId() != null) {
				if (!BasicValidateUtils.idNumMatchType(dto.getHouseOfficerIdType(), dto.getHouseOfficerId())) {
					if (errorsArgsMap.get("houseOfficerId") == null) {
						errorsArgsMap.put("houseOfficerId", new String[] { "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]" });
					} else {
						Object[] errorMsg = errorsArgsMap.get("houseOfficerId");
						arrayExpand(errorMsg, "[" + ErrorCodeConstant.ERROR_1002.getErrorMsg() + "]");
						errorsArgsMap.put("houseOfficerId", errorMsg);
					}
				}
			}
    		if (dto.getHouseOfficerTelephone() == null) {
    			errorsArgsMap.put("houseOfficerTelephone", new Object[] { "网络信息安全责任人固定电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseOfficerTelephone(dto.getHouseOfficerTelephone().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseOfficerTelephone", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseOfficerMobile() == null) {
    			errorsArgsMap.put("houseOfficerMobile", new Object[] { "网络信息安全责任人移动电话" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseOfficerMobile(dto.getHouseOfficerMobile().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseOfficerMobile", new Object[] { result.getMsg() });
    			}
    		}
    		if (dto.getHouseOfficerEmail() == null) {
    			errorsArgsMap.put("houseOfficerEmail", new Object[] { "网络信息安全责任人Email" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
    		} else {
    			result = validateHouseOfficerEmail(dto.getHouseOfficerEmail().toString());
    			if (!vaildateSuccess(result)) {
    				errorsArgsMap.put("houseOfficerEmail", new Object[] { result.getMsg() });
    			}
    		}
//			if (dto.getAreaCode() == null) {
//				errorsArgsMap.put("areaCode", new Object[] { "地市码" + ErrorCodeConstant.ERROR_1009.getErrorMsg() + "" });
//			} else {
//				if (dto.getAreaCodeValidateType() == 1) {
//					result = validateAreaCode(dto);
//					if (!vaildateSuccess(result)) {
//						errorsArgsMap.put("areaCode", new Object[] { result.getMsg() });
//					}
//				}
//    		}
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

    private ValidateResult validateJxzId(String jxzId){
        String propertyName = "经营者ID";
        return BasicValidateUtils.validateMaxLength(propertyName,jxzId,getFiledLengthByPropertyName(prefix,"jxzId"));
    }

//    private ValidateResult validateProviderName(String providerName){
//        String propertyName = "经营者名称";
//        return BasicValidateUtils.validateMaxLength(propertyName,providerName,getFiledLengthByPropertyName(prefix,"providerName"));
//    }

    private ValidateResult validateHouseName(String houseName){
        String propertyName = "机房名称";
        return  BasicValidateUtils.validateMaxLength(propertyName,houseName,getFiledLengthByPropertyName(prefix,"houseName"));
    }

    private ValidateResult validateHouseIdStr(String houseIdStr){
        String propertyName = "机房编号";
        return BasicValidateUtils.validateMaxLength(propertyName,houseIdStr,getFiledLengthByPropertyName(prefix,"houseIdStr"));
    }

    private ValidateResult validateIdentify(String identify){
        String propertyName = "专线标识";
        String tableName = JCDMRedisConstant.IDC_JCDM_ZXBS;
        return validateInRange(propertyName,tableName+":"+identify,identify.toString());
    }

    private ValidateResult validateHouseType(String houseType){
        String propertyName = "机房性质";
        String tableName = JCDMRedisConstant.IDC_JCDM_JFXZ;
        return validateInRange(propertyName,tableName+":"+houseType,houseType.toString());
    }

    private ValidateResult validateHouseAddress(String houseAddress){
        String propertyName = "机房地址";
        return BasicValidateUtils.validateMaxLength(propertyName,houseAddress,getFiledLengthByPropertyName(prefix,"houseAddress"));
    }

    private ValidateResult validateHouseZipCode(String houseZipCode){
        String propertyName = "机房邮编";
        return BasicValidateUtils.validateZipCode(propertyName,houseZipCode,getFiledLengthByPropertyName(prefix,"houseZipCode"));
    }

    private ValidateResult validateHouseProvince(String houseProvince){
        String propertyName = "机房所在省或直辖市";
        return BasicValidateUtils.validateFixedLength(propertyName,houseProvince,getFiledLengthByPropertyName(prefix,"houseProvince"));
    }

    private ValidateResult validateHouseCity(String houseCity){
        String propertyName = "机房所在市或区(县)";
        return BasicValidateUtils.validateFixedLength(propertyName,houseCity,getFiledLengthByPropertyName(prefix,"houseCity"));
    }

    private ValidateResult validateHouseCounty(String houseCounty){
        String propertyName = "机房所在县";
        return BasicValidateUtils.validateFixedLength(propertyName,houseCounty,getFiledLengthByPropertyName(prefix,"houseCounty"));
    }

    private ValidateResult validateHouseOfficerName(String houseOfficerName){
        String propertyName = "网络信息安全责任人姓名";
        return BasicValidateUtils.validateMaxLength(propertyName,houseOfficerName,getFiledLengthByPropertyName(prefix,"houseOfficerName"));
    }

    private ValidateResult validateHouseOfficerIdType(String houseOfficerIdType){
        String propertyName = "网络信息安全责任人证件类型";
        return validatePersonalCardType(propertyName,houseOfficerIdType);
    }

    private ValidateResult validateHouseOfficerId(String houseOfficerId){
        String propertyName = "网络信息安全责任人证件号码";
        return validateIdCard(propertyName,houseOfficerId,getFiledLengthByPropertyName(prefix,"houseOfficerId"));
    }

    private ValidateResult validateHouseOfficerTelephone(String houseOfficerTelephone){
        String propertyName = "网络信息安全责任人固定电话";
        return validateTelephoneNum(propertyName,houseOfficerTelephone,getFiledLengthByPropertyName(prefix,"houseOfficerTelephone"));
    }

    private ValidateResult validateHouseOfficerMobile(String houseOfficerMobile){
        String propertyName = "网络信息安全责任人移动电话";
        return validateMobileNum(propertyName,houseOfficerMobile,getFiledLengthByPropertyName(prefix,"houseOfficerMobile"));
    }

    private ValidateResult validateHouseOfficerEmail(String houseOfficerEmail){
        String propertyName = "网络信息安全责任人Email";
        return validateEmail(propertyName,houseOfficerEmail,getFiledLengthByPropertyName(prefix,"houseOfficerEmail"));
    }

    private ValidateResult validateAreaCode(BaseModel baseModel){
        String propertyName = "地市码";
        return validateAreaCode(propertyName,baseModel);
    }
    
    public boolean ifExistJyzId(HouseInformation dto) {
        if (dto.getJyzId()==null){
            return false;
        }
        IdcInformation idcInformationDTO = new IdcInformation();
        idcInformationDTO.setJyzId(dto.getJyzId());
        return rptIdcinfoMapper.findByJyzId(idcInformationDTO)!=null?true:false;
    }
    
    public boolean ifRepeatHouseName(HouseInformation dto) {
        if (dto.getHouseName()==null){
            return false;
        }
        return rptHouseMainMapper.findByHouseName(dto)==null?true:false;
    }
    
    public boolean ifRepeatHouseIdStr(HouseInformation dto) {
        if (dto.getHouseIdStr()==null){
            return false;
        }
        return rptHouseMainMapper.findByHouseIdStr(dto)==null?true:false;
    }
}
