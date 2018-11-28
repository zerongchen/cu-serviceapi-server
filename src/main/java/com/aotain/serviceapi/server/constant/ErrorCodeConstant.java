package com.aotain.serviceapi.server.constant;

import com.aotain.serviceapi.server.util.PropertyTool;

public enum ErrorCodeConstant {
	
	ERROR_1001(1001,getErrProperInfo(1001)),
	ERROR_1002(1002,getErrProperInfo(1002)),
	ERROR_1003(1003,getErrProperInfo(1003)),
	ERROR_1004(1004,getErrProperInfo(1004)),
	ERROR_1005(1005,getErrProperInfo(1005)),
	ERROR_1006(1006,getErrProperInfo(1006)),
	ERROR_1007(1007,getErrProperInfo(1007)),
	ERROR_1008(1008,getErrProperInfo(1008)),
	ERROR_1009(1009,getErrProperInfo(1009)),
	ERROR_1010(1010,getErrProperInfo(1010)),
	ERROR_1011(1011,getErrProperInfo(1011)),
	ERROR_1012(1012,getErrProperInfo(1012)),
	ERROR_1013(1013,getErrProperInfo(1013)),
	ERROR_1014(1014,getErrProperInfo(1014)),
	ERROR_1015(1015,getErrProperInfo(1015)),
	ERROR_1016(1016,getErrProperInfo(1016)),
	ERROR_1017(1017,getErrProperInfo(1017)),
	ERROR_1018(1018,getErrProperInfo(1018)),
	ERROR_1019(1019,getErrProperInfo(1019));
	
	private Integer errorCode;
	private String errorMsg;
	
	
	
	private ErrorCodeConstant(Integer errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public static String getErrMsgByErrCode(int code) {
        for (ErrorCodeConstant c : ErrorCodeConstant.values()) {
            if (c.getErrorCode() == code) {
                return c.errorMsg;
            }
        }
        return null;
    }

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	} 
	
	public static String getErrProperInfo(int code){
		return PropertyTool.instance.getPropValue("ERROR_"+code);
	}
	
	
	public static void main(String[] args) {
		System.out.println(ErrorCodeConstant.ERROR_1009.getErrorMsg());
	}
}
