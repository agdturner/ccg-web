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
import uk.ac.leeds.ccg.andyt.data.postcode.Data_UKPostcodeHandler;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.web.core.Web_Object;

/**
 * To be extended by Run methods.
 */
public abstract class Web_AbstractRun extends Web_Object implements Runnable {

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
     * @param s
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    protected void init( Web_ZooplaHousepriceScraper s,
            boolean restart) {
        initType();
        this.ZooplaHousepriceScraper = s;
        this.firstpartPostcode = s.getFirstpartPostcode();
        this.url = s.getUrl();
        this._NAA = Data_UKPostcodeHandler.get_NAA();
        this.restart = restart;
        this.addressAdditionalPropertyDetails = new TreeMap<>();
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
            //sharedLogPR = Generic_IO.getPrintWriter(sharedLogFile, true);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            outPR = env.io.getPrintWriter(outFile, restart);
            logPR = env.io.getPrintWriter(logFile, restart);
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
    protected String[] getPostcodeForRestart(String type, String filenamepart) {
        String[] r = null;
        try {
            File outDir = new File(ZooplaHousepriceScraper.getDirectory(), type);
            if (!outDir.exists()) {
                return null;
            }
            outDir.mkdirs();
            logFile = new File(outDir, filenamepart + ".log");
            if (logFile.length() == 0L) {
                return null;
            }
            String line;
            try (BufferedReader br = env.io.getBufferedReader(logFile)) {
                StreamTokenizer st = new StreamTokenizer(br);
                env.io.setStreamTokenizerSyntax1(st);
                int token = st.nextToken();
                line = null;
                while (token != StreamTokenizer.TT_EOF) {
                    switch (token) {
                        case StreamTokenizer.TT_WORD:
                            line = st.sval;
                        case StreamTokenizer.TT_EOL:
                            break;
                    }
                    token = st.nextToken();
                }
            }
            String[] fields = null;
            if (line != null) {
                fields = line.split(" ");
            }
            r = new String[2];
            if (fields[0].startsWith("number")) {
                return r;
            } else {
                r = new String[2];
                r[0] = Generic_String.getLowerCase(fields[0]);
                r[1] = Generic_String.getLowerCase(fields[1]);
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
        return r;
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
