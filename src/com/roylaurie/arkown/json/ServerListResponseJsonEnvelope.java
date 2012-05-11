/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.roylaurie.arkown.server.Server;
import com.roylaurie.modeljson.JsonEnvelope;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;
import com.roylaurie.modeljson.exception.JsonException;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class ServerListResponseJsonEnvelope extends JsonEnvelope {
    public static final String PROTOTYPE_SERVERS = "servers";
    
    public ServerListResponseJsonEnvelope(ExclusionStrategyType serverListStrategyType) {
        super(ArkownJsonFactory.getInstance());

        addPrototype(new Prototype(
            PROTOTYPE_SERVERS,
            serverListStrategyType,
            new TypeToken<List<Server>>(){}.getType()
        ));
        
        putObject(PROTOTYPE_SERVERS, new ArrayList<Server>());        
    }
    
    public ServerListResponseJsonEnvelope(String json, ExclusionStrategyType serverListStrategyType) throws JsonException {
        super(ArkownJsonFactory.getInstance());
        
        addPrototype(new Prototype(
            PROTOTYPE_SERVERS,
            serverListStrategyType,
            new TypeToken<List<Server>>(){}.getType()
        ));
        
        fromJson(json);
    }
    
    /**
     * Retrieves the immutable server list.
     *
     * @return ArrayList<Server>
     */
    @SuppressWarnings("unchecked")
    public List<Server> getServerList() {
        return Collections.unmodifiableList(((List<Server>)getObject(PROTOTYPE_SERVERS)));
    }
    
    @SuppressWarnings("unchecked")
    public void addServer(Server server) {
        ((List<Server>)getObject(PROTOTYPE_SERVERS)).add(server);
        setNeedsJsonRefresh();
    }
   
    @SuppressWarnings("unchecked")
    public void removeServer(Server server) {
        ((List<Server>)getObject(PROTOTYPE_SERVERS)).remove(server);
        setNeedsJsonRefresh();
    }    
}
