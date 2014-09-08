import javafx.scene.paint.Stop;
import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import weka.core.Stopwords;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/4/2014.
 */
public class Estimate {

    public static double findRelation(String tweet ,Tokenizer tokenizer,POSTaggerME tagger , Connection connection) throws SQLException {

        tweet = tweet.toLowerCase().replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^'\\w\\s\\,]", "").replaceAll("http\\s*(\\w+)", "");
        String tokens[] = tokenizer.tokenize(tweet);
        String arrText[] = tagger.tag(tokens);

        double score = 0;
        int count = 0;
        int keyword_value = 0;
        for (String match : arrText) {
            Matcher matcher = Pattern.compile("N\\s*(\\w+)").matcher(match);
            if (matcher.find()) {
                String group = tokens[keyword_value];
                System.out.println(tokens[keyword_value]);
                String query = "SELECT * FROM keywords_new where term = '"+group+"' and count_val > 1";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next())
                {
                    double newScore = rs.getDouble("value");
                    long val = rs.getLong("count_val");

                    score = score + newScore/(val-1);
                }
                count++;
            }
            keyword_value++;
        }

        if (count == 0)
            return score;
        else
            return score/count;
    }

    public static Double findRelation2(String tweet,Connection connection) throws SQLException {

        Matcher matcher = Pattern.compile("#\\s*(\\w+)").matcher(tweet);
        double score = 0;
        int count  = 0;
        while (matcher.find()) {
            String query = "SELECT * FROM hashtags where term = '"+matcher.group(1)+"' and count_val > 1";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
            {
                double newScore = rs.getDouble("value");
                long val = rs.getLong("count_val");

                score = score + newScore/(val-1);
            }
            count++;
        }

        if (count == 0)
            return score;
        else
            return score/count;
    }

    public static double findRelation3(String text ,Connection connection) throws SQLException {

        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(text);
        double score = 0;
        int count = 0;
        while (matcher.find()) {
            String query = "SELECT * FROM mentions where term = '"+matcher.group(1)+"' and count_val > 1";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
            {
                //System.out.println("MENTION FOUND");
                double newScore = rs.getDouble("value");
                long val = rs.getLong("count_val");

                score = score + newScore/(val-1);
            }
            count++;
        }

        //System.out.println(score);
        if (count == 0)
            return score;
        else
            return score/count;
    }

    public static Pair<Double,Pair<Double,Double>> getRetweetCount(Connection connection,Tokenizer tokenizer,POSTaggerME tagger,String tweet) throws SQLException {

        double val1 = findRelation(tweet.toLowerCase(),tokenizer,tagger,connection);
        double val2 = findRelation2(tweet.toLowerCase(),connection);
        double val3 = findRelation3(tweet.toLowerCase(),connection);

        System.out.println(val1 + " " + val2 + " " + val3);
        Pair<Double,Pair<Double,Double>> pairPair = new Pair<Double, Pair<Double, Double>>(val1,new Pair<Double,Double>(val2,val3));

        return pairPair;
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        File file = new File("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\data.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        double arr[][] = new double[600][4];

        String query = "SELECT * FROM final_analysis where lang LIKE 'en'";
        //System.out.println(query);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;

        while (rs.next()){
            String tweet = rs.getString("tweet");
            long user_id = rs.getLong("user_id");
            long id = rs.getLong("id");

            Pair<Double,Pair<Double,Double>> pair = getRetweetCount(connection,tokenizer,tagger,tweet);

            arr[count][0] = rs.getLong("followers");
            arr[count][1] = pair.getKey();
            arr[count][2] = pair.getValue().getKey();
            arr[count][3] = pair.getValue().getValue();

            bw.write(String.valueOf(arr[count][0]));
            bw.write(",");
            bw.write(String.valueOf(arr[count][1]));
            bw.write(",");
            bw.write(String.valueOf(arr[count][2]));
            bw.write(",");
            bw.write(String.valueOf(arr[count][3]));
            bw.write(",");
            bw.write(String.valueOf(rs.getDouble("rating")));
            bw.write("\n");

            count++;
        }

        bw.close();
    }
}
