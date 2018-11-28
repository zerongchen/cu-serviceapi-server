package com.aotain.serviceapi.server.constant;

/**
 * 
 * @author Jason
 *
 */
public enum CheckFieldType {
	
	XKZH(0),
	JYZMC(1),
	ADDRESS(2),
	POST(3),
	RYZJLX(4),
	ZJLX(5),
	ZJHM(6),
	TELEPHONE(7),
	MOBILEPHONE(8),
	EMAIL(9),
	JFMC(10),
	JFXZ(11),
	SJXZQHDM(12),
	QJXZQHDM(13),
	XJXZQHDM(14),
	FRAME_DISTRIBUTION(15),
	FRAME_OCCUPANCY(16),
	FRAME_USERTYPE(17),
	BANDWIDTH(18),
	LINK_TYPE(19),
	IP_ADDR(20),
	IP_TYPE(21),
	SOURCEUNIT(22),
	ALLOCATIONUNIT(23),
	DATE(24),
	DWSX(25),
	FWNR(26),
	YYFWLX(27),
	BUSINESS(28),
	DOMAIN(29),
	REGID(30),
	JRFS(31),
	VIRTUALHOST_TYPE(32),
	VIRTUALHOST_STATE(33);
	
	private int value;
	
	private CheckFieldType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
