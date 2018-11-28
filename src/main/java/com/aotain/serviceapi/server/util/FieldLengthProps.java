package com.aotain.serviceapi.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum FieldLengthProps {

	instance();

	private final static String FILE_NAME = "field-length.properties";

	private Properties prop;

	private FieldLengthProps() {
		try {
			InputStream is = FieldLengthProps.class.getClassLoader().getResourceAsStream("config/"+FILE_NAME);
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

	public static void main(String[] args) {
		System.out.println(FieldLengthProps.instance.getLength("IDC_ISMS_BASE_IDC.IDCID"));
	}

}
