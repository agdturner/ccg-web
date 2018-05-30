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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;

/**
 * To be extended by Run methods.
 */
public abstract class Web_AbstractRun implements Runnable {

    /**
     * For storing which type of class this is (for convenience). Known types
     * are "AANN", "AANA", "ANN", "ANA", "AAN", "AN".
     */
    private String type;
    protected boolean restart;
    protected Web_ZooplaHousepriceScraper ZooplaHousepriceScraper;
    // For convenience
    protected String url;
    /**
     * A reference to ZooplaHousepriceScraper._NAA for convenience
     */
    protected TreeSet<String> _NAA;
    // Other fields
    /**
     * For storing the first part of a postcode in lower case
     */
    protected String firstpartPostcode;
    protected File outFile;
    protected PrintWriter outPR;
    protected File logFile;
    protected PrintWriter logPR;
    protected PrintWriter sharedLogPR;
    /**
     * For storing property codes with additional property details.
     *
     * @TODO Currently property details are assumed not to change over time,
     * when in reality this might be the case.
     */
    TreeMap<String, String> addressAdditionalPropertyDetails;

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * For initialising type.
     */
    protected void initType() {
        if (this instanceof Web_Run_aann_naa) {
            type = "AANN";
        }
        if (this instanceof Web_Run_aana_naa) {
            type = "AANA";
        }
        if (this instanceof Web_Run_ann_naa) {
            type = "ANN";
        }
        if (this instanceof Web_Run_ana_naa) {
            type = "ANA";
        }
        if (this instanceof Web_Run_aan_naa) {
            type = "AAN";
        }
        if (this instanceof Web_Run_an_naa) {
            type = "AN";
        }
    }

    /**
     * @param tZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     * @param sharedLogFile The log file in a directory into which the results
     * will be stored.
     */
    protected void init(
            Web_ZooplaHousepriceScraper tZooplaHousepriceScraper,
            boolean restart) {
        initType();
        this.ZooplaHousepriceScraper = tZooplaHousepriceScraper;
        this.firstpartPostcode = tZooplaHousepriceScraper.getFirstpartPostcode();
        this.url = tZooplaHousepriceScraper.getUrl();
        this._NAA = Generic_UKPostcode_Handler.get_NAA();
        this.restart = restart;
        this.addressAdditionalPropertyDetails = new TreeMap<String, String>();
    }

    public void checkRequestRate() {
//        double timeDiffSecs = (double) (System.currentTimeMillis() - startTime) / 1000D;
//        long requests = sharedLogFile.length();
//        double requestsRate = (double) requests / (double) timeDiffSecs;
//        synchronized (this) {
//            while (requestsRate > allowedRequestsPerSecond) {
//                try {
//                    this.wait(1000);
//                    timeDiffSecs = (double) (System.currentTimeMillis() - startTime) / 1000D;
//                    requests = sharedLogFile.length();
//                    requestsRate = (double) requests / (double) timeDiffSecs;
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Web_AbstractRun.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
    }

    protected void initialiseOutputs(
            String type,
            String filenamepart) {
        try {
            File outDirectory = new File(
                    ZooplaHousepriceScraper.getDirectory(),
                    type);
            outDirectory.mkdirs();
            outFile = new File(outDirectory, filenamepart + ".csv");
            logFile = new File(outDirectory, filenamepart + ".log");
            //sharedLogPR = Generic_StaticIO.getPrintWriter(sharedLogFile, true);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            outPR = Generic_StaticIO.getPrintWriter(outFile, restart);
            logPR = Generic_StaticIO.getPrintWriter(logFile, restart);
        } catch (IOException ex) {
            System.err.println(ex.toString());
            Logger.getLogger(Web_AbstractRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks the log file for completed run. If run completed then null is
     * returned otherwise a String[] of length 2 is returned the first element
     * being the first part of last postcode returned, the second part being the
     * second part of the last postcode returned.
     *
     * @param type
     * @param filenamepart
     * @return
     */
    protected String[] getPostcodeForRestart(
            String type,
            String filenamepart) {
        String[] result = null;
        try {
            File outDirectory = new File(ZooplaHousepriceScraper.getDirectory(), type);
            if (!outDirectory.exists()) {
                return null;
            }
            outDirectory.mkdirs();
            logFile = new File(outDirectory, filenamepart + ".log");
            if (logFile.length() == 0L) {
                return null;
            }
            BufferedReader br = Generic_StaticIO.getBufferedReader(logFile);
            StreamTokenizer st = new StreamTokenizer(br);
            Generic_StaticIO.setStreamTokenizerSyntax1(st);
            int token = st.nextToken();
            String line = null;
            while (token != StreamTokenizer.TT_EOF) {
                switch (token) {
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                    case StreamTokenizer.TT_EOL:
                        break;
                }
                token = st.nextToken();
            }
            br.close();
            String[] fields = null;
            if (line != null) {
                fields = line.split(" ");
            }
            result = new String[2];
            if (fields[0].startsWith("number")) {
                return result;
            } else {
                result = new String[2];
                result[0] = Generic_StaticString.getLowerCase(fields[0]);
                result[1] = Generic_StaticString.getLowerCase(fields[1]);
                String firstPartPostcodeType = ZooplaHousepriceScraper.getFirstPartPostcodeType(fields[0]);
                String secondPartPostcodeType = ZooplaHousepriceScraper.getSecondPartPostcodeType(fields[1]);
                if (!(firstPartPostcodeType.equalsIgnoreCase(getType())
                        && secondPartPostcodeType.equalsIgnoreCase("naa"))) {
                    return null;
                }
                /**
                 * fields[0] contains a valid first part of a postcode and
                 * fields[1] contains a valid second part of a postcode
                 */
            }
        } catch (IOException ex) {
            Logger.getLogger(Web_AbstractRun.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    protected String getReportString(
            int counter,
            int numberOfHousepriceRecords,
            int numberOfPostcodesWithHousepriceRecords) {
        return "Attempted " + counter
                + ", Number Of Houseprice Records " + numberOfHousepriceRecords
                + ", Number Of Postcodes With Houseprice Records " + numberOfPostcodesWithHousepriceRecords;
    }

    protected void finalise(
            int counter,
            int numberOfHousepriceRecords,
            int numberOfPostcodesWithHousepriceRecords) {
        // Final reporting
        System.out.println("Attempted " + counter);
        logPR.println("numberOfHousepriceRecords " + numberOfHousepriceRecords);
        System.out.println("numberOfHousepriceRecords " + numberOfHousepriceRecords);
        logPR.println("numberOfPostcodesWithHousepriceRecords " + numberOfPostcodesWithHousepriceRecords);
        System.out.println("numberOfPostcodesWithHousepriceRecords " + numberOfPostcodesWithHousepriceRecords);
        outPR.close();
        logPR.close();
    }
}
