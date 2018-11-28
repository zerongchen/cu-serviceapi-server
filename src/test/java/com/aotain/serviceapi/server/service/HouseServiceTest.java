package com.aotain.serviceapi.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
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
import com.aotain.cu.serviceapi.model.UserBandwidthInformation;
import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;
import com.aotain.serviceapi.server.ServiceapiServerApplication;
import com.aotain.serviceapi.server.constant.HouseConstant;
import com.aotain.serviceapi.server.dao.preinput.HouseFrameMapper;
import com.aotain.serviceapi.server.dao.preinput.HouseIpSegmentMapper;
import com.aotain.serviceapi.server.dao.preinput.HouseLinkMapper;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import com.aotain.serviceapi.server.dao.preinput.UserHHMapper;
import com.aotain.serviceapi.server.dao.preinput.UserPrincipalMapper;
import com.aotain.serviceapi.server.dao.preinput.UserVirtualMachineMapper;
import com.aotain.serviceapi.server.service.preinput.impl.HouseLinkServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.IHouseInformationServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.PreHouseFrameServiceImpl;
import com.aotain.serviceapi.server.service.preinput.impl.PreHouseIpsegServiceImpl;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)
public class HouseServiceTest{
    @Autowired
    private IHouseInformationServiceImpl houseInformationService;

    @Autowired
	private HouseLinkServiceImpl houseLinkService;
    @Autowired
	private PreHouseFrameServiceImpl preHouseFrameService;
    @Autowired
	private PreHouseIpsegServiceImpl preHouseIpsegService;
    @Autowired
    private HouseLinkMapper houseLinkMapper;
    @Autowired
    private HouseFrameMapper houseFrameMapper;
    @Autowired
    private HouseIpSegmentMapper houseIpSegmentMapper;
    @Autowired
    private HousePrincipalMapper housePrincipalMapper;
    @Autowired
    private UserPrincipalMapper userPrincipalMapper;
	@Autowired
	private IHouseInformationServiceImpl iHouseInformationServiceImpl;
	@Autowired
	private UserVirtualMachineMapper userVirtualMachineMapper;
	@Autowired
	private UserHHMapper userHHMapper;
	
    @Test
    public void test(){
        ResultDto resultDto = houseInformationService.approve("4244");
        System.out.println(resultDto);
    }

    @Test
    public void testRRR(){
       // ResultDto resultDto = houseInformationService.findCheckResultById("4244");
        //System.out.println("result=========================:"+resultDto);
        HouseInformationDTO dto = new HouseInformationDTO();
        dto.setHouseId(4244L);
        System.out.println("================="+houseInformationService.getHouseInfoById(dto).getHouseName());
    }
    
	@Test
	public void insertILinkinfo() {
		try {
			List<String> cityCodeList = new ArrayList<>();
			cityCodeList.add("440300");
			List<HouseGatewayInformationDTO> linkList = new ArrayList<HouseGatewayInformationDTO>();
			
			HouseGatewayInformationDTO linkDto2 = new HouseGatewayInformationDTO();
			linkDto2.setLinkNo("链路1003号");
			linkDto2.setAreaCode("440300");
			linkDto2.setCityCodeList(cityCodeList);
			linkDto2.setBandWidth(100L);
			linkDto2.setHouseId(4308L);
			linkDto2.setGatewayIP("192.168.10.1");
			linkDto2.setCreateUserId(869);
			
			//机房主体新增+上报成功，此时新增机架
			HouseInformation houseInformation = new HouseInformation();
			houseInformation.setHouseId(4308L);
			houseInformation.setCzlx(HouseConstant.OperationTypeEnum.ADD.getValue());
			houseInformation.setDealFlag(HouseConstant.DealFlagEnum.REPORT_SUCCESS.getValue());
			houseInformationService.updateHouseDealFlagByHouseId(houseInformation);
			
			linkList.add(linkDto2);
			houseLinkService.batchInsertHouseLinkInfos(linkList, null, false);
			
			HouseGatewayInformationDTO linkDto3 = new HouseGatewayInformationDTO();
			linkDto3.setLinkNo("链路1004号");
			linkDto3.setAreaCode("440300");
			linkDto3.setCityCodeList(cityCodeList);
			linkDto3.setBandWidth(100L);
			linkDto3.setHouseId(4307L);
			linkDto3.setGatewayIP("192.168.10.1");
			linkDto3.setCreateUserId(869);
			
			//机房主体提交上报中，此时新增机架
			houseInformation.setHouseId(4307L);
			houseInformation.setCzlx(HouseConstant.OperationTypeEnum.ADD.getValue());
			houseInformation.setDealFlag(HouseConstant.DealFlagEnum.REPORTING.getValue());
			houseInformationService.updateHouseDealFlagByHouseId(houseInformation);
			
			linkList.clear();
			linkList.add(linkDto3);
			houseLinkService.batchInsertHouseLinkInfos(linkList, null, false);
			
		} catch (Exception e) {
			System.out.println("link insert fail..............." + e);
		}
		System.out.println("link insert success...............");
	}
	
	@Test
	public void updateOrDeleteLinkinfo() {
		try {
			List<String> cityCodeList = new ArrayList<>();
			cityCodeList.add("440300");
			HouseGatewayInformationDTO linkDto = new HouseGatewayInformationDTO();
			linkDto.setGatewayId(10556L);
			linkDto.setLinkNo("链路100号");
			linkDto.setAreaCode("440300");
			linkDto.setBandWidth(1000L);
			linkDto.setHouseId(4308L);
			linkDto.setCityCodeList(cityCodeList);
			linkDto.setGatewayIP("192.168.10.10");
			linkDto.setCreateUserId(869);			
			//houseLinkService.updateData(linkDto);
			
			

			HouseGatewayInformationDTO linkDto2 = new HouseGatewayInformationDTO();
			linkDto2.setLinkNo("链路101号");
			linkDto2.setAreaCode("440300");
			linkDto2.setCityCodeList(cityCodeList);
			linkDto2.setBandWidth(100L);
			linkDto2.setHouseId(4308L);
			linkDto2.setGatewayIP("192.168.10.1");
			linkDto2.setCreateUserId(869);
		} catch (Exception e) {
			System.out.println("update or delete insert fail..............." + e);
		}
		System.out.println("link update or delete success...............");
	}
    
    @Test
    public void insertFrameInfo(){
    	List<String> cityCodeList = new ArrayList<>();
		cityCodeList.add("440300");
        try {
			List<HouseFrameInformation> frameList = new ArrayList<HouseFrameInformation>();
			HouseFrameInformationDTO frameDto = new HouseFrameInformationDTO();
			frameDto.setFrameName("113号机架");
			frameDto.setDistribution(1);
			frameDto.setUseType(1);
			frameDto.setCreateUserId(869);
			frameDto.setCityCodeList(cityCodeList);
			frameDto.setAreaCode("440300");
			frameDto.setHouseId(4244L);
			frameDto.setOccupancy(1);
			
			HouseFrameInformationDTO frameDto2 = new HouseFrameInformationDTO();
			frameDto2.setFrameName("114号机架");
			frameDto2.setDistribution(1);
			frameDto2.setUseType(1);
			frameDto2.setCreateUserId(869);
			frameDto2.setCityCodeList(cityCodeList);
			frameDto2.setAreaCode("440300");
			frameDto2.setHouseId(4244L);
			frameDto2.setOccupancy(1);
			List<HouseUserFrameInformation> userFrameList = new ArrayList<>();
			HouseUserFrameInformation uDto1= new HouseUserFrameInformation();
			HouseUserFrameInformation uDto2= new HouseUserFrameInformation();
			/*uDto1.setFrameId(484616L);
			uDto1.setHouseId(4244L);*/
			uDto1.setUserName("一号机架员");
			uDto1.setIdType(2);
			uDto1.setIdNumber("362425199308151272");
			
			uDto2.setUserName("二号机架员");
			uDto2.setIdType(2);
			uDto2.setIdNumber("362425199308151272");
			userFrameList.add(uDto1);
			userFrameList.add(uDto2);
			frameDto2.setUserFrameList(userFrameList);
			
			frameList.add(frameDto);
			frameList.add(frameDto2);
			//System.out.println("insert frame success======:"+houseFrameMapper.insertUserFrame(uDto1));
			System.out.println("insert frame success======:"+preHouseFrameService.batchInsertFrameInfos(frameList, null, false));
		} catch (Exception e) {
			System.out.println("insert frame failed"+e);
		}
    }
    
    @Test
    public void insertIPseg(){
    	List<HouseIPSegmentInforDTO> ipList = new ArrayList();
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	try {
    		HouseIPSegmentInforDTO ipDto = new HouseIPSegmentInforDTO();
			ipDto.setStartIP("192.168.11.13");
			ipDto.setEndIP("192.168.11.13");
			ipDto.setUseTime("2019-01-01");
			ipDto.setIpType(2);
			ipDto.setAreaCode("440300");
			ipDto.setIpSegNo("ip000113");
			ipDto.setAllocationUnit("idc");
			ipDto.setSourceUnit("idc");
			ipDto.setCreateUserId(869);
			ipDto.setHouseId(4244L);
			ipDto.setCityCodeList(cityCodeList);
			
			HouseIPSegmentInforDTO ipDto2 = new HouseIPSegmentInforDTO();
			ipDto2.setStartIP("192.168.11.14");
			ipDto2.setEndIP("192.168.11.14");
			ipDto2.setUseTime("2019-01-01");
			ipDto2.setIpType(2);
			ipDto2.setAreaCode("440300");
			ipDto2.setIpSegNo("ip000114");
			ipDto2.setAllocationUnit("idc");
			ipDto2.setSourceUnit("idc");
			ipDto2.setCreateUserId(869);
			ipDto2.setHouseId(4244L);
			ipDto2.setCityCodeList(cityCodeList);
			ipList.add(ipDto);
			ipList.add(ipDto2);
			System.out.println("insert ipseg success======:"+preHouseIpsegService.batchInsertHouseIpsegInfos(ipList, null, false));
		} catch (Exception e) {
			System.out.println("insert ipseg failed"+e);
		}
    }
    
    @Test
    public void deleteLinkInfo(){
    	List<Long> linkIds = new ArrayList<>();
    	linkIds.add(10472L);
    	linkIds.add(10473L);
    	try {
			//System.out.println("delete success=="+houseLinkService.batchDeleteHouseLinkInfos(linkIds));
			//houseLinkMapper.deleteByHouseId(4210L);
			//System.out.println("delete success==="+houseLinkMapper.deleteByLinkId(10469L));
		} catch (Exception e) {
			System.out.println(" delete failed"+e);
		}
    }
    
    @Test
    public void deleteFrameInfo(){
    	List<Long> frameIds = new ArrayList<>();
    	frameIds.add(3595L);
    	frameIds.add(3596L);
    	try {
			//System.out.println("delete frame by frameId success==="+houseFrameMapper.deleteByFrameId(3611));
    		//System.out.println("delete frame by frameId success==="+preHouseFrameService.batchDeleteHouseFrameInfos(frameIds));
		} catch (Exception e) {
			System.out.println("delete frame failed==="+e);
		}
    }
    @Test 
    public void deleteIpsegInfo(){
    	List<Long> ipIds = new ArrayList<>();
    	ipIds.add(276979L);
    	ipIds.add(277150L);
    	try {
			//System.out.println("delete frame by frameId success==="+houseFrameMapper.deleteByFrameId(3611));
    		//System.out.println("delete ipseg by frameId success==="+preHouseIpsegService.batchDeleteHouseIpsegInfos(ipIds));
		} catch (Exception e) {
			System.out.println("delete ipseg failed==="+e);
		}
    }
    
    @Test
    public void deleteHouseMainInfo(){
    	
    }
    
    @Test
    public void findIpsegInfo(){
    	HouseIPSegmentInformation dto = houseIpSegmentMapper.findByIpSegId(276985L);
    	System.out.println("result===="+dto.getUserName());
    }
    
    @Test
    public void findFrameInfo(){
    	HouseFrameInformation frame = new HouseFrameInformation();
    	
    	try {
			//System.err.println("result===="+houseFrameMapper.findFrameInfoByFrameId(3595).getFrameName());
		} catch (Exception e) {
			System.out.println("result==="+e);
		}
    }
    
    
    @Test
    public void findLinkInfo(){
    	try {
			//System.err.println("result===="+houseLinkMapper.findByLinkId(10326).getLinkNo());
		} catch (Exception e) {
			System.out.println("result==="+e);
		}
    }
    
    @Test
    public void findUserInfo(){
    	UserInformation userInfo = new UserInformation();
    	userInfo.setUnitName("测试111");
    	userInfo.setIdType(2);
    	userInfo.setIdNumber("12345566");
    	try {
			System.out.println("result==="+userPrincipalMapper.findByUnitNameAndIdTypeAndNumber(userInfo).getUserCode());
		} catch (Exception e) {
			System.out.println("insert failed==="+e);
		}
    }
    
    @Test
    public void update(){
    	Map<String, Object> map = new HashMap<String, Object>();
		/*map.put("frameId", 3595);
		map.put("czlx", 1);//删除
		map.put("dealFlag", 0);//未上报
		try {
			System.out.println("update success=="+houseFrameMapper.updateFrameStatusByframeId(map));
		} catch (Exception e) {
			System.out.println("update failed=="+e);
		}*/
    	
    	
    	/*map.put("linkId", 10470);
		map.put("czlx", 2);//删除
		map.put("dealFlag", 1);//未上报
		try {
			System.out.println("update success=="+houseLinkMapper.updateLinkStatusByLinkId(map));
		} catch (Exception e) {
			System.out.println("update failed=="+e);
		}*/
		
		map.put("ipsegId", 276985);
		map.put("czlx", 1);//删除
		map.put("dealFlag", 1);//未上报
		try {
			System.out.println("update success=="+houseIpSegmentMapper.updateIPSegtatusByIpsegId(map));
		} catch (Exception e) {
			System.out.println("update failed=="+e);
		}
    }
    
    @Test
    public void testFindById(){
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
    	HouseInformationDTO houseDto = new HouseInformationDTO();
    	houseDto.setCityCodeList(cityCodeList);
    	houseDto.setUserAuthHouseList(authHouseList);
    	houseDto.setUserAuthIdentityList(authIdentityList);
    	houseDto.setHouseId(4327L);
    	
    	houseDto.setHouseName("更新机房测试1");
    	houseDto.setHouseCity(440300);
    	houseDto.setAreaCode("440300");
    	houseDto.setHouseAddress("测试地址");
    	houseDto.setHouseCounty(440307);
    	houseDto.setHouseIdStr("testHouse7012");
    	houseDto.setHouseProvince(440000);
    	houseDto.setIdentity(1);
    	houseDto.setHouseOfficerEmail("52@qq.com");
    	houseDto.setHouseType(1);
    	houseDto.setHouseOfficerName("ggg");
    	houseDto.setHouseOfficerId("420821198709136117");
    	houseDto.setHouseOfficerIdType(2);
    	houseDto.setHouseOfficerMobile("13437968100");
    	houseDto.setHouseOfficerTelephone("2552093");
    	houseDto.setCreateUserId(869);
    	
    	System.out.println(housePrincipalMapper.findByHouseId(houseDto));
    }
    
	@Test
    public void testDemo() {
		HouseInformation houseDto = new HouseInformation();
		houseDto.setHouseId(Long.valueOf("4330"));
		houseDto.setDealFlag(HouseConstant.DealFlagEnum.CHECK_FAIL.getValue());
		houseDto.setVerificationResult("预审不通过");
		housePrincipalMapper.updateDealFlagById(houseDto);
    }
	
    @Test
    public void approveHouse() {
    	try {
			System.out.println("result===" + houseInformationService.approve("4338"));
		} catch (Exception e) {
			System.out.println("insert failed===" + e);
		}
    }
    
    @Test
    public void updateHouse(){
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	authHouseList.add("4327");
    	authHouseList.add("4325");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	
    	HouseInformationDTO houseDto = new HouseInformationDTO();
    	houseDto.setCityCodeList(cityCodeList);
    	houseDto.setUserAuthHouseList(authHouseList);
    	houseDto.setUserAuthIdentityList(authIdentityList);
    	houseDto.setHouseId(4327L);
    	houseDto.setHouseName("更新机房测试1");
    	houseDto.setHouseCity(440300);
    	houseDto.setAreaCode("440300");
    	houseDto.setHouseAddress("测试地址");
    	houseDto.setHouseCounty(440307);
    	houseDto.setHouseIdStr("testHouse7012");
    	houseDto.setHouseProvince(440000);
    	houseDto.setIdentity(1);
    	houseDto.setHouseOfficerEmail("52@qq.com");
    	houseDto.setHouseType(1);
    	houseDto.setHouseOfficerName("ggg");
    	houseDto.setHouseOfficerId("420821198709136117");
    	houseDto.setHouseOfficerIdType(2);
    	houseDto.setHouseOfficerMobile("13437968100");
    	houseDto.setHouseOfficerTelephone("2552093");
    	houseDto.setUpdateUserId(869);
    	
    	HouseInformationDTO houseDto1 = new HouseInformationDTO();
    	houseDto1.setCityCodeList(cityCodeList);
    	houseDto1.setUserAuthHouseList(authHouseList);
    	houseDto1.setUserAuthIdentityList(authIdentityList);
    	houseDto1.setHouseId(4325L);
    	houseDto1.setHouseCity(440300);
    	houseDto1.setAreaCode("440300");
    	houseDto1.setHouseCounty(440307);
    	houseDto1.setHouseIdStr("testHouse7010");
    	houseDto1.setHouseProvince(440000);
    	//houseDto1.setIdentity(1);
    	houseDto1.setHouseOfficerEmail("52@qq.com");
    	houseDto1.setHouseType(1);
    	houseDto1.setHouseOfficerName("ggg");
    	//houseDto1.setHouseOfficerId("420821198709136117");
    	houseDto1.setHouseOfficerIdType(2);
    	houseDto1.setHouseOfficerMobile("13437968100");
    	houseDto1.setHouseOfficerTelephone("2552093");
    	houseDto1.setHouseName("更新机房测试2");
    	houseDto1.setHouseAddress("更新机房测试地址");
    	houseDto1.setUpdateUserId(869);
    	
    	List<HouseInformationDTO> resultList = new ArrayList<HouseInformationDTO>();
    	//resultList.add(houseDto);
    	resultList.add(houseDto1);
    	for (HouseInformationDTO house : resultList) {
    		System.out.println("result===" + houseInformationService.updateData(house));
		}
    }
    
    @Test
    public void insertHouse(){
    	List<String> cityCodeList = new ArrayList<>();
    	cityCodeList.add("440300");
    	List<String> authHouseList = new ArrayList<>();
    	authHouseList.add("4307");
    	authHouseList.add("4308");
    	List<String> authIdentityList = new ArrayList<>();
    	authIdentityList.add("1");
    	authIdentityList.add("5");
    	//1.机房主体
    	HouseInformation houseDto = new HouseInformation();
    	houseDto.setCityCodeList(cityCodeList);
    	houseDto.setUserAuthHouseList(authHouseList);
    	houseDto.setUserAuthIdentityList(authIdentityList);
    	houseDto.setAppId(62);
    	houseDto.setDataPermissionId(94);
    	houseDto.setDataPermissionToken("AUTHHOUSELIST");
    	houseDto.setDataPermissionName("隶属机房");
    	houseDto.setDataPermissionDesc("隶属机房");
    	houseDto.setPermissionMethodUrl("http://192.168.50.152:8060/rest/data/updatedataPermissionactions");
    	houseDto.setHouseName("测试机房14");
    	houseDto.setHouseCity(440300);
    	houseDto.setAreaCode("440300");
    	houseDto.setHouseAddress("测试地址");
    	houseDto.setHouseCounty(440307);
    	houseDto.setHouseIdStr("testHouse7014");
    	houseDto.setHouseProvince(440000);
    	houseDto.setIdentity(1);
    	houseDto.setHouseOfficerEmail("52@qq.com");
    	houseDto.setHouseType(1);
    	houseDto.setHouseOfficerName("ggg");
    	houseDto.setHouseOfficerId("420821198709136117");
    	houseDto.setHouseOfficerIdType(2);
    	houseDto.setHouseOfficerMobile("13437968100");
    	houseDto.setHouseOfficerTelephone("2552093");
    	houseDto.setCreateUserId(869);
    	
    	//2.机架
    	List<HouseFrameInformation> frameList = new ArrayList<HouseFrameInformation>();
    	HouseFrameInformation frameDto = new HouseFrameInformation();
    	frameDto.setCityCodeList(cityCodeList);
    	frameDto.setUserAuthHouseList(authHouseList);
    	frameDto.setUserAuthIdentityList(authIdentityList);
		frameDto.setFrameName("401号机架");
		frameDto.setUseType(1);
		frameDto.setCreateUserId(869);
		frameDto.setAreaCode("440300");
		frameDto.setOccupancy(1);
		
		HouseFrameInformation frameDto2 = new HouseFrameInformation();
		frameDto2.setCityCodeList(cityCodeList);
		frameDto2.setUserAuthHouseList(authHouseList);
		frameDto2.setUserAuthIdentityList(authIdentityList);
		frameDto2.setFrameName("402号机架");
		frameDto2.setUseType(1);
		frameDto2.setCreateUserId(869);
		frameDto2.setAreaCode("440300");
		frameDto2.setOccupancy(1);
		List<HouseUserFrameInformation> userFrameList = new ArrayList<>();
		HouseUserFrameInformation uDto1= new HouseUserFrameInformation();
		HouseUserFrameInformation uDto2= new HouseUserFrameInformation();
		
		uDto1.setUserName("一号机架员");
		uDto1.setIdType(2);
		uDto1.setIdNumber("362425199308151272");
		
		uDto2.setUserName("二号机架员");
		uDto2.setIdType(2);
		uDto2.setIdNumber("362425199308151272");
		userFrameList.add(uDto1);
		userFrameList.add(uDto2);
		frameDto2.setUserFrameList(userFrameList);
		
		frameList.add(frameDto);
		frameList.add(frameDto2);
    	
    	//3.链路
		List<HouseGatewayInformation> linkList = new ArrayList<HouseGatewayInformation>();
		HouseGatewayInformation linkDto = new HouseGatewayInformation();
		linkDto.setCityCodeList(cityCodeList);
		linkDto.setUserAuthHouseList(authHouseList);
		linkDto.setUserAuthIdentityList(authIdentityList);
		linkDto.setLinkNo("链路701号");
		linkDto.setBandWidth(100L);
		linkDto.setGatewayIP("192.168.10.1");
		linkDto.setCreateUserId(869);
		linkDto.setAreaCode("440300");
		
		HouseGatewayInformation linkDto2 = new HouseGatewayInformation();
		linkDto2.setCityCodeList(cityCodeList);
		linkDto2.setUserAuthHouseList(authHouseList);
		linkDto2.setUserAuthIdentityList(authIdentityList);
		linkDto2.setLinkNo("链路702号");
		linkDto2.setAreaCode("440300");
		linkDto2.setBandWidth(100L);
		linkDto2.setGatewayIP("192.168.10.1");
		linkDto2.setCreateUserId(869);
		linkList.add(linkDto);
		linkList.add(linkDto2);
    	
    	//4.IP地址段
    	List<HouseIPSegmentInformation> ipList = new ArrayList<HouseIPSegmentInformation>();
    	HouseIPSegmentInformation ipDto = new HouseIPSegmentInformation();
    	ipDto.setCityCodeList(cityCodeList);
    	ipDto.setUserAuthHouseList(authHouseList);
    	ipDto.setUserAuthIdentityList(authIdentityList);
		ipDto.setStartIP("192.168.10.10");
		ipDto.setEndIP("192.168.10.10");
		ipDto.setUseTime("2019-01-01");
		ipDto.setIpType(2);
		ipDto.setAreaCode("440300");
		ipDto.setCreateUserId(869);
		
		HouseIPSegmentInformation ipDto2 = new HouseIPSegmentInformation();
		ipDto2.setCityCodeList(cityCodeList);
		ipDto2.setUserAuthHouseList(authHouseList);
		ipDto2.setUserAuthIdentityList(authIdentityList);
		ipDto2.setStartIP("192.168.10.11");
		ipDto2.setEndIP("192.168.10.11");
		ipDto2.setUseTime("2019-01-01");
		ipDto2.setIpType(2);
		ipDto2.setAreaCode("440300");
		ipDto2.setCreateUserId(869);
		ipList.add(ipDto);
		ipList.add(ipDto2);	
		
		//houseDto.setFrameList(frameList);
		//houseDto.setGatewayInfoList(linkList);
		//houseDto.setIpSegList(ipList);
    	
		try {
			ResultDto resultDto = houseInformationService.insertData(houseDto);
			System.out.println("result===" + JSON.toJSONString(resultDto));
		} catch (Exception e) {
			System.out.println("insert failed===" + e);
		}
    }
    
    @Test
    public void findHouseLinkUser(){
    	List<UserInformation> userList = housePrincipalMapper.findHaveReportFrameAndIPsegConnectUserInfoByHouseId(4244);
    	for(UserInformation user:userList){
    		System.out.println("result=="+user.getUnitName());
    	}
    }

	@Test
	public void deleteHouseMain(){
		List<Long> houseList = new ArrayList<>();
		houseList.add(4275L);
		try{
			//System.out.println("result==="+houseInformationService.batchDeleteHouseInfos(houseList));
		} catch (Exception e){
			System.out.println("delete failed==="+e);
		}

	}

	@Test
	public void findLinkInfos(){
		HouseGatewayInformationDTO dto = new HouseGatewayInformationDTO();
		try {
			System.out.println("result==="+iHouseInformationServiceImpl.getIndexLink(dto));
		} catch (Exception e) {
			System.out.println("failedss==="+e);
		}
	}
	
	@Test
	public void FindHouse(){
		HouseInformationDTO houseDto = new HouseInformationDTO();
		HouseInformationDTO houseDto1 = new HouseInformationDTO();
		//houseDto1.setHouseId(823L);
		houseDto1.setHouseName("傲天科技IDC机房-勿删1");
		try {
			System.out.println("result=="+housePrincipalMapper.findByHouseName(houseDto1).getHouseName());
		} catch (Exception e) {
			System.out.println("failed=="+e);
		}
	}
	
	@Test
	public void updateUserHHInfo(){
		UserBandwidthInformation dto = new UserBandwidthInformation();
		dto.setHhId(29263L);
		dto.setBandWidth(699L);
		try {
			System.out.println("result==="+userHHMapper.updateUserHHInfoByHHid(dto));
		} catch (Exception e) {
			System.out.println("result failed==="+e);
		}
	}
	
	@Test
	public void updateUserVirtualInfo(){
		UserVirtualInformation dto = new UserVirtualInformation();
		dto.setVirtualId(1041L);
		dto.setName("testVir");
		dto.setNetworkAddress("6.6.6.6");
		dto.setMgnAddress("6.6.6.6");
		dto.setVirtualNo("test6666");
		dto.setType(1);
		dto.setStatus(1);
		try {
			System.out.println("result==="+userVirtualMachineMapper.updateVirtualInfoById(dto));
		} catch (Exception e) {
			System.out.println("result failed==="+e);
		}
		
	}
	
	/*@Test
	public void findUserVirtualList(){
		UserVirtualInformation dto = new UserVirtualInformation();
		dto.setVirtualId(1041L);
		dto = userVirtualMachineMapper.getIndexUserVirtual(dto).get(0);
		try {
			System.out.println("result==="+dto.toString()+"==virNo:"+dto.getVirtualNo()+"==name:"+dto.getName()+"==address:"+dto.getMgnAddress()+"--"+dto.getNetworkAddress());
			System.out.println("result==="+userVirtualMachineMapper.getUserVirtualList(dto));
		} catch (Exception e) {
			System.out.println("result failed==="+e);
		}
	}*/

	@Test
	public void findLinkList(){
		HouseGatewayInformationDTO dto = new HouseGatewayInformationDTO();
		dto.setGatewayId(10488L);
    	try {
			System.out.println("result=="+houseLinkMapper.getIndexHouseLink(dto).get(0).getLinkNo());;
		} catch (Exception e) {
			System.out.println("result failed==="+e);
		}
	}
    
}
