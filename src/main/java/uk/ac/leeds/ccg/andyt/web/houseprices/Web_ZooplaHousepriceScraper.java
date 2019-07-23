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
 * The intended uses of the data are to look at: House price paid fluctuations,
 * the locations of house sales and the locations of newly built properties with
 * regard population migration. The data may have other uses, such as in
 * estimating the total value of the UK housing stock.
 *
 * The program can use multiple threads, but unless your internet connection is
 * slow then having more threads won't help as the number of requests is rate
 * limited.
 */
import uk.ac.leeds.ccg.andyt.data.postcode.Data_UKPostcodeHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.generic.execution.Generic_Execution;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
import uk.ac.leeds.ccg.andyt.web.Web_Scraper;
import uk.ac.leeds.ccg.andyt.web.core.Web_Environment;

public class Web_ZooplaHousepriceScraper extends Web_Scraper {

    private String url0;
    private String url1;
    /**
     * For storing the first part of a postcode in lower case
     */
    private String firstpartPostcode;
    /**
     * For storing a set of all NAA String combinations where: N is a numerical
     * integer from 0 to 9 inclusive; and, Both A are alphabetical characters
     * from _AtoZ_not_CIKMOV.
     */
    private static TreeSet<String> _NAA;
    /**
     * For storing (in lower case) all the letters of the Alphabet except C, I,
     * K, M, O, and V.
     */
    private static TreeSet<String> _AtoZ_not_CIKMOV;
    /**
     * For storing (in lower case) all the letters of the Alphabet except Q, V,
     * and X.
     */
    private static TreeSet<String> _AtoZ_not_QVX;
    /**
     * For storing (in lower case) all the letters of the Alphabet except I, J,
     * and Z.
     */
    private static TreeSet<String> _AtoZ_not_IJZ;
    /**
     * For storing (in lower case) the letters of the Alphabet A, B, C, D, E, F,
     * G, H, J, K, S, T, U, and W.
     */
    private static TreeSet<String> _ABCDEFGHJKSTUW;
    /**
     * For storing (in lower case) the letters of the Alphabet A, B, E, H, M, N,
     * P, R, V, W, X, and Y.
     */
    private static TreeSet<String> _ABEHMNPRVWXY;
    /**
     * For storing the digits 0 to 9 as Strings
     */
    private static TreeSet<String> _0to9;
    /**
     * For storing the outcodes from zoopla. These are meant to define what data
     * Zoopla serves out.
     */
    private static TreeSet<String> outcodes;

    /**
     * Creates a new instance of ZooplaHousepriceScraper
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
        if (args.length == 0) {
            args = new String[2];
            // This defaults to the directory I want to output to.
            args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
            //args[0] = "E:/zoopla/";
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
        new Web_ZooplaHousepriceScraper(new Web_Environment()).run(args);
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
            sharedLogFile = new File(
                    args[0],
                    "log");
            getSharedLogFile().createNewFile();
            getSharedLogFile().deleteOnExit();
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
                TreeMap<String, String> addressAdditionalPropertyDetails = new TreeMap<String, String>();
                getHousepriceData(args[2], args[3], addressAdditionalPropertyDetails);
//                if (args[1].equalsIgnoreCase("r")) {
//                    getHousepriceData(args[2], args[3], true);
//                } else {
//                    getHousepriceData(args[2], args[3], false);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } catch (Error e) {
            e.printStackTrace(System.err);
        }
    }

    private void init_Outcodes(File file) {
        Web_XMLDOMReader aXMLDOMReader = new Web_XMLDOMReader(file);
        outcodes = aXMLDOMReader.outcodePostcodes;
        outcodes.add("sw9");
    }

    public void getHousepriceData(
            String firstPartPostcode,
            String secondPartPostcode,
            TreeMap<String, String> addressAdditionalPropertyDetails) {
        firstPartPostcode = Generic_String.getLowerCase(firstPartPostcode);
        secondPartPostcode = Generic_String.getLowerCase(secondPartPostcode);
        TreeSet<String> allprices = new TreeSet<String>();
        if (!getFirstPartPostcodeType(firstPartPostcode).isEmpty()) {
            String secondPartPostcodeType = getSecondPartPostcodeType(secondPartPostcode);
            if (secondPartPostcodeType.equalsIgnoreCase("NAA")) {
                url = url1 + firstPartPostcode;
                if (isReturningOutcode(firstPartPostcode, getUrl())) {
                    url += "-" + secondPartPostcode;
                    TreeSet<String> prices = getHTMLandFormat(
                            getUrl(),
                            firstPartPostcode,
                            secondPartPostcode,
                            addressAdditionalPropertyDetails);
                    allprices.addAll(prices);
                }
            } else {
                if (secondPartPostcodeType.equalsIgnoreCase("NA")) {
                    Iterator<String> ite = Data_UKPostcodeHandler.get_AtoZ_not_CIKMOV().iterator();
                    while (ite.hasNext()) {
                        String a = ite.next();
                        url = url1 + firstPartPostcode;
                        url += "-" + secondPartPostcode + a;
                        TreeSet<String> prices = getHTMLandFormat(
                                getUrl(),
                                firstPartPostcode,
                                secondPartPostcode + a,
                                addressAdditionalPropertyDetails);
                        allprices.addAll(prices);
                    }
                } else {
                    if (secondPartPostcodeType.equalsIgnoreCase("N")) {
                        Iterator<String> ite = Data_UKPostcodeHandler.get_AtoZ_not_CIKMOV().iterator();
                        while (ite.hasNext()) {
                            String a0 = ite.next();
                            Iterator<String> ite2 = Data_UKPostcodeHandler.get_AtoZ_not_CIKMOV().iterator();
                            while (ite2.hasNext()) {
                                String a1 = ite2.next();
                                url = url1 + firstPartPostcode;
                                url += "-" + secondPartPostcode + a0 + a1;
                                TreeSet<String> prices = getHTMLandFormat(
                                        getUrl(),
                                        firstPartPostcode,
                                        secondPartPostcode + a0 + a1,
                                        addressAdditionalPropertyDetails);
                                allprices.addAll(prices);
                            }
                        }
                    }
                }
            }
        }
        Iterator<String> ite = allprices.iterator();
        while (ite.hasNext()) {
            System.out.println(ite.next());
        }
    }

    public void getHousepriceData(
            String firstpartPostcode,
            boolean restart)
            throws Exception {
        this.firstpartPostcode = Generic_String.getLowerCase(firstpartPostcode);
        // Initialisation
        //executorService = Executors.newCachedThreadPool();
        //int nThreads = 1 + (23 * 3) + (23 * 23 * 2); 
        int nThreads = 1;//256; 128;
        executorService = Executors.newFixedThreadPool(nThreads);
        HashSet<Future> futures = new HashSet<Future>();
        String postcodeType = getFirstPartPostcodeType(this.firstpartPostcode);
        if (postcodeType.equalsIgnoreCase("AANN")) {
            futures.addAll(format_AANN_NAA(restart));
        }
        if (postcodeType.equalsIgnoreCase("AANA")) {
            futures.addAll(format_AANA_NAA(restart));
        }
        if (postcodeType.equalsIgnoreCase("ANN")) {
            futures.addAll(format_ANN_NAA(restart));
        }
        if (postcodeType.equalsIgnoreCase("ANA")) {
            futures.addAll(format_ANA_NAA(restart));
        }
        if (postcodeType.equalsIgnoreCase("AAN")) {
            futures.addAll(format_AAN_NAA(firstpartPostcode, restart));
        }
        if (postcodeType.equalsIgnoreCase("AN")) {
            futures.addAll(format_AN_NAA(restart));
        }
        // Wait for results then shutdown executorService
        Generic_Execution.shutdownExecutorService(
                getExecutorService(),
                futures,
                this,
                100000L,
                10L);
    }

    /**
     * Check SecondPart Postcode is in the format "naa", "na" or "n" where a
     * stands for an alphabetical character and n stands for a numerical
     * character.
     *
     * @param secondPartPostcode
     * @return
     */
    public String getSecondPartPostcodeType(String secondPartPostcode) {
        if (secondPartPostcode.length() < 4) {
            if (secondPartPostcode.length() == 3) {
                String _0 = secondPartPostcode.substring(0, 1);
                String _1 = secondPartPostcode.substring(1, 2);
                String _2 = secondPartPostcode.substring(2, 3);
                if (Data_UKPostcodeHandler.get_0to9().contains(_0)
                        && Data_UKPostcodeHandler.get_AtoZ_not_CIKMOV().contains(_1)
                        && Data_UKPostcodeHandler.get_AtoZ_not_CIKMOV().contains(_2)) {
                    return "NAA";
                }
            } else {
                if (secondPartPostcode.length() == 2) {
                    String _0 = secondPartPostcode.substring(0, 1);
                    String _1 = secondPartPostcode.substring(1, 2);
                    if (Data_UKPostcodeHandler.get_0to9().contains(_0)
                            && Data_UKPostcodeHandler.get_AtoZ_not_CIKMOV().contains(_1)) {
                        return "NA";
                    }
                } else {
                    if (secondPartPostcode.length() == 1) {
                        String _0 = secondPartPostcode.substring(0, 1);
                        if (Data_UKPostcodeHandler.get_0to9().contains(_0)) {
                            return "N";
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * If firstPartPostcode is not a valid first part for a postcode return an
     * empty string otherwise return "aann", "aana", "ann", "ana", "aan", "an"
     * respectively where a stands for an alphabetical character and n stands
     * for a numerical character.
     *
     * @param firstPartPostcode
     * @return
     */
    public String getFirstPartPostcodeType(String firstPartPostcode) {
        // Resolve type from firstPartPostcode
        String unresolvedMessage = firstPartPostcode + " is not recognised as a first part of a postcode";
        // Return a String or null (AANN, AANA, ANN, ANA, AAN, AN)
        if (firstPartPostcode.length() > 4 || firstPartPostcode.length() < 2) {
            System.err.println(unresolvedMessage);
            return "";
        }
        if (firstPartPostcode.length() == 4) {
            String _0 = firstPartPostcode.substring(0, 1);
            String _1 = firstPartPostcode.substring(1, 2);
            String _2 = firstPartPostcode.substring(2, 3);
            String _3 = firstPartPostcode.substring(3, 4);
            if (Data_UKPostcodeHandler.get_AtoZ_not_QVX().contains(_0)
                    && Data_UKPostcodeHandler.get_AtoZ_not_IJZ().contains(_1)
                    && Data_UKPostcodeHandler.get_0to9().contains(_2)
                    && Data_UKPostcodeHandler.get_0to9().contains(_3)) {
                return "AANN";
            } else {
                if (Data_UKPostcodeHandler.get_AtoZ_not_QVX().contains(_0)
                        && Data_UKPostcodeHandler.get_AtoZ_not_IJZ().contains(_1)
                        && Data_UKPostcodeHandler.get_0to9().contains(_2)
                        && Data_UKPostcodeHandler.get_ABEHMNPRVWXY().contains(_3)) {
                    return "ANNA";
                } else {
                    System.err.println(unresolvedMessage);
                    return "";
                }
            }
        }
        if (firstPartPostcode.length() == 3) {
            String _0 = firstPartPostcode.substring(0, 1);
            String _1 = firstPartPostcode.substring(1, 2);
            String _2 = firstPartPostcode.substring(2, 3);
            if (Data_UKPostcodeHandler.get_AtoZ_not_QVX().contains(_0)) {
                if (Data_UKPostcodeHandler.get_0to9().contains(_1)) {
                    if (Data_UKPostcodeHandler.get_0to9().contains(_2)) {
                        return "ANN";
                    } else {
                        if (Data_UKPostcodeHandler.get_ABCDEFGHJKSTUW().contains(_2)) {
                            return "ANA";
                        } else {
                            System.err.println(unresolvedMessage);
                            return "";
                        }
                    }
                } else {
                    if (Data_UKPostcodeHandler.get_AtoZ_not_IJZ().contains(_1)) {
                        if (Data_UKPostcodeHandler.get_0to9().contains(_2)) {
                            return "AAN";
                        } else {
                            if (Data_UKPostcodeHandler.get_ABCDEFGHJKSTUW().contains(_2)) {
                                return "ANA";
                            } else {
                                System.err.println(unresolvedMessage);
                                return "";
                            }
                        }
                    }
                }
            } else {
                System.err.println(unresolvedMessage);
                return "";
            }
        }
        if (firstPartPostcode.length() == 2) {
            String _0 = firstPartPostcode.substring(0, 1);
            String _1 = firstPartPostcode.substring(1, 2);
            if (Data_UKPostcodeHandler.get_AtoZ_not_QVX().contains(_0)) {
                if (Data_UKPostcodeHandler.get_0to9().contains(_1)) {
                    return "AN";
                }
            }
        }
        System.err.println(unresolvedMessage);
        return "";
    }

    /**
     * Postcode Formats: AN NAA example M1 1AA; ANN NAA (example, M60 1NW); AAN
     * NAA (example, CR2 6XH); AANN NAA (example, DN55 1PT); ANA NAA (example,
     * W1A 1HQ); AANA NAA (example, EC1A 1BB). The letters Q, V and X are not
     * used in the first position. The letters I, J and Z are not used in the
     * second position. The only letters to appear in the third position are A,
     * B, C, D, E, F, G, H, J, K, S, T, U and W. The only letters to appear in
     * the fourth position are A, B, E, H, M, N, P, R, V, W, X and Y. The second
     * half of the Postcode is always consistent numeric, alpha, alpha format
     * and the letters C, I, K, M, O and V are never used. These conventions may
     * change in the future if operationally required. GIR 0AA is a Postcode
     * that was issued historically and does not confirm to current rules on
     * valid Postcode formats, It is however, still in use. The Postcode is a
     * combination of between five and seven letters / numbers which define four
     * different levels of geographic unit. It is part of a coding system
     * created and used by the Royal Mail across the United Kingdom for the
     * sorting of mail. The Postcodes are an abbreviated form of address which
     * enable a group of delivery points (a delivery point being a property or a
     * post box) to be specifically identified. There are two types of Postcode,
     * these being large and small user Postcodes. A large user Postcode is one
     * that has been assigned to a single address due to the large volume of
     * mail received at that address. A small user Postcode identifies a group
     * of delivery points. On average there are 15 delivery points per Postcode,
     * however this can vary between 1 and 100. Each Postcode consists of two
     * parts. The first part is the Outward Postcode, or Outcode. This is
     * separated by a single space from the second part which is the Inward
     * Postcode, or Incode. The Outward Postcode enables mail to be sent to the
     * correct local area for delivery. This part of the code contains the area
     * and the district to which the mail is to be delivered. The Inward
     * Postcode is used to sort the mail at the local area delivery office. It
     * consists of a numeric character followed by two alphabetic characters.
     * The numeric character identifies the sector within the postal district.
     * The alphabetic characters then define one or more properties within the
     * sector. An example Postcode is PO1 3AX. PO refers to the Postcode Area of
     * Portsmouth. There are 124 Postcode Areas in the UK. PO1 refers to a
     * Postcode District within the Postcode Area of Portsmouth. There are
     * approximately 2900 Postcode Districts. PO1 3 refers to the Postcode
     * Sector. There are approximately 9,650 Postcode Sectors. The AX completes
     * the Postcode. The last two letters define the 'Unit Postcode' which
     * identifies one or more small user delivery points or an individual Large
     * User. There are approximately 1.71 million Unit Postcodes in the UK.
     * (http://www.govtalk.gov.uk/gdsc/html/frames/PostCode.htm) PostCode Type
     * as HTML: <xsd:simpleType name="PostCodeType"> <xsd:annotation>
     * <xsd:documentation>complex pattern for postcode, which matches
     * definition, accepted by some parsers is: "(GIR 0AA) |
     * ((([A-Z-[QVX]][0-9][0-9]?) | (([A-Z-[QVX]][A-Z-[IJZ]][0-9][0-9]?) |
     * (([A-Z-[QVX]][0-9][A-HJKSTUW]) |
     * ([A-Z-[QVX]][A-Z-[IJZ]][0-9][ABEHMNPRVWXY]))))
     * [0-9][A-Z-[CIKMOV]]{2})"</xsd:documentation> </xsd:annotation>
     * <xsd:restriction base="xsd:string"> <xsd:pattern
     * value="[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][A-Z-[CIKMOV]]{2}"/>
     * </xsd:restriction> </xsd:simpleType>
     * (http://www.govtalk.gov.uk/gdsc/schemaHtml/bs7666-v2-0-xsd-PostCodeType.htm)
     * (see also XML schema document BS7666
     * http://www.govtalk.gov.uk/gdsc/schemas/bs7666-v2-0.xsd)
     *
     * @param restart controls the mode of operation. If restart is true then
     * log files are open and read to identify where an interrupted run had got
     * to in order to restart.
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
        HashSet<Future> futures = new HashSet<Future>();
//        // Format   Example Postcode
//        // AN NAA   W1 1NW
//        HashSet<Future> results_AN_NAA = format_AN_NAA(restart);
//        futures.addAll(results_AN_NAA);
//        // Format   Example Postcode
//        // ANN NAA  M11 1NW
//        HashSet<Future> results_ANN_NAA = format_ANN_NAA(restart);
//        futures.addAll(results_ANN_NAA);
//        // Format 	Example Postcode
//        //  AAN NAA     CR2 6XH
//        HashSet<Future> results_AAN_NAA = format_AAN_NAA(restart);
//        futures.addAll(results_AAN_NAA);
//        // Format 	Example Postcode
//        //  ANA NAA     W1A 1HQ
//        // ANA_NAA
//        HashSet<Future> results_ANA_NAA = format_ANA_NAA(restart);
//        futures.addAll(results_ANA_NAA);
        // Format 	Example Postcode
        //  AANA NAA    EC1A 1BB
        HashSet<Future> results_AANA_NAA = format_AANA_NAA(restart);
        futures.addAll(results_AANA_NAA);
        // Format 	Example Postcode
        //  AANN NAA    DN12 1PT
        HashSet<Future> results_AANN_NAA = format_AANN_NAA(restart);
        futures.addAll(results_AANN_NAA);
        // Wait for results then shutdown executorService
        Generic_Execution.shutdownExecutorService(
                getExecutorService(),
                futures,
                this,
                100000L,
                10L);
    }

    private HashSet<Future> format_AN_NAA(boolean restart) throws IOException {
        HashSet<Future> result = new HashSet<>();
        Web_Run_an_naa a_Run_an_naa = new Web_Run_an_naa(
                this, restart);
        result.add(getExecutorService().submit(a_Run_an_naa));
        return result;
    }

    /**
     * Format Example Postcode ANA NAA: E1A 1BB
     */
    private HashSet<Future> format_ANA_NAA(boolean restart)
            throws IOException {
        Iterator<String> _AtoZ_not_QVX_Iterator0 = Data_UKPostcodeHandler.get_AtoZ_not_QVX().iterator();
        HashSet<Future> result = new HashSet<Future>();
        while (_AtoZ_not_QVX_Iterator0.hasNext()) {
            String a0 = (String) _AtoZ_not_QVX_Iterator0.next();
            Web_Run_ana_naa a_Run_ana_naa = new Web_Run_ana_naa(
                    this, restart);
            result.add(getExecutorService().submit(a_Run_ana_naa));
        }
        return result;
    }

    /**
     * Format Example Postcode AANA NAA: EC1A 1BB
     */
    private HashSet<Future> format_AANA_NAA(boolean restart)
            throws IOException {
        Iterator<String> _AtoZ_not_QVX_Iterator0 = Data_UKPostcodeHandler.get_AtoZ_not_QVX().iterator();
        HashSet<Future> result = new HashSet<Future>();
        while (_AtoZ_not_QVX_Iterator0.hasNext()) {
            String a0 = (String) _AtoZ_not_QVX_Iterator0.next();
            Iterator<String> _AtoZ_not_IJZ_Iterator = Data_UKPostcodeHandler.get_AtoZ_not_IJZ().iterator();
            while (_AtoZ_not_IJZ_Iterator.hasNext()) {
                String a1 = _AtoZ_not_IJZ_Iterator.next();
                firstpartPostcode = a0 + a1;
                System.out.println("format_AANA " + firstpartPostcode + "NA");
                Web_Run_aana_naa a_Run_aana_naa = new Web_Run_aana_naa(
                        this,
                        restart);
                result.add(getExecutorService().submit(a_Run_aana_naa));
            }
        }
        return result;
    }

    /**
     * Format Example Postcode AANN NAA: DN12 1BB
     */
    private HashSet<Future> format_AANN_NAA(boolean restart)
            throws IOException {
        HashSet<Future> result = new HashSet<Future>();
        Iterator<String> _AtoZ_not_QVX_Iterator0 = Data_UKPostcodeHandler.get_AtoZ_not_QVX().iterator();
        while (_AtoZ_not_QVX_Iterator0.hasNext()) {
            String a0 = (String) _AtoZ_not_QVX_Iterator0.next();

//            if (!(a0.equalsIgnoreCase("A") || a0.equalsIgnoreCase("B"))) {
//                //System.out.println("format_aann " + a0 + "ann");

//                String a0 = "C";

            Iterator<String> _AtoZ_not_IJZ_Iterator = Data_UKPostcodeHandler.get_AtoZ_not_IJZ().iterator();
            while (_AtoZ_not_IJZ_Iterator.hasNext()) {
                String a1 = _AtoZ_not_IJZ_Iterator.next();
                firstpartPostcode = a0 + a1;
                System.out.println("format_AANN " + firstpartPostcode + "NN");

                Web_Run_aann_naa a_Run_aann_naa = new Web_Run_aann_naa(
                        this, restart);
                result.add(getExecutorService().submit(a_Run_aann_naa));
            }
//            }
        }
        return result;
    }

    /**
     * Format Example Postcode AAN NAA: EN1 1BB
     *
     * @param firstpartpostcode This is expected in the form of up to 3
     * characters.
     * @TODO Add checking further up in the program when ascertaining what type
     * of postcode is being parsed to ensure it is worth firing a request. Only
     * potentially valid postcodes should have requests sent.
     */
    private HashSet<Future> format_AAN_NAA(
            String firstpartpostcode,
            boolean restart)
            throws IOException {
        if (firstpartpostcode != null) {
            if (firstpartpostcode.length() != 0) {
                HashSet<Future> result = new HashSet<Future>();
                int firstpartpostcodelength = firstpartpostcode.length();
                if (firstpartpostcodelength > 0 && firstpartpostcodelength < 4) {
                    Web_Run_aan_naa a_Run_aan_naa = new Web_Run_aan_naa(
                            this,
                            restart);
                    result.add(getExecutorService().submit(a_Run_aan_naa));
                }
                return result;
            }
        }
        return format_AAN_NAA(restart);
    }

    /**
     * Format Example Postcode AAN NAA: EN1 1BB
     */
    private HashSet<Future> format_AAN_NAA(boolean restart)
            throws IOException {
        Iterator<String> _AtoZ_not_QVX_Iterator0 = Data_UKPostcodeHandler.get_AtoZ_not_QVX().iterator();
        HashSet<Future> result = new HashSet<Future>();
        while (_AtoZ_not_QVX_Iterator0.hasNext()) {
            firstpartPostcode = (String) _AtoZ_not_QVX_Iterator0.next();
            Web_Run_aan_naa a_Run_aan_naa = new Web_Run_aan_naa(
                    this,
                    restart);
            result.add(getExecutorService().submit(a_Run_aan_naa));
        }
        return result;
    }

    /**
     * Format Example Postcode ANN NAA: E11 1BB
     */
    private HashSet<Future> format_ANN_NAA(boolean restart)
            throws IOException {
        Iterator<String> _AtoZ_not_QVX_Iterator0 = Data_UKPostcodeHandler.get_AtoZ_not_QVX().iterator();
        HashSet<Future> result = new HashSet<Future>();
        while (_AtoZ_not_QVX_Iterator0.hasNext()) {
            String a0 = (String) _AtoZ_not_QVX_Iterator0.next();
            Web_Run_ann_naa a_Run_ann_naa = new Web_Run_ann_naa(
                    this, restart);
            result.add(getExecutorService().submit(a_Run_ann_naa));
        }
        return result;
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
     * @param addressAdditionalPropertyDetails
     * @param tSecondPartOfPostcode
     * @param tFirstPartOfPostcode
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
            Iterator aIterator = prices.iterator();
            while (aIterator.hasNext()) {
                outPW.write((String) aIterator.next());
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
     * @return number of records
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
        TreeSet<String> result = new TreeSet<String>();
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

        tFormattedOutput =
                tFirstPartOfPostcode + " "
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
            lowerCaseAdd0 = Generic_String.getLowerCase(splitAddress[0]).trim();
            lowerCaseAdd1 = lowerCaseAdd0.replaceAll(" ", "-");
        } else {
            if (splitAddress.length == 3 || splitAddress.length == 4) {
                lowerCaseAdd0 = Generic_String.getLowerCase(splitAddress[1]).trim();
                lowerCaseAdd1 = lowerCaseAdd0.replaceAll(" ", "-");
            } else {
                try {
                    String message = "splitAddress.length == "+ splitAddress.length + ":"
                            + " splitAddress.length != 2 || 3 || 4 :"
                            + " splitAddress; ";
                    for (int i = 0; i < splitAddress.length; i ++) {
                        message += splitAddress[i] + ", ";
                    }
                    throw new Exception(message);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                    Logger.getLogger(Web_ZooplaHousepriceScraper.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Web_ZooplaHousepriceScraper.class.getName()).log(Level.SEVERE, null, ex);
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
