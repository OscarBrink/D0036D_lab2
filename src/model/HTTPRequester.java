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

    /**
     * Constructor
     *
     * @param apiURLString String consisting of the parameterized API-URL.
     */
    HTTPRequester(String apiURLString) {
        this.apiURLString = apiURLString;
    }

    /**
     * Takes a request containing values for longitude, latitude, and meters
     * above sea-level.
     *
     * @param locationData HashMap containing parameters for the API-URL.
     *                     The parameters are 
     * @return An InputStream recieving data from the API at the requested URL.
     */
    InputStream request(HashMap<String,String> locationData) throws IOException {
        String requestURLString = apiURLString;
        for (HashMap.Entry<String, String> entry : locationData.entrySet()) {
            requestURLString = requestURLString.replaceFirst(entry.getKey(), entry.getValue());
        }


        System.out.println("URL: " + requestURLString);

        return new URL(requestURLString).openStream();
    }

}
