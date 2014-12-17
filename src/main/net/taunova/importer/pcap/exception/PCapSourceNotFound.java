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
public class PCapSourceNotFound extends PCapException {


    /**
     * 
     * @param message
     */
    public PCapSourceNotFound(String message) {
        super(message);
    }
    
    /**
     *
     * @param cause
     */
    public PCapSourceNotFound(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public PCapSourceNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}