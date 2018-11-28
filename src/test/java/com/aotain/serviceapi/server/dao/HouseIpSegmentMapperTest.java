package com.aotain.serviceapi.server.dao;

import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;
import com.aotain.serviceapi.server.BaseTest;
import com.aotain.serviceapi.server.dao.preinput.HouseIpSegmentMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/23
 */
public class HouseIpSegmentMapperTest extends BaseTest{

    @Autowired
    private HouseIpSegmentMapper houseIpSegmentMapper;

    @Test
    public void test(){
        HouseIPSegmentInformation houseIPSegmentInformation = houseIpSegmentMapper.findByIpSegNo("ZXIP276933");

        houseIPSegmentInformation.setUserName("bangggg");
        houseIpSegmentMapper.updateHouseIpSegment(houseIPSegmentInformation);
    }

}
