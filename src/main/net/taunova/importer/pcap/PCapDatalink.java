/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package net.taunova.importer.pcap;

/**
 * Represents link-layer header type, specifying the type of headers at the
 * beginning of the packet.
 *
 * This can be various types such as 802.11, 802.11 with various radio
 * information, PPP, Token Ring, FDDI, etc.
 *
 * @author Renat Gilmanov
 */
public class PCapDatalink {

    /**
     * Link-layer header.
     */
    private int type;
    
    /**
     * Constructs a data link object with type code as specified
     * here: {@link http://www.tcpdump.org/linktypes.html}.
     * 
     * @param type link type code
     */
    public PCapDatalink(int type) {
        this.type = type;        
    }

    /**
     * Returns link type code.
     * 
     * @return link type code
     */
    public int getType() {
        return type;
    }
    
    public String toString() {
        return Integer.toString(type);
    }
}
