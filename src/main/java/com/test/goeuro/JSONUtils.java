package com.test.goeuro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Note: This code is modified version of CDL.java from JSON.org
 * 
 * JSON data utilities
 *
 */

public class JSONUtils {

	
	/**
	 * Convert given data to CSV format
	 * 
	 * @param names -cloumnes we are interested in 
	 * @param ja - data object
	 * @return CSV data
	 * @throws JSONException
	 */
	public static String toString(JSONArray names, JSONArray ja) throws JSONException {
		if (names == null || names.length() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < ja.length(); i += 1) {
			JSONObject jo = ja.optJSONObject(i);
			if (jo != null) {
				sb.append(rowToString(jo.toJSONArray(names)));
			}
		}

		return sb.toString();
	}

	/**
	 * Convert given data to CSV format recursively
	 * 
	 * TODO: Right now in case of object having nested object, all the keys and values are converted to CSV
	 * TODO: Latter if we pass the interested name then we can convert only selected
	 *  
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static String rowToString(JSONArray ja) throws JSONException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ja.length(); i += 1) {
			if (i > 0) {
				sb.append(',');
			}
			Object object = ja.opt(i);
			if (object != null) {
				
				//If this is JSONObject means we have Object having another object
				if(object instanceof JSONObject) {
					JSONObject jsonObj = (JSONObject)object;
					//TODO: Take these names as parameter so that we can selectively convert the values to CSV
					JSONArray names = jsonObj.names(); //Right now all columns we are interested in nested object
					JSONArray array = new JSONArray();
					array.put(object);
					sb.append(toString(names, array));
					//remove added new line char
					sb.deleteCharAt(sb.length()-1);
				} else {
					String string = object.toString();
					if (string.length() > 0 && (string.indexOf(',') >= 0 || 
							string.indexOf('\n') >= 0 || string.indexOf('\r') >= 0 || 
							string.indexOf(0) >= 0 || string.charAt(0) == '"')) {
						sb.append('"');
						int length = string.length();
						for (int j = 0; j < length; j += 1) {
							char c = string.charAt(j);
							if (c >= ' ' && c != '"') {
								sb.append(c);
							}
						}
						sb.append('"');
					} else {
						sb.append(string);
					}
				}
			}
		}
		sb.append('\n');
		return sb.toString();
	}
}
