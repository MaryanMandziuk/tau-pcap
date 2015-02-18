/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.util.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Renat Gilmanov
 */
public class AbstractCharParser extends Parser {

    private static final int INITIAL_STATE = 1;    
    private static final int STATES = 1; // 0, 1
    
    private static final int STATE_MASK =  0x00000FFF;
    private static final int ACTION_MASK = 0xFFFFF000;
    
    protected Token rootNode = new Token();
    protected Token currentNode = rootNode;
    protected int state = INITIAL_STATE;
    
    protected Stack<Context> stack = new Stack();
    protected Map<Object, Object[]> stateMap = new HashMap<>();
    protected Map<String, Object[]> stringMap = new HashMap<>();        
    

    private RulesProvider rules;
    private Object[] finalStep = null;
    
    /**
     * 
     */
    public AbstractCharParser() {
    }
    
    public AbstractCharParser(RulesProvider rules) {
        setRules(rules);
    }    
    
    public final void setRules(RulesProvider rules) {
        this.rules = rules;
        translate(rules.getRules());        
    }
    
    /**
     * Resets parser state.
     */    
    protected void reset() {
        if (null == rules) {
            throw new IllegalStateException("Rules are not defined for this Parser instance");
        }
        
        state = INITIAL_STATE;
        stack.clear();
        rootNode = new Token();
        currentNode = rootNode;        
    }
    
    /**
     * Text parsing routine.
     * 
     * @param text text to be parsed
     */
    public void parse(String text) {
        reset();
        
        int index = 0;
        while (index < text.length()) {
            boolean tokenFound = false;        
            int savedState = state;
            
            for (String token : stringMap.keySet()) {
                if (text.startsWith(token, index)) {
                    processRule(token, stringMap.get(token));
                    System.out.println("  --- STR rule for a str " + savedState + " -> " + state + " '" + token + "' state: ");
                    tokenFound = true;
                    index += token.length();
                    break;
                }
            }

            if (!tokenFound) {
                char ch = text.charAt(index++);
                if (stateMap.containsKey(ch)) {
                    processRule(Character.toString(ch), stateMap.get(ch));
                    System.out.println("  --- RUL rule for a chr " + savedState + " -> " + state + " '" + ch + "' state: ");
                } else {
                    System.out.println("  --- EMP for a chr '" + ch + "' state: " + state);
                }
            }
        }
        printTokens(rootNode);
    }

    /**
     * Processes a token according to the given rule.
     * 
     * @param token token to be processed
     * @param rule processing rule
     */
    protected void processRule(String token, Object[] rule) {        
        int command = (int)rule[STATES + state];  
        
        int action = command & ACTION_MASK;
        int newState = command & STATE_MASK;
        
         if(newState != state && newState != 0) {
            if(!currentNode.isEmpty()) {
                Token nextNode = new Token();
                currentNode.setNext(nextNode);            
                currentNode = nextNode;
            }
            state = newState;                
        }                
        
        switch(action) {
            case SKIP:
                // do nothing
                break;
            case ADD:
                currentNode.append(token);
                break;
            case PUSH:
                Token subToken = new Token();
                stack.push(new Context(currentNode, state));
                currentNode.addChild(subToken);
                currentNode = subToken;
                break;
            case POP:                
                Context context = stack.pop();
                currentNode = context.getCurrent();
                state = context.getState();
                break;
            case PASS:
                break;
            case WARN:
                System.out.println("Warning, wrong token: '" + token + "' ");
                break;
            case ERR:
                System.out.println("Error, wrong token: '" + token + "' ");
                break;       
            default:
                System.out.println("Unsupported action: " + Integer.toHexString(action));
                break;
        }        
    }
    
    /**
     * Translates defined steps into char processing rules.
     * 
     * @param steps a set of steps to be translated
     */
    protected void translate(Object[][] steps) {
        if(steps.length == 0) {
            throw new IllegalArgumentException("no states is not allowed");
        }
        
        final int STEPS = steps[0].length;
        
        for(int i=0; i<steps.length;i++) {
            int type = (Integer)steps[i][1];
            String defintion = (String)steps[i][0];
            
            if(null == defintion) {
                throw new IllegalArgumentException("Rule definition can't be null");
            }

            Object[] rule = new Object[STEPS];
            System.arraycopy(steps[i], 0, rule, 0, STEPS);
            
            switch(type) {
                case CHAR:
                    _processChar(defintion, rule);
                    break;
                case STRING:
                    _processString(defintion, rule);
                    break;
                case RANGE:
                    _processRange(defintion, rule);
                    break;
                case CLIST:
                    _processCharList(defintion, rule);
                    break;
                case SLIST:
                    _processStringList(defintion, rule);
                    break;
                case ALL:
                    _createFinalRule(steps);
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
    
    protected void _processString(String str, Object[] states) {
        if(str.length() == 0) {
            throw new IllegalArgumentException("String can't be empty");
        }
        
        _registerInput(str, states);
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
    
    protected static void printTokens(Token root) {
        
        Token token = root;
        while(null != token) {
            System.out.print("'");
            System.out.print(token.getText());
            System.out.print("' ");
            token = token.getNext();
        }
        System.out.println();
        
        for(Token t : root.getChildren()) {
            printTokens(t);
        }
    }            
}
