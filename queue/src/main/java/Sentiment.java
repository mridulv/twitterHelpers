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
public class Sentiment {
    public void getSentiment(String text) throws IOException, URISyntaxException {
        System.out.println(" " + text);
        String urlstr = "http://access.alchemyapi.com/calls/text/TextGetTextSentiment?apikey=849cd6a16603167fca72b5279d47a603af54d8a2&text="+text;
        URL url = new URL(urlstr);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        System.out.println(url.toString());
        try{
            //make connection
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("Content-Type", "application/xml");
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);
            PrintStream ps = new PrintStream(urlc.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String l = null;
            while ((l=br.readLine())!=null) {
                System.out.println(l);
            }
            br.close();
        } catch (Exception e){
        }
    }
}
