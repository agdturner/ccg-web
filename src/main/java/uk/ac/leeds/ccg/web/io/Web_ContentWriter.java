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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.leeds.ccg.web.core.Web_Strings;

/**
 * Web_ContentWriter
 *
 * @author Andy Turner
 * @version 1.0
 */
public class Web_ContentWriter {

    /**
     * For storing the HTML head.
     */
    public ArrayList<byte[]> head;

    /**
     * For storing the HTML body.
     */
    public ArrayList<byte[]> body;

    /**
     * Creates a new instance.
     */
    public Web_ContentWriter() {
        body = new ArrayList<>();
    }

    /**
     * Add content.
     *
     * @param s The string to add to {@link #body}
     */
    public void add(String s) {
        body.add(s.getBytes());
    }

    /**
     * Writes a HTML file.
     *
     * @param dir directory to write to
     * @param filename filename
     * @param title HTML page title.
     * @param head Elements to add to the HTML HEAD. If {@code null}
     * it is ignored.
     * @throws IOException if thrown.
     */
    public void writeHTML(Path dir, String filename, String title,
            List<String> head) throws IOException {
        Path f = Paths.get(dir.toString(), filename + Web_Strings.symbol_dot
                + Web_Strings.HTML);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        try ( OutputStream os = Files.newOutputStream(f)) {
            writeHTMLDTD(os);
            writeHTMLHead(os, title, head);
            writeHTMLBody(os);
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
        os.write(Web_Strings.DTD);
        os.write(Web_Strings.LINE_SEPARATOR);
    }

    /**
     * Write the HTML Head.
     *
     * @param os The Output Stream to write to.
     * @param title The title.
     * @param head The page head.
     * @throws IOException if thrown.
     */
    public void writeHTMLHead(OutputStream os, String title, List<String> head)
            throws IOException {
        writel(os, Web_Strings.HTML_ST);
        writel(os, Web_Strings.HEAD_ST);
        writel(os, Web_Strings.TITLE_ST + title + Web_Strings.TITLE_ET);
        // Add scripts.
        if (head != null) {
            for (int i = 0; i < head.size(); i++) {
                writel(os, head.get(i));
            }
        }
        writel(os, Web_Strings.HEAD_ET);
    }

    /**
     * Write the HTML Body {@link #body}.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writeHTMLBody(OutputStream os) throws IOException {
        writel(os, Web_Strings.BODY_ST);
        writePageSectionContents(os);
        Iterator<byte[]> i = body.iterator();
        while (i.hasNext()) {
            writel(os, i.next());
        }
        writel(os, Web_Strings.BODY_ET);
        writel(os, Web_Strings.HTML_ET);
    }

    /*** 
     * Write the Page Section Contents.
     *
     * @param os The Output Stream to write to.
     * @throws IOException if thrown.
     */
    public void writePageSectionContents(OutputStream os) throws IOException {}
    
//    /**
//     * Write a H1 Start Tag.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH1ST(OutputStream os) throws IOException {
//        os.write(H1ST);
//    }
//
//    /**
//     * Write a H1 End Tag followed by a new line.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH1ET(OutputStream os) throws IOException {
//        os.write(H1ET);
//        os.write(LINE_SEPARATOR);
//    }
//
//    /**
//     * Write a H2 Start Tag.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH2ST(OutputStream os) throws IOException {
//        os.write(H2ST);
//    }
//
//    /**
//     * Write a H2 End Tag followed by a new line.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH2ET(OutputStream os) throws IOException {
//        os.write(H2ET);
//        os.write(LINE_SEPARATOR);
//    }
//
//    /**
//     * Write a H3 Start Tag.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH3ST(OutputStream os) throws IOException {
//        os.write(H3ST);
//    }
//
//    /**
//     * Write a H3 End Tag followed by a new line.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH3ET(OutputStream os) throws IOException {
//        os.write(H3ET);
//        os.write(LINE_SEPARATOR);
//    }
//
//    /**
//     * Write a H4 Start Tag.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH4ST(OutputStream os) throws IOException {
//        os.write(H4ST);
//    }
//
//    /**
//     * Write a H4 End Tag followed by a new line.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH4ET(OutputStream os) throws IOException {
//        os.write(H4ET);
//        os.write(LINE_SEPARATOR);
//    }
//
//    /**
//     * Write a H5 Start Tag.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH5ST(OutputStream os) throws IOException {
//        os.write(H5ST);
//    }
//
//    /**
//     * Write a H5 End Tag followed by a new line.
//     *
//     * @param os The Output Stream to write to.
//     * @throws IOException if thrown.
//     */
//    public void writeH5ET(OutputStream os) throws IOException {
//        os.write(H5ET);
//        os.write(LINE_SEPARATOR);
//    }
//
//    /**
//     * Write string followed by a new line.
//     *
//     * @param os The Output Stream to write to.
//     * @param s The String to write
//     * @throws IOException if thrown.
//     */
//    public void write(OutputStream os, String s) throws IOException {
//        os.write(s.getBytes());
//    }
//
    /**
     * Write string.
     *
     * @param os The Output Stream to write to.
     * @param s The String to write.
     * @throws IOException if thrown.
     */
    public void writel(OutputStream os, String s) throws IOException {
        os.write(s.getBytes());
        os.write(Web_Strings.LINE_SEPARATOR);
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
        os.write(Web_Strings.LINE_SEPARATOR);
    }

    /**
     * Generate and return an HTML link.
     *
     * @param url The URL as a path.
     * @param linkText The text for the link.
     * @return A link e.g. {@code "<a href="https://example.com">example</a>"}
     */
    public static String getLink(Path url, String linkText) {
        return getLink(url.toString(), linkText);
    }

    /**
     * Generate and return an HTML link.
     *
     * @param url The URL as a String e.g. "https://example.com".
     * @param linkText The text for the link e.g. "example".
     * @return A link e.g. {@code "<a href="https://example.com">example</a>"}
     */
    public static String getLink(String url, String linkText) {
        return "<a href=\"" + url + "\">" + linkText + "</a>";
    }

    /**
     * Generate and return an HTML link.
     *
     * @param url The URL as a Path.
     * @param linkID The ID for the link.
     * @param linkClass The class for the link.
     * @param linkText The text for the link e.g. "example".
     * @return A link e.g.
     * {@code "<a id="id" href="https://example.com">example</a>"}
     */
    public static String getLink(String url, String linkID, String linkClass,
            String linkText) {
        return "<a id=\"" + linkID + "\" class=\"" + linkClass + "\" href=\""
                + url + "\">" + linkText + "</a>";
    }

    /**
     * Generate and return an HTML link disguised as a button. This is
     * deprecated as it is not good practice to do this for accessibility
     * reasons. Links should be links using <a href=""></a> which are activated
     * differently by users. The expectation of what a button does is different.
     *
     * @param path The Path.
     * @param id The id for the button.
     * @param label The link text in the button.
     * @return A link e.g. {@code "<a href="https://example.com">example</a>"}
     */
    @Deprecated
    public static String getLinkButton(String path, String id, String label) {
        StringBuilder sb = new StringBuilder("<button ");
        if (id != null) {
            sb.append("id=\"").append(id).append("\" ");
        }
        sb.append("onclick=\"document.location='").append(path).append("'\">")
                .append(label).append("</button>");
        return sb.toString();
    }

    /**
     * Wrap a string with an HTML start and end tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     * @param tag The tag text.
     */
    public void addT(StringBuilder sb, String s, String tag) {
        addST(sb, tag);
        sb.append(s);
        addET(sb, tag);
    }

    /**
     * Wrap a String with a start tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     * @param tag The tag text.
     */
    public void addST(StringBuilder sb, String s, String tag) {
        addST(sb, tag);
        sb.append(s);
    }

    /**
     * Add a start tag.
     *
     * @param sb The StringBuilder to append to.
     * @param tag The tag text.
     */
    public void addST(StringBuilder sb, String tag) {
        sb.append("<").append(tag).append(">");
    }

    /**
     * Wrap a String with an end tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     * @param tag The tag text.
     */
    public void addET(StringBuilder sb, String s, String tag) {
        sb.append(s);
        addET(sb, tag);
    }

    /**
     * Add an end tag.
     *
     * @param sb The StringBuilder to append to.
     * @param tag The tag text.
     */
    public void addET(StringBuilder sb, String tag) {
        sb.append("</").append(tag).append(">\n");
    }
    
    /**
     * Wraps a string with start and end HTML Heading 1 tags.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addH1(StringBuilder sb, String s) {
        sb.append(Web_Strings.H1_ST).append(s).append(Web_Strings.H1_ET);
    }
    
    /**
     * Wraps a string with start and end HTML Heading 1 tags.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addH2(StringBuilder sb, String s) {
        sb.append(Web_Strings.H2_ST).append(s).append(Web_Strings.H2_ET);
    }
    
    /**
     * Wraps a string with start and end HTML Heading 3 tags.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addH3(StringBuilder sb, String s) {
        sb.append(Web_Strings.H3_ST).append(s).append(Web_Strings.H3_ET);
    }
    
    /**
     * Wraps a string with start and end HTML Heading 4 tags.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addH4(StringBuilder sb, String s) {
        sb.append(Web_Strings.H4_ST).append(s).append(Web_Strings.H4_ET);
    }
    
    /**
     * Wraps a string with start and end HTML Heading 5 tags.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addH5(StringBuilder sb, String s) {
        sb.append(Web_Strings.H5_ST).append(s).append(Web_Strings.H5_ET);
    }
            
    /**
     * Add a start HTML DIV tag
     *
     * @param sb The StringBuilder to append to.
     */
    public void addDIVST(StringBuilder sb) {
        sb.append(Web_Strings.DIV_ST);
    }

    /**
     * Add an end HTML DIV tag
     *
     * @param sb The StringBuilder to append to.
     */
    public void addDIVET(StringBuilder sb) {
        sb.append(Web_Strings.DIV_ET);
    }

    /**
     * Wraps a string with start and end HTML list item tags.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addLI(StringBuilder sb, String s) {
        sb.append(Web_Strings.LI_ST).append(s).append(Web_Strings.LI_ET);
    }

    /**
     * Append an HTML start list item tag.
     *
     * @param sb The StringBuilder to append to.
     */
    public void addLIST(StringBuilder sb) {
        sb.append(Web_Strings.LI_ST);
    }
    
    /**
     * Append an HTML start list item tag with an ID.
     *
     * @param sb The StringBuilder to append to.
     * @param id The ID to add to the tag.
     */
    public void addLIIDST(StringBuilder sb, String id) {
        sb.append("<").append(Web_Strings.LI).append(" id=\"").append(id)
                .append("\">");
    }
    
    /**
     * Append an HTML start list item tag.
     *
     * @param sb The StringBuilder to append to.
     * @param indent A positive integer 0 is no indent, indents are spaces.
     */
    public void addLIST(StringBuilder sb, int indent) {
        addIndent(sb, indent);
        addLIST(sb);
    }
    
    public void addIndent(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i ++) {
            sb.append(" ");
        }
    }
       
    /**
     * Wrap the start of a string with an HTML start list item tag.
     *
     * @param sb The StringBuilder to append to.
     * @param id The string to append as the id.
     * @param s The string to append after the start tag.
     */
    public void addLIIDST(StringBuilder sb, String id, String s) {
        addLIIDST(sb, id);
        sb.append(s);
    }
    
    /**
     * Wrap the start of a string with an HTML start list item tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to append after the start tag.
     */
    public void addLIST(StringBuilder sb, String s) {
        addLIST(sb);
        sb.append(s);
    }
    
    /**
     * Wrap the start of a string with an HTML start list item tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to append after the start tag.
     * @param indent A positive integer 0 is no indent, indents are spaces.
     */
    public void addLIST(StringBuilder sb, String s, int indent) {
        addIndent(sb, indent);
        addLIST(sb);
        sb.append(s);
    }

    /**
     * Append an HTML end list item tag.
     *
     * @param sb The StringBuilder to append to.
     */
    public void addLIET(StringBuilder sb) {
        sb.append(Web_Strings.LI_ET);
    }
    
    /**
     * Wrap the end of a string with an HTML end list item tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to append after the start tag.
     */
    public void addLIET(StringBuilder sb, String s) {
        sb.append(s).append(Web_Strings.LI_ET);
    }

    /**
     * Add a start HTML ordered list tag
     *
     * @param sb The StringBuilder to append to.
     */
    public void addOLST(StringBuilder sb) {
        sb.append(Web_Strings.OL_ST);
    }

    /**
     * Add an end HTML ordered list tag
     *
     * @param sb The StringBuilder to append to.
     */
    public void addOLET(StringBuilder sb) {
        sb.append(Web_Strings.OL_ET);
    }
    
    /**
     * Wrap a string with an HTML paragraph tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addP(StringBuilder sb, String s) {
        addPST(sb, s);
        sb.append(Web_Strings.P_ET);
    }
    
    /**
     * Wrap the start of a string with an HTML start paragraph tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to add after the start tag.
     */
    public void addPST(StringBuilder sb, String s) {
        sb.append(Web_Strings.P_ST).append(s);
    }

    /**
     * Wrap the end of a string with an HTML end paragraph tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The String to add before the end tag.
     */
    public void addPET(StringBuilder sb, String s) {
        sb.append(s).append(Web_Strings.P_ET);
    }

    /**
     * Wrap a string with an HTML PRE tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addPRE(StringBuilder sb, String s) {
        sb.append(Web_Strings.PRE_ST).append(s).append(Web_Strings.PRE_ET);
    }
    
    /**
     * Wrap start of a string with an HTML PRE start tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addPREST(StringBuilder sb, String s) {
        sb.append(Web_Strings.PRE_ST).append(s);
    }

    /**
     * Wrap a string with an HTML end PRE tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addPREET(StringBuilder sb, String s) {
        sb.append(s).append(Web_Strings.PRE_ET);
    }
    
    /**
     * Wrap a string with an HTML PRE tag.
     *
     * @param sb The StringBuilder to append to.
     * @param tag The details for the CODE tag can be null.
     * @param s The string to wrap.
     */
    public void addCODE(StringBuilder sb, String tag, String s) {
        sb.append(Web_Strings.CODE_ST).append(s).append(Web_Strings.CODE_ET);
    }
    
    /**
     * Wrap start of a string with an HTML PRE start tag.
     *
     * @param sb The StringBuilder to append to.
     * @param tag The details for the CODE tag can be null.
     * @param s The string to wrap.
     */
    public void addCODEST(StringBuilder sb, String tag, String s) {
        if (tag == null) {
            sb.append(Web_Strings.CODE_ST).append(s);
        } else {
            sb.append("<" + Web_Strings.CODE + " " + tag + ">").append(s);
        }
    }

    /**
     * Wrap a string with an HTML end PRE tag.
     *
     * @param sb The StringBuilder to append to.
     * @param s The string to wrap.
     */
    public void addCODEET(StringBuilder sb, String s) {
        sb.append(s).append(Web_Strings.CODE_ET);
    }
    
    /**
     * Add a start HTML unordered list tag
     *
     * @param sb The StringBuilder to append to.
     */
    public void addULST(StringBuilder sb) {
        sb.append(Web_Strings.UL_ST);
    }

    /**
     * Add an end HTML unordered list tag
     *
     * @param sb The StringBuilder to append to.
     */
    public void addULET(StringBuilder sb) {
        sb.append(Web_Strings.UL_ET);
    }
}
