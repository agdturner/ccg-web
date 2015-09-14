package uk.ac.leeds.ccg.andyt.web.SoGWebPages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import uk.ac.leeds.ccg.andyt.web.WebContentHandler;

public abstract class WebContentWriter extends WebContentHandler {

    /**
     * Creates a new instance of RegressionPlots
     */
    public WebContentWriter() {
    }

    public void writeHTML(
            String baseURL,
            String dir,
            String filenamePrefix,
            String filenameSuffix)
            throws IOException {
        String pageTitle = "";
        String version = "";
        writeHTML(
             baseURL,
             dir,
             filenamePrefix,
             filenameSuffix,
             pageTitle,
             version);
    }

    public void writeHTML(
            String baseURL,
            String dir,
            String filenamePrefix,
            String filenameSuffix,
            String pageTitle,
            String version)
            throws IOException {
        Calendar calendar = Calendar.getInstance();
        File f = new File(
                dir,
                filenamePrefix + filenameSuffix + ".xhtml2.0.html");
        FileOutputStream fos = new FileOutputStream(f);
        byte[] lineSeparator = System.getProperty("line.separator").getBytes();
        writeHTMLDTD(
                lineSeparator,
                fos);
        writeHTMLHead(lineSeparator, pageTitle, fos, pageTitle, calendar);
        writeHTMLBody(lineSeparator, baseURL, filenamePrefix, filenameSuffix, 
                fos, pageTitle, version, calendar);
        fos.flush();
        fos.close();
    }

    public void writeHTMLBodyFooter(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            FileOutputStream a_FileOutputStream,
            String _Version,
            Calendar a_Calendar)
            throws IOException {
        a_FileOutputStream.write("<div>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!-- Begin Footer -->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<ul>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<li><a name=\"Validation_and_Metadata\"></a><h2>Validation and Metadata</h2>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<ul>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<li>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!-- Begin Validation -->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!-- For validating the RDF linked from the header-->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<a href=\"http://www.w3.org/RDF/Validator/ARPServlet?URI=" + baseURL + filenamePrefix + ".rdf.xml\">").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/rdf_w3c_button.gif\" alt=\"[Validate RDF]\" title=\"W3C RDF Validation\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</a>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!-- For validating this page. -->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!--<a href=\"http://validator.w3.org/check/referer\">".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/valid-xhtml2.png\" alt=\"[Validate XHTML 2.0]\" title=\"W3C XHTML 2.0 Validation\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</a>-->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!-- For validating the CSS linked from the header. -->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<a href=\"http://jigsaw.w3.org/css-validator/check/referer\">".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<img src=\"http://www.geog.leeds.ac.uk/people/a.turner/images/vcss.gif\" alt=\"[Validate CSS]\" title=\"W3C CSS Validation\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</a>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</li>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!-- End Validation -->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        String _Date = a_Calendar.get(Calendar.YEAR) + "-" + (a_Calendar.get(Calendar.MONTH) + 1) + "-" + a_Calendar.get(Calendar.DAY_OF_MONTH);
        a_FileOutputStream.write(("<li>Version " + _Version + " of this page published on " + _Date + ".</li>").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<li>Page hosted on the <a href=\"http://www.geog.leeds.ac.uk/\" title=\"School of Geography Home Page @ University of Leeds\">School of Geography</a> webserver at the <a href=\"http://www.leeds.ac.uk/\" title=\"University of Leeds Home Page\">University of Leeds</a>.</li>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<li>Copyright: Andy Turner, University of Leeds</li>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</ul></li>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<!-- End Footer -->".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</ul></div>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</body>".getBytes());
    }

    public abstract void writeHTMLBodyMain(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream a_FileOutputStream)
            throws IOException;

    public void writeHTMLBodyStart(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream a_FileOutputStream,
            String _PageTitle)
            throws IOException {
        a_FileOutputStream.write("<body>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<div>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<ul>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<li><h1><a href=\"" + baseURL + filenamePrefix + filenameSuffix + ".xhtml2.0.html\" title=\"" + _PageTitle + " @ School of Geography, University of Leeds\">" + _PageTitle + "</a></h1></li>").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<li><h3><a href=\"http://www.geog.leeds.ac.uk/people/a.turner/\" title=\"Andy Turner's Home Page @ School of Geography, University of Leeds\"><img src=\"http://www.geog.leeds.ac.uk/people/a.turner/a.turner.png\" alt=\"[An image of Andy Turner]\" /></a></h3></li>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</ul></div>".getBytes());
        a_FileOutputStream.write(lineSeparator);
    }

    public void writeHTMLBodyStart(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream a_FileOutputStream)
            throws IOException {
        a_FileOutputStream.write("<body>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<div>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<ul>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<li><h1><a href=\"" + baseURL + filenamePrefix + filenameSuffix + ".xhtml2.0.html\" title=\"Andy Turner's " + filenamePrefix + filenameSuffix + " Web Page @ School of Geography, University of Leeds\">" + filenamePrefix + filenameSuffix + " Page</a></h1></li>").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<li><h3><a href=\"http://www.geog.leeds.ac.uk/people/a.turner/\" title=\"Andy Turner's Home Page @ School of Geography, University of Leeds\"><img src=\"http://www.geog.leeds.ac.uk/people/a.turner/a.turner.png\" alt=\"[An image of Andy Turner]\" /></a></h3></li>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</ul></div>".getBytes());
        a_FileOutputStream.write(lineSeparator);
    }

    public void writeHTMLBody(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream a_FileOutputStream,
            String _PageTitle,
            String _Version,
            Calendar a_Calendar)
            throws IOException {
        writeHTMLBodyStart(lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileOutputStream, _PageTitle);
        writeHTMLBodyMain(lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileOutputStream);
        writeHTMLBodyFooter(lineSeparator, baseURL, filenamePrefix, a_FileOutputStream, _Version, a_Calendar);
    }

    public void writeHTMLBody(
            byte[] lineSeparator,
            String baseURL,
            String filenamePrefix,
            String filenameSuffix,
            FileOutputStream a_FileOutputStream,
            Calendar a_Calendar)
            throws IOException {
        writeHTMLBodyStart(lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileOutputStream);
        writeHTMLBodyMain(lineSeparator, baseURL, filenamePrefix, filenameSuffix, a_FileOutputStream);
        writeHTMLBodyFooter(lineSeparator, baseURL, filenamePrefix, a_FileOutputStream, "1.0.0", a_Calendar);
    }

    public void writeHTMLHead(
            byte[] lineSeparator,
            String filenamePrefix,
            FileOutputStream a_FileOutputStream,
            Calendar a_Calendar)
            throws IOException {
        a_FileOutputStream.write("<head>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<link rel=\"schema.DC\" href=\"http://purl.org/dc/elements/1.1/\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.language\" content=\"en\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.format\" content=\"text/html\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.publisher\" content=\"School of Geography, University of Leeds\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.rights\" content=\"http://www.leeds.ac.uk/copyright.html\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.title\" content=\"Andy Turner's " + filenamePrefix + " Web Page @ School of Geography, University of Leeds\" />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.description\" content=\"Andy Turner's " + filenamePrefix + " Web Page @ School of Geography, University of Leeds\" />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.date\" content=\"" + a_Calendar.get(Calendar.YEAR) + "-" + (a_Calendar.get(Calendar.MONTH) + 1) + "-" + a_Calendar.get(Calendar.DAY_OF_MONTH) + "\" />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.contributor\" content=\"Andy Turner\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.subject\" content=" + filenamePrefix + " />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.creator\" content=\"Andy Turner\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<title>Andy Turner's " + filenamePrefix + " Web Page @ School of Geography, University of Leeds</title>").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"Keywords\" content=\"Andy,Turner,Geography\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"description\" content=\"Web Page\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"author\" content=\"Andy Turner\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<link rel=\"stylesheet\" href=\"http://www.geog.leeds.ac.uk/people/a.turner/style/SOGStyle1CSS2.1.css\" type=\"text/css\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</head>".getBytes());
        a_FileOutputStream.write(lineSeparator);
    }

    public void writeHTMLHead(
            byte[] lineSeparator,
            String filenamePrefix,
            FileOutputStream a_FileOutputStream,
            String _PageTitle,
            Calendar a_Calendar)
            throws IOException {
        a_FileOutputStream.write("<head>".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<link rel=\"schema.DC\" href=\"http://purl.org/dc/elements/1.1/\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.language\" content=\"en\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.format\" content=\"text/html\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.publisher\" content=\"School of Geography, University of Leeds\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.rights\" content=\"http://www.leeds.ac.uk/copyright.html\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.title\" content=\"" + _PageTitle + " @ School of Geography, University of Leeds\" />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.description\" content=\"" + _PageTitle + " @ School of Geography, University of Leeds\" />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.date\" content=\"" + a_Calendar.get(Calendar.YEAR) + "-" + (a_Calendar.get(Calendar.MONTH) + 1) + "-" + a_Calendar.get(Calendar.DAY_OF_MONTH) + "\" />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.contributor\" content=\"Andy Turner\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<meta name=\"DC.subject\" content=" + filenamePrefix + " />").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"DC.creator\" content=\"Andy Turner\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(("<title>" + _PageTitle + " @ School of Geography, University of Leeds</title>").getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"Keywords\" content=\"Andy,Turner,Geography\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"description\" content=\"Web Page\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<meta name=\"author\" content=\"Andy Turner\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("<link rel=\"stylesheet\" href=\"http://www.geog.leeds.ac.uk/people/a.turner/style/SOGStyle1CSS2.1.css\" type=\"text/css\" />".getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write("</head>".getBytes());
        a_FileOutputStream.write(lineSeparator);
    }

    public void writeHTMLDTD(
            byte[] lineSeparator,
            FileOutputStream a_FileOutputStream)
            throws IOException {
        a_FileOutputStream.write(getXMLDeclaration().getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(getXMLStyleSheetDeclaration().getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(getDTD().getBytes());
        a_FileOutputStream.write(lineSeparator);
        a_FileOutputStream.write(getHTMLStartTag().getBytes());
        a_FileOutputStream.write(lineSeparator);
    }

}
