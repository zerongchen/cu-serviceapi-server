/*package com.aotain.serviceapi.server.dao;

import com.aotain.cu.serviceapi.dto.HouseInformationDTO;
import com.aotain.cu.serviceapi.model.HouseInformation;
import com.aotain.cu.serviceapi.model.IdcJcdmXzqydm;
import com.aotain.serviceapi.server.BaseTest;
import com.aotain.serviceapi.server.dao.preinput.HousePrincipalMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

*//**
 * Demo class
 *
 * @author bang
 * @date 2018/07/23
 *//*
public class HousePrincipalMapperTest extends BaseTest{

    @Autowired
    private HousePrincipalMapper housePrincipalMapper;

    @Autowired
    private IdcJcdmXzqydmMapper idcJcdmXzqydmMapper;

    @Test
    public void test(){
        HouseInformation houseInformation = new HouseInformation();
        houseInformation.setHouseId(4244L);
        houseInformation.setCzlx(1);
        housePrincipalMapper.updateHouseDealFlagByHouseId(houseInformation);
    }

    @Test
    public void testSelect(){
        HouseInformationDTO houseInformation = new HouseInformationDTO();
        houseInformation.setHouseId(4244L);
        HouseInformationDTO houseInformation1 = housePrincipalMapper.findByHouseId(houseInformation);
        System.out.println(houseInformation1);
    }

    @Test
    public void testaa(){
        IdcJcdmXzqydm idcJcdmXzqydm = idcJcdmXzqydmMapper.getXzqydmCodeByCode("440100");
        System.out.println(idcJcdmXzqydm);
    }
}
*/