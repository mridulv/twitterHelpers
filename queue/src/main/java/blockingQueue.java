import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;

import twitter4j.*;

/**
 * Created by mridul.v on 8/19/2014.
 */
public class blockingQueue {
    public static ArrayBlockingQueue<Status> queue;

    public blockingQueue() throws InterruptedException {
        queue = new ArrayBlockingQueue<Status>(1000);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();

        Thread.sleep(4000);
    }

    public static void main(String args[]) throws InterruptedException {
        blockingQueue blockingQueue = new blockingQueue();
    }
}
