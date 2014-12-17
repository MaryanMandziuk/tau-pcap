/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer;

import net.taunova.importer.pcap.exception.PCapInvalidFormat;
import net.taunova.importer.pcap.exception.PCapSourceNotFound;

/**
 * Defines task-like abstraction in order to hide PCap parsing details.
 * 
 * @author Renat Gilmanov
 */
public interface PCapImportTask {

    /**
     * Initializes processing logic and verifies all preconditions are met.
     * 
     * @throws PCapSourceNotFound if supplied data source does not exist
     * @throws PCapInvalidFormat if the data provided has a wrong format
     */
    void init() throws PCapSourceNotFound, PCapInvalidFormat;

    /**
     * Reads the next PCap entry.
     * 
     * @throws PCapInvalidFormat if data entry is broken
     */
    void processNext() throws PCapInvalidFormat;

    /**
     * Verifies data source does not have data to be processed.
     * 
     * @return true if there is no data to process
     */
    boolean isFinished();
}