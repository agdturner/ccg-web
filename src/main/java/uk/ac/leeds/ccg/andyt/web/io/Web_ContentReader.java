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
package uk.ac.leeds.ccg.andyt.web.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Calendar;

public abstract class Web_ContentReader extends Web_ContentHandler {
    
    /** Creates a new instance of WebContentReader */
    public Web_ContentReader() {
    }
    
    public void readHTML (
            String baseURL,
            String directory,
            String filenamePrefix,
            String filenameSuffix )
            throws Exception {
        FileInputStream a_FileInputStream = new FileInputStream( new File( directory, filenamePrefix + filenameSuffix + ".xhtml2.0.html" ) );
        byte[] lineSeparator = System.getProperty("line.separator").getBytes();
        readHTMLDTD( lineSeparator, a_FileInputStream );
//        readHTMLHead( lineSeparator, filenamePrefix, a_FileInputStream );
//        readHTMLBody( lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileInputStream );
//        a_FileInputStream.flush();
//        a_FileInputStream.close();
    }
    
    public void readHTMLBodyFooter(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            FileInputStream a_FileInputStream )
            throws IOException {
//        a_FileInputStream.write( new String( "<!-- Begin Footer -->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<div>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<ul>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!-- Begin Validation -->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!-- For validating the RDF linked from the header-->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<a href=\"http://www.w3.org/RDF/Validator/ARPServlet?URI=" + baseURL + filenamePrefix + ".rdf.xml\">" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/rdf_w3c_button.gif\" alt=\"[Validate RDF]\" title=\"W3C RDF Validation\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</a>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!-- For validating this page. -->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!--<a href=\"http://validator.w3.org/check/referer\">" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/valid-xhtml10.png\" alt=\"[Validate XHTML 2.0]\" title=\"W3C XHTML 2.0 Validation\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</a>-->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!-- For validating the CSS linked from the header. -->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<a href=\"http://jigsaw.w3.org/css-validator/check/referer\">" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/vcss.gif\" alt=\"[Validate CSS]\" title=\"W3C CSS Validation\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</a>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!-- End Validation -->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<li>Page hosted on the <a href=\"http://www.geog.leeds.ac.uk/\" title=\"School of Geography Home Page @ University of Leeds\">School of Geography</a> webserver at the <a href=\"http://www.leeds.ac.uk/\" title=\"University of Leeds Home Page\">University of Leeds</a>.</li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<li>Copyright: Andy Turner, University of Leeds</li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</ul>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</div>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!-- End Footer -->" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</body>" ).getBytes() );
    }
    
    public abstract void readHTMLBodyMain(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileInputStream a_FileInputStream )
            throws IOException;
        
    public void readHTMLBodyStart( 
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileInputStream a_FileInputStream )
            throws IOException {
//        a_FileInputStream.write( new String( "<body>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<div><ul>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<li><h1><a href=\"" + baseURL + filenamePrefix + filenameSuffix + ".xhtml2.0.html\" title=\"Andy Turner's " + filenamePrefix + filenameSuffix + " Web Page @ School of Geography, University of Leeds\">" + filenamePrefix + filenameSuffix + " Page</a></h1></li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<li><h3><a href=\"http://www.geog.leeds.ac.uk/people/a.turner/\" title=\"Andy Turner's Home Page @ School of Geography, University of Leeds\"><img src=\"http://www.geog.leeds.ac.uk/people/a.turner/a.turner.png\" alt=\"[An image of Andy Turner]\" /></a></h3></li>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</ul></div>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
    }
    
    public void readHTMLBody(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileInputStream a_FileInputStream )
            throws IOException {
        readHTMLBodyStart( lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileInputStream );
        readHTMLBodyMain( lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileInputStream );
        readHTMLBodyFooter( lineSeparator, baseURL, filenamePrefix, a_FileInputStream );
    }
    
    public void readHTMLHead(
            byte[] lineSeparator,
            String filenamePrefix,
            FileInputStream a_FileInputStream )
            throws IOException {
//        a_FileInputStream.write( new String( "<head>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<link rel=\"schema.DC\" href=\"http://purl.org/dc/elements/1.1/\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.language\" content=\"en\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.format\" content=\"text/html\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.publisher\" content=\"School of Geography, University of Leeds\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.rights\" content=\"http://www.leeds.ac.uk/copyright.html\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.title\" content=\"Andy Turner's xhtml2.0 MoSeS " + filenamePrefix + " Page @ School of Geography, University of Leeds\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.description\" content=\"Andy Turner's xhtml2.0 MoSeS " + filenamePrefix + " Page @ School of Geography, University of Leeds\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        Calendar a_Calendar = Calendar.getInstance();
//        a_FileInputStream.write( new String( "<meta name=\"DC.date\" content=\"" + a_Calendar.get( Calendar.YEAR ) + "-" + ( a_Calendar.get( Calendar.MONTH ) + 1 ) + "-" + a_Calendar.get( Calendar.DAY_OF_MONTH ) + "\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.contributor\" content=\"Andy Turner\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.subject\" content=" + filenamePrefix + " />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"DC.creator\" content=\"Andy Turner\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<title>Andy Turner's xhtml2.0 MoSeS " + filenamePrefix + " Page @ School of Geography, University of Leeds</title>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"Keywords\" content=\"Andy Turner,MoSeS,Demography\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"description\" content=\"Project Web Page\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<meta name=\"author\" content=\"Andy Turner\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<link rel=\"stylesheet\" href=\"http://www.geog.leeds.ac.uk/people/a.turner/style/SOGStyle1CSS2.1.css\" type=\"text/css\" />" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "</head>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
    }
    
    public void readHTMLDTD(
            byte[] lineSeparator,
            FileInputStream a_FileInputStream )
            throws Exception {
        
        byte[] lineSeparatorRead = new byte[ lineSeparator.length ];
        
        // Read XMLDeclaration
        String tXMLDeclaration = this.getXMLDeclaration();
        byte[] tXMLDeclarationByteArray = new byte[ tXMLDeclaration.length() ];
        a_FileInputStream.read( tXMLDeclarationByteArray );
        String tXMLDeclarationRead = new String( tXMLDeclarationByteArray );
        System.out.println( tXMLDeclarationRead );
        // Test for equality
        aTestForEquality( tXMLDeclarationRead, tXMLDeclaration );
        
        // Read line separator
        a_FileInputStream.read( lineSeparatorRead );
        // Test for equality
        aTestForEquality( lineSeparatorRead, lineSeparator );
        
        // Read 
        String tXMLStyleSheetDeclaration = this.getXMLStyleSheetDeclaration();
        byte[] tXMLStyleSheetDeclarationByteArray = new byte[ tXMLStyleSheetDeclaration.length() ];
        a_FileInputStream.read( tXMLStyleSheetDeclarationByteArray );
        String tXMLStyleSheetDeclarationRead = new String( tXMLStyleSheetDeclarationByteArray );
        System.out.println( tXMLStyleSheetDeclarationRead );
        // Test for equality
        aTestForEquality( tXMLStyleSheetDeclarationRead, tXMLStyleSheetDeclaration );
        
//            a_FileInputStream.read( new String( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<?xml-stylesheet type=\"text/css\" href=\"http://www.w3.org/MarkUp/style/xhtml2.css\"?>" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 2.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml2.dtd\">" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( "<html xmlns=\"http://www.w3.org/2002/06/xhtml2/\" xml:lang=\"en\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
//        a_FileInputStream.write( new String( " xsi:schemaLocation=\"http://www.w3.org/2002/06/xhtml2/ http://www.w3.org/MarkUp/SCHEMA/xhtml2.xsd\">" ).getBytes() );
//        a_FileInputStream.write( lineSeparator );
    }
}
