package model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;

/**
 *
 * @author  Oscar Brink
 *          2018-09-16
 */
class HTTPRequester {

    private String apiURLString;
    private File tempXMLFile;

    HTTPRequester(String apiURLString, String tempXMLFilePath) {
        this.apiURLString = apiURLString;
        this.tempXMLFile = new File(tempXMLFilePath);
    }

    File request(HashMap<String,String> locationData) throws IOException {
        String requestURLString = apiURLString;
        for (HashMap.Entry<String, String> entry : locationData.entrySet()) {
            requestURLString = requestURLString.replaceFirst(entry.getKey(), entry.getValue());
        }

        this.copyToTempXML(new URL(requestURLString));

        System.out.println("URL: " + requestURLString);

        return this.tempXMLFile;
    }

    private void copyToTempXML(URL requestURL) throws IOException {
        Files.copy(
                requestURL.openStream(),
                this.tempXMLFile.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );
    }

}
