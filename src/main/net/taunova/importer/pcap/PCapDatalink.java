/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package net.taunova.importer.pcap;

/**
 *
 * @author Renat Gilmanov
 */
public class PCapDatalink {

    private int type;
    
    /**
     * 
     * @param type 
     */
    public PCapDatalink(int type) {
        this.type = type;        
    }

    /**
     * 
     * @return 
     */
    public int getType() {
        return type;
    }
    
    public String toString() {
        return Integer.toString(type);
    }
}
