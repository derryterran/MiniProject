package com.terran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.terran.util.TerranController;
import com.terran.util.TerranDBUtil;
import com.terran.util.TerranUtil;

/**
 * Servlet implementation class RESTServletController
 */
@WebServlet("/RESTServletController")
public class RESTServletController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RESTServletController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out=response.getWriter();
		String req=request.getParameter("countries");
		String title="";
		String result="{\"result\":\"No Data - Please do sync countries\"};";
		TerranUtil tu=new TerranUtil();
		try {
			if(req.equals("syncCountries")) {
				title="Sync Countries from External API";
				result=tu.callRestCustom(new HashMap(), TerranDBUtil.loadProp().getProperty("synccountries.url"),"GET");
				result=result+";";
			}else if(req.equals("getCountries")) {
				title="Get Countries from DB";
				result=tu.callRestCustom(new HashMap(), TerranDBUtil.loadProp().getProperty("getcountries.url"),"GET");
				result=result+";";
			}else if(req.equals("insertOrUpdateCountry")) {
				title="Insert or Update Country";
				String val=request.getParameter("inputJson");
				String enc=request.getParameter("userEnc");
				Map map=TerranUtil.convertJsonToMap(val);
				result=tu.callRestCustom(map, TerranDBUtil.loadProp().getProperty("insertupdate.url")+"?user="+enc,"POST");
				result=result.replace("\\", "/");
				result=result+";";
			}else if(req.equals("getChecksumFile")) {
				title="Get Checksum File";
				String ckSm=request.getParameter("ckSum");
				String fNm=request.getParameter("fNm");
				fNm=fNm.replace(" ", "");
				fNm= fNm.replace("\n", "").replace("\r", "");
				ckSm=ckSm.replace(" ", "");
				ckSm=ckSm.replace("\n", "").replace("\r", "");
				File f=new File(fNm);
				FileReader reader = new FileReader(f.getAbsolutePath());
			    BufferedReader br = new BufferedReader(reader);
			    String sLine=null;
			    String encrypted=null;
			    StringBuilder sb = new StringBuilder();
			    while ((sLine = br.readLine()) != null) {
			    	sb.append(sLine);
			    }
			    encrypted=sb.toString();
			    TerranController tc=new TerranController();
			    String decrypted=tc.decryptAES(encrypted);
			    MessageDigest md = MessageDigest.getInstance("MD5");
			    String checkSum=TerranUtil.checksum(f.getAbsolutePath(), md);
				if(checkSum.equals(ckSm)) {
					result="{\"result\":\"Valid Checksum\",\"encryptedValue\":\""+encrypted+"\",\"decryptedValue\":\""+decrypted+"\",\"checksum\":\""+checkSum+"\",\"fileName\":\""+f.getName()+"\"}";
				}else {
					result="{\"result\":\"Invalid Checksum\"}";
				}
				result=result+";";
			}
		}catch(Exception e) {
			e.printStackTrace();
			result="{\"result\":\"Error occured\"};";
		}
		out.println("<html>");
	    out.println("<head>");
	    out.print("<meta charset=\"ISO-8859-1\"> ");
	    out.println("<title>REST Service Countries : "+title+"</title>");
	    out.print("<style>body {\r\n" + 
	    		"  padding: 3rem;\r\n" + 
	    		"  font-size: 16px;\r\n" + 
	    		"}\r\n" + 
	    		"\r\n" + 
	    		"textarea {\r\n" + 
	    		"  width: 100%;\r\n" + 
	    		"  min-height: 30rem;\r\n" + 
	    		"  font-family: \"Lucida Console\", Monaco, monospace;\r\n" + 
	    		"  font-size: 0.8rem;\r\n" + 
	    		"  line-height: 1.2;\r\n" + 
	    		"}</style>");
	    out.println("</head>");
	    out.println("<body bgcolor=\"white\">");
	    out.print("<h3>REST Service Countries : "+title+"</h3>");
	    out.print("<br/>");
	    out.print("<a href=\"/MiniProject\">Back</a><br/>");
	    out.print("<pre>");
	    out.print("<textarea name=\"\" id=\"json\" cols=\"30\" rows=\"10\"></textarea>");
	    out.print("<script>var data= "+result+"");
		out.print("document.getElementById(\"json\").textContent = JSON.stringify(data, undefined, 2);");
	    out.print("</script>");
		out.print("</pre>");
	    out.println("</body>");
	    out.println("</html>");
	}

}
