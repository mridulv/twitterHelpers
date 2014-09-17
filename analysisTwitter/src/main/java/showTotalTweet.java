import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mridul.v on 9/16/2014.
 */
public class showTotalTweet {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT * FROM analysis_tweets_new";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Map<String,Integer> hashmap = new HashMap<String, Integer>();

        while(rs.next()){
            String timestamp = rs.getString("timestamp");
            String arr[] = timestamp.split(" ");
            String date = arr[1] + " " +arr[2] + " " + arr[5];

            int count = hashmap.containsKey(date) ? hashmap.get(date) : 0;
            hashmap.put(date,count+1);
        }

        for (String str : hashmap.keySet()){
            System.out.println(str + " " + hashmap.get(str));
        }
    }
}
