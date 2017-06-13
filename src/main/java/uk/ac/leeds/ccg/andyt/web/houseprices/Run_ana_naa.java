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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;

/**
 * Class for formatting postcodes of the ana_naa format.
 */
public class Run_ana_naa extends AbstractRun {

    /**
     * A reference to tZooplaHousepriceScraper.getABCDEFGHJKSTUW() for
     * convenience.
     */
    private TreeSet<String> _ABCDEFGHJKSTUW;

    /**
     * @param tZooplaHousepriceScraper The ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Run_ana_naa(
            ZooplaHousepriceScraper tZooplaHousepriceScraper,
            boolean restart) {
        init(tZooplaHousepriceScraper, restart);
        this._ABCDEFGHJKSTUW = Generic_UKPostcode_Handler.get_ABCDEFGHJKSTUW();
    }

    @Override
    public void run() {
        if (restart == false) {
            formatNew();
        } else {
            if (firstpartPostcode.length() > 1) {
                throw new NotImplementedException();
            }
            // Initialise output files
            String filenamepart = "_Houseprices_" + firstpartPostcode + "na";
            String[] postcodeForRestart = getPostcodeForRestart(
                    getType(), filenamepart);
            if (postcodeForRestart == null) {
                formatNew();
            } else {
                if (postcodeForRestart[0] != null) {
                    int n0Restart = Integer.valueOf(postcodeForRestart[0].substring(1, 2));
                    String a1Restart = postcodeForRestart[0].substring(2, 3);
                    // Initialise output files
                    initialiseOutputs(getType(), filenamepart);
                    // Process
                    Iterator<String> _ABCDEFGHJKSTUW_Iterator;
                    Iterator<String> _NAA_Iterator;
                    int n0;
                    int _int0;
                    String _NAAString;
                    int counter = 0;
                    int numberOfHousepriceRecords = 0;
                    int numberOfPostcodesWithHousepriceRecords = 0;
                    String a1;
                    boolean n0Restarter = false;
                    boolean a1Restarter = false;
                    boolean secondPartPostcodeRestarter = false;
                    for (n0 = 0; n0 < 10; n0++) {
                        if (!n0Restarter) {
                            if (n0 == n0Restart) {
                                n0Restarter = true;
                            }
                        } else {
                            _ABCDEFGHJKSTUW_Iterator = _ABCDEFGHJKSTUW.iterator();
                            while (_ABCDEFGHJKSTUW_Iterator.hasNext()) {
                                a1 = (String) _ABCDEFGHJKSTUW_Iterator.next();
                                if (!a1Restarter) {
                                    if (a1.equalsIgnoreCase(a1Restart)) {
                                        a1Restarter = true;
                                    }
                                } else {
                                    String completeFirstPartPostcode = firstpartPostcode
                                            + Integer.toString(n0) + a1;
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
                                                        firstpartPostcode + Integer.toString(n0) + a1,
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
                    finalise(counter, numberOfHousepriceRecords, numberOfPostcodesWithHousepriceRecords);
                }
            }
        }
    }

    private void formatNew() {
        int counter = 0;
        int numberOfHousepriceRecords = 0;
        int numberOfPostcodesWithHousepriceRecords = 0;
        String _NAAString;
        int _int0;
        if (firstpartPostcode.length() != 1) {
            throw new NotImplementedException();
        } else {
            // Initialise output files
            String filenamepart = "_Houseprices_" + firstpartPostcode + "na";
            initialiseOutputs("ana", filenamepart);
            // Process
            Iterator<String> _ABCDEFGHJKSTUW_Iterator;
            Iterator<String> _NAA_Iterator;
            int n0;
            String a1;
            for (n0 = 0; n0 < 10; n0++) {
                _ABCDEFGHJKSTUW_Iterator = _ABCDEFGHJKSTUW.iterator();
                while (_ABCDEFGHJKSTUW_Iterator.hasNext()) {
                    a1 = (String) _ABCDEFGHJKSTUW_Iterator.next();
                    String completeFirstPartPostcode = firstpartPostcode
                            + Integer.toString(n0) + a1;
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
                                    firstpartPostcode + Integer.toString(n0) + a1,
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
        }
        finalise(counter, numberOfHousepriceRecords, numberOfPostcodesWithHousepriceRecords);
    }
}
