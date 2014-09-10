import java.sql.*;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mridul.v on 8/20/2014.
 */
public class analysis {
    static HashMap<String,Integer> hashMap = new HashMap<String, Integer>();

    public static void analysisText(String text) throws ClassNotFoundException, SQLException {
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        PreparedStatement preparedStatement;

        String[] arr = text.split(" ");
        for (String str: arr) {
            int set = 0;
            String query = "SELECT * FROM term_frequency where keyword LIKE '" + str + "'";

            Statement statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
//            statement.setFetchSize(Integer.MIN_VALUE);

            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                set = 1;
                int index = rs.findColumn("occurence");
                int val = rs.getInt("occurence");
                rs.updateInt(index,val+1);
                rs.updateRow();
            }
            if (set == 0){
                query = "REPLACE into term_frequency(keyword) VALUES (?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, str);
                preparedStatement.executeUpdate();

                preparedStatement.close();
            }
        }

    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        Connection connection2 = DriverManager.getConnection(dbUrl, username, password);

        // missing functionality:
        Date startDate = new Date();
        String query = "SELECT * FROM analysis_tweets";

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count=0;
        PreparedStatement preparedStatement;

        while(rs.next()){
            String queryAux = "REPLACE into analysis_tweets_new(key_val,id,timestamp,seconds,user_id,username,followers,mention_id,hashtag,retweet,favourites,rating,lat_lng,place,lang,tweet,sentiment,country,keywords,groups) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            long reTweet = Long.valueOf(rs.getLong("retweet"));
            double tweetRating = (reTweet + (0.88490419729401604)*reTweet + 132.30818861707201)/2;

            preparedStatement = connection2.prepareStatement(queryAux);
            preparedStatement.setString(1, rs.getString("key_val"));
            preparedStatement.setLong(2, rs.getLong("id"));
            preparedStatement.setString(3, rs.getString("timestamp"));
            preparedStatement.setLong(4, rs.getLong("seconds"));
            preparedStatement.setLong(5, rs.getLong("user_id"));
            preparedStatement.setString(6, rs.getString("username"));
            preparedStatement.setLong(7, rs.getLong("followers"));
            preparedStatement.setString(8, rs.getString("mention_id"));
            preparedStatement.setString(9, rs.getString("hashtag"));
            preparedStatement.setLong(10, rs.getLong("retweet"));
            preparedStatement.setLong(11, rs.getLong("favourites"));
            preparedStatement.setFloat(12 , (float)tweetRating);
            preparedStatement.setString(13, rs.getString("lat_lng"));
            preparedStatement.setString(14, rs.getString("place"));
            preparedStatement.setString(15, rs.getString("lang"));
            preparedStatement.setString(16, rs.getString("tweet"));
            preparedStatement.setDouble(17, rs.getDouble("sentiment"));
            preparedStatement.setString(18, rs.getString("country"));
            preparedStatement.setString(19, rs.getString("keywords"));
            preparedStatement.setString(20, rs.getString("groups"));
            preparedStatement.executeUpdate();
        }


        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        System.out.println(msElapsedTime);
    }
}
