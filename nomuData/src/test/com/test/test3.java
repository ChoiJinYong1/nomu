package test.com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.json.JSONObject;


public class test3 {

	  public static void main(String[] args)  {
		  POST();
	  }
	  
	  public static String POST() {
		  InputStream is = null;
		  String result = "";
		  try {
			  URL urlCon = new URL("http://nomu.hyunjang.co.kr:8001/NomuApiService/rest/web/login");
			  Object obj = new Object();
			  
			  HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
			  String json = "";
			  JSONObject jsonObject = new JSONObject();
			  
			  jsonObject.append("id", "COMMON.AUTHLOGIN_S");
			  jsonObject.append("url", "/web/login");
			  jsonObject.append("request", "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
			  
			  //jsonObject.accumulate("id", "COMMON.AUTHLOGIN_S");
			  //jsonObject.accumulate("url", "/web/login");
			  //jsonObject.accumulate("request", "{\"ROWS\":[{\"REQUEST_ID\":\"S|COMMON.AUTHLOGIN_S\",\"PARAMETER\":{\"USERID\":\"sb0950\",\"USERPW\":\"sbsb7356\",\"APPID\":\"API_WEB\",\"DEVICEID\":\"API_NOMU_M0079\",\"REMARK\":\"IE 8.0\"}}]}");
			  
			  json = jsonObject.toString();

			  System.out.println(json);
			  //httpCon.setRequestProperty("Accept", "application/json");
			  //httpCon.setRequestProperty("Content-type", "application/json");
			  httpCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); 
			  httpCon.setRequestMethod("POST");
			  
			  httpCon.setDoOutput(true);
			  httpCon.setDoInput(true);
			  
			  
			  
			  OutputStream os = httpCon.getOutputStream();
			  os.write(json.getBytes("UTF-8"));
			  OutputStreamWriter wr = new OutputStreamWriter(httpCon.getOutputStream());
			  wr.write(json);
			  wr.flush();
			  //os.write(json.getBytes("euc-kr"));
			  System.out.println(json.getBytes("euc-kr"));
			  //os.flush();
			  //os.close();
			  
			  //Logger.getLogger(os.toString());
			  //result = "Result";
			  try {
				  is = httpCon.getInputStream();
				  if(is != null)
					  result = convertInputStreamToString(is);
				  else
					  result = "Did not Work!";
			  } catch (IOException e) {
				  e.printStackTrace();
			  } finally {
				  httpCon.disconnect();
			  }
			  
		  } catch(IOException e) {
			  e.printStackTrace();
		  } catch(Exception e) {
			  Logger.getLogger("InputStream", e.getLocalizedMessage());
			  //Logger.d(, );
		  }

		  System.out.println(result);
		  //System.out.println(is.toString());
		  return result;
	  }
	  
	  private static String convertInputStreamToString(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();

		}

}
