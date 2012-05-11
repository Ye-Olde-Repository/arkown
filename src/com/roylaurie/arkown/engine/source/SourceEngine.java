/**
 * 
 */
package com.roylaurie.arkown.engine.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.engine.EngineType;
import com.roylaurie.arkown.engine.Connection.CommandConnection;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.arkown.engine.Connection.QueryConnection;

/**
 * @author rlaurie
 *
 */
public final class SourceEngine extends Engine {
    private static final String NAME = "Source";
    private static final EngineType TYPE = EngineType.SOURCE;
    private static final Capability[] sCapabilities = new Capability[] {
        Capability.QUERY,
        Capability.COMMAND,
        Capability.QUERY_VIA_COMMAND
    };
    
    private static SourceEngine sInstance = null;
    
    public static enum SourceProduct implements Product {
        COUNTER_STRIKE_SOURCE,
        DAY_OF_DEFEAT_SOURCE,
        HALF_LIFE_2_DEATHMATCH,
        HALF_LIFE_DEATHMATCH_SOURCE,
        LEFT_4_DEAD,
        LEFT_4_DEAD_2,
        TEAM_FORTRESS_2;
        
        private static final HashMap<Integer, SourceProduct> sApplicationIdMap = new HashMap<Integer, SourceProduct>();
        private static final HashMap<SourceProduct, String> sNameMap = new HashMap<SourceProduct, String>();
        
        static {
            // application id map. one-to-many. 
            // http://developer.valvesoftware.com/wiki/Steam_Application_IDs#Source_Engine_Games      
            sApplicationIdMap.put(240, SourceProduct.COUNTER_STRIKE_SOURCE);
            sApplicationIdMap.put(300, SourceProduct.DAY_OF_DEFEAT_SOURCE);
            sApplicationIdMap.put(320, SourceProduct.HALF_LIFE_2_DEATHMATCH);
            sApplicationIdMap.put(360, SourceProduct.HALF_LIFE_DEATHMATCH_SOURCE);
            sApplicationIdMap.put(440, SourceProduct.TEAM_FORTRESS_2);
            sApplicationIdMap.put(500, SourceProduct.LEFT_4_DEAD);
            sApplicationIdMap.put(550, SourceProduct.LEFT_4_DEAD_2);
            
            // name map. one-to-one.
            // http://developer.valvesoftware.com/wiki/Steam_Application_IDs#Source_Engine_Games 
            sNameMap.put(SourceProduct.COUNTER_STRIKE_SOURCE, "Counter-Strike: Source");
            sNameMap.put(SourceProduct.DAY_OF_DEFEAT_SOURCE, "Day of Defeat: Source");
            sNameMap.put(SourceProduct.HALF_LIFE_2_DEATHMATCH, "Half-Life 2: Deathmatch");
            sNameMap.put(SourceProduct.HALF_LIFE_DEATHMATCH_SOURCE, "Half-Life Deathmatch: Source");
            sNameMap.put(SourceProduct.TEAM_FORTRESS_2, "Team Fortress 2");
            sNameMap.put(SourceProduct.LEFT_4_DEAD, "Left 4 Dead");
            sNameMap.put(SourceProduct.LEFT_4_DEAD_2, "Left 4 Dead 2");            
        }          
        
        public String getName() {
            return sNameMap.get(this);
        }        
        
        public int getApplicationId() {
            for (Entry<Integer, SourceProduct> entry : sApplicationIdMap.entrySet()) {
                if (entry.getValue() == this) {
                    return entry.getKey();
                }
            }    
            
            throw new IllegalStateException("Product type does not exist for `" + this + ".");
        }
    }
    
    private SourceEngine() {
        super(NAME, TYPE, sCapabilities);
    }

    public static final SourceEngine getInstance() {
        if (sInstance == null) {
            sInstance = new SourceEngine();
        }
        
        return sInstance;
    }
    
    @Override
    public CommandConnection factoryCommandConnection(String hostname, int port, Credentials credentials) {
        //TODO: return new SourceCommandConnection(hostname, port);
        return null;
    }

    @Override
    public QueryConnection factoryQueryConnection(String hostname, int port) {
        return new SourceQueryConnection(hostname, port);
    }

    /**
     * Retrieves the product enum class used by this engine.
     * Typically used in conjuction with the generic Enum class.
     * 
     * @return Class<? extends Enum>
     */
    @Override
    public Class<? extends Enum<? extends SourceProduct>> getProductClass() {
        return SourceProduct.class;
    }
    
    /**
     * Retrieves an array of available products.
     *
     * @return  Enum<? extends Product>[]
     */
    public Enum<? extends SourceProduct>[] getProducts() {
        return SourceProduct.values();
    }
    
    /**
     * Factories a product from it's name.
     *
     * @param String name
     * @return Enum<? extends SourceProduct>
     */
    @Override
    public Enum<? extends SourceProduct> parseProductFromName(String name) {
        for (Map.Entry<SourceProduct, String> entry : SourceProduct.sNameMap.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        
        throw new IndexOutOfBoundsException("Unknown SourceProduct `" + name + "`.");
    }
    
    /**
     * Factories a product by it's Source application id.
     *
     * @param int applicationId
     * @return Enum<? extends SourceProduct>
     */
    public Enum<? extends SourceProduct> parseProductFromApplicationId(int applicationId) {
        return SourceProduct.sApplicationIdMap.get(applicationId);
    }    
    
    @Override
    public int getDefaultQueryPort() {
        return 27015;
    }        
}
