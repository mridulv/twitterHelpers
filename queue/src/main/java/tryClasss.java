/**
 * Created by mridul.v on 8/19/2014.
 */
public class tryClasss {
    public static void main(String args[]){
        String s = "#tweetishappening It is a tweet";
        System.out.println(s.replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)",""));
    }
}
