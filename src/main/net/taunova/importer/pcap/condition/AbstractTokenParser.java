/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap.condition;

/**
 *
 * @author Renat Gilmanov
 */
public class AbstractTokenParser extends AbstractParser {

    private static final int STATES = 2;    
        
    //private final int STATES = 2;
    // next state
    // command
    private final Object[][] test = {
        //    
        {"\\s", 1, 1},        
        {"a", 1, 1},
        {"A", 1, 1}};
    
    protected String[] tokenize(String text) {
        return null;
    }
    
    public String process(String text) {
        return null;
    }
}
