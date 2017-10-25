package uk.ac.leeds.ccg.andyt.web.SoGWebPages;

import java.io.FileOutputStream;
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.web.SoGWebPages.Web_ContentWriter;

public class Web_ContentWriterImplementation_1 extends Web_ContentWriter {
    
    String _ReleaseVersion;
    
    /**
     * Creates a new instance of WebContentWriterImplementation_1
     */
    public Web_ContentWriterImplementation_1() {}
    
    public static void main( String[] args ) {
        Web_ContentWriterImplementation_1 _MoSeS = new Web_ContentWriterImplementation_1();
        _MoSeS.run();
    }
    
    public void run() {
        String _BaseURL = "http://www.geog.leeds.ac.uk/people/a.turner/src/andyt/java/projects/MoSeS/";
        //String _Directory = "C:/Work/src/andyt/java/web/src/uk/ac/leeds/ccg/andyt/web/SoGWebPages/home/src/andyt/java/projects/WebContentWriterImplementation_1/";
        String _Directory = "Z:/a.turner/src/andyt/java/projects/MoSeS/";
        String _FilenamePrefix = "MoSeS";
        String _FilenameSuffix = "";
        String _PageTitle = "Andy Turner's MoSeS Source Code Web Page";
        String _Version = "1.0.1";
        _ReleaseVersion = "0.8.0";
        try {
            writeHTML(_BaseURL,_Directory,_FilenamePrefix,_FilenameSuffix,_PageTitle,_Version);
        } catch ( IOException _IOException ) {
            _IOException.printStackTrace();
        }
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
        a_FileOutputStream.write( "<li><h2>Introduction</h2>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( ("<li>Information about a library of packages for Modelling and Simulation for e-Social Science. " +
                "This began to evolve in July 2005 as <a href=\"http://www.geog.leeds.ac.uk/people/a.turner/projects/MoSeS/\">MoSeS</a> got underway. " +
                "Version 0.1 of the software was released in October 2005 as open source under the <a href=\"License.txt\" title=\"GNU LESSER GENERAL PUBLIC LICENSE Version 2.1, February 1999\">GNU LESSER GENERAL PUBLIC LICENSE Version 2.1</a>. " +
                "The software has been developed on <a href=\"http://java.sun.com/\" title=\"Java Technology Home Page\">Java</a> <a href=\"http://java.sun.com/j2se/1.5.0\" title=\"Java 2 Platform Standard Edition (J2SE) 5.0\">1.5 (J2SE 2 Platform Standard Edition 5.0)</a>.</li>").getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li>Contents".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a href=\"#License\">License</a></li>".getBytes() );
        a_FileOutputStream.write( ("<li><a href=\"#" + _ReleaseVersion + "\">Version " + _ReleaseVersion + "</a></li>").getBytes() );
        a_FileOutputStream.write( "<li><a href=\"#Acknowledgements\">Acknowledgements</a></li>".getBytes() );
        a_FileOutputStream.write( "<li><a href=\"#Validation_and_Metadata\">Validation and Metadata</a></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a name=\"License\"></a><h2>License</h2>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a href=\"License.txt\" title=\"GNU LESSER GENERAL PUBLIC LICENSE Version 2.1, February 1999\">GNU LESSER GENERAL PUBLIC LICENSE Version 2.1</a>.</li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( ("<li><a name=\"" + _ReleaseVersion + "\"></a><h2>Version " + _ReleaseVersion + "</h2>").getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a href=\"./dist/MoSeS.jar\" title=\"Java Archive File of Classes\">Java Archive File of Classes</a></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a href=\"./src/\" title=\"Root Directory of the Source Code\">Root Directory of the Source Code</a></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a href=\"./dist/javadoc/\" title=\"API Documentation\">API Documentation</a></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a href=\"./lib/\" title=\"Library of Dependent Packages\">Library of Dependencies</a></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( ("<li><a href=\"./releases/MoSeS_" + _ReleaseVersion + ".zip\" title=\"MoSeS_" + _ReleaseVersion + " Software Bundle\">MoSeS_" + _ReleaseVersion + " Software Bundle</li>").getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li><a name=\"Acknowledgements\"></a><h2>Acknowledgements</h2>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li>The <a href=\"http://www.esrc.ac.uk/\" title=\"The ESRC Home Page\">ESRC</a> has supported this work as:".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<ul>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li>RES-149-25-0034 (<a href=\"http://www.ncess.ac.uk/research/nodes/MoSeS/\" title=\"ESRC National Centre for e-Social Science - Nodes: MoSeS\">MoSeS</a>)</li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "<li>Thank you <a href=\"http://www.leeds.ac.uk/\" title=\"The University of Leeds Home Page\">University of Leeds</a> especially the <a href=\"http://www.geog.leeds.ac.uk/\" title=\"The School of Geography Home Page\">School of Geography</a> and <a href=\"http://www.ccg.leeds.ac.uk/\" title=\"The Centre for Computational Geography Home Page\">CCG</a> for your support and encouragement over the years.</li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</ul></li>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
        a_FileOutputStream.write( "</div>".getBytes() );
        a_FileOutputStream.write( lineSeparator );
    }
}
