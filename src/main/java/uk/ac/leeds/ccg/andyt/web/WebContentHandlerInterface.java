/*
 * WebContentHandlerInterface.java
 *
 * Created on 19 March 2007, 16:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.leeds.ccg.andyt.web;

import java.net.URL;

/**
 *
 * @author geoagdt
 */
public interface WebContentHandlerInterface {
    
    public Object update( URL webPage );
    
    public Object write( URL webPage );

    public Object read( URL webPage );
    
}
