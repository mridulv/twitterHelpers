import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONArray;
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
public class userAnalysis {
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
        String query = "SELECT user_id,GROUP_CONCAT(tweet SEPARATOR ',') as final_tweet,AVG(rating) as avg_rating,count(*) as total FROM analysis_tweets_new GROUP BY user_id HAVING avg_rating > 100";

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0 ;
        while(rs.next()){
            String text = rs.getString("final_tweet").toLowerCase().replaceAll("@\\s*(\\w+)","").replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)","");
            int t = rs.getInt("total");
            long user_id = rs.getLong("user_id");
            long rating = rs.getLong("avg_rating");

            String arr_text[] = text.split(",");

            PreparedStatement preparedStatement;
            Map<String,Integer> hashMap = new HashMap<String, Integer>();
            helper bvc =  new helper(hashMap);
            TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

            for (String arr: arr_text){
                String arrAux[] = arr.split(" ");
                for (String arr2 : arrAux){
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

            String insert = "INSERT into user_vector VALUES (?,?,?)";
            preparedStatement = connection2.prepareStatement(insert);
            preparedStatement.setLong(1,user_id);
            preparedStatement.setString(2, json);
            preparedStatement.setLong(3, rating);
            preparedStatement.executeUpdate();

        }
        System.out.println(count);

    }
}

