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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.http.entity.StringEntity;

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
		String jpStr = response.toString(); 
		reader.close();

		System.out.println(response.toString());
		//System.out.println(jpStr);
		httpClient.close();
		try {
			//JSONParser jp = new JSONParser();
			//JSONObject jo = (JSONObject) jp.parse(jpStr);
			JSONObject jo = new JSONObject(jpStr);
			JSONArray ja = jo.getJSONObject("ROWS").getJSONArray("RESULT_DATA");
			//JSONArray ja = jo.getJSONObject("ROWS").getJSONArray("RESULT_DATA");
			//JSONArray ja = jo.getJSONObject("ROWS").getJSONArray("RESULT_DATA");
			//JSONArray ja = jo.getJSONObject("ROWS").getJSONArray("RESULT_DATA");
			//JSONArray rowInfo = (JSONArray) jo);

			//System.out.println(rowInfo.toString());
			//JSONObject jo2 = (JSONObject) jp.parse(rowInfo.toString());
			//JSONArray rowInfo2 = (JSONArray) jo2.get("RESULT_DATA");
			
			
			System.out.println(ja.toString());
			System.out.println(ja.length());
			for(int i = 0; i <ja.length(); i++) {
				System.out.println("ROW_" + i + " ========================");
				JSONObject ro = (JSONObject) ja.get(i);
				/*
				System.out.println("DEVICEID:==>" + ro.get("DEVICEID"));
				System.out.println("APPID:==>" + ro.get("APPID"));
				System.out.println("AUTHKEY:==>" + ro.get("AUTHKEY"));
				System.out.println("RESULT_CODE:==>" + ro.get("RESULT_CODE"));
				*/
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		
	
		/*
		CloseableHttpClient httpClient2 = HttpClients.createDefault();
		List<NameValuePair> urlParameters2 = new ArrayList<NameValuePair>();
		HttpPost httpPost2 = new HttpPost(POST_URL2);
		httpPost2.addHeader("User-Agent", USER_AGENT);		
		
		String tempStr2 = "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}";
		urlParameters2.add(new BasicNameValuePair("JSON", tempStr2));
		
		HttpEntity postParams2 = new UrlEncodedFormEntity(urlParameters2);
		httpPost2.setEntity(postParams2);

		CloseableHttpResponse httpResponse2 = httpClient2.execute(httpPost2);

		System.out.println("POST Response Status:: "
				+ httpResponse2.getStatusLine().getStatusCode());

		BufferedReader reader2 = new BufferedReader(new InputStreamReader(
				httpResponse2.getEntity().getContent(),"UTF-8"));

		String inputLine2;
		StringBuffer response2 = new StringBuffer();

		while ((inputLine2 = reader2.readLine()) != null) {
			response2.append(inputLine2);
		}
		reader2.close();
		

		// print result
		System.out.println(response2.toString());
		httpClient2.close();
		 */
	}

}