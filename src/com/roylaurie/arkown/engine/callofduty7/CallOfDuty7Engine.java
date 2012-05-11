/**
 * 
 */
package com.roylaurie.arkown.engine.callofduty7;

import java.util.HashMap;
import java.util.Map;

import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.engine.EngineType;
import com.roylaurie.arkown.engine.Connection.CommandConnection;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.arkown.engine.Connection.QueryConnection;

/**
 * @author rlaurie
 *
 */
public final class CallOfDuty7Engine extends Engine {
    private static final String NAME = "Call of Duty 7";
    private static final EngineType TYPE = EngineType.CALL_OF_DUTY_7;
    private static final Capability[] sCapabilities = new Capability[] {
        Capability.COMMAND,
        Capability.QUERY_VIA_COMMAND
    };
    
    private static CallOfDuty7Engine sInstance = null;
    
    public static enum CallOfDuty7Product implements Product {
            BLACK_OPS;
            
        private static final HashMap<CallOfDuty7Product, String> sNameMap = new HashMap<CallOfDuty7Product, String>();
        
        static {
            // name map. one-to-one.
            sNameMap.put(CallOfDuty7Product.BLACK_OPS, "Call of Duty: Black Ops");         
        }        
        
        public String getName() {
            return sNameMap.get(this);
        }
    };
        
    private CallOfDuty7Engine() {
        super(NAME, TYPE, sCapabilities);
    }

    public static final CallOfDuty7Engine getInstance() {
        if (sInstance == null) {
            sInstance = new CallOfDuty7Engine();
        }
        
        return sInstance;
    }    
    
    @Override
    public Class<? extends Enum<? extends CallOfDuty7Product>> getProductClass() {
        return CallOfDuty7Product.class;
    }
    
    @Override
    public Enum<? extends CallOfDuty7Product>[] getProducts() {
        return CallOfDuty7Product.values();
    }

    @Override
    public Enum<? extends CallOfDuty7Product> parseProductFromName(String name) {
        for (Map.Entry<CallOfDuty7Product, String> entry : CallOfDuty7Product.sNameMap.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        
        throw new IndexOutOfBoundsException("Unknown SourceProduct `" + name + "`.");
    }    

    @Override
    public int getDefaultQueryPort() {
        return 3084;
    }       
    
    @Override
    public CommandConnection factoryCommandConnection(String hostname, int port, Credentials credentials) {
        return new CallOfDuty7CommandConnection(hostname, port, credentials);
    }

    @Override
    public QueryConnection factoryQueryConnection(String hostname, int port) {
        //TODO: return new CallOfDuty7QueryConnection(hostname, port);
        return null;
    }
    
    public QueryConnection factoryProxyQueryConnection(String hostname, int port) {
        //TODO: return new CallOfDuty7QueryConnection(hostname, port);
        return null;
    }
}
