/**
 * 
 */
package com.roylaurie.arkown.engine;

import com.roylaurie.arkown.engine.Connection.ConnectionException;
import com.roylaurie.arkown.engine.Connection.Transmission;

/**
 * @author rlaurie
 *
 */
public abstract class Query {
    private Enum<?> mResource = null;
    private Transmission mTransmission = null;
    
    @SuppressWarnings("serial")
    public static final class QueryException extends Exception {
        public QueryException(String message) {
            super(message);
        }
        
        public QueryException(Throwable cause) {
            super(cause);
        }          
    }

    public static abstract class QueryResult {}
    
    protected Query(Enum<?> resource, Transmission transmission) {
        setResource(resource);
        setTransmission(transmission);
    }
    
    /**
     * @return the resource
     */
    public final Enum<?> getResource() {
        return mResource;
    }
    
    /**
     * @param resource the resource to set
     */
    public void setResource(Enum<?> resource) {
        mResource = resource;
    }    
    
    public abstract QueryResult query() throws QueryException, ConnectionException;

    private final Transmission setTransmission(Transmission transmission) {
        return mTransmission = transmission;
    }
    
    /**
     * @return the connection
     */
    protected final Transmission getTransmission() {
        return mTransmission;
    }
    
    protected final Connection getConnection() {
        return getTransmission().getConnection();
    }
}
