package uk.ac.leeds.ccg.andyt.web;

import java.io.File;

public abstract class Web_ContentHandler {
    
    /** Creates a new instance of WebContentHandler */
    public Web_ContentHandler() {
    }
    
    public String getDTD() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 2.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml2.dtd\">";
    }
    
    public String getXMLDeclaration() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }
    
    public String getXMLStyleSheetDeclaration() {
        return "<?xml-stylesheet type=\"text/css\" href=\"http://www.w3.org/MarkUp/style/xhtml2.css\"?>";
    }
    
    public String getHTMLStartTag() {
        return "<html xmlns=\"http://www.w3.org/2002/06/xhtml2/\" xml:lang=\"en\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/2002/06/xhtml2/ http://www.w3.org/MarkUp/SCHEMA/xhtml2.xsd\">";
    }
    
    /**
     * If aString is not equal to bString ignoring case, then this throws an Exception
     * @param aString
     * @param bString
     * @throws java.lang.Exception
     */
    public void aTestForEquality(
            String aString,
            String bString )
            throws Exception {
        if ( ! aString.equalsIgnoreCase( bString ) ) {
            throw new Exception( "! " + aString + ".equalsIgnoreCase( " + bString + " )" );
        }
    }
    
    /**
     * If a is not equal b, then this throws an Exception
     * @param a
     * @param b
     * @throws java.lang.Exception
     */
    public void aTestForEquality(
            byte[] a,
            byte[] b )
            throws Exception {
        if ( a.length == b.length ) {
            for ( int i = 0; i < a.length; i ++ )
                if ( a[ i ] != b[ i ] ) {
                    throw new Exception( "" + a[ i ] + " != " + b[ i ] );
                }
        } else {
            throw new Exception( "" + a.length + " != " + b.length );
        }
    }
}
