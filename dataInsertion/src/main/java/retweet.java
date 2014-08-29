import java.math.BigInteger;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by mridul.v on 8/28/2014.
 */
public class retweet {
    public static void main(String args[]) throws ClassNotFoundException, ParseException, SQLException {
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbClass = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        Connection connectionAux = DriverManager.getConnection(dbUrl, username, password);
        String query = "SELECT * FROM analysis_tweets";

        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = statement.executeQuery(query);

        int count  = 0;
        Date startDate = new Date();
        while (rs.next()){
            String key_val = rs.getString("key_val");
            String aux = "SELECT * FROM new_tweets where key_val LIKE '" + key_val + "'";

            Statement statementAux = connectionAux.createStatement();
            ResultSet rsAux = statementAux.executeQuery(aux);
            rsAux.absolute(1);


            System.out.println(rsAux.getString("tweet") + aux + rsAux.getString("retweet"));

            int k = Integer.valueOf(rsAux.getString("retweet"));
            int k2 = Integer.valueOf(rsAux.getString("favourites"));

            System.out.println("count is " + count + " ");

            rs.updateLong("retweet",k);
            rs.updateLong("favourites",k2);
            rs.updateRow();

            count++;
        }
    }
}
