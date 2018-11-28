package com.aotain.serviceapi.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum FieldInfoProps {

	instance();
	
	private final static String FILE_NAME = "field-length.properties";
	
	private Properties prop;
	
	private FieldInfoProps() {
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "config/basic/" + FILE_NAME;
			InputStream is = new FileInputStream(new File(path));
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
	
}
