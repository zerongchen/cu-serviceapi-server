package com.aotain.serviceapi.server.service.report;

import java.util.ArrayList;
import java.util.List;

import com.aotain.serviceapi.server.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.HouseFrameInformation;
import com.aotain.cu.serviceapi.model.HouseGatewayInformation;
import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.HouseUserFrameInformation;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.utils.ResponseConstant;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseFrameMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseGatewayMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseIpsegMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseMapper;
import com.aotain.serviceapi.server.dao.report.RptIsmsBaseHouseUserframeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class RptHouseServiceImpl implements RptHouseService{

	private Logger log = LoggerFactory.getLogger(RptHouseServiceImpl.class);

	@Autowired
	private RptIsmsBaseHouseMapper rptIsmsBaseHouseMapper;
	@Autowired
	private RptIsmsBaseHouseIpsegMapper rptIsmsBaseHouseIpsegMapper;
	@Autowired
	private RptIsmsBaseHouseFrameMapper rptIsmsBaseHouseFrameMapper;
	@Autowired
	private RptIsmsBaseHouseGatewayMapper rptIsmsBaseHouseGatewayMapper;
	@Autowired
	private RptIsmsBaseHouseUserframeMapper rptIsmsBaseHouseUserframeMapper;


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
		if(dto.getIsPaging().equals(1)){
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptIsmsBaseHouseMapper.getDtoList(dto);
			PageInfo<HouseInformationDTO> pageResult = new PageInfo<HouseInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		}else{
			info = rptIsmsBaseHouseMapper.getDtoList(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public HouseInformationDTO getHouseInfoById(HouseInformationDTO dto) {
		return rptIsmsBaseHouseMapper.getHouseInfoById(dto);
	}

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
		if(dto.getIsPaging().equals(1)){
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptIsmsBaseHouseFrameMapper.getDtoList(dto);
			PageInfo<HouseFrameInformationDTO> pageResult = new PageInfo<HouseFrameInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		}else{
			info = rptIsmsBaseHouseFrameMapper.getDtoList(dto);
		}
		result.setRows(info);
		return result;
	}

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
		if(dto.getIsPaging().equals(1)){
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptIsmsBaseHouseGatewayMapper.getDtoList(dto);
			PageInfo<HouseGatewayInformationDTO> pageResult = new PageInfo<HouseGatewayInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		}else{
			info = rptIsmsBaseHouseGatewayMapper.getDtoList(dto);
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
		if(dto.getIsPaging().equals(1)){
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptIsmsBaseHouseIpsegMapper.getDtoList(dto);
			PageInfo<HouseIPSegmentInforDTO> pageResult = new PageInfo<HouseIPSegmentInforDTO>(info);
			result.setTotal(pageResult.getTotal());
		}else{
			info = rptIsmsBaseHouseIpsegMapper.getDtoList(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public ResultDto update(HouseInformation ass) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResponseConstant.RESPONSE_ERROR); //0成功  1失败
		
		int i=1;
		// 机房主体只有机房名不为空时，才做更新；子节点始终需要更新
		if(StringUtils.isNotBlank(ass.getHouseName())){
			i = rptIsmsBaseHouseMapper.updateData(ass);
		}
		if(i>0){
			for(HouseIPSegmentInformation a : ass.getIpSegList()){
				if(a.getOperateType().intValue()==1){
					rptIsmsBaseHouseIpsegMapper.insertData(a);
				}else if(a.getOperateType().intValue()==2){
					rptIsmsBaseHouseIpsegMapper.updateData(a);
				}else if(a.getOperateType().intValue()==3){
					rptIsmsBaseHouseIpsegMapper.deleteData(a);
				}else{
					throw new RuntimeException("IP地址段变更操作类型有误");
				}
			}
			for(HouseFrameInformation a : ass.getFrameList()){
				if(a.getOperateType().intValue()==1){
					rptIsmsBaseHouseFrameMapper.insertData(a);
					if (a.getUserFrameList() != null && a.getUserFrameList().size() > 0) {
						for(HouseUserFrameInformation b : a.getUserFrameList()){
							rptIsmsBaseHouseUserframeMapper.insertData(b);
						}
					}
				}else if(a.getOperateType().intValue()==2){
					rptIsmsBaseHouseFrameMapper.updateData(a);
					
					// 用户带宽关系表每次都为全量数据，先删除后新增
					// 1. 删除机架下所有的关联信息
					HouseUserFrameInformation hufi = new HouseUserFrameInformation();
					hufi.setHouseId(a.getHouseId());
					hufi.setFrameId(a.getFrameId());
					rptIsmsBaseHouseUserframeMapper.deleteData(hufi);
					// 2. 再重新新增
					if (a.getUserFrameList() != null && a.getUserFrameList().size() > 0) {
						for(HouseUserFrameInformation b : a.getUserFrameList()){
							rptIsmsBaseHouseUserframeMapper.insertData(b);
						}
					}
				}else if(a.getOperateType().intValue()==3){
					rptIsmsBaseHouseFrameMapper.deleteData(a);
					// 直接删除机架下所有的关联信息（原来是遍历关系表一个个删除）
					HouseUserFrameInformation hufi = new HouseUserFrameInformation();
					hufi.setHouseId(a.getHouseId());
					hufi.setFrameId(a.getFrameId());
					rptIsmsBaseHouseUserframeMapper.deleteData(hufi);
				}else{
					throw new RuntimeException("机架变更操作类型有误");
				}
			}
			for(HouseGatewayInformation a : ass.getGatewayInfoList()){
				if(a.getOperateType().intValue()==1){
					rptIsmsBaseHouseGatewayMapper.insertData(a);
				}else if(a.getOperateType().intValue()==2){
					rptIsmsBaseHouseGatewayMapper.updateData(a);
				}else if(a.getOperateType().intValue()==3){
					rptIsmsBaseHouseGatewayMapper.deleteData(a);
				}else{
					throw new RuntimeException("链路变更操作类型有误");
				}
			}
			result.setResultCode(ResponseConstant.RESPONSE_SUCCESS);
		}
		return result;
	}

	@Override
	public ResultDto delete(Integer houseId) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResponseConstant.RESPONSE_ERROR); //0成功  1失败
		rptIsmsBaseHouseIpsegMapper.deleteDataByFK(houseId);
		rptIsmsBaseHouseUserframeMapper.deleteDataByFK(houseId);
		rptIsmsBaseHouseFrameMapper.deleteDataByFK(houseId);
		rptIsmsBaseHouseGatewayMapper.deleteDataByFK(houseId);

		HouseInformation ass=new HouseInformation();
		ass.setHouseId(houseId.longValue());
		rptIsmsBaseHouseMapper.deleteData(ass);
		result.setResultCode(ResponseConstant.RESPONSE_SUCCESS);
		return result;
	}

	@Override
	public ResultDto insert(HouseInformation ass) {
		ResultDto result = new ResultDto();
		result.setResultCode(ResponseConstant.RESPONSE_ERROR); // 0成功 1失败
		boolean checkFlag = checkHouseExsist(ass, result);
		if (checkFlag) {
			int i = rptIsmsBaseHouseMapper.insertData(ass);
			if (i > 0) {
				if (ass.getIpSegList() != null && ass.getIpSegList().size() > 0) {
					for (HouseIPSegmentInformation a : ass.getIpSegList()) {
						rptIsmsBaseHouseIpsegMapper.insertData(a);
					}
				}
				if (ass.getFrameList() != null && ass.getFrameList().size() > 0) {
					for (HouseFrameInformation a : ass.getFrameList()) {
						rptIsmsBaseHouseFrameMapper.insertData(a);
						if (a.getUserFrameList() != null && a.getUserFrameList().size() > 0) {
							for (HouseUserFrameInformation b : a.getUserFrameList()) {
								rptIsmsBaseHouseUserframeMapper.insertData(b);
							}
						}
					}
				}
				if (ass.getGatewayInfoList() != null && ass.getGatewayInfoList().size() > 0) {
					for (HouseGatewayInformation a : ass.getGatewayInfoList()) {
						rptIsmsBaseHouseGatewayMapper.insertData(a);
					}
				}
				result.setResultCode(ResponseConstant.RESPONSE_SUCCESS);
			}
		}
		return result;
	}

	public boolean checkHouseExsist(HouseInformation ass,ResultDto result){
		int count=rptIsmsBaseHouseMapper.getCount(ass);
		if (count>0){
			result.setStatusCode(2001);//机房已存在
			return false;
		}else{
			if (ass.getIpSegList()!=null && ass.getIpSegList().size()>0){
				for (int i = 0; i < ass.getIpSegList().size(); i++) {
					int ip=rptIsmsBaseHouseIpsegMapper.getCount(ass.getIpSegList().get(i));
					if(ip>0){
						result.setStatusCode(2002);//IP地址段已存在
						return false;
					}
				}
			}
			if (ass.getFrameList()!=null && ass.getFrameList().size()>0){
				for (int i = 0; i < ass.getFrameList().size(); i++) {
					int ip=rptIsmsBaseHouseFrameMapper.getCount(ass.getFrameList().get(i));
					if(ip>0){
						result.setStatusCode(2003);//机架已存在
						return false;
					}
					HouseFrameInformation houseFrameInfor = ass.getFrameList().get(i);
					if(houseFrameInfor != null && houseFrameInfor.getUserFrameList() != null){
						for (HouseUserFrameInformation a : houseFrameInfor.getUserFrameList()) {
							int huf=rptIsmsBaseHouseUserframeMapper.getCount(a);
							if(huf>0){
								result.setStatusCode(2004);//机架用户已存在
								return false;
							}
						}
					}
				}
			}
			if (ass.getGatewayInfoList()!=null && ass.getGatewayInfoList().size()>0){
				for (int i = 0; i < ass.getGatewayInfoList().size(); i++) {
					int ip=rptIsmsBaseHouseGatewayMapper.getCount(ass.getGatewayInfoList().get(i));
					if(ip>0){
						result.setStatusCode(2005);//链路已存在
						return false;
					}
				}
			}
			return true;
		}
	}
}
