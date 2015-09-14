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

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Class for formatting postcodes of the an_naa format.
 */
public class Run_an_naa extends AbstractRun {

    /**
     * A reference to tZooplaHousepriceScraper.getAtoZ_not_QVX() for
     * convenience.
     */
    private TreeSet<String> _AtoZ_not_QVX;

    /**
     * @param tZooplaHousepriceScraper The ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Run_an_naa(
            ZooplaHousepriceScraper tZooplaHousepriceScraper,
            boolean restart) {
        init(tZooplaHousepriceScraper, restart);
        this._AtoZ_not_QVX = tZooplaHousepriceScraper.getAtoZ_not_QVX();
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
                                    if (tZooplaHousepriceScraper.isReturningOutcode(completeFirstPartPostcode, aURLString0)) {
                                        _NAA_Iterator = _NAA.iterator();
                                        while (_NAA_Iterator.hasNext()) {
                                            _NAAString = (String) _NAA_Iterator.next();
                                            if (!secondPartPostcodeRestarter) {
                                                if (_NAAString.equalsIgnoreCase(postcodeForRestart[1])) {
                                                    secondPartPostcodeRestarter = true;
                                                }
                                            } else {
                                                String aURLString = aURLString0 + "-" + _NAAString;
                                                _int0 = tZooplaHousepriceScraper.writeHouseprices(
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
                                        ZooplaHousepriceScraper.updateLog(
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
                if (tZooplaHousepriceScraper.isReturningOutcode(completeFirstPartPostcode, aURLString0)) {
                    _NAA_Iterator = _NAA.iterator();
                    while (_NAA_Iterator.hasNext()) {
                        _NAAString = (String) _NAA_Iterator.next();
                        String aURLString = aURLString0 + "-" + _NAAString;
                        _int0 = tZooplaHousepriceScraper.writeHouseprices(
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
                    ZooplaHousepriceScraper.updateLog(
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
