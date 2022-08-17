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
 * @version 1.0
 */
public abstract class Web_ContentHandler {

    /**
     * Creates a new instance of Web_ContentHandler
     */
    public Web_ContentHandler() {
    }
    
    public static final byte[] DTD = "<!DOCTYPE html>".getBytes();
    public static final byte[] HTMLST = "<html lang=en-GB>".getBytes();
    public static final byte[] HTMLET = "</html>".getBytes();
    public static final byte[] HEADST = "<head>".getBytes();
    public static final byte[] HEADET = "</head>".getBytes();
    public static final byte[] BODYST = "<body>".getBytes();
    public static final byte[] BODYET = "</body>".getBytes();
    public static final byte[] DIVST = "<div>".getBytes();
    public static final byte[] DIVET = "</div>".getBytes();
    public static final byte[] OLST = "<ol>".getBytes();
    public static final byte[] OLET = "</ol>".getBytes();
    public static final byte[] ULST = "<ul>".getBytes();
    public static final byte[] ULET = "</ul>".getBytes();
    public static final byte[] LIST = "<li>".getBytes();
    public static final byte[] LIET = "</li>".getBytes();
    public static final byte[] PST = "<p>".getBytes();
    public static final byte[] PET = "</p>".getBytes();
}
