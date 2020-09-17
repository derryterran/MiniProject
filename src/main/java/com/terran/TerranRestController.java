package com.terran;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.terran.util.TerranController;

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
		TerranController tc=new TerranController();
		return tc.retrieveCountriesFromDB();
	}
    
    //This is rest service to retrieve country from External API
	@GetMapping("/syncCountries")
	@ApiOperation(value = "Get countries from External API")
	public String synCountry() throws Exception {
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
		logger.info("insert or update country");
		try {
			tc.decryptAES(user);
			flag=true;
		}catch(Exception e){
			result="{\"result\":\"Failed to encrypt\"}";
		}
		if(flag) {
			try {
				tc.addCountry(json);
				result="{\"result\":\"Successfully executed\"}";
			}catch(Exception e){
				result="{\"result\":\"Failed to execute\"}";
			}
		}
		
		return result;
	}
}
