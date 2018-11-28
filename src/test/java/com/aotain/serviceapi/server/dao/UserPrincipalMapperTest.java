/*package com.aotain.serviceapi.server.dao;

import com.aotain.cu.serviceapi.model.UserInformation;
import com.aotain.serviceapi.server.BaseTest;
import com.aotain.serviceapi.server.dao.preinput.UserPrincipalMapper;
import com.aotain.serviceapi.server.util.SpringUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

*//**
 * Demo class
 *
 * @author bang
 * @date 2018/07/23
 *//*
public class UserPrincipalMapperTest extends BaseTest{

    @Autowired
    private UserPrincipalMapper userPrincipalMapper;

    @Test
    public void test(){
        UserInformation userInformation = new UserInformation();
        userInformation.setUserId(30303L);
        userInformation.setUpdateTime(new Date());
        userInformation.setUpdateUserId(SpringUtil.getCurrentUserId().intValue());
        userInformation.setDealFlag(1);
        userPrincipalMapper.updateDealFlagByUserId(userInformation);
    }
}
*/