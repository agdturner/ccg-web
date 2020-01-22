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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import uk.ac.leeds.ccg.generic.execution.Generic_Execution;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.web.core.Web_Environment;
import uk.ac.leeds.ccg.web.core.Web_Object;

/**
 *
 * @author Andy Turner
 */
public class Web_Scraper extends Web_Object {

    protected final Generic_Execution exec;
    protected double connectionCount;
    protected File directory;
    protected ExecutorService executorService;
    protected double permittedConnectionRate;
    protected long startTime;
    protected String url;
    public File dir;

    public Web_Scraper(Web_Environment e) {
        super(e);
        exec = new Generic_Execution(e.env);
    }
            
    protected void checkConnectionRate() {
        long timeToWaitInMilliseconds = 1000;
        double connectionRate = getConnectionRate();
        synchronized (this) {
            while (connectionRate > permittedConnectionRate) {
                try {
                    System.out.println("connectionRate > permittedConnectionRate");
                    System.out.println("" + connectionRate + " > " + permittedConnectionRate);
                    wait(timeToWaitInMilliseconds);
                    connectionRate = getConnectionRate();
                } catch (InterruptedException ex) {
                    env.env.log(ex.getMessage());
                }
            }
        }
    }

    public double getConnectionRate() {
        return getConnections() / (double) getTimeRunningMillis();
    }

    public double getConnections() {
        return connectionCount;
    }

    /**
     * @return the directory
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * @return the executorService
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    protected HttpURLConnection getOpenHttpURLConnection(String url) {
        checkConnectionRate();
        connectionCount += 1.0;
        HttpURLConnection r = null;
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
        //        // Set up proxy to use address and port from http://www.leeds.ac.uk/proxy.pac
        //        int port = 3128;
        //        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("www-cache.leeds.ac.uk", port));
        try {
            //            r = (HttpURLConnection) u.openConnection(proxy);
            if (u != null) {
                r = (HttpURLConnection) u.openConnection();
            }
            //            r.setRequestMethod("GET"); // GET is the default anyway.
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return r;
    }

    public long getTimeRunningMillis() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    protected String getFilename(String url) {
        String result = url.replaceAll(":", "_");
        result = result.replaceAll("\\?", "_Q_");
        result = result.replaceAll("/", "_");
        return result;
    }

    public ArrayList<String> getHTML(int numberOfRecursiveAttempts, 
            int attemptNumber, String sURL, PrintWriter outputPW) {
        /**
         * Wait 5 seconds.
         */
        synchronized (this) {
            try {
                this.wait(5000 * attemptNumber);
            } catch (InterruptedException ex) {
                env.env.log(ex.getMessage());
            }
        }
        ArrayList<String> result = new ArrayList<>();
        URL u = null;
        HttpURLConnection httpURLConnection ;
        BufferedReader br;
        String line;
        try {
            u = new URL(sURL);
        } catch (MalformedURLException e) {
            e.printStackTrace(System.err);
            //System.exit(1);
        }
        try {
            httpURLConnection = (HttpURLConnection) u.openConnection();
//            //It all used to work, but now it seems they are using session ids etc.
//            System.out.println("httpURLConnection.getURL() " + httpURLConnection.getURL());
//            System.out.println("httpURLConnection.getResponseCode() " + httpURLConnection.getResponseCode());
//            SocketPermission sp;
//            sp = (SocketPermission) httpURLConnection.getPermission();
//            System.out.println("sp.getActions() " + sp.getActions());
//            httpURLConnection.connect();
//            System.out.println("sp.getActions() " + sp.getActions());
//            System.out.println("httpURLConnection.getResponseCode() " + httpURLConnection.getResponseCode());
//            System.out.println("httpURLConnection.getContentType() " + httpURLConnection.getContentType());
//            Map<String, List<String>> ps;
//            ps = httpURLConnection.getRequestProperties();
//            String p;
//            List<String> l;
//            Iterator<String> ite2;
//            Iterator<String> ite;
//            ite = ps.keySet().iterator();
//            while (ite.hasNext()) {
//                p = ite.next();
//                System.out.println("Request property " + p);
//                l = ps.get(p);
//                ite2 = l.iterator();
//                while (ite2.hasNext()) {
//                    System.out.println("property " + ite2.next());
//                }
//            }
//            System.out.println("httpURLConnection.getURL() " + httpURLConnection.getURL());
//            // From here it worked
            httpURLConnection.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while ((line = br.readLine()) != null) {
                outputPW.write(line);
                result.add(line);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(System.err);
            return getHTML(numberOfRecursiveAttempts, attemptNumber++, sURL, outputPW);
            //System.exit(1);
        }
        //System.exit(1);
        return result;
    }

    protected PrintWriter getPrintWriter(String url) throws IOException {
        String filename = getFilename(url);
        Path outf = Paths.get(dir.toString(), filename);
        Files.createDirectories(outf.getParent());
        Files.createFile(outf);
        return Generic_IO.getPrintWriter(outf, false);
    }
    
}
