package com.project.latinoEcke.utils;

import java.io.File;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProjectUtils {
	private ProjectUtils() {

	}

	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
		return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);

	}

	public static String getUUID() {
		Date date = new Date();
		long time = date.getTime();

		return "BILL-" + time;
	}

//	public static JSONArray getJsonArrayFromString(String data) throws JSONException {
//		JSONArray jsonArray = new JSONArray(data);
//		return jsonArray;
//
//	}
//
//	public static Map<String, Object> getMapFromJson(String data) {
//		if (!Strings.isNullOrEmpty(data))
//			return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
//
//			}.getType());
//		return new HashMap<>();
//
//	}

	public static Boolean isFileExist(String path) {
		try {
			File file = new File(path);
			return (file != null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;

	}

}
