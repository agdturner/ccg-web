/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.web.houseprices;

import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;

/**
 *
 * @author geoagdt
 */
public class Web_HousePriceRecord {

    /**
     * id (for indexing - this should be unique for each record)
     */
    private long ID;
    /**
     * number/name street-name, postcode
     */
    private String postcode;
    /**
     * number/name street-name, postcode
     */
    private String address;
    /**
     * number/name street-name, postcode
     */
    private String cityRegion;
    /**
     * number/name street-name, postcode
     */
    private String fullAddress;
    /**
     * Date
     */
    private String datePurchased;
    /**
     * Price Paid
     */
    private long price;
    /**
     * zooplaPropertyCode
     */
    private long zooplaPropertyCode;
    /**
     * zooplaExtraInfoURL
     */
    private String zooplaExtraInfoURL;
    /**
     * Flat/Detached/Semi-Detached/Mid-Terrace/End-Terrace/Townhouse etc
     */
    private String propertyType0;
    /**
     * Leasehold/Freehold
     */
    private String propertyType1;
    private int numberOfBedrooms;
    private int numberOfReceptionRooms;
    private int numberOfBathrooms;
    //private boolean garden;
    private boolean exLocalAuthority;

    /*
     * Example csv record
     * sw9 0xy,"Flat 8, Goodwood Mansions, Stockwell Park Walk, London SW9 0XY",
     * 28th Jul 2006,175000,23214986,
     * http://www.zoopla.co.uk/property/flat-8/goodwood-mansions/stockwell-park-walk/london/sw9-0xy/23214986,
     * Flat,Leasehold,2 Beds,1 Baths,1 Receps
     */
    public Web_HousePriceRecord(long ID) {
        this.ID = ID;
    }
    
    @Override
    public String toString() {
        return "" + ID + ", " 
                + postcode + ", "
                + address + ", "
                + cityRegion + ", "
                + fullAddress + ", "
                + datePurchased + ", "
                + price + ", "
                + zooplaPropertyCode + ", "
                + zooplaExtraInfoURL + ", "
                + propertyType0 + ", "
                + propertyType1 + ", "
                + numberOfBedrooms + ", "
                + numberOfReceptionRooms + ", "
                + numberOfBathrooms;
    }

    public static String toStringFields() {
        return "ID, postcode, address, cityRegion, fullAddress, datePurchased, "
                + "price, zooplaPropertyCode, zooplaExtraInfoURL, "
                + "propertyType0, propertyType1, numberOfBedrooms, "
                + "numberOfReceptionRooms, numberOfBathrooms";
    }

    public void processLine(String line) {
        String[] fields = line.split("\"");
        postcode = fields[0].substring(0, fields[0].length() - 1);
        initAddress(fields[1]);

        if (fields.length < 3) {
            int debug = 1;
        }

        fields = fields[2].split(",");
        datePurchased = fields[1];
        price = Long.valueOf(fields[2]);
        zooplaPropertyCode = Long.valueOf(fields[3]);
        zooplaExtraInfoURL = fields[4];
        propertyType0 = fields[5];
        propertyType1 = fields[6];
        numberOfBedrooms = getN(fields[7]);
        numberOfReceptionRooms = getN(fields[8]);
        numberOfBathrooms = getN(fields[9]);
    }

    public void initAddress(String s) {
        fullAddress = s;
        
        String uppercaseFullAddress = Generic_StaticString.getUpperCase(fullAddress);
        if (uppercaseFullAddress.contains("FOUNTAIN PLACE")) {
            int debug = 1;
        }
        if (uppercaseFullAddress.contains("FOXLEY SQUARE")) {
            int debug = 1;
        }
        if (uppercaseFullAddress.contains("CARLTON COURT")) {
            int debug = 1;
        }
        if (uppercaseFullAddress.contains("HAMMELTON GREEN")) {
            int debug = 1;
        }
        if (uppercaseFullAddress.contains("FAIRVBAIRN GREEN")) {
            int debug = 1;
        }
        
        String[] addressComponents = s.split(",");
        String[] split = addressComponents[addressComponents.length - 1].trim().split(" ");
        cityRegion = "";
        for (int i = 0; i < split.length - 2; i++) {
            cityRegion += split[i] + " ";
        }
        cityRegion = cityRegion.trim();
        String upperCasePostcode = Generic_StaticString.getUpperCase(postcode);
        address = "";
        if (addressComponents[0].startsWith("Flat ")) {
//            String[] split2 = addressComponents[0].split(" ");
//            if (split2.length > 1) {
//                address += Generic_StaticString.getUpperCase(split2[1]);
//            }
//            address += ", " + Generic_StaticString.getUpperCase(addressComponents[1]);
            address += Generic_StaticString.getUpperCase(addressComponents[0]);
            address += ", " + Generic_StaticString.getUpperCase(addressComponents[1]);
            address += ", " + upperCasePostcode;
        } else {
            address += Generic_StaticString.getUpperCase(addressComponents[0]);
            address += ", " + upperCasePostcode;
        }
        address = address.trim();
    }

    public int getN(String s) {
        String[] split = s.split(" ");
        if (split[0].equalsIgnoreCase("--")) {
            return -1;
        }
        if (split[0].equalsIgnoreCase("10+")) {
            return 11;
        }
        return Integer.valueOf(split[0]);
    }

    public long getID() {
        return ID;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCityRegion() {
        return cityRegion;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public long getPrice() {
        return price;
    }

    public long getZooplaPropertyCode() {
        return zooplaPropertyCode;
    }

    public String getZooplaExtraInfoURL() {
        return zooplaExtraInfoURL;
    }

    public String getPropertyType0() {
        return propertyType0;
    }

    public String getPropertyType1() {
        return propertyType1;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public int getNumberOfReceptionRooms() {
        return numberOfReceptionRooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public boolean isExLocalAuthority() {
        return exLocalAuthority;
    }

    public String getAddress() {
        return address;
    }
    
    public int getYearPurchased() {
        if (datePurchased.contains("1980")) {
            return 1980;
        }
        if (datePurchased.contains("1981")) {
            return 1981;
        }
        if (datePurchased.contains("1982")) {
            return 1982;
        }
        if (datePurchased.contains("1983")) {
            return 1983;
        }
        if (datePurchased.contains("1984")) {
            return 1984;
        }
        if (datePurchased.contains("1985")) {
            return 1985;
        }
        if (datePurchased.contains("1986")) {
            return 1986;
        }
        if (datePurchased.contains("1987")) {
            return 1987;
        }
        if (datePurchased.contains("1988")) {
            return 1988;
        }
        if (datePurchased.contains("1989")) {
            return 1989;
        }
        if (datePurchased.contains("1990")) {
            return 1990;
        }
        if (datePurchased.contains("1991")) {
            return 1991;
        }
        if (datePurchased.contains("1992")) {
            return 1992;
        }
        if (datePurchased.contains("1993")) {
            return 1993;
        }
        if (datePurchased.contains("1994")) {
            return 1994;
        }
        if (datePurchased.contains("1995")) {
            return 1995;
        }
        if (datePurchased.contains("1996")) {
            return 1996;
        }
        if (datePurchased.contains("1997")) {
            return 1997;
        }
        if (datePurchased.contains("1998")) {
            return 1998;
        }
        if (datePurchased.contains("1999")) {
            return 1999;
        }
        if (datePurchased.contains("2000")) {
            return 2000;
        }
        if (datePurchased.contains("2001")) {
            return 2001;
        }
        if (datePurchased.contains("2002")) {
            return 2002;
        }
        if (datePurchased.contains("2003")) {
            return 2003;
        }
        if (datePurchased.contains("2004")) {
            return 2004;
        }
        if (datePurchased.contains("2005")) {
            return 2005;
        }
        if (datePurchased.contains("2006")) {
            return 2006;
        }
        if (datePurchased.contains("2007")) {
            return 2007;
        }
        if (datePurchased.contains("2008")) {
            return 2008;
        }
        if (datePurchased.contains("2009")) {
            return 2009;
        }
        if (datePurchased.contains("2010")) {
            return 2010;
        }
        if (datePurchased.contains("2011")) {
            return 2011;
        }
        if (datePurchased.contains("2012")) {
            return 2012;
        }
        if (datePurchased.contains("2013")) {
            return 2013;
        }
        if (datePurchased.contains("2014")) {
            return 2014;
        }
        if (datePurchased.contains("2015")) {
            return 2015;
        }
        if (datePurchased.contains("2016")) {
            return 2016;
        }
        if (datePurchased.contains("2017")) {
            return 2017;
        }
        return 2018;
        
    }
}
