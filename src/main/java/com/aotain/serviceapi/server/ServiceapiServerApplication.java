package com.aotain.serviceapi.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@EnableEurekaClient
public class ServiceapiServerApplication {
	private static Logger logger = Logger.getLogger(ServiceapiServerApplication.class);
	
	@Value("${app.version}")
	private static String appVersion;
	
    public static void main(String[] args) {
    	logger.info("ServicApi version is "+appVersion);
        SpringApplication.run(ServiceapiServerApplication.class, args);
    }
}
