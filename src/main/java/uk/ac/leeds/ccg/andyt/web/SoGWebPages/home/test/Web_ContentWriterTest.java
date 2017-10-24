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
import uk.ac.leeds.ccg.andyt.web.SoGWebPages.Web_ContentWriter;

/**
 *
 * @author geoagdt
 */
public class Web_ContentWriterTest extends Web_ContentWriter {
    
    /** Creates a new instance of Test */
    public Web_ContentWriterTest() {}
    
    public static void main( String[] args ) throws IOException {
        Web_ContentWriterTest aTest = new Web_ContentWriterTest();
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
   
    @Override
    public void writeHTMLBodyMain(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream fos )
            throws IOException {
        writeHTMLBodyMain1 ( lineSeparator, baseURL, filenamePrefix, filenameSuffix, fos );
    }
    
    public void writeHTMLBodyMain1(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream fos )
            throws IOException {
        fos.write( lineSeparator );
        fos.write( "<div>".getBytes() );
        fos.write( lineSeparator );
        fos.write( "<ul>".getBytes() );
        fos.write( lineSeparator );
        fos.write( ("<li><h2>" + filenamePrefix + "</h2>").getBytes() );
        fos.write( lineSeparator );
        fos.write( "<ul>".getBytes() );
        fos.write( lineSeparator );
        fos.write( "<li></li>".getBytes() );
        fos.write( lineSeparator );
        fos.write( "</ul></li>".getBytes() );
        fos.write( lineSeparator );
        fos.write( "</ul></li>".getBytes() );
        fos.write( lineSeparator );
        fos.write( "</ul>".getBytes() );
        fos.write( lineSeparator );
        fos.write( "</div>".getBytes() );
        fos.write( lineSeparator );
    }
    
}
