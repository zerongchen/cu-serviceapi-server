package com.aotain.serviceapi.server.config;

import com.aotain.serviceapi.server.filter.HttpBasicAuthorizeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/11/20
 */
@Configuration
public class FilterConfigBean {
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpBasicAuthorizeFilter());
        registration.addUrlPatterns("/*");
        registration.setName("authFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
}
