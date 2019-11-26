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

import uk.ac.leeds.ccg.andyt.postcode.UKPC_Checker;

/**
 * Class for formatting postcodes of the an_naa format.
 */
public class Web_Run_ann_naa extends Web_AbstractRun {

    /**
     * @param z The Web_ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Web_Run_ann_naa(Web_ZooplaHousepriceScraper z, boolean restart) {
        init(z, restart);
    }

    @Override
    public void run() {
        try {
            if (restart == false) {
                formatNew();
            } else {
                if (firstpartPostcode.length() > 1) {
                    throw new Exception();
                }
                // Initialise output files
                String filenamepart = "_Houseprices_" + firstpartPostcode + "nn";
                String[] pfr = getPostcodeForRestart(getType(), filenamepart);
                if (pfr == null) {
                    formatNew();
                } else {
                    int n0Restart = Integer.valueOf(pfr[0].substring(1, 2));
                    int n1Restart = Integer.valueOf(pfr[0].substring(2, 3));
                    // Initialise output files
                    initialiseOutputs(getType(), filenamepart);
                    // Process
                    int n0;
                    int n1;
                    int[] counts = getCounts();
                    boolean n0Restarter = false;
                    boolean n1Restarter = false;
                    boolean sppr = false;
                    for (n0 = 0; n0 < 10; n0++) {
                        if (!n0Restarter) {
                            if (n0 == n0Restart) {
                                n0Restarter = true;
                            }
                        } else {
                            for (n1 = 0; n1 < 10; n1++) {
                                if (!n1Restarter) {
                                    if (n1 == n1Restart) {
                                        n1Restarter = true;
                                    }
                                } else {
                                    String fpp = firstpartPostcode + Integer.toString(n0) + Integer.toString(n1);
                                    String aURLString0 = url + fpp;
                                    sppr = doX(sppr, pfr, fpp, aURLString0, counts);
                                }
                            }
                        }
//            System.out.println(getReportString(counts));
                    }
                    // Final reporting
                    finalise(counts);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            env.env.log(ex.getMessage());
        }
    }

    private void formatNew() throws Exception {
        // Initialise output files
        String filenamepart = "_Houseprices_" + firstpartPostcode + "nn";
        initialiseOutputs(UKPC_Checker.TYPE_ANN, filenamepart);
        if (firstpartPostcode.length() != 1) {
            throw new Exception();
        } else {
            int n0;
            int n1;
            int[] counts = getCounts();
            for (n0 = 0; n0 < 10; n0++) {
                for (n1 = 0; n1 < 10; n1++) {
                    String fpp = firstpartPostcode + Integer.toString(n0)
                            + Integer.toString(n1);
                    String aURLString0 = url + fpp;
                    doX(fpp, aURLString0, counts);
                }
//            System.out.println(getReportString(counts));
            }
            // Final reporting
            finalise(counts);
        }
    }
}
