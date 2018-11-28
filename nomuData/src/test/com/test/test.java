package test.com.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;




public class test {
	public static void main(String[] args) {
        System.out.println("----------------------------------------------------------");
        System.out.println("JSON String »ý¼º");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "À¯Çõ");
        //    "name":"À¯Çõ"
        JSONArray jsonArray = new JSONArray();
        JSONObject school = new JSONObject();
        school.put("subject1", "math");
        school.put("subject2", "korean");
        jsonArray.add(school);
        //    "school":[{"subject1":"math","subject2":"korean"}]
         
        jsonObject.put("school", jsonArray);
         
        System.out.println(jsonObject.toString());
         
        System.out.println("----------------------------------------------------------");
        System.out.println("JSON String ÆÄ½Ì");
        try{
            JSONArray returnSchool = (JSONArray)jsonObject.get("school");
            for(int i=0;i<returnSchool.size();i++){
                JSONObject returnSubject = (JSONObject) returnSchool.get(i);
                System.out.println("subject : "+returnSubject.get("subject1"));
                System.out.println("subject : "+returnSubject.get("subject2"));
            }
            String name = (String)jsonObject.get("name");
            System.out.println("name : " + name );
             
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("----------------------------------------------------------");
    }

}
