package com.jennifer.jennifer.ui.palette.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON解析工具类
 *
 */
public class ModelParser {

	  private static GsonBuilder builder = new GsonBuilder();
	  private static Gson gson = builder.create();
	  public static <T> T getObjectfromJson(String jsonStr, Class<T> classType) {
		  try {
			  T obj = gson.fromJson(jsonStr, classType);
			  return obj;
		  }catch (Exception e){
			  e.printStackTrace();
		  }
		  return null;
	  }


	  public static String getStringfromObject(Object obj){
		  return gson.toJson(obj);
	  }
}
