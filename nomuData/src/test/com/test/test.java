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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class test {

	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String POST_URL2 = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/workercommon";

	public static void main(String[] args) throws IOException {
		sendPOST();
		System.out.println("POST DONE");
	}

	private static void sendPOST() throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL2);
		httpPost.addHeader("User-Agent", USER_AGENT);
		httpPost.addHeader("DEVICEID", "API_NOMU_M0079");
		httpPost.addHeader("APPID", "API_WEB");
		httpPost.addHeader("AUTHKEY", "fae6416b-f38c-4606-83d1-cc960bbef9ce");
		

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		
		String tempStr = "{\"ROWS\":[{\"REQUEST_ID\":\"S|USERAPI.WORKING_SEARCH_S\",\"PARAMETER\":{\"COM_CD\":\"M0079\",\"SITE_CD\":\"S300011863\",\"REQDATE\":\"2018-11-15\",\"RES_NO\":\"\"}}]}";
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

		System.out.println(jpStr);
	}
}