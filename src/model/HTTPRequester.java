package model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
class HTTPRequester {

    private String apiURLString;

    HTTPRequester(String apiURLString) {
        this.apiURLString = apiURLString;
    }

    InputStream request(HashMap<String,String> locationData) throws IOException {
        String requestURLString = apiURLString;
        for (HashMap.Entry<String, String> entry : locationData.entrySet()) {
            requestURLString = requestURLString.replaceFirst(entry.getKey(), entry.getValue());
        }


        System.out.println("URL: " + requestURLString);

        return new URL(requestURLString).openStream();
    }

}
