package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class test {

	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String GET_URL = "http://hmkcode.appspot.com/jsonservlet";

	private static final String POST_URL = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login";
	private static final String POST_URL2 = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/workercommon";
	//private static final String POST_URL = "http://httpbin.org/post";

	public static void main(String[] args) throws IOException {
		sendGET();
		System.out.println("GET DONE");
		sendPOST();
		System.out.println("POST DONE");
	}

	private static void sendGET() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(GET_URL);
		httpGet.addHeader("User-Agent", USER_AGENT);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

		System.out.println("GET Response Status:: "
				+ httpResponse.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent()));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		// print result
		System.out.println(response.toString());
		httpClient.close();
	}

	private static void sendPOST() throws IOException {

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


		System.out.println(response.toString());
		httpClient.close();
	
		
	}

}
