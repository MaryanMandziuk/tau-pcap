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
public class PCapException extends Exception {

    /**
     * 
     * @param message
     */
    public PCapException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public PCapException(Throwable cause) {
        this("Invalid PCap format", cause);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public PCapException(String message, Throwable cause) {
        super(message, cause);
    }
}
