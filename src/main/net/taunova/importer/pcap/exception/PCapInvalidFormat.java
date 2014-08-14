/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap.exception;

/**
 *
 * @author Renat Gilmanov
 */
public class PCapInvalidFormat extends PCapException {


    /**
     * 
     * @param message
     */
    public PCapInvalidFormat(String message) {
        super(message);
    }
    
    /**
     *
     * @param cause
     */
    public PCapInvalidFormat(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public PCapInvalidFormat(String message, Throwable cause) {
        super(message, cause);
    }
}