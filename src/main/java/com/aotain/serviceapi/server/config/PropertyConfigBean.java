package com.aotain.serviceapi.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/11/21
 */
@Configuration
public class PropertyConfigBean {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
