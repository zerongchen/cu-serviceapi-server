package com.aotain.serviceapi.server.service;

import com.aotain.cu.serviceapi.model.*;
import com.aotain.serviceapi.server.ServiceapiServerApplication;
import com.aotain.serviceapi.server.dao.DemoMapper;
import com.aotain.serviceapi.server.dao.report.*;
import com.aotain.serviceapi.server.service.report.IdcInfoService;
import com.aotain.serviceapi.server.service.report.RptUserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author chenzr
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)
public class DemoTest {

/*    @Autowired
    private DemoMapper demoMapper;*/

    @Autowired
    private IdcInfoService infoService;

    @Autowired
    private RptBaseUserMapper baseUserMapper;

    @Autowired
    private RptBaseServiceDomainMapper rptBaseServiceDomainMapper;

    @Autowired
    private RptUserBandWidthMapper rptUserBandWidthMapper;

    @Autowired
    private RptUserServiceMapper rptUserServiceMapper;

    @Autowired
    private RptUserVirtualMapper rptUserVirtualMapper;

    @Autowired
    private RptUserInfoService rptUserInfoService;

/*    @Test
    public void test(){


        String s = demoMapper.getOne();
        System.out.println(s);

    }*/

    @Test
    public void test01(){

        IdcInformation info = new IdcInformation();
        info.setJyzId(8);
        info.setIdcId("idc");
        info.setIdcName("idc");
        info.setIdcAddress("idc add");
        info.setIdcZipCode("440000");
        info.setCorporater("ope");
        info.setOfficerName("office1");
        info.setOfficerId("1");
        info.setOfficerIdType(1);
        info.setOfficerTelephone("558858");
        info.setOfficerEmail("@163");
        info.setOfficerMobile("0755");
        info.setEcName("ecName");
        info.setEcId("0");
        info.setEcEmail("@163");
        info.setEcIdType(1);
        info.setEcTelephone("000");
        info.setEcMobile("888");
//        infoMapper.insert(info);
        UserInformation record = new UserInformation();
        record.setUserId((long)1);
        info.setUserList( Arrays.asList(record));
        try {
            infoService.add(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void test02(){
//        UserInformation record = new UserInformation();
//        record.setUserId((long)1);
//        record.setCreateUserId(1);
//        record.setUpdateUserId(1);
//        record.setUserCode("12");
//        record.setUnitZipCode("333");
//        record.setUnitZipCode("232");
//        record.setUnitNature(111);
//        record.setUnitName("ddd");
//        record.setUnitAddress("333");
//        record.setServiceRegTime("23232");
//        record.setRegisteTime("23243");
//        record.setOfficerTelphone("2324324");
//        record.setOfficerName("erer");
//        record.setOfficerMobile("erer");
//        record.setOfficerIdType(222);
//        record.setOfficerEmail("eeeee");
//        record.setOfficerId("33");
//        record.setJyzId(1);
//        record.setIdType(11);
//        record.setIdNumber("erer");
//        record.setNature(11);
////        baseUserMapper.insert(record);
//
//
//        List<UserServiceInformation > record1 = new ArrayList<UserServiceInformation>();
//        for(int i=1;i<3;i++){
//            UserServiceInformation service = new UserServiceInformation();
//            service.setBusiness(1);
//            service.setRegisterId("111");
//            service.setRegType(1);
//            service.setServiceContent("333");
//            service.setServiceId((long)i);
//            service.setServiceType(1);
//            service.setSetmode(1);
//            service.setCreateUserId(1);
//            service.setUpdateUserId(1);
//            service.setCreateTime(new Date());
//            service.setUpdateTime(new Date());
//            service.setUserId((long)1);
//            if(i==1){
//                List<ServiceDomainInformation> record2 = new ArrayList<>();
//                for(int j=1;j<3;j++){
//                    ServiceDomainInformation domain = new ServiceDomainInformation();
//                    domain.setDomainId((long)j);
//                    domain.setDomainName("ddd"+j);
//                    domain.setOperationType(1);
//                    domain.setServiceId((long)1);
//                    domain.setUserId((long)1);
//                    domain.setCreateUserId(1);
//                    domain.setUpdateUserId(1);
//                    domain.setCreateTime(new Date());
//                    domain.setUpdateTime(new Date());
//                    record2.add(domain);
//                }
//                service.setDomainList(record2);
//            }
//            record1.add(service);
//        }
//
////        rptUserServiceMapper.insertList(record);
//
///*        List<ServiceDomainInformation> record2 = new ArrayList<>();
//        for(int i=1;i<3;i++){
//            ServiceDomainInformation domain = new ServiceDomainInformation();
//            domain.setDomainId((long)i);
//            domain.setDomainName("ssss");
//            domain.setOperationType(1);
//            domain.setServiceId((long)1);
//            domain.setUserId((long)1);
//            domain.setCreateUserId(1);
//            domain.setUpdateUserId(1);
//            domain.setCreateTime(new Date());
//            domain.setUpdateTime(new Date());
//            record2.add(domain);
//        }*/
////        rptBaseServiceDomainMapper.insertList(record);
//        record.setServiceList(record1);
//
//        List<UserBandwidthInformation> record3 = new ArrayList<>();
//        for(int i=1;i<3;i++){
//            UserBandwidthInformation ban = new UserBandwidthInformation();
//            ban.setBandWidth((long)1);
//            ban.setDistributeTime("20180712");
//            ban.setHhId((long)i);
//            ban.setUserId((long)1);
//            ban.setHouseId((long)1);
//            ban.setUserId((long)1);
//            ban.setCreateUserId(1);
//            ban.setUpdateUserId(1);
//            ban.setCreateTime(new Date());
//            ban.setUpdateTime(new Date());
//            record3.add(ban);
//        }
////        rptUserBandWidthMapper.insertList(record);
//        record.setBandwidthList(record3);
//
//        List<UserVirtualInformation> record4 = new ArrayList<>();
//        for(int i=1;i<3;i++){
//            UserVirtualInformation vit = new UserVirtualInformation();
//            vit.setHhId((long)1);
//            vit.setHouseId((long)1);
//            vit.setMgnAddress("112");
//            vit.setName("wwwww");
//            vit.setNetworkAddress("2343");
//            vit.setStatus(1);
//            vit.setUserId((long)1);
//            vit.setVirtualId((long)i);
//            vit.setCreateUserId(1);
//            vit.setUpdateUserId(1);
//            vit.setCreateTime(new Date());
//            vit.setUpdateTime(new Date());
//            record4.add(vit);
//        }
////        rptUserVirtualMapper.insertList(record);
//        record.setVirtualList(record4);
//        try {
//            rptUserInfoService.modify(record);
//        }catch (Exception e){
//        e.printStackTrace();
//        }
//
//    }
}
