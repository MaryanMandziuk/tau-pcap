/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.util.parser;

/**
 *
 * @author Renat Gilmanov
 */
public abstract class Parser {
    
    protected static final int SKIP   = 0x01000; // skip char
    protected static final int ADD    = 0x02000; // append char to the current context
    protected static final int PUSH   = 0x04000; // push context
    protected static final int POP    = 0x08000; // pop context
    protected static final int PASS   = 0x10000; // pass to the next rule
    protected static final int WARN   = 0x20000; // report a worning
    protected static final int ERR    = 0x40000; // report an error
       
    protected static final int ALL    = 0x1;    
    protected static final int CHAR   = 0x2;
    protected static final int STRING = 0x3;
    protected static final int RANGE  = 0x4;
    protected static final int CLIST  = 0x5;
    protected static final int SLIST  = 0x6;

    public abstract void parse(String text);
}
