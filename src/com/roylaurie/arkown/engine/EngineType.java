package com.roylaurie.arkown.engine;

import com.roylaurie.arkown.engine.callofduty7.CallOfDuty7Engine;
import com.roylaurie.arkown.engine.source.SourceEngine;

/**
 * Provides an enumerated list of all available Engine sub-classes.
 * 
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 */
public enum EngineType {
    // keep these in alphabetic order
    CALL_OF_DUTY_7,
    SOURCE;
    
    /**
     * This uses switch instead of a map due to instatiation quirks the relationship between enginetype and
     * engine children.
     *
     * @return Engine
     */
    public Engine getEngine() {
        switch (this) {
        case CALL_OF_DUTY_7:
            return CallOfDuty7Engine.getInstance();
        case SOURCE:
            return SourceEngine.getInstance();
        }
        
        throw new IllegalStateException("Invalid engine type.");
    }
    
    /**
     * Retrieve the appropriate EngineType for the given engine name.
     * Uses Engine.getName() to compare.
     *
     * @param String name
     * @return EngineType
     */
    public static EngineType parseFromEngineName(String name) {
        for (EngineType engineType : values()) {
            if (engineType.getEngine().getName().equals(name)) {
                return engineType;
            }
        }
        
        throw new IllegalArgumentException("Unknown engine `" + name + "`.");
    }      
}
