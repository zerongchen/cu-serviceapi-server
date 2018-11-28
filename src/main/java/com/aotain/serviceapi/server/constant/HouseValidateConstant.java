package com.aotain.serviceapi.server.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 机房校验相关常量类
 *
 * @author bang
 * @date 2018/07/13
 */
@Getter
@Setter
public class HouseValidateConstant {

    public enum HouseOccupancyType{

        UNASSIGN("未分配",1),
        ASSIGN("分配",2);

        @Getter
        @Setter
        private String status;
        @Getter
        @Setter
        private Integer value;
        HouseOccupancyType(String status,int value){
            this.status = status;
            this.value = value;
        }
    }

    public enum IpSegmentUseType{
        STATIC("静态",0),
        DYNAMIC("动态",1),
        REMAIN("保留",2),
        SPECIAL("专线",3),
        CLOUD_VIRTUAL("云虚拟",999);


        @Getter
        @Setter
        private String status;
        @Getter
        @Setter
        private Integer value;
        IpSegmentUseType(String status,int value){
            this.status = status;
            this.value = value;
        }
    }
}
