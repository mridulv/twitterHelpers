import com.mashape.unirest.http.exceptions.UnirestException;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import twitter4j.JSONException;

import java.io.*;
import java.net.*;
import java.sql.*;

/**
 * Created by mridul.v on 9/16/2014.
 */
public class insertLoc {
    public static String getLocation(String text) throws MalformedURLException, URISyntaxException, UnirestException, JSONException
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
            if (str.indexOf("adminArea1") == -1)
                return "world";
            else
                return str.substring(str.indexOf("adminArea1")+13,str.indexOf("adminArea1")+15);
        } catch (Exception e){
            return "world";
        }
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, UnirestException, JSONException, URISyntaxException {
        File folder = new File("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\loc_data\\");

        String out = "C:\\Users\\mridul.v\\Downloads\\twitter_Project\\loc_data_inserted\\";
        File[] listOfFiles = folder.listFiles();

        //BufferedReader buffer = new BufferedReader(new FileReader("C:\\Users\\mridul.v\\Downloads\\Twitter_Sentiment\\backend\\FlumeData.1407786558156"));
        int total = 0;
        for (File file:listOfFiles) {
            BufferedReader buffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
            int duplicates = 0;
            String line;
            String delimStr[];
            PrintWriter writer = new PrintWriter(out + file.getName(), "UTF-8");
            while ((line = buffer.readLine()) != null) {
                delimStr = line.split(",");
                String loc = getLocation(URLEncoder.encode(delimStr[10], "UTF-8"));
                System.out.println(loc);
                writer.println(line+","+loc);
            }
            writer.close();
        }
    }
}
