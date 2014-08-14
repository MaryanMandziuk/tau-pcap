/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap;

/**
 *
 * @author Renat.Gilmanov
 */
public class PCapHelper {

    /**
     *
     * @param value
     * @return
     */
    public static int convert(int value) {
        return ((value & 0x000000FF) << 24
                | (value & 0x00FF0000) >>> 8
                | (value & 0x0000FF00) << 8
                | (value & 0xFF000000) >>> 24);
    }

    /**
     *
     * @param value
     * @return
     */
    public static short convert(short value) {
        return (short) (((value & 0xFF00) >>> 8)
                | ((value & 0x00FF) << 8));
    }

    /**
     *
     */
    private PCapHelper() {
    }
}
