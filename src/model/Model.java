package model;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import controller.Controller;
import org.xml.sax.SAXException;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import view.View;

// Testing
// UNIX : String fPath = System.getProperty("user.dir") + "/../testfiles/places.xml";
// WIN  : String fPath = System.getProperty("user.dir") + "\\testfiles\\places.xml";

/**
 * Central class of the data model.
 * 
 * @author  Oscar Brink
 *          2018-10-09
 *
 */
public class Model {
    
    private static SAXParserFactory parserFactory;

    private View view;

    private SAXParser saxParser;
    private WeatherHandler weatherHandler;
    private PlacesHandler placesHandler;
    private File placesFile;

    private String  tempXMLFilePath,
                    cacheFilePath;

    private long leaseTime = 1200; // default caching-lease = 20 min

    private HashMap<String, Long> cacheLeases;

    private HTTPRequester httpRequester;

    private XMLWriter xmlWriter;


    /**
     * Constructor.
     */
    public Model() throws ParserConfigurationException, SAXException {
        String sep = File.separator;
        String fPath = System.getProperty("user.dir") + sep + "testfiles" + sep + "places.xml";
        this.placesFile = new File(fPath);

        parserFactory = SAXParserFactory.newInstance();

        this.saxParser = parserFactory.newSAXParser();
        this.placesHandler = new PlacesHandler();
        this.weatherHandler = new WeatherHandler();

        try {
            this.readStoredCacheLeases();
        } catch (IOException e) {
            this.cacheLeases = new HashMap<String, Long>();
        }

        this.tempXMLFilePath = System.getProperty("user.dir") + sep + "testfiles" + sep + "test.xml";
        this.cacheFilePath = System.getProperty("user.dir") + sep + "testfiles" + sep + "cache" + sep;
        this.httpRequester = new HTTPRequester("https://api.met.no/weatherapi/locationforecast/1.9/?lat=latitude&lon=longitude&msl=altitude");

        this.xmlWriter = new XMLWriter(cacheFilePath);
    }

    /**
     * Set the view that the data model pushes to.
     *
     * @param view View that the data model pushes info to.
     */
    public void setView(View view) {
        this.view = view;
    }

    public void storeCacheLeases() throws IOException {
        List<String> textLines = new ArrayList<String>();
        for (HashMap.Entry<String, Long> entry : cacheLeases.entrySet()) {
            textLines.add(entry.getKey() + " " + entry.getValue().toString());
        }
        Path file = Paths.get(this.cacheFilePath + "cacheLeases.txt");
        Files.write(file, textLines, StandardCharsets.UTF_8);
    }

    private void readStoredCacheLeases() throws IOException {
        for (String line:
        Files.readAllLines(Paths.get(this.cacheFilePath + "cacheLeases.txt"), StandardCharsets.UTF_8)) {
            this.cacheLeases.put(
                    line.replaceAll(" .*", line),
                    Long.valueOf(line.replaceAll(".* ", line))
            );
        }
    }

    /*
     * Checks if lease-time has passed. Returns false if it has.
     */
    private boolean checkCacheLease(String placeName) {
        for (HashMap.Entry<String, Long> entry : this.cacheLeases.entrySet()) {
            System.out.println(entry);
            if (entry.getKey().equals(placeName)) {
                return ((entry.getValue() - (System.currentTimeMillis() / 1000)) > 0);
            }
        }
        return false;
    }

    /*
     * Called when new data has been retrieved from API, sets a new
     * cache-lease for the place.
     */
    private long setCacheLease(String placeName) {
        long cacheLeaseTime = (System.currentTimeMillis() / 1000) + this.leaseTime;
        this.cacheLeases.put(placeName, cacheLeaseTime);
        return cacheLeaseTime;
    }


    /**
     * Sets the time until cached weather data retrieved from the api has 
     * expired. After this time the application will get data from the api.
     *
     * @param leaseTime Data expiration time in seconds.
     */
    public void setLeaseTime(long leaseTime) {
        if (leaseTime >= 600) {
            this.leaseTime = leaseTime;
            this.xmlWriter.setLeaseTime(leaseTime);
        } else {
            String message = "Lease time must be at least 10 minutes (600 s).";
            throw new IllegalArgumentException(message);
        }
    }

    private void cacheData(String placeName) throws IOException {
        this.setCacheLease(placeName); // TODO Here is the cache thing

        Files.copy(
                Paths.get(tempXMLFilePath),
                Paths.get(cacheFilePath + placeName + ".xml"),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );
    }

    private void copyToCacheXML(String placeName, InputStream requestInput)
            throws IOException {
        Files.copy(
                requestInput,
                Paths.get(cacheFilePath + placeName + ".xml"),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );
    }

    /**
     * Takes a request from controller and gets the data either from cache or
     * from the met.no API.
     *
     * @param placeName The name of the place to get weather data from. This
     *                  has to be specified in the file places.xml
     * @param date The date to get weather data for.
     * @param time The time to get weather data for.
     * @throws IOException
     * @throws SAXException
     */
    public void request(String placeName, String date, String time)
            throws IOException, SAXException {

        this.weatherHandler.setDateTime(date, time);
        this.weatherHandler.resetCachingMode(); // caching mode not yet impl.

        this.view.updateTemperature(getWeatherData(placeName).get("temperature"));
    }

    private HashMap<String, String> getWeatherData(String placeName)
            throws SAXException, IOException {

        if (!checkCacheLease(placeName)) {
            this.copyToCacheXML(
                    placeName,
                    this.httpRequester.request(this.getPlaceData(placeName))
            );
            this.setCacheLease(placeName); // TODO Remove
        }

        try {
            this.saxParser.parse(
                    new File(cacheFilePath + placeName + ".xml"),
                    this.weatherHandler
            );
        } catch (XMLDataRetrievedException dataRetriever) {
            HashMap<String, String> data = dataRetriever.getData();
            System.out.println(data);
            return data;
        }

//        if (checkCacheLease(placeName)) {
//            // this.weatherHandler.resetCachingMode(); caching mode not yet impl.
//
//        } else {
//            // this.weatherHandler.setCachingMode(); caching mode not yet impl.
//            try {
//                this.saxParser.parse(
//                        this.httpRequester.request(this.getPlaceData(placeName)),
//                        this.weatherHandler
//                );
//            } catch (XMLDataRetrievedException dataRetriever) {
//                HashMap<String, String> data = dataRetriever.getData();
//                this.cacheData(placeName);
//                return data;
//            }
//        }
        throw new SAXException();
    }


    private HashMap<String, String> getPlaceData(String placeName)
            throws SAXException, IOException {
        this.placesHandler.setPlaceName(placeName);

        /*
         * This structure might appear weird, since data is retrieved through
         * the catch clause. This is to avoid parsing the entire XML-file after
         * the desired data has been retrieved. See PlacesHandler.java for impl.
         */
        try {
            this.saxParser.parse(this.placesFile, this.placesHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            return dataRetriever.getData();
        }
        throw new SAXException();
    }

}

