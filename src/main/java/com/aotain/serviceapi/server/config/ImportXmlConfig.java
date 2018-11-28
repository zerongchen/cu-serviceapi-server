package com.aotain.serviceapi.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations= {"classpath*:spring-application.xml"})
public class ImportXmlConfig {
}
