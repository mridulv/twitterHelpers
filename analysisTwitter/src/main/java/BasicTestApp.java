import com.semantria.CallbackHandler;
import com.semantria.Session;
import com.semantria.mapping.Document;
import com.semantria.mapping.output.DocAnalyticData;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;



public class BasicTestApp
{
    private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 10; //in millisec

	public static void main(String args[]) throws ClassNotFoundException, SQLException, InterruptedException {
        Class.forName(conn.dbClass);
        Connection connection = DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

		String key = "0485b0a7-9386-49cb-8dad-63b509a3251d";
		String secret = "9075f8ac-b727-418d-b3a3-f1669c8634da";

		if( args != null && args.length == 2 )
		{
			key = args[0];
			secret = args[1];
		}

        Date startdate = new Date();
        String query = "SELECT * FROM analysis_tweets_new LIMIT  1,5";

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0 ;
        List<String> initialTexts = new ArrayList<String>();
        while(rs.next()) {
            initialTexts.add(rs.getString("tweet"));
        }
        Session session = Session.createSession(key, secret, true);
        session.setCallbackHandler(new CallbackHandler());
        for(String text : initialTexts)
        {
            String uid = UUID.randomUUID().toString();
            // Creates a sample document which need to be processed on Semantria
            // Queues document for processing on Semantria service
            if( session.queueDocument( new Document( uid, text )) == 202)
            {
                System.out.println("\"" + uid + "\" document queued succsessfully.");
            }
        }
        System.out.println();
        Thread.sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
        List<DocAnalyticData> processed = new ArrayList<DocAnalyticData>();

        while(processed.size() < initialTexts.size())
        {
            // Requests processed results from Semantria service
            List<DocAnalyticData> temp = session.getProcessedDocuments();
            processed.addAll(temp);
            if(processed.size() >= initialTexts.size()) break;
            System.out.println("Retrieving your processed results...");
            Thread.currentThread().sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
        }
        // Output results
        ArrayList<Float> arrayList = new ArrayList<Float>(500);
        for(DocAnalyticData doc : processed)
        {
            arrayList.add(doc.getSentimentScore());
        }

        Date endDate = new Date();

        System.out.println(endDate.getTime() - startdate.getTime());
        System.out.println(arrayList.toString());
	}
}
