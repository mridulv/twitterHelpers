/**
 * Created by mridul.v on 8/19/2014.
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ArrayBlockingQueue;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import twitter4j.*;

public class Consumer implements Runnable{

    protected ArrayBlockingQueue<Status> queue;

    public Consumer(ArrayBlockingQueue<Status> queue) {
        this.queue = queue;
    }

    public void run() {
        while (true) {
            try {
                Sentiment sentiment = new Sentiment();
                Status status = queue.take();
                System.out.println("dnljsdlndsas" + status.getText());
                String tweet_json = TwitterObjectFactory.getRawJSON(status);
                DBObject doc = (DBObject) JSON.parse(tweet_json);
                if (status.getText() != null) {
                    System.out.println(status.getText());
                    try {
                        sentiment.getSentiment(status.getText().replaceAll("http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," "));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
