package com.terran.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.codehaus.jackson.map.ObjectMapper;
//util class to support project
public class TerranUtil {
	
	//call rest web service
	@SuppressWarnings("rawtypes")
	public String callRest(Map content, String urli) {
		String result = "";
		try {
			URL url = new URL(urli);
			
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("x-rapidapi-host", "restcountries-v1.p.rapidapi.com");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("x-rapidapi-key", "c7fd1fb128msh063555c551d2e97p1f314djsneff48a65fd2b");
			conn.setRequestProperty("useQueryString", "true");
		
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			while ((output = br.readLine()) != null) {
				result = result + output;
			}
			conn.disconnect();

		} catch (Exception e) {
			result = "Client service not found :" + urli + " - " + e;
			e.printStackTrace();
		}
		return result;
	}
	//convert map to json
	@SuppressWarnings("rawtypes")
	public static String convertJson(Map map) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(map);
			json = String.valueOf(json);
			String s = json;
			s = "" + s;
			s = s.replaceAll("\\\\", "");
			return s;
		} catch (Exception e) {
			return "TerranService :Failed to processing data :" + e.toString();
		}
	}
	//convert list of map to json
	@SuppressWarnings("rawtypes")
	public static String convertJsonList(List list) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(list);
			json = String.valueOf(json);
			String s = json;
			s = "" + s;
			s = s.replaceAll("\\\\", "");
			return s;
		} catch (Exception e) {
			return "TerranService :Failed to processing data :" + e.toString();
		}
	}
	//convert json to map
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map convertJsonToMap(String json) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, String> map = mapper.readValue(json, Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}
}
