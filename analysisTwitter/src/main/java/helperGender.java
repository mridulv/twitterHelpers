import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created by mridul.v on 8/21/2014.
 */
public class helperGender {
    public String getGender(String text) throws MalformedURLException, URISyntaxException, UnirestException, JSONException {

        HttpResponse<JsonNode> response = Unirest.get("http://api.genderize.io?name="+text).asJson();
        JSONObject json = new JSONObject(response.getBody().toString());
        System.out.println(json.toString());
        return json.getString("gender");
    }
}
