package com.aotain.serviceapi.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public enum PropertyTool {

	instance();

	private final static String FILE_NAME = "error.properties";
	private Properties prop;

	public static void main(String[] args) {
		System.out.println(PropertyTool.instance.prop.get("smms.ws.url"));
		PropertyTool.instance.prop.setProperty("smms.ws.url", "http://192.168.5.101:8088/interface/idcCommand?wsdl");
		System.out.println(PropertyTool.instance.prop.get("smms.ws.url"));
	}

	private PropertyTool() {
		try {
//			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/config/" + FILE_NAME;
//			InputStream is = new FileInputStream(new File(path));
//			prop = new Properties();
//			prop.load(is);
			InputStream is = PropertyTool.class.getClassLoader().getResourceAsStream("config/"+FILE_NAME);
			prop = new Properties();
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public Integer getLength(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}

	public String getCN(String key) {
		return prop.getProperty(key);
	}


	public Boolean savePropValue(String key,String value){
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/" + FILE_NAME;
		try {
			OutputStream os = new FileOutputStream(path);
			prop.setProperty(key, value);
			prop.store(os, "update success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getPropValue(String key){
		return prop.getProperty(key);
	}
}
