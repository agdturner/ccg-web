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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Web_ContentReader
 * 
 * @author Andy Turner
 * @version 1.0.0
 */
public abstract class Web_ContentReader extends Web_ContentHandler {

    /**
     * Creates a new instance of WebContentReader
     */
    public Web_ContentReader() {
    }

    /**
     * 
     * @param url base URL
     * @param dir directory
     * @param prefix filename prefix
     * @param suffix filename suffix
     * @throws Exception 
     */
    public void readHTML(String url, String dir, String prefix, String suffix)
            throws Exception {
        InputStream is = Files.newInputStream(Paths.get(dir, prefix + suffix 
                + ".xhtml2.0.html"));
        byte[] lineSeparator = System.getProperty("line.separator").getBytes();
        readHTMLDTD(lineSeparator, is);
//        readHTMLHead( lineSeparator, filenamePrefix, is );
//        readHTMLBody( lineSeparator, baseURL, filenamePrefix, filenameSuffix, is );
//        fis.flush();
//        fis.close();
    }

    public void readHTMLDTD(byte[] lineSeparator, InputStream is)
            throws Exception {
        byte[] lineSeparatorRead = new byte[lineSeparator.length];
        // Read XMLDeclaration
        String xmlDeclaration = this.getXMLDeclaration();
        byte[] xmlb = new byte[xmlDeclaration.length()];
        is.read(xmlb);
        String xmls = new String(xmlb);
        System.out.println(xmls);
        // Test for equality
        aTestForEquality(xmls, xmlDeclaration);
        // Read line separator
        is.read(lineSeparatorRead);
        // Test for equality
        aTestForEquality(lineSeparatorRead, lineSeparator);
        // Read 
        String xmlSSD = getXMLStyleSheetDeclaration();
        byte[] xmlSSDb = new byte[xmlSSD.length()];
        is.read(xmlSSDb);
        String xmlSSDs = new String(xmlSSDb);
        System.out.println(xmlSSDs);
        // Test for equality
        aTestForEquality(xmlSSDs, xmlSSD);
    }
}
