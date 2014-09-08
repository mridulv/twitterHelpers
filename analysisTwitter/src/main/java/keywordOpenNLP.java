import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import weka.core.Stopwords;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mridul.v on 9/7/2014.
 */
public class keywordOpenNLP {
    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        String query = "SELECT * FROM analysis_tweets_new WHERE lang LIKE 'en' ";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        Map<String, Double> hashMap = new HashMap<String, Double>();
        Map<String, Integer> hashCount = new HashMap<String, Integer>();

        int count = 0;

        Stopwords stopwords = new Stopwords();

        while (rs.next()) {
            String text = rs.getString("tweet");
            double rating = rs.getLong("rating");

            int keyword_value = 0;
            text = text.toLowerCase().replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^'\\w\\s\\,]", "").replaceAll("http\\s*(\\w+)", "");
            String tokens[] = tokenizer.tokenize(text);
            String arrText[] = tagger.tag(tokens);

            for (String match : arrText) {
                Matcher matcher = Pattern.compile("N\\s*(\\w+)").matcher(match);
                if (matcher.find()){
                    String group = tokens[keyword_value];
                    if (hashMap.containsKey(group)) {
                        double value = hashMap.get(group);
                        int total = hashCount.get(group);
                        value = value + rating;

                        hashCount.put(group, total + 1);
                        hashMap.put(group, value);
                    } else {
                        double value = rating;
                        hashMap.put(group, value);
                        hashCount.put(group, 1);
                    }
                }
                keyword_value++;
            }
            count++;

            if (count % 1000 == 0)
                System.out.println(count);
        }

        PreparedStatement preparedStatement;
        for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
            String group = entry.getKey();
            Double value = entry.getValue();
            int total = hashCount.get(group);

            query = "REPLACE into keywords_new(term,value,count_val) VALUES (?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, group);
            preparedStatement.setDouble(2, value);
            preparedStatement.setInt(3, total);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
    }
}
