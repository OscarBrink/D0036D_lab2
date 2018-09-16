package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;

class HTTPRequester {

    private String apiURLString;
    private URL requestURL;

    HTTPRequester(String apiURLString) {
        this.apiURLString = apiURLString;
    }

    InputStream request(HashMap<String,String> locationData) throws IOException {
        String requestURLString = apiURLString;
        for (HashMap.Entry<String, String> entry : locationData.entrySet()) {
            requestURLString = requestURLString.replaceFirst(entry.getKey(), entry.getValue());
        }

        System.out.println("URL: " + requestURLString);

        // testing
        String sep = File.separator;
        String fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "skelleftea_2242_2018_09_15.xml";
        File inF = new File(fPath);

        fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "test.xml";
        File outF = new File(fPath);

        InputStream targetStream = new FileInputStream(inF);

        Files.copy(targetStream, outF.toPath());

        //new URL(requestURLString).getContent();
        InputStream newStream = new FileInputStream(outF);

        return newStream;
    }

}
