/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.web.houseprices;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;

/**
 *
 * @author geoagdt
 */
public class HousepriceLoader {
    
    public TreeMap<Long,HousePriceRecord> housepriceRecords;
    
    public HousepriceLoader(){ }
    
    public void scrape(String firstpartPostcode) {
        String[] args = new String[3];
        args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
        args[1] = ""; //"r";
        args[2] = Generic_StaticString.getUpperCase(firstpartPostcode);
        //args[3] = "7";
        new ZooplaHousepriceScraper().run(args);
    }

    public void scrape(
            String firstpartPostcode, 
            String secondpartPostcode) {
        String[] args = new String[4];
        args[0] = "/nfs/see-fs-02_users/geoagdt/scratch02/zoopla/";
        args[1] = ""; //"r";
        args[2] = Generic_StaticString.getUpperCase(firstpartPostcode);
        args[3] = Generic_StaticString.getUpperCase(secondpartPostcode);
        //args[3] = "7";
        new ZooplaHousepriceScraper().run(args);
    }

    public void load(String postcode) {
        long ID = 0L;
        housepriceRecords = new TreeMap<Long,HousePriceRecord>();
        File file = new File("/scratch01/Work/Projects/NewEnclosures/_Houseprices_sw9.csv");
        BufferedReader br = Generic_StaticIO.getBufferedReader(file);
        StreamTokenizer st = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(st);
        //String s = "'";
        st.wordChars('\'', '\'');
        int token;
        String line = "";
        try {
            token = st.nextToken();
            line = st.sval;
            token = st.nextToken();
            while (token != StreamTokenizer.TT_EOF) {
                switch (token) {
                    case StreamTokenizer.TT_EOL:
                        if (line != null) {
                            HousePriceRecord rec = new HousePriceRecord(ID);
                            System.out.println(line);
                            rec.processLine(line);
                            housepriceRecords.put(ID, rec);
                            ID ++;
                        }
                        break;
                    default:
                        line = st.sval;
                        break;
                }
                //token = st.nextToken();
                token = st.nextToken();
            }
        } catch (IOException ex) {
            Logger.getLogger(HousepriceLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
}
