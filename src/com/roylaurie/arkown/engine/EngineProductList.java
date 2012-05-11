/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.roylaurie.arkown.engine.Engine.Product;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class EngineProductList {
    private ArrayList<EngineProductPair> mEngineProductList = new ArrayList<EngineProductPair>();
    private ArrayList<EngineProductPair> mProductList = new ArrayList<EngineProductPair>();
    
    private static EngineProductList sInstance = null;
    
    public static final class EngineProductPair implements Comparable<EngineProductPair> {
        private Engine mEngine = null;
        private Enum<? extends Product> mProduct = null;
        
        public EngineProductPair(Engine engine, Enum<? extends Product> product) {
            mEngine = engine;
            mProduct = product;
        }
        
        public EngineProductPair(Engine engine) {
            mEngine = engine;
        }        
        
        @Override
        public int hashCode() {
            return toString().hashCode();
        }
        
        @Override
        public boolean equals(Object rh) {
            if (rh == null || !(rh instanceof EngineProductPair)) {
                return false;
            }
            
            return ( hashCode() == rh.hashCode() );
        }

        @Override
        public int compareTo(EngineProductPair rh) {
            if (rh == null) {
                return 1;
            }
            
            return ( toString().compareTo(rh.toString()) );
        }        
        
        @Override
        public String toString() {
            if (getProduct() != null) {
                return ((Product)getProduct()).getName();
            }
            
            return ( getEngine().getName() + " Engine" );
        }
        
        /**
         * Determines whether this pair indicates a product or engine.
         *
         * @return boolean TRUE if product, FALSE if engine.
         */
        public boolean isProduct() {
            return ( getProduct() != null );
        }
        
        /**
         * Retrieves the engine.
         *
         * @return Engine
         */
        public Engine getEngine() {
            return mEngine;
        }
        
        /**
         * Retrieves the product.
         *
         * @return Enum<? extends Product>
         */
        public Enum<? extends Product> getProduct() {
            return mProduct;
        }
    }
    
    private EngineProductList() {
        for (EngineType engineType : EngineType.values()) {
            Engine engine = engineType.getEngine();
            
            mEngineProductList.add(new EngineProductPair(engine));
            
            for (Enum<? extends Product> product : engine.getProducts()) {
                EngineProductPair productPair = new EngineProductPair(engine, product);
                
                mEngineProductList.add(productPair);
                mProductList.add(productPair);
            }
        }

        Collections.sort(mProductList);
    }
    
    public static EngineProductList getInstance() {
        if (sInstance == null) {
            sInstance = new EngineProductList();
        }
        
        return sInstance;
    }

    public List<EngineProductPair> getProductList() {
        return Collections.unmodifiableList(mProductList);
    }
    
    public List<EngineProductPair> getEngineProductList() {
        return Collections.unmodifiableList(mEngineProductList);
    }    
}
