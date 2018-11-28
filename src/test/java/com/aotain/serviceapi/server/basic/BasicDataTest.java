package com.aotain.serviceapi.server.basic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.HouseFrameInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseGatewayInformationDTO;
import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.cu.serviceapi.dto.UserBandwidthInformationDTO;
import com.aotain.cu.serviceapi.dto.UserInformationDTO;
import com.aotain.cu.serviceapi.dto.UserServiceInformationDTO;
import com.aotain.cu.serviceapi.model.HouseUserFrameInformation;
import com.aotain.cu.serviceapi.model.ServiceDomainInformation;
import com.aotain.cu.serviceapi.model.UserVirtualInformation;
import com.aotain.serviceapi.server.ServiceapiServerApplication;
import com.aotain.serviceapi.server.util.Tools;
import com.aotain.serviceapi.server.validate.impl.HouseFrameValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.HouseIpSegmentValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.HouseLinkValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.HousePrincipalValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.IdcInformationValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.UserNetworkValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.UserPrincipalValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.UserServiceValidatorImpl;
import com.aotain.serviceapi.server.validate.impl.UserVirtualMachineValidatorImpl;

/**
 * 基础数据字段测试
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)*/
public class BasicDataTest {
	final static String LENGTH_65_CHINESE = "长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长";
	final static String LENGTH_64_CHINESE = "长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长";
	
	final static String LENGTH_129_ENGLISH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
									+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	final static String LENGTH_128_ENGLISH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
									+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	
	final static String LENGTH_65_ENGLISH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	final static String LENGTH_64_ENGLISH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	final static String LENGTH_33_ENGLISH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	final static String LENGTH_32_ENGLISH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	final static String LENGTH_7_ENGLISH = "1111111";
	final static String LENGTH_6_ENGLISH = "111111";
	
	final static String MOBILEPHONE_TEST_SUCCESS = "13237968100";
	final static String MOBILEPHONE_TEST_ERROR = "132379681001";
	final static String MOBILEPHONE_TEST_ERROR_2 = "03237968100";
	
	final static String TELILEPHONE_TEST_SUCESS = "2552033";
	final static String TELILEPHONE_TEST_SUCESS_2 = "0796-2552033";
	final static String TELILEPHONE_TEST_SUCESS_3 = "0796-25520332";
	final static String TELILEPHONE_TEST_ERROR = "123456789";
	final static String TELILEPHONE_TEST_ERROR_2 = "233233333";
	
	final static String EMALIL_TEST_SUCCESS ="52@11.com";
	final static String EMALIL_TEST_ERR_1 ="52@@11.com";
	final static String EMALIL_TEST_ERR_2 ="5211.com";
	
	public static void main(String[] args) {
		/*StringBuffer sb = new StringBuffer();
		for(int i=0;i<129;i++){
			sb.append("a");
		}
		System.out.println(sb);*/
		System.out.println(Tools.isIpV6Address("AD80:0000:0000:0000:ABAA:0000:00C2:0002"));
		System.out.println(Tools.isIpAddress("AD80:0000:0000:0000:ABAA:0000:00C2:0002"));
		
	}
	/**
	 * 校验经营者测试
	 * 
	 */
	@Test
	public void idcManageTest(){
		IdcInformationValidatorImpl idcValidator = new IdcInformationValidatorImpl();
		/**
		 * 1.经营者空字段校验
		 */
		IDCInformationDTO model1 = new IDCInformationDTO();
		AjaxValidationResult  result = idcValidator.validateBean(model1);
		System.out.println(result.toString());
		
		/**
		 * 2.经营者字段长度校验
		 */
		IDCInformationDTO model2 = new IDCInformationDTO();
		//许可证号
		model2.setIdcId(LENGTH_65_CHINESE);
		//经营者名称
		model2.setIdcName(LENGTH_64_CHINESE);
		//企业法人
		model2.setCorporater(LENGTH_129_ENGLISH);
		//邮编
		model2.setIdcZipCode(LENGTH_6_ENGLISH);
		//地址
		model2.setIdcAddress(LENGTH_129_ENGLISH);
		//网络负责人名称
		model2.setOfficerName(LENGTH_33_ENGLISH);
		//证件号码
		model2.setOfficerId(LENGTH_32_ENGLISH);
		//证件类型
		//model2.setOfficerIdType(10);redis没有加载先不测试
		//责任人固定电话
		model2.setOfficerTelephone(TELILEPHONE_TEST_ERROR_2);
		//责任人移动电话
		model2.setOfficerMobile(MOBILEPHONE_TEST_ERROR);
		//责任人邮箱
		model2.setOfficerEmail(EMALIL_TEST_ERR_1);
		//应急人名称
		model2.setEcName(LENGTH_129_ENGLISH);
		//应急人证件号码
		model2.setEcId(LENGTH_32_ENGLISH);
		//应急人证件类型
		//TODO：;redis没有加载先不测试
		//应急人固定电话
		model2.setEcTelephone(TELILEPHONE_TEST_ERROR);
		//应急人移动电话
		model2.setEcMobile(MOBILEPHONE_TEST_SUCCESS);
		//应急人邮箱
		model2.setEcEmail(EMALIL_TEST_ERR_1);
		AjaxValidationResult  result2 = idcValidator.validateBean(model2);
		System.out.println(result2.toString());
	}
	/**
	 * 校验机房主体测试
	 * 
	 */
	@Test
	public void houseMainTest(){
		HousePrincipalValidatorImpl houseValidator = new HousePrincipalValidatorImpl();
		
		/**
		 * 1.机房空字段校验
		 */
		HouseInformationDTO model1 = new HouseInformationDTO();
		AjaxValidationResult result = houseValidator.validateBean(model1);
		System.out.println(result.toString()+"-"+result.getErrorsArgsMap().size());		
		
		/**
		 * 2.机房字段长度校验
		 */
		HouseInformationDTO model2 = new HouseInformationDTO();
		//机房名称
		model2.setHouseName(LENGTH_129_ENGLISH);
		//机房编号
		model2.setHouseIdStr(LENGTH_65_CHINESE);
		//专线标识
		//model2.setIdentity(2);
		//机房性质
		//model2.setHouseType(4);
		//机房地址
		model2.setHouseAddress(LENGTH_128_ENGLISH);
		//机房邮编
		model2.setHouseZipCode(LENGTH_7_ENGLISH);
		//机房所在省或直辖市
		model2.setHouseProvince(1234567);
		//机房所在市或区(县)
		model2.setHouseCity(12345678);
		//机房所在县
		model2.setHouseCounty(12345);
		//网络信息安全责任人姓名
		model2.setHouseOfficerName(LENGTH_33_ENGLISH);
		//网络信息安全责任人证件类型
		//model2.setHouseOfficerIdType(0);
		//网络信息安全责任人证件号码
		//model2.setHouseOfficerId("3600001");
		//网络信息安全责任人固定电话
		model2.setHouseOfficerTelephone(TELILEPHONE_TEST_ERROR_2);
		//网络信息安全责任人移动电话
		model2.setHouseOfficerMobile(MOBILEPHONE_TEST_ERROR_2);
		//网络信息安全责任人Email
		model2.setHouseOfficerEmail(LENGTH_65_CHINESE);
		//地市码
		AjaxValidationResult result2 = houseValidator.validateBean(model2);
		System.out.println(result2.toString()+"-"+result2.getErrorsArgsMap().size());	
	}
	
	/**
	 * 校验机房机架测试
	 * 
	 */
	@Test
	public void houseFrameTest(){
		HouseFrameValidatorImpl frameValidator = new HouseFrameValidatorImpl();
		/**
		 * 1.机房机架空字段校验
		 */
		HouseFrameInformationDTO model1 = new HouseFrameInformationDTO();
		AjaxValidationResult  result = frameValidator.validateBean(model1);
		System.out.println(result.toString()+"--"+result.getErrorsArgsMap().size());
		
		/**
		 * 2.机房机架字段长度校验
		 */
		HouseFrameInformationDTO model2 = new HouseFrameInformationDTO();
		//机房名称
		//机房机架
		model2.setFrameName(LENGTH_65_CHINESE);
		//分配状态
		//占用状态
		//使用类型
		//所属客户
		List<HouseUserFrameInformation> list = new ArrayList<>();
		HouseUserFrameInformation dto = new HouseUserFrameInformation();
		dto.setUserName("testUser");
		list.add(dto);
		model2.setUserFrameList(list);
		AjaxValidationResult result2 = frameValidator.validateBean(model2);
		System.out.println(result2.toString()+"-"+result2.getErrorsArgsMap().size());	
	}
	
	/**
	 * 校验机房链路测试
	 * 
	 */
	@Test
	public void houseLinkTest(){
		 HouseLinkValidatorImpl linkValidator = new HouseLinkValidatorImpl();
		/**
		 * 1.机房链路空字段校验
		 */
		 HouseGatewayInformationDTO model1 = new HouseGatewayInformationDTO();
		 AjaxValidationResult  result = linkValidator.validateBean(model1);
		 System.out.println(result.toString()+"--"+result.getErrorsArgsMap().size());	 
		 
		 /**
		  * 2.机房链路字段长度校验
		  */
		 
		 HouseGatewayInformationDTO model2 = new HouseGatewayInformationDTO();
		 //机房id
		 //链路编号
		 model2.setLinkNo(LENGTH_129_ENGLISH);
		 //出入口带宽
		 model2.setBandWidth(-1L);
		 //链路类型
		 //model2.setLinkType(1);
		 AjaxValidationResult result2 = linkValidator.validateBean(model2);
		 System.out.println(result2.toString()+"-"+result2.getErrorsArgsMap().size());
	}
	
	/**
	 * 校验机房IP地址段测试
	 * 
	 */
	@Test
	public void houseIPsegTest(){
		HouseIpSegmentValidatorImpl ipsegValidator = new HouseIpSegmentValidatorImpl();
		/**
		 * 机房IP地址段空字段校验
		 */
		/*HouseIPSegmentInforDTO model1 = new HouseIPSegmentInforDTO();
		AjaxValidationResult  result = ipsegValidator.validateBean(model1);
		System.out.println(result.toString()+"--"+result.getErrorsArgsMap().size());	
		
		*//**
		  * 2.机房IP地址段字段IPv4校验
		  *//*
		HouseIPSegmentInforDTO model2 = new HouseIPSegmentInforDTO();
		//机房id
		//起始ip
		model2.setStartIP("1.1.1.1.1");
		//终止ip
		model2.setEndIP("1.1.1.256");
		//ip地址使用方式
		//分配时间
		model2.setUseTime("2015-1-1");
		//用户单位
		model2.setUserName(LENGTH_65_CHINESE);
		AjaxValidationResult result2 = ipsegValidator.validateBean(model2);
		System.out.println(result2.toString()+"-"+result2.getErrorsArgsMap().size());
		
		*//**
		  * 3.机房IP地址段字段IPv4范围校验
		  *//*
		HouseIPSegmentInforDTO model3 = new HouseIPSegmentInforDTO();
		//机房id
		//起始ip
		model3.setStartIP("1.1.1.5");
		//终止ip
		model3.setEndIP("1.1.1.252");
		//ip地址使用方式
		//分配时间
		//用户单位
		AjaxValidationResult result3 = ipsegValidator.validateBean(model3);
		System.out.println(result3.toString()+"-"+result3.getErrorsArgsMap().size());
		*/
		/**
		  * 4.机房IP地址段起始终止联合校验
		  */
		HouseIPSegmentInforDTO model4 = new HouseIPSegmentInforDTO();
		//机房id
		//起始ip
		model4.setStartIP("1.1.1.5");
		//终止ip
		model4.setEndIP("1.1.1.4");
		//ip地址使用方式
		//分配时间
		//用户单位
		AjaxValidationResult result4 = ipsegValidator.validateBean(model4);
		System.out.println(result4.toString()+"---"+result4.getErrorsArgsMap().size());
		
		/**
		 * 5.机房IP地址段IPV6校验
		 */
		HouseIPSegmentInforDTO model5 = new HouseIPSegmentInforDTO();
		//机房id
		//起始ip
		model5.setStartIP("AD80:0000:0000:0000:ABAA:0000:00C2:0002");
		//终止ip
		model5.setEndIP("AD80:0000:0000:0000:ABAA:0000:00C1:0002");
		//ip地址使用方式
		
		//分配时间
		
		//用户单位
		AjaxValidationResult result5 = ipsegValidator.validateBean(model5);
		System.out.println(result5.toString()+"---"+result5.getErrorsArgsMap().size());
	}
	
	/**
	 * 6.校验用户主体测试
	 * 
	 */
	@Test
	public void userManinInfoTest(){
		UserPrincipalValidatorImpl validator = new UserPrincipalValidatorImpl();
		/**
		 * 1.空子段校验
		 */
		UserInformationDTO model1 = new UserInformationDTO();
		AjaxValidationResult  result = validator.validateBean(model1);
		System.out.println(result.toString()+"--"+result.getErrorsArgsMap().size());
		
		/**
		 * 2.数据库长度校验
		 */
		UserInformationDTO model2 = new UserInformationDTO();
		//经营者id
		//d单位名称 
		model2.setUnitName(LENGTH_129_ENGLISH);
		// 用户编号
		model2.setUserCode(LENGTH_65_CHINESE);
		//用户属性
		//用户类型
		//单位属性
		//单位证件类型
		//单位证件号码
		model2.setIdNumber(LENGTH_33_ENGLISH);
		//单位所在省
		model2.setUnitAddressProvinceCode("00000");
		//单位所在市 
		model2.setUnitAddressCityCode("0000000");
		//单位所在县 
		model2.setUnitAddressAreaCode("0000000");
		//责任人名称
		model2.setOfficerName(LENGTH_33_ENGLISH);
		//责任人证件类型 
		//责任人证件号码 
		model2.setOfficerId(LENGTH_33_ENGLISH);
		//责任人固定电话 
		model2.setOfficerTelphone(TELILEPHONE_TEST_ERROR_2);
		//责任人移动电话 
		model2.setOfficerMobile(MOBILEPHONE_TEST_ERROR_2);
		//责任人 邮箱
		model2.setOfficerEmail(EMALIL_TEST_ERR_1);
		//责任人地市码
		model2.setAreaCode("123456");
		AjaxValidationResult  result2 = validator.validateBean(model2);
		System.out.println(result2.toString()+"--"+result2.getErrorsArgsMap().size());
	}
	
	/**
	 * 7.校验用户服务测试
	 * 
	 */
	@Test
	public void userServiceInfoTest(){
		/**
		 * 1.空子段校验
		 */
		UserServiceValidatorImpl validator = new UserServiceValidatorImpl();
		UserServiceInformationDTO model1 = new UserServiceInformationDTO();
		AjaxValidationResult  result = validator.validateBean(model1);
		System.out.println(result.toString()+"--"+result.getErrorsArgsMap().size());
		/**
		 * 2.数据库长度校验
		 */
		UserServiceInformationDTO model2 = new UserServiceInformationDTO();
		//用户ID
		//服务内容
		//model2.setServiceContent("2,3");
		//应用服务类型
		//业务类型
		//域名
		List<ServiceDomainInformation> domainList = new ArrayList<>();
		ServiceDomainInformation domain = new ServiceDomainInformation();
		domain.setDomainName("http://是.是-是.2-2;");
		domainList.add(domain);
		model2.setDomainList(domainList);
		//网站备案类型
		//备案号或许可证号
		model2.setRegisterId(LENGTH_65_CHINESE);
		//接入方式
		//地市码
		model2.setAreaCode("555000");


		AjaxValidationResult  result2 = validator.validateBean(model2);
		System.out.println(result2.toString()+"--"+result2.getErrorsArgsMap().size());
		
	}
	
	/**
	 * 8.校验用户网络资源测试
	 * 
	 */
	@Test
	public void userHHInfoTest(){
		/**
		 * 1.空子段校验
		 */
		UserNetworkValidatorImpl validator = new UserNetworkValidatorImpl();
		UserBandwidthInformationDTO model1 = new UserBandwidthInformationDTO();
		AjaxValidationResult  result = validator.validateBean(model1);
		System.out.println(result.toString()+"--"+result.getErrorsArgsMap().size());
		/**
		 * 2.数据库长度校验
		 *
		*/
		UserBandwidthInformationDTO model2 = new UserBandwidthInformationDTO();
		//用户ID
		//机房ID
		//带宽
		model2.setBandWidth(3L);
		//分配时间
		model2.setDistributeTime("2014-12-12");
		//地市码
		model2.setAreaCode("000");
		AjaxValidationResult  result2 = validator.validateBean(model2);
		System.out.println(result2.toString()+"--"+result2.getErrorsArgsMap().size());
		


		
	}
	
	/**
	 * 9.校验用户虚拟机测试
	 * 
	 */
	@Test
	public void userHHVirtualInfoTest(){
		/**
		 * 1.空子段校验
		 */
		UserVirtualMachineValidatorImpl validator = new UserVirtualMachineValidatorImpl();
		UserVirtualInformation model1 = new UserVirtualInformation();
		AjaxValidationResult  result = validator.validateBean(model1);
		System.out.println(result.toString()+"--"+result.getErrorsArgsMap().size());
		/**
		 * 2.数据库长度校验
		 */
		UserVirtualInformation model2 = new UserVirtualInformation();
		//用户ID
		//机房ID
		//虚拟主机编号
		model2.setName(LENGTH_129_ENGLISH);

		//虚拟主机名
		model2.setVirtualNo(LENGTH_65_CHINESE);

		//虚拟主机网络地址
		model2.setNetworkAddress("1.1.1.257");

		//虚拟主机管理地址
		model2.setMgnAddress("AD80:0000:0000:0000:ABAA:0000:00C2:0002");

		//虚拟主机状态
		//虚拟主机类型
		//地市码
		model2.setAreaCode(LENGTH_64_CHINESE);
		AjaxValidationResult  result2 = validator.validateBean(model2);
		System.out.println(result2.toString()+"--"+result2.getErrorsArgsMap().size());

	}
}
