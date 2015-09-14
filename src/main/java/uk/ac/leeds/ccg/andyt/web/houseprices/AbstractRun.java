/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognise copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the
 * public domain. We make this dedication for the benefit of the public at large
 * and to the detriment of our heirs and successors. We intend this dedication
 * to be an overt act of relinquishment in perpetuity of all present and future
 * rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;

/**
 * To be extended by Run methods.
 */
public abstract class AbstractRun implements Runnable {

    /**
     * For storing which type of class this is (for convenience). Known types
     * are "AANN", "AANA", "ANN", "ANA", "AAN", "AN".
     */
    private String type;
    protected boolean restart;
    protected ZooplaHousepriceScraper tZooplaHousepriceScraper;
    // For convenience
    protected String url;
    /**
     * A reference to tZooplaHousepriceScraper._NAA for convenience
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
        if (this instanceof Run_aann_naa) {
            type = "AANN";
        }
        if (this instanceof Run_aana_naa) {
            type = "AANA";
        }
        if (this instanceof Run_ann_naa) {
            type = "ANN";
        }
        if (this instanceof Run_ana_naa) {
            type = "ANA";
        }
        if (this instanceof Run_aan_naa) {
            type = "AAN";
        }
        if (this instanceof Run_an_naa) {
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
            ZooplaHousepriceScraper tZooplaHousepriceScraper,
            boolean restart) {
        initType();
        this.tZooplaHousepriceScraper = tZooplaHousepriceScraper;
        this.firstpartPostcode = tZooplaHousepriceScraper.getFirstpartPostcode();
        this.url = tZooplaHousepriceScraper.getUrl();
        this._NAA = tZooplaHousepriceScraper.getNAA();
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
//                    Logger.getLogger(AbstractRun.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
    }

    protected void initialiseOutputs(
            String type,
            String filenamepart) {
        try {
            File outDirectory = new File(
                    tZooplaHousepriceScraper.getDirectory(),
                    type);
            outDirectory.mkdirs();
            outFile = new File(
                    outDirectory,
                    filenamepart + ".csv");
            logFile = new File(
                    outDirectory,
                    filenamepart + ".log");
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
            Logger.getLogger(AbstractRun.class.getName()).log(Level.SEVERE, null, ex);
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
            File outDirectory = new File(
                    tZooplaHousepriceScraper.getDirectory(),
                    type);
            if (!outDirectory.exists()) {
                return null;
            }
            outDirectory.mkdirs();
            logFile = new File(
                    outDirectory,
                    filenamepart + ".log");
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
                String firstPartPostcodeType = tZooplaHousepriceScraper.getFirstPartPostcodeType(fields[0]);
                String secondPartPostcodeType = tZooplaHousepriceScraper.getSecondPartPostcodeType(fields[1]);
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
            Logger.getLogger(AbstractRun.class.getName()).log(Level.SEVERE, null, ex);
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
