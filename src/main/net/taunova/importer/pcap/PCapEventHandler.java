/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package net.taunova.importer.pcap;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Renat.Gilmanov
 */
public interface PCapEventHandler {

    /**
     * 
     */
    void onImportStart();
    
    /**
     * 
     */
    void onImportFinish();
    
    /**
     * 
     */
    void onImportFailed();    
    
    /**
     * 
     * @param version
     * @param type
     */
    void handleInfo(PCapVersion version, PCapDatalink type);

    /**
     * 
     * @param entity
     * @param body
     */
    void handleEntity(int saved, int actual, long timestamp, DataInputStream stream) throws IOException;
}



