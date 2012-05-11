/**
 * @copyright (c) 2010 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.engine;

import com.roylaurie.arkown.engine.Connection.CommandConnection;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.arkown.engine.Connection.QueryConnection;

/**
 * Base class that defines a unique software engine, capable remote query and/or command.
 * 
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 */
public abstract class Engine implements Comparable<Engine> {
    /**
     * The user-friendly name.
     */
    private String mName = null;
    
    /**
     * The engine type.
     */
    private EngineType mType = null;
    
    /**
     * Maps capabilities of this engine to their available(TRUE)/unavailable(FALSE) boolean value.
     * By default, all capabilities are unavailable, unless mapped otherwise.
     */
    private Capability[] mCapabilities = null;
    
    /**
     * Defines various capabilities that are optionally available to a given engine.
     * By default, all capabilities are unavailable.
     * 
     * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
     */
    public static enum Capability {
        QUERY,
        COMMAND,
        QUERY_VIA_COMMAND,
        CREDENTIAL_USERNAME,
        ACTIVE_TERMINAL_COMMAND, // not compatible with PASSIVE_TERMINAL_COMMAND
        PASSIVE_TERMINAL_COMMAND; // not compatible with ACTIVE_TERMINAL_COMMAND
    }
    
    /**
     * Defines basic product enumeration functionality in Engine sub-classes.
     * 
     * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
     */
    public static interface Product {
        public abstract String getName();
    }
    
    /**
     * 
     * Preferred explicit constructor.
     *
     * @param String name
     * @param EngineType type
     * @param Capability[] availableCapabilities
     */
    protected Engine(String name, EngineType type, Capability[] availableCapabilities) {
        setName(name);
        setType(type);
        setAvailableCapabilities(availableCapabilities);
    }    
    
    @Override
    public int compareTo(Engine rh) {
        if (rh == null) {
            return 1;
        }
        
        return (getName().compareToIgnoreCase(rh.getName()));
    }
    
    /**
     * Retrieves the user-friendly name.
     * 
     * @return String
     */
    public final String getName() {
        return mName;
    }

    /**
     * Sets the user-friendly name.
     * Used during construciton.
     * 
     * @param String name
     */
    private void setName(String name) {
        mName = name;
    }
    
    /**
     * Retrieves the type.
     *
     * @return EngineType
     */
    public final EngineType getType() {
        return mType;
    }

    /**
     * Sets the type
     * 
     * @param EngineType type
     */
    private void setType(EngineType type) {
        mType = type;
    }    
    
    /**
     * Sets the specified capabilities as unavailable to this engine.
     * By default, all capabilities are unavailable.
     * 
     * @param Capability[] capabilities
     */
    private void setAvailableCapabilities(Capability[] capabilities) {
        mCapabilities = capabilities;
    }
    
    /**
     * Determines whether this engine supports the specified capability or not.
     * By default, all capabilities are unavailable.
     * 
     * @return boolean
     */
    public final boolean hasCapability(Capability capability) {
        for (Capability engineCapability : mCapabilities) {
            if (capability == engineCapability) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Helper method that performs a valueOf for this engine's product class.
     */
    @SuppressWarnings("unchecked")
    public final Enum<? extends Product> productValueOf(String enumToken) {
        return Enum.valueOf((Class<? extends Enum>)getProductClass(), enumToken);
    }
    
    /**
     * Retrieves the product enum class used by this engine.
     * 
     * @return Class<? extends Enum<? extends Product>>
     */
    public abstract Class<? extends Enum<? extends Product>> getProductClass();
    
    /**
     * Factories a product from it's name.
     *
     * @param String name
     * @return Enum<? extends Product>
     */
    public abstract Enum<? extends Product> parseProductFromName(String name);
    
    /**
     * Retrieves an array of available products.
     *
     * @return  Enum<? extends Product>[]
     */
    public abstract Enum<? extends Product>[] getProducts();
    
    /**
     * Retrieves the default port used in querying.
     *
     * @return int
     */
    public abstract int getDefaultQueryPort();
    
    /**
     * Retrieves a query connection.
     *
     * @param String hostname
     * @param int port
     * @return QueryConnection
     */
    public abstract QueryConnection factoryQueryConnection(String hostname, int port);
    
    /**
     * Retrieves a command connection.
     *
     * @param String hostname
     * @param int port
     * @param Credentials credentials
     * @return CommandConnection
     */
    public abstract CommandConnection factoryCommandConnection(String hostname, int port, Credentials credentials);
}
