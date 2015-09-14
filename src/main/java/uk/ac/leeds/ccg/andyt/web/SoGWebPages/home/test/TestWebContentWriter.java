/*
 * Test.java
 *
 * Created on 13 March 2007, 18:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.leeds.ccg.andyt.web.SoGWebPages.home.test;

import java.io.FileOutputStream;
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.web.SoGWebPages.WebContentWriter;

/**
 *
 * @author geoagdt
 */
public class TestWebContentWriter extends WebContentWriter {
    
    /** Creates a new instance of Test */
    public TestWebContentWriter() {}
    
    public static void main( String[] args ) throws IOException {
        TestWebContentWriter aTest = new TestWebContentWriter();
        aTest.run();
    }
    
    public void run() throws IOException {
        String baseURL = "http://www.geog.leeds.ac.uk/people/a.turner/test/";
        String directory = "Z:/a.turner/test/";
        //String directory = "C:/Work/src/andyt/java/web/src/uk/ac/leeds/ccg/andyt/web/SoGWebPages/home/test/";
        String filenamePrefix = "test";
        String filenameSuffix = "";
        writeHTML( baseURL, directory, filenamePrefix, filenameSuffix );
    }
   
    public void writeHTMLBodyMain(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream a_FileOutputStream )
            throws IOException {
        writeHTMLBodyMain1 ( lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileOutputStream );
    }
    
    public void writeHTMLBodyMain1(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream a_FileOutputStream )
            throws IOException {
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<div>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( ("<li><h2>" + filenamePrefix + "</h2>").getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</div>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
    }
    
}
