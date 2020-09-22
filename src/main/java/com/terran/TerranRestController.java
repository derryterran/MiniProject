package com.terran;




import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.terran.util.TerranController;
import com.terran.util.TerranUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "TerranRestController")
@RestController
public class TerranRestController {
    private static final Logger logger = LogManager.getLogger(TerranRestController.class);
	
    //This is rest service to retrieve country from DB
    @GetMapping("/countries")
    @ApiOperation(value = "Get countries from DB")
	public String getCountries() throws Exception {
    	logger.info("Get countries from DB");
		TerranController tc=new TerranController();
		return tc.retrieveCountriesFromDB();
	}
    
    //This is rest service to retrieve country from External API
	@GetMapping("/syncCountries")
	@ApiOperation(value = "Get countries from External API")
	public String synCountry() throws Exception {
		logger.info("Sync country");
		TerranController tc=new TerranController();
		
		return tc.syncCountries();
	}
	//This is rest service to add country to DB
	@PostMapping("/addOrUpdateCountry")
	@ApiOperation(value = "Add or Update Country")
	public String addOrUpdateCountry(@RequestParam(name = "user") String user,@RequestBody String json) throws Exception {
		String result="{\"result\":\"Failed to execute\"}";
		boolean flag=false;
		TerranController tc=new TerranController();
		String decryptedUser="";
		logger.info("insert or update country");
		List<String> ls=null;
		try {
			logger.info("Encrypted User :" +user);
			decryptedUser=tc.decryptAES(user);
			ls=TerranUtil.writeToFile(user, decryptedUser);
		    flag=true;
		}catch(Exception e){
			e.printStackTrace();
			result="{\"result\":\"Failed to encrypt\"}";
		}
		if(flag) {
			try {
				tc.addCountry(json);
				result="{\"result\":\"Successfully executed\",\"checksum\":\""+ls.get(0)+"\",\"fileName\":\""+ls.get(1)+"\",\"decryptedUser\":\""+decryptedUser+"\"}";
			}catch(Exception e){
				e.printStackTrace();
				result="{\"result\":\"Failed to execute\"}";
			}
		}
		
		return result;
	}
}
