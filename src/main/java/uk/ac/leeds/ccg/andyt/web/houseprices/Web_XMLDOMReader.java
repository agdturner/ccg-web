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
package uk.ac.leeds.ccg.andyt.web.houseprices;

import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.ac.leeds.ccg.andyt.data.format.Data_ReadXML;

/**
 *
 */
public class Web_XMLDOMReader extends Data_ReadXML {

    public TreeSet<String> outcodePostcodes;

    public Web_XMLDOMReader() {
    }

    public Web_XMLDOMReader(
            File file) {
        init(file, "url");
        parseNodeList();
    }
    
    public static void main(String args[]) {
        Web_XMLDOMReader aXMLDOMReader = new Web_XMLDOMReader();
        File file = new File("/scratch02/zoopla/outcodes.xml");
        String nodeName = "url";
        aXMLDOMReader.init(file, nodeName);
        aXMLDOMReader.parseNodeList();
    }

    protected void print() {
        Iterator<String> ite = outcodePostcodes.iterator();
        while (ite.hasNext()) {
            System.out.println(ite.next());
        }
    }

    @Override
    protected final void parseNodeList() {
        //readNodeListElements();
        outcodePostcodes = new TreeSet<>();
        /*
         * <url>
         *  <lastmod>2013-03-05</lastmod>
         *  <priority>0.4</priority>
         *  <loc>http://www.zoopla.co.uk/home-values/browse/tn35/</loc>
         *  <changefreq>weekly</changefreq>
         * </url>
         */
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            //System.out.println(nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element aElement = (Element) nNode;
                //System.out.println("lastmod : " + aElement.getAttribute("lastmod"));
                //System.out.println("priority : " + aElement.getElementsByTagName("priority").item(0).getTextContent());
                String loc = aElement.getElementsByTagName("loc").item(0).getTextContent();
                //System.out.println("loc : " + loc);
                if (loc.contains("http://www.zoopla.co.uk/home-values/")) {
                    String[] split = loc.split("http://www.zoopla.co.uk/home-values/");
                    //System.out.println("split[1] : " + split[1]);
                    String[] split2 = split[1].split("/");
                    if (split2.length == 1) {
                        //System.out.println("split2[0] : " + split2[0]);
                        outcodePostcodes.add(split2[0]);
                    }
                }
                //System.out.println("changefreq : " + aElement.getElementsByTagName("changefreq").item(0).getTextContent());
            }
        }
        //print();
    }
}