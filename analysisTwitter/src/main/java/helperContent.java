import twitter4j.JSONArray;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mridul.v on 8/20/2014.
 */
public class helperContent {
    public String getContent(String text) throws IOException, URISyntaxException {
        int i;
        String urlstr = "http://access.alchemyapi.com/calls/text/TextGetRankedConcepts?outputMode=json&apikey=849cd6a16603167fca72b5279d47a603af54d8a2&text="+text;
        URL url = new URL(urlstr);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        String content = "{}";

        try{
            //make connection
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("Content-Type", "application/json");
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);
            PrintStream ps = new PrintStream(urlc.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String str = "";
            String l = null;
            while ((l=br.readLine())!=null) {
                str = str + l;
            }
            br.close();
            JSONObject jsonObj = new JSONObject(str);
            JSONArray jsonArray = new JSONArray(jsonObj.get("concepts").toString());

            JSONArray jsonArray1 = new JSONArray();
            for(i=0;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String concept = json.getString("text");
                String relevance = json.getString("relevance");

                JSONObject json_new = new JSONObject();
                json_new.put("concept",concept);
                json_new.put("relevance",relevance);

                jsonArray1.put(json_new);
            }
            content = jsonArray1.toString();

        } catch (Exception e){
        }
        return content;
    }
}
