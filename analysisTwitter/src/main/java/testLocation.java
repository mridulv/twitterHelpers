import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Created by mridul.v on 9/16/2014.
 */
public class testLocation {
    public static void getLocation(String text) throws MalformedURLException, URISyntaxException, UnirestException, JSONException
    {
        int i;
        String urlstr = "http://open.mapquestapi.com/geocoding/v1/address?key=Fmjtd%7Cluur2huz2q%2Cax%3Do5-9wa000&callback=renderOptions&inFormat=kvp&outFormat=json&location="+text;
        System.out.println(urlstr);
        URL url = new URL(urlstr);
        String content = "{}";

        try {
            //make connection
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("Content-Type", "application/json");
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);
            PrintStream ps = new PrintStream(urlc.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String str = "";
            String l = null;
            while ((l = br.readLine()) != null) {
                str = str + l;
            }
            br.close();
            System.out.println(str);
            System.out.println(str.substring(str.indexOf("adminArea1")+13,str.indexOf("adminArea1")+15));
        } catch (Exception e){
        }
    }

    public static void main(String args[]) throws MalformedURLException, UnirestException, JSONException, URISyntaxException, UnsupportedEncodingException {
           String location = "Delhi India";
           getLocation(URLEncoder.encode(location, "UTF-8"));
    }
}
