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
package uk.ac.leeds.ccg.andyt.web.guardian;

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
import uk.ac.leeds.ccg.andyt.data.format.Data_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.web.core.Web_Environment;
import uk.ac.leeds.ccg.andyt.web.core.Web_Object;

/**
 *
 * @author Andy Turner
 */
public class GuardianGetPage extends Web_Object {

    GuardianGetPage(Web_Environment e) {
        super(e);
    }

    public static void main(String[] args) {
        new GuardianGetPage(new Web_Environment()).run();
    }

    public void run() {
        File dataDir;
        dataDir = new File(System.getProperty("user.dir"), "data/");
        String filename;
        filename = "LexisNexis - The Guardian - Refugees AND BrexitHeadlinesForArticlesContaining_Syria.csv";
        run(dataDir, filename);
        filename = "LexisNexis - The Guardian - RefugeesHeadlinesForArticlesContaining_Syria.csv";
        run(dataDir, filename);
    }

    void run(File dataDir, String filename) {
        File inputDir;
        inputDir = new File(dataDir, "input");
        inputDir = new File(inputDir, "LexisNexis");
        File fin = new File(inputDir, filename);

        File outputDataDir;
        outputDataDir = new File(dataDir, "output");
        outputDataDir = new File(outputDataDir, "LexisNexis");
        File fout = new File(outputDataDir, filename);
        File fout2 = new File(dataDir, filename + "del");
        PrintWriter pw;
        pw = env.io.getPrintWriter(fout, false);

        String GuardianAPIKey;
        GuardianAPIKey = getGuardianAPIKey(dataDir);

        ArrayList<String> lines;
        lines = Data_ReadCSV.read(fin, outputDataDir, 6);

        String s;
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                pw.println(lines.get(i));
                System.out.println(lines.get(i));
            } else {
                String[] vals;
                vals = lines.get(i).split(",\"");
                for (int j = 0; j < vals.length; j++) {
                    if (j != 1) {
                        if (j != vals.length - 1) {
                            s = vals[j] + ",\"";
                            pw.print(s);
                            System.out.print(s);
                        } else {
                            pw.println(vals[j]);
                            System.out.println(vals[j]);
                        }
                    } else {
                        String sf = vals[vals.length - 1];
                        String title = sf.substring(0, sf.length() - 2);
                        String page;
                        page = getPage(vals[0], title, GuardianAPIKey, fout2);
                        s = vals[j].replace("\"", "") + " " + page + "\",\"";
                        pw.print(s);
                        System.out.print(s);
                    }
                }
            }
        }
        pw.close();
    }

    String getPage(String date, String Title, String GuardianAPIKey, File f) {
//        String Title;
//        Title = "Terror threats will be the new normal for Europe, experts say; \n"
//                + "Analysts believe there will be more security alerts and cancellations of major events after Paris attacks";
//        Title = "The Brexit nightmare is becoming reality. The remain camp is in denial; From Cameron's Panama Papers debacle to the weakness of Merkel and Hollande, the omens for Britain remaining in the EU get poorer by the day. Does anyone care?";
        String title;
        title = Title.replaceAll("\n", "-");
        title = title.replaceAll(" ", "-");
        title = title.replaceAll("'", "");
        title = title.replaceAll(",", "");
        title = title.replaceAll("\\.", "");
        title = title.replaceAll(";", "");
        title = title.replaceAll(":", "");
        title = title.replaceAll("\\?", "");
        title = title.replaceAll("!", "");
        title = title.replaceAll("--", "-");
        title = title.replaceAll("--", "-");
        title = title.replaceAll("--", "-");
        title = title.replaceAll("--", "-");

        //System.out.println(title);
//        //String date = "2016-04-09";
//        String[] dateParts = date.split("/");
//        String date2 = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
        //newspaperPageNumber,newspaperEditionDate
        String url;
        url = "http://content.guardianapis.com/search?"
                + "from-date=" + date + "&to-date=" + date
                + "&show-fields=newspaperPageNumber%2CnewspaperEditionDate&q="
                + title + "&api-key=" + GuardianAPIKey;
//        File f;
//        f = new File(outputDataDir, "GuardianGetPage.html");
        ArrayList<String> html;
        html = getHTML(url, f);
        if (html.size() > 0) {
            String[] split = html.get(0).split("\"newspaperPageNumber\":");
            if (split.length > 1) {
                String[] split2 = split[1].split("\"");
                //System.out.println("Page " + split2[1]);
                return "Page " + split2[1];
            }
        }
        return "";
    }

    public ArrayList<String> getHTML( String sURL, File fileToStore) {
        ArrayList<String> r = new ArrayList<>();
        URL url = null;
        PrintWriter pw  = env.io.getPrintWriter(fileToStore, false);
        HttpURLConnection con = null;
        BufferedReader br = null;
        String line = null;
        try {
            url = new URL(sURL);
        } catch (MalformedURLException e) {
            e.printStackTrace(System.err);
            //System.exit(1);
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            while ((line = br.readLine()) != null) {
                pw.println(line);
                r.add(line);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(System.err);
            //System.exit(1);
        }
        //System.exit(1);
        pw.close();
        return r;
    }

    String getGuardianAPIKey(File dataDir) {
        String r = "";
        File f;
        File dir;
        dir = new File(dataDir, "private");
        f = new File(dir, "GuardianAPIKey.txt");
        BufferedReader br;
        br = env.io.getBufferedReader(f);
        try {
            r = br.readLine();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(GuardianGetPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}
