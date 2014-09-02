import com.google.gson.Gson;
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
 * Created by mridul.v on 9/1/2014.
 */
public class tweetAnalysis {
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {

        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Stopwords stopwords = new Stopwords();

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        Connection connection2 = DriverManager.getConnection(dbUrl, username, password);

        // missing functionality:
        Date startDate = new Date();
        String query = "SELECT * from analysis_tweets_new WHERE key_val LIKE '12425151%'";

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0 ;

        Map<String,Integer> hashMap = new HashMap<String, Integer>();
        helper bvc =  new helper(hashMap);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

        while(rs.next()){
            count++;
            String text = rs.getString("tweet").toLowerCase().replaceAll("@\\s*(\\w+)","").replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)","");

            String arr_text[] = text.split(" ");

            for (String arr2 : arr_text){
                if (!(stopwords.is(arr2) || arr2.equals(""))) {
                    if (hashMap.containsKey(arr2))
                        hashMap.put(arr2, hashMap.get(arr2) + 1);
                    else
                        hashMap.put(arr2, 1);
                }
            }
        }

        sorted_map.putAll(hashMap);
        System.out.println(sorted_map.toString());

        Gson gson = new Gson();
        String json = gson.toJson(sorted_map);
        System.out.println("json = " + json);

        PreparedStatement preparedStatement;

        String insert = "INSERT into entity_vector VALUES (?,?)";
        preparedStatement = connection2.prepareStatement(insert);
        preparedStatement.setLong(1,2425151);
        preparedStatement.setString(2, json);
        preparedStatement.executeUpdate();
        System.out.println(count);

    }
}
