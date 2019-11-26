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

public class Web_Run_an_naa extends Web_AbstractRun {

    /**
     * @param z The Web_ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Web_Run_an_naa(Web_ZooplaHousepriceScraper z, boolean restart) {
        init(z, restart);
    }

    @Override
    public void run() {
        try {
            if (restart == false) {
                formatNew();
            } else {
                // Initialise output files
                String filenamepart = "_Houseprices_an";
                String[] pfr = getPostcodeForRestart(
                        getType(), filenamepart);
                if (pfr == null) {
                    formatNew();
                } else {
                    if (pfr[0] != null) {
                        String a0Restart = pfr[0].substring(0, 1);
                        int n0Restart = Integer.valueOf(pfr[0].substring(1, 2));
                        initialiseOutputs(getType(), filenamepart);
                        // Process
                        int n0;
                        int[] counts = getCounts();
                        boolean a0Restarter = false;
                        boolean n0Restarter = false;
                        boolean sppr = false;
                        for (int i = 0; i < UKPC_Checker.AtoZ_not_QVX.length; i++) {
                            String a0 = String.valueOf(UKPC_Checker.AtoZ_not_QVX[i]).toLowerCase();
                            if (!a0Restarter) {
                                if (a0.equalsIgnoreCase(a0Restart)) {
                                    a0Restarter = true;
                                }
                            } else {
                                for (n0 = 0; n0 < 10; n0++) {
                                    if (!n0Restarter) {
                                        if (n0 == n0Restart) {
                                            n0Restarter = true;
                                        }
                                    } else {
                                        String fpp = a0 + Integer.toString(n0);
                                        String aURLString0 = url + fpp;
                                        sppr = doX(sppr, pfr, fpp, aURLString0, counts);
                                    }
                                }
                            }
//                        System.out.println(getReportString(counts));
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
        // Initialise output files
        String filenamepart = "_Houseprices_an";
        initialiseOutputs(UKPC_Checker.TYPE_AN, filenamepart);
        // Process
        int n0;
        int[] counts = getCounts();
        for (int i = 0; i < UKPC_Checker.AtoZ_not_QVX.length; i++) {
            String a0 = String.valueOf(UKPC_Checker.AtoZ_not_QVX[i]).toLowerCase();
            for (n0 = 0; n0 < 10; n0++) {
                String fpp = a0 + Integer.toString(n0);
                String aURLString0 = url + fpp;
                doX(fpp, aURLString0, counts);
            }
//            System.out.println(getReportString(counts));
        }
        // Final reporting
        finalise(counts);
    }
}
