package com.terran.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


//This the controller of REST service
public class TerranController {
	private static final Logger logger = LogManager.getLogger(TerranController.class);
	//retrieve country from external API
	public Object retrieveCountriesFromExternalAPI() throws JsonParseException, JsonMappingException, IOException {
		  logger.info("Retrieve country from external API");
		  TerranUtil terranUtil=new TerranUtil();
		  Map<String,String> inputMap=new HashMap<String,String>();
		  String json=terranUtil.callRest(inputMap, "https://restcountries-v1.p.rapidapi.com/subregion/South-Eastern%20Asia");
		  ObjectMapper mapper = new ObjectMapper();
		  List<Map<String, Object>> data = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>(){});
		  return data;
	}
	//retrieve country from external DB
	@SuppressWarnings("rawtypes")
	public String retrieveCountriesFromDB() {
		try {
			TerranDBUtil tbu=new TerranDBUtil();
			tbu.connect();
		    List lsCountry=tbu.getCountries();
			String json=TerranUtil.convertJsonList(lsCountry);
			tbu.close();
			return json;
			} catch (Exception e)
			{
				logger.error("Error found "+e.toString());
				e.printStackTrace();
				return "{\"result\":\"Failed to execute\"}";
			}
	}
	//Sync country get country from external API and insert into DB
	@SuppressWarnings("rawtypes")
	public String syncCountries() {
		TerranDBUtil tbu=new TerranDBUtil();
		try {
			tbu.connect();
			List lsExternalCountries=(List)retrieveCountriesFromExternalAPI();
			tbu.insertOrUpdateCountries(lsExternalCountries);
			String json=TerranUtil.convertJsonList(lsExternalCountries);
			tbu.close();
			return json;
		}catch(Exception e) {
			logger.error("Error found "+e.toString());
			return "{\"result\":\"Failed to execute\"}";
		}
	}
	
	// add a country into DB
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String addCountry(String json) {
		String result="{\"result\":\"Failed to execute\"}";
		try {
			Map map=TerranUtil.convertJsonToMap(json);
			List ls=new ArrayList();
			ls.add(map);
			TerranDBUtil tbu=new TerranDBUtil();
			tbu.connect();
			tbu.insertOrUpdateCountries(ls);
			tbu.close();
			return "{\"result\":\"Successfully executed\"}";
		}catch(Exception e) {
			logger.error("Error found "+e.toString());
			e.printStackTrace();
		}
		return result;
	}
	// do AES encryption
	public String encryptText(String text) throws Exception {
		String encryptedTextBase64 = TerranEncDec.encrypt(text.getBytes(TerranCrypto.UTF_8), TerranCrypto.PASSWORD);
		return encryptedTextBase64;
	}
	//do AES decryption
	public String decryptAES(String text) throws Exception {
		String decryptedText = TerranEncDec.decrypt(text, TerranCrypto.PASSWORD);
		return decryptedText;
	}
}
