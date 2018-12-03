package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
import org.json.JSONArray;


public class test {

	private static final String USER_AGENT = "Mozilla/5.0";
	
	private static final String NOMUID = "sb0950";
	
	private static final String NOMUPW = "sbsb7356";
	
	private static final String SITEID = "S300011863";
	
	private static final String POST_URL = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login";

	private static final String POST_URL2 = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/workercommon";
	
	
	public static void main(String[] args) throws IOException {
		
		
		   int sleepSec = 6 ;
	         
	        // 시간 출력 포맷
	        final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
	 
	         
	        // 주기적인 작업을 위한
	        final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
	         
	         
	        exec.scheduleAtFixedRate(new Runnable(){
	             
	            public void run(){
	                try {
	 
	                    Calendar cal = Calendar.getInstance() ;
	                     
	                     
	                    // 콘솔에 현재 시간 출력

	                    System.out.println(fmt.format(cal.getTime())) ;
	            		sendPOST();
	                     
	                } catch (Exception e) {
	                     
	                    e.printStackTrace();
	                     
	                     
	                    // 에러 발생시 Executor를 중지시킨다
	                    exec.shutdown() ;
	                }
	            }
	        }, 0, sleepSec, TimeUnit.HOURS);
	    }
		
		
		
	
	
	private static String getAuthKey() throws IOException{

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL);
		httpPost.addHeader("User-Agent", USER_AGENT);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		
		
		
		String tempStr = "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"" + NOMUID + "\",\"USERPW\":\"" + NOMUPW + "\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}";
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
	
	@SuppressWarnings("deprecation")
	private static void sendPOST() throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL2);
		httpPost.addHeader("User-Agent", USER_AGENT);
		httpPost.addHeader("DEVICEID", "API_NOMU_M0079");
		httpPost.addHeader("APPID", "API_WEB");
		httpPost.addHeader("AUTHKEY", getAuthKey());
		

		Date now = new Date();

		//now.setYear(2018);
		now.setMonth(10);
		now.setDate(15);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String tDate = format.format(now).toString();
		
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		
		String tempStr = "{\"ROWS\":[{\"REQUEST_ID\":\"S|USERAPI.WORKING_SEARCH_S\",\"PARAMETER\":{\"COM_CD\":\"M0079\",\"SITE_CD\":\"" + SITEID + "\",\"REQDATE\":\"" + tDate + "\",\"RES_NO\":\"\"}}]}";
		urlParameters.add(new BasicNameValuePair("JSON", tempStr));
		
		HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
		httpPost.setEntity(postParams);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		
		//System.out.println("POST Response Status: "
				//+ httpResponse.getStatusLine().getStatusCode());

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

		//System.out.println("RESULT_DATA: "
			//	+ ja3.toString());
		//System.out.println("ja3.length(): "
			//	+ ja3.length());
		
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
			}
			con.close();	
		} catch(Exception e){
			System.out.println("Mysql Server Not Connection.");			
			e.printStackTrace();
		}
		
	}
	
	private static void insert(JSONObject jo, Connection con, String tDate) throws SQLException {
		String qc = " SELECT nomu_idx, start_time, end_time FROM nomu WHERE stddate = '" + tDate + "' and resno = '" + jo.get("RESNO") + "'";
		String qc2 = "INSERT INTO nomu(stddate, name, resno, end_time, start_time, create_id) "	+ "VALUES (?, ?, ?, ?, ?, 'admin')";
		String qc3 = "UPDATE nomu SET start_time = ?, end_time = ? WHERE stddate = '" + tDate + "' AND resno = ?" ;
		PreparedStatement st = con.prepareStatement(qc);		
		//System.out.println(st);
		ResultSet rs = st.executeQuery();
		//System.out.println(rs.isBeforeFirst());
		st = null;
		int rs2 = 0;
		
		//SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
		
		if(rs.isBeforeFirst()) {
			while(rs.next()) {
				//System.out.println(trans.format(rs.getTimestamp("start_time"))  + "|" + jo.getString("START_TIME") );
				//System.out.println(trans.format(rs.getTimestamp("start_time"))  + "|" + jo.getString("START_TIME") );
				
				//System.out.println( "|" + jo.optString("END_TIME", null));
				//System.out.println(trans.format(rs.getTimestamp("end_time"))  + "|" + jo.opt("END_TIME"));
				//System.out.println(trans.format(rs.getTimestamp("end_time")).toString()  + "|" + jo.optString("END_TIME",null));
				//if(rs.getDate("start_time").toString() != jo.getString("START_TIME").toString() || rs.getDate("end_time").toString() != jo.optString("END_TIME",null).toString()) {
					st = con.prepareStatement(qc3);
					st.setString(1, jo.getString("START_TIME"));
					st.setString(2, jo.optString("END_TIME",null));
					st.setString(3, jo.getString("RESNO"));
					rs2 = st.executeUpdate();
					//st.execute();			
					System.out.println(getSQL(st) + "\r" + "result : " + rs2);	
				//}
			}
									
			
		} else {
			
			st = con.prepareStatement(qc2);
			st.setString(1, tDate);
			st.setString(2, jo.getString("NAME"));
			st.setString(3, jo.getString("RESNO"));
			st.setString(4, jo.optString("END_TIME",null));
			st.setString(5, jo.getString("START_TIME"));
			rs2 = st.executeUpdate();
			System.out.println(getSQL(st) + "\r" + "result : " + rs2);
							
			
		}
		
		
	}
	
	private static String getSQL (Statement stmt){
	    String tempSQL = stmt.toString();

	    int i1 = tempSQL.indexOf(":")+2;
	    tempSQL = tempSQL.substring(i1);

	    return tempSQL;
	}

}