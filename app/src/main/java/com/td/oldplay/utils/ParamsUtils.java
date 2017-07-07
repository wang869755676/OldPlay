package com.td.oldplay.utils;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;


/**
 *  对请求参数进行加密
 */
public class ParamsUtils {
	/**
	 * 
	 * @param requesturl
	 * @param obj
	 * @return
	 */
	public static String getResponseStr(String requesturl, Object obj) {
		String requestUrl = requesturl;
		if (obj != null) {
			Gson gson = null;
			gson = new Gson();
			String str = gson.toJson(obj);// Json编码
			str = Base64Utils.getBase64(str);// Base64加密
			str = str.replaceAll("/", "-");
			requestUrl += str;
			requestUrl = AppUtils.checkURL(requestUrl);
		}
		return requestUrl;
	}
	
	public static String getHxResponseApi(String requesturl, List<String> usernames){
		String requestUrl = requesturl;
		if (usernames != null && usernames.size()>0) {
			Gson gson = null;
			gson = new Gson();
			String str = gson.toJson(usernames);// Json编码
			str = Base64Utils.getBase64(str);// Base64加密
			str = str.replaceAll("/", "-");
			requestUrl += str;
			requestUrl = AppUtils.checkURL(requestUrl);
		}
		return requestUrl;
	}
	
	public static String buildParams(String requesturl, Map<String, Object> params) {
		String requestUrl = requesturl;
		if (params == null) {
			return requestUrl;
		}
		Gson gson = null;
		gson = new Gson();
		String str = gson.toJson(params);// Json编码
		str = Base64Utils.getBase64(str);// Base64加密
		str = str.replaceAll("/", "-");
		requestUrl += str;
		requestUrl = AppUtils.checkURL(requestUrl);
		return requestUrl;
	}
}
