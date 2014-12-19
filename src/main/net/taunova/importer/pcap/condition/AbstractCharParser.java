/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap.condition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Renat Gilmanov
 */
public class AbstractCharParser extends AbstractParser {

    private static final int STATES = 2;
    // next state
    // command
    private final Object[][] test = {

        // IN        TYPE    START      LEFT     OPERATOR   RIGHT    END
        //                     1         2          3        4        5
        {"0-9",     RANGE,  2& ADD,   2& ADD,      ERR,   4& ADD,   ERR  },
        {"a-z",     RANGE,  2& ADD,   2& ADD,      ERR,   4& ADD,   ERR  },
        {"A-Z",     RANGE,  2& ADD,   2& ADD,      ERR,   4& ADD,   ERR  },
        {".:",      CLIST,     ERR,   2& ADD,      ERR,   4& ADD,   ERR  },        
        {" ",       CHAR,      SKIP,  3& SKIP,  4& SKIP,  5& SKIP,  SKIP },        
        {"(",       CHAR,   1& PUSH,  1& PUSH,     ERR,   1& PUSH,  ERR  },
        {")",       CHAR,      ERR,      POP,      POP,      POP,   POP  },
        {"==,!=,",  SLIST,     ERR,   4& ADD,   4& ADD,      ERR,   ERR  },
        {"><",      CLIST,     ERR,   4& ADD,   4& ADD,      ERR,   ERR  },
        {"port",    STRING,    ERR,   4& ADD,   4& ADD,      ERR,   ERR  },
        {"<=,>=",   SLIST,     ERR,   4& ADD,   4& ADD,      ERR,   ERR  },                
        {"and,not", SLIST,     ERR,   4& ADD,   4& ADD,      ERR,   ERR  },
        {"other",   ALL,       ERR,      ERR,      ERR,      ERR,   ERR  },
    };

    private Object[] finalStep = null;
    
    /**
     * 
     */
    public AbstractCharParser() {
        translate(test);
    }
    
    //protected TreeMap<Object, Object[]> stateMap = new TreeMap<>();
    protected List<Object> stateList = new LinkedList<>();
    protected Map<Object, Object[]> stateMap = new HashMap<>();
    protected Map<String, Object[]> stringMap = new HashMap<>();
    
    
    public void parse(String text) {
        
    }
    
    /**
     * 
     * 
     * @param step 
     */
    protected void translate(Object[][] step) {
        if(step.length == 0) {
            throw new IllegalArgumentException("no states is not allowed");
        }
        
        final int STEPS = step[0].length;
        
        for(int i=0; i<step.length;i++) {
            int type = (Integer)step[i][1];
            String defintion = (String)step[i][0];
            
            if(null == defintion) {
                throw new IllegalArgumentException("Rule definition can't be null");
            }
            
            switch(type) {
                case CHAR:
                    _processChar(defintion, step[i]);
                    break;
                case STRING:
                    _processString(defintion);
                    break;
                case RANGE:
                    _processRange(defintion, step[i]);
                    break;
                case CLIST:
                    _processCharList(defintion, step[i]);
                    break;
                case SLIST:
                    _processStringList(defintion, step[i]);
                    break;
                case ALL:
                    _createFinalRule(step);
                    break;
                default:
                    System.out.println("Unknown type: " + type);
                    break;
            }            
        }        
        
    }
    
    protected void _processChar(String str, Object[] states) {
        if(str.length() != 1) {
            throw new IllegalArgumentException("Can't parse the char rule: " + str);
        }            
        char ch = str.charAt(0);       
        _registerInput(ch, states);
    }

    protected void _processRange(String range, Object[] states) {        
        if(range.length() !=3 || range.charAt(1) != '-') {
            throw new IllegalArgumentException("Range should be defined as 'a-b': " + range);
        }
        
        char first = range.charAt(0);
        char last = range.charAt(2);
        int size = last - first;
        
        if(size > 100) {
            throw new IllegalArgumentException("Range is too big: " + range + " " + size);            
        }
        
        for(char next=first; next<first+size; next++) {
            _registerInput(next, states);
        }       
    }        

    protected void _processCharList(String list, Object[] states) {
        if(list.length() == 0) {
            throw new IllegalArgumentException("Can't parse char rule: " + list);
        }                    
        
        for(char ch: list.toCharArray()) {
            _registerInput(ch, states);
        }
    }            
    
    protected void _createFinalRule(Object[] states) {
        finalStep = states;
    }
    
    protected void _processString(String str) {
        if(str.length() == 0) {
            throw new IllegalArgumentException("String can't be empty");
        }
        
        _registerInput(str, test);
    }    
    
    
    protected void _registerInput(char ch, Object[] states) {
        if(stateMap.containsKey(ch)) {
            throw new IllegalArgumentException("Char has already been registered: " + ch);
        }
        stateMap.put(ch, states);
    }
    
    protected void _registerInput(String string, Object[] states) {
        if(stateMap.containsKey(string)) {
            throw new IllegalArgumentException("String has already been registered: " + string);
        }
        stringMap.put(string, states);
    }        
    
    protected void _processStringList(String range, Object[] states) {
        final String SEPARATOR = ",";
        if(!range.contains(SEPARATOR)) {
            throw new IllegalArgumentException("Can't parse string list: " + range);
        }                    
        
        for(String string : range.split(SEPARATOR)) {
            stringMap.put(string, states);
        }
    }        
    
    public static void main(String[] args) {
        AbstractCharParser parser = new AbstractCharParser();        
    }
    
    // (ip.src==10.0.0.1 and tcp.srcport==80) or (ip.dst==10.0.0.1 and tcp.dstport==80)
    // tcp contains traffic
    // tcp.port==4000
    // (tcp[0:2] > 1500 and tcp[0:2] < 1550) or (tcp[2:2] > 1500 and tcp[2:2] < 1550)
    // tcp portrange 1501-1549
    // not ether dst 01:80:c2:00:00:0e
    // dst port 135 or dst port 445 or dst port 1433  and tcp[tcpflags] & (tcp-syn) != 0 and tcp[tcpflags] & (tcp-ack) = 0 and src net 192.168.0.0/24
    
    // ip.addr==10.0.0.1 && ip.addr==10.0.0.2    
}
