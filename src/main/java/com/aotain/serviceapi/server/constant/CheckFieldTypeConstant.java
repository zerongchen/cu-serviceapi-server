package com.aotain.serviceapi.server.constant;

import java.util.HashMap;
import java.util.Map;

public class CheckFieldTypeConstant {
	
	public static final int XKZH	 			= 0;//许可证号
	public static final int JYZMC 				= 1;//经营者名称
	public static final int ADDRESS 			= 2;//通信地址
	public static final int POST 				= 3;//通信地址邮编
	public static final int RYZJLX 				= 4;//人员证件类型
	public static final int ZJLX 				= 5;//证件类型
	public static final int ZJHM 				= 6;//证件号码
	public static final int TELEPHONE 			= 7;//固定电话
	public static final int MOBILEPHONE 		= 8;//移动电话
	public static final int EMAIL 				= 9;//联系人Email
	public static final int JFMC 				= 10;//机房名称
	public static final int JFXZ 				= 11;//机房性质
	public static final int SJXZQHDM 			= 12;//省/直辖市级行政区划代码
	public static final int QJXZQHDM 			= 13;//市/区级行政区划代码
	public static final int XJXZQHDM 			= 14;//县级行政区划代码
	public static final int FRAME_DISTRIBUTION 	= 15;//机架分配状态
	public static final int FRAME_OCCUPANCY 	= 16;//机架占用状态
	public static final int FRAME_USERTYPE 		= 17;//机架使用类型
	public static final int BANDWIDTH 			= 18;//带宽
	public static final int LINK_TYPE 			= 19;//链路类型
	public static final int IP_ADDR 			= 20;//IP地址
	public static final int IP_TYPE 			= 21;//IP地址使用方式
	public static final int SOURCEUNIT 			= 22;//来源单位
	public static final int ALLOCATIONUNIT 		= 23;//分配单位
	public static final int DATE 				= 24;//日期
	public static final int DWSX 				= 25;//单位属性
	public static final int FWNR 				= 26;//服务内容
	public static final int YYFWLX 				= 27;//应用服务类型
	public static final int BUSINESS 			= 28;//业务类型
	public static final int DOMAIN 				= 29;//域名
	public static final int REGID 				= 30;//备案号或许可证号
	public static final int JRFS 				= 31;//接入方式
	public static final int VIRTUALHOST_TYPE 	= 32;//虚拟主机类型
	public static final int VIRTUALHOST_STATE 	= 33;//虚拟主机状态
	
	public static final Map<String, String> ZJLX_MAP 					= new HashMap<String, String>(4); 
	public static final Map<String, String> RYZJLX_MAP 					= new HashMap<String, String>(4); 
	public static final Map<String, String> JFXZ_MAP   					= new HashMap<String, String>(3); 
	public static final Map<String, String> FRAME_DISTRIBUTION_MAP   	= new HashMap<String, String>(2); 
	public static final Map<String, String> FRAME_OCCUPANCY_MAP   		= new HashMap<String, String>(2);
	public static final Map<String, String> FRAME_USERTYPE_MAP   		= new HashMap<String, String>(2);
	public static final Map<String, String> LINK_TYPE_MAP   			= new HashMap<String, String>(4);
	public static final Map<String, String> IP_TYPE_MAP   				= new HashMap<String, String>(3);
	public static final Map<String, String> DWSX_MAP   					= new HashMap<String, String>(7);
	public static final Map<String, String> FWNR_MAP   					= new HashMap<String, String>();
	public static final Map<String, String> YYFWLX_MAP   				= new HashMap<String, String>(2);
	public static final Map<String, String> BUSINESS_MAP   				= new HashMap<String, String>(2);
	public static final Map<String, String> JRFS_MAP   					= new HashMap<String, String>(5);
	public static final Map<String, String> VIRTUALHOST_TYPE_MAP   		= new HashMap<String, String>(3);
	public static final Map<String, String> VIRTUALHOST_STATE_MAP   	= new HashMap<String, String>(3);
	
	static {
		
		ZJLX_MAP.put("1", "工商营业执照号码");
		ZJLX_MAP.put("2", "身份证");
		ZJLX_MAP.put("3", "组织机构代码证书");
		ZJLX_MAP.put("4", "事业法人证书");
		ZJLX_MAP.put("5", "军队代号");
		ZJLX_MAP.put("6", "社团法人证书");
		ZJLX_MAP.put("7", "护照");
		ZJLX_MAP.put("8", "军官证");
		ZJLX_MAP.put("9", "台胞证");
		ZJLX_MAP.put("999", "其他");
		
		RYZJLX_MAP.put("2", "身份证");
		RYZJLX_MAP.put("7", "护照");
		RYZJLX_MAP.put("8", "军官证");
		RYZJLX_MAP.put("9", "台胞证");
		
		JFXZ_MAP.put("1", "租用");
		JFXZ_MAP.put("2", "自建");
		JFXZ_MAP.put("999", "其他");
		
		FRAME_DISTRIBUTION_MAP.put("1", "未分配");
		FRAME_DISTRIBUTION_MAP.put("2", "已分配");
		
		FRAME_OCCUPANCY_MAP.put("1", "未占用");
		FRAME_OCCUPANCY_MAP.put("2", "已占用");
		
		FRAME_USERTYPE_MAP.put("1", "自用");
		FRAME_USERTYPE_MAP.put("2", "出租");
		
		LINK_TYPE_MAP.put("1", "电信");
		LINK_TYPE_MAP.put("2", "联通");
		LINK_TYPE_MAP.put("3", "移动");
		LINK_TYPE_MAP.put("4", "铁通");
		
		IP_TYPE_MAP.put("0", "静态");
		IP_TYPE_MAP.put("1", "动态");
		IP_TYPE_MAP.put("2", "保留");
		
		DWSX_MAP.put("1", "军队");
		DWSX_MAP.put("2", "政府机关");
		DWSX_MAP.put("3", "事业单位");
		DWSX_MAP.put("4", "企业");
		DWSX_MAP.put("5", "个人");
		DWSX_MAP.put("6", "社会团体");
		DWSX_MAP.put("999", "其他");
		
		FWNR_MAP.put("1", "即时通信");
		FWNR_MAP.put("2", "搜索引擎");
		FWNR_MAP.put("3", "综合门户");
		FWNR_MAP.put("4", "网上邮局");
		FWNR_MAP.put("5", "网络新闻");
		FWNR_MAP.put("6", "博客/个人空间");
		FWNR_MAP.put("7", "网络广告/信息");
		FWNR_MAP.put("8", "单位门户网站");
		FWNR_MAP.put("9", "网络购物");
		FWNR_MAP.put("10", "网上支付");
		FWNR_MAP.put("11", "网上银行");
		FWNR_MAP.put("12", "网上炒股/股票基金");
		FWNR_MAP.put("13", "网络游戏");
		FWNR_MAP.put("14", "网络音乐");
		FWNR_MAP.put("15", "网络影视");
		FWNR_MAP.put("16", "网络图片");
		FWNR_MAP.put("17", "网络软件/下载");
		FWNR_MAP.put("18", "网上求职");
		FWNR_MAP.put("19", "网上交友/婚介");
		FWNR_MAP.put("20", "网上房产");
		FWNR_MAP.put("21", "网络教育");
		FWNR_MAP.put("22", "网站建设");
		FWNR_MAP.put("23", "WAP");
		FWNR_MAP.put("24", "其他");
		
		YYFWLX_MAP.put("0", "内部应用");
		YYFWLX_MAP.put("1", "电信业务/对外应用服务");
		
		BUSINESS_MAP.put("1", "IDC业务");
		BUSINESS_MAP.put("2", "ISP业务");
		
		JRFS_MAP.put("0", "专线");
		JRFS_MAP.put("1", "虚拟主机");
		JRFS_MAP.put("2", "主机托管");
		JRFS_MAP.put("3", "主机托管");
		JRFS_MAP.put("999", "其他");
		
		VIRTUALHOST_TYPE_MAP.put("1", "共享式");
		VIRTUALHOST_TYPE_MAP.put("2", "专用式");
		VIRTUALHOST_TYPE_MAP.put("3", "云虚拟");
		
		VIRTUALHOST_STATE_MAP.put("1", "运行");
		VIRTUALHOST_STATE_MAP.put("2", "挂起");
		VIRTUALHOST_STATE_MAP.put("3", "关机");
		
	}
	
}
