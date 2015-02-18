/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap.filter;

import net.taunova.util.parser.AbstractTokenParser;
import net.taunova.util.parser.RulesProvider;

/**
 *
 * @author Renat.Gilmanov
 */
public class ConditionParser extends AbstractTokenParser implements RulesProvider {

    // next state
    // command
    private final Object[][] test = {

        // IN        TYPE    START      LEFT     OPERATOR   RIGHT    END
        //                     1         2          3        4        5
        {"0-9",     RANGE,  2| ADD,   2| ADD,   4| ADD,   4| ADD,   ERR  },
        {"a-z",     RANGE,  2| ADD,   2| ADD,   4| ADD,   4| ADD,   ERR  },
        {"A-Z",     RANGE,  2| ADD,   2| ADD,   4| ADD,   4| ADD,   ERR  },
        {".:",      CLIST,     ERR,   2| ADD,      ERR,   4| ADD,   ERR  },        
        {" ",       CHAR,      SKIP,     SKIP,  4| SKIP,    SKIP,   SKIP },        
        {"(",       CHAR,   1| PUSH,  2| PUSH,     ERR,   2| PUSH,  ERR  },
        {")",       CHAR,      ERR,      POP,      POP,      POP,   POP  },
        {"==,!=,",  SLIST,     ERR,   3| ADD,   4| ADD,      ERR,   ERR  },
        {"><",      CLIST,     ERR,   4| ADD,   4| ADD,      ERR,   ERR  },
        {"port",    STRING,    ERR,   2| ADD,   4| ADD,      ERR,   ERR  },
        {"<=,>=",   SLIST,     ERR,   4| ADD,   4| ADD,      ERR,   ERR  },                
        {"and,or",  SLIST,  1| ADD,   4| ADD,   4| ADD,   1| ADD,   ERR  },
        {"other",   ALL,       ERR,      ERR,      ERR,      ERR,   ERR  },
    };            
    
    public ConditionParser() {
        super();
        setRules(this);
    }    
    
    public static void main(String[] args) {
        ConditionParser parser = new ConditionParser();                
//        parser.parse("tcp.port == 4000");
//        parser.parse("tcp.port==4000");
//        parser.parse("(tcp.port == 4000)");
//        parser.parse("(tcp.port==4000)");
//        parser.parse("ip.src == 10.0.0.1 and tcp.srcport == 80");
//        parser.parse("(ip.src == 10.0.0.1 and tcp.srcport == 80)");
        parser.parse("(ip.src==10.0.0.1 and tcp.srcport==80) or (ip.dst==10.0.0.1 and tcp.dstport==80)");
    }
    
    // (ip.src==10.0.0.1 and tcp.srcport==80) or (ip.dst==10.0.0.1 and tcp.dstport==80)
    // tcp contains traffic
    // tcp.port==4000
    // (tcp[0:2] > 1500 and tcp[0:2] < 1550) or (tcp[2:2] > 1500 and tcp[2:2] < 1550)
    // tcp portrange 1501-1549
    // not ether dst 01:80:c2:00:00:0e
    // dst port 135 or dst port 445 or dst port 1433  and tcp[tcpflags] & (tcp-syn) != 0 and tcp[tcpflags] & (tcp-ack) = 0 and src net 192.168.0.0/24
    
    // ip.addr==10.0.0.1 && ip.addr==10.0.0.2        

    @Override
    public Object[][] getRules() {
        return test;
    }

 
}
