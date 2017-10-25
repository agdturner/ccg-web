package uk.ac.leeds.ccg.andyt.web;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Web_ScraperFacebook {

    public Web_ScraperFacebook() {
    }

    /** Main method
     * @param args
     * @throws java.lang.Exception */
    public static void main(String[] args) throws Exception {
        new Web_ScraperFacebook().run(args);
    }

    public void run(String[] args) throws Exception {
        String profileURL = "http://www.facebook.com/search.php?init=dir&q=BNP&type=groups#!/group.php?gid=280968101201";
        getFacebookContacts(profileURL);
    }

    public void getFacebookContacts(String a_URL_String) throws Exception {
        File out_File = new File("/scratch01/Work/data/Facebook/Test.html");
        out_File.getParentFile().mkdirs();
        out_File.createNewFile();
        PrintWriter out_PrintWriter = new PrintWriter(out_File);
        File log_File = new File("/scratch01/Work/data/Facebook/Test.log");
        log_File.createNewFile();
        PrintWriter log_PrintWriter = new PrintWriter(log_File);
        getHTML(a_URL_String, out_PrintWriter);
        out_PrintWriter.close();
        log_PrintWriter.close();
    }

    public void getHTML(
            String a_URL_String,
            PrintWriter a_PrintWriter) {
        HashSet result = new HashSet();
        URL a_URL = null;
        HttpURLConnection a_HttpURLConnection = null;
        BufferedReader a_BufferedReader = null;
        String line = null;
        try {
            a_URL = new URL(a_URL_String);
        } catch (MalformedURLException a_MalformedURLException) {
            System.exit(1);
        }
        try {
            a_HttpURLConnection = (HttpURLConnection) a_URL.openConnection();
        } catch (IOException a_IOException) {
            System.exit(1);
        }
        try {
            a_HttpURLConnection.setRequestMethod("GET");
        } catch (ProtocolException a_ProtocolException) {
            System.exit(1);
        }
        try {
            a_BufferedReader = new BufferedReader(
                    new InputStreamReader(a_HttpURLConnection.getInputStream()));
            while ((line = a_BufferedReader.readLine()) != null) {
                a_PrintWriter.write(line);
            }
        } catch (IOException a_IOException) {
            System.exit(1);
        }

    }
}
