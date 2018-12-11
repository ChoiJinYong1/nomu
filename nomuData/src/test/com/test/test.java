package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
	
	private static final String POST_URL = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login";

	private static final String POST_URL2 = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/workercommon";

	private static final String DRIVERNAME = "org.gjt.mm.mysql.Driver";

	private static final String DBNAME = "shinboerp";

	private static final String DBURL = "jdbc:mysql://localhost:3306/" + DBNAME;

	public static void main(String[] args) throws IOException {
		
		
	   int sleepSec = 3 ;
	         
	        // 시간 출력 포맷
       final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	        // 주기적인 작업을 위한
       final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
	   
       
       exec.scheduleAtFixedRate(new Runnable(){
             
            public void run(){
                try {
 
                    Calendar cal = Calendar.getInstance() ;                     
                     
                    // 콘솔에 현재 시간 출력
                    System.out.println("============================	getStart	============================") ;
                    System.out.println("작업시작시간 : " + fmt.format(cal.getTime())) ;
	                if(isHostAvailable("nomu.hyunjang.co.kr")) {
	                	System.out.println("인터넷 연결 확인 완료하였습니다.") ;
	                	sendPOST();
	                } else {
	                	System.out.println("인터넷연결을 확인하십시요.");
	                }
	                	
	                System.out.println("============================	getEnd		============================") ;
	                 
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
	
	private static void sendPOST() throws IOException {
		String tSiteCD = "";
		

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL2);
		httpPost.addHeader("User-Agent", USER_AGENT);
		httpPost.addHeader("DEVICEID", "API_NOMU_M0079");
		httpPost.addHeader("APPID", "API_WEB");
		httpPost.addHeader("AUTHKEY", getAuthKey());
		

		Date now = new Date();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String tDate = format.format(now).toString();

		try {
			
			Class.forName(DRIVERNAME);
			
			Connection con = DriverManager.getConnection(DBURL, "root", "1111");
			System.out.println("Mysql DB Connection.");
			
			ResultSet rs = getSitecd(con);
			
			if(rs.isBeforeFirst()) {
				while(rs.next()) {
					tSiteCD = "";
					tSiteCD = rs.getString("site_cd");
					
					System.out.println(rs.getString("site_nm") + "(" + rs.getString("site_cd") +")");
					List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
					
					String tempStr = "{\"ROWS\":[{\"REQUEST_ID\":\"S|USERAPI.WORKING_SEARCH_S\",\"PARAMETER\":{\"COM_CD\":\"M0079\",\"SITE_CD\":\"" + tSiteCD + "\",\"REQDATE\":\"" + tDate + "\",\"RES_NO\":\"\"}}]}";
					//System.out.println(tempStr);
					urlParameters.add(new BasicNameValuePair("JSON", tempStr));
					
					HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
					httpPost.setEntity(postParams);
					
					CloseableHttpResponse httpResponse = httpClient.execute(httpPost);					

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
					
					JSONArray ja3 = jo2.getJSONArray("RESULT_DATA");
					if(ja3.length() == 0 ) {
						System.out.println("저장할 데이터가 없습니다.");
					} else {
						for(int i=0; i< ja3.length(); i++) {
							JSONObject tempoj = ja3.getJSONObject(i);
							insert (tempoj, con, tDate, tSiteCD);
						}	
					}
					
					
				}
			}

			httpClient.close();
			con.close();	
		} catch(Exception e){
			System.out.println("Mysql Server Not Connection.");			
			e.printStackTrace();
		}
	}
	
	private static void insert(JSONObject jo, Connection con, String tDate, String tSiteCD) throws SQLException, Exception {
		String qc = " SELECT nomu_idx, start_time, site_cd, end_time, name FROM nomu WHERE stddate = '" + tDate + "' and resno = '" + jo.get("RESNO") + "'";
		String qc2 = "INSERT INTO nomu(stddate, site_cd, name, resno, end_time, start_time, create_id) "	+ "VALUES (?, ?, ?, ?, ?, ?, 'admin')";
		String qc3 = "UPDATE nomu SET start_time = ?, end_time = ? WHERE stddate = '" + tDate + "' AND resno = ? AND site_cd = ?" ;
		PreparedStatement st = con.prepareStatement(qc);
		ResultSet rs = st.executeQuery();
		st = null;
		int rs2 = 0;
		
		if(rs.isBeforeFirst()) {
			while(rs.next()) {
				String jostartdt = new String();
				String rsstartdt = new String();
				String joenddt = new String();
				String rsenddt = new String();				
				
				jostartdt = jo.optString("START_TIME", "");
				if(rs.getString("start_time") == null)
					rsstartdt = "";
				else
					rsstartdt = rs.getString("start_time").substring(0, 19);
				joenddt = jo.optString("END_TIME", "");
				if(rs.getString("end_time") == null)
					rsenddt = "";
				else
					rsenddt = rs.getString("end_time").substring(0, 19);
				
				if(jostartdt.equals(rsstartdt) && joenddt.equals(rsenddt)) {
					System.out.println(rs.getString("name") + ":변경사항 없음");
						
				} else {
					st = con.prepareStatement(qc3);					
					st.setString(1, jo.getString("START_TIME"));
					st.setString(2, jo.optString("END_TIME",null));
					st.setString(3, jo.getString("RESNO"));
					st.setString(4, tSiteCD);
					rs2 = st.executeUpdate();
					System.out.println(getSQL(st) + "\r" + "result : " + rs2);
				}
			}
									
			
		} else {
			
			st = con.prepareStatement(qc2);
			st.setString(1, tDate);
			st.setString(2, tSiteCD);
			st.setString(3, jo.getString("NAME"));
			st.setString(4, jo.getString("RESNO"));
			st.setString(5, jo.optString("END_TIME",null));
			st.setString(6, jo.getString("START_TIME"));
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
	
	private static ResultSet getSitecd(Connection con) throws SQLException {
		String qc = " SELECT site_cd, site_nm FROM nomu_site";
		PreparedStatement st = con.prepareStatement(qc);
		ResultSet rs = st.executeQuery();
		
		return rs;
	}
	
	private static boolean isHostAvailable(String hostName) throws IOException
    {
        try(Socket socket = new Socket())
        {
            int port = 80;
            InetSocketAddress socketAddress = new InetSocketAddress(hostName, port);
            socket.connect(socketAddress, 3000);

            return true;
        }
        catch(UnknownHostException unknownHost)
        {
            return false;
        }
    }
}