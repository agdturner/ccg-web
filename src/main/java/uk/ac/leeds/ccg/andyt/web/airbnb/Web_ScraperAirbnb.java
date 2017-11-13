package uk.ac.leeds.ccg.andyt.web.airbnb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;

public class Web_ScraperAirbnb {

    public Web_ScraperAirbnb() {
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        new Web_ScraperAirbnb().run(args);
    }

    String url;
    File dir;

    public void run(String[] args) {
        url = "https://www.airbnb.co.uk/s/Barcelona--Spain";
        dir = new File(System.getProperty("user.dir"));
        dir = Generic_StaticIO.createNewFile(dir);
        File logFile = new File(dir, "Test.log");
        try {
            logFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Web_ScraperAirbnb.class.getName()).log(Level.SEVERE, null, ex);
        }
        parsePlace(url);
    }

    String getFilename(String url) {
        String result = url.replaceAll(":", "_");
        result = result.replaceAll("\\?", "_Q_");
        result = result.replaceAll("/", "_");
        return result;
    }

    PrintWriter getPrintWriter(String url) {
        PrintWriter pw;
        String filename;
        File outputFile;
        filename = getFilename(url);
        outputFile = new File(dir,
                filename);
        outputFile.getParentFile().mkdirs();
        try {
            outputFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Web_ScraperAirbnb.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = Generic_StaticIO.getPrintWriter(outputFile, false);
        return pw;
    }

    public void parsePlace(String url) {
        String homesurl = url + "/homes";
        PrintWriter outputPW = getPrintWriter(homesurl + ".html");
        ArrayList<String> lines;
        lines = getHTML(homesurl, outputPW);
        outputPW.close();

        // Get first section of homes listing
        Object[] parseHomes = parseHomes(lines, true);
        HashSet<String> rooms = (HashSet<String>) parseHomes[0];

        // Get remaining section of homes listings
        int max = (Integer) parseHomes[1];
        for (int i = 2; i <= max; i++) {
            homesurl = url + "?section_offset=" + i;
            outputPW = getPrintWriter(homesurl + ".html");
            lines = getHTML(homesurl, outputPW);
            outputPW.close();
            parseHomes = parseHomes(lines, false);
            rooms.addAll((HashSet<String>) parseHomes[0]);
        }

        // Go through all the room listings and get some details.
        Iterator<String> ite;
        ite = rooms.iterator();
        String roomsURL;

        HashMap<String, TreeMap<Integer, Object[]>> hostListings = new HashMap<>();
        while (ite.hasNext()) {
            roomsURL = ite.next();
            outputPW = getPrintWriter(roomsURL + ".html");
            lines = getHTML(roomsURL, outputPW);
            outputPW.close();
            Object[] hostRooms;
            hostRooms = parseRooms(roomsURL, lines);
            //HashSet<String> roomListing;
            String hostID = (String) hostRooms[1];
            //roomListing = (HashSet<String>) hostRooms[0];
            TreeMap<Integer, Object[]> numberRoomlisting;
            if (hostListings.containsKey(hostID)) {
                numberRoomlisting = hostListings.get(hostID);
                int n = numberRoomlisting.lastKey() + 1;
                numberRoomlisting.put(n, hostRooms);
            } else {
                numberRoomlisting = new TreeMap<>();
                numberRoomlisting.put(1, hostRooms);
                hostListings.put(hostID, numberRoomlisting);
            }
            
        }

        // Compile and write result
        outputPW = getPrintWriter("Barcelona2017-11-11.txt");
        ite = hostListings.keySet().iterator();

        outputPW.println("HostID,TotalHomes,TotalRooms");
        TreeMap<Integer, Object[]> hostList;
        String hostID;
        int totalHomes;
        int totalRooms;
        Iterator<Integer> ite2;
        while (ite.hasNext()) {
            hostID = ite.next();
            hostList = hostListings.get(hostID);
            totalHomes = hostList.lastKey();
            totalRooms = 0;
            ite2 = hostList.keySet().iterator();
            while (ite2.hasNext()) {
                totalRooms += ((HashSet<String>) hostList.get(ite2.next())[0]).size();
            }
            outputPW.println(hostID + "," + totalHomes + "," + totalRooms);
        }
        outputPW.close();

        File resultDetail = new File(
                dir, "result.dat");
        Generic_StaticIO.writeObject(hostListings, resultDetail);

    }

    public ArrayList<String> getHTML(
            String sURL,
            PrintWriter outputPW) {
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
                outputPW.write(line);
                result.add(line);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(System.err);
            //System.exit(1);
        }
        //System.exit(1);

        return result;
    }

    Object[] parseHomes(ArrayList<String> lines, boolean parseMax) {
        Object[] result = new Object[2];
        HashSet<String> homes = new HashSet<>();
        result[0] = homes;
        Iterator<String> ite = lines.iterator();
        int max = Integer.MIN_VALUE;
        String line;
        String placeURL;
        while (ite.hasNext()) {
            line = ite.next();
            String s;
            s = "/rooms/";
            if (line.contains(s)) {
                String[] split;
                split = line.split("\"");
                for (int i = 0; i < split.length; i++) {
                    if (split[i].contains(s)) {
                        if (split[i].contains("airbnb")) {
                            placeURL = "https://" + split[i].split("\\?")[0];
                            System.out.println(placeURL);
                            homes.add(placeURL);
                        }
                    }
                }
            }
            if (parseMax) {
                s = "section_offset=";
                if (line.contains(s)) {
                    String[] split;
                    split = line.split("\"");
                    for (int i = 0; i < split.length; i++) {
                        if (split[i].contains(s)) {
                            String[] split2 = split[i].split("=");
                            if (split2.length > 2) {
                                max = Math.max(max, Integer.valueOf(split2[split2.length - 1]));
                                //System.out.println(split[i]);
                            } else {
                                System.out.println(split[i]);
                            }
                        }
                    }
                }
            }
        }
        result[1] = max;
        return result;
    }

    /**
     *
     * @param roomsURL Sent in for packing into result.
     * @param lines
     * @return
     */
    Object[] parseRooms(String roomsURL, ArrayList<String> lines) {
        Object[] result = new Object[3]; // HostID, number of Rooms 
        HashSet<String> rooms = new HashSet<>();
        result[0] = rooms;
        result[2] = roomsURL;
        Iterator<String> ite = lines.iterator();
        String line;
        String hostID = "";
        while (ite.hasNext()) {
            line = ite.next();
            String s;
            String s2;
            String s3;
            s = "host_name";
            s2 = "profile_path";
            s3 = "/users/show/";
            if (line.contains(s) && line.contains(s2) && line.contains(s3)) {
                String[] split;
                split = line.split("/users/show/");
                hostID = split[1].split("\"")[0];
            }
//            [{"id":"4818588/double_bed","quantity":1,"type":"double_bed"}],"id":4818588,"room_number":1}],"market":"Barcelona","name":"Luxury@Rambla: bedroom+livingroom 
                s = "room_number";
                s2 = "id";
//                s3 = "quantity";
//                String s4;
//                s4 = "type";
                String s5;
                s5 = "listing_rooms";
                if (line.contains(s) && line.contains(s2) && line.contains(s5)) {
                    String detail = line.split("listing_rooms")[1].split(",\"photos\"")[0] + "}]";
                    String[] beds = detail.split("beds");
                    for (int i = 0; i < beds.length; i ++){
                    rooms.add(beds[i]);
                    }
                }
        }
        hostID = hostID.split("#")[0];
        System.out.println("hostID " + hostID);

        result[1] = hostID;
        return result;
    }
}
