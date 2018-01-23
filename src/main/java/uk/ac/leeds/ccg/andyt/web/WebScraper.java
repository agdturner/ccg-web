/*
 * Copyright 2017 Andy Turner, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import uk.ac.leeds.ccg.andyt.web.houseprices.Web_ZooplaHousepriceScraper;

/**
 *
 * @author Andy Turner
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
                    Logger.getLogger(Web_ZooplaHousepriceScraper.class.getName()).log(Level.SEVERE, null, ex);
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
        return Web_ZooplaHousepriceScraper.sharedLogFile;
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
