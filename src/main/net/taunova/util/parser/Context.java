/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.util.parser;

/**
 *
 * @author Renat.Gilmanov
 */
class Context {
    
    private int state;
    private Token current;
    
    /**
     * 
     * @param root
     * @param state 
     */
    public Context(Token current, int state) {
        this.current = current;
        this.state = state;
    }

    /**
     * 
     * @return 
     */
    public Token getCurrent() {
        return current;
    }        
    
    /**
     * 
     * @return 
     */
    public int getState() {
        return state;
    }
   
}
