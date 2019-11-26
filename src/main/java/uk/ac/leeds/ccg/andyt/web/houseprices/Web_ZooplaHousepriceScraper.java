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
package uk.ac.leeds.ccg.andyt.web.houseprices;

/**
 * This is a class for scraping house price paid data from the Zoopla website
 * (http://www.zoopla.co.uk/). If you use this software then please do so
 * considerately so that you do not adversely affect other Zoopla users. If you
 * plan to scrape more than a few thousand postcodes, then it is best practice
 * to contact Zoopla. Currently the number of requests is rate limited to about
 * 20K requests per hour (about 5 requests per second).
 *
 * This software was developed out of frustration. The price paid data from the
 * Land Registry was available on-line via the Zoopla website, but the Land
 * Registry were only making the last 12 months of data available for research
 * without what seemed to me to be extortionate pricing.
 *
 * In July 2013 it was announced that the Land Registry would be making all
 * historical price paid data available as open data under the Open Government
 * License
 * (http://www.landregistry.gov.uk/market-trend-data/public-data/price-paid-data).
 *
 * The intended uses of the data are to look at the geographical variation in
 * price paid data, the locations of house sales, the locations of newly built
 * properties. This work may support a range of other uses.
 */
import uk.ac.leeds.ccg.andyt.postcode.UKPC_Checker;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
import uk.ac.leeds.ccg.andyt.web.Web_Scraper;
import uk.ac.leeds.ccg.andyt.web.core.Web_Environment;

public class Web_ZooplaHousepriceScraper extends Web_Scraper {

    protected UKPC_Checker pcc;

    private String url0;
    private String url1;
    /**
     * For storing the first part of a postcode in lower case
     */
    private String firstpartPostcode;

    /**
     * For storing the outcodes from Zoopla. These are meant to define what data
     * Zoopla serves out.
     */
    private static TreeSet<String> outcodes;

    /**
     * Creates a new instance of ZooplaHousepriceScraper
     *
     * @param e
     */
    public Web_ZooplaHousepriceScraper(Web_Environment e) {
        super(e);
    }

    /**
     * Main method.
     *
     * @param args args[0] directory args[1] restart args[2] firstpartPostcode -
     * Optional and need not be complete args[3] secondpartPostcode - Optional
     * and need not be complete
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                args = new String[2];
                // Directory to output to.
                args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
                //args[0] = "C:/zoopla/";
                //args[1] = "r";
                args[1] = "";
            }
            // Test run 1
            args = new String[4];
            args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
            args[1] = ""; //"r";
            args[2] = "LS7";
            args[3] = "2EU";

//        // Test run 2
//        args = new String[4];
//        args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
//        args[1] = "";
//        args[2] = "LS7";
//        args[3] = "2E";
//        
//        // Test run 3
//        args = new String[4];
//        args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
//        args[1] = "";
//        args[2] = "LS7";
//        args[3] = "2";
//        
//        // Test run 4
//        args = new String[3];
//        args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
//        args[1] = "";
//        args[2] = "LS7";
//
//        // Test run 5
//        args = new String[3];
//        args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
//        args[1] = "";
//        args[2] = "SW9";
            Web_ZooplaHousepriceScraper p = new Web_ZooplaHousepriceScraper(
                    new Web_Environment(new Data_Environment()));
            p.run(args);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     *
     * @param args
     */
    public void run(String[] args) {
        try {
            startTime = System.currentTimeMillis();
            double permittedConnectionsPerHour = 20000;
            permittedConnectionRate = permittedConnectionsPerHour / Generic_Time.MilliSecondsInHour;
            connectionCount = 0;
            directory = new File(args[0]);
            // url0 = "http://www.houseprices.co.uk/e.php?q=";
            url0 = "http://www.zoopla.co.uk";
            url1 = url0 + "/house-prices/";
            url = url1;
            File outcodesFile = new File(directory, "outcodes.xml");
            init_Outcodes(outcodesFile);
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("r")) {
                    getHousepriceData(true);
                } else {
                    getHousepriceData(false);
                }
            }
            if (args.length == 3) {
                getHousepriceData(args[2], false);
//                if (args[1].equalsIgnoreCase("r")) {
//                    getHousepriceData(args[2], true);
//                } else {
//                    getHousepriceData(args[2], false);
//                }
            }
            if (args.length == 4) {
                TreeMap<String, String> addressAdditionalPropertyDetails = new TreeMap<>();
                getHousepriceData(args[2], args[3], addressAdditionalPropertyDetails);
//                if (args[1].equalsIgnoreCase("r")) {
//                    getHousepriceData(args[2], args[3], true);
//                } else {
//                    getHousepriceData(args[2], args[3], false);
//                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace(System.err);
        }
    }

    private void init_Outcodes(File file) {
        Web_XMLDOMReader aXMLDOMReader = new Web_XMLDOMReader(file);
        outcodes = aXMLDOMReader.outcodePostcodes;
        outcodes.add("sw9");
    }

    /**
     *
     * @param fpp firstPartPostcode
     * @param spp secondPartPostcode
     * @param aapd addressAdditionalPropertyDetails
     */
    public void getHousepriceData(String fpp, String spp,
            TreeMap<String, String> aapd) {
        String postcode = (fpp + spp).toUpperCase();
        fpp = fpp.toLowerCase();
        spp = spp.toLowerCase();
        TreeSet<String> allprices = new TreeSet<>();
        int pt = pcc.getUnitPostcodeType(postcode);
        if (pt != 0) {
            url = url1 + fpp;
            if (isReturningOutcode(fpp, getUrl())) {
                url += "-" + spp;
                TreeSet<String> prices = getHTMLandFormat(getUrl(), fpp, spp, aapd);
                allprices.addAll(prices);
            }
            Iterator<String> ite = allprices.iterator();
            while (ite.hasNext()) {
                System.out.println(ite.next());
            }
        }
    }

    /**
     *
     * @param fpp firstpartPostcode
     * @param restart
     * @throws Exception
     */
    public void getHousepriceData(String fpp, boolean restart)
            throws Exception {
        this.firstpartPostcode = fpp.toLowerCase();
        // Initialisation
        //executorService = Executors.newCachedThreadPool();
        //int nThreads = 1 + (23 * 3) + (23 * 23 * 2); 
        int nThreads = 1;//256; 128;
        executorService = Executors.newFixedThreadPool(nThreads);
        HashSet<Future> futures = new HashSet<>();
        int pt = pcc.getFirstPartPostcodeType(fpp.toUpperCase().toCharArray());
        if (pt == UKPC_Checker.TYPE_AANN) {
            futures.addAll(format_AANN_NAA(restart));
        }
        if (pt == UKPC_Checker.TYPE_AANA) {
            futures.addAll(format_AANA_NAA(restart));
        }
        if (pt == UKPC_Checker.TYPE_ANN) {
            futures.addAll(format_ANN_NAA(restart));
        }
        if (pt == UKPC_Checker.TYPE_ANA) {
            futures.addAll(format_ANA_NAA(restart));
        }
        if (pt == UKPC_Checker.TYPE_AAN) {
            futures.addAll(format_AAN_NAA(restart));
        }
        if (pt == UKPC_Checker.TYPE_AN) {
            futures.addAll(format_AN_NAA(restart));
        }
        // Wait for results then shutdown executorService
        exec.shutdownExecutorService(getExecutorService(), futures, this,
                100000L, 10L);
    }

    /**
     * @param restart controls the mode of operation. If restart is true then
     * log files are opened and read to identify where an interrupted run had
     * got to in order to restart.
     * @throws java.lang.Exception
     */
    public void getHousepriceData(
            boolean restart)
            throws Exception {
        // Initialisation
        //executorService = Executors.newCachedThreadPool();
        //int nThreads = 1 + (23 * 3) + (23 * 23 * 2); 
        //int nThreads = 256;
        //int nThreads = 128;
        //int nThreads = 64;
        //int nThreads = 23;
        int nThreads = 10;
        executorService = Executors.newFixedThreadPool(nThreads);
        HashSet<Future> futures = new HashSet<>();
        // Format   Example Postcode
        // AN NAA   W1 1NW
        HashSet<Future> results_AN_NAA = format_AN_NAA(restart);
        futures.addAll(results_AN_NAA);
        // Format   Example Postcode
        // ANN NAA  M11 1NW
        HashSet<Future> results_ANN_NAA = format_ANN_NAA(restart);
        futures.addAll(results_ANN_NAA);
        // Format 	Example Postcode
        //  AAN NAA     CR2 6XH
        HashSet<Future> results_AAN_NAA = format_AAN_NAA(restart);
        futures.addAll(results_AAN_NAA);
        // Format 	Example Postcode
        //  ANA NAA     W1A 1HQ
        // ANA_NAA
        HashSet<Future> results_ANA_NAA = format_ANA_NAA(restart);
        futures.addAll(results_ANA_NAA);
        // Format 	Example Postcode
        //  AANA NAA    EC1A 1BB
        HashSet<Future> results_AANA_NAA = format_AANA_NAA(restart);
        futures.addAll(results_AANA_NAA);
        // Format 	Example Postcode
        //  AANN NAA    DN12 1PT
        HashSet<Future> results_AANN_NAA = format_AANN_NAA(restart);
        futures.addAll(results_AANN_NAA);
        // Wait for results then shutdown executorService
        exec.shutdownExecutorService(getExecutorService(), futures, this,
                100000L, 10L);
    }

    private HashSet<Future> format_AN_NAA(boolean restart) throws IOException {
        HashSet<Future> r = new HashSet<>();
        r.add(getExecutorService().submit(new Web_Run_an_naa(this, restart)));
        return r;
    }

    /**
     * Format Example Postcode ANA NAA: E1A 1BB
     */
    private HashSet<Future> format_ANA_NAA(boolean restart)
            throws IOException {
        HashSet<Future> r = new HashSet<>();
        r.add(getExecutorService().submit(new Web_Run_ana_naa(this, restart)));
        return r;
    }

    /**
     * Format Example Postcode AANA NAA: EC1A 1BB
     */
    private HashSet<Future> format_AANA_NAA(boolean restart)
            throws IOException {
        HashSet<Future> r = new HashSet<>();
        r.add(getExecutorService().submit(new Web_Run_aana_naa(this, restart)));
        return r;
    }

    /**
     * Format Example Postcode AANN NAA: DN12 1BB
     */
    private HashSet<Future> format_AANN_NAA(boolean restart)
            throws IOException {
        HashSet<Future> r = new HashSet<>();
        r.add(getExecutorService().submit(new Web_Run_aann_naa(this, restart)));
        return r;
    }

    /**
     * Format Example Postcode AAN NAA: EN1 1BB
     */
    private HashSet<Future> format_AAN_NAA(boolean restart)
            throws IOException {
        HashSet<Future> r = new HashSet<>();
        r.add(getExecutorService().submit(new Web_Run_aan_naa(this, restart)));
        return r;
    }

    /**
     * Format Example Postcode ANN NAA: E11 1BB
     */
    private HashSet<Future> format_ANN_NAA(boolean restart)
            throws IOException {
        HashSet<Future> r = new HashSet<>();
        r.add(getExecutorService().submit(new Web_Run_ann_naa(this, restart)));
        return r;
    }

//    @Deprecated
//    public String getLowerCase(String upperCase) {
//        return Generic_String.getLowerCase(upperCase);
//    }
    public void writeHouseprices(
            PrintWriter pw,
            String aURL_String,
            String tFirstPartOfPostcode,
            String tSecondPartOfPostcode,
            TreeMap<String, String> addressAdditionalPropertyDetails) {
        TreeSet<String> prices = getHTMLandFormat(
                aURL_String,
                tFirstPartOfPostcode,
                tSecondPartOfPostcode,
                addressAdditionalPropertyDetails);
        Iterator aIterator = prices.iterator();
        while (aIterator.hasNext()) {
            pw.write((String) aIterator.next());
            pw.println();
        }
        pw.flush();
        //System.out.println( "Done " + _URLString );
    }

    /**
     * @param outPW
     * @param logPW
     * @param sharedLogPW
     * @param aURL_String
     * @param tSecondPartOfPostcode
     * @param tFirstPartOfPostcode
     * @param addressAdditionalPropertyDetails
     * @return number of records
     */
    public int writeHouseprices(
            PrintWriter outPW,
            PrintWriter logPW,
            PrintWriter sharedLogPW,
            String aURL_String,
            String tFirstPartOfPostcode,
            String tSecondPartOfPostcode,
            TreeMap<String, String> addressAdditionalPropertyDetails) {
        TreeSet<String> prices = getHTMLandFormat(
                aURL_String,
                tFirstPartOfPostcode,
                tSecondPartOfPostcode,
                addressAdditionalPropertyDetails);
//        sharedLogPW.print('.');
//        sharedLogPW.flush();
        if (prices.isEmpty()) {
            logPW.println(tFirstPartOfPostcode + " " + tSecondPartOfPostcode + " 0 Records");
            logPW.flush();
            return 0;
        } else {
            Iterator ite = prices.iterator();
            while (ite.hasNext()) {
                outPW.write((String) ite.next());
                outPW.println();
            }
            outPW.flush();
            logPW.println(tFirstPartOfPostcode + " " + tSecondPartOfPostcode + " " + prices.size() + " Records");
            return prices.size();
        }
    }

    /**
     * @param logPW
     * @param completeFirstPartPostcode
     * @param sharedLogPW
     */
    public static void updateLog(
            PrintWriter logPW,
            PrintWriter sharedLogPW,
            String completeFirstPartPostcode) {
        logPW.println(completeFirstPartPostcode + " 0aa 0 Records");
        logPW.println(completeFirstPartPostcode + " 9zz 0 Records");
        logPW.flush();
//        sharedLogPW.append('.');
//        sharedLogPW.flush();
    }

    public boolean isReturningOutcode(
            String completeFirstPartPostcode,
            String aURLString) {
        if (outcodes.contains(completeFirstPartPostcode)) {
            try {
                HttpURLConnection connection = getOpenHttpURLConnection(aURLString);
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
//                if (responseCode == 301) {
//                    return false;
//                }
//                if (responseCode == 404) {
//                    return false;
//                }
                    String message = aURLString + " connection.getResponseCode() "
                            + responseCode
                            + " see http://en.wikipedia.org/wiki/List_of_HTTP_status_codes";
                    if (responseCode == 301 || responseCode == 302 || responseCode == 303
                            || responseCode == 403 || responseCode == 404) {
                        message += " and http://en.wikipedia.org/wiki/HTTP_";
                        message += Integer.toString(responseCode);
                    }
                    throw new Error(message);
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            return true;
        }
        return false;
    }

    public TreeSet<String> getHTMLandFormat(
            String aURLString,
            String tFirstPartOfPostcode,
            String tSecondPartOfPostcode,
            TreeMap<String, String> addressAdditionalPropertyDetails) {
        TreeSet<String> result = new TreeSet<>();
        HttpURLConnection connection;
        BufferedReader br;
        String line;
        try {
            connection = getOpenHttpURLConnection(aURLString);
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                if (responseCode == 404) {
                    return result;
                }
                String message = aURLString + " connection.getResponseCode() "
                        + responseCode
                        + "see http://en.wikipedia.org/wiki/List_of_HTTP_status_codes";
                if (responseCode == 301 || responseCode == 302 || responseCode == 303
                        || responseCode == 403) {
                    message += "and http://en.wikipedia.org/wiki/HTTP_";
                    message += Integer.toString(responseCode);
                }
                throw new Error(message);
            }
            br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            try {
                while ((line = br.readLine()) != null) {
                    if (line.contains("house prices paid")) {
                        while (true) {
                            addResult(result,
                                    tFirstPartOfPostcode,
                                    tSecondPartOfPostcode,
                                    addressAdditionalPropertyDetails,
                                    br);
                            // Scan to next record
                            line = br.readLine();
                            while (!line.contains("<tr >")) {
                                line = br.readLine();
                            }
                            line = br.readLine();
                            while (!line.contains("<tr >")) {
                                line = br.readLine();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                //e.printStackTrace(System.err);
            }
            br.close();
            //}
        } catch (Exception e) {
            //e.printStackTrace(System.err);
        }
        return result;
    }

    public String getHTMLandFormatPropertyDetails(
            String aURLString) {
        String result = "";
        HttpURLConnection connection;
        BufferedReader br;
        String line;
        try {
            connection = getOpenHttpURLConnection(aURLString);
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                if (responseCode == 404) {
                    return result;
                }
                String message = aURLString + " connection.getResponseCode() "
                        + responseCode
                        + "see http://en.wikipedia.org/wiki/List_of_HTTP_status_codes";
                if (responseCode == 301 || responseCode == 302 || responseCode == 303
                        || responseCode == 403) {
                    message += "and http://en.wikipedia.org/wiki/HTTP_";
                    message += Integer.toString(responseCode);
                }
                throw new Error(message);
            }
            br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            try {
                while ((line = br.readLine()) != null) {
                    if (line.contains("attributes-update")) {
                        line = br.readLine();
                        while (!line.contains("<")) {
                            result += line.trim();
                            line = br.readLine();
                        }
                    }
                }
            } catch (IOException e) {
                //e.printStackTrace(System.err);
            }
            br.close();
            //}
        } catch (Exception e) {
            //e.printStackTrace(System.err);
        }
        return result;
    }

    private void addResult(
            TreeSet<String> result,
            String tFirstPartOfPostcode,
            String tSecondPartOfPostcode,
            TreeMap<String, String> addressAdditionalPropertyDetails,
            BufferedReader br) throws IOException {
        String s_Address;
        String tFormattedOutput;
        String s_PropertyType0;
        String s_PropertyType1;
        String s_DatePurchased;
        String s_Price;
        String line;
        // get s_DatePurchased;
        line = br.readLine();
        while (!line.contains("<strong>")) {
            line = br.readLine();
        }
        line = br.readLine();
        s_DatePurchased = line.trim();
        // get Address
        line = br.readLine();
        while (!line.startsWith(">")) {
            line = br.readLine();
        }
        s_Address = line.split("<")[0].substring(1);

//        boolean debug = false;
//        if (s_Address.startsWith("37")) {
//            debug = true;
//            System.out.println(s_Address);
//        }
        // get s_PropertyType0 s_PropertyType1
        line = br.readLine();
        while (!line.contains("attributes-update")) {

//            if (debug) {
//                System.out.println(line);
//            }
            line = br.readLine();
        }
        line = br.readLine();
        s_PropertyType0 = line.trim();
        br.readLine();
        line = br.readLine();
        s_PropertyType1 = line.trim();
        if (s_PropertyType1.equalsIgnoreCase("--,")) {
            s_PropertyType1 = "";
        }
        // get price
        line = br.readLine();
        while (!line.contains("&pound;")) {

//            if (debug) {
//                System.out.println(line);
//            }
            line = br.readLine();
        }
        String[] price = line.split("&pound;")[1].split("<")[0].split(",");
        s_Price = "";
        for (int i = 0; i < price.length; i++) {
            s_Price += price[i];
        }

        String additionalPropertyDetails = getAdditionalPropertyDetails(
                addressAdditionalPropertyDetails,
                br,
                s_Address);

        tFormattedOutput
                = tFirstPartOfPostcode + " "
                + tSecondPartOfPostcode + ",\""
                + s_Address + "\","
                //+ s_PropertyType0
                //+ s_PropertyType1 + ","
                + s_DatePurchased + ","
                + s_Price + ","
                + additionalPropertyDetails;

        result.add(tFormattedOutput);
        // Scan to next record
        line = br.readLine();
        while (!line.contains("<tr >")) {
            line = br.readLine();
        }
        line = br.readLine();
        while (!line.contains("<tr >")) {
            line = br.readLine();
        }
    }

    private String getAdditionalPropertyDetails(
            TreeMap<String, String> addressAdditionalPropertyDetails,
            BufferedReader br,
            String s_Address) {
        String result = null;
        String[] splitAddress = s_Address.split(",");
        String lowerCaseAdd0;
        String lowerCaseAdd1 = null;
        if (splitAddress.length == 2) {
            lowerCaseAdd0 = splitAddress[0].trim().toLowerCase();
            lowerCaseAdd1 = lowerCaseAdd0.replaceAll(" ", "-");
        } else {
            if (splitAddress.length == 3 || splitAddress.length == 4) {
                lowerCaseAdd0 = splitAddress[1].trim().toLowerCase();
                lowerCaseAdd1 = lowerCaseAdd0.replaceAll(" ", "-");
            } else {
                try {
                    String message = "splitAddress.length == " + splitAddress.length + ":"
                            + " splitAddress.length != 2 || 3 || 4 :"
                            + " splitAddress; ";
                    for (int i = 0; i < splitAddress.length; i++) {
                        message += splitAddress[i] + ", ";
                    }
                    throw new Exception(message);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                    Logger.getLogger(Web_ZooplaHousepriceScraper.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        String line;
        try {
            line = br.readLine();
            while (!line.contains(lowerCaseAdd1)) {
                line = br.readLine();
            }
            String[] splitURL0 = line.split("\"");
            String[] splitURL1 = splitURL0[1].split("/");
            String propertyID = splitURL1[splitURL1.length - 1];
            result = getAdditionalPropertyDetails(
                    addressAdditionalPropertyDetails,
                    s_Address,
                    propertyID,
                    url0 + splitURL0[1]);
        } catch (IOException ex) {
            Logger.getLogger(Web_ZooplaHousepriceScraper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private String getAdditionalPropertyDetails(
            TreeMap<String, String> addressAdditionalPropertyDetails,
            String s_Address,
            String propertyID,
            String url) {
        String result;
        String key = s_Address + "," + propertyID;
        if (addressAdditionalPropertyDetails.containsKey(key)) {
            return addressAdditionalPropertyDetails.get(key);
        } else {
            result = propertyID + "," + url + ",";
            result += getHTMLandFormatPropertyDetails(url);
            addressAdditionalPropertyDetails.put(key, result);
        }
        return result;
    }

    /**
     * @return the firstpartPostcode
     */
    public String getFirstpartPostcode() {
        return firstpartPostcode;
    }

    /**
     * @return the outcodes
     */
    public TreeSet<String> getOutcodes() {
        return outcodes;
    }
}
