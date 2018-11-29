package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class test6 {

	public static void main(String[] args) throws IOException {
		//final URL url = new URL("http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/workercommon");
		final URL url = new URL("http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login");
		final URLConnection urlConnection = url.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type", "application/json");
		urlConnection.setRequestProperty("Accept", "application/json");
		urlConnection.connect();
		final OutputStream os = urlConnection.getOutputStream();
		
		//String wrStr = URLEncoder.encode("{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
		String wrStr = "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}";
		//String wrStr= "{\"ROWS\":[{\"REQUEST_ID\":\"S|USERAPI.WORKING_SEARCH_S\",\"PARAMETER\":{\"COM_CD\":\"M0079\",\"SITE_CD\":\"S300011863\",\"REQDATE\":\"2018-11-15\",\"RES_NO\":\"\"}}]}";
		System.out.println(wrStr);
		
		os.write(wrStr.getBytes());
		os.flush();
		//final InputStream inputStream = urlConnection.getInputStream();		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8")));		
		System.out.println(br.readLine());
		
	}
}
