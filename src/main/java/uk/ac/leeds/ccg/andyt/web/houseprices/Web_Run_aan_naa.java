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

import java.io.IOException;
import uk.ac.leeds.ccg.andyt.postcode.UKPC_Checker;

public class Web_Run_aan_naa extends Web_AbstractRun {

    /**
     * @param z The Web_ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Web_Run_aan_naa(Web_ZooplaHousepriceScraper z, boolean restart) {
        init(z, restart);
    }

    @Override
    public void run() {
        try {
            if (restart == false) {
                formatNew();
            } else {
                if (firstpartPostcode.length() > 1) {
                    throw new Exception("firstpartPostcode.length() > 1");
                }
                // Initialise output files
                String filenamepart = "_Houseprices_" + firstpartPostcode + "an";
                /**
                 * pfr postcodeForRestart
                 */
                String[] pfr = getPostcodeForRestart(getType(), filenamepart);
                if (pfr == null) {
                    formatNew();
                } else {
                    if (pfr[0] != null) {
                        String a1Restart = pfr[0].substring(1, 2);
                        int n0Restart = Integer.valueOf(pfr[0].substring(2, 3));
                        int[] counts = getCounts();
                        // Initialise output files
                        initialiseOutputs(getType(), filenamepart);
                        // Process
                        boolean n0Restarter = false;
                        boolean a1Restarter = false;
                                                boolean sppr = false;
                        for (int i = 0; i < UKPC_Checker.AtoZ_not_IJZ.length; i++) {
                            String a1 = String.valueOf(UKPC_Checker.AtoZ_not_IJZ[i]).toLowerCase();
                            if (!a1Restarter) {
                                if (a1.equalsIgnoreCase(a1Restart)) {
                                    a1Restarter = true;
                                }
                            } else {
                                for (int n0 = 0; n0 < 10; n0++) {
                                    if (!n0Restarter) {
                                        if (n0 == n0Restart) {
                                            n0Restarter = true;
                                        }
                                    } else {
                                        // cfpp completeFirstPartPostcode
                                        String fpp = firstpartPostcode + a1 + Integer.toString(n0);
                                        String aURLString0 = url + fpp;
                                        checkRequestRate();
                                        sppr = doX(sppr, pfr, fpp, aURLString0, counts);
                                        doX(fpp, aURLString0, counts);
                                    }
                                }
                            }
                        }
                        // Final reporting
                        finalise(counts);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            env.env.log(ex.getMessage());
        }
    }

    private void formatNew() throws IOException {
        int[] counts = getCounts();
        if (firstpartPostcode.length() == 3) {
            // Initialise output files
            String filenamepart = "_Houseprices_" + firstpartPostcode;
            initialiseOutputs(getType(), filenamepart);
            // Process
            String aURLString0 = url + firstpartPostcode;
            checkRequestRate();
            doX(firstpartPostcode, aURLString0, counts);
        } else {
            if (firstpartPostcode.length() == 2) {
                // Initialise output files
                String filenamepart = "_Houseprices_" + firstpartPostcode + "n";
                initialiseOutputs(getType(), filenamepart);
                // Process
                for (int n0 = 0; n0 < 10; n0++) {
                    String fpp = firstpartPostcode + Integer.toString(n0);
                    String aURLString0 = url + fpp;
                    checkRequestRate();
                    doX(fpp, aURLString0, counts);
                }
            } else {
                // Initialise output files
                String filenamepart = "_Houseprices_" + firstpartPostcode + "an";
                initialiseOutputs(UKPC_Checker.TYPE_AAN, filenamepart);
                // Process
                for (int i = 0; i < UKPC_Checker.AtoZ_not_IJZ.length; i++) {
                    String a1 = String.valueOf(UKPC_Checker.AtoZ_not_IJZ[i]).toLowerCase();
                    for (int n0 = 0; n0 < 10; n0++) {
                        String fpp = firstpartPostcode + a1 + Integer.toString(n0);
                        String aURLString0 = url + fpp;
                        checkRequestRate();
                        doX(fpp, aURLString0, counts);
                    }
                }
            }
        }
        // Final reporting
        finalise(counts);
    }
}
