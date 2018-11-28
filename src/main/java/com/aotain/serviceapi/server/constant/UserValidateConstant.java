package com.aotain.serviceapi.server.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/16
 */
@Getter
@Setter
public class UserValidateConstant {

    /**
     * 接入方式常量枚举类
     */
    public enum SetMode{

        DEDICATED("专线",0),
        VIRTUAL("虚拟主机",1),
        HOSTING("主机托管",2),
        RENDING("整机租用",3),
        OTHER("其它",999);
        @Getter
        @Setter
        private String status;
        @Getter
        @Setter
        private Integer value;
        SetMode(String status,int value){
            this.status = status;
            this.value = value;
        }
    }

    public enum ServiceType{
        IDC("内部应用",0),
        ISP("电信对外应用",1);

        @Getter
        @Setter
        private String status;
        @Getter
        @Setter
        private Integer value;
        ServiceType(String status,int value){
            this.status = status;
            this.value = value;
        }
    }

    public enum BusinessType{
        IDC("IDC业务",1),
        ISP("ISP业务",2);

        @Getter
        @Setter
        private String status;
        @Getter
        @Setter
        private Integer value;
        BusinessType(String status,int value){
            this.status = status;
            this.value = value;
        }
    }
    
	public enum IdentifyType {
		IDC_USER("IDC用户", "1"), 
		ISP_USER("ISP用户", "2"), 
		IDCISP_USER("IDC/ISP用户", "3"), 
		CDN_USER("CDN用户", "4"), 
		DEDICATED_USER("专线用户", "5");

		@Getter
		@Setter
		private String status;
		@Getter
		@Setter
		private String value;

		IdentifyType(String status, String value){
            this.status = status;
            this.value = value;
        }
	}
}
