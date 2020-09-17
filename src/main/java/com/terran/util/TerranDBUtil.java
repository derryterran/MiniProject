package com.terran.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//to manage DB connection and DML
public class TerranDBUtil {
	private Connection conn = null;
	
	//load properties
	public static Properties loadProp() {
		try{
			InputStream input =TerranDBUtil.class.getClassLoader().getResourceAsStream("application.properties");
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

	}
	//connect to DB
	public void connect() {
		conn = null;
		try {
			// db parameters
			Class.forName("org.sqlite.JDBC");
			String url = loadProp().getProperty("db.url");
			// create a connection to the database
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	//close connection
	public void close() {
		try {
			if (conn != null) {
				conn.close();// every connection will be closed
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	//insert or update country
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertOrUpdateCountries(List countries) {
		try {
			Statement stm = conn.createStatement();

			for (int i = 0; i < countries.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) countries.get(i);
				String countryCode = (String) map.get("alpha2Code");
				if (isCountryExist(countryCode) == false) {
					String queryInsertHeader = "INSERT INTO TB_COUNTRY(COUNTRY_CD)VALUES('" + countryCode + "')";
					stm.execute(queryInsertHeader);
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						String val = "";
						if (entry.getValue() != null) {
							val = entry.getValue().toString().replace("'", "''");
						}
						String queryInsertDetail = "INSERT INTO TB_COUNTRY_DETAIL(COUNTRY_CD,COUNTRY_VAR,COUNTRY_VAL)VALUES('"
								+ countryCode + "','" + entry.getKey() + "','" + val + "')";
						stm.executeUpdate(queryInsertDetail);
					}
				}else {
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						String val = "";
						if (entry.getValue() != null) {
							val = entry.getValue().toString().replace("'", "''");
						}
						String queryUpdateDetail = "UPDATE TB_COUNTRY_DETAIL SET COUNTRY_VAL='" + val + "' WHERE COUNTRY_VAR='"+entry.getKey()+"' AND COUNTRY_CD='"+countryCode+"'";
						int count=stm.executeUpdate(queryUpdateDetail);
						if(count==0) {
							String queryInsertDetail = "INSERT INTO TB_COUNTRY_DETAIL(COUNTRY_CD,COUNTRY_VAR,COUNTRY_VAL)VALUES('"
									+ countryCode + "','" + entry.getKey() + "','" + val + "')";
							stm.executeUpdate(queryInsertDetail);
						}
					}
				}
			}
			stm.close();// every connection will be closed
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//check if country exist
	public boolean isCountryExist(String countryCode) {
		boolean flag = false;
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM TB_COUNTRY WHERE COUNTRY_CD='" + countryCode + "'");
			while (rs.next()) {
				flag = true;
			}
			rs.close();// every connection will be closed
			stm.close();// every connection will be closed
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	//get all countries
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getCountries() {
		List lsCountry = new ArrayList();
		List<String> ls = new ArrayList();
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM TB_COUNTRY");
			while (rs.next()) {
				ls.add(rs.getString("COUNTRY_CD"));
			}
			rs.close();// every connection will be closed
			for (String countryCd : ls) {
				ResultSet rs2 = stm
						.executeQuery("SELECT * FROM TB_COUNTRY_DETAIL WHERE COUNTRY_CD='" + countryCd + "'");
				Map map = new HashMap();
				while (rs2.next()) {
					map.put(rs.getString("COUNTRY_VAR"), rs2.getString("COUNTRY_VAL"));
				}
				lsCountry.add(map);
				rs2.close();// every connection will be closed
			}
			stm.close();// every connection will be closed
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lsCountry;
	}
}
