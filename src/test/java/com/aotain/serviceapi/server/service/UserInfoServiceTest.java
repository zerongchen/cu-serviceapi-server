package com.aotain.serviceapi.server.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.dto.UserVirtualInformationDTO;
import com.aotain.cu.serviceapi.model.PageResult;
import com.aotain.cu.serviceapi.model.ServiceDomainInformation;
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import com.aotain.cu.serviceapi.model.UserServiceInformation;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;
import com.aotain.serviceapi.server.ServiceapiServerApplication;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.service.preinput.PreUserBandWidthService;
import com.aotain.serviceapi.server.service.preinput.impl.PreUserInfoServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.PreUserServerServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.PreUserVirtualServiceImpl;
import com.aotain.serviceapi.server.validate.IUserPrincipalValidator;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/08/09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)
public class UserInfoServiceTest {

    @Autowired
    private PreUserInfoServiceImpl preUserInfoService;
    
    @Autowired
    private PreUserServerServiceImpl preUserServerServiceImpl;
    
    @Autowired
    private PreUserVirtualServiceImpl preUserVirtualServiceImpl;
    
    @Autowired
    private PreUserBandWidthService preUserBandWidthServiceImpl;
    
    @Autowired
	@Qualifier(value="userPrincipalValidatorImpl")
	private IUserPrincipalValidator userPrincipalValidator;
    
    @Test
   	public void testUserQuery() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	UserInformationDTO dto = new UserInformationDTO();
    	dto.setCityCodeList(cityCodeList);
    	dto.setUserAuthHouseList(authHouseList);
    	dto.setUserAuthIdentityList(authIdentityList);
    	PageResult<UserInformationDTO> pageResult = preUserInfoService.listUserData(dto);
    	System.out.println(JSON.toJSONString(pageResult));
    }
    
    @Test
   	public void testUserDelete() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	UserInformationDTO dto1 = new UserInformationDTO();
    	dto1.setUserId(30332L);
    	
    	UserInformationDTO dto2 = new UserInformationDTO();
    	dto2.setUserId(30342L);
    	
    	List<UserInformationDTO> dtos = new ArrayList<UserInformationDTO>();
    	dtos.add(dto1);
    	dtos.add(dto2);
    	
    	ResultDto resultDto = preUserInfoService.batchDeleteUserInfo(dtos);
    	System.out.println(JSON.toJSONString(resultDto));
    }
    
    @Test
   	public void testUserValidate() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	UserInformationDTO u1 = new UserInformationDTO();
    	u1.setCityCodeList(cityCodeList);
    	u1.setUserAuthHouseList(authHouseList);
    	u1.setUserAuthIdentityList(authIdentityList);
    	u1.setUnitName("上报测试用户1");
    	u1.setNature(1);
    	u1.setUnitNature(4);
    	u1.setIdentify("1,5");
    	u1.setIdType(2);
    	u1.setIdNumber("441522199912022413");
    	u1.setUnitAddressProvinceCode("440000");
    	u1.setUnitAddressCityCode("440300");
    	u1.setUnitAddressAreaCode("440303");
    	u1.setUnitAddress("科技中二路");
    	u1.setRegisteTime("2018-08-30");
    	u1.setAreaCode("440300");
    	u1.setOfficerName("张三");
    	u1.setOfficerIdType(2);
    	u1.setOfficerId("420821198709136117");
    	u1.setOfficerTelphone("0755-87686578");
    	u1.setOfficerMobile("18729319879");
    	u1.setOfficerEmail("18729319879@189.cn");
    	u1.setCreateUserId(869);
    	
    	u1.setOperateType(HouseConstant.OperationTypeEnum.ADD.getValue());
    	userPrincipalValidator.preValidate(u1);
    	
    }
    
    @Test
   	public void testUserInsert() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	UserInformationDTO u1 = new UserInformationDTO();
    	u1.setCityCodeList(cityCodeList);
    	u1.setUserAuthHouseList(authHouseList);
    	u1.setUserAuthIdentityList(authIdentityList);
    	u1.setUnitName("测试单位3");
    	u1.setNature(1);
    	u1.setUnitNature(4);
    	u1.setIdentify("1,5");
    	u1.setIdType(2);
    	u1.setIdNumber("420821198709136117");
    	u1.setUnitAddressProvinceCode("440000");
    	u1.setUnitAddressCityCode("440300");
    	u1.setUnitAddressAreaCode("440303");
    	u1.setUnitAddress("科技中二路");
    	u1.setRegisteTime("2018-08-30");
    	u1.setAreaCode("440300");
    	u1.setOfficerName("张三");
    	u1.setOfficerIdType(2);
    	u1.setOfficerId("420821198709136117");
    	u1.setOfficerTelphone("0755-87686578");
    	u1.setOfficerMobile("18729319879");
    	u1.setOfficerEmail("18729319879@189.cn");
    	u1.setCreateUserId(869);
    	
    	List<UserServiceInformation> serviceList = new ArrayList<UserServiceInformation>();
    	UserServiceInformation s1 = new UserServiceInformation();
    	s1.setCityCodeList(cityCodeList);
    	s1.setUserAuthHouseList(authHouseList);
    	s1.setUserAuthIdentityList(authIdentityList);
    	s1.setIdentify("1,5");
    	s1.setServiceType(1);
    	s1.setServiceContent("2,3,4");
    	s1.setSetmode(1);//主机托管
    	s1.setBusiness(1);
    	s1.setAreaCode("440300");
    	s1.setCreateUserId(869);
    	serviceList.add(s1);
    	u1.setServiceList(serviceList);
    	
    	List<UserBandwidthInformation> bandwidthList = new ArrayList<UserBandwidthInformation>();
		UserBandwidthInformation b1 = new UserBandwidthInformation();
		b1.setAreaCode("440300");
		b1.setCityCodeList(cityCodeList);
    	b1.setUserAuthHouseList(authHouseList);
    	b1.setUserAuthIdentityList(authIdentityList);
		b1.setHouseId(4307L);
		b1.setDistributeTime("2018-08-19");
		b1.setBandWidth(13L);
		b1.setCreateUserId(869);
		bandwidthList.add(b1);
    	u1.setBandwidthList(bandwidthList);
    	
    	UserInformationDTO u2 = new UserInformationDTO();
    	u2.setCityCodeList(cityCodeList);
    	u2.setUserAuthHouseList(authHouseList);
    	u2.setUserAuthIdentityList(authIdentityList);
    	u2.setCityCodeList(cityCodeList);
    	u2.setUserAuthHouseList(authHouseList);
    	u2.setUserAuthIdentityList(authIdentityList);
    	u2.setUnitName("测试单位4");
    	u2.setNature(1);
    	u2.setUnitNature(4);
    	u2.setIdentify("1,5");
    	u2.setIdType(2);
    	u2.setIdNumber("420821198709136117");
    	u2.setUnitAddressProvinceCode("440000");
    	u2.setUnitAddressCityCode("440300");
    	u2.setUnitAddressAreaCode("440304");
    	u2.setUnitAddress("科技中二路");
    	u2.setRegisteTime("2018-08-30");
    	u2.setAreaCode("440300");
    	u2.setOfficerName("张三");
    	u2.setOfficerIdType(2);
    	u2.setOfficerId("420821198709136117");
    	u2.setOfficerTelphone("0755-87686578");
    	u2.setOfficerMobile("18729319879");
    	u2.setOfficerEmail("18729319879@189.cn");
    	u2.setCreateUserId(869);
    	
    	List<UserServiceInformation> serviceList2 = new ArrayList<UserServiceInformation>();
    	UserServiceInformation s2 = new UserServiceInformation();
    	s2.setCityCodeList(cityCodeList);
    	s2.setUserAuthHouseList(authHouseList);
    	s2.setUserAuthIdentityList(authIdentityList);
    	s2.setIdentify("1,5");
    	s2.setServiceType(1);
    	s2.setServiceContent("2,3,4");
    	s2.setSetmode(2);//主机托管
    	s2.setBusiness(1);
    	s2.setAreaCode("440300");
    	s2.setCreateUserId(869);
    	serviceList2.add(s2);
    	u2.setServiceList(serviceList2);
    	
    	List<UserBandwidthInformation> bandwidthList2 = new ArrayList<UserBandwidthInformation>();
		UserBandwidthInformation b2 = new UserBandwidthInformation();
		b2.setAreaCode("440300");
		b2.setCityCodeList(cityCodeList);
    	b2.setUserAuthHouseList(authHouseList);
    	b2.setUserAuthIdentityList(authIdentityList);
		b2.setHouseId(4307L);
		b2.setDistributeTime("2018-08-19");
		b2.setBandWidth(13L);
		b2.setCreateUserId(869);
		bandwidthList2.add(b2);
    	u2.setBandwidthList(bandwidthList2);
    	
    	List<UserVirtualInformation> virtuals = new ArrayList<UserVirtualInformation>();	
	    UserVirtualInformation v1 = new UserVirtualInformation();
    	v1.setCityCodeList(cityCodeList);
    	v1.setUserAuthHouseList(authHouseList);
    	v1.setUserAuthIdentityList(authIdentityList);
    	v1.setAreaCode("440300");
    	v1.setHouseId(4307L);
    	v1.setName("虚拟主机测试1");
    	v1.setType(1);
    	v1.setNetworkAddress("10.10.10.10");
    	v1.setMgnAddress("10.10.10.10");
    	v1.setVirtualNo("虚拟主机测试1");
    	v1.setCreateUserId(869);
    	virtuals.add(v1);
    	u2.setVirtualList(virtuals);
    	
    	List<UserInformationDTO> dtos = new ArrayList<UserInformationDTO>();
    	dtos.add(u1);
    	dtos.add(u2);
    	
    	for (UserInformationDTO user : dtos) {
        	ResultDto resultDto = preUserInfoService.insertData(user);
        	System.out.println(JSON.toJSONString(resultDto));
		}
    	
    }
    
    @Test
   	public void testUserVirtualQuery() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
    	UserVirtualInformationDTO dto = new UserVirtualInformationDTO();
    	dto.setCityCodeList(cityCodeList);
    	dto.setUserAuthHouseList(authHouseList);
    	dto.setUserAuthIdentityList(authIdentityList);
    	PageResult<UserVirtualInformationDTO> pageResult = preUserVirtualServiceImpl.getUserVirtualList(dto);
    	System.out.println(JSON.toJSONString(pageResult));
    }
    
    @Test
   	public void testUserVirtualInert() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
	    List<UserVirtualInformation> virtuals = new ArrayList<UserVirtualInformation>();	
    	
	    UserVirtualInformation v1 = new UserVirtualInformation();
    	v1.setCityCodeList(cityCodeList);
    	v1.setUserAuthHouseList(authHouseList);
    	v1.setUserAuthIdentityList(authIdentityList);
    	v1.setAreaCode("440300");
    	v1.setUserId(30332L);
    	v1.setHouseId(4307L);
    	v1.setName("虚拟主机测试1");
    	v1.setType(1);
    	v1.setNetworkAddress("10.10.10.10");
    	v1.setMgnAddress("10.10.10.10");
    	v1.setVirtualNo("虚拟主机测试1");
    	v1.setCreateUserId(869);
    	
    	UserVirtualInformation v2 = new UserVirtualInformation();
    	v2.setCityCodeList(cityCodeList);
    	v2.setUserAuthHouseList(authHouseList);
    	v2.setUserAuthIdentityList(authIdentityList);
    	v2.setAreaCode("440300");
    	v2.setUserId(30332L);
    	v2.setHouseId(4308L);
    	v2.setName("虚拟主机测试2");
    	v2.setType(2);
    	v2.setNetworkAddress("10.10.10.10");
    	v2.setMgnAddress("10.10.10.10");
    	v2.setVirtualNo("虚拟主机测试2");
    	v2.setCreateUserId(869);
    	
    	virtuals.add(v1);
    	virtuals.add(v2);
    	ResultDto resultDto = preUserVirtualServiceImpl.batchInsertDatas(virtuals, null, false);
     	System.out.println(JSON.toJSONString(resultDto));
    }
    
    @Test
   	public void testUserVirtualUpdate() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
	    List<UserVirtualInformation> virtuals = new ArrayList<UserVirtualInformation>();	
    	
	    UserVirtualInformation v1 = new UserVirtualInformation();
    	v1.setCityCodeList(cityCodeList);
    	v1.setUserAuthHouseList(authHouseList);
    	v1.setUserAuthIdentityList(authIdentityList);
    	v1.setAreaCode("440300");
    	v1.setVirtualId(1155L);
    	v1.setUserId(30332L);
    	v1.setHouseId(4307L);
    	v1.setName("虚拟主机测试3");
    	v1.setType(1);
    	v1.setNetworkAddress("10.10.10.100");
    	v1.setMgnAddress("10.10.10.100");
    	v1.setVirtualNo("虚拟主机测试1");
    	v1.setUpdateUserId(869);
    	
    	UserVirtualInformation v2 = new UserVirtualInformation();
    	v2.setCityCodeList(cityCodeList);
    	v2.setUserAuthHouseList(authHouseList);
    	v2.setUserAuthIdentityList(authIdentityList);
    	v2.setAreaCode("440300");
    	v2.setVirtualId(1156L);
    	v2.setUserId(30332L);
    	v2.setHouseId(4308L);
    	v2.setName("虚拟主机测试4");
    	v2.setType(2);
    	v2.setNetworkAddress("10.10.10.200");
    	v2.setMgnAddress("10.10.10.200");
    	v2.setVirtualNo("虚拟主机测试2");
    	v2.setUpdateUserId(869);
    	
    	virtuals.add(v1);
    	virtuals.add(v2);
    	ResultDto resultDto = preUserVirtualServiceImpl.updateData(virtuals);
     	System.out.println(JSON.toJSONString(resultDto));
    }
    
    @Test
   	public void testUserVirtualDelete() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
	    List<UserVirtualInformation> virtuals = new ArrayList<UserVirtualInformation>();	
    	
	    UserVirtualInformation v1 = new UserVirtualInformation();
    	v1.setCityCodeList(cityCodeList);
    	v1.setUserAuthHouseList(authHouseList);
    	v1.setUserAuthIdentityList(authIdentityList);
    	v1.setAreaCode("440300");
    	v1.setVirtualId(1155L);
    	
    	UserVirtualInformation v2 = new UserVirtualInformation();
    	v2.setCityCodeList(cityCodeList);
    	v2.setUserAuthHouseList(authHouseList);
    	v2.setUserAuthIdentityList(authIdentityList);
    	v2.setAreaCode("440300");
    	v2.setVirtualId(1156L);
    	
    	virtuals.add(v1);
    	virtuals.add(v2);
    	ResultDto resultDto = preUserVirtualServiceImpl.batchDeleteDatas(virtuals);
     	System.out.println(JSON.toJSONString(resultDto));
    }
    
    @Test
   	public void testUserServiceQuery() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	UserServiceInformationDTO dto = new UserServiceInformationDTO();
    	dto.setCityCodeList(cityCodeList);
    	dto.setUserAuthHouseList(authHouseList);
    	dto.setUserAuthIdentityList(authIdentityList);
    	PageResult<UserServiceInformationDTO> pageResult = preUserServerServiceImpl.getServerInfoList(dto);
    	System.out.println(JSON.toJSONString(pageResult));
    }
    
    @Test
   	public void testUserServiceInsert() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
    	List<UserServiceInformationDTO> services = new ArrayList<UserServiceInformationDTO>();
    	UserServiceInformationDTO s1 = new UserServiceInformationDTO();
    	s1.setCityCodeList(cityCodeList);
    	s1.setUserAuthHouseList(authHouseList);
    	s1.setUserAuthIdentityList(authIdentityList);
    	s1.setUserId(30346L);
    	s1.setServiceType(1);
    	s1.setServiceContent("2,3,4");
    	s1.setSetmode(2);//主机托管
    	s1.setBusiness(1);
    	//s1.setRegType(2);
    	//s1.setRegisterId("粤ICP备1000号");
    	s1.setAreaCode("440300");
    	s1.setCreateUserId(869);
    	
    	UserServiceInformationDTO s2 = new UserServiceInformationDTO();
    	s2.setCityCodeList(cityCodeList);
    	s2.setUserAuthHouseList(authHouseList);
    	s2.setUserAuthIdentityList(authIdentityList);
    	s2.setUserId(30346L);
    	s2.setServiceType(1);
    	s2.setServiceContent("2,3,4");
    	s2.setSetmode(2);//主机托管
    	s2.setBusiness(1);
    	s2.setRegType(2);
    	s2.setRegisterId("粤ICP备1002号");
    	s2.setAreaCode("440300");
    	s2.setCreateUserId(869);

    	List<ServiceDomainInformation> domains = new ArrayList<ServiceDomainInformation>();
    	ServiceDomainInformation d1 = new ServiceDomainInformation();
    	d1.setDomainName("baidu1.com");
    	ServiceDomainInformation d2 = new ServiceDomainInformation();
    	d2.setDomainName("baidu2.com");
    	
    	domains.add(d1);
    	domains.add(d2);
    	s2.setDomainList(domains);
    	services.add(s1);
    	services.add(s2);
    	ResultDto result = preUserServerServiceImpl.insertData(services, null, false);
    	System.out.println(JSON.toJSONString(result));
    }
    
    @Test
   	public void testUserServiceUpdate() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
    	List<UserServiceInformationDTO> services = new ArrayList<UserServiceInformationDTO>();
    	UserServiceInformationDTO s1 = new UserServiceInformationDTO();
    	s1.setCityCodeList(cityCodeList);
    	s1.setUserAuthHouseList(authHouseList);
    	s1.setUserAuthIdentityList(authIdentityList);
    	s1.setServiceId(21614L);
    	s1.setUserId(30346L);
    	s1.setServiceType(1);
    	s1.setServiceContent("2,3,4");
    	s1.setSetmode(2);//主机托管
    	s1.setBusiness(1);
    	//s1.setRegType(2);
    	//s1.setRegisterId("粤ICP备1000号");
    	s1.setAreaCode("440300");
    	s1.setUpdateUserId(869);
    	
    	UserServiceInformationDTO s2 = new UserServiceInformationDTO();
    	s2.setCityCodeList(cityCodeList);
    	s2.setUserAuthHouseList(authHouseList);
    	s2.setUserAuthIdentityList(authIdentityList);
    	s2.setServiceId(21615L);
    	s2.setUserId(30346L);
    	s2.setServiceType(1);
    	s2.setServiceContent("2,3,4");
    	s2.setSetmode(2);//主机托管
    	s2.setBusiness(1);
    	s2.setRegType(2);
    	s2.setRegisterId("粤ICP备1003号");
    	s2.setAreaCode("440300");
    	s2.setUpdateUserId(869);

    	List<ServiceDomainInformation> domains = new ArrayList<ServiceDomainInformation>();
    	ServiceDomainInformation d1 = new ServiceDomainInformation();
    	d1.setDomainName("baidu3.com");
    	ServiceDomainInformation d2 = new ServiceDomainInformation();
    	d2.setDomainName("baidu4.com");
    	ServiceDomainInformation d3 = new ServiceDomainInformation();
    	d3.setDomainName("baidu5.com");
    	
    	domains.add(d1);
    	domains.add(d2);
    	domains.add(d3);
    	s2.setDomainList(domains);
    	services.add(s1);
    	services.add(s2);
    	ResultDto result = preUserServerServiceImpl.updateData(services);
    	System.out.println(JSON.toJSONString(result));
    }
    
    @Test
   	public void testUserServiceDelete() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
    	List<UserServiceInformationDTO> services = new ArrayList<UserServiceInformationDTO>();
    	UserServiceInformationDTO s1 = new UserServiceInformationDTO();
    	s1.setCityCodeList(cityCodeList);
    	s1.setUserAuthHouseList(authHouseList);
    	s1.setUserAuthIdentityList(authIdentityList);
    	s1.setServiceId(21614L);
    	
    	UserServiceInformationDTO s2 = new UserServiceInformationDTO();
    	s2.setCityCodeList(cityCodeList);
    	s2.setUserAuthHouseList(authHouseList);
    	s2.setUserAuthIdentityList(authIdentityList);
    	s2.setServiceId(21615L);

    	services.add(s1);
    	services.add(s2);
    	ResultDto result = preUserServerServiceImpl.deleteData(services);
    	System.out.println(JSON.toJSONString(result));
    }
    
    @Test
   	public void testUserBandwidthDelete() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	List<UserBandwidthInformationDTO> bands = new ArrayList<UserBandwidthInformationDTO>();
		UserBandwidthInformationDTO b1 = new UserBandwidthInformationDTO();
		b1.setAreaCode("440300");
		b1.setCityCodeList(cityCodeList);
    	b1.setUserAuthHouseList(authHouseList);
    	b1.setUserAuthIdentityList(authIdentityList);
    	b1.setHhId(29396L);
    	
    	UserBandwidthInformationDTO b2 = new UserBandwidthInformationDTO();
		b2.setAreaCode("440300");
		b2.setCityCodeList(cityCodeList);
    	b2.setUserAuthHouseList(authHouseList);
    	b2.setUserAuthIdentityList(authIdentityList);
    	b2.setHhId(29397L);
    	
    	bands.add(b1);
    	bands.add(b2);
    	preUserBandWidthServiceImpl.deleteData(bands);
    }
    
    @Test
   	public void testUserBandwidthQuery() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
    	UserBandwidthInformationDTO dto = new UserBandwidthInformationDTO();
    	dto.setCityCodeList(cityCodeList);
    	dto.setUserAuthHouseList(authHouseList);
    	dto.setUserAuthIdentityList(authIdentityList);
    	PageResult<UserBandwidthInformationDTO> pageResult = preUserBandWidthServiceImpl.getServerInfoList(dto);
		System.out.println(JSON.toJSONString(pageResult));
    }

    @Test
	public void testUserBandwidthUpdate() throws Exception {
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
		
		List<UserBandwidthInformationDTO> bands = new ArrayList<UserBandwidthInformationDTO>();
		UserBandwidthInformationDTO b1 = new UserBandwidthInformationDTO();
		b1.setAreaCode("440300");
		b1.setCityCodeList(cityCodeList);
    	b1.setUserAuthHouseList(authHouseList);
    	b1.setUserAuthIdentityList(authIdentityList);
    	b1.setHhId(29396L);
		b1.setUserId(30334L);
		b1.setHouseId(4307L);
		b1.setDistributeTime("2018-08-19");
		b1.setBandWidth(130L);
		b1.setUpdateUserId(869);
		
		UserBandwidthInformationDTO b2 = new UserBandwidthInformationDTO();
		b2.setAreaCode("440300");
		b2.setCityCodeList(cityCodeList);
    	b2.setUserAuthHouseList(authHouseList);
    	b2.setUserAuthIdentityList(authIdentityList);
    	b2.setHhId(29397L);
		b2.setUserId(30332L);
		b2.setHouseId(4307L);
		b2.setDistributeTime("2018-09-19");
		b2.setBandWidth(300L);
		b2.setUpdateUserId(869);
		
		bands.add(b1);
		bands.add(b2);
    	ResultDto resultDto = preUserBandWidthServiceImpl.updateData(bands);
		System.out.println(JSON.toJSONString(resultDto));
    }
    
	@Test
	public void testUserBandwidthInsert() throws Exception {
		List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
		
		List<UserBandwidthInformation> bands = new ArrayList<UserBandwidthInformation>();
		UserBandwidthInformation b1 = new UserBandwidthInformation();
		b1.setAreaCode("440300");
		b1.setCityCodeList(cityCodeList);
    	b1.setUserAuthHouseList(authHouseList);
    	b1.setUserAuthIdentityList(authIdentityList);
		b1.setUserId(30334L);
		b1.setHouseId(4307L);
		b1.setDistributeTime("2018-08-19");
		b1.setBandWidth(13L);
		b1.setCreateUserId(869);
		
		UserBandwidthInformation b2 = new UserBandwidthInformation();
		b2.setAreaCode("440300");
		b2.setCityCodeList(cityCodeList);
    	b2.setUserAuthHouseList(authHouseList);
    	b2.setUserAuthIdentityList(authIdentityList);
		b2.setUserId(30332L);
		b2.setHouseId(4307L);
		b2.setDistributeTime("2018-08-19");
		b2.setBandWidth(13L);
		b2.setCreateUserId(869);
		
		bands.add(b1);
		bands.add(b2);
		ResultDto resultDto = preUserBandWidthServiceImpl.insertData(bands, null, false);
		System.out.println(JSON.toJSONString(resultDto));
	}
    
    @Test
    public void test(){
        UserInformationDTO dto = new UserInformationDTO();
        dto.setIsPaging(0);
        PageResult<UserInformationDTO> result = preUserInfoService.listUserData(dto);
        System.out.println(result);
    }

    @Test
    public void test2(){
        UserInformationDTO userInformationDTO = preUserInfoService.findByUserId("24381");
        System.out.println(userInformationDTO);
    }
    
    @Test
    public void testApprove(){
    	ResultDto resultDto = preUserInfoService.approve("30414");
    	System.out.println(JSON.toJSONString(resultDto));
    }
}
