import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/6/2014.
 */
public class dayTime {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        long user_id = 27260086;

        String query = "SELECT * FROM analysis_tweets_new where user_id ="+user_id;

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Map<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("Mon",0);
        hashMap.put("Tue",1);
        hashMap.put("Wed",2);
        hashMap.put("Thu",3);
        hashMap.put("Fri",4);
        hashMap.put("Sat",5);
        hashMap.put("Sun",6);

        int count = 0;
        double time[] = new double[24];
        double day[] = new double[7];

        while (rs.next()){
                String tim = rs.getString("timestamp");
                double rating = rs.getDouble("rating");

                String dayVal = tim.split(" ")[0];
                Integer timeVal = Integer.valueOf(tim.split(" ")[3].substring(0,2));

                time[timeVal] = time[timeVal] + rating;
                day[hashMap.get(dayVal)] = day[hashMap.get(dayVal)] + rating;
        }

        System.out.println(Arrays.toString(time));
        System.out.println(Arrays.toString(day));
    }
}
