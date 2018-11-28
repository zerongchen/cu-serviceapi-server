package com.aotain.serviceapi.server.util;

import com.aotain.common.config.ContextUtil;
import com.aotain.serviceapi.server.dao.RegexMapper;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/16
 */
public class RegexUtils {

    /**
     * 根据subType获取对应的正则表达式
     * @param subType
     * @return
     */
    public static String getRegexValueBySubType(int subType){
        RegexMapper regexMapper = ContextUtil.getContext().getBean(RegexMapper.class);
        return regexMapper.getValueBySubType(subType);
    }
}
