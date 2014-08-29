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
 * Created by mridul.v on 8/19/2014.
 */
public class helperSentiment {
    public double getSentiment(String text) throws IOException, URISyntaxException {
        String urlstr = "http://access.alchemyapi.com/calls/text/TextGetTextSentiment?outputMode=json&apikey=849cd6a16603167fca72b5279d47a603af54d8a2&text="+text;
        URL url = new URL(urlstr);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        double sentiment = 0;

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
            JSONObject sentiments = new JSONObject(jsonObj.get("docSentiment").toString());
            if (!(sentiments.get("type").toString().equals("neutral"))) {
                sentiment = Double.valueOf(sentiments.getString("score"));
            }
        } catch (Exception e){
        }
        return sentiment;
    }
}
