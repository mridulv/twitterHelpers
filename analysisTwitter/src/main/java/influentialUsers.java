import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONException;
import weka.core.Stopwords;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mridul.v on 9/3/2014.
 */
public class influentialUsers {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {

        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Stopwords stopwords = new Stopwords();

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);

        // missing functionality:
        Date startDate = new Date();

        Map<Long, Long> hashMap = new HashMap<Long, Long>();
        helper2 bvc = new helper2(hashMap);
        TreeMap<Long, Long> sorted_map = new TreeMap<Long, Long>(bvc);

        String entity = "74286565";

        String query = "SELECT count(*) as total, avg(rating) as rating_avg, followers , user_id from analysis_tweets_new where key_val LIKE '1"+entity+"%' GROUP BY user_id";
        System.out.println(query);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()){
            long user_id = rs.getLong("user_id");
            double rating_avg = rs.getLong("rating_avg");

            double normalized_rating = (rating_avg + (61.8149737505)*rating_avg + 9972.69849548)/2;
            hashMap.put(user_id,(long)rating_avg);
        }

        sorted_map.putAll(hashMap);
        System.out.println(sorted_map.toString());
    }
}
