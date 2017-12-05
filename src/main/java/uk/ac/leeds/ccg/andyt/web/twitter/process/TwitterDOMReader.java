/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.web.twitter.process;

import java.io.File;
import java.util.TreeSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_XMLDOMReader;

public class TwitterDOMReader extends Generic_XMLDOMReader {
    

    protected TwitterDOMReader(File f) {
        init(f, "*");
    }
    
    protected void initNodeList() {
        nodeList = aDocument.getElementsByTagName("*");
    }

    /**
     * Return the index in the nodeList of the next node after that with index i
     * with name equal to nodeName or return nodeList.getLength();
     *
     * @param nodeName
     * @param nodeList
     * @param i
     * @return
     */
    protected int find(String nodeName, NodeList nodeList, int i) {
        int j;
        Node n;
        String nNodeName;
        for (j = i; j < nodeList.getLength(); j++) {
            n = nodeList.item(j);
            nNodeName = n.getNodeName();
            String nTextContent;
            if (nNodeName.equalsIgnoreCase(nodeName)) {
                return j;
            }
        }
        return j;
    }
    
    protected String getTwitterText() {
        String result;
        result = "";
//        ArrayList<String> result;
//        result = new ArrayList<String>();
        boolean foundLayer;
        foundLayer = false;
        int i;
        i = 0;
        Node n;
        n = null;
        String nTextContent;
        i = find("<div class=\"js-text-tweet-container\">", nodeList, i);
        //System.out.println(nodeList.item(i).getNodeName());
        while (!foundLayer) {
            i = find("Layer", nodeList, i + 1);
            //System.out.println(nodeList.item(i).getNodeName());
            i = find("ows:Identifier", nodeList, i + 1);
            n = nodeList.item(i);
            //System.out.println(n.getNodeName());
            nTextContent = n.getTextContent();
//            if (nTextContent.equalsIgnoreCase(layerName)) {
//                //System.out.println(nTextContent);
//                foundLayer = true;
//            }
        }
        i = find("Value", nodeList, i + 1);
        n = nodeList.item(i);
        //System.out.println(nodeList.item(i).getNodeName());
        nTextContent = n.getTextContent();
//        result.add(new SARIC_Time(se, nTextContent));
        //System.out.println(nTextContent);
        i = find("Value", nodeList, i + 1);
        n = nodeList.item(i);
        while (nodeList.item(i + 1).getNodeName().equalsIgnoreCase("Value")) {
            nTextContent = n.getTextContent();
//            result.add(new SARIC_Time(se, nTextContent));
            //System.out.println(nTextContent);
            i = find("Value", nodeList, i + 1);
            n = nodeList.item(i);
        }
        nTextContent = n.getTextContent();
//        result.add(new SARIC_Time(se, nTextContent));
        return result;
    }
    
    @Override
    protected void parseNodeList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
