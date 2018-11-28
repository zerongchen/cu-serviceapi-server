package com.aotain.serviceapi.server.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.ServiceapiServerApplication;
import com.aotain.serviceapi.server.dao.report.RptBaseUserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)
public class RptUserDaoTest {
	@Autowired
    private RptBaseUserMapper rptBaseUserMapper;
	
	@Test
	public void testQueryUser(){
		UserInformation info = rptBaseUserMapper.selectByPrimaryKey(116929L);
		System.out.println(info);
	}
}
