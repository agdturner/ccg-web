package uk.ac.leeds.ccg.andyt.web.airbnb;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.web.Web_Scraper;
import uk.ac.leeds.ccg.andyt.web.core.Web_Environment;

public class Web_ScraperAirbnb extends Web_Scraper {

    public Web_ScraperAirbnb(Web_Environment e) {
        super(e);
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Web_ScraperAirbnb p = new Web_ScraperAirbnb(new Web_Environment(
                    new Data_Environment(new Generic_Environment())));
            p.run(args);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    String place;
    String date;
    boolean overwrite;

    public void run(String[] args) throws IOException {
        //doUKPostcodeAreas();
        doGlobalCity();
    }

    public void doGlobalCity() throws IOException {
        overwrite = true;
        //overwrite = false; // This will only re-get the data if it has not already been got (today).
        place = "Barcelona--Spain";
        date = java.time.LocalDate.now().toString();
        url = "https://www.airbnb.co.uk/s/" + place;
        dir = new File(System.getProperty("user.dir"), "data");
        dir = new File(dir, "airbnb");
        dir = new File(dir, place);
        dir = new File(dir, date);
        dir.mkdirs();
        File logFile = new File(dir, "Test.log");
            logFile.createNewFile();
        parsePlace(url);
    }

    public void doUKPostcodeAreas() throws IOException {
        //overwrite = true;
        overwrite = false; // This will only re-get the data if it has not already been got (today).
        for (int i = 1; i < 30; i++) {
            place = "Leeds-LS" + i;
//        place = "Liverpool";
//        place = "Manchester";
//        place = "Sunderland";
            date = java.time.LocalDate.now().toString();
            url = "https://www.airbnb.co.uk/s/" + place;
            dir = new File(System.getProperty("user.dir"), "data");
            dir = new File(dir, "airbnb");
            dir = new File(dir, place);
            dir = new File(dir, date);
            dir.mkdirs();
            File logFile = new File(dir, "Test.log");
            logFile.createNewFile();
            parsePlace(url);
        }
    }


    public void parsePlace(String url) throws IOException {
        String homesurl = url + "/homes";
        //String homesurl = url + "/homes?refinement_paths%5B%5D=%2Fhomes&place_id=&query=Barcelona%2C%20Spain&allow_override%5B%5D=&s_tag=s2aGabIK";
        ArrayList<String> lines;
        PrintWriter outputPW;
        if (!overwrite) {
            outputPW = getPrintWriter(homesurl + ".html");
            lines = getHTML(10, 1, homesurl, outputPW);
            outputPW.close();
        } else {
            String filename;
            File outputFile;
            filename = getFilename(url);
            outputFile = new File(dir, filename);
            if (outputFile.exists()) {
                lines = env.env.io.readIntoArrayList_String(outputFile);
            } else {
                outputPW = getPrintWriter(homesurl + ".html");
                lines = getHTML(10, 1, homesurl, outputPW);
                outputPW.close();
            }
        }

        // Get first section of homes listing
        Object[] parseHomes = parseHomes(lines, true);
        HashSet<String> rooms = (HashSet<String>) parseHomes[0];

        // Get remaining section of homes listings
        int max = (Integer) parseHomes[1];
        for (int i = 2; i <= max; i++) {
            homesurl = url + "?section_offset=" + i;
            outputPW = getPrintWriter(homesurl + ".html");
            lines = getHTML(10, 1, homesurl, outputPW);
            outputPW.close();
            parseHomes = parseHomes(lines, false);
            rooms.addAll((HashSet<String>) parseHomes[0]);
        }

        // Go through all the room listings and get some details.
        Iterator<String> ite;
        ite = rooms.iterator();
        String roomsURL;

        PrintWriter outputPW2;
        outputPW2 = getPrintWriter(place + date + "Listings.txt");
        outputPW2.println("URL,Host_ID,Price_Per_Night");

        HashMap<String, TreeMap<Integer, Object[]>> hostListings = new HashMap<>();
        while (ite.hasNext()) {
            roomsURL = ite.next();
            outputPW = getPrintWriter(roomsURL + ".html");
            lines = getHTML(100, 1, roomsURL, outputPW);
            outputPW.close();
            Object[] hostRooms;
            hostRooms = parseRooms(roomsURL, lines);
            String[] details;
            details = (String[]) hostRooms[1];
            for (int i = 0; i < details.length; i++) {
                outputPW2.print(details[i]);
                if (i < details.length - 1) {
                    outputPW2.print(",");
                }
            }
            outputPW2.println();
            outputPW2.flush();
            //HashSet<String> roomListing;
            String hostID = (String) details[1];
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

        outputPW2.close();

        // Compile and write result
        outputPW = getPrintWriter(place + date + ".txt");
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
        File resultDetail = new File(dir, "result.dat");
        env.env.io.writeObject(hostListings, resultDetail);
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
        Object[] result = new Object[2];
        String[] result2 = new String[3]; // URL, hostID, pricePerNight
        HashSet<String> rooms = new HashSet<>();
        result[0] = rooms;
        result[1] = result2;
        result2[0] = roomsURL;
        Iterator<String> ite = lines.iterator();
        String line;
        String hostID = "";
        String pricePerNight = "";
        while (ite.hasNext()) {
            line = ite.next();
            String s;
            String s2;
            String s3;

            if (line.contains("_10cqp947")) {
                line = ite.next();
                String[] split;
                split = line.split("<span>");
                pricePerNight = split[0].split("</span>")[0];
            }

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
                for (int i = 0; i < beds.length; i++) {
                    rooms.add(beds[i]);
                }
            }
        }
        hostID = hostID.split("#")[0];
        System.out.println("hostID " + hostID);

        result2[1] = hostID;
        result2[2] = pricePerNight;
        return result;
    }
}
