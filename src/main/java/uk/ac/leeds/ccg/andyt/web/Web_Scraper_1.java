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
package uk.ac.leeds.ccg.andyt.web;

/*
 * Scaper.java
 *
 * Created on 26 February 2007, 15:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import uk.ac.leeds.ccg.andyt.web.houseprices.Web_ZooplaHousepriceScraper;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Web_Scraper_1 {
    
    /** Creates a new instance of Scaper */
    public Web_Scraper_1() {
    }
    
    /** Main method
     * @param args
     * @throws java.lang.Exception */
    public static void main( String[] args ) throws Exception {
        Web_ZooplaHousepriceScraper aScraper_1 = new Web_ZooplaHousepriceScraper();
        aScraper_1.run( args );
    }
    
    public void run( String[] args ) throws Exception {
        //getHTML( "http://www.houseprices.co.uk/e.php?q=LS7+2EU", "LS7", "2EU" );
        //getHousepriceDataForGary();
        if ( args.length == 0 ) {
            args = new String[2];
            args[0] = "C:/Work/data/HousePrices/UK/Houseprices.csv";
            //
            //args[0] = "C:/Work/data/HousePrices/UK/MyPropertySpy.csv";
            //args[1] = "Hull_postcodes.csv";
            args[1] = "C:/Work/data/HousePrices/UK/postcodes.csv";
        }
        //http://www.mypropertyspy.co.uk/house-prices/ls7+2eu/1
        getHousepriceDataForUK( args );
    }
    
    
    /**
     * Format 	Example Postcode
     *  AN NAA      M1 1AA
     *  ANN NAA     M60 1NW
     *  AAN NAA     CR2 6XH
     *  AANN NAA    DN55 1PT
     *  ANA NAA     W1A 1HQ
     *  AANA NAA    EC1A 1BB
     * The letters Q, V and X are not used in the first position.
     * The letters I, J and Z are not used in the second position.
     * The only letters to appear in the third position are A, B, C, D, E, F, G, H, J, K, S, T, U and W.
     * The only letters to appear in the fourth position are A, B, E, H, M, N, P, R, V, W, X and Y.
     * The second half of the Postcode is always consistent numeric, alpha, alpha format and the letters C, I, K, M, O and V are never used.
     * These conventions may change in the future if operationally required.
     * GIR 0AA is a Postcode that was issued historically and does not confirm to current rules on valid Postcode formats, It is however, still in use.
     * The Postcode is a combination of between five and seven letters / numbers which define four different levels of geographic unit. It is part of a coding system created and used by the Royal Mail across the United Kingdom for the sorting of mail. The Postcodes are an abbreviated form of address which enable a group of delivery points (a delivery point being a property or a post box) to be specifically identified. There are two types of Postcode, these being large and small user Postcodes.
     * A large user Postcode is one that has been assigned to a single address due to the large volume of mail received at that address.
     * A small user Postcode identifies a group of delivery points. On average there are 15 delivery points perPostcode, however this can vary between 1 and 100.
     * Each Postcode consists of two parts. The first part is the Outward Postcode, or Outcode. This is separated by a single space from the second part which is the Inward Postcode, or Incode.The Outward Postcode enables mail to be sent to the correct local area for delivery. This part of the code contains the area and the district to which the mail is to be delivered.The Inward Postcode is used to sort the mail at the local area delivery office. It consists of a numeric character followed by two alphabetic characters. The numeric character identifies the sector within the postal district. The alphabetic characters then define one or more properties within the sector.
     * The following characters are never used in the inward part of the Postcode:C I K M O V.
     * An example Postcode is PO1 3AX. PO refers to the Postcode Area of Portsmouth. There are 124 Postcode Areas in the UK. PO1 refers to a Postcode District within the Postcode Area of Portsmouth. There are approximately 2900 Postcode Districts. PO1 3 refers to the Postcode Sector. There are approximately 9,650 Postcode Sectors. The AX completes the Postcode. The last two letters define the 'Unit Postcode' which identifies one or more small user delivery points or an individual Large User. There are approximately 1.71 million Unit Postcodes in the UK.
     * (http://www.govtalk.gov.uk/gdsc/html/frames/PostCode.htm)
     * PostCode Type as HTML:
     * <xsd:simpleType name="PostCodeType">
     * <xsd:annotation>
     * <xsd:documentation>complex pattern for postcode, which matches definition, accepted by some parsers is: "(GIR 0AA)|((([A-Z-[QVX]][0-9][0-9]?)|(([A-Z-[QVX]][A-Z-[IJZ]][0-9][0-9]?)|(([A-Z-[QVX]][0-9][A-HJKSTUW])|([A-Z-[QVX]][A-Z-[IJZ]][0-9][ABEHMNPRVWXY])))) [0-9][A-Z-[CIKMOV]]{2})"</xsd:documentation>
     * </xsd:annotation>
     * <xsd:restriction base="xsd:string">
     * <xsd:pattern value="[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][A-Z-[CIKMOV]]{2}"/>
     * </xsd:restriction>
     * </xsd:simpleType>
     * (http://www.govtalk.gov.uk/gdsc/schemaHtml/bs7666-v2-0-xsd-PostCodeType.htm)
     * (see also XML schema document BS7666 http://www.govtalk.gov.uk/gdsc/schemas/bs7666-v2-0.xsd)
     * @param args
     * @throws java.lang.Exception
     */
    public void getHousepriceDataForUK( String[] args ) throws Exception {
        //Calendar _Calendar = Calendar.getInstance();
        
        
        
        
        
        long _TimeInMillis = Calendar.getInstance().getTimeInMillis();
        
        /*
        // NP8 1SW
        String a0 = "np8";
        String a1 = "1sw";
        String aURLString = new String( "http://www.mypropertyspy.co.uk/house-prices/np8+1sw/" );
        File outFile = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_HousepriceTest.csv" );
        outFile.createNewFile();
        PrintWriter outPrintWriter = new PrintWriter( outFile );
        File logFile = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices_AN.log" );
        logFile.createNewFile();
        PrintWriter logPrintWriter = new PrintWriter( logFile );
        int _int0 = writeHouseprices( outPrintWriter, logPrintWriter, aURLString, a0, a1 );
       outPrintWriter.close();
        logPrintWriter.close();
    
*/
        
        // Set up output files and writers
        // AN_NAA
        File outFile_AN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices_AN.csv" );
        outFile_AN.createNewFile();
        PrintWriter outPrintWriter_AN = new PrintWriter( outFile_AN );
        File logFile_AN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices_AN.log" );
        logFile_AN.createNewFile();
        PrintWriter logPrintWriter_AN = new PrintWriter( logFile_AN );
        // ANN_NAA
        File outFile_ANN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices_ANN.csv" );
        outFile_ANN.createNewFile();
        PrintWriter outPrintWriter_ANN = new PrintWriter( outFile_ANN );
        File logFile_ANN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices_ANN.log" );
        logFile_ANN.createNewFile();
        PrintWriter logPrintWriter_ANN = new PrintWriter( logFile_ANN );
        // AAN_NAA
        File outFile_AAN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__AAN.csv" );
        outFile_AAN.createNewFile();
        PrintWriter outPrintWriter_AAN = new PrintWriter( outFile_AAN );
        File logFile_AAN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__AAN.log" );
        logFile_AAN.createNewFile();
        PrintWriter logPrintWriter_AAN = new PrintWriter( logFile_AAN );
        // AANN_NAA
        File outFile_AANN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__AANN.csv" );
        outFile_AANN.createNewFile();
        PrintWriter outPrintWriter_AANN = new PrintWriter( outFile_AANN );
        File logFile_AANN = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__AANN.log" );
        logFile_AANN.createNewFile();
        PrintWriter logPrintWriter_AANN = new PrintWriter( logFile_AANN );
        // ANA_NAA
        File outFile_ANA = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__ANA.csv" );
        outFile_ANA.createNewFile();
        PrintWriter outPrintWriter_ANA = new PrintWriter( outFile_ANA );
        File logFile_ANA = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__ANA.log" );
        logFile_ANA.createNewFile();
        PrintWriter logPrintWriter_ANA = new PrintWriter( logFile_ANA );
        // AANA_NAA
        File outFile_AANA = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__AANA.csv" );
        outFile_AANA.createNewFile();
        PrintWriter outPrintWriter_AANA = new PrintWriter( outFile_AANA );
        File logFile_AANA = new File( "C:/Work/data/HousePrices/UK_Postcodes_" + _TimeInMillis + "_Houseprices__AANA.log" );
        logFile_AANA.createNewFile();
        PrintWriter logPrintWriter_AANA = new PrintWriter( logFile_AANA );
        
        HashSet tPostcodes = new HashSet();
        // Special cases
        tPostcodes.add( "GIR 0AA" );
        HashSet _AtoZ_not_QVX = getUpperCaseStringAlphabetHashSet();
        _AtoZ_not_QVX.remove( "Q" );
        _AtoZ_not_QVX.remove( "V" );
        _AtoZ_not_QVX.remove( "X" );
        HashSet _AtoZ_not_IJZ = getUpperCaseStringAlphabetHashSet();
        _AtoZ_not_IJZ.remove( "I" );
        _AtoZ_not_IJZ.remove( "J" );
        _AtoZ_not_IJZ.remove( "Z" );
        HashSet _ABCDEFGHJKSTUW = new HashSet();
        _ABCDEFGHJKSTUW.add( "A" );
        _ABCDEFGHJKSTUW.add( "B" );
        _ABCDEFGHJKSTUW.add( "C" );
        _ABCDEFGHJKSTUW.add( "D" );
        _ABCDEFGHJKSTUW.add( "E" );
        _ABCDEFGHJKSTUW.add( "F" );
        _ABCDEFGHJKSTUW.add( "G" );
        _ABCDEFGHJKSTUW.add( "H" );
        _ABCDEFGHJKSTUW.add( "J" );
        _ABCDEFGHJKSTUW.add( "K" );
        _ABCDEFGHJKSTUW.add( "S" );
        _ABCDEFGHJKSTUW.add( "T" );
        _ABCDEFGHJKSTUW.add( "U" );
        _ABCDEFGHJKSTUW.add( "W" );
        HashSet _ABEHMNPRVWXY = new HashSet();
        _ABEHMNPRVWXY.add( "A" );
        _ABEHMNPRVWXY.add( "B" );
        _ABEHMNPRVWXY.add( "E" );
        _ABEHMNPRVWXY.add( "H" );
        _ABEHMNPRVWXY.add( "M" );
        _ABEHMNPRVWXY.add( "N" );
        _ABEHMNPRVWXY.add( "P" );
        _ABEHMNPRVWXY.add( "R" );
        _ABEHMNPRVWXY.add( "V" );
        _ABEHMNPRVWXY.add( "W" );
        _ABEHMNPRVWXY.add( "X" );
        _ABEHMNPRVWXY.add( "Y" );
        HashSet _AtoZ_not_CIKMOV = getUpperCaseStringAlphabetHashSet();
        _AtoZ_not_CIKMOV.remove( "C" );
        _AtoZ_not_CIKMOV.remove( "I" );
        _AtoZ_not_CIKMOV.remove( "K" );
        _AtoZ_not_CIKMOV.remove( "M" );
        _AtoZ_not_CIKMOV.remove( "O" );
        _AtoZ_not_CIKMOV.remove( "V" );
        Iterator _AtoZ_not_CIKMOV_Iterator1 = _AtoZ_not_CIKMOV.iterator();
        Iterator _AtoZ_not_CIKMOV_Iterator2 = _AtoZ_not_CIKMOV.iterator();
        HashSet _NAA = new HashSet();
        String a0;
        String a1;
        int _int0 = 0;
        int _NumberOfHousepriceRecords = 0;
        int _NumberOfPostcodesWithHousepriceRecords = 0;
        int _TotalNumberOfHousepriceRecords = 0;
        int _TotalNumberOfPostcodesWithHousepriceRecords = 0;
        int n0;
        while( _AtoZ_not_CIKMOV_Iterator1.hasNext() ) {
            a0 = ( String ) _AtoZ_not_CIKMOV_Iterator1.next();
            while( _AtoZ_not_CIKMOV_Iterator2.hasNext() ) {
                a1 = ( String ) _AtoZ_not_CIKMOV_Iterator2.next();
                for ( n0 = 0; n0 < 9; n0 ++ ) {
                    _NAA.add( Integer.toString( n0 ) + a0 + a1 );
                }
            }
        }
        String aURLString;
        int n1;
        Iterator _NAA_Iterator;
        String _NAAString;
        Iterator _AtoZ_not_QVX_Iterator0;
        Iterator _AtoZ_not_IJZ_Iterator0;
        // Format 	Example Postcode
        //  AN NAA      M1 1AA
        logPrintWriter_AN.println( "Format AN NAA" );
        System.out.println( "Format AN NAA" );
        _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
        while( _AtoZ_not_QVX_Iterator0.hasNext() ) {
            a0 = ( String ) _AtoZ_not_QVX_Iterator0.next();
            for ( n0 = 0; n0 < 9; n0 ++ ) {
                _NAA_Iterator = _NAA.iterator();
                while ( _NAA_Iterator.hasNext() ) {
                    _NAAString = ( String ) _NAA_Iterator.next();
                    aURLString = "http://www.houseprices.co.uk/e.php?q=" + a0 + Integer.toString( n0 ) + "+" + _NAAString;

                    aURLString = "http://www.houseprices.co.uk/e.php?q=LS7+2EU";
                    writeHouseprices(
                            outPrintWriter_AN,
                            logPrintWriter_AN,
                            aURLString,
                            "LS7",
                            "2EU" );

                    //May have multiple pages of results. Results are given 10 at a time
                    //aURLString = new String( "http://www.mypropertyspy.co.uk/house-prices/" + a0 + Integer.toString( n0 ) + "+" + _NAAString + "/");
                    // Get number of pages of results
                    
//                    writeHouseprices(
//                            outPrintWriter_AN,
//                            logPrintWriter_AN,
//                            aURLString,
//                            a0 + Integer.toString( n0 ),
//                            _NAAString );
                    _NumberOfHousepriceRecords += _int0;
                    if ( _int0 > 0 ) {
                        _NumberOfPostcodesWithHousepriceRecords ++;
                    }
                }
            }
        }
        _TotalNumberOfHousepriceRecords += _NumberOfHousepriceRecords;
        _TotalNumberOfPostcodesWithHousepriceRecords += _NumberOfPostcodesWithHousepriceRecords;
        logPrintWriter_AN.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        System.out.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        logPrintWriter_AN.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        System.out.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        _NumberOfHousepriceRecords = 0;
        _NumberOfPostcodesWithHousepriceRecords = 0;
        outPrintWriter_AN.close();
        logPrintWriter_AN.close();
        // Format 	Example Postcode
        //  ANN NAA     M60 1NW
        logPrintWriter_ANN.println( "Format ANN NAA" );
        System.out.println( "Format ANN NAA" );
        _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
        while( _AtoZ_not_QVX_Iterator0.hasNext() ) {
            a0 = ( String ) _AtoZ_not_QVX_Iterator0.next();
            for ( n0 = 0; n0 < 9; n0 ++ ) {
                for ( n1 = 0; n1 < 9; n1 ++ ) {
                    _NAA_Iterator = _NAA.iterator();
                    while ( _NAA_Iterator.hasNext() ) {
                        _NAAString = ( String ) _NAA_Iterator.next();
                        aURLString = "http://www.houseprices.co.uk/e.php?q=" + a0 + Integer.toString( n0 ) + Integer.toString( n1 ) + "+" + _NAAString;
                        _int0 = writeHouseprices( outPrintWriter_ANN, logPrintWriter_ANN, aURLString, a0 + Integer.toString( n0 ) + Integer.toString( n1 ), _NAAString );
                        _NumberOfHousepriceRecords += _int0;
                        if ( _int0 > 0 ) {
                            _NumberOfPostcodesWithHousepriceRecords ++;
                        }
                    }
                }
            }
        }
        _TotalNumberOfHousepriceRecords += _NumberOfHousepriceRecords;
        _TotalNumberOfPostcodesWithHousepriceRecords += _NumberOfPostcodesWithHousepriceRecords;
        logPrintWriter_ANN.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        System.out.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        logPrintWriter_ANN.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        System.out.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        _NumberOfHousepriceRecords = 0;
        _NumberOfPostcodesWithHousepriceRecords = 0;
        outPrintWriter_ANN.close();
        logPrintWriter_ANN.close();
        // Format 	Example Postcode
        //  AAN NAA     CR2 6XH
        logPrintWriter_AAN.println( "Format AAN NAA" );
        System.out.println( "Format AAN NAA" );
        
        _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
        while( _AtoZ_not_QVX_Iterator0.hasNext() ) {
            a0 = ( String ) _AtoZ_not_QVX_Iterator0.next();
            _AtoZ_not_IJZ_Iterator0 = _AtoZ_not_IJZ.iterator();
            while( _AtoZ_not_IJZ_Iterator0.hasNext() ) {
                a1 = ( String ) _AtoZ_not_IJZ_Iterator0.next();
                for ( n0 = 0; n0 < 9; n0 ++ ) {
                    _NAA_Iterator = _NAA.iterator();
                    while ( _NAA_Iterator.hasNext() ) {
                        _NAAString = ( String ) _NAA_Iterator.next();
                        aURLString = "http://www.houseprices.co.uk/e.php?q=" + a0 + a1 + Integer.toString( n0 ) + "+" + _NAAString;
                        _int0 = writeHouseprices( outPrintWriter_AAN, logPrintWriter_AAN, aURLString, a0 + a1 + Integer.toString( n0 ), _NAAString );
                        _NumberOfHousepriceRecords += _int0;
                        if ( _int0 > 0 ) {
                            _NumberOfPostcodesWithHousepriceRecords ++;
                        }
                    }
                }
            }
        }
        _TotalNumberOfHousepriceRecords += _NumberOfHousepriceRecords;
        _TotalNumberOfPostcodesWithHousepriceRecords += _NumberOfPostcodesWithHousepriceRecords;
        logPrintWriter_AAN.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        System.out.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        logPrintWriter_AAN.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        System.out.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        _NumberOfHousepriceRecords = 0;
        _NumberOfPostcodesWithHousepriceRecords = 0;
        outPrintWriter_AAN.close();
        logPrintWriter_AAN.close();
        // Format 	Example Postcode
        //  AANN NAA    DN55 1PT
        logPrintWriter_AANN.println( "Format AANN NAA" );
        System.out.println( "Format AANN NAA" );
        _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
        while( _AtoZ_not_QVX_Iterator0.hasNext() ) {
            a0 = ( String ) _AtoZ_not_QVX_Iterator0.next();
            _AtoZ_not_IJZ_Iterator0 = _AtoZ_not_IJZ.iterator();
            while( _AtoZ_not_IJZ_Iterator0.hasNext() ) {
                a1 = ( String ) _AtoZ_not_IJZ_Iterator0.next();
                for ( n0 = 0; n0 < 9; n0 ++ ) {
                    for ( n1 = 0; n1 < 9; n1 ++ ) {
                        _NAA_Iterator = _NAA.iterator();
                        while ( _NAA_Iterator.hasNext() ) {
                            _NAAString = ( String ) _NAA_Iterator.next();
                            aURLString = "http://www.houseprices.co.uk/e.php?q=" + a0 + a1 + Integer.toString( n0 ) + Integer.toString( n1 ) + "+" + _NAAString;
                            _int0 = writeHouseprices( outPrintWriter_AANN, logPrintWriter_AANN, aURLString, a0 + a1 + Integer.toString( n0 ) + Integer.toString( n1 ), _NAAString );
                            _NumberOfHousepriceRecords += _int0;
                            if ( _int0 > 0 ) {
                                _NumberOfPostcodesWithHousepriceRecords ++;
                            }
                        }
                    }
                }
            }
        }
        _TotalNumberOfHousepriceRecords += _NumberOfHousepriceRecords;
        _TotalNumberOfPostcodesWithHousepriceRecords += _NumberOfPostcodesWithHousepriceRecords;
        logPrintWriter_AANN.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        System.out.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        logPrintWriter_AANN.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        System.out.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        _NumberOfHousepriceRecords = 0;
        _NumberOfPostcodesWithHousepriceRecords = 0;
        outPrintWriter_AANN.close();
        logPrintWriter_AANN.close();
        // Format 	Example Postcode
        //  ANA NAA     W1A 1HQ
        logPrintWriter_ANA.println( "Format ANA NAA" );
        System.out.println( "Format ANA NAA" );
        Iterator _ABCDEFGHJKSTUW_Iterator;
        _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
        while( _AtoZ_not_QVX_Iterator0.hasNext() ) {
            a0 = ( String ) _AtoZ_not_QVX_Iterator0.next();
            _ABCDEFGHJKSTUW_Iterator = _ABCDEFGHJKSTUW.iterator();
            while( _ABCDEFGHJKSTUW_Iterator.hasNext() ) {
                a1 = ( String ) _ABCDEFGHJKSTUW_Iterator.next();
                for ( n0 = 0; n0 < 9; n0 ++ ) {
                    _NAA_Iterator = _NAA.iterator();
                    while ( _NAA_Iterator.hasNext() ) {
                        _NAAString = ( String ) _NAA_Iterator.next();
                        aURLString = "http://www.houseprices.co.uk/e.php?q=" + a0 + Integer.toString( n0 ) + a1 + "+" + _NAAString;
                        _int0 = writeHouseprices( outPrintWriter_ANA, logPrintWriter_ANA, aURLString, a0 + Integer.toString( n0 ) + a1, _NAAString );
                        _NumberOfHousepriceRecords += _int0;
                        if ( _int0 > 0 ) {
                            _NumberOfPostcodesWithHousepriceRecords ++;
                        }
                    }
                }
            }
        }
        _TotalNumberOfHousepriceRecords += _NumberOfHousepriceRecords;
        _TotalNumberOfPostcodesWithHousepriceRecords += _NumberOfPostcodesWithHousepriceRecords;
        logPrintWriter_ANA.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        System.out.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        logPrintWriter_ANA.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        System.out.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        _NumberOfHousepriceRecords = 0;
        _NumberOfPostcodesWithHousepriceRecords = 0;
        outPrintWriter_ANA.close();
        logPrintWriter_ANA.close();
        // Format 	Example Postcode
        //  AANA NAA    EC1A 1BB
        logPrintWriter_AANA.println( "Format AANA NAA" );
        System.out.println( "Format AANA NAA" );
        Iterator _ABEHMNPRVWXY_Iterator;
        _AtoZ_not_QVX_Iterator0 = _AtoZ_not_QVX.iterator();
        String a3;
        while( _AtoZ_not_QVX_Iterator0.hasNext() ) {
            a0 = ( String ) _AtoZ_not_QVX_Iterator0.next();
            _AtoZ_not_IJZ_Iterator0 = _AtoZ_not_IJZ.iterator();
            while( _AtoZ_not_IJZ_Iterator0.hasNext() ) {
                a1 = ( String ) _AtoZ_not_IJZ_Iterator0.next();
                for ( n0 = 0; n0 < 9; n0 ++ ) {
                    _ABEHMNPRVWXY_Iterator = _ABEHMNPRVWXY.iterator();
                    while( _ABEHMNPRVWXY_Iterator.hasNext() ) {
                        a3 = ( String ) _ABEHMNPRVWXY_Iterator.next();
                        _NAA_Iterator = _NAA.iterator();
                        while ( _NAA_Iterator.hasNext() ) {
                            _NAAString = ( String ) _NAA_Iterator.next();
                            aURLString = "http://www.houseprices.co.uk/e.php?q=" + a0 + a1 + Integer.toString( n0 ) + a3 + "+" + _NAAString;
                            _int0 = writeHouseprices( outPrintWriter_AANA, logPrintWriter_AANA, aURLString, a0 + a1 + Integer.toString( n0 ) + a3, _NAAString );
                            _NumberOfHousepriceRecords += _int0;
                            if ( _int0 > 0 ) {
                                _NumberOfPostcodesWithHousepriceRecords ++;
                            }
                        }
                    }
                }
            }
        }
        _TotalNumberOfHousepriceRecords += _NumberOfHousepriceRecords;
        _TotalNumberOfPostcodesWithHousepriceRecords += _NumberOfPostcodesWithHousepriceRecords;
        logPrintWriter_AANA.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        System.out.println( "_NumberOfHousepriceRecords " + _NumberOfHousepriceRecords );
        logPrintWriter_AANA.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        System.out.println( "_TotalNumberOfHousepriceRecords " + _TotalNumberOfHousepriceRecords );
        outPrintWriter_AANA.close();
        logPrintWriter_AANA.close();
    }
    
    public void writeHouseprices( 
            PrintWriter _PrintWriter,
            String _URLString,
            String _String1,
            String _String2 ) {
        HashSet tHouseprices = getHTML( _URLString, _String1, _String2 );
        Iterator aIterator = tHouseprices.iterator();
        while ( aIterator.hasNext() ) {
            _PrintWriter.write( ( String ) aIterator.next() );
            _PrintWriter.println();
        }
        _PrintWriter.flush();
        //System.out.println( "Done " + _URLString );
    }
    
    /**
     * @param _String2
     * @param _String1
     * @param _URLString
     * @return number of recrods
     */
    public int writeHouseprices(
            PrintWriter _PrintWriter,
            PrintWriter _LogPrintWriter,
            String _URLString,
            String _String1,
            String _String2 ) {
        HashSet tHouseprices = getHTML( 
                _URLString,
                _String1,
                _String2 );
        if ( tHouseprices.isEmpty() ) {
            _LogPrintWriter.println( _String1 + " " + _String2 + " " + " 0 Records" );
            _LogPrintWriter.flush();
            return 0;
        } else {
            Iterator aIterator = tHouseprices.iterator();
            while ( aIterator.hasNext() ) {
                _PrintWriter.write( ( String ) aIterator.next() );
                _PrintWriter.println();
            }
            _PrintWriter.flush();
            _LogPrintWriter.println( _String1 + " " + _String2 + " " + tHouseprices.size() + " Records" );
            return tHouseprices.size();
        }
    }
    
    /**
     * Rene wants data for "LS* postcodes
     * LS postcodes are assumed to be "LS* @##" where; * is a number [1,29], @ is a number [1,9], and # are letters [A,Z].
     * Only a fraction of all possible postcodes are actually used at present.
     * @param args
     * @throws java.lang.Exception
     */
    public void getHousepriceDataForRene( String[] args ) throws Exception {
        Calendar _Calendar = Calendar.getInstance();
        File outFile = new File( "C:/Work/People/Rene Jordan/LS_Postcodes_" + _Calendar.getTimeInMillis() + "_Houseprices.csv" );
        outFile.createNewFile();
        //File outFile = new File( args[0] );
        //FileOutputStream aFileOutputStream = new FileOutputStream( outFile );
        PrintWriter aPrintWriter = new PrintWriter( outFile );
        HashSet tPostcodes = new HashSet();
        HashMap alphabet = new HashMap();
        alphabet.put(0,"A");
        alphabet.put(1,"B");
        alphabet.put(2,"C");
        alphabet.put(3,"D");
        alphabet.put(4,"E");
        alphabet.put(5,"F");
        alphabet.put(6,"G");
        alphabet.put(7,"H");
        alphabet.put(8,"I");
        alphabet.put(9,"J");
        alphabet.put(10,"K");
        alphabet.put(11,"L");
        alphabet.put(12,"M");
        alphabet.put(13,"N");
        alphabet.put(14,"O");
        alphabet.put(15,"P");
        alphabet.put(16,"Q");
        alphabet.put(17,"R");
        alphabet.put(18,"S");
        alphabet.put(19,"T");
        alphabet.put(20,"U");
        alphabet.put(21,"V");
        alphabet.put(22,"W");
        alphabet.put(23,"X");
        alphabet.put(24,"Y");
        alphabet.put(25,"Z");
        for ( int i = 0; i < 25; i ++ ) {
            for ( int j = 0; j < 25; j ++ ) {
                for ( int k = 1; k < 29; k ++ ) {
                    for ( int l = 1; l < 9; l ++ ) {
                        tPostcodes.add( "LS" + k + " " + l + ( String ) alphabet.get( i ) + ( String ) alphabet.get( j ) );
                    }
                }
            }
        }
        Iterator tPostcodesIterator = tPostcodes.iterator();
        while ( tPostcodesIterator.hasNext() ) {
            String aPostcode = ( String ) tPostcodesIterator.next();
            String[] splitPostcode = aPostcode.split( " " );
            String aURLString = "http://www.houseprices.co.uk/e.php?q=" + splitPostcode[0] + "+" + splitPostcode[1];
            HashSet tHouseprices = getHTML( aURLString, splitPostcode[0], splitPostcode[1] );
            Iterator aIterator = tHouseprices.iterator();
            while ( aIterator.hasNext() ) {
                aPrintWriter.write( ( String ) aIterator.next() );
                aPrintWriter.println();
            }
            aPrintWriter.flush();
            System.out.println( "Done " + aURLString );
        }
    }
    
    /**
     * Stuart wants data for "LS7 1" and "LS2 9"
     * @param args
     * @throws java.lang.Exception
     */
    public void getHousepriceDataForStuart( String[] args ) throws Exception {
        File outFile = new File( args[0] );
        //FileOutputStream aFileOutputStream = new FileOutputStream( outFile );
        PrintWriter aPrintWriter = new PrintWriter( outFile );
        HashSet tPostcodes = new HashSet();
        HashMap alphabet = new HashMap();
        alphabet.put(0,"A");
        alphabet.put(1,"B");
        alphabet.put(2,"C");
        alphabet.put(3,"D");
        alphabet.put(4,"E");
        alphabet.put(5,"F");
        alphabet.put(6,"G");
        alphabet.put(7,"H");
        alphabet.put(8,"I");
        alphabet.put(9,"J");
        alphabet.put(10,"K");
        alphabet.put(11,"L");
        alphabet.put(12,"M");
        alphabet.put(13,"N");
        alphabet.put(14,"O");
        alphabet.put(15,"P");
        alphabet.put(16,"Q");
        alphabet.put(17,"R");
        alphabet.put(18,"S");
        alphabet.put(19,"T");
        alphabet.put(20,"U");
        alphabet.put(21,"V");
        alphabet.put(22,"W");
        alphabet.put(23,"X");
        alphabet.put(24,"Y");
        alphabet.put(25,"Z");
        for ( int i = 0; i < 25; i ++ ) {
            for ( int j = 0; j < 25; j ++ ) {
                tPostcodes.add( "LS7 1" + ( String ) alphabet.get( i ) + ( String ) alphabet.get( j ) );
                tPostcodes.add( "LS2 9" + ( String ) alphabet.get( i ) + ( String ) alphabet.get( j ) );
            }
        }
        Iterator tPostcodesIterator = tPostcodes.iterator();
        while ( tPostcodesIterator.hasNext() ) {
            String aPostcode = ( String ) tPostcodesIterator.next();
            String[] splitPostcode = aPostcode.split( " " );
            String aURLString = "http://www.houseprices.co.uk/e.php?q=" + splitPostcode[0] + "+" + splitPostcode[1];
            HashSet tHouseprices = getHTML( aURLString, splitPostcode[0], splitPostcode[1] );
            Iterator aIterator = tHouseprices.iterator();
            while ( aIterator.hasNext() ) {
                aPrintWriter.write( ( String ) aIterator.next() );
                aPrintWriter.println();
            }
            aPrintWriter.flush();
            System.out.println( "Done " + aURLString );
        }
    }
    
    public void getHousepriceDataForGary( String[] args ) throws Exception {
//        File aFile = new File( "C:/Work/organisations/UoL/SoG/Students/Gary Wainman/Hull_postcodes.csv" );
        File aFile = new File( args[1] );
        FileInputStream aFileInputStream = new FileInputStream( aFile );
//        File outFile = new File( "C:/Work/organisations/UoL/SoG/Students/Gary Wainman/Hull_houseprices.csv" );
        File outFile = new File( args[0] );
        //FileOutputStream aFileOutputStream = new FileOutputStream( outFile );
        PrintWriter aPrintWriter = new PrintWriter( outFile );
        StreamTokenizer aStreamTokenizer = new StreamTokenizer( aFileInputStream );
        aStreamTokenizer.eolIsSignificant( true );
        aStreamTokenizer.wordChars( ' ', ' ' );
        aStreamTokenizer.wordChars( ',', ',' );
        //Skip header first line
        aStreamTokenizer.nextToken();
        while ( aStreamTokenizer.nextToken() != StreamTokenizer.TT_EOF ) {
            if ( aStreamTokenizer.ttype != StreamTokenizer.TT_EOL ) {
                String line = aStreamTokenizer.sval;
                String tFirstPartOfPostcode = line.substring( 0, 4 );
                if ( tFirstPartOfPostcode.endsWith( " " ) ) {
                    tFirstPartOfPostcode = line.substring( 0, 3 );
                }
                String tSecondPartOfPostcode = line.substring( 4, 7 );
                //System.out.println( "" + line );
                //System.out.println(  );
                String aURLString = "http://www.houseprices.co.uk/e.php?q=" + tFirstPartOfPostcode + "+" + tSecondPartOfPostcode;
                HashSet tHouseprices = getHTML( aURLString, tFirstPartOfPostcode, tSecondPartOfPostcode );
                Iterator aIterator = tHouseprices.iterator();
                while ( aIterator.hasNext() ) {
                    aPrintWriter.write( ( String ) aIterator.next() );
                    aPrintWriter.println();
                }
                aPrintWriter.flush();
                System.out.println( "Done " + aURLString );
            }
        }
        //System.out.println( "" + result );
    }
    
    public HashSet getHTML(
            String aURLString,
            String tFirstPartOfPostcode,
            String tSecondPartOfPostcode ) {
        HashSet result = new HashSet();
        URL aURL;
        HttpURLConnection aHttpURLConnection;
        BufferedReader aBufferedReader;
        String line;
        String tFormattedOutput;
        String s_AONs;
        String s_Street;
        String s_Locality;
        String s_PropertyType;
        String s_Duration;
        String s_DatePurchased;
        String s_Price;
        String[] items;
        int page = 0;
        int count = 0;
        boolean complete = false;
        try {
            while ( ! complete ) {
                //page ++;
                //aURLString += new String( "" + page );
                aURL = new URL( aURLString );
                aHttpURLConnection = ( HttpURLConnection ) aURL.openConnection();
                aHttpURLConnection.setRequestMethod( "GET" );
                aBufferedReader = new BufferedReader(
                        new InputStreamReader( aHttpURLConnection.getInputStream() ) );
                while ( ( line = aBufferedReader.readLine() ) != null ) {
                    if ( line.contains(tFirstPartOfPostcode + " " + tSecondPartOfPostcode)){
                        
                    }
//                    //if ( line.contains( "Postcode" ) ) {
//                        count ++;
//                        // This is the postcode so goto the next
//                        // Get s_HouseNumberOrName
//                        while ( ( line = aBufferedReader.readLine() ) != null ) {
//                            if ( line.contains( "AONs" ) ) {
//                                items = line.split( ">" );
//                                s_AONs = items[1].split("<")[0];
//
//                                // Get s_Street
//                                while ( ( line = aBufferedReader.readLine() ) != null ) {
//                                    if ( line.contains( "Street" ) ) {
//                                        items = line.split( ">" );
//                                        s_Street = items[1].split("<")[0];
//
//                                        // Get s_Locality
//                                        while ( ( line = aBufferedReader.readLine() ) != null ) {
//                                            if ( line.contains( "Locality" ) ) {
//                                                items = line.split( ">" );
//                                                s_Locality = items[1].split("<")[0];
//
//                                                // Get s_PropertyType
//                                                while ( ( line = aBufferedReader.readLine() ) != null ) {
//                                                    if ( line.contains( "Propertytype" ) ) {
//                                                        items = line.split( ">" );
//                                                        s_PropertyType = items[1].split("<")[0];
//
//                                                        // Get s_Duration
//                                                        while ( ( line = aBufferedReader.readLine() ) != null ) {
//                                                            if ( line.contains( "Duration" ) ) {
//                                                                items = line.split( ">" );
//                                                                s_Duration = items[1].split("<")[0];
//
//                                                                // Get s_DatePurchased
//                                                                while ( ( line = aBufferedReader.readLine() ) != null ) {
//                                                                    if ( line.contains( "DatePurchased" ) ) {
//                                                                        items = line.split( ">" );
//                                                                        s_DatePurchased = items[1].split("<")[0];
//
//                                                                        // Get s_Price
//                                                                        while ( ( line = aBufferedReader.readLine() ) != null ) {
//                                                                            if ( line.contains( "Price" ) ) {
//                                                                                items = line.split( ">" );
//                                                                                s_Price = items[1].split("<")[0];
//                                                                                tFormattedOutput =
//                                                                                        tFirstPartOfPostcode + " "
//                                                                                        + tSecondPartOfPostcode + ","
//                                                                                        + s_AONs + ","
//                                                                                        + s_Street + ","
//                                                                                        + s_Locality + ","
//                                                                                        + s_PropertyType + ","
//                                                                                        + s_Duration + ","
//                                                                                        + s_DatePurchased + ","
//                                                                                        + s_Price;
//                                                                                result.add( tFormattedOutput );
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
                if ( count <= 9 ) {
                    complete = true;
                }
            aBufferedReader.close();
            }
        } catch ( Exception aException ) {
            aException.printStackTrace();
        }
        return result;
    }
    
    public HashSet getUpperCaseStringAlphabetHashSet() {
        HashSet result = new HashSet();
        result.add( "A" );
        result.add( "B" );
        result.add( "C" );
        result.add( "D" );
        result.add( "E" );
        result.add( "F" );
        result.add( "G" );
        result.add( "H" );
        result.add( "I" );
        result.add( "J" );
        result.add( "K" );
        result.add( "L" );
        result.add( "M" );
        result.add( "N" );
        result.add( "O" );
        result.add( "P" );
        result.add( "Q" );
        result.add( "R" );
        result.add( "S" );
        result.add( "T" );
        result.add( "U" );
        result.add( "V" );
        result.add( "W" );
        result.add( "X" );
        result.add( "Y" );
        result.add( "Z" );
        return result;
    }
}
