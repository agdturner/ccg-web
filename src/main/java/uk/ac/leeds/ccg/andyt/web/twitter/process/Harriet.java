/*
 * Copyright 2017 geoagdt.
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
package uk.ac.leeds.ccg.andyt.web.twitter.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;

/**
 *
 * @author geoagdt
 */
public class Harriet {

    Harriet() {
    }

    public static void main(String[] args) {
        new Harriet().run();
    }

    public void run() {
        File dataDir;
//        dataDir = new File("M:/teaching/GEOG3600 Dissertation/2017-2018/Harriet Jack/data");
        dataDir = new File(
                System.getProperty("user.dir"),
                "data/");
        File inputDataDir;
//        inputDataDir = new File(dataDir, "input");
        inputDataDir = new File(dataDir, "input/twitter");
        File outputDataDir;
//        outputDataDir = new File(dataDir, "output");
        outputDataDir = new File(dataDir, "output/twitter");
        File inputFile;
        File outputFile;
        inputFile = new File(inputDataDir, "LCC.csv");
        outputFile = new File(outputDataDir, "LCCout.csv");
        ArrayList<String> lines;
        //lines = Generic_StaticIO.readIntoArrayList_String(inputFile, 0);
        lines = Generic_ReadCSV.read(inputFile, outputDataDir, 7);

        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(outputFile, false);

        File f;
        Iterator<String> ite;
        ite = lines.iterator();
        String line;
        String header = ite.next();
//        System.out.println(header);
        pw.println(header + ",full text available, full text of tweet");
        boolean fullTextAvailable;
        while (ite.hasNext()) {
            line = ite.next();
            fullTextAvailable = false;
            pw.print(line);
            System.out.print(line);

            String[] split;
            split = line.split("\",");
            if (split.length > 2) {
                String ttweetText = "";

                String[] split2;
                split2 = split[1].split(" ");
                System.out.println("" + split[1]);
                if (split2.length > 1) {
                    String url;
                    url = split2[split2.length - 1];
                    if (url.startsWith("https://t.co/")) {
//                        System.out.print(url);

                        f = new File(inputDataDir,
                                url.replace("https://t.co/", "") + ".html");

//                        //Uncomment the following to get the data.
//                        ArrayList<String> html;
//                        html = getHTML(url, f);
//                        if (html.size() > 0) {
                        if (f.length() > 0) {

                            try {
//                                Document doc = Jsoup.connect(url).get();

                                Document doc = Jsoup.parse(f, "utf-8");

                                String title = doc.title();

                                boolean foundTweetText;
                                ttweetText = "";

                                boolean foundSpan;
                                boolean endOfTweetText;
                                foundTweetText = false;
                                foundSpan = false;
                                endOfTweetText = false;

//                                Element tweetText = doc.select("js-tweet-text-container").first(); // 
////                                Element tweetText = doc.select("title").first();
//                                // Element tweetText = doc.select("<div class=\"js-tweet-text-container\">").first();
//                                if (tweetText != null) {
//                                    System.out.println(tweetText.text());
//                                }
                                Elements links = doc.getAllElements();// work from here using jsoup
                                //Elements links = doc.getElementsByTag("div class=\"js-tweet-text-container\"");// work from here using jsoup
                                Element e;
                                Iterator<Element> ite2;
                                ite2 = links.iterator();
                                while (ite2.hasNext()) {
                                    e = ite2.next();
                                    if (!endOfTweetText) {
                                        List<Node> nodes = e.childNodes();
                                        Iterator<Node> iteN;
                                        iteN = nodes.iterator();
                                        Node n;
                                        while (iteN.hasNext()) {
                                            n = iteN.next();
                                            System.out.println(n.toString());
                                            if (!endOfTweetText) {
                                                Attributes attributes;
                                                attributes = n.attributes();
                                                Iterator<Attribute> ite3;
                                                Attribute attribute;
                                                ite3 = attributes.iterator();
                                                while (ite3.hasNext()) {
                                                    attribute = ite3.next();
                                                    if (!endOfTweetText) {
                                                        if (!foundTweetText) {
//                                                            if (attribute.getValue().equals("js-tweet-text-container")) {
//                                                                                System.out.println("found tweet text");
//                                                            }
                                                            if (attribute.getKey().equalsIgnoreCase("class")) {
                                                                //System.out.println("attribute key " + attribute.getKey());
                                                                //System.out.println("attribute value " + attribute.getValue());

                                                                if (attribute.getValue().equals("js-tweet-text-container")) {
                                                                    System.out.println("found tweet text");
                                                                    foundTweetText = true;
                                                                    foundSpan = true;
                                                                    int childNodeSize;
                                                                    childNodeSize = n.childNodeSize();
                                                                    for (int i2 = 0; i2 < childNodeSize; i2++) {
                                                                        Node n2 = n.childNode(i2);
                                                                        System.out.println("node name " + n2.nodeName());
                                                                        if (n2.nodeName().equalsIgnoreCase("p")) {
                                                                            int childNodeSize2;
                                                                            childNodeSize2 = n2.childNodeSize();
                                                                            for (int i3 = 0; i3 < childNodeSize2; i3++) {
                                                                                Node n3 = n2.childNode(i3);
                                                                                System.out.println("node name " + n3.nodeName());
                                                                                Attributes aattributes;
                                                                                aattributes = n3.attributes();
                                                                                Iterator<Attribute> itea;
                                                                                itea = aattributes.iterator();
                                                                                Attribute aattribute;
                                                                                while (itea.hasNext()) {
                                                                                    aattribute = itea.next();
                                                                                    if (aattribute.getKey().equalsIgnoreCase("#text")) {
                                                                                        if (aattribute.getValue().startsWith("<p")) {
                                                                                            System.out.println("end of Tweet text");
                                                                                            endOfTweetText = true;
                                                                                        } else {
                                                                                            ttweetText += aattribute.getValue();
//                                                                        System.out.println("attribute key " + attribute.getKey());
//                                                                        System.out.println("attribute value " + attribute.getValue());
//                                                                        System.out.println("node name " + n.nodeName());
                                                                                        }
                                                                                    } else {
                                                                                        if (aattribute.getKey().equalsIgnoreCase("href")) {
                                                                                            if (aattribute.getValue().startsWith("/hashtag/")) {
                                                                                                String st;
                                                                                                st = aattribute.getValue().replace("/hashtag/", "#");
                                                                                                ttweetText += st.replaceAll("\\?src=hash", "");
                                                                                            } else if (aattribute.getValue().startsWith("/")) {
                                                                                                ttweetText += aattribute.getValue().replace("/", "@");
                                                                                            } else {
//                                                                                                System.out.println("aattribute.getKey() " + aattribute.getKey());
//                                                                                                System.out.println("aattribute.getValue() " + aattribute.getValue());
                                                                                            }
                                                                                        } else {
//                                                                                            System.out.println("aattribute.getKey() " + aattribute.getKey());
//                                                                                            System.out.println("aattribute.getValue() " + aattribute.getValue());
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    endOfTweetText = true;
                                                                }

                                                                if (attribute.getValue().equalsIgnoreCase("html-attribute-value")) {
//                                                                    System.out.println("attribute value " + attribute.getValue());
//                                                                    System.out.println("node name " + n.nodeName());
                                                                    int childNodeSize;
                                                                    childNodeSize = n.childNodeSize();
                                                                    for (int i2 = 0; i2 < childNodeSize; i2++) {
                                                                        Node n2 = n.childNode(i2);
//                                                                        System.out.println("node name " + n2.nodeName());
                                                                        Attributes attributes2;
                                                                        attributes2 = n2.attributes();
                                                                        Iterator<Attribute> ite32;
                                                                        Attribute attribute2;
                                                                        ite32 = attributes2.iterator();
                                                                        while (ite32.hasNext()) {
                                                                            attribute2 = ite32.next();
//                                                                            System.out.println("attribute key " + attribute2.getKey());
//                                                                            System.out.println("attribute value " + attribute2.getValue());
                                                                            if (attribute2.getValue().equals("js-tweet-text-container")) {
                                                                                System.out.println("found tweet text");
                                                                                foundTweetText = true;
                                                                                int childNodeSize2;
                                                                                childNodeSize2 = n2.childNodeSize();
                                                                                for (int i3 = 0; i3 < childNodeSize2; i3++) {
                                                                                    Node n3 = n2.childNode(i3);
//                                                                                    System.out.println("node name " + n3.nodeName());
                                                                                }
                                                                            }

                                                                        }
                                                                    }
                                                                    //                                       e2 = n.attributes()..
                                                                    //}
                                                                }

                                                            }
                                                        } else {
                                                            if (!foundSpan) {
                                                                //System.out.println("node name " + n.nodeName());
                                                                if (n.nodeName().equalsIgnoreCase("span")) {
                                                                    System.out.println("found span node");
                                                                    foundSpan = true;
                                                                }
                                                            } else {
//                                                                System.out.println("attribute key " + attribute.getKey());
//                                                                System.out.println("attribute value " + attribute.getValue());
                                                                if (attribute.getKey().equalsIgnoreCase("#text")) {
                                                                    if (attribute.getValue().startsWith("<p")) {
                                                                        System.out.println("end of Tweet text");
                                                                        endOfTweetText = true;
                                                                    } else {
                                                                        ttweetText += attribute.getValue();
//                                                                        System.out.println("attribute key " + attribute.getKey());
//                                                                        System.out.println("attribute value " + attribute.getValue());
//                                                                        System.out.println("node name " + n.nodeName());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                fullTextAvailable = true;
//                                foundTweetText = false;
//                                foundSpan = false;
//                                endOfTweetText = false;
                                //System.out.print(parseHTML(f));
                            } catch (IOException ex) {
                                Logger.getLogger(Harriet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            //ttweetText = "Tweet no longer available to get full text!";
                            ttweetText = "";
                        }
                    } else {
                        fullTextAvailable = true;
                        ttweetText = split[1].substring(1);
//                            System.out.print(url);
                    }
                } else {
                    ttweetText = split[1];
                }
                if (fullTextAvailable) {
//                    System.out.println(",\"Y\"");
                    pw.print(",\"Y\"");
                } else {
//                    System.out.println(",\"N\"");
                    pw.print(",\"N\"");
                }
                System.out.println(",\"" + ttweetText + "\"");
                pw.println(",\"" + ttweetText + "\"");
//            if (line.contains("http")) {
//              System.out.println(line);
//            }
            }
        }
    }

    public ArrayList<String> getHTML(
            String sURL,
            File fileToStore) {
        ArrayList<String> result = new ArrayList<>();
        URL url = null;
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(fileToStore, false);
        HttpURLConnection httpURLConnection = null;
        BufferedReader br = null;
        String line = null;
        try {
            url = new URL(sURL);
        } catch (MalformedURLException e) {
            e.printStackTrace(System.err);
            //System.exit(1);
        }
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            br = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            while ((line = br.readLine()) != null) {
                pw.println(line);
                result.add(line);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(System.err);
            //System.exit(1);
        }
        //System.exit(1);
        pw.close();
        return result;
    }

    String parseHTML(File f) {
        String result;
        TwitterDOMReader r;
        r = new TwitterDOMReader(f);
        result = r.getTwitterText();
        return result;
    }
}
