package com.aotain.serviceapi.server.util;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/20
 */
//TODO
public class SpringUtil {
    /**
     * 获取当前登录用户的id
     * @return
     */
    public static Long getCurrentUserId(){
        Long[] arr = new Long[]{30303L,30292L,30296L,30269L};
        int i = (int)Math.round(Math.random()*4);
        return arr[i];
    }

    public static void main(String[] args) {
        System.out.println(Math.random()*4);
    }
}
