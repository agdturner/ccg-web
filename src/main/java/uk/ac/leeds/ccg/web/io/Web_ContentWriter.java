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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import uk.ac.leeds.ccg.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.web.core.Web_Strings;

/**
 * Web_ContentWriter
 *
 * @author Andy Turner
 * @version 1.0
 */
public class Web_ContentWriter extends Web_ContentHandler {

    /**
     * For storing the line separator.
     */
    public byte[] ls;

    /**
     * For storing the head.
     */
    public ArrayList<byte[]> h;

    /**
     * For storing the body.
     */
    public ArrayList<byte[]> b;

    /**
     * Creates a new instance of RegressionPlots
     */
    public Web_ContentWriter() {
        ls = System.getProperty("line.separator").getBytes();
        b = new ArrayList<>();
    }

    /**
     * Add content.
     *
     * @param s The string to add to {@link #b}
     */
    public void add(String s) {
        b.add(s.getBytes());
    }

   /**
     * Add content.
     *
     * @param first The first String to add on a single line to {@link #b}.
     * @param others An ordered sequence of Strings to add on a single line to
     * {@link #b}.
     */
    public void add(String first, String... others) {
        String s = first;
        for (String other : others) {
            s = s + other;
        }
        add(s);
    }

    /**
     * Add content.
     *
     * @param b The byte array to add to {@link #b}
     */
    public void add(byte[] b) {
        this.b.add(b);
    }

    /**
     * Add content.
     *
     * @param first The first byte array to add on a single line to {@link #b}.
     * @param others An ordered sequence of byte arrays to add on a single line 
     * to {@link #b}.
     */
    public void add(byte[] first, byte[]... others) {
        this.b.add(Generic_Collections.concatenate(first, others));
    }

    /**
     * Writes a HTML file.
     *
     * @param dir directory to write to
     * @param filename filename
     * @param title HTML page title.
     * @param version Page version.
     * @throws IOException if thrown.
     */
    public void writeHTML(Path dir, String filename, String title)
            throws IOException {
        Path f = Paths.get(dir.toString(), filename + Web_Strings.symbol_dot
                + Web_Strings.s_html);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        try ( OutputStream os = Files.newOutputStream(f)) {
            writeHTMLDTD(os);
            writeHTMLHead(os, title);
            writeHTMLBody(os);
            writeHTMLBodyFooter(os);
            os.flush();
        }
    }

    /**
     * Write the Document Type Descriptor (DTD).
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeHTMLDTD(OutputStream os) throws IOException {
        os.write(DTD);
        os.write(ls);
    }

    /**
     * Write the HTML Head.
     *
     * @param os The Output Stream to write to.
     * @param title The title.
     * @throws IOException if thrown.
     */
    public void writeHTMLHead(OutputStream os, String title) throws IOException {
        writel(os, HTMLST);
        writel(os, HEADST);
        writel(os, "<title>" + title + "</title>");
        writel(os, HEADET);
    }

    /**
     * Write the HTML Body {@link #b}.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeHTMLBody(OutputStream os) throws IOException {
        writel(os, BODYET);
        Iterator<byte[]> i = b.iterator();
        while (i.hasNext()) {
            writel(os, i.next());
        }
    }

    /**
     * Write the HTML Body Footer.
     *
     * @param os The Output Stream to write to.
     * @param version version
     * @throws IOException if thrown.
     */
    public void writeHTMLBodyFooter(OutputStream os) throws IOException {
        //write(os, "<div>");
        writeDIVST(os);
        write(os, "<p>Last modified on " + LocalDate.now().toString() + ".</p>");
        String cc0 = "https://creativecommons.org/share-your-work/public-domain/cc0/";
        write(os, "<p>" + getLink(cc0, "CCO Licence") + "</p>");
        write(os, "</div>");
        writeDIVET(os);
        writel(os, BODYET);
        writel(os, HTMLET);
    }

    /**
     * Write the BODY Start Tag.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeHTMLBODYST(OutputStream os) throws IOException {
        os.write(BODYST);
        os.write(ls);
    }

    /**
     * Write a DIV Start Tag. This is always on a line on its own.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeDIVST(OutputStream os) throws IOException {
        os.write(DIVST);
        os.write(ls);
    }

    /**
     * Write a DIV End Tag. This is always on a line on its own.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeDIVET(OutputStream os) throws IOException {
        os.write(DIVET);
        os.write(ls);
    }

    /**
     * Write a P Start Tag.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writePST(OutputStream os) throws IOException {
        os.write(PST);
    }

    /**
     * Write a P End Tag followed by a new line.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writePET(OutputStream os) throws IOException {
        os.write(PET);
        os.write(ls);
    }

    /**
     * Write a H1 Start Tag.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH1ST(OutputStream os) throws IOException {
        os.write(H1ST);
    }

    /**
     * Write a H1 End Tag followed by a new line.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH1ET(OutputStream os) throws IOException {
        os.write(H1ET);
        os.write(ls);
    }

    /**
     * Write a H2 Start Tag.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH2ST(OutputStream os) throws IOException {
        os.write(H2ST);
    }

    /**
     * Write a H2 End Tag followed by a new line.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH2ET(OutputStream os) throws IOException {
        os.write(H2ET);
        os.write(ls);
    }

    /**
     * Write a H3 Start Tag.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH3ST(OutputStream os) throws IOException {
        os.write(H3ST);
    }

    /**
     * Write a H3 End Tag followed by a new line.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH3ET(OutputStream os) throws IOException {
        os.write(H3ET);
        os.write(ls);
    }

    /**
     * Write a H4 Start Tag.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH4ST(OutputStream os) throws IOException {
        os.write(H4ST);
    }

    /**
     * Write a H4 End Tag followed by a new line.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH4ET(OutputStream os) throws IOException {
        os.write(H4ET);
        os.write(ls);
    }

    /**
     * Write a H5 Start Tag.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH5ST(OutputStream os) throws IOException {
        os.write(H5ST);
    }

    /**
     * Write a H5 End Tag followed by a new line.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeH5ET(OutputStream os) throws IOException {
        os.write(H5ET);
        os.write(ls);
    }

    /**
     * Write string followed by a new line.
     *
     * @param os The Output Stream to write to.
     * @param s The String to write
     * @throws IOException if thrown.
     */
    public void write(OutputStream os, String s) throws IOException {
        os.write(s.getBytes());
    }

    /**
     * Write string.
     *
     * @param os The Output Stream to write to.
     * @param s The String to write.
     * @throws IOException if thrown.
     */
    public void writel(OutputStream os, String s) throws IOException {
        os.write(s.getBytes());
        os.write(ls);
    }

    /**
     * Write string.
     *
     * @param os The Output Stream to write to.
     * @param b The byte array to write.
     * @throws IOException if thrown.
     */
    public void writel(OutputStream os, byte[] b) throws IOException {
        os.write(b);
        os.write(ls);
    }
    
    /**
     * Generate and return an HTML link.
     * 
     * @param url The URL.
     * @param name The name for the link.
     * @return A link e.g. {@code "<a href="https://example.com">example</a>"}
     */
    public String getLink(Path url, String name) {
        return "<a href=\"" + url.toString() + "\">" + name + "</a>";
    }
    
    /**
     * Generate and return an HTML link.
     * 
     * @param url The URL.
     * @param name The name for the link.
     * @return A link e.g. {@code "<a href="https://example.com">example</a>"}
     */
    public String getLink(String url, String name) {
        return "<a href=\"" + url + "\">" + name + "</a>";
    }
}
