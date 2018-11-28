package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;


public class test5 {
	public static void main(String[] args) throws JSONException, IOException {
		
	JSONObject json = new JSONObject();
	//json.put("id", "COMMON.AUTHLOGIN_S");
	//json.put("url", "/web/login");
	json.put("JSON", "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
	Object obj = new Object();
	obj = "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}";
	String body = json.toString().replace("\\", "");
	//String body = json.st;
	System.out.println(body);
	URL postUrl = new URL("http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login");
	HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
	connection.setDoOutput(true); 				// xml내용을 전달하기 위해서 출력 스트림을 사용
	connection.setInstanceFollowRedirects(false);  //Redirect처리 하지 않음
	connection.setRequestMethod("POST");
	connection.setRequestProperty("Content-Type", "application/json");
	OutputStream os= connection.getOutputStream();
	os.write(body.getBytes());
	os.flush();
	System.out.println("Location: " + connection.getHeaderField("Location"));
	
	  BufferedReader br = new BufferedReader(new InputStreamReader(
			  (connection.getInputStream())));
			   
			  String output;
			  System.out.println("Output from Server .... \n");
			  while ((output = br.readLine()) != null) {
			  System.out.println(output);
			  }
	connection.disconnect();
	}
}

