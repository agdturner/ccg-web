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

/**
 * Web_ContentHandler
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public abstract class Web_ContentHandler {

    /**
     * Creates a new instance of WebContentHandler
     */
    public Web_ContentHandler() {
    }

    public String getDTD() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 2.0//EN\" "
                + "\"http://www.w3.org/MarkUp/DTD/xhtml2.dtd\">";
    }

    public String getXMLDeclaration() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    }

    public String getXMLStyleSheetDeclaration() {
        return "<?xml-stylesheet type=\"text/css\" "
                + "href=\"http://www.w3.org/MarkUp/style/xhtml2.css\"?>";
    }

    public String getHTMLStartTag() {
        return "<html xmlns=\"http://www.w3.org/2002/06/xhtml2/\" "
                + "xml:lang=\"en\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xsi:schemaLocation=\"http://www.w3.org/2002/06/xhtml2/ "
                + "http://www.w3.org/MarkUp/SCHEMA/xhtml2.xsd\">";
    }

    /**
     * If a is not equal to b ignoring case, then this throws an Exception
     *
     * @param a
     * @param b
     * @throws java.lang.Exception
     */
    public void aTestForEquality(String a, String b)
            throws Exception {
        if (!a.equalsIgnoreCase(b)) {
            throw new Exception("! " + a + ".equalsIgnoreCase( " + b + " )");
        }
    }

    /**
     * If a is not equal b, then this throws an Exception
     *
     * @param a
     * @param b
     * @throws java.lang.Exception
     */
    public void aTestForEquality(byte[] a, byte[] b) throws Exception {
        if (a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
                    throw new Exception("" + a[i] + " != " + b[i]);
                }
            }
        } else {
            throw new Exception("" + a.length + " != " + b.length);
        }
    }
}
