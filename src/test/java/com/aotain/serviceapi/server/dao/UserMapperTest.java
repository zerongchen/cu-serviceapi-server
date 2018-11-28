package com.aotain.serviceapi.server.dao;

import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.ServiceapiServerApplication;
import com.aotain.serviceapi.server.dao.preinput.UserInfoMapper;
import com.aotain.serviceapi.server.dao.report.RptBaseUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)
public class UserMapperTest {
	@Autowired
    private UserInfoMapper mapper;
	
	@Test
	public void testQueryUser(){

		List<UserInformation> list = new ArrayList<>();
		for (int i=0;i<5;i++){
			UserInformation userInformation = new UserInformation();
			userInformation.setJyzId(1);
			userInformation.setUserCode(String.valueOf(i));
			userInformation.setNature(1);
			userInformation.setUnitName(String.valueOf(i));
			userInformation.setUnitNature(1);
			userInformation.setIdType(1);
			userInformation.setIdNumber(String.valueOf(i));
			userInformation.setOfficerName(String.valueOf(i));
			userInformation.setOfficerEmail(String.valueOf(i));
			userInformation.setOfficerMobile(String.valueOf(i));
			userInformation.setOfficerTelphone(String.valueOf(i));
			userInformation.setOfficerId(String.valueOf(i));
			userInformation.setOfficerIdType(i);
			userInformation.setUnitAddress(String.valueOf(i));
			userInformation.setUnitZipCode(String.valueOf(i));
			userInformation.setRegisteTime("2018-10-10");
			list.add(userInformation);
		}
//		mapper.insertUserInfos(list);
	}
}
