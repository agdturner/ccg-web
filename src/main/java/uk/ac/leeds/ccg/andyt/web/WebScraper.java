/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.web;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.web.houseprices.ZooplaHousepriceScraper;

/**
 *
 * @author geoagdt
 */
public class WebScraper {

    protected static File sharedLogFile;
    protected double connectionCount;
    protected File directory;
    protected ExecutorService executorService;
    protected double permittedConnectionRate;
    protected long startTime;
    protected String url;

    protected void checkConnectionRate() {
        long timeToWaitInMilliseconds = 1000;
        double connectionRate = getConnectionRate();
        synchronized (this) {
            while (connectionRate > permittedConnectionRate) {
                try {
                    System.out.println("connectionRate > permittedConnectionRate");
                    System.out.println("" + connectionRate + " > " + permittedConnectionRate);
                    wait(timeToWaitInMilliseconds);
                    connectionRate = getConnectionRate();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ZooplaHousepriceScraper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public double getConnectionRate() {
        return getConnections() / (double) getTimeRunningMillis();
    }

    public double getConnections() {
        return connectionCount;
    }

    /**
     * @return the directory
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * @return the sharedLogFile
     */
    public static File getSharedLogFile() {
        return ZooplaHousepriceScraper.sharedLogFile;
    }
    
    /**
     * @return the executorService
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    protected HttpURLConnection getOpenHttpURLConnection(String aURLString) {
        checkConnectionRate();
        connectionCount += 1.0;
        HttpURLConnection result = null;
        URL aURL = null;
        try {
            aURL = new URL(aURLString);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
        //        // Set up proxy to use address and port from http://www.leeds.ac.uk/proxy.pac
        //        int port = 3128;
        //        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("www-cache.leeds.ac.uk", port));
        try {
            //            result = (HttpURLConnection) aURL.openConnection(proxy);
            if (aURL != null) {
                result = (HttpURLConnection) aURL.openConnection();
            }
            //            result.setRequestMethod("GET"); // GET is the default anyway.
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public long getTimeRunningMillis() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    
}
