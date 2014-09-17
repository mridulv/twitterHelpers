import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import sun.beans.editors.DoubleEditor;
import twitter4j.JSONException;
import weka.core.Stopwords;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/10/2014.
 */
public class OpenNLPUser {
    public static String removeURLS(String text)
    {
        String regexp = "\\(?\\bhttps?://[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        text = text.replaceAll(regexp,"");
        return text;
    }
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException, URISyntaxException, UnirestException, JSONException {

        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        Stopwords stopwords = new Stopwords();

        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);
        Connection connection2 = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);
        Connection connection3 = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        // missing functionality:
        Date startDate = new Date();
        String query = "SELECT username,user_id,GROUP_CONCAT(tweet SEPARATOR ',') as final_tweet,AVG(rating) as avg_rating,count(*) as total FROM final_tweet_analysis GROUP BY user_id HAVING avg_rating > 500";

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0 ;
        while(rs.next()){
            String text = rs.getString("final_tweet").toLowerCase();
            int t = rs.getInt("total");
            long user_id = rs.getLong("user_id");
            long rating = rs.getLong("avg_rating");
            String username = rs.getString("username");

            rating = (long)(rating + (61.8149737505)*rating + 9972.69849548)/2;

            String arr_text[] = text.split(",");

            PreparedStatement preparedStatement;
            Map<String,Double> hashMap = new HashMap<String, Double>();
            Map<String,Double> hashMap2 = new HashMap<String, Double>();
            helper3 bvc =  new helper3(hashMap2);
            TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);

            for (String arr: arr_text){
                int keyword_value = 0;
                arr = removeURLS(arr.toLowerCase()).replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^\\w\\s\\,]", " ");
                String tokens[] = tokenizer.tokenize(arr);
                String arrText[] = tagger.tag(tokens);

                for (String match : arrText) {
                    Matcher matcher = Pattern.compile("N\\s*(\\w+)").matcher(match);
                    if (matcher.find()){
                        String group = tokens[keyword_value];
                        if ((!stopwords.is(group)) && group.length() > 2) {
                            if (hashMap.containsKey(group)) {
                                double value = hashMap.get(group);
                                value = value + rating;
                                hashMap.put(group, value);
                            } else {
                                double value = rating;
                                hashMap.put(group, value);
                            }
                        }
                    }
                    keyword_value++;
                }
            }

            for(String key : hashMap.keySet()){
                double value = hashMap.get(key);

                String queryNew = "SELECT * FROM keywords_new WHERE term LIKE '"+key+"' ";
                Statement stmtNew = connection3.createStatement();
                ResultSet rsNew = stmtNew.executeQuery(queryNew);

                if (rsNew.absolute(1)){
                    double num = rsNew.getDouble("value");
                    value = value/num;
                    hashMap2.put(key,value);
                }
            }

            sorted_map.putAll(hashMap2);
            System.out.println(sorted_map.toString());

            Gson gson = new Gson();
            String json = gson.toJson(sorted_map);
            System.out.println("json = " + json);

            String insert = "REPLACE into user_vector(username,user_id,keyword_vector,rating) VALUES (?,?,?,?)";
            preparedStatement = connection2.prepareStatement(insert);
            preparedStatement.setString(1,username);
            preparedStatement.setLong(2,user_id);
            preparedStatement.setString(3, json);
            preparedStatement.setLong(4, rating);
            preparedStatement.executeUpdate();
            count++;
            System.out.println(count);
        }
        System.out.println(count);

    }
}
