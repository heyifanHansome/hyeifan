package com.stylefeng.guns.modular.ossFileInput;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	 public static String getValueByKey(String keyWord){
			 Properties prop = new Properties();
			 InputStream in = PropertiesUtil.class.getResourceAsStream("/config.properties");
					 try {
						 prop.load(in);
						 return prop.getProperty(keyWord).trim();
					 } catch (IOException e) {
						 e.printStackTrace();
					 }
					     return "";
					 }
		     }

