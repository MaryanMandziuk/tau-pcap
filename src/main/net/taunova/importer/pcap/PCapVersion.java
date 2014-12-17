/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap;

/**
 * Represents a PCap version.
 * 
 * @author Renat.Gilmanov
 */
public class PCapVersion {
    private short major;
    private short minor;

    /**
     * 
     * @param major
     * @param minor 
     */
    public PCapVersion(short major, short minor) {
        this.major = major;
        this.minor = minor;
    }

    /**
     * 
     * @return 
     */
    public short getMajor() {
        return major;
    }

    /**
     * 
     * @return 
     */
    public short getMinor() {
        return minor;
    }
    
    public String toString() {
        return major + "." + minor;
    }
}
