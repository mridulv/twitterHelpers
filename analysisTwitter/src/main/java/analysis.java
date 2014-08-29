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

        // missing functionality:
        Date startDate = new Date();
        String query = "SELECT * FROM new_tweets where lang LIKE 'en' ";

        Statement stmt = connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count=0;

        while(rs.next()){
            if (count % 1000 == 0)
                System.out.println(count);

//            if (count <= 10000)
//                analysisText(rs.getString("tweet").toLowerCase().replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]", "").replaceAll(",", " ").replaceAll("http\\s*(\\w+)", ""));
//            else
//                break;

            count++;
        }


        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        System.out.println(msElapsedTime);
    }
}
