package com.aotain.serviceapi.server.service.report;

import com.aotain.cu.serviceapi.dto.*;
import com.aotain.cu.serviceapi.model.*;
import com.aotain.cu.utils.ResponseConstant;
import com.aotain.serviceapi.server.constant.CommonConstant;
import com.aotain.serviceapi.server.dao.report.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserInfoServiceImpl
 * @Author tanzj
 * @Date 2018/7/10
 **/
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class RptUserInfoServiceImpl implements RptUserInfoService {

	@Autowired
	private RptBaseUserMapper rptBaseUserMapper;

	@Autowired
	private RptBaseServiceDomainMapper rptBaseServiceDomainMapper;

	@Autowired
	private RptUserBandWidthMapper rptUserBandWidthMapper;

	@Autowired
	private RptUserServiceMapper rptUserServiceMapper;

	@Autowired
	private RptUserVirtualMapper rptUserVirtualMapper;

	@Override
	public PageResult<UserInformationDTO> getUserInfoList(UserInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<UserInformationDTO> result = new PageResult<UserInformationDTO>();
		// 两个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes()) || StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty) {
			return result;
		}
		List<UserInformationDTO> info = new ArrayList<UserInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptBaseUserMapper.getDtoList(dto);
			PageInfo<UserInformationDTO> pageResult = new PageInfo<UserInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = rptBaseUserMapper.getDtoList(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public UserInformationDTO getUserInfoById(UserInformationDTO dto) {
		return rptBaseUserMapper.getUserInfoById(dto);
	}

	@Override
	public PageResult<UserServiceInformationDTO> getUserServiceList(UserServiceInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<UserServiceInformationDTO> result = new PageResult<UserServiceInformationDTO>();
		// 两个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes()) || StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty) {
			return result;
		}
		List<UserServiceInformationDTO> info = new ArrayList<UserServiceInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptUserServiceMapper.getDtoList(dto);
			PageInfo<UserServiceInformationDTO> pageResult = new PageInfo<UserServiceInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = rptUserServiceMapper.getDtoList(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public PageResult<UserBandwidthInformationDTO> getUserBandWidthList(UserBandwidthInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<UserBandwidthInformationDTO> result = new PageResult<UserBandwidthInformationDTO>();
		// 三个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes()) || StringUtils.isEmpty(dto.getAuthHouses())
				|| StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty) {
			return result;
		}
		List<UserBandwidthInformationDTO> info = new ArrayList<UserBandwidthInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptUserBandWidthMapper.getDtoList(dto);
			PageInfo<UserBandwidthInformationDTO> pageResult = new PageInfo<UserBandwidthInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = rptUserBandWidthMapper.getDtoList(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public PageResult<UserVirtualInformationDTO> getUserVirtualList(UserVirtualInformationDTO dto) {
		dto.setAreaCodes(StringUtils.join(dto.getCityCodeList(), ","));
		dto.setAuthHouses(StringUtils.join(dto.getUserAuthHouseList(), ","));
		dto.setAuthIdentities(StringUtils.join(dto.getUserAuthIdentityList(), ","));
		PageResult<UserVirtualInformationDTO> result = new PageResult<UserVirtualInformationDTO>();
		// 两个里面任意一个为空返回空
		boolean isEmpty = StringUtils.isEmpty(dto.getAreaCodes()) || StringUtils.isEmpty(dto.getAuthIdentities());
		if (!dto.getAuthFilter() && isEmpty) {
			return result;
		}
		List<UserVirtualInformationDTO> info = new ArrayList<UserVirtualInformationDTO>();
		if (dto.getIsPaging().equals(1)) {
			PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
			info = rptUserVirtualMapper.getDtoList(dto);
			PageInfo<UserVirtualInformationDTO> pageResult = new PageInfo<UserVirtualInformationDTO>(info);
			result.setTotal(pageResult.getTotal());
		} else {
			info = rptUserVirtualMapper.getDtoList(dto);
		}
		result.setRows(info);
		return result;
	}

	@Override
	public ResultDto insert(UserInformation userInformation) throws Exception {
		ResultDto result = new ResultDto();
		int userInforCount = rptBaseUserMapper.selectCountById(userInformation);
		if (userInforCount > 0) {
			throw new Exception("用户已存在");
		} else {
			rptBaseUserMapper.insert(userInformation);
			// 用户服务信息
			if (userInformation.getServiceList() != null && userInformation.getServiceList().size() > 0) {
				List<UserServiceInformation> serviceList = new ArrayList<UserServiceInformation>();
				if (userInformation.getServiceList().size() > 1) {
					for (int i = 0; i < userInformation.getServiceList().size(); i++) {
						for (int j = i + 1; j < userInformation.getServiceList().size(); j++) {
							if (userInformation.getServiceList().get(i).getServiceId()
									.equals(userInformation.getServiceList().get(j).getServiceId())) {
								throw new Exception("用户服务信息已存在");
							}
						}
					}
				}
				for (UserServiceInformation serviceInformation : userInformation.getServiceList()) {
					int serviceUserCount = rptUserServiceMapper.selectCountById(serviceInformation);
					if (serviceUserCount > 0) {
						throw new Exception("用户服务信息已存在");
					} else {
						serviceList.add(serviceInformation);
					}
				}
				rptUserServiceMapper.insertList(serviceList);
				// 用户域名信息
				for (UserServiceInformation tmp : userInformation.getServiceList()) {
					if (tmp.getDomainList() != null && tmp.getDomainList().size() > 0) {
						List<ServiceDomainInformation> domainList = new ArrayList<ServiceDomainInformation>();
						if (tmp.getDomainList().size() > 1) {
							for (int i = 0; i < tmp.getDomainList().size(); i++) {
								for (int j = i + 1; j < tmp.getDomainList().size(); j++) {
									if (tmp.getDomainList().get(i).getDomainId()
											.equals(tmp.getDomainList().get(j).getDomainId())) {
										throw new Exception("用户域名信息已存在");
									}
								}
							}
						}
						for (ServiceDomainInformation temDomain : tmp.getDomainList()) {
							int serviceUserCount = rptBaseServiceDomainMapper.selectCountById(temDomain);
							if (serviceUserCount > 0) {
								throw new Exception("用户域名信息已存在");
							} else {
								domainList.add(temDomain);
							}
						}
						rptBaseServiceDomainMapper.insertList(domainList);
					}
				}
			}
			// 用户带宽信息
			if (userInformation.getBandwidthList() != null && userInformation.getBandwidthList().size() > 0) {
				List<UserBandwidthInformation> bandwidthList = new ArrayList<UserBandwidthInformation>();
				if (userInformation.getBandwidthList().size() > 1) {
					for (int i = 0; i < userInformation.getBandwidthList().size(); i++) {
						for (int j = i + 1; j < userInformation.getBandwidthList().size(); j++) {
							if (userInformation.getBandwidthList().get(i).getHhId()
									.equals(userInformation.getBandwidthList().get(j).getHhId())) {
								throw new Exception("用户带宽信息已存在");
							}
						}
					}
				}

				for (UserBandwidthInformation domainTem : userInformation.getBandwidthList()) {
					int serviceUserCount = rptUserBandWidthMapper.selectCountById(domainTem);
					if (serviceUserCount > 0) {
						throw new Exception("用户带宽信息已存在");
					} else {
						bandwidthList.add(domainTem);
					}
				}
				rptUserBandWidthMapper.insertList(bandwidthList);
			}

			// 用户虚拟主机信息
			if (userInformation.getVirtualList() != null && userInformation.getVirtualList().size() > 0) {
				List<UserVirtualInformation> virtualList = new ArrayList<UserVirtualInformation>();
				if (userInformation.getVirtualList().size() > 1) {
					for (int i = 0; i < userInformation.getVirtualList().size(); i++) {
						for (int j = i + 1; j < userInformation.getVirtualList().size(); j++) {
							if (userInformation.getVirtualList().get(i).getVirtualId()
									.equals(userInformation.getVirtualList().get(j).getVirtualId())) {
								throw new Exception("用户虚拟主机信息已存在");
							}
						}
					}
				}

				for (UserVirtualInformation virtualTem : userInformation.getVirtualList()) {
					int serviceUserCount = rptUserVirtualMapper.selectCountById(virtualTem);
					if (serviceUserCount > 0) {
						throw new Exception("用户虚拟主机信息已存在");
					} else {
						virtualList.add(virtualTem);
					}
				}
				rptUserVirtualMapper.insertList(virtualList);
			}
		}
		result.setResultCode(ResponseConstant.RESPONSE_SUCCESS);
		return result;
	}

	@Override
	public ResultDto modify(UserInformation userInformation) throws Exception {
		ResultDto result = new ResultDto();
		// 上报用户信息更新
		// 机房主体只有用户名称不为空时，才做更新；子节点始终需要更新
		if (StringUtils.isNotBlank(userInformation.getUnitName())) {
			rptBaseUserMapper.updateOrAdd(userInformation);
		}
		// 用户服务信息更新
		if (userInformation.getServiceList() != null && userInformation.getServiceList().size() > 0) {
			for (int i = 0; i < userInformation.getServiceList().size(); i++) {
				for (int j = i + 1; j < userInformation.getServiceList().size(); j++) {
					if (userInformation.getServiceList().get(i).getServiceId()
							.equals(userInformation.getServiceList().get(j).getServiceId())) {
						throw new Exception("用户服务信息录入重复");
					}
				}
			}

			final List<UserServiceInformation> serviceList = new ArrayList<>();
			final List<ServiceDomainInformation> domainList = new ArrayList<>();
			final List<Long> serviceIds = new ArrayList<>();
			for (UserServiceInformation serviceInformation : userInformation.getServiceList()) {
				if (serviceInformation.getDomainList() != null && serviceInformation.getDomainList().size() > 0) {
					for (int i = 0; i < serviceInformation.getDomainList().size(); i++) {
						for (int j = i + 1; j < serviceInformation.getDomainList().size(); j++) {
							if (serviceInformation.getDomainList().get(i).getDomainId()
									.equals(serviceInformation.getDomainList().get(j).getDomainId())) {
								throw new Exception("用户域名信息录入重复");
							}
						}
					}
					final List<Long> domainIdList = new ArrayList<>();
					if (serviceInformation.getOperateType() == 3) {
						// 删除所有域名：domainIdList为null或者size=0时，将删除所有域名
						domainIdList.clear();
					} else if (serviceInformation.getOperateType() == 2) {
						// 删除需要删除的域名
						serviceInformation.getDomainList().stream().forEach(si -> {
							if (si.getOperateType() == 3) {
								domainIdList.add(si.getDomainId());
							}
						});
					} else {
						domainIdList.add(-1L); // 添加一个不存在的ID防止数据被清空
					}
					// 调用删除域名接口
					Map<String, Object> query = new HashMap<>();
					query.put("domainIds", domainIdList);
					query.put("serviceId", serviceInformation.getServiceId());
					rptBaseServiceDomainMapper.deleteByIds(query);
				}
				userInformation.getServiceList().stream().forEach(sl -> {
					if (sl.getOperateType() == 3) { // 需要删除的服务
						serviceIds.add(sl.getServiceId());
						// 清除服务下的域名
						Map<String, Object> query = new HashMap<>();
						query.put("domainIds", Collections.emptyList());
						query.put("serviceId", sl.getServiceId());
						rptBaseServiceDomainMapper.deleteByIds(query);
					} else { // 新增或者修改的服务
						serviceList.add(sl);
						// 看是否需要添加服务
						if (sl.getDomainList() != null) {
							sl.getDomainList().stream().filter(dl -> dl.getOperateType() != 3)
									.forEach(dl -> domainList.add(dl));
						}
					}
				});
			}
			// 调用删除服务接口
			Map<String, Object> query = new HashMap<>();
			query.put("serviceIds", serviceIds);
			query.put("userId", userInformation.getUserId());
			if (serviceIds.size() > 0) { // 有服务时，才调用删除服务接口
				rptUserServiceMapper.deleteByIds(query);
			}
			if (serviceList != null && !serviceList.isEmpty()) { // 新增或者修改服务
				rptUserServiceMapper.updateOrAdd(serviceList);
			}
			if (domainList != null && !domainList.isEmpty()) { // 新增或者修复域名
				rptBaseServiceDomainMapper.updateOrAdd(domainList);
			}
		}

		// 用户带宽信息
		if (userInformation.getBandwidthList() != null && userInformation.getBandwidthList().size() > 0) {
			List<UserBandwidthInformation> bandwidthList = new ArrayList<UserBandwidthInformation>();
			List<Long> bandIds = new ArrayList<>();
			for (int i = 0; i < userInformation.getBandwidthList().size(); i++) {
				for (int j = i + 1; j < userInformation.getBandwidthList().size(); j++) {
					if (userInformation.getBandwidthList().get(i).getHhId()
							.equals(userInformation.getBandwidthList().get(j).getHhId())) {
						throw new Exception("用户带宽信息已存在");
					}
				}
				UserBandwidthInformation ubi = userInformation.getBandwidthList().get(i);
				if (ubi.getOperateType() == 3) { // 删除
					bandIds.add(userInformation.getBandwidthList().get(i).getHhId());
				} else { // 新增或者修改
					bandwidthList.add(userInformation.getBandwidthList().get(i));
				}
			}
			Map<String, Object> query = new HashMap<>();
			query.put("hhIds", bandIds);
			query.put("userId", userInformation.getUserId());
			// 调用删除机架
			if (bandIds != null && bandIds.size() > 0) {
				rptUserBandWidthMapper.deleteByIds(query);
			}
			if (bandwidthList != null && !bandwidthList.isEmpty()) {
				rptUserBandWidthMapper.updateOrAdd(bandwidthList);
			}
		}

		// 用户虚拟主机信息
		if (userInformation.getVirtualList() != null && userInformation.getVirtualList().size() > 0) {
			List<UserVirtualInformation> vitualList = new ArrayList<UserVirtualInformation>();
			List<Long> virtualIds = new ArrayList<>();
			for (int i = 0; i < userInformation.getVirtualList().size(); i++) {
				for (int j = i + 1; j < userInformation.getVirtualList().size(); j++) {
					if (userInformation.getVirtualList().get(i).getVirtualId()
							.equals(userInformation.getVirtualList().get(j).getVirtualId())) {
						throw new Exception("用户虚拟主机信息已存在");
					}
				}
				UserVirtualInformation uvi = userInformation.getVirtualList().get(i);
				if (uvi.getOperateType() == 3) { // 删除
					virtualIds.add(userInformation.getVirtualList().get(i).getVirtualId());
				} else {// 新增或者修改
					vitualList.add(userInformation.getVirtualList().get(i));
				}
			}
			Map<String, Object> query = new HashMap<>();
			query.put("virtualIds", virtualIds);
			query.put("userId", userInformation.getUserId());
			if (virtualIds != null && virtualIds.size() > 0) {
				rptUserVirtualMapper.deleteByIds(query);
			}
			if (vitualList != null && !vitualList.isEmpty()) {
				rptUserVirtualMapper.updateOrAdd(vitualList);
			}
		}
		result.setResultCode(ResponseConstant.RESPONSE_SUCCESS);
		return result;
	}

	@Override
	public ResultDto delete(Long userId) throws Exception {

		ResultDto result = new ResultDto();
		UserInformation userInformation = rptBaseUserMapper.selectByPrimaryKey(userId);
		if (userInformation != null) {
			// 用户服务信息删除
			if (userInformation.getServiceList() != null && userInformation.getServiceList().size() > 0) {
				List<Long> serviceIds = new ArrayList<>();
				for (UserServiceInformation serviceInformation : userInformation.getServiceList()) {
					if (serviceInformation.getDomainList() != null && serviceInformation.getDomainList().size() > 0) {
						rptBaseServiceDomainMapper.deleteByIdList(serviceInformation.getDomainList());
					}
				}
				rptUserServiceMapper.deleteByIdList(userInformation.getServiceList());
			}

			// 用户带宽信息删除
			if (userInformation.getBandwidthList() != null && userInformation.getBandwidthList().size() > 0) {
				rptUserBandWidthMapper.deleteByIdList(userInformation.getBandwidthList());
			}

			// 用户虚拟主机信息删除
			if (userInformation.getVirtualList() != null && userInformation.getVirtualList().size() > 0) {
				rptUserVirtualMapper.deleteByIdList(userInformation.getVirtualList());
			}
			// 上报用户信息删除
			rptBaseUserMapper.deleteByPrimaryKey(userInformation.getUserId());
		}
		result.setResultCode(ResponseConstant.RESPONSE_SUCCESS);
		return result;
	}
}
