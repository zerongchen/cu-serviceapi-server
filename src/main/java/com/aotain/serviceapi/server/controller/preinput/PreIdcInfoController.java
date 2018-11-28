package com.aotain.serviceapi.server.controller.preinput;

import com.aotain.common.utils.redis.ApproveIdUtil;
import com.aotain.common.utils.redis.DataApproveUtil;
import com.aotain.cu.serviceapi.dto.*;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.cu.serviceapi.model.WaitApproveProcess;
import com.aotain.serviceapi.server.constant.DealFlagEnum;
import com.aotain.serviceapi.server.controller.CommonController;
import com.aotain.serviceapi.server.service.CommonUtilService;
import com.aotain.serviceapi.server.service.preinput.IdcInformationService;
import com.aotain.serviceapi.server.service.preinput.PreUserInfoService;
import com.aotain.serviceapi.server.service.preinput.impl.IHouseInformationServiceImpl;
import com.aotain.serviceapi.server.util.DateUtils;
import com.aotain.serviceapi.server.validate.impl.IdcInformationValidatorImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="/pre/idcinfo")
@Api(value="PreIdcInfoController",description="预录入模块-经营者信息")
public class PreIdcInfoController extends CommonController {

    private Logger log = LoggerFactory.getLogger(PreIdcInfoController.class);
    
    public static final String MODULE_NAME = "经营者";

    @Autowired
    private IdcInformationValidatorImpl idcInformationValidator;
    @Autowired
    private IdcInformationService idcInformationService;
    @Autowired
    private IHouseInformationServiceImpl iHouseInformationServiceImpl;
    @Autowired
    private PreUserInfoService preUserInfoService;

    @Autowired
    private CommonUtilService commonUtilService;

    @ApiOperation(value="查询经营者数据", notes="查询经营者数据")
    @RequestMapping(value = "listIdcInfo", method = {  RequestMethod.POST })
    @ResponseBody
    public PageResult<IdcInformation> listIdcInfo() {
        PageResult<IdcInformation> result = new PageResult<IdcInformation>();
        List<IdcInformation> list = idcInformationService.getList();
        result.setRows(list);
        return result;
    }

    @ApiOperation(value="查询经营者数据是否存在", notes="查询经营者数据是否存在")
    @RequestMapping(value = "checkIdcExsist", method = {  RequestMethod.POST })
    @ResponseBody
    public boolean checkIdcExsist() {
        List<IdcInformation> list = idcInformationService.getList();
        return list.size()>0?true:false;
    }

    @ApiOperation(value="新增经营者数据", notes="新增经营者数据")
    @RequestMapping(value="insert", method = {  RequestMethod.POST })
    @ResponseBody
    public ResultDto insert(@RequestBody @ApiParam(required = true, name = "idcInformation", value="经营者实体")IdcInformation idcInformation){
        try {
            return idcInformationService.insertData(idcInformation);

        } catch (Exception e) {
            log.error("idc insert error ",e);
            return getErrorResult();
        }
    }

    @ApiOperation(value="更新经营者数据", notes="更新经营者数据")
    @RequestMapping(value="update", method = {  RequestMethod.POST })
    @ResponseBody
    public ResultDto update(@RequestBody @ApiParam(required = true, name = "idcInformation", value="经营者实体")IdcInformation idcInformation){
        try {
            return idcInformationService.updateData(idcInformation);

        } catch (Exception e) {
            log.error("idc update error ",e);
            return getErrorResult();
        }
    }

    @ApiOperation(value="校验经营者数据", notes="校验经营者数据")
    @RequestMapping(value="validate", method = {  RequestMethod.POST })
    @ResponseBody
    public AjaxValidationResult validate(@RequestBody @ApiParam(required = true, name = "idcInformation", value="经营者实体")IDCInformationDTO idcInformation){
        try {
            return idcInformationValidator.preValidate(idcInformation);
        } catch (Exception e) {
            log.error("idc validate error ",e);
        }
        return null;
    }

    @ApiOperation(value="删除经营者数据", notes="删除经营者数据")
    @RequestMapping(value="delete", method = {  RequestMethod.POST })
    @ResponseBody
    public ResultDto delete(@RequestParam @ApiParam(required = true, name = "jyzId", value= "经营者ID")Integer jyzId){
        try {
            return idcInformationService.deleteData(jyzId);
        } catch (Exception e) {
            log.error("idc delete error ",e);
            return getErrorResult();
        }
    }

    @ApiOperation(value="预审经营者数据", notes="预审经营者数据")
    @RequestMapping(value="preValidate", method = {  RequestMethod.POST })
    @ResponseBody
    public ResultDto preValidate(@RequestParam @ApiParam(required = true, name = "jyzId", value= "经营者ID")Integer jyzId){
        try {
            ResultDto result=idcInformationService.preValidate(jyzId);

            //写redis信息
            Long approveId=ApproveIdUtil.getInstance().getApproveId();
            DataApproveUtil.getInstance().setDataApprove("1"+jyzId,approveId);
            //写流水表信息
            WaitApproveProcess ass=new WaitApproveProcess();
            ass.setApproveId(approveId);
            ass.setType(1);
            ass.setDataId(Long.valueOf(jyzId.toString()));
            if(result.getResultCode()==0){
                ass.setDealFlag(DealFlagEnum.STATUS2.getCode());
            }else{
                ass.setDealFlag(DealFlagEnum.STATUS1.getCode());
            }
            IdcInformation dto = new IdcInformation();
            dto.setJyzId(jyzId);
            dto = idcInformationService.getData(dto);
            ass.setWarnData(dto.getVerificationResult());
            ass.setDealTime(DateUtils.getCurrentyyyMMddHHmmss());
            Date date=new Date();
            ass.setCreateTime(date);
            ass.setUpdateTime(date);
            int i=commonUtilService.insertApproveProcess(ass);
            if(i<=0){
                log.error("idc approve insertApproveProcess error,jyzId="+jyzId);
            }
            return result;
        } catch (Exception e) {
            log.error("idc preValidate error ",e);
        }
        return getErrorResult();
    }

    @ApiOperation(value="撤销预审经营者数据", notes="撤销预审经营者数据")
    @RequestMapping(value="revokeValid", method = {  RequestMethod.POST })
    @ResponseBody
    public ResultDto revokeValid(@RequestParam @ApiParam(required = true, name = "jyzId", value= "经营者ID")Integer jyzId){
        try {
            return idcInformationService.revokeValid(jyzId);
        } catch (Exception e) {
            log.error("idc revokeValid error ",e);
        }
        return getErrorResult();
    }

    @ApiOperation(value="级联预审经营者数据", notes="预审经营者数据")
    @RequestMapping(value="preValidateCascade", method = {  RequestMethod.POST })
    @ResponseBody
    public ResultDto preValidateCascade(@RequestParam @ApiParam(required = true, name = "jyzId", value= "经营者ID")Integer jyzId){
        try {
            //预审经营者
            ResultDto result = idcInformationService.preValidate(jyzId);
            if(result.getResultCode() == ResultDto.ResultCodeEnum.ERROR.getCode()){
                return result;
            }

            //预审机房
            HouseInformationDTO ass=new HouseInformationDTO();
            ass.setJyzId(jyzId);
            ass.setDealFlag(0);
            List<HouseInformationDTO> list=iHouseInformationServiceImpl.getHouseByJyzId(ass);
            if(list !=null && list.size()>0){
                for(HouseInformationDTO bo:list){
                    iHouseInformationServiceImpl.approve(bo.getHouseId().toString());
                }
            }
            List<UserInformation> userList=preUserInfoService.findListByJyz(jyzId);
            if(userList !=null && userList.size()>0){
                for(UserInformation bo:userList){
                    preUserInfoService.approve(bo.getUserId().toString());
                }
            }
            return getSuccessResult();
        } catch (Exception e) {
            log.error("idc preValidateCascade error ",e);
            return getErrorResult("idc preValidateCascade error!");
        }
    }

    @ApiOperation(value="经营者审核结果", notes="经营者审核结果")
    @RequestMapping(value="idcValidateMsg", method = {  RequestMethod.POST })
    @ResponseBody
    public List<ApproveResultDto> idcValidateMsg(@RequestBody @ApiParam(required = true, name = "approveId", value="审核结果ID")String approveId) {
        try {
            return idcInformationService.getApproveResult(approveId);
        } catch (Exception e) {
            log.error("idc idcValidateMsg error ",e);
        }
        return null;
    }
}
