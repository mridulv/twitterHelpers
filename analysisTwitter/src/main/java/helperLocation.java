import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import twitter4j.JSONException;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by mridul.v on 8/21/2014.
 */
public class helperLocation {
    public String getLocation(String text) throws IOException, UnirestException, JSONException {
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(text).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
        String str = "World";
        if (geocoderResponse.getStatus().value().equals("OK")) {
            BigDecimal lat = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLat();
            BigDecimal lng = geocoderResponse.getResults().get(0).getGeometry().getLocation().getLng();
            HttpResponse<String> response = Unirest.get("http://api.geonames.org/countryCode?lat="+lat+"&lng="+lng+"&username=drbakshi").asString();
            if (response.getBody().length() < 10) {
                return response.getBody();
            }
        }
        return str;
    }
}
