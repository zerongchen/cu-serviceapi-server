package com.aotain.serviceapi.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aotain.cu.serviceapi.dto.ResultDto;
import com.aotain.cu.serviceapi.model.args.ReqSynDelHouseToPassport;
import com.aotain.serviceapi.server.ServiceapiServerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)
public class AuthroitySynchTest {

	@Autowired
	private SynAuthroityService service;
	
	@Test
	public void test(){
		ReqSynDelHouseToPassport req = new ReqSynDelHouseToPassport();
		req.setAppId(62L);
		req.setHouseId(4597L);
		req.setSynDelUrl("http://192.168.50.152:8060/rest/data/permissionareas/settings/delete");
		req.setSynQueryUrl("http://192.168.50.152:8060/rest/data/permissionareas");
		ResultDto rs = service.synDelHouse(req);
		System.out.println(rs);
	}
}
