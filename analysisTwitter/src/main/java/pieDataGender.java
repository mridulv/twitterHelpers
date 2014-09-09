import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mridul.v on 9/8/2014.
 */
public class pieDataGender {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        long user_id = 27260086;

        String query = "SELECT * FROM analysis_tweets where key_val LIKE '120536157%'";

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

        Map<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("male",0);
        hashMap2.put("female",1);
        hashMap2.put("unknown",2);

        int count = 0;
        double gender[] = new double[3];
        double day[] = new double[7];

        while (rs.next()){
            String tim = rs.getString("timestamp");
            String gen = rs.getString("groups");
            double rating = rs.getDouble("rating");

            String dayVal = tim.split(" ")[0];
            Integer timeVal = Integer.valueOf(tim.split(" ")[3].substring(0,2));

            gender[hashMap2.get(gen)] = gender[hashMap2.get(gen)] + rating;
            day[hashMap.get(dayVal)] = day[hashMap.get(dayVal)] + rating;
        }

        System.out.println(Arrays.toString(gender));
        System.out.println(Arrays.toString(day));
    }
}
