/*package com.aotain.serviceapi.server.dao;

import com.aotain.cu.serviceapi.model.HouseGatewayInformation;
import com.aotain.serviceapi.server.BaseTest;
import com.aotain.serviceapi.server.dao.preinput.HouseLinkMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

*//**
 * Demo class
 *
 * @author bang
 * @date 2018/07/23
 *//*
public class HouseLinkMapperTest extends BaseTest{

    @Autowired
    private HouseLinkMapper houseLinkMapper;

    @Test
    @Rollback(value = false)
    public void testSave(){
        HouseGatewayInformation houseGatewayInformation = houseLinkMapper.findByLinkNo("test_idcLink001");
        houseGatewayInformation.setHouseId(823L);
        houseGatewayInformation.setLinkType(2);
        houseGatewayInformation.setAccessUnit("测试aaa");
        houseGatewayInformation.setLinkNo("ia-dba-dd");
        houseGatewayInformation.setGatewayId(10426L);
        houseLinkMapper.insertSelective(houseGatewayInformation);
    }

    @Test
    public void testUpdate(){
        HouseGatewayInformation houseGatewayInformation = houseLinkMapper.findByLinkNo("test_idcLink001");
        houseGatewayInformation.setLinkType(2);
        houseLinkMapper.updateHouseGatewayByLinkNo(houseGatewayInformation);
    }
}
*/