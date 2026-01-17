/*
 * Copyright 2022 Centre for Computational Geography.
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
package uk.ac.leeds.ccg.web.core;

import uk.ac.leeds.ccg.generic.core.Generic_Strings;

/**
 *
 * @author Andy Turner
 */
public class Web_Strings extends Generic_Strings {
    
    /**
     * BODY
     */
    public static final String BODY = "BODY";
    
    /**
     * BODY
     */
    public static final String CODE = "CODE";
    
    /**
     * DIV
     */
    public static final String DIV = "DIV";
    
    /**
     * H1
     */
    public static final String H1 = "H1";
    
    /**
     * H2
     */
    public static final String H2 = "H2";
    
    /**
     * H4
     */
    public static final String H4 = "H4";
    
    /**
     * H3
     */
    public static final String H3 = "H3";
    
    /**
     * H5
     */
    public static final String H5 = "H5";
    
    /**
     * HEAD
     */
    public static final String HEAD = "HEAD";
    
    /**
     * HTML
     */
    public static final String HTML = "HTML";
    
    /**
     * HTML
     */
    public static final String html = "html";
    
    /**
     * LI
     */
    public static final String LI = "LI";
    
    /**
     * OL
     */
    public static final String OL = "OL";
    
    /**
     * P
     */
    public static final String P = "P";
    
    /**
     * PRE
     */
    public static final String PRE = "PRE";
    
    /**
     * TITLE
     */
    public static final String TITLE = "TITLE";
    
    /**
     * UL
     */
    public static final String UL = "UL";
    
    /**
     * Create a new instance.
     */
    public Web_Strings(){}
    
    /**
     * HTML Start Tag
     */
    public static final byte[] HTML_ST = (getStartTag(Web_Strings.HTML + " lang=en-GB") + "\n").getBytes();
    
    /**
     * HTML End Tag
     */
    public static final byte[] HTML_ET = (getEndTag(Web_Strings.HTML) + "\n").getBytes();
    
    /**
     * HEAD Start Tag
     */
    public static final byte[] HEAD_ST = (getStartTag(Web_Strings.HEAD) + "\n").getBytes();

    /**
     * HEAD End Tag
     */
    public static final byte[] HEAD_ET = (getEndTag(Web_Strings.HEAD) + "\n").getBytes();
    
    /**
     * BODY Start Tag
     */
    public static final byte[] BODY_ST = (getStartTag(Web_Strings.BODY) + "\n").getBytes();
    
    /**
     * BODY End Tag
     */
    public static final byte[] BODY_ET = (getEndTag(Web_Strings.BODY) + "\n").getBytes();
    
    /**
     * DIV Start Tag
     */
    public static final String DIV_ST = getStartTag(Web_Strings.DIV) + "\n";
    
    /**
     * DIV End Tag
     */
    public static final String DIV_ET = getEndTag(Web_Strings.DIV) + "\n";
    
    /**
     * OL Start Tag
     */
    public static final String OL_ST = getStartTag(Web_Strings.OL) + "\n";
    
    /**
     * OL End Tag
     */
    public static final String OL_ET = getEndTag(Web_Strings.OL) + "\n";
    
    /**
     * UL Start Tag
     */
    public static final String UL_ST = getStartTag(Web_Strings.UL) + "\n";
    
    /**
     * UL End Tag
     */
    public static final String UL_ET = getEndTag(Web_Strings.UL) + "\n";
    
    /**
     * LI Start Tag
     */
    public static final String LI_ST = getStartTag(Web_Strings.LI);
    
    /**
     * LI End Tag
     */
    public static final String LI_ET = getEndTag(Web_Strings.LI) + "\n";
    
    /**
     * P Start Tag
     */
    public static final String P_ST = getStartTag(Web_Strings.P);
    
    /**
     * P End Tag
     */
    public static final String P_ET = getEndTag(Web_Strings.P) + "\n";
    
    /**
     * H1 Start Tag
     */
    public static final String H1_ST = getStartTag(Web_Strings.H1);
    
    /**
     * H1 End Tag
     */
    public static final String H1_ET = getEndTag(Web_Strings.H1) + "\n";
    
    /**
     * H2 Start Tag
     */
    public static final String H2_ST = getStartTag(Web_Strings.H2);
    
    /**
     * H2 End Tag
     */
    public static final String H2_ET = getEndTag("H2") + "\n";
    
    /**
     * H3 Start Tag
     */
    public static final String H3_ST = getStartTag(Web_Strings.H3);
    
    /**
     * H3 End Tag
     */
    public static final String H3_ET = getEndTag(Web_Strings.H3) + "\n";
    
    /**
     * H4 Start Tag
     */
    public static final String H4_ST = getStartTag(Web_Strings.H4);
    
    /**
     * H4 End Tag
     */
    public static final String H4_ET = getEndTag(Web_Strings.H4) + "\n";
    
    /**
     * H5 Start Tag
     */
    public static final String H5_ST = getStartTag(Web_Strings.H5);
    
    /**
     * H5 End Tag
     */
    public static final String H5_ET = getEndTag(Web_Strings.H5) + "\n";
    
    /**
     * PRE Start Tag
     */
    public static final String PRE_ST = getStartTag(Web_Strings.PRE);
    
    /**
     * PRE End Tag
     */
    public static final String PRE_ET = getEndTag(Web_Strings.PRE);
    
    /**
     * CODE Start Tag
     */
    public static final String CODE_ST = getStartTag(Web_Strings.CODE);
    
    /**
     * PRE End Tag
     */
    public static final String CODE_ET = getEndTag(Web_Strings.CODE);
    
    /**
     * TITLE Start Tag
     */
    public static final String TITLE_ST = getStartTag(Web_Strings.TITLE);

    /**
     * TITLE End Tag
     */
    public static final String TITLE_ET = getEndTag(Web_Strings.TITLE);
    
    /**
     * The line separator. (\n)
     */
    public static final byte[] LINE_SEPARATOR = System.getProperty("line.separator").getBytes();
    
    /**
     * Document Type Descriptor for HTML 
     */
    public static final byte[] DTD = getStartTag("!DOCTYPE html").getBytes();
    
    /**
     * @param tag The name of the tag.
     * @return {@code "<" + tag + ">"}
     */
    public static String getStartTag(String tag) {
        return "<" + tag + ">";
    }
    
    /**
     * @param tag The name of the tag.
     * @return {@code "</" + tag + ">"}
     */
    public static String getEndTag(String tag) {
        return getStartTag("/" + tag);
    }

    /**
     * @param id The string for the id.
     * @return {@code " " + "id=\"" + id + "\""}
     */
    public static String getID(String id) {
        return " " + "id=\"" + id + "\"";
    }

    /**
     * @param tag The name of the tag.
     * @return {@code "<" + tag + ">"}
     */
    public static String getStartTag(String tag, String id) {
        return "<" + tag
                + getID(id)
                + ">";
    }
    
    /**
     * @param className The string for the class.
     * @return {@code " " + "class=\"" + className + "\""}
     */
    public static String getClass(String className) {
        return " " + "class=\"" + className + "\"";
    }
        
    /**
     * @param tag The name of the tag.
     * @return {@code "<" + tag + ">"}
     */
    public static String getStartTag(String tag, String id, String className) {
        return "<" + tag
                + getID(id)
                + getClass(className)
                + ">";
    }
    
    
}
