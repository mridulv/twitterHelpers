import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import weka.core.Stopwords;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by mridul.v on 9/1/2014.
 */
public class correlation {
    public static TreeMap callFunc(String entity_id) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {

        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Stopwords stopwords = new Stopwords();

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        Connection connection2 = DriverManager.getConnection(dbUrl, username, password);

        // missing functionality:
        String query = "SELECT * from analysis_tweets_new WHERE key_val LIKE '1"+entity_id+"%'";
        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;

        Map<String, Integer> hashMap = new HashMap<String, Integer>();
        helper bvc = new helper(hashMap);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

        while (rs.next()) {
            count++;
            String text = rs.getString("tweet").toLowerCase().replaceAll("@\\s*(\\w+)", "").replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]", "").replaceAll(",", " ").replaceAll("http\\s*(\\w+)", "");

            String arr_text[] = text.split(" ");

            for (String arr2 : arr_text) {
                if (!(stopwords.is(arr2) || arr2.equals(""))) {
                    if (hashMap.containsKey(arr2))
                        hashMap.put(arr2, hashMap.get(arr2) + 1);
                    else
                        hashMap.put(arr2, 1);
                }
            }
        }

        sorted_map.putAll(hashMap);
        System.out.println(sorted_map);
        return sorted_map;
    }

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

        Map<Long,Long> hashMap = new HashMap<Long, Long>();
        helper2 bvc =  new helper2(hashMap);
        TreeMap<Long,Long> sorted_map = new TreeMap<Long,Long>(bvc);

        String entity = "20536157";

        Map<String,Integer> myMap = callFunc(entity);

        String queryAux = "SELECT * from user_vector";
        Statement stmtAux = connection2.createStatement();
        ResultSet rsAux = stmtAux.executeQuery(queryAux);

        long user_id = 0;
        long max = Long.MIN_VALUE;

        while (rsAux.next()) {
            long user = rsAux.getLong("user_id");
            long rating = rsAux.getLong("rating");

            System.out.println(user);
            Map<String, Integer> myMapAux = new HashMap<String, Integer>();
            String sAux = rsAux.getString("keyword_vector");
            sAux = sAux.substring(1, sAux.length() - 1);
            if (!sAux.equals("")) {
                String[] pairsAux = sAux.split(",");
                for (int i = 0; i < pairsAux.length / 2; i++) {
                    String pair = pairsAux[i];
                    String[] keyValue = pair.split(":");
                    int val = keyValue[0].length();
                    myMapAux.put(keyValue[0].substring(1,val - 1), Integer.valueOf(keyValue[1]));
                }

                long score = 0;

                for (Map.Entry<String, Integer> entry : myMap.entrySet()) {
                    String key = entry.getKey();
                    int value = entry.getValue();
                    if (myMapAux.containsKey(key)) {
                        score = score + myMapAux.get(key) * value;
                    }
                }

                score = score * rating;

                hashMap.put(user , score);
//                    if (score > max) {
//                        user_id = user;
//                        max = score;
//                    }
            }
        }
        sorted_map.putAll(hashMap);
        System.out.println(sorted_map.toString());

        int count = 0 ;

        for (Map.Entry<Long , Long> entry : sorted_map.entrySet()) {
            String query2 = "SELECT * FROM analysis_tweets_new WHERE key_val LIKE '1"+entity+"%' and user_id = " +entry.getKey();
            System.out.println(query2);
            Statement stmt2 = connection2.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            if (count == 10)
                break;
            if (!rs2.next()){
                System.out.println("the maximum correlated user id " + entry.getKey() + " with a score of " + entry.getValue());
                count++;
            }
        }
        Date endDate = new Date();

        System.out.println(endDate.getTime() - startDate.getTime());
    }
}
