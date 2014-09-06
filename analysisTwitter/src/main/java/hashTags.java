import weka.core.Stopwords;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/5/2014.
 */
public class hashTags {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT * FROM analysis_tweets_new";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        PreparedStatement preparedStatement;
        Map<String, Double> hashMap = new HashMap<String, Double>();
        Map<String, Integer> hashCount = new HashMap<String, Integer>();

        int count = 0;

        Stopwords stopwords = new Stopwords();

        while (rs.next()) {
            String text = rs.getString("tweet");
            double rating = rs.getLong("rating");
            String arrText[] = text.toLowerCase().replaceAll("@\\p{L}+", "").replaceAll("#\\p{L}+", "").replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]", "").replaceAll(",", " ").replaceAll("http\\s*(\\w+)", "").split(" ");
            for (String group : arrText) {
                if (!(stopwords.is(group) || group.matches("[-+]?\\d*\\.?\\d+"))) {
                    if (hashMap.containsKey(group)) {
                        double value = hashMap.get(group);
                        int total = hashCount.get(group);

                        value = value + rating;

                        hashCount.put(group, total + 1);
                        hashMap.put(group, value);

                        query = "REPLACE into keywords(term,value,count_val) VALUES (?,?,?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, group);
                        preparedStatement.setDouble(2, value);
                        preparedStatement.setInt(3, total + 1);

                        preparedStatement.executeUpdate();
                    } else {
                        double value = rating;
                        hashMap.put(group, value);
                        hashCount.put(group, 1);
                        query = "REPLACE into keywords(term,value,count_val) VALUES (?,?,?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, group);
                        preparedStatement.setDouble(2, value);
                        preparedStatement.setInt(3, 1);
                        preparedStatement.executeUpdate();
                    }
                    preparedStatement.close();
                }
            }

            count++;

            if (count % 1000 == 0)
                System.out.println(count);
        }
    }
}