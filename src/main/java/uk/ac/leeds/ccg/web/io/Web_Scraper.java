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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
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
 * Web_Scraper
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Web_Scraper extends Web_Object {

    protected final Generic_Execution exec;
    protected double connectionCount;
    protected Path directory;
    protected ExecutorService executorService;
    protected double permittedConnectionRate;
    protected long startTime;
    protected String url;
    public Path dir;

    public Web_Scraper(Web_Environment e) {
        super(e);
        exec = new Generic_Execution(e.env);
    }

    /**
     * Check ad delay if connection rate is over {@link #permittedConnectionRate}.
     */
    protected void checkConnectionRate() {
        long timeToWaitInMilliseconds = 1000;
        double cr = getConnectionRate();
        synchronized (this) {
            while (cr > permittedConnectionRate) {
                try {
                    env.env.log("connectionRate=" + cr);
                    env.env.log("permittedConnectionRate=" + permittedConnectionRate);
                    env.env.log("connectionRate > permittedConnectionRate");
                    env.env.log("waiting " + timeToWaitInMilliseconds + " milliseconds");
                    wait(timeToWaitInMilliseconds);
                    cr = getConnectionRate();
                } catch (InterruptedException ex) {
                    env.env.log(ex.getMessage());
                }
            }
        }
    }

    /**
     * @return A calculated connection rate. 
     */
    public double getConnectionRate() {
        return getConnections() / (double) getTimeRunningMillis();
    }

    /**
     * @return {@link #connectionCount} 
     */
    public double getConnections() {
        return connectionCount;
    }

    /**
     * @return {@link #directory}
     */
    public Path getDirectory() {
        return directory;
    }

    /**
     * @return {@link #executorService}
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * By default do not use a proxy.
     * @param url url
     * @return HttpURLConnection
     * @throws IOException If encountered.
     */
    protected HttpURLConnection getOpenHttpURLConnection(String url)
            throws IOException {
        return getOpenHttpURLConnection(url, false, 0, "");
    }

    /**
     * For http://www.leeds.ac.uk/proxy.pac:
     * <ul>
     * <li>proxyAddress = "www-cache.leeds.ac.uk"</li>
     * <li>port = 3128</li>
     * </ul>
     *
     * @param url url
     * @param useProxy If true then use the proxy.
     * @param port The proxy port.
     * @param proxyAddress The proxy address
     * @return HttpURLConnection
     * @throws IOException If encountered.
     */
    protected HttpURLConnection getOpenHttpURLConnection(String url,
            boolean useProxy, int port, String proxyAddress)
            throws IOException {
        checkConnectionRate();
        connectionCount += 1.0;
        HttpURLConnection r;
        URL u = new URL(url);
        if (useProxy) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    proxyAddress, port));
            r = (HttpURLConnection) u.openConnection(proxy);
        } else {
            r = (HttpURLConnection) u.openConnection();
        }
        //r.setRequestMethod("GET"); // GET is the default anyway.
        return r;
    }

    public long getTimeRunningMillis() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * @return {@link #url}
     */
    public String getUrl() {
        return url;
    }

    protected String getFilename(String url) {
        String r = url.replaceAll(":", "_");
        r = r.replaceAll("\\?", "_Q_");
        r = r.replaceAll("/", "_");
        return r;
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
        ArrayList<String> r = new ArrayList<>();
        URL u = null;
        HttpURLConnection httpURLConnection;
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
                r.add(line);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(System.err);
            return getHTML(numberOfRecursiveAttempts, attemptNumber++, sURL, outputPW);
            //System.exit(1);
        }
        //System.exit(1);
        return r;
    }

    protected PrintWriter getPrintWriter(String url) throws IOException {
        Path outf = Paths.get(dir.toString(), getFilename(url));
        Files.createDirectories(outf.getParent());
        Files.createFile(outf);
        return Generic_IO.getPrintWriter(outf, false);
    }

}
