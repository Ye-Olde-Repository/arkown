/**
 * Copyright (C) 2011 Roy Laurie Software <http://www.roylaurie.com>
 */
package com.roylaurie.arkown.json;

import com.roylaurie.arkown.server.Server;
import com.roylaurie.modeljson.JsonEnvelope;
import com.roylaurie.modeljson.JsonFactory.ExclusionStrategyType;
import com.roylaurie.modeljson.exception.JsonException;

/**
 * @author Roy Laurie <roy.laurie@roylaurie.com> RAL
 *
 */
public final class ServerUpdateRequestJsonEnvelope extends JsonEnvelope {
    public static final String PROTOTYPE_SERVER = "server";
    
    public ServerUpdateRequestJsonEnvelope() {
        super(ArkownJsonFactory.getInstance());

        addPrototype(new Prototype(
            PROTOTYPE_SERVER,
            ExclusionStrategyType.PRIVATE,
            Server.class
        ));
        
        putObject(PROTOTYPE_SERVER, null);        
    }
    
    public ServerUpdateRequestJsonEnvelope(String json) throws JsonException {
        super(ArkownJsonFactory.getInstance());
        
        addPrototype(new Prototype(
            PROTOTYPE_SERVER,
            ExclusionStrategyType.PRIVATE,
            Server.class
        ));
        
        fromJson(json);
    }
    
    /**
     * Retrieves the immutable server list.
     *
     * @return Server
     */
    public Server getServer() {
        return (Server)getObject(PROTOTYPE_SERVER);
    }
    
    public void setServer(Server server) {
        putObject(PROTOTYPE_SERVER, server);
        setNeedsJsonRefresh();
    } 
}
