package uk.ac.leeds.ccg.andyt.web.houseprices;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author geoagdt Adapted from:
 * http://www.mkyong.com/java/how-to-read-xml-file-in-java-sax-parser/
 */
public class Web_XMLSAXReader {

    public static void main(String args[]) {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler();
            saxParser.parse("/scratch02/zoopla/outcodes.xml", handler);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }
    
    DefaultHandler handler = new DefaultHandler() {
        boolean bfname = false;
        boolean blname = false;
        boolean bnname = false;
        boolean bsalary = false;

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {

            System.out.println("Start Element :" + qName);

            if (qName.equalsIgnoreCase("FIRSTNAME")) {
                bfname = true;
            }

            if (qName.equalsIgnoreCase("LASTNAME")) {
                blname = true;
            }

            if (qName.equalsIgnoreCase("NICKNAME")) {
                bnname = true;
            }

            if (qName.equalsIgnoreCase("SALARY")) {
                bsalary = true;
            }

        }

        @Override
        public void endElement(String uri, String localName,
                String qName) throws SAXException {

            System.out.println("End Element :" + qName);

        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {

            if (bfname) {
                System.out.println("First Name : " + new String(ch, start, length));
                bfname = false;
            }

            if (blname) {
                System.out.println("Last Name : " + new String(ch, start, length));
                blname = false;
            }

            if (bnname) {
                System.out.println("Nick Name : " + new String(ch, start, length));
                bnname = false;
            }

            if (bsalary) {
                System.out.println("Salary : " + new String(ch, start, length));
                bsalary = false;
            }

        }
    };
}
