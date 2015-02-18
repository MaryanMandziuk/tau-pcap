/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.util.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Renat.Gilmanov
 */
public class Token {
    private final StringBuilder text = new StringBuilder();
    private final List<Token> children = new ArrayList<>();
    private Token next;    
    
    /**
     * 
     */
    public Token() {
        
    }
    
    public void addChild(Token child) {
        children.add(child);
    }

    public Token getNext() {
        return next;
    }

    public void setNext(Token next) {
        this.next = next;
    }

    public List<Token> getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    /**
     * 
     * @param chunk 
     */
    public void append(String chunk) {
        text.append(chunk);
    }

    /**
     * 
     * @return 
     */
    public String getText() {
        return text.toString();
    }

    /**
     * Verifies token is empty and can be used instead of creating a new one.
     * 
     * @return true if token is empty
     */
    public boolean isEmpty() {
        return (text.length() == 0);
    }
}
