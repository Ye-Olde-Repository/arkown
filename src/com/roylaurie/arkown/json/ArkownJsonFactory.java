/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.json;

import com.google.gson.GsonBuilder;
import com.roylaurie.arkown.engine.Engine;
import com.roylaurie.arkown.engine.Connection.Credentials;
import com.roylaurie.arkown.engine.callofduty7.CallOfDuty7Engine;
import com.roylaurie.arkown.engine.json.CredentialsJsonAdapter;
import com.roylaurie.arkown.engine.json.EngineJsonAdapter;
import com.roylaurie.arkown.engine.source.SourceEngine;
import com.roylaurie.arkown.server.PlayerClient;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.arkown.server.callofduty7.CallOfDuty7Server;
import com.roylaurie.arkown.server.json.PlayerClientJsonAdapter;
import com.roylaurie.arkown.server.json.ServerJsonAdapter;
import com.roylaurie.arkown.server.source.SourceServer;
import com.roylaurie.modeljson.JsonAdapter;
import com.roylaurie.modeljson.JsonFactory;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class ArkownJsonFactory extends JsonFactory {
    public static final int API_VERSION = 1;
    private static ArkownJsonFactory sInstance = null;
    
    private ArkownJsonFactory() {
        super(API_VERSION);
    }
    
    public static ArkownJsonFactory getInstance() {
        if (sInstance == null) {
            sInstance = new ArkownJsonFactory();
        }

        return sInstance;
    }
    
    @Override
    protected void configureGsonBuilder(GsonBuilder builder) {
        JsonAdapter<?> adapter = null;
        
        builder.registerTypeAdapter(Credentials.class, new CredentialsJsonAdapter(this));
        
        // engines
        adapter = new EngineJsonAdapter(this);
        builder.registerTypeAdapter(SourceEngine.class, adapter);
        builder.registerTypeAdapter(CallOfDuty7Engine.class, adapter);
        builder.registerTypeAdapter(Engine.class, adapter);
        
        // servers        
        adapter = new ServerJsonAdapter(this);
        builder.registerTypeAdapter(SourceServer.class, adapter);
        builder.registerTypeAdapter(CallOfDuty7Server.class, adapter);
        builder.registerTypeAdapter(Server.class, adapter);
        
        // clients
        builder.registerTypeAdapter(PlayerClient.class, new PlayerClientJsonAdapter(this));
    }
}
