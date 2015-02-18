/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap;

import java.io.File;
import net.taunova.importer.PCapImportTask;

/**
 * Provides various helpers.
 * 
 * @author Renat.Gilmanov
 */
public final class PCapHelper {

    /**
     * Create an import tasks implementation in order to import data from a file.
     * 
     * @param file file to be processed
     * @param handler processing handler
     * @return instance of the task
     */
    public static PCapImportTask createImportTask(File file, PCapEventHandler handler) {
        return new PCapImportTaskImpl(file, handler);
    }
    
    /**
     * Converts endianness.
     * 
     * @param value value to be converted
     * @return converted result
     */
    public static int convert(int value) {
        return ((value & 0x000000FF) << 24
                | (value & 0x00FF0000) >>> 8
                | (value & 0x0000FF00) << 8
                | (value & 0xFF000000) >>> 24);
    }

    /**
     * Converts endianness.
     * 
     * @param value value to be converted
     * @return converted result
     */
    public static short convert(short value) {
        return (short) (((value & 0xFF00) >>> 8)
                | ((value & 0x00FF) << 8));
    }

    /**
     * Utility class constructor.
     */
    private PCapHelper() {
    }
}
