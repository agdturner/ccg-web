/*
 * Test.java
 *
 * Created on 13 March 2007, 18:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.leeds.ccg.andyt.web.SoGWebPages.home.test;

import java.io.FileInputStream;
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.web.SoGWebPages.WebContentReader;
import uk.ac.leeds.ccg.andyt.web.SoGWebPages.WebContentWriter;

public class TestWebContentReader extends WebContentReader {
    
    /** Creates a new instance of TestWebContentReader */
    public TestWebContentReader() {}
    
    public static void main( String[] args ) throws IOException {
        TestWebContentReader aTestWebContentReader = new TestWebContentReader();
        aTestWebContentReader.run();
    }
    
    public void run() throws IOException {
        String baseURL = "http://www.geog.leeds.ac.uk/people/a.turner/test/";
        //String directory = "Z:/a.turner/test/";
        String directory = "C:/Work/src/andyt/java/web/src/uk/ac/leeds/ccg/andyt/web/SoGWebPages/home/test/";
        String filenamePrefix = "test";
        String filenameSuffix = "";
        try {
            readHTML( baseURL, directory, filenamePrefix, filenameSuffix );
        } catch ( Exception _Exception ) {
            _Exception.printStackTrace();
        }
    }
   
    public void readHTMLBodyMain(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileInputStream a_FileInputStream )
            throws IOException {
        readHTMLBodyMain1( lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileInputStream );
    }
    
    public void readHTMLBodyMain1(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileInputStream a_FileInputStream )
            throws IOException {
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<div>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<ul>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<li><h2>" + filenamePrefix + "</h2>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<ul>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<li></li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</ul></li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</ul></li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</ul>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</div>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
    }
    
}
