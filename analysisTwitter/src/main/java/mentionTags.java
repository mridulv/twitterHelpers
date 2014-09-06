import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/5/2014.
 */
public class mentionTags {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT * FROM analysis_tweets_new";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        PreparedStatement preparedStatement;
        Map<String,Double> hashMap = new HashMap<String,Double>();
        Map<String,Integer> hashCount = new HashMap<String,Integer>();

        int count  = 0;

        while(rs.next()) {
            String text = rs.getString("tweet");
            double rating = rs.getLong("rating");
            Matcher matcher = Pattern.compile("#\\s*(\\w+)").matcher(text.toLowerCase());
            while (matcher.find()) {
                if (hashMap.containsKey(matcher.group(1))) {
                    double value = hashMap.get(matcher.group(1));
                    int total = hashCount.get(matcher.group(1));

                    value = value + rating;

                    hashCount.put(matcher.group(1),total + 1);
                    hashMap.put(matcher.group(1) , value);

                    query = "REPLACE into hashtags(term,value,count_val) VALUES (?,?,?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, matcher.group(1));
                    preparedStatement.setDouble(2, value);
                    preparedStatement.setInt(3, total+1);

                    preparedStatement.executeUpdate();
                } else {
                    double value = rating;
                    hashMap.put(matcher.group(1),value);
                    hashCount.put(matcher.group(1),1);
                    query = "REPLACE into hashtags(term,value,count_val) VALUES (?,?,?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, matcher.group(1));
                    preparedStatement.setDouble(2, value);
                    preparedStatement.setInt(3, 1);
                    preparedStatement.executeUpdate();
                }
                preparedStatement.close();
            }

            count++;

            if (count % 1000 == 0)
                System.out.println(count);
        }

    }
}
