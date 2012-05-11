/**
 * 
 */
package com.roylaurie.arkown.command;

import java.util.ArrayList;
import java.util.HashMap;

import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.engine.Engine.Product;

/**
 * @author rlaurie
 *
 */
public final class Category implements Comparable<Category> {
    public static final String MAP_NAME = "name";
    public static final String MAP_ENGINE = "engine";
    public static final String MAP_ENGINE_PRODUCT = "engine_product";
    public static final String MAP_PRODUCT = "product";
    public static final String MAP_DATABASE_ID = "local_id";
    public static final String MAP_APPLICATION_DATABASE_ID = "id";
    
    private String mName = null;
    private long mApplicationDatabaseId = 0;
    private long mDatabaseId = 0;
    private ArrayList<Command> mCommandList = new ArrayList<Command>();
    private Engine mEngine = null;
    private Enum<? extends Product> mProduct =  null;
    
    public Category() { }
    
    public Category(String name) {
        setName(name);
    }
    
    @Override
    public boolean equals(Object rh) {
        if (rh == null || rh.getClass() != getClass()) {
            return false;
        }
        
        return ( hashCode() == rh.hashCode() );
    }
    
    @Override
    public int hashCode() {
        int code = 1;
        code = code * 7 + ( mName == null ? 0 : mName.hashCode() );
        code = code * 7 + ( mEngine == null ? 0 : mEngine.hashCode() );
        code = code * 7 + ( mProduct == null ? 0 : mProduct.hashCode() );
        
        return code;      
    }    

    @Override
    public int compareTo(Category rh) {
        int eq = getName().compareToIgnoreCase(rh.getName());
        if (eq != 0) {
            return eq;
        }
        
        eq = getEngine().compareTo(rh.getEngine());
        if (eq != 0 || mProduct == null) {
            return eq;
        } else if (!rh.hasProductFilter()) {
            return -1;
        }
        
        return ( ((Product)mProduct).getName().compareToIgnoreCase(((Product)rh.getProduct()).getName()) );
    }      
    
    /**
     * @return the name
     */
    public String getName() {
        return mName;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        mName = name;
    }
    
    /**
     * @return the databaseId
     */
    public long getApplicationDatabaseId() {
        return mApplicationDatabaseId;
    }
    
    /**
     * @param databaseId the databaseId to set
     */
    public void setApplicationDatabaseId(long databaseId) {
        mApplicationDatabaseId = databaseId;
    }
    
    /**
     * @return the databaseId
     */
    public long getDatabaseId() {
        return mDatabaseId;
    }
    
    /**
     * @param databaseId the databaseId to set
     */
    public void setDatabaseId(long databaseId) {
        mDatabaseId = databaseId;
    }
    
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        
        map.put(MAP_NAME, getName());
        map.put(MAP_APPLICATION_DATABASE_ID, Long.toString(getApplicationDatabaseId()));
        map.put(MAP_DATABASE_ID, Long.toString(getDatabaseId()));
        map.put(MAP_ENGINE, getEngine().getName());
        
        if (hasProductFilter()) {
            map.put(MAP_PRODUCT, ((Product)getProduct()).getName());
            map.put(MAP_ENGINE_PRODUCT, ((Product)getProduct()).getName());
        } else {
            map.put(MAP_PRODUCT, "");
            map.put(MAP_ENGINE_PRODUCT, getEngine().getName() + " Engine");
        }
        
        return map;
    }

    /**
     * @return the commandList
     */
    public ArrayList<Command> getCommands() {
        return mCommandList;
    }

    /**
     * @param commandList the commandList to set
     */
    public void setCommands(ArrayList<Command> commandList) {
        mCommandList = commandList;
        
        for (Command command : mCommandList) {
            command.setCategory(this);
        }
    }
    
    public void addCommand(Command command) {
        command.setCategory(this);
        mCommandList.add(command);
    }
    
    public void removeCommand(Command command) {
        mCommandList.remove(command);
        command.setCategory(null);
    }

    /**
     * @return the engine
     */
    public Engine getEngine() {
        return mEngine;
    }

    /**
     * @param engine the engine to set
     */
    public void setEngine(Engine engine) {
        mEngine = engine;
    }
    
    /**
     * Determines whether this category is filtered by product or just engine.
     *
     * @return boolean TRUE if filetered by product, FALSE if not.
     */
    public boolean hasProductFilter() {
        return ( mProduct != null );
    }
    
    /**
     * @return the engine
     */
    public Enum<? extends Product> getProduct() {
        return mProduct;
    }

    /**
     * @param engine the engine to set
     */
    public void setProduct(Enum<? extends Product> engineProduct) {
        mProduct = engineProduct;
    }    
}
