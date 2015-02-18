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
public interface RulesProvider {
    
    /**
     * Returns an array holding all parsing rules.
     * 
     * @return parsing rules
     */
    Object[][] getRules();
}
