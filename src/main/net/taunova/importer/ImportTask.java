/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer;

import java.io.FileNotFoundException;
import net.taunova.importer.pcap.exception.PCapInvalidFormat;

/**
 *
 * @author Renat Gilmanov
 */
public interface ImportTask {

    /**
     *
     * @throws FileNotFoundException
     * @throws PCapInvalidFormat
     */
    void init() throws FileNotFoundException, PCapInvalidFormat;

    /**
     *
     * @throws PCapInvalidFormat
     */
    void processNext() throws PCapInvalidFormat;

    /**
     *
     * @return
     */
    boolean isFinished();
}