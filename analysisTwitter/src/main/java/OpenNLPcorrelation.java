import com.mashape.unirest.http.exceptions.UnirestException;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import twitter4j.JSONException;
import weka.core.Stopwords;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/10/2014.
 */
public class OpenNLPcorrelation {
    public static TreeMap callFunc(String entity_id) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {

        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);
        Connection connection2 = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT * from analysis_tweets_new WHERE key_val LIKE '1"+entity_id+"%' and lang LIKE 'en'";
        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;

        Map<String, Double> hashMap = new HashMap<String, Double>();
        Map<String, Double> hashMap2 = new HashMap<String, Double>();
        helper3 bvc = new helper3(hashMap2);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);

        while (rs.next()) {
            count++;
            String arr = rs.getString("tweet");
            double rating = rs.getDouble("rating");

            int keyword_value = 0;
            arr = arr.toLowerCase().replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^'\\w\\s\\,]", "").replaceAll("'\\p{L}+"," ").replaceAll("'"," ").replaceAll("http\\s*(\\w+)", "");
            String tokens[] = tokenizer.tokenize(arr);
            String arrText[] = tagger.tag(tokens);

            for (String match : arrText) {
                Matcher matcher = Pattern.compile("N\\s*(\\w+)").matcher(match);
                if (matcher.find()){
                    String group = tokens[keyword_value];
                    if (hashMap.containsKey(group)) {
                        double value = hashMap.get(group);
                        value = value + rating;
                        hashMap.put(group, value);
                    } else {
                        double value = rating;
                        hashMap.put(group, value);
                    }
                }
                keyword_value++;
            }
        }

        for(String key : hashMap.keySet()){
            double value = hashMap.get(key);

            String queryNew = "SELECT * FROM keywords_new WHERE term LIKE '"+key+"' ";
            Statement stmtNew = connection2.createStatement();
            ResultSet rsNew = stmtNew.executeQuery(queryNew);

            if (rsNew.absolute(1)){
                double num = rsNew.getDouble("value");
                value = value/num;
                hashMap2.put(key,value);
            }
        }

        sorted_map.putAll(hashMap2);
        System.out.println(sorted_map);
        return sorted_map;
    }

    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {

        Class.forName(conn.dbClass);
        Connection connection2 = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        java.util.Date startDate = new java.util.Date();

        Map<Long,Double> hashMap = new HashMap<Long, Double>();
        helper4 bvc =  new helper4(hashMap);
        TreeMap<Long,Double> sorted_map = new TreeMap<Long,Double>(bvc);

        String entity = "74286565";

        Map<String,Double> myMap = callFunc(entity);

        String queryAux = "SELECT * from user_vector";
        Statement stmtAux = connection2.createStatement();
        ResultSet rsAux = stmtAux.executeQuery(queryAux);

        long user_id = 0;
        long max = Long.MIN_VALUE;

        while (rsAux.next()) {
            long user = rsAux.getLong("user_id");
            long rating = rsAux.getLong("rating");

            System.out.println(user);
            Map<String, Double> myMapAux = new HashMap<String, Double>();
            String sAux = rsAux.getString("keyword_vector");
            sAux = sAux.substring(1, sAux.length() - 1);
            if (!sAux.equals("")) {
                String[] pairsAux = sAux.split(",");
                for (int i = 0; i < pairsAux.length / 2; i++) {
                    String pair = pairsAux[i];
                    String[] keyValue = pair.split(":");
                    int val = keyValue[0].length();
                    myMapAux.put(keyValue[0].substring(1,val - 1), Double.valueOf(keyValue[1]));
                }

                double score = 0;

                for (Map.Entry<String, Double> entry : myMap.entrySet()) {
                    String key = entry.getKey();
                    double value = entry.getValue();
                    if (myMapAux.containsKey(key)) {
                        score = score + myMapAux.get(key) * value;
                    }
                }

                score = score * rating;

                hashMap.put(user , score);
            }
        }

        sorted_map.putAll(hashMap);
        System.out.println(sorted_map.toString());

        int count = 0 ;

        for (Map.Entry<Long , Double> entry : sorted_map.entrySet()) {
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
        java.util.Date endDate = new java.util.Date();

        System.out.println(myMap);
        System.out.println(endDate.getTime() - startDate.getTime());
    }
}
