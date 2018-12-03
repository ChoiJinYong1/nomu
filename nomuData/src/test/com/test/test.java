package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import java.sql.*;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONArray;


public class test {

	private static final String USER_AGENT = "Mozilla/5.0";
	
	private static final String POST_URL = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login";

	private static final String POST_URL2 = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/workercommon";
	
	
	public static void main(String[] args) throws IOException {
		//getAuthKey();

		
		sendPOST();
		System.out.println("POST DONE");
	}
	
	private static String getAuthKey() throws IOException{

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL);
		httpPost.addHeader("User-Agent", USER_AGENT);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		
		
		
		String tempStr = "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}";
		urlParameters.add(new BasicNameValuePair("JSON", tempStr));
		
		HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
		httpPost.setEntity(postParams);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		
		
		
		System.out.println("POST Response Status:: "
				+ httpResponse.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent(),"UTF-8"));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		
		reader.close();

		String jpStr = response.toString();

		JSONObject jo = new JSONObject(jpStr);
		JSONObject jo2 = new JSONObject(jo.get("ROWS").toString().substring(1, jo.get("ROWS").toString().length() - 1));
		JSONObject jo3 = new JSONObject(jo2.get("RESULT_DATA").toString().substring(1, jo2.get("RESULT_DATA").toString().length() - 1));
		
		httpClient.close();
		return jo3.get("AUTHKEY").toString();
	
	}
	
	private static void sendPOST() throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL2);
		httpPost.addHeader("User-Agent", USER_AGENT);
		httpPost.addHeader("DEVICEID", "API_NOMU_M0079");
		httpPost.addHeader("APPID", "API_WEB");
		httpPost.addHeader("AUTHKEY", getAuthKey());
		

		Date now = new Date();
		
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("date:" + format.format(now).toString());
		String tDate = format.format(now).toString();
		
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		
		String tempStr = "{\"ROWS\":[{\"REQUEST_ID\":\"S|USERAPI.WORKING_SEARCH_S\",\"PARAMETER\":{\"COM_CD\":\"M0079\",\"SITE_CD\":\"S300011863\",\"REQDATE\":\"" + tDate + "\",\"RES_NO\":\"\"}}]}";
		urlParameters.add(new BasicNameValuePair("JSON", tempStr));
		
		HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
		httpPost.setEntity(postParams);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		
		System.out.println("POST Response Status: "
				+ httpResponse.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent(),"UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		httpClient.close();
		
		String jpStr = response.toString();
		
		JSONObject jo = new JSONObject(jpStr);
		JSONObject jo2 = new JSONObject(jo.get("ROWS").toString().substring(1, jo.get("ROWS").toString().length() - 1));
		
		JSONArray ja3 = jo2.getJSONArray("RESULT_DATA");

		System.out.println("RESULT_DATA: "
				+ ja3.toString());
		System.out.println("ja3.length(): "
				+ ja3.length());
		
		try {
			String driverName = "org.gjt.mm.mysql.Driver";
			String DBName = "shinboerp";
			String dbURL = "jdbc:mysql://localhost:3306/" + DBName;
			//String SQL = " select * from account;";
			
			Class.forName(driverName);
			
			Connection con = DriverManager.getConnection(dbURL, "root", "1111");
			System.out.println("Mysql DB Connection.");
			
			
			for(int i=0; i< ja3.length(); i++) {
				JSONObject tempoj = ja3.getJSONObject(i);
				insert (tempoj, con, tDate);
				//System.out.println(ja3.get(i).toString());	
				
			}
			
			/*
			Statement stmt = con.createStatement();
			
			stmt.executeQuery(SQL);
			
			ResultSet result = stmt.executeQuery(SQL);
			
			while(result.next()) {
				System.out.println(result.getString(1) + "\t");
				System.out.println(result.getString(2) + "\t");
				System.out.println(result.getString(3) + "\t");
				System.out.println(result.getString(4) + "\t");
			}
			*/
			con.close();	
		} catch(Exception e){
			System.out.println("Mysql Server Not Connection.");			
			e.printStackTrace();
		}
		
	}
	
	private static void insert(JSONObject jo, Connection con, String tDate) {
		String qc = " SELECT nomu_idx FROM nomu WHERE stddate = '" + tDate + "' and resno = '" + jo.get("RESNO") + "'";
		System.out.println(qc);
		
	}
}