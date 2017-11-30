/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;

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
        dataDir = new File("M:/teaching/GEOG3600 Dissertation/2017-2018/Harriet Jack/data");
        File inputDataDir;
        inputDataDir = new File(dataDir, "input");
        File outputDataDir;
        outputDataDir = new File(dataDir, "output");
        File inputFile;
        inputFile = new File(inputDataDir, "LCC.csv");
        ArrayList<String> lines;
        //lines = Generic_StaticIO.readIntoArrayList_String(inputFile, 0);
        lines = Generic_ReadCSV.read(inputFile, outputDataDir, 7);
        
        Iterator<String> ite;
        ite = lines.iterator();
        String line;
        String header = ite.next();
        System.out.println(header);
        while (ite.hasNext()) {
            line = ite.next();
            //System.out.println(line);
            
            String[] split;
            split = line.split("\",");
            
            for (int i = 0; i < split.length; i ++) {
                if (i == 1) {
                    
                } else {
                    System.out.print(split[i] + "\",");
                }
            }
            System.out.println();
            
//            if (line.contains("http")) {
//              System.out.println(line);
//            }
        }
    }
    
    public ArrayList<String> getHTML(
            String sURL) {
        ArrayList<String> result = new ArrayList<>();
        URL url = null;
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
                result.add(line);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(System.err);
            //System.exit(1);
        }
        //System.exit(1);

        return result;
    }
}
