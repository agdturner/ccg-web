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
 * Class for formatting postcodes of the ana_naa format.
 */
public class Web_Run_ana_naa extends Web_AbstractRun {

    /**
     * @param z The Web_ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Web_Run_ana_naa(Web_ZooplaHousepriceScraper z, boolean restart) {
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
                String filenamepart = "_Houseprices_" + firstpartPostcode + "na";
                String[] pfr = getPostcodeForRestart(getType(), filenamepart);
                if (pfr == null) {
                    formatNew();
                } else {
                    if (pfr[0] != null) {
                        int n0Restart = Integer.valueOf(pfr[0].substring(1, 2));
                        String a1Restart = pfr[0].substring(2, 3);
                        // Initialise output files
                        initialiseOutputs(getType(), filenamepart);
                        // Process
                        int n0;
                        int[] counts = getCounts();
                        boolean n0Restarter = false;
                        boolean a1Restarter = false;
                        boolean sppr = false;
                        for (n0 = 0; n0 < 10; n0++) {
                            if (!n0Restarter) {
                                if (n0 == n0Restart) {
                                    n0Restarter = true;
                                }
                            } else {
                                for (int i = 0; i < UKPC_Checker.ABCDEFGHJKSTUW.length; i++) {
                                    String a1 = String.valueOf(UKPC_Checker.ABCDEFGHJKSTUW[i]).toLowerCase();
                                    if (!a1Restarter) {
                                        if (a1.equalsIgnoreCase(a1Restart)) {
                                            a1Restarter = true;
                                        }
                                    } else {
                                        String fpp = firstpartPostcode + Integer.toString(n0) + a1;
                                        String aURLString0 = url + fpp;
                                        sppr = doX(sppr, pfr, fpp, aURLString0, counts);
                                    }
                                }
                            }
//                        System.out.println(getReportString(counts));
                        }
                        finalise(counts);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            env.env.log(ex.getMessage());
        }
    }

    private void formatNew() throws Exception {
        int[] counts = getCounts();
        if (firstpartPostcode.length() != 1) {
            throw new Exception();
        } else {
            // Initialise output files
            String filenamepart = "_Houseprices_" + firstpartPostcode + "na";
            initialiseOutputs(UKPC_Checker.TYPE_ANA, filenamepart);
            // Process
            int n0;
            for (n0 = 0; n0 < 10; n0++) {
                for (int i = 0; i < UKPC_Checker.ABCDEFGHJKSTUW.length; i++) {
                    String a1 = String.valueOf(UKPC_Checker.ABCDEFGHJKSTUW[i]).toLowerCase();
                    String fpp = firstpartPostcode + Integer.toString(n0) + a1;
                    String aURLString0 = url + fpp;
                    doX(fpp, aURLString0, counts);
                }
//            System.out.println(getReportString(counts));
            }
        }
        finalise(counts);
    }
}
