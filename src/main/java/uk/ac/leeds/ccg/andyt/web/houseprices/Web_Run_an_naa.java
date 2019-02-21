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

import java.util.Iterator;
import java.util.TreeSet;
import uk.ac.leeds.ccg.andyt.data.postcode.Data_UKPostcodeHandler;

/**
 * Class for formatting postcodes of the an_naa format.
 */
public class Web_Run_an_naa extends Web_AbstractRun {

    /**
     * A reference to ZooplaHousepriceScraper.getAtoZ_not_QVX() for
 convenience.
     */
    private TreeSet<String> _AtoZ_not_QVX;

    /**
     * @param tZooplaHousepriceScraper The Web_ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Web_Run_an_naa(
            Web_ZooplaHousepriceScraper tZooplaHousepriceScraper,
            boolean restart) {
        init(tZooplaHousepriceScraper, restart);
        this._AtoZ_not_QVX = Data_UKPostcodeHandler.get_AtoZ_not_QVX();
    }

    @Override
    public void run() {
        if (restart == false) {
            formatNew();
        } else {
            // Initialise output files
            String filenamepart = "_Houseprices_an";
            String[] postcodeForRestart = getPostcodeForRestart(
                    getType(), filenamepart);
            if (postcodeForRestart == null) {
                formatNew();
            } else {
                if (postcodeForRestart[0] != null) {
                    String a0Restart = postcodeForRestart[0].substring(0, 1);
                    int n0Restart = Integer.valueOf(postcodeForRestart[0].substring(1, 2));
                    initialiseOutputs(getType(), filenamepart);
                    // Process
                    Iterator<String> _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
                    Iterator<String> _NAA_Iterator;
                    String a0;
                    int n0;
                    int _int0;
                    String _NAAString;
                    int counter = 0;
                    int numberOfHousepriceRecords = 0;
                    int numberOfPostcodesWithHousepriceRecords = 0;
                    boolean a0Restarter = false;
                    boolean n0Restarter = false;
                    boolean secondPartPostcodeRestarter = false;
                    while (_AtoZ_not_QVX_Iterator0.hasNext()) {
                        a0 = _AtoZ_not_QVX_Iterator0.next();
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
                                    String completeFirstPartPostcode = a0 + Integer.toString(n0);
                                    String aURLString0 = url + completeFirstPartPostcode;
                                    checkRequestRate();
                                    if (ZooplaHousepriceScraper.isReturningOutcode(completeFirstPartPostcode, aURLString0)) {
                                        _NAA_Iterator = _NAA.iterator();
                                        while (_NAA_Iterator.hasNext()) {
                                            _NAAString = (String) _NAA_Iterator.next();
                                            if (!secondPartPostcodeRestarter) {
                                                if (_NAAString.equalsIgnoreCase(postcodeForRestart[1])) {
                                                    secondPartPostcodeRestarter = true;
                                                }
                                            } else {
                                                String aURLString = aURLString0 + "-" + _NAAString;
                                                _int0 = ZooplaHousepriceScraper.writeHouseprices(
                                                        outPR,
                                                        logPR,
                                                        sharedLogPR,
                                                        aURLString,
                                                        a0 + Integer.toString(n0),
                                                        _NAAString,
                                addressAdditionalPropertyDetails);
                                                counter++;
                                                numberOfHousepriceRecords += _int0;
                                                if (_int0 > 0) {
                                                    numberOfPostcodesWithHousepriceRecords++;
                                                }
                                            }
                                        }
                                    } else {
                                        Web_ZooplaHousepriceScraper.updateLog(
                                                logPR,
                                                sharedLogPR,
                                                completeFirstPartPostcode);
                                    }
                                }
                            }
                        }
//                        System.out.println(getReportString(
//                                counter,
//                                numberOfHousepriceRecords,
//                                numberOfPostcodesWithHousepriceRecords));
                    }
                    // Final reporting
                    finalise(counter, numberOfHousepriceRecords, numberOfPostcodesWithHousepriceRecords);
                }
            }
        }
    }

    private void formatNew() {
        // Initialise output files
        String filenamepart = "_Houseprices_an";
        initialiseOutputs("an", filenamepart);
        // Process
        Iterator<String> _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
        Iterator<String> _NAA_Iterator;
        String a0;
        int n0;
        int _int0;
        String _NAAString;
        int counter = 0;
        int numberOfHousepriceRecords = 0;
        int numberOfPostcodesWithHousepriceRecords = 0;
        while (_AtoZ_not_QVX_Iterator0.hasNext()) {
            a0 = _AtoZ_not_QVX_Iterator0.next();
            for (n0 = 0; n0 < 10; n0++) {
                String completeFirstPartPostcode = a0 + Integer.toString(n0);
                String aURLString0 = url + completeFirstPartPostcode;
                checkRequestRate();
                if (ZooplaHousepriceScraper.isReturningOutcode(completeFirstPartPostcode, aURLString0)) {
                    _NAA_Iterator = _NAA.iterator();
                    while (_NAA_Iterator.hasNext()) {
                        _NAAString = (String) _NAA_Iterator.next();
                        String aURLString = aURLString0 + "-" + _NAAString;
                        _int0 = ZooplaHousepriceScraper.writeHouseprices(
                                outPR,
                                logPR,
                                sharedLogPR,
                                aURLString,
                                a0 + Integer.toString(n0),
                                _NAAString,
                                addressAdditionalPropertyDetails);
                        counter++;
                        numberOfHousepriceRecords += _int0;
                        if (_int0 > 0) {
                            numberOfPostcodesWithHousepriceRecords++;
                        }
                    }
                } else {
                    Web_ZooplaHousepriceScraper.updateLog(
                            logPR,
                            sharedLogPR,
                            completeFirstPartPostcode);
                }
            }
//            System.out.println(getReportString(
//                    counter,
//                    numberOfHousepriceRecords,
//                    numberOfPostcodesWithHousepriceRecords));
        }
        // Final reporting
        finalise(counter, numberOfHousepriceRecords, numberOfPostcodesWithHousepriceRecords);
    }
}
