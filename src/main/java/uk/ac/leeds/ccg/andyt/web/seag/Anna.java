/*
 * Copyright 2018 geoagdt.
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
package uk.ac.leeds.ccg.andyt.web.seag;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import uk.ac.leeds.ccg.andyt.web.Web_Scraper;
import uk.ac.leeds.ccg.andyt.web.core.Web_Environment;

/**
 *
 * @author geoagdt
 */
public class Anna extends Web_Scraper {

    public Anna(Web_Environment e) {
        super(e);
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        new Anna(new Web_Environment()).run(args);
    }

    public void run(String[] args) {
        run();
    }

    public void run() {
        url = "http://www.gov.scot/seag/publicsearch.aspx";
        dir = new File(System.getProperty("user.dir"), "data");
        dir = new File(dir, "seag");
        dir = new File(dir, "anna");
        dir.mkdirs();
        recs = new HashSet<>();
        parse(url);
    }

    HashSet<String> recs;

    public void parse(String url) {
//        ArrayList<String> lines;
        File f;
        PrintWriter outputPW;
        outputPW = getPrintWriter(url + ".html");
//        lines = getHTML(10, 1, url, outputPW);
//        Iterator<String> ite;
//        ite = lines.iterator();
//        while (ite.hasNext()) {
//            System.out.println(ite.next());
//        }
//        outputPW.close();

//        Map<String, String> allFields = new HashMap<>();
//        allFields.put("search1$GridView1", "Page$2");
//        try {
////            Document doc = Jsoup.connect(url).get();
//            Document doc = Jsoup.connect(url)
//                    .followRedirects(true)
//                    .userAgent("netscape")
//                    .data(allFields)
//                    .post();
//            System.out.println(doc.html());
//        } catch (IOException ex) {
//            Logger.getLogger(Anna.class.getName()).log(Level.SEVERE, null, ex);
//        }
        // Initialise webClient
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
        String START_URL = url;
        try {
            HtmlPage page = webClient.getPage(START_URL);
            printPage(page, outputPW);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.waitForBackgroundJavaScript(3000);
            int totalPages = 212;
            for (int i = 2; i < totalPages; i++) {
                ScriptResult sr = page.executeJavaScript("javascript:__doPostBack('search1$GridView1','Page$" + i + "')");
                Page page2 = sr.getNewPage();
                if (page2.isHtmlPage()) {
                    HtmlPage page2h = (HtmlPage) page2;
                    printPage(page2h, outputPW);
                }
            }
            //http://www.gov.scot/seag/details.aspx?id=PRE\00844&sid=2
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        outputPW.close();
    }

    protected void printPage(HtmlPage page2h, PrintWriter outputPW) {
        String txt = page2h.asText();
//        System.out.println(txt);
//                    String xml = page2h.asXml();
//                    System.out.println(xml);
        String[] txts = txt.split("Date Last Modified");
        for (int j = 1; j < txts.length; j++) {
            String[] txts2 = txts[j].split("\\r|\\n");
            String s;
            for (int k = 0; k < txts2.length; k++) {
                if (txts2[k].startsWith("SEA") || txts2[k].startsWith("UK")
                        || txts2[k].startsWith("EU") || txts2[k].startsWith("PRE")) {
                    String[] s2 = txts2[k].split("\t");
                    s = "\"" + s2[0] + "\",\"http://www.gov.scot/seag/details.aspx?id=" + txts2[k].replaceAll("\t", "\",\"") + "\"";
                    if (recs.contains(s)) {
                        System.out.println("Duplicate" + s);
                    } else {
                        //System.out.println(s);
                        outputPW.println(s);
                        recs.add(s);
                    }
                } else {
                    if (!txts2[k].isEmpty()) {
                        if (txts2[k].equalsIgnoreCase("1\t2\t3\t4\t5\t6\t7\t8\t9\t10...")) {
                            System.out.println(txts2[k]);
                        }
                    }
                }
            }

//            s = s.replaceAll("(\\r|\\n)", "") + "\"";
//            System.out.println(s);
//            outputPW.println(s);
//            for (int k = 1; k < txts2.length; k++) {
//                String s2;
//                s2 = "\"http://www.gov.scot/seag/details.aspx?id=" + txts2[k].replaceAll("\t", "\",\"");
//                //s2 = s2.replaceAll("(\\r|\\n)", "") + "\"";
//                System.out.println(s2);
//                outputPW.println(s2);
//            }
//                        System.out.println(txts[j]);
//                        outputPW.println(txts[j]);
        }
        outputPW.flush();
    }

}
