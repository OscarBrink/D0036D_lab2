package model;

import model.xmlFileIO.XMLDataRetrievedException;
import model.xmlFileIO.XMLWriter;
import model.xmlFileIO.handlers.LeaseHandler;
import model.xmlFileIO.handlers.PlacesHandler;
import model.xmlFileIO.handlers.WeatherHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;

import view.View;

import java.util.HashMap;

/**
 * Central class of the data model.
 * 
 * @author  Oscar Brink
 *          2018-10-15
 */
public class Model {
    
    private static SAXParserFactory parserFactory;

    private View view;

    private SAXParser saxParser;
    private PlacesHandler placesHandler;
    private LeaseHandler leaseHandler;
    private WeatherHandler weatherHandler;

    private String  applicationDirPath,
                    placesFilePath,
                    cacheFilePath;

    private long leaseTime = 1200; // default caching-lease = 20 min

    private HashMap<String, Long> cacheLeases;

    private HTTPRequester httpRequester;

    private XMLWriter xmlWriter;


    /**
     * Constructor. Sets up entire data-model for usage.
     *
     * @throws ParserConfigurationException Thrown if something goes
     *         wrong when generating parsers.
     * @throws SAXException Thrown if something goes
     *         wrong when generating parsers.
     * @throws IOException Thrown when application could not find or create
     *         application directories.
     */
    public Model() throws ParserConfigurationException, SAXException, IOException {
        // Creating parser objects
        parserFactory = SAXParserFactory.newInstance();
        this.saxParser = parserFactory.newSAXParser();

        this.placesHandler = new PlacesHandler();
        this.leaseHandler = new LeaseHandler();
        this.weatherHandler = new WeatherHandler();

        String sep = File.separator;

        // Main path where application data will be cached.
        this.applicationDirPath = System.getProperty("user.home") + sep + ".weatherAppBrink";

        // Creating application dir if not yet existing.
        File applicationDirectory = new File(this.applicationDirPath + sep + "cache");
        if (!applicationDirectory.isDirectory()) {
            if (!applicationDirectory.mkdirs()) {
                throw new IOException();
            }
        }

        // Setting path-strings for cache and places-file
        this.cacheFilePath = this.applicationDirPath + sep + "cache";
        this.placesFilePath = this.applicationDirPath + sep + "places.xml";

        this.httpRequester = new HTTPRequester("https://api.met.no/weatherapi/locationforecast/1.9/?lat=latitude&lon=longitude&msl=altitude");

        this.xmlWriter = new XMLWriter(cacheFilePath);

        // Read any cache-leases that might have been stored from previous run of application
        // Otherwise, initialize empty cache-lease map
        try {
            this.readStoredCacheLeases();
        } catch (IOException e) {
            this.cacheLeases = new HashMap<String, Long>();
        }

        // Setup shutdown-hook to store active cache-leases on application shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    Model.this.storeCacheLeases();
                } catch (IOException e) {
                    // If cache-leases failed to write, do nothing.
                    //e.printStackTrace();
                }
            }
        }));
    }

    /**
     * Set the view that the data model pushes to.
     *
     * @param view View that the data model pushes info to.
     */
    public void setView(View view) {
        this.view = view;
    }

    /*
     * Method runs on close of program.
     * Stores the active cache-leases in the program
     */
    private void storeCacheLeases() throws IOException {
        HashMap<String, Long> activeLeases = new HashMap<String, Long>();

        for (HashMap.Entry<String, Long> entry : this.cacheLeases.entrySet()) {
            if ((entry.getValue() - (System.currentTimeMillis() / 1000)) > 0) {
                activeLeases.put(entry.getKey(), entry.getValue());
            }
        }

        this.xmlWriter.storeCacheLeases(activeLeases);
    }

    /*
     * Method tries to read stored cache leases, if they exist.
     * IOException otherwise
     */
    private void readStoredCacheLeases() throws IOException, SAXException {
        try {
            saxParser.parse(
                    new File(this.cacheFilePath + "cacheLeases.xml"),
                    this.leaseHandler
            );
        } catch (XMLDataRetrievedException dataRetriever) {
            HashMap<String, Long> cacheLeases = new HashMap<String, Long>();
            for (HashMap.Entry<String, String> entry : dataRetriever.getData().entrySet()) {
                cacheLeases.put(entry.getKey(), Long.valueOf(entry.getValue()));
            }

            this.cacheLeases = cacheLeases;
        }
    }

    /*
     * Checks if lease-time has passed. Returns false if it has.
     */
    private boolean checkCacheLease(String placeName) {
        for (HashMap.Entry<String, Long> entry : this.cacheLeases.entrySet()) {
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
        } else {
            String message = "Lease time must be at least 10 minutes.";
            throw new IllegalArgumentException(message);
        }
    }

    /*
     * Parses input from the API, and writes retrieved data to cache-file
     */
    private void cacheData(String placeName, InputStream requestInput)
            throws IOException, SAXException {
        this.setCacheLease(placeName);
        this.weatherHandler.setCachingMode();

        try {
            // Parsing input from API
            this.saxParser.parse(requestInput, this.weatherHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            // Write to cache-file
            this.xmlWriter.cacheData(placeName, dataRetriever.getData());
        }

        this.weatherHandler.resetCachingMode();
    }

    /**
     * Takes a request from controller and gets the data either from cache or
     * from the met.no API.
     *
     * @param placeName The name of the place to get weather data from. This
     *                  has to be specified in the file places.xml
     * @param date The date to get weather data for.
     * @param time The time to get weather data for.
     * @throws IOException Thrown from child methods.
     * @throws SAXException Thrown from child methods.
     */
    public void request(String placeName, String date, String time)
            throws IOException, SAXException {

        this.weatherHandler.setDateTime(date, time);

        this.view.updateTemperature(getWeatherData(placeName).get("temperature"));
    }

    /*
     * Reads data either from cache or from API.
     * If from API the data is first cached.
     */
    private HashMap<String, String> getWeatherData(String placeName)
            throws SAXException, IOException {

        if (!checkCacheLease(placeName)) {
            this.cacheData(
                    placeName,
                    this.httpRequester.request(this.getPlaceData(placeName))
            );
        }

        try {
            this.saxParser.parse(
                    new File(cacheFilePath + placeName + ".xml"),
                    this.weatherHandler
            );
        } catch (XMLDataRetrievedException dataRetriever) {
            HashMap<String, String> data = dataRetriever.getData();
            return data;
        }

        throw new SAXException();
    }

    /*
     * Retrieves position data of a place from places.xml
     * Gets longitude, latitude, and altitude
     */
    private HashMap<String, String> getPlaceData(String placeName)
            throws SAXException, IOException {
        this.placesHandler.setPlaceName(placeName);

        try {
            this.saxParser.parse(new File(placesFilePath), this.placesHandler);
        } catch (XMLDataRetrievedException dataRetriever) {
            return dataRetriever.getData();
        }
        throw new SAXException();
    }

}

