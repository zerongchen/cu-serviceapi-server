package com.aotain.serviceapi.server.service.preinput.impl;

import java.util.List;
import java.util.Random;

import com.aotain.cu.serviceapi.dto.ApproveResultDto;
import com.aotain.serviceapi.server.dao.CommonUtilMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.BaseModel;
import com.aotain.cu.serviceapi.model.IdcInformation;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.preinput.IdcInformationMapper;
import com.aotain.serviceapi.server.service.preinput.BaseService;
import com.aotain.serviceapi.server.service.preinput.IdcInformationService;
import com.aotain.serviceapi.server.validate.IdcInformationValidator;

@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class IdcInformationServiceImpl extends BaseService implements IdcInformationService {

    private Logger log = LoggerFactory.getLogger(IdcInformationServiceImpl.class);

    @Autowired
    private IdcInformationMapper idcInformationMapper;

    @Autowired
    private CommonUtilMapper commonMapper;

    public IdcInformationServiceImpl(IdcInformationValidator idcInformationValidator) {
        super(idcInformationValidator);
    }

    @Override
    public IDCInformationDTO findByJyzId(IDCInformationDTO ass) {
        return idcInformationMapper.findByJyzId(ass);
    }

    @Override
    public IdcInformation getData(IdcInformation ass) {
        return idcInformationMapper.getData(ass);
    }

    @Override
    public List<ApproveResultDto> getApproveResult(String approveId) {
        ApproveResultDto dto = new ApproveResultDto();
        dto.setApproveId(Long.valueOf(approveId));
        return commonMapper.getApproveResult(dto);
    }

    @Override
    public List<IdcInformation> getList() {
        List<IdcInformation> ass = idcInformationMapper.getList(null);
        return ass;
    }

    @Override
    public ResultDto insertData(IdcInformation ass) {
        ass.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
        //校验输入参数
        ResultDto result = validateResult(ass);
        if(!result.getAjaxValidationResult().validateIsSuccess()){
            log.info("idc information insert validate failed!");
            return result;
        }

        //判断经营者是否已经存在
        List<IdcInformation> idcList = getList();
        if(idcList != null && idcList.size() > 0){
            log.info("idc information has existed!");
            return getErrorResult("["+ass.getIdcName()+"]:"+"idc information has existed!");
        }

        //插入数据库
        ass.setJyzId(new Random().nextInt(100)+1);
        ass.setCzlx(HouseConstant.CzlxEnum.ADD.getValue()); //新增
        ass.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue()); //未预审
        int i = idcInformationMapper.insertData(ass);
        if(i <= 0){//经营者新增失败
            log.info("idc information insert to database failed!");
            return getErrorResult("["+ass.getIdcName()+"]:"+"idc information insert to database error!");
        }

        return getSuccessResult(ass.getJyzId());
    }


    @Override
    public ResultDto updateData(IdcInformation ass) {
        ass.setOperateType(HouseConstant.OperationTypeEnum.MODIFY.getValue());
        //校验输入参数
        ResultDto result = validateResult(ass);
        if(!result.getAjaxValidationResult().validateIsSuccess()){
            log.info("idc information update validate failed!");
            return result;
        }
        //获取原信息
        IdcInformation domain=new IdcInformation();
        domain.setJyzId(ass.getJyzId());
        domain = idcInformationMapper.getData(domain);
        if(domain == null){
            log.info("idc information does not exist!");
            return getErrorResult("["+ass.getIdcName()+"]:"+"idc information does not exist!");
        }

        //判断经营者是否已经上报， 如果已上报则为变更，否则是新增
        if((domain.getCzlx()==HouseConstant.CzlxEnum.ADD.getValue() && domain.getDealFlag()==HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue()) || domain.getCzlx()==HouseConstant.CzlxEnum.MODIFY.getValue()){
            ass.setCzlx(HouseConstant.CzlxEnum.MODIFY.getValue());
        }else{
            ass.setCzlx(HouseConstant.CzlxEnum.ADD.getValue());
        }
        ass.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
        ass.setVerificationResult("");
        int i = idcInformationMapper.updateData(ass);
        if(i <= 0){//经营者修改失败
            log.info("idc information update to database failed!");
            return getErrorResult("["+ass.getIdcName()+"]:"+"idc information update to database error!");
        }

        return getSuccessResult();
    }

	@Override
	public ResultDto deleteData(Integer jyzId) {
		// 获取原信息
		IdcInformation ass = new IdcInformation();
		ass.setJyzId(jyzId);
		ass = idcInformationMapper.getData(ass);
		if (ass != null) {
			if ((ass.getCzlx() == HouseConstant.CzlxEnum.ADD.getValue() && ass.getDealFlag() == HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue())
					|| ass.getCzlx() == HouseConstant.CzlxEnum.MODIFY.getValue()) {
				// 经营者已上报： 操作类型改成删除, 状态改成上报审核中
				IdcInformation domain = new IdcInformation();
				domain.setJyzId(jyzId);
				domain.setCzlx(HouseConstant.CzlxEnum.DELETE.getValue());
				domain.setDealFlag(HouseConstant.DealFlagEnum.CHECKING.getValue());
				domain.setVerificationResult("");
				int i = idcInformationMapper.updateData(domain);

				if (i <= 0) {// 经营者修改失败
					log.info("idc information update czlx to database failed!");
					return getErrorResult("[" + ass.getIdcName() + "]:" + "idc information update czlx to database error!");
				}
				return getSuccessResult();
			} else {
				if (ass.getCzlx() == HouseConstant.CzlxEnum.ADD.getValue()
						&& (ass.getDealFlag() != HouseConstant.DealFlagEnum.REPORTING.getValue()
								|| ass.getDealFlag() != HouseConstant.DealFlagEnum.CHECKING.getValue())) {
					// 物理删除经营者
					int i = idcInformationMapper.deleteData(ass);
					if (i <= 0) {// 经营者删除失败
						log.info("idc information delete to database failed!");
						return getErrorResult("[" + ass.getIdcName() + "]:" + "idc information delete to database error!");
					}
				}
				return getSuccessResult();
			}
		}
		return getSuccessResult();
	}

    @Override
    public ResultDto revokeValid(Integer jyzId) {
        IdcInformation ass=new IdcInformation();
        ass.setJyzId(jyzId);
        ass=getData(ass);
        if(ass.getDealFlag() == HouseConstant.DealFlagEnum.CHECKING.getValue()){
            IdcInformation domain=new IdcInformation();
            domain.setJyzId(jyzId);
            domain.setDealFlag(HouseConstant.DealFlagEnum.NOT_CHECK.getValue());
            int i = idcInformationMapper.updateData(domain);
            if(i <= 0){
                log.info("idc information revokeValid failed!");
                return getErrorResult("["+jyzId+"]:"+"idc information revokeValid error!");
            }

            return getSuccessResult();
        }

        return getErrorResult("["+jyzId+"]:"+"idc information revokeValid failed!");
    }

    @Override
    public ResultDto preValidate(Integer jyzId) {
        //ResultDto resultDto = new ResultDto();
        IdcInformation ass=new IdcInformation();
        ass.setJyzId(jyzId);
        ass=getData(ass);
        if(ass.getDealFlag() == HouseConstant.DealFlagEnum.CHECKING.getValue() || ass.getDealFlag() == HouseConstant.DealFlagEnum.REPORTING.getValue()){
            return getSuccessResult();
        }

        IdcInformation domain=new IdcInformation();
        domain.setJyzId(jyzId);

        if(ass.getDealFlag() == HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue()){
            domain.setCzlx(HouseConstant.CzlxEnum.MODIFY.getValue());
        }
        domain.setDealFlag(HouseConstant.DealFlagEnum.CHECKING.getValue());
        domain.setVerificationResult("");

        //更新预审状态
        int i = idcInformationMapper.updateData(domain);
        if(i <= 0){//经营者修改失败
            log.info("idc information update dealflag to database failed!");
            return getErrorResult("["+ass.getIdcName()+"]:"+"idc information update dealflag to database error!");
        }

        return getSuccessResult();
    }

    @Override
    protected int insert(BaseModel baseModel) {
        return 0;
    }

    @Override
    protected int update(BaseModel baseModel) {
        return 0;
    }

    @Override
    protected int delete(BaseModel baseModel) {
        return 0;
    }
}
