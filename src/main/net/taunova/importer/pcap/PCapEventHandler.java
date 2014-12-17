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
 * Receives notification of the logical content of a PCap data.
 * 
 * @author Renat.Gilmanov
 */
public interface PCapEventHandler {

    /**
     * Receive notification of the beginning of the data.
     */
    void onImportStart();
    
    /**
     * Receive notification of the end of the data.
     */
    void onImportFinish();
    
    /**
     * Receive notification a processing error.
     */
    void onImportFailed();    
    
    /**
     * Receive notification of a PCap header.
     * 
     * @param version extracted version
     * @param type extracted type
     */
    void handleInfo(PCapVersion version, PCapDatalink type);

    /**
     * Receive notification of an entity.
     * 
     * @param saved saved entity size
     * @param actual actual entity size
     * @param timestamp entity timestamp
     * @param stream data stream to read entity content
     * @throws java.io.IOException
     */
    void handleEntity(int saved, int actual, long timestamp, DataInputStream stream) throws IOException;
}



