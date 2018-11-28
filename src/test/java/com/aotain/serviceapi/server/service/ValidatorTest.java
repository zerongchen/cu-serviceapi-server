package com.aotain.serviceapi.server.service;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.IDCInformationDTO;
import com.aotain.serviceapi.server.ServiceapiServerApplication;
import com.aotain.serviceapi.server.validate.impl.IdcInformationValidatorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceapiServerApplication.class)
public class ValidatorTest {

    @Autowired
    private IdcInformationValidatorImpl idcInformationValidator;

    @Test
    public void test(){
        IDCInformationDTO idcInformationDTO = new IDCInformationDTO();
        AjaxValidationResult ajaxValidationResult = idcInformationValidator.preValidate(idcInformationDTO);
        System.out.println(ajaxValidationResult+"===============");
    }
}
