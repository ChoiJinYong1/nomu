package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class test4 {

	public static void main(String[] args) throws IOException, JSONException {
		//String urlStr = "http://hmkcode.appspot.com/jsonservlet";
		String urlStr = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login";
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		
		conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.setRequestProperty("Context-Type", "application/x-www-form-urlencoded; charset=UTF-8" );

		String json = "";
		String tmpStr = "";
		JSONObject jsonObject = new JSONObject();
		
		//Map<String,Object> map = new HashMap<String,Object>();
		//map.put("JSON", "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
		//jsonObject.append("id", "COMMON.AUTHLOGIN_S");
		//jsonObject.append("url", "/web/login");
		//jsonObject.append("JSON", "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
		jsonObject.accumulate("JSON", "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
		//jsonObject.("JSON", "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
		//tmpStr = "JSON: {\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}" ;
		json = URLEncoder.encode(jsonObject.toString());
		System.out.println(json);		
		json = jsonObject.toString();
		System.out.println(json);		
		
		OutputStream os = conn.getOutputStream();
		os.write(json.getBytes());
		os.flush();
		
		InputStream is = conn.getInputStream();
		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			System.out.println(jsonText);			
		} finally {
			is.close();
		}
		
	}
	  private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
	  }
}
