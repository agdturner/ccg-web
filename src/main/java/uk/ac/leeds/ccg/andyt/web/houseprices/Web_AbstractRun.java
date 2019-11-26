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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.postcode.UKPC_Checker;
import uk.ac.leeds.ccg.andyt.web.core.Web_Object;

/**
 * To be extended by Run methods.
 */
public abstract class Web_AbstractRun extends Web_Object implements Runnable {

    UKPC_Checker pcc;

    /**
     * For storing which type of class this is (for convenience). Known types
     * are "AANN", "AANA", "ANN", "ANA", "AAN", "AN".
     */
    private int type;
    protected boolean restart;
    protected Web_ZooplaHousepriceScraper ZooplaHousepriceScraper;
    // For convenience
    protected String url;
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
    public int getType() {
        return type;
    }

    /**
     * For initialising type.
     */
    protected void initType() {
        if (this instanceof Web_Run_aann_naa) {
            type = UKPC_Checker.TYPE_AANN;
        }
        if (this instanceof Web_Run_aana_naa) {
            type = UKPC_Checker.TYPE_AANA;
        }
        if (this instanceof Web_Run_ann_naa) {
            type = UKPC_Checker.TYPE_ANN;
        }
        if (this instanceof Web_Run_ana_naa) {
            type = UKPC_Checker.TYPE_ANA;
        }
        if (this instanceof Web_Run_aan_naa) {
            type = UKPC_Checker.TYPE_AAN;
        }
        if (this instanceof Web_Run_an_naa) {
            type = UKPC_Checker.TYPE_AN;
        }
    }

    /**
     * @param z
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    protected final void init(Web_ZooplaHousepriceScraper z, boolean restart) {
        initType();
        this.ZooplaHousepriceScraper = z;
        this.firstpartPostcode = z.getFirstpartPostcode();
        this.url = z.getUrl();
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

    protected void initialiseOutputs(int type, String filenamepart) throws IOException {
        File outDir = new File(ZooplaHousepriceScraper.getDirectory(), "" + type);
        outDir.mkdirs();
        outFile = new File(outDir, filenamepart + ".csv");
        logFile = new File(outDir, filenamepart + ".log");
        //sharedLogPR = Generic_IO.getPrintWriter(sharedLogFile, true);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        outPR = env.env.io.getPrintWriter(outFile, restart);
        logPR = env.env.io.getPrintWriter(logFile, restart);
    }

    /**
     * Checks the log file for completed run.If run completed then null is
     * returned otherwise a String[] of length 2 is returned the first element
     * being the first part of last postcode returned, the second part being the
     * second part of the last postcode returned.
     *
     * @param type
     * @param filenamepart
     * @return
     * @throws java.io.FileNotFoundException
     */
    protected String[] getPostcodeForRestart(int type, String filenamepart)
            throws FileNotFoundException, IOException {
        String[] r;
        File outDir = new File(ZooplaHousepriceScraper.getDirectory(), "" + type);
        if (!outDir.exists()) {
            return null;
        }
        outDir.mkdirs();
        logFile = new File(outDir, filenamepart + ".log");
        if (logFile.length() == 0L) {
            return null;
        }
        String line;
        try (BufferedReader br = env.env.io.getBufferedReader(logFile)) {
            StreamTokenizer st = new StreamTokenizer(br);
            env.env.io.setStreamTokenizerSyntax1(st);
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
            r[0] = fields[0].toLowerCase();
            r[1] = fields[1].toLowerCase();
        }
        return r;
    }

    /**
     * @param counts[0] counter, counts[1] numberOfHousepriceRecords, counts[2]
     * numberOfPostcodesWithHousepriceRecords.
     * @return
     */
    protected String getReportString(int[] counts) {
        return "Attempted " + counts[0]
                + ", Number Of Houseprice Records " + counts[1]
                + ", Number Of Postcodes With Houseprice Records " + counts[2];
    }

    /**
     * @param counts[0] counter, counts[1] numberOfHousepriceRecords, counts[2]
     * numberOfPostcodesWithHousepriceRecords.
     */
    protected void finalise(int counts[]) {
        // Final reporting
        System.out.println("Attempted " + counts[0]);
        logPR.println("numberOfHousepriceRecords " + counts[1]);
        System.out.println("numberOfHousepriceRecords " + counts[1]);
        logPR.println("numberOfPostcodesWithHousepriceRecords " + counts[2]);
        System.out.println("numberOfPostcodesWithHousepriceRecords " + counts[2]);
        outPR.close();
        logPR.close();
    }

    /**
     * @param fpp firstPartPostcode
     * @param aURLString0
     * @param counts counter, numberOfHousepriceRecords,
     * numberOfPostcodesWithHousepriceRecords
     */
    protected void doX(String fpp, String aURLString0, int[] counts) {
        checkRequestRate();
        if (ZooplaHousepriceScraper.isReturningOutcode(fpp, aURLString0)) {
            for (int j = 0; j < UKPC_Checker.digits.length; j++) {
                String n = String.valueOf(UKPC_Checker.digits[j]);
                for (int k = 0; k < UKPC_Checker.AtoZ_not_CIKMOV.length; k++) {
                    String na = n + String.valueOf(UKPC_Checker.AtoZ_not_CIKMOV[k]);
                    for (int l = 0; l < UKPC_Checker.AtoZ_not_CIKMOV.length; l++) {
                        String naa = (na + String.valueOf(UKPC_Checker.AtoZ_not_CIKMOV[l])).toLowerCase();
                        String aURLString = aURLString0 + "-" + naa;
                        int i = ZooplaHousepriceScraper.writeHouseprices(
                                outPR, logPR, sharedLogPR, aURLString, fpp,
                                naa, addressAdditionalPropertyDetails);
                        counts[0] = counts[0] + 1;
                        counts[1] = counts[1] + i;
                        if (i > 0) {
                            counts[2] = counts[2] + 1;
                        }
                    }
                }
            }
        } else {
            Web_ZooplaHousepriceScraper.updateLog(logPR, sharedLogPR, fpp);
        }
    }

    /**
     * @param sppr secondPartPostcodeRestarter
     * @param pfr postcodeForRestart
     * @param fpp firstPartPostcode
     * @param aURLString0
     * @param counts counter, numberOfHousepriceRecords,
     * numberOfPostcodesWithHousepriceRecords
     * @return 
     */
    protected boolean doX(boolean sppr, String[] pfr, String fpp, String aURLString0, int[] counts) {
        checkRequestRate();
        if (ZooplaHousepriceScraper.isReturningOutcode(fpp, aURLString0)) {
            for (int j = 0; j < UKPC_Checker.digits.length; j++) {
                String n = String.valueOf(UKPC_Checker.digits[j]);
                for (int k = 0; k < UKPC_Checker.AtoZ_not_CIKMOV.length; k++) {
                    String na = n + String.valueOf(UKPC_Checker.AtoZ_not_CIKMOV[k]);
                    for (int l = 0; l < UKPC_Checker.AtoZ_not_CIKMOV.length; l++) {
                        String naa = (na + String.valueOf(UKPC_Checker.AtoZ_not_CIKMOV[l])).toLowerCase();
                        if (!sppr) {
                            if (naa.equalsIgnoreCase(pfr[1])) {
                                sppr = true;
                            }
                        } else {
                            String aURLString = aURLString0 + "-" + naa;
                            int i = ZooplaHousepriceScraper.writeHouseprices(
                                    outPR, logPR, sharedLogPR, aURLString, fpp,
                                    naa, addressAdditionalPropertyDetails);
                            counts[0] = counts[0] + 1;
                            counts[1] = counts[1] + i;
                            if (i > 0) {
                                counts[2] = counts[2] + 1;
                            }
                        }
                    }
                }
            }
        } else {
            Web_ZooplaHousepriceScraper.updateLog(logPR, sharedLogPR, fpp);
        }
        return sppr;
    }

    /**
     * @return A int[] of size 3 with values set to 0.
     */
    public int[] getCounts() {
        int[] counts = new int[3];
        counts[0] = 0;
        counts[1] = 0;
        counts[2] = 0;
        return counts;
    }
}
