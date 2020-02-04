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
package uk.ac.leeds.ccg.web.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public abstract class Web_ContentWriter extends Web_ContentHandler {

    /**
     * Creates a new instance of RegressionPlots
     */
    public Web_ContentWriter() {
    }

    public void writeHTML(String baseURL, String dir, String filenamePrefix,
            String filenameSuffix) throws IOException {
        String pageTitle = "";
        String version = "";
        writeHTML(baseURL, dir, filenamePrefix, filenameSuffix, pageTitle,
                version);
    }

    public void writeHTML(String baseURL, String dir, String filenamePrefix,
            String filenameSuffix, String pageTitle, String version)
            throws IOException {
        Calendar calendar = Calendar.getInstance();
        Path f = Paths.get(dir, filenamePrefix + filenameSuffix + ".xhtml2.0.html");
        try (OutputStream os = Files.newOutputStream(f)) {
            byte[] lineSeparator = System.getProperty("line.separator").getBytes();
            writeHTMLDTD(lineSeparator, os);
            writeHTMLHead(lineSeparator, pageTitle, os, pageTitle, calendar);
            writeHTMLBody(lineSeparator, baseURL, filenamePrefix, filenameSuffix,
                    os, pageTitle, version, calendar);
            os.flush();
        }
    }

    public void writeHTMLBodyFooter(byte[] lineSeparator, String baseURL,
            String filenamePrefix, OutputStream os, String version,
            Calendar calendar) throws IOException {
        os.write("<div>".getBytes());
        os.write(lineSeparator);
        os.write("<!-- Begin Footer -->".getBytes());
        os.write(lineSeparator);
        os.write("<ul>".getBytes());
        os.write(lineSeparator);
        os.write("<li><a name=\"Validation_and_Metadata\"></a><h2>Validation and Metadata</h2>".getBytes());
        os.write(lineSeparator);
        os.write("<ul>".getBytes());
        os.write(lineSeparator);
        os.write("<li>".getBytes());
        os.write(lineSeparator);
        os.write("<!-- Begin Validation -->".getBytes());
        os.write(lineSeparator);
        os.write("<!-- For validating the RDF linked from the header-->".getBytes());
        os.write(lineSeparator);
        os.write(("<a href=\"http://www.w3.org/RDF/Validator/ARPServlet?URI=" + baseURL + filenamePrefix + ".rdf.xml\">").getBytes());
        os.write(lineSeparator);
        os.write("<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/rdf_w3c_button.gif\" alt=\"[Validate RDF]\" title=\"W3C RDF Validation\" />".getBytes());
        os.write(lineSeparator);
        os.write("</a>".getBytes());
        os.write(lineSeparator);
        os.write("<!-- For validating this page. -->".getBytes());
        os.write(lineSeparator);
        os.write("<!--<a href=\"http://validator.w3.org/check/referer\">".getBytes());
        os.write(lineSeparator);
        os.write("<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/valid-xhtml2.png\" alt=\"[Validate XHTML 2.0]\" title=\"W3C XHTML 2.0 Validation\" />".getBytes());
        os.write(lineSeparator);
        os.write("</a>-->".getBytes());
        os.write(lineSeparator);
        os.write("<!-- For validating the CSS linked from the header. -->".getBytes());
        os.write(lineSeparator);
        os.write("<a href=\"http://jigsaw.w3.org/css-validator/check/referer\">".getBytes());
        os.write(lineSeparator);
        os.write("<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/vcss.gif\" alt=\"[Validate CSS]\" title=\"W3C CSS Validation\" />".getBytes());
        os.write(lineSeparator);
        os.write("</a>".getBytes());
        os.write(lineSeparator);
        os.write("</li>".getBytes());
        os.write(lineSeparator);
        os.write("<!-- End Validation -->".getBytes());
        os.write(lineSeparator);
        String _Date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        os.write(("<li>Version " + version + " of this page published on " + _Date + ".</li>").getBytes());
        os.write(lineSeparator);
        os.write("<li>Page hosted on the <a href=\"http://www.geog.leeds.ac.uk/\" title=\"School of Geography Home Page @ University of Leeds\">School of Geography</a> webserver at the <a href=\"http://www.leeds.ac.uk/\" title=\"University of Leeds Home Page\">University of Leeds</a>.</li>".getBytes());
        os.write(lineSeparator);
        os.write("<li>Copyright: Andy Turner, University of Leeds</li>".getBytes());
        os.write(lineSeparator);
        os.write("</ul></li>".getBytes());
        os.write(lineSeparator);
        os.write("<!-- End Footer -->".getBytes());
        os.write(lineSeparator);
        os.write("</ul></div>".getBytes());
        os.write(lineSeparator);
        os.write("</body>".getBytes());
    }

    public abstract void writeHTMLBodyMain(byte[] lineSeparator, String baseURL,
            String filenamePrefix, String filenameSuffix, OutputStream os)
            throws IOException;

    public void writeHTMLBodyStart(byte[] lineSeparator, String baseURL,
            String filenamePrefix, String filenameSuffix, OutputStream os,
            String title) throws IOException {
        os.write("<body>".getBytes());
        os.write(lineSeparator);
        os.write("<div>".getBytes());
        os.write(lineSeparator);
        os.write("<ul>".getBytes());
        os.write(lineSeparator);
        os.write(("<li><h1><a href=\"" + baseURL + filenamePrefix + filenameSuffix + ".xhtml2.0.html\" title=\"" + title + " @ School of Geography, University of Leeds\">" + title + "</a></h1></li>").getBytes());
        os.write(lineSeparator);
        os.write("<li><h3><a href=\"http://www.geog.leeds.ac.uk/people/a.turner/\" title=\"Andy Turner's Home Page @ School of Geography, University of Leeds\"><img src=\"http://www.geog.leeds.ac.uk/people/a.turner/a.turner.png\" alt=\"[An image of Andy Turner]\" /></a></h3></li>".getBytes());
        os.write(lineSeparator);
        os.write("</ul></div>".getBytes());
        os.write(lineSeparator);
    }

    public void writeHTMLBodyStart(byte[] lineSeparator, String baseURL,
            String filenamePrefix, String filenameSuffix, OutputStream os)
            throws IOException {
        os.write("<body>".getBytes());
        os.write(lineSeparator);
        os.write("<div>".getBytes());
        os.write(lineSeparator);
        os.write("<ul>".getBytes());
        os.write(lineSeparator);
        os.write(("<li><h1><a href=\"" + baseURL + filenamePrefix + filenameSuffix + ".xhtml2.0.html\" title=\"Andy Turner's " + filenamePrefix + filenameSuffix + " Web Page @ School of Geography, University of Leeds\">" + filenamePrefix + filenameSuffix + " Page</a></h1></li>").getBytes());
        os.write(lineSeparator);
        os.write("<li><h3><a href=\"http://www.geog.leeds.ac.uk/people/a.turner/\" title=\"Andy Turner's Home Page @ School of Geography, University of Leeds\"><img src=\"http://www.geog.leeds.ac.uk/people/a.turner/a.turner.png\" alt=\"[An image of Andy Turner]\" /></a></h3></li>".getBytes());
        os.write(lineSeparator);
        os.write("</ul></div>".getBytes());
        os.write(lineSeparator);
    }

    public void writeHTMLBody(byte[] lineSeparator, String baseURL,
            String filenamePrefix, String filenameSuffix, OutputStream os,
            String pageTitle, String version, Calendar calendar)
            throws IOException {
        writeHTMLBodyStart(lineSeparator, baseURL, filenamePrefix, filenameSuffix, os, pageTitle);
        writeHTMLBodyMain(lineSeparator, baseURL, filenamePrefix, filenameSuffix, os);
        writeHTMLBodyFooter(lineSeparator, baseURL, filenamePrefix, os, version, calendar);
    }

    public void writeHTMLBody(byte[] lineSeparator, String baseURL,
            String filenamePrefix, String filenameSuffix, OutputStream os,
            Calendar calendar) throws IOException {
        writeHTMLBodyStart(lineSeparator, baseURL, filenamePrefix, filenameSuffix, os);
        writeHTMLBodyMain(lineSeparator, baseURL, filenamePrefix, filenameSuffix, os);
        writeHTMLBodyFooter(lineSeparator, baseURL, filenamePrefix, os, "1.0.0", calendar);
    }

    public void writeHTMLHead(byte[] lineSeparator, String filenamePrefix,
            OutputStream os, Calendar calendar) throws IOException {
        os.write("<head>".getBytes());
        os.write(lineSeparator);
        os.write("<link rel=\"schema.DC\" href=\"http://purl.org/dc/elements/1.1/\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.language\" content=\"en\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.format\" content=\"text/html\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.publisher\" content=\"School of Geography, University of Leeds\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.rights\" content=\"http://www.leeds.ac.uk/copyright.html\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />".getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.title\" content=\"Andy Turner's " + filenamePrefix + " Web Page @ School of Geography, University of Leeds\" />").getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.description\" content=\"Andy Turner's " + filenamePrefix + " Web Page @ School of Geography, University of Leeds\" />").getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.date\" content=\"" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "\" />").getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.contributor\" content=\"Andy Turner\" />".getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.subject\" content=" + filenamePrefix + " />").getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.creator\" content=\"Andy Turner\" />".getBytes());
        os.write(lineSeparator);
        os.write(("<title>Andy Turner's " + filenamePrefix + " Web Page @ School of Geography, University of Leeds</title>").getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"Keywords\" content=\"Andy,Turner,Geography\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"description\" content=\"Web Page\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"author\" content=\"Andy Turner\" />".getBytes());
        os.write(lineSeparator);
        os.write("<link rel=\"stylesheet\" href=\"http://www.geog.leeds.ac.uk/people/a.turner/style/SOGStyle1CSS2.1.css\" type=\"text/css\" />".getBytes());
        os.write(lineSeparator);
        os.write("</head>".getBytes());
        os.write(lineSeparator);
    }

    public void writeHTMLHead(byte[] lineSeparator, String filenamePrefix,
            OutputStream os, String title, Calendar calendar)
            throws IOException {
        os.write("<head>".getBytes());
        os.write(lineSeparator);
        os.write("<link rel=\"schema.DC\" href=\"http://purl.org/dc/elements/1.1/\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.language\" content=\"en\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.format\" content=\"text/html\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.publisher\" content=\"School of Geography, University of Leeds\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.rights\" content=\"http://www.leeds.ac.uk/copyright.html\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />".getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.title\" content=\"" + title + " @ School of Geography, University of Leeds\" />").getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.description\" content=\"" + title + " @ School of Geography, University of Leeds\" />").getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.date\" content=\"" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "\" />").getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.contributor\" content=\"Andy Turner\" />".getBytes());
        os.write(lineSeparator);
        os.write(("<meta name=\"DC.subject\" content=" + filenamePrefix + " />").getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"DC.creator\" content=\"Andy Turner\" />".getBytes());
        os.write(lineSeparator);
        os.write(("<title>" + title + " @ School of Geography, University of Leeds</title>").getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"Keywords\" content=\"Andy,Turner,Geography\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"description\" content=\"Web Page\" />".getBytes());
        os.write(lineSeparator);
        os.write("<meta name=\"author\" content=\"Andy Turner\" />".getBytes());
        os.write(lineSeparator);
        os.write("<link rel=\"stylesheet\" href=\"http://www.geog.leeds.ac.uk/people/a.turner/style/SOGStyle1CSS2.1.css\" type=\"text/css\" />".getBytes());
        os.write(lineSeparator);
        os.write("</head>".getBytes());
        os.write(lineSeparator);
    }

    public void writeHTMLDTD(byte[] lineSeparator, OutputStream os)
            throws IOException {
        os.write(getXMLDeclaration().getBytes());
        os.write(lineSeparator);
        os.write(getXMLStyleSheetDeclaration().getBytes());
        os.write(lineSeparator);
        os.write(getDTD().getBytes());
        os.write(lineSeparator);
        os.write(getHTMLStartTag().getBytes());
        os.write(lineSeparator);
    }

}
